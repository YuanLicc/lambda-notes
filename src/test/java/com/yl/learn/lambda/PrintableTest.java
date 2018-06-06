package com.yl.learn.lambda;

import junit.framework.TestCase;

public class PrintableTest extends TestCase {

    public void testPrintable() {
        Printable printable = () -> System.out.println("aaa");
        printable.print();

        PrintableOneParam printableOneParam = arg -> System.out.println(arg);
        printableOneParam.print("aaaa");

        PrintableOneParam printableOneParam1 = arg -> {
            System.out.println("one line");
            System.out.println(arg);
        };

        String name = "ccc";

        PrintableMultiParams printableMultiParams = (arg, isPrint) -> {
            if(isPrint) {
                System.out.println(arg + name);
            }
        };

        printableMultiParams.print("kk", true);
        printableMultiParams.print("kk", false);
    }
}
