package com.yl.learn.funref;

import com.yl.learn.fina.Person;
import junit.framework.TestCase;

import java.util.function.Supplier;

public class FunRefTest extends TestCase {
    public void testFunRef() {
        String namePrefix = PersonOperator.getNameAndPrefix(Person::new);

        Supplier<Person> personSupplier1 = () -> new Person("ccc");
        String aa = PersonOperator.getNameAndPrefix(personSupplier1);
        System.out.println(namePrefix + aa);
    }
}
