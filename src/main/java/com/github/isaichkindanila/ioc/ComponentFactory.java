package com.github.isaichkindanila.ioc;

import com.github.isaichkindanila.ioc.annotation.Component;

import java.util.Arrays;

class ComponentFactory {

    private static Object[] getParameters(ComponentContainer container, Class[] argClasses) {
        return Arrays.stream(argClasses)
                .map(container::getComponent)
                .toArray();
    }

    @SuppressWarnings("unchecked")
    private static Wrapper createComponent0(ComponentContainer container, ComponentInfo info) throws Exception {
        var componentClass = info.getComponentClass();
        var argClasses = info.getArgClasses();

        var constructor = componentClass.getConstructor(argClasses);
        var parameters = getParameters(container, argClasses);

        constructor.setAccessible(true);

        var component = constructor.newInstance(parameters);
        var annotation = (Component) componentClass.getAnnotation(Component.class);

        return new Wrapper(component, annotation.name());
    }

    static Wrapper createComponent(ComponentContainer container, ComponentInfo info) {
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
