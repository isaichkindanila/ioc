package com.github.isaichkindanila.ioc.beans;

import com.github.isaichkindanila.ioc.annotation.Bean;

@Bean
public class CompositeBean {
    private final GreetingBean greetingBean;

    public CompositeBean(GreetingBean greetingBean) {
        this.greetingBean = greetingBean;
    }

    public String getPersonalizedGreeting(String name) {
        return greetingBean.getGreeting() + ", " + name;
    }
}
