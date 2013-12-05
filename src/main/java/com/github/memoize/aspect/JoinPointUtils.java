package com.github.memoize.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * User: Kelvin
 * Date: 11/30/13
 * Time: 10:12 PM
 */
public class JoinPointUtils {

    public static Class getClass(ProceedingJoinPoint joinPoint) {
        return joinPoint.getTarget().getClass();
    }

    public static Method getMethod(ProceedingJoinPoint joinPoint) {
        // http://stackoverflow.com/questions/5714411/getting-the-java-lang-reflect-method-from-a-proceedingjoinpoint
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }

}
