package com.github.isaichkindanila.ioc;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class TopologicalSortTest {
    private List<BeanInfo> beanInfoList;

    @Before
    public void init() {
        beanInfoList = List.of(
                new BeanInfo(String.class, new Class[]{Double.class, Thread.class}),
                new BeanInfo(Thread.class, new Class[]{}),
                new BeanInfo(Double.class, new Class[]{Thread.class})
        );
    }

    @Test
    public void testTopologicalSort() {
        var sorted = TopologicalSort.sorted(beanInfoList)
                .stream()
                .map(BeanInfo::getBeanClass)
                .toArray(Class[]::new);

        var expected = new Class[]{Thread.class, Double.class, String.class};

        Assert.assertArrayEquals(expected, sorted);
    }
}
