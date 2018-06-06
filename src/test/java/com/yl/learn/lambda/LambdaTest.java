package com.yl.learn.lambda;

import junit.framework.TestCase;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LambdaTest extends TestCase {
    public void testLambda() {

        JButton button = new JButton("click me");
        button.addActionListener(event -> System.out.println("clicked"));

        String name = "aaa";

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("clicked" + name);
            }
        });
    }
}
