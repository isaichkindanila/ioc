package com.github.isaichkindanila.ioc;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class TopologicalSortTest {
    private List<BeanInfo> beanInfoList;

    @Before
    public void init() {
        beanInfoList = List.of(
                new BeanInfo("3", new String[]{"1", "2"}),
                new BeanInfo("1", new String[]{}),
                new BeanInfo("2", new String[]{"1"})
        );
    }

    @Test
    public void testTopologicalSort() {
        var sorted = TopologicalSort.sorted(beanInfoList);
        var str = sorted.stream()
                .map(BeanInfo::getClassName)
                .collect(Collectors.joining());

        Assert.assertEquals("123", str);
    }
}
