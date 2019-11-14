package com.github.isaichkindanila.ioc.components;

import com.github.isaichkindanila.ioc.Component;
import com.github.isaichkindanila.ioc.interfaces.GreetingInterface;

@Component
public class ComplexComponent {
    private final GreetingInterface greetingInterface;

    public ComplexComponent(GreetingInterface greetingInterface) {
        this.greetingInterface = greetingInterface;
    }

    public String getPersonalizedGreeting(String name) {
        return greetingInterface.getGreeting() + ", " + name;
    }
}
