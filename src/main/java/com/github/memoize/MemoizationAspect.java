package com.github.memoize;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class MemoizationAspect {

    private Memoizer memoizer;
    public MemoizationAspect() {
        // TODO: look in config file to get user-selected implementation of Memoizer
        memoizer = new DefaultMemoizer();
    }

    @Around("@annotation(com.github.memoize.Memoizable)")
    public Object callWithMemoization(ProceedingJoinPoint joinPoint) throws Throwable {
        return memoizer.callWithMemoization(joinPoint);
    }
}
