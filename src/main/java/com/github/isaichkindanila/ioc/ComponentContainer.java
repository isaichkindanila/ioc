package com.github.isaichkindanila.ioc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class responsible for finding and creating components.
 */
@SuppressWarnings("WeakerAccess")
public class ComponentContainer {
    private final Collection<Object> container;

    public static ComponentContainer newInstance(String basePackage, Object... baseComponents) {
        var instance = new ComponentContainer(baseComponents);

        var componentsInfoList = ComponentParser.parseFrom(basePackage);
        var sortedInfoList = TopologicalSort.sorted(componentsInfoList);

        sortedInfoList.forEach(instance::addComponent);

        return instance;
    }

    private ComponentContainer(Object... components) {
        container = new ArrayList<>(Arrays.asList(components));
    }

    private void addComponent(ComponentInfo info) {
        container.add(ComponentFactory.createComponent(this, info));
    }

    public <T> List<T> getComponents(Class<? extends T> clazz) {
        return container.stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    public <T> T getComponent(Class<? extends T> clazz) {
        var candidates = getComponents(clazz);

        if (candidates.size() == 1) {
            // exactly 1 component found - everything is OK
            return candidates.get(0);
        }

        if (candidates.size() == 0) {
            // no components found - throw exception
            throw new ComponentException("no beans found for class " + clazz.getName());
        } else {
            // several components found - throw exception
            var arrayString = candidates.stream()
                    .map(bean -> bean.getClass().getName())
                    .collect(Collectors.joining(", "));

            var builder = new StringBuilder()
                    .append("multiple beans found for class ")
                    .append(clazz)
                    .append(": [")
                    .append(arrayString)
                    .append(']');

            throw new ComponentException(builder.toString());
        }
    }
}
