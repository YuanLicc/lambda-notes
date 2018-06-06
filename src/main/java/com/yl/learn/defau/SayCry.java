package com.yl.learn.defau;

public interface SayCry extends Say, Cry {
    @Override
    default void say() {
        Say.super.say();
        Cry.super.say();
    }
}
