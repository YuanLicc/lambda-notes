package com.yl.learn.defau;

public interface Say {

    default void say() {
        System.out.println("say something");
    }

    void sayLoudly();
}
