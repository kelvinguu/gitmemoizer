package com.github.memoize;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
/**
 * User: Kelvin
 * Date: 12/5/13
 * Time: 11:05 PM
 */
public class TestRunner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses();
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
    }
}
