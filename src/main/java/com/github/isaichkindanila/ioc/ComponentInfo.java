package com.github.isaichkindanila.ioc;

class ComponentInfo {
    private final Class componentClass;
    private final Class[] argClasses;

    ComponentInfo(Class componentClass, Class[] argClasses) {
        this.componentClass = componentClass;
        this.argClasses = argClasses;
    }

    Class getComponentClass() {
        return componentClass;
    }

    Class[] getArgClasses() {
        return argClasses;
    }
}
