package com.github.isaichkindanila.ioc.beans;

import com.github.isaichkindanila.ioc.annotation.Bean;
import com.github.isaichkindanila.ioc.interfaces.ConflictingInterface;

public class ConflictingBean2 implements ConflictingInterface {

    @Bean
    public ConflictingBean2() {
    }

    public ConflictingBean2(String parameter) {
    }
}
