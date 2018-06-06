package com.yl.learn.map;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

public class MapTest extends TestCase {
    public void testMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);

        Integer c = map.computeIfAbsent("c", (key) -> key.hashCode() * -1);
        System.out.println(c);

        Integer d = map.compute("a", (key, value) -> key.hashCode() + value);
        System.out.println(d);

        map.forEach(
                (key, value) -> {
                    System.out.println(key + ":" + value);
                }
        );
    }
}
