package com.yl.learn.lambda;

import junit.framework.TestCase;

public class PrintableHasReturnTest extends TestCase {
    public void testPrintableHasReturn() {
        PrintableHasReturn printableHasReturn =(String printed) -> {
            return printed;
        };

        PrintableHasReturn printableHasReturn1 = printed -> ("".equals(printed)) + "";
        System.out.println(printableHasReturn1.print("aaa"));

    }
}
