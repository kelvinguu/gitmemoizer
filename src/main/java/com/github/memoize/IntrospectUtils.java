package com.github.memoize;

import org.apache.commons.io.IOUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * User: Kelvin
 * Date: 11/30/13
 * Time: 10:12 PM
 */
public class IntrospectUtils {

    public static Class getClass(ProceedingJoinPoint joinPoint) {
        return joinPoint.getTarget().getClass();
    }

    public static Method getMethod(ProceedingJoinPoint joinPoint) {
        // http://stackoverflow.com/questions/5714411/getting-the-java-lang-reflect-method-from-a-proceedingjoinpoint
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }

    public static byte[] getClassBytes(Class targetClass) throws IOException {
        // TODO: when will this fail?
        String classFileString = targetClass.getSimpleName() + ".class";
        return IOUtils.toByteArray(targetClass.getResourceAsStream(classFileString));
    }

    public static byte[] getMethodBytes(Method targetMethod) {
        // TODO: only return bytecode for method
        return null;
    }

}
