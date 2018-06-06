package com.yl.learn.array;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class ArrayTest extends TestCase {
    public void testArray() {
        int[] array = new int[20];
        Arrays.parallelSetAll(array, i -> i);
        System.out.println(array);

        IntStream.range(0, array.length).parallel().forEach(i -> array[i] = i);


        Arrays.parallelPrefix(array, Integer::sum);
        System.out.println(array);

    }
}
