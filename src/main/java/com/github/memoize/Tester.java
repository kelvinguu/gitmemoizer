package com.github.memoize;
import org.apache.log4j.Logger;

public class Tester {

    private static Logger logger = Logger.getLogger(Tester.class);

    public static void main(String[] args) throws Exception {

        Calculator calc = new Calculator();
        logger.debug("First result: " + calc.sum(1, 2));
        logger.debug("Second result: " + calc.sum(1, 2));
    }
}