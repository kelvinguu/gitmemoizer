package com.github.memoize;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.io.IOException;

@Aspect
public class MemoizationAspect {

    private Memoizer memoizer;
    public MemoizationAspect() throws IOException {
        // TODO: look in JSON-based config file to get user-selected
        // implementation of Memoizer and any String arguments to pass to the
        // Memoizer's constructor
        String repoPath = "/Users/Kelvin/Dropbox/projects/memoize/code/.git";
        memoizer = new GitMemoizer(repoPath);
    }

    @Around("@annotation(com.github.memoize.Memoizable)")
    public Object callWithMemoization(ProceedingJoinPoint joinPoint) throws Throwable {
        return memoizer.callWithMemoization(joinPoint);
    }
}
