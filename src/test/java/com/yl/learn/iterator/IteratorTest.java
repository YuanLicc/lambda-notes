package com.yl.learn.iterator;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IteratorTest extends TestCase {
    public void testIterator() {
        List<Integer> items = new ArrayList<>();
        items.add(1);
        items.add(3);
        items.add(2);

        System.out.println(items.stream().filter(t -> {
                    System.out.println(t);
                    return t == 2;
                }).count());

        List<Integer> re = items.stream().collect(Collectors.toList());
        List<Integer> re1 = items.stream().map(item -> item - 1).collect(Collectors.toList());
        long co = Stream.of(re, re1).flatMap(item -> item.stream()).count();
        System.out.println(co);

        Integer min = items.stream().max(Comparator.comparing(item -> item * -1)).get();
        System.out.println(min);

        Integer accR = items.stream().reduce(4, (count, elements) -> count = count + 1);
        System.out.println(accR);

        Integer minVa = items.stream().reduce(Integer.MAX_VALUE, (minItem, item) -> minItem > item ? item : minItem);
        System.out.println(minVa);


        items.stream().forEach(item -> {
            item = item + 1;
        });

        int[] array = items.stream().mapToInt(item -> item - 1).toArray();
        System.out.println(array);

        Map<String, Integer> map = items.stream().map(item -> item - 1).collect(Collectors.toMap(
                (item) -> item.hashCode() + "key", (item) -> item
        ));
        System.out.println(map);

        Integer minK = items.stream().collect(Collectors.minBy((min1, min2)-> min1 > min2 ? 1 : -1)).get();
        System.out.println(minK);

        Map<Boolean, List<Integer>> cc = items.stream().collect(Collectors.partitioningBy((item) -> item <= 1 ? true : false));
        System.out.println(cc);

        Map<Integer, List<Integer>> kk = items.stream().collect(Collectors.groupingBy(item -> item));
        System.out.println(kk);

        String gg = items.stream().map(item -> item + "").collect(Collectors.joining("-", "{","}"));
        System.out.println(gg);
    }
}
