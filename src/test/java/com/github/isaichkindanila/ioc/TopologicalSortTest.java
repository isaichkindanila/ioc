package com.github.isaichkindanila.ioc;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TopologicalSortTest {
    private List<ComponentInfo> componentInfoList;

    private ComponentInfo info(Class clazz, Class... parameters) {
        var params = Arrays.stream(parameters)
                .map(c -> new Parameter(c, ""))
                .collect(Collectors.toList());

        return new ComponentInfo(clazz, params);
    }

    @Before
    public void init() {
        componentInfoList = List.of(
                info(String.class, Double.class, Thread.class),
                info(Thread.class),
                info(Double.class, Thread.class)
        );
    }

    @Test
    public void testTopologicalSort() {
        var sorted = TopologicalSort.sorted(componentInfoList)
                .stream()
                .map(ComponentInfo::getComponentClass)
                .toArray(Class[]::new);

        var expected = new Class[]{Thread.class, Double.class, String.class};

        Assert.assertArrayEquals(expected, sorted);
    }
}
