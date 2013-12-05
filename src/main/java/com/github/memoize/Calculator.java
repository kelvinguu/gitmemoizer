package com.github.memoize;

import com.github.memoize.aspect.Memoizable;
import org.apache.log4j.Logger;

public class Calculator {

    private Logger logger = Logger.getLogger(Calculator.class);

    @Memoizable
    public int sum(int a, int b) throws InterruptedException {
        Thread.sleep(500);
        return a + b;
    }

}
