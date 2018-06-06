package com.yl.learn.fina;

import com.sun.istack.internal.NotNull;

import java.io.Serializable;
import java.util.Objects;

public class Person implements Serializable {
    private String name;

    public Person() {
        this.name = "aaa";
    }

    public Person(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
