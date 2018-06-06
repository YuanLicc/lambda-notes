package com.yl.learn.optional;

import com.yl.learn.defau.Say;
import com.yl.learn.fina.Person;
import junit.framework.TestCase;

import java.util.Optional;

public class OptionalTest extends TestCase {
    public void testOptional() {
        Optional<Integer> optionalInteger = Optional.empty();
        System.out.println(optionalInteger.orElse(1));
        System.out.println(optionalInteger.orElseGet(() -> 1));

        Optional<Integer> optionalInteger1 = Optional.of(21);
        System.out.println(optionalInteger1.orElse(21));
        System.out.println(optionalInteger1.orElseGet(() -> 1));

    }
}
