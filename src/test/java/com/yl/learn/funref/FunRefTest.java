package com.yl.learn.funref;

import com.yl.learn.fina.Person;
import junit.framework.TestCase;

import java.util.function.Supplier;

public class FunRefTest extends TestCase {
    public void testFunRef() {
        String namePrefix = PersonOperator.getNameAndPrefix(Person::new);

        Supplier<Person> personSupplier = () -> new Person();

        Supplier<Person> personSupplier1 = () -> new Person("ccc");
    }
}
