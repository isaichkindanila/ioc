package com.github.isaichkindanila.ioc.components;

import com.github.isaichkindanila.ioc.annotation.Component;
import com.github.isaichkindanila.ioc.annotation.Named;
import com.github.isaichkindanila.ioc.interfaces.ConflictingInterface;
import com.github.isaichkindanila.ioc.interfaces.GreetingInterface;

@Component
public class ComplexComponent {
    private final GreetingInterface greetingInterface;

    @SuppressWarnings("unused")
    public ComplexComponent(GreetingInterface greetingInterface,
                            @Named("c2") ConflictingInterface c2) {
        this.greetingInterface = greetingInterface;
    }

    public String getPersonalizedGreeting(String name) {
        return greetingInterface.getGreeting() + ", " + name;
    }
}
