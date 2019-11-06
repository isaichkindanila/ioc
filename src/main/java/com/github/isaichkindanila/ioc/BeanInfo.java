package com.github.isaichkindanila.ioc;

class BeanInfo {
    private final String className;
    private final String[] args;

    BeanInfo(String className, String[] args) {
        this.className = className;
        this.args = args;
    }

    String getClassName() {
        return className;
    }

    String[] getArgs() {
        return args;
    }
}
