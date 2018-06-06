package com.yl.learn.fina;

import junit.framework.TestCase;

public class PersonTest extends TestCase {
    public void testFinalPerson() {
        final Person person = new Person();
        person.setName("dd");
        System.out.println(person.getName());
    }
}
