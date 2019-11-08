package com.github.isaichkindanila.ioc.beans;

import com.github.isaichkindanila.ioc.annotation.Bean;
import com.github.isaichkindanila.ioc.interfaces.GreetingInterface;

@Bean
public class CompositeBean {
    private final GreetingInterface greetingInterface;

    public CompositeBean(GreetingInterface greetingInterface) {
        this.greetingInterface = greetingInterface;
    }

    public String getPersonalizedGreeting(String name) {
        return greetingInterface.getGreeting() + ", " + name;
    }
}
