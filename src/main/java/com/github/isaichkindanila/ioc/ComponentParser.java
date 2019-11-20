package com.github.isaichkindanila.ioc;

import com.github.isaichkindanila.ioc.annotation.Component;
import com.github.isaichkindanila.ioc.annotation.Named;
import io.github.classgraph.ClassGraph;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class ComponentParser {

    private ComponentParser() {
    }

    private static Parameter parseParameter(java.lang.reflect.Parameter parameter) {
        var clazz = parameter.getType();
        var annotation = parameter.getAnnotation(Named.class);

        if (annotation != null) {
            return new Parameter(clazz, annotation.value());
        } else {
            return new Parameter(clazz, null);
        }
    }

    private static ComponentInfo parse(Class clazz) {
        var constructors = clazz.getConstructors();

        if (constructors.length != 1) {
            throw new ComponentException(
                    constructors.length + " public constructors found for " + clazz.getName()
            );
        }

        var parameters = Arrays.stream((constructors[0].getParameters()))
                .map(ComponentParser::parseParameter)
                .collect(Collectors.toList());

        return new ComponentInfo(clazz, parameters);
    }

    static List<ComponentInfo> parseFrom(String basePackage) {
        try (var scan = new ClassGraph()
                .whitelistPackages(basePackage)
                .ignoreClassVisibility()
                .enableAnnotationInfo()
                .scan()) {

            var components = scan.getClassesWithAnnotation(Component.class.getName());

            return components.loadClasses().stream()
                    .map(ComponentParser::parse)
                    .collect(Collectors.toList());
        }
    }
}
