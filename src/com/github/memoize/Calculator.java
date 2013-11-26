package com.github.memoize;

import org.apache.log4j.Logger;

/**
 * Time consuming example class
 *
 * @author Igor Urmincek
 *
 */
public class Calculator {

    private Logger logger = Logger.getLogger(Calculator.class);

    @Cacheable
    public int sum(int a, int b) {
        logger.info("Calculating " + a + " + " + b);
        try {
            // pretend this is an expensive operation
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            logger.error("Something went wrong...", e);
        }
        return a + b;
    }

}
