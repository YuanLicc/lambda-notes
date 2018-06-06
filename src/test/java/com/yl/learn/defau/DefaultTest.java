package com.yl.learn.defau;

import junit.framework.TestCase;

public class DefaultTest extends TestCase {
    public void testDefault() {
        Say say = () -> {System.out.println("loudly say");};
        say.sayLoudly();
        say.say();

        SayThanks sayThanks = () -> {System.out.println("kkk");};
        sayThanks.say();
        sayThanks.sayLoudly();

        SayCry sayCry = () -> {System.out.println("aaa");};
        sayCry.say();
        sayCry.sayLoudly();
    }
}
