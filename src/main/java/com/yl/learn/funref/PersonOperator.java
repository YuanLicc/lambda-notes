package com.yl.learn.funref;

import com.yl.learn.fina.Person;

import java.util.function.Supplier;

public class PersonOperator {

    public static String getNameAndPrefix(Supplier<Person> supplier) {
        return "person:" + supplier.get().getName();
    }

}
