package com.github.isaichkindanila.ioc;

import com.github.isaichkindanila.ioc.beans.CompositeBean;
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

    @Test
    public void getCompositeBean() {
        var bean = container.getBean(CompositeBean.class);
        var greeting = bean.getPersonalizedGreeting("test");

        Assert.assertEquals("hello, test", greeting);
    }

    @Test
    public void getMultipleBeans() {
        var beans = container.getBeans(ConflictingInterface.class);
        Assert.assertEquals(2, beans.size());
    }

    @Test
    public void addBean() {
        try {
            container.getBean(Throwable.class);
            Assert.fail("BeanException expected");
        } catch (BeanException ignore) {}

        var bean = new RuntimeException();
        container = BeanContainer.newInstance(bean);

        Assert.assertSame(bean, container.getBean(Throwable.class));
    }
}
