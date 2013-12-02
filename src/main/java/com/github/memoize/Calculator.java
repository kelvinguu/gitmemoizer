package com.github.memoize;

import org.apache.log4j.Logger;

public class Calculator {

    private Logger logger = Logger.getLogger(Calculator.class);

    @Memoizable
    public int sum(int a, int b) {
        logger.info("Calculating " + a + " + " + b);
        try {
            // pretend this is an expensive operation
            Thread.sleep(500);
        } catch (InterruptedException e) {
            logger.error("Something went wrong...", e);
        }
        return a + b;
    }

}
