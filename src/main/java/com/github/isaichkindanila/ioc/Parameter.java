package com.github.isaichkindanila.ioc;

class Parameter {
    private final Class clazz;
    private final String name;

    Parameter(Class clazz, String name) {
        this.clazz = clazz;
        this.name = name;
    }

    Class getClazz() {
        return clazz;
    }

    String getName() {
        return name;
    }
}
