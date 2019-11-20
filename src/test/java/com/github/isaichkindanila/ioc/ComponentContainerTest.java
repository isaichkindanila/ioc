package com.github.isaichkindanila.ioc;

import com.github.isaichkindanila.ioc.components.ComplexComponent;
import com.github.isaichkindanila.ioc.components.ConflictingComponent1;
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
    public void componentNotFound() {
        container.getComponent(ComponentContainerTest.class);
    }

    @Test(expected = ComponentException.class)
    public void multipleComponentsFound() {
        container.getComponent(ConflictingInterface.class);
    }

    @Test
    public void getSimpleComponent() {
        var component = container.getComponent(GreetingInterface.class);
        Assert.assertEquals("hello", component.getGreeting());
    }

    @Test
    public void getComplexComponent() {
        var component = container.getComponent(ComplexComponent.class);
        var greeting = component.getPersonalizedGreeting("test");

        Assert.assertEquals("hello, test", greeting);
    }

    @Test
    public void getMultipleComponents() {
        var components = container.getComponents(ConflictingInterface.class);
        Assert.assertEquals(2, components.size());
    }

    @Test
    public void getNamedComponent() {
        var component = container.getComponent(ConflictingInterface.class, "c1");
        Assert.assertEquals(ConflictingComponent1.class, component.getClass());
    }

    @Test
    public void addComponent() {
        try {
            container.getComponent(Throwable.class);
            Assert.fail("ComponentException expected");
        } catch (ComponentException ignore) {
        }

        var component = new RuntimeException();
        container = ComponentContainer.newInstance(PACKAGE, "", component);

        Assert.assertSame(component, container.getComponent(Throwable.class));
    }
}
