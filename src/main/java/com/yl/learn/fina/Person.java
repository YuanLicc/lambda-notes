package com.yl.learn.fina;

import java.io.Serializable;

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
