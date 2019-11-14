package com.github.isaichkindanila.ioc.components;

import com.github.isaichkindanila.ioc.annotation.Component;
import com.github.isaichkindanila.ioc.interfaces.ConflictingInterface;

@Component(name = "1")
public class ConflictingComponent1 implements ConflictingInterface {
}
