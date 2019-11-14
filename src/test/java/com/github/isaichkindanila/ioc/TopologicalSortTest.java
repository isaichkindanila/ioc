package com.github.isaichkindanila.ioc;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class TopologicalSortTest {
    private List<ComponentInfo> componentInfoList;

    @Before
    public void init() {
        componentInfoList = List.of(
                new ComponentInfo(String.class, new Class[]{Double.class, Thread.class}),
                new ComponentInfo(Thread.class, new Class[]{}),
                new ComponentInfo(Double.class, new Class[]{Thread.class})
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
