package com.github.memoize;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

public class Tester {

    private static Logger logger = Logger.getLogger(Tester.class);

    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.DEBUG);

        //String repoPath = "/Users/Kelvin/Dropbox/projects/memoize/code/.git";
        //RepositoryFacade repo = new RepositoryFacade(repoPath);

        Calculator calc = new Calculator();

        // result will be calculated and stored in cache
        logger.debug("Calculated result: " + calc.sum(1, 2));

        // result will be retrieved from cache
        logger.debug("Reloaded result: " + calc.sum(1, 2));
    }
}