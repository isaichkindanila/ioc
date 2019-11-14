package com.github.isaichkindanila.ioc;

@SuppressWarnings("WeakerAccess")
public class Wrapper {
    private final Object component;
    private final String name;

    public Wrapper(Object component, String name) {
        this.component = component;
        this.name = name;
    }

    public Wrapper(Object component) {
        this(component, "");
    }

    Object getComponent() {
        return component;
    }

    String getName() {
        return name;
    }
}
