package com.github.isaichkindanila.ioc;

class BeanInfo {
    private final Class beanClass;
    private final Class[] argClasses;

    BeanInfo(Class beanClass, Class[] argClasses) {
        this.beanClass = beanClass;
        this.argClasses = argClasses;
    }

    Class getBeanClass() {
        return beanClass;
    }

    Class[] getArgClasses() {
        return argClasses;
    }
}
