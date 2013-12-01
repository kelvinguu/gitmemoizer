package com.github.memoize;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

public class Tester {

    private static Logger logger = Logger.getLogger(Tester.class);

    public static void main(String[] args) {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.DEBUG);
        logger.debug("Starting...");
        Calculator calc = new Calculator();

        // result will be calculated and stored in cache
        logger.debug("1 + 2 = " + calc.sum(1, 2));

        // result will be retrieved from cache
        logger.debug("1 + 2 = " + calc.sum(1, 2));

        logger.debug("Finished!");
    }
}