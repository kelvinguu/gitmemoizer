package com.github.memoize.core;

import org.aspectj.lang.ProceedingJoinPoint;

public interface Memoizer {
    public Object callWithMemoization(ProceedingJoinPoint joinPoint) throws Throwable;
}
