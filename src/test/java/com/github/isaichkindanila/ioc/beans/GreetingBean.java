package com.github.isaichkindanila.ioc.beans;

import com.github.isaichkindanila.ioc.annotation.Bean;
import com.github.isaichkindanila.ioc.interfaces.GreetingInterface;

@Bean
public class GreetingBean implements GreetingInterface {
    private final String greeting;

    public GreetingBean() {
        this("hello");
    }

    @SuppressWarnings("WeakerAccess")
    public GreetingBean(String greeting) {
        this.greeting = greeting;
    }

    @Override
    public String getGreeting() {
        return greeting;
    }
}
