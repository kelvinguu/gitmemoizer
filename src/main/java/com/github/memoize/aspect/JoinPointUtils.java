package com.github.memoize.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import java.lang.reflect.Method;

public class JoinPointUtils {

    public static Class getClass(JoinPoint joinPoint) {
        return joinPoint.getTarget().getClass();
    }

    public static Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }

}
