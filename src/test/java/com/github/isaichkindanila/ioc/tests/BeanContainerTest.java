package com.github.isaichkindanila.ioc.tests;

import com.github.isaichkindanila.ioc.BeanContainer;
import com.github.isaichkindanila.ioc.BeanException;
import com.github.isaichkindanila.ioc.interfaces.ConflictingInterface;
import com.github.isaichkindanila.ioc.interfaces.GreetingInterface;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BeanContainerTest {
    private BeanContainer container;

    @Before
    public void init() {
        container = BeanContainer.newInstance();
        container.readBeansFromClasspath();
    }

    @Test(expected = BeanException.class)
    public void beanNotFound() {
        container.getBean(BeanContainerTest.class);
    }

    @Test(expected = BeanException.class)
    public void multipleBeansFound() {
        container.getBean(ConflictingInterface.class);
    }

    @Test
    public void getSimpleBean() {
        var bean = container.getBean(GreetingInterface.class);
        Assert.assertEquals("hello", bean.getGreeting());
    }
}
