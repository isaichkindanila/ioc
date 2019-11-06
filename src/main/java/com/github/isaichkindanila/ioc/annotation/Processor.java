package com.github.isaichkindanila.ioc.annotation;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("com.github.isaichkindanila.ioc.annotation.Bean")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class Processor extends AbstractProcessor {

    private final JSONArray beans = new JSONArray();

    private ExecutableElement getConstructorOf(Element element) {
        var enclosed = element.getEnclosedElements();
        var constructors = ElementFilter.constructorsIn(enclosed)
                .stream()
                .filter(e -> e.getModifiers().contains(Modifier.PUBLIC))
                .collect(Collectors.toList());

        if (constructors.size() == 0) {
            throw new ProcessingException("public constructor not found", element);
        }

        if (constructors.size() == 1) {
            return constructors.get(0);
        }

        return constructors.stream()
                .filter(c -> c.getParameters().size() == 0)
                .findAny()
                .orElseThrow(() -> new ProcessingException("default constructor not found", element));
    }

    private void assertClassIsValid(Element element) {
        if (!element.getKind().isClass()) {
            throw new ProcessingException("must be a class", element);
        }

        if (element.getModifiers().contains(Modifier.ABSTRACT)) {
            throw new ProcessingException("must be non abstract class", element);
        }
    }

    private void assertConstructorIsValid(Element element) {
        if (!element.getModifiers().contains(Modifier.PUBLIC)) {
            throw new ProcessingException("must be public", element);
        }
    }

    private String getClassName(Element element) {
        return element.asType().toString();
    }

    private Collection<String> getArgClasses(ExecutableElement constructor) {
        return constructor.getParameters()
                .stream()
                .map(Element::asType)
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    private void addBean(Element classElement, ExecutableElement constructor) {
        var className = getClassName(classElement);
        var args = getArgClasses(constructor);

        var bean = new JSONObject();
        bean.put("class", className);
        bean.put("args", args);

        beans.put(bean);
    }

    private void process(Element element) {
        Element classElement;
        ExecutableElement constructor;

        if (element.getKind().isClass()) {
            classElement = element;
            constructor = getConstructorOf(element);
        } else {
            classElement = element.getEnclosingElement();
            constructor = (ExecutableElement) element;
        }

        assertClassIsValid(classElement);
        assertConstructorIsValid(constructor);
        addBean(classElement, constructor);
    }

    private void finish() throws IOException {
        var out = processingEnv.getFiler().createResource(
                StandardLocation.CLASS_OUTPUT,
                "",
                "beans.json"
        ).openWriter();

        out.write(beans.toString(2));
        out.close();
    }

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        var elements = roundEnv.getElementsAnnotatedWith(Bean.class);

        for (var element : elements) {
            try {
                process(element);
            } catch (ProcessingException e) {
                processingEnv.getMessager().printMessage(Kind.ERROR, e.message, e.element);
            }
        }

        if (roundEnv.processingOver()) {
            try {
                finish();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        return false;
    }

    private static class ProcessingException extends RuntimeException {
        private final String message;
        private final Element element;

        ProcessingException(String message, Element element) {
            this.message = message;
            this.element = element;
        }
    }
}
