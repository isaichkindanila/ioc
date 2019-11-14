package com.github.isaichkindanila.ioc;

import com.github.isaichkindanila.ioc.components.ComplexComponent;
import com.github.isaichkindanila.ioc.interfaces.ConflictingInterface;
import com.github.isaichkindanila.ioc.interfaces.GreetingInterface;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ComponentContainerTest {
    private static final String PACKAGE = "com.github.isaichkindanila.ioc.components";
    private ComponentContainer container;

    @Before
    public void init() {
        container = ComponentContainer.newInstance(PACKAGE, "hello");
    }

    @Test(expected = ComponentException.class)
    public void beanNotFound() {
        container.getComponent(ComponentContainerTest.class);
    }

    @Test(expected = ComponentException.class)
    public void multipleComponentsFound() {
        container.getComponent(ConflictingInterface.class);
    }

    @Test
    public void getSimpleComponent() {
        var bean = container.getComponent(GreetingInterface.class);
        Assert.assertEquals("hello", bean.getGreeting());
    }

    @Test
    public void getComplexComponent() {
        var bean = container.getComponent(ComplexComponent.class);
        var greeting = bean.getPersonalizedGreeting("test");

        Assert.assertEquals("hello, test", greeting);
    }

    @Test
    public void getMultipleComponents() {
        var beans = container.getComponents(ConflictingInterface.class);
        Assert.assertEquals(2, beans.size());
    }

    @Test
    public void addComponent() {
        try {
            container.getComponent(Throwable.class);
            Assert.fail("BeanException expected");
        } catch (ComponentException ignore) {}

        var component = new RuntimeException();
        container = ComponentContainer.newInstance(PACKAGE, "", component);

        Assert.assertSame(component, container.getComponent(Throwable.class));
    }
}
