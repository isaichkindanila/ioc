package com.github.isaichkindanila.ioc;

import com.github.isaichkindanila.ioc.components.ComplexComponent;
import com.github.isaichkindanila.ioc.components.ConflictingComponent1;
import com.github.isaichkindanila.ioc.interfaces.ConflictingInterface;
import com.github.isaichkindanila.ioc.interfaces.GreetingInterface;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ReflectionContextTest {
    private static final String PACKAGE = "com.github.isaichkindanila.ioc.components";
    private Context context;

    @Before
    public void init() {
        context = ReflectionContext.newInstance(PACKAGE, "hello");
    }

    @Test(expected = ComponentException.class)
    public void componentNotFound() {
        context.getComponent(ReflectionContextTest.class);
    }

    @Test(expected = ComponentException.class)
    public void multipleComponentsFound() {
        context.getComponent(ConflictingInterface.class);
    }

    @Test
    public void getSimpleComponent() {
        var component = context.getComponent(GreetingInterface.class);
        Assert.assertEquals("hello", component.getGreeting());
    }

    @Test
    public void getComplexComponent() {
        var component = context.getComponent(ComplexComponent.class);
        var greeting = component.getPersonalizedGreeting("test");

        Assert.assertEquals("hello, test", greeting);
    }

    @Test
    public void getMultipleComponents() {
        var components = context.getComponents(ConflictingInterface.class);
        Assert.assertEquals(2, components.size());
    }

    @Test
    public void getNamedComponent() {
        var component = context.getComponent(ConflictingInterface.class, "c1");
        Assert.assertEquals(ConflictingComponent1.class, component.getClass());
    }

    @Test
    public void addComponent() {
        try {
            context.getComponent(Throwable.class);
            Assert.fail("ComponentException expected");
        } catch (ComponentException ignore) {}

        var component = new RuntimeException();
        context = ReflectionContext.newInstance(PACKAGE, "", component);

        Assert.assertSame(component, context.getComponent(Throwable.class));
    }
}
