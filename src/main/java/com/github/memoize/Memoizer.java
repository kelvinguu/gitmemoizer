package com.github.memoize;

import org.aspectj.lang.ProceedingJoinPoint;
import java.io.IOException;

public interface Memoizer {
    public Object callWithMemoization(ProceedingJoinPoint joinPoint) throws Throwable;
}
