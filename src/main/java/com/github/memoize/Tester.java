package com.github.memoize;

import com.google.common.base.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

import java.util.Arrays;
import java.util.List;

public class Tester {

    private static Logger logger = Logger.getLogger(Tester.class);

    public static void main(String[] args) {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.DEBUG);
        logger.debug("Starting...");

        //List<String> aList = Arrays.asList("one", "two", "three");
        //List<String> bList = Arrays.asList("one", "two", "three");
        //
        //Dummy a = new Dummy(aList);
        //Dummy b = new Dummy(bList);
        //
        //boolean testTransients = true;
        //boolean same = EqualsBuilder.reflectionEquals(a, b, testTransients);
        //System.out.println(same);
        //
        //System.out.println(HashCodeBuilder.reflectionHashCode(a,testTransients));
        //System.out.println(HashCodeBuilder.reflectionHashCode(b,testTransients));

        Calculator calc = new Calculator();

        // result will be calculated and stored in cache
        logger.debug("1 + 2 = " + calc.sum(1, 2));

        // result will be retrieved from cache
        logger.debug("1 + 2 = " + calc.sum(1, 2));

        logger.debug("Finished!");
    }
}