package com.github.memoize.core;

import java.lang.reflect.Method;

/**
 * User: Kelvin
 * Date: 12/2/13
 * Time: 6:59 PM
 */
public class StaticAnalysisUtils {

    public static String extractMethodDefinition(Method targetMethod, String source) {
        String name = targetMethod.getName();
        // TODO: actually extract method source
        return source;
    }

    public static String extractPackage(String source) {
        String[] lines = source.split("\n");
        for (String line : lines) {
            // TODO: this does not necessarily match all valid pkg statements
            if (line.matches("^package \\S+;$")) {
                return line.substring(0,line.length()-1).split(" ")[1];
            }
        }
        return null;
    }
}
