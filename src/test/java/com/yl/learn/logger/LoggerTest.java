package com.yl.learn.logger;

import junit.framework.TestCase;

import java.util.logging.Logger;

public class LoggerTest extends TestCase {
    public void testLogger() {
        Logger logger = Logger.getLogger(LoggerTest.class.getName());

    }
}
