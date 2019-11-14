package com.github.isaichkindanila.ioc;

import io.github.classgraph.ClassGraph;

import java.util.List;
import java.util.stream.Collectors;

class ComponentParser {

    private static ComponentInfo parse(Class clazz) {
        var constructors = clazz.getConstructors();

        if (constructors.length != 1) {
            throw new ComponentException(
                    constructors.length + " public constructors found for " + clazz.getName()
            );
        }

        return new ComponentInfo(clazz, constructors[0].getParameterTypes());
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

    private ComponentParser() {}
}
