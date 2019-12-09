package com.github.isaichkindanila.ioc;

import com.github.isaichkindanila.ioc.annotation.Component;

class ComponentFactory {

    private ComponentFactory() {
    }

    @SuppressWarnings("unchecked")
    private static ComponentWrapper createComponent0(ReflectionContext container, ComponentInfo info) throws Exception {
        var componentClass = info.getComponentClass();

        var constructor = componentClass.getConstructor(info.getArgClasses());
        var parameters = info.getParameters().stream()
                .map(container::getComponent)
                .toArray();

        constructor.setAccessible(true);

        var component = constructor.newInstance(parameters);
        var annotation = (Component) componentClass.getAnnotation(Component.class);

        return new ComponentWrapper(component, annotation.name());
    }

    static ComponentWrapper createComponent(ReflectionContext container, ComponentInfo info) {
        try {
            return createComponent0(container, info);
        } catch (Exception e) {
            var className = info.getComponentClass().getName();
            var message = String.format("failed to create component %s", className);

            throw new ComponentException(message, e);
        }
    }
}
