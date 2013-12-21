package com.github.memoize.core;

import com.github.memoize.aspect.Memoizable;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import java.io.File;

import java.lang.Math;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class GitMemoizerTests {

    // Integration tests for GitMemoizer.

    private GitMemoizer gitMemoizer;

    @Rule
    // TODO: is this right syntax? Needs to be static?
    public ExternalResource resource = new ExternalResource() {

        @Override
        protected void before() throws Throwable {
            File repoPath = new File("/Users/Kelvin/Dropbox/projects/memoize/code/.git");
            boolean checkCommit = false;
            gitMemoizer = new GitMemoizer(repoPath, checkCommit);
        }

    };

    @Test
    public void testCacheHit() throws Throwable {

        // arguments to be memoized
        double a = 3;
        double b = 20;
        Object[] args = {a, b};

        // test class whose method is called
        class Foo {
            @Memoizable
            public double bar(double a, double b) {
                return Math.pow(a, b);
            }
        }

        // mock joinPoint
        MethodSignature methodSignature = mock(MethodSignature.class);
        Method method = Foo.class.getMethod("bar", double.class, double.class);
        when(methodSignature.getMethod()).thenReturn(method);

        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(joinPoint.getArgs()).thenReturn(args);
        when(joinPoint.proceed()).thenReturn(new Foo().bar(a,b));

        // run
        Object result1 = gitMemoizer.callWithMemoization(joinPoint);
        Object result2 = gitMemoizer.callWithMemoization(joinPoint);


        // check that joinPoint only executed code once
        verify(joinPoint, times(1)).proceed();
    }

    // TODO: test that it fails when faulty commit SHA is given

}
