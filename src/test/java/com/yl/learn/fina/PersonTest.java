package com.yl.learn.fina;

import junit.framework.TestCase;

public class PersonTest extends TestCase {
    public void testFinalPerson() {
        final Person person = new Person();
        person.setName("dd");
        Person person1 = new Person("ccc");
        System.out.println(person.getName() + " " + person1.getName());
    }
}
