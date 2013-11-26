package com.github.memoize;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

/**
 * Sample app that uses AspectJ for caching
 *
 * @author Igor Urmincek
 *
 */
public class App {

    private static Logger logger = Logger.getLogger(App.class);

    public static void main(String[] args) {
        BasicConfigurator.configure();

        logger.debug("Starting...");
        Calculator calc = new Calculator();

        // result will be calculated and stored in cache
        logger.info("1 + 2 = " + calc.sum(1, 2));

        // result will be retrieved from cache
        logger.info("1 + 2 = " + calc.sum(1, 2));

        logger.debug("Finished!");
    }
}