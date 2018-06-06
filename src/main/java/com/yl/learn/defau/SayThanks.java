package com.yl.learn.defau;

public interface SayThanks extends Say {
    @Override
    default void say() {
        Say.super.say();
    }
}
