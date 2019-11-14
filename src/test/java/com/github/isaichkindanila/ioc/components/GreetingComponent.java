package com.github.isaichkindanila.ioc.components;

import com.github.isaichkindanila.ioc.annotation.Component;
import com.github.isaichkindanila.ioc.interfaces.GreetingInterface;

@Component
class GreetingComponent implements GreetingInterface {
    private final String greeting;

    public GreetingComponent(String greeting) {
        this.greeting = greeting;
    }

    @Override
    public String getGreeting() {
        return greeting;
    }
}
