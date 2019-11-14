package com.github.isaichkindanila.ioc.components;

import com.github.isaichkindanila.ioc.annotation.Component;
import com.github.isaichkindanila.ioc.interfaces.ConflictingInterface;

public class ConflictingComponent2 implements ConflictingInterface {

    @Component
    public ConflictingComponent2() {
    }

    public ConflictingComponent2(String parameter) {
    }
}
