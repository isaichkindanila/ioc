package com.github.isaichkindanila.ioc;

import java.util.Arrays;

class ComponentFactory {

    private static Object[] getParameters(ComponentContainer container, Class[] argClasses) {
        return Arrays.stream(argClasses)
                .map(container::getComponent)
                .toArray();
    }

    @SuppressWarnings("unchecked")
    private static Object createComponent0(ComponentContainer container, ComponentInfo info) throws Exception {
        var beanClass = info.getComponentClass();
        var argClasses = info.getArgClasses();

        var constructor = beanClass.getConstructor(argClasses);
        var parameters = getParameters(container, argClasses);

        constructor.setAccessible(true);
        return constructor.newInstance(parameters);
    }

    static Object createComponent(ComponentContainer container, ComponentInfo info) {
        try {
            return createComponent0(container, info);
        } catch (Exception e) {
            var className = info.getComponentClass().getName();
            var message = String.format("failed to create component %s", className);

            throw new ComponentException(message, e);
        }
    }

    private ComponentFactory() {}
}
