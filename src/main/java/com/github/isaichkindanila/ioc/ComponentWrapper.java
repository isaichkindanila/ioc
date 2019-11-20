package com.github.isaichkindanila.ioc;

@SuppressWarnings("WeakerAccess")
public class ComponentWrapper {
    private final Object component;
    private final String name;

    public ComponentWrapper(Object component, String name) {
        this.component = component;
        this.name = name;
    }

    public ComponentWrapper(Object component) {
        this(component, "");
    }

    Object getComponent() {
        return component;
    }

    String getName() {
        return name;
    }
}
