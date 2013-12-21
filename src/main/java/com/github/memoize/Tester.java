package com.github.memoize;

import com.github.memoize.core.GitMemoizer;
import org.apache.log4j.Logger;

import java.io.File;


public class Tester {

    private static Logger logger = Logger.getLogger(Tester.class);

    public static void main(String[] args) throws Exception {

        GitMemoizer gitMemoizer = new GitMemoizer(new File("/Users/Kelvin/Dropbox/projects/memoize/code/.git"));

        Calculator calc = new Calculator();
        logger.debug("First result: " + calc.sum(1, 2));
        logger.debug("Second result: " + calc.sum(1, 2));
    }
}