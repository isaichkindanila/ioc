package com.github.isaichkindanila.ioc.components;

import com.github.isaichkindanila.ioc.annotation.Component;
import com.github.isaichkindanila.ioc.interfaces.ConflictingInterface;

@Component(name = "2")
class ConflictingComponent2 implements ConflictingInterface {
    public ConflictingComponent2() {
    }
}
