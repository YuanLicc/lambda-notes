package com.yl.learn.defau;

public interface Cry {
    default void say() {
        System.out.println("cry");
    }
}
