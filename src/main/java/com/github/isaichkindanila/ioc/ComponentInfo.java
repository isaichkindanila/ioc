package com.github.isaichkindanila.ioc;

import java.util.List;

class ComponentInfo {
    private final Class componentClass;
    private final List<Parameter> parameters;
    private final Class[] argClasses;

    ComponentInfo(Class componentClass, List<Parameter> parameters) {
        this.componentClass = componentClass;
        this.parameters = parameters;

        argClasses = parameters.stream()
                .map(Parameter::getClazz)
                .toArray(Class[]::new);
    }

    Class getComponentClass() {
        return componentClass;
    }

    List<Parameter> getParameters() {
        return parameters;
    }

    Class[] getArgClasses() {
        return argClasses;
    }
}
