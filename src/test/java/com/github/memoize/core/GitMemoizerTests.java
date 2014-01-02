package com.github.memoize.core;

import com.github.memoize.aspect.Memoizable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;
import java.io.File;

import java.lang.Math;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class GitMemoizerTests {

    // Integration tests for GitMemoizer.

    // test class whose method is called
    private class Foo {
        @Memoizable
        public double bar(double a, double b) {
            return Math.pow(a, b);
        }
    }

    private GitMemoizer gitMemoizer;
    private ProceedingJoinPoint joinPoint;

    @Rule
    // TODO: these tests have been broken
    public ExternalResource resource = new ExternalResource() {

        @Override
        protected void before() throws Throwable {
            // initialize GitMemoizer
            //File repoPath = new File("/Users/Kelvin/Dropbox/projects/memoize/code/.git");
            //File cachePath = new File("/Users/Kelvin/Desktop/memo_cache");
            //boolean checkCommit = false;
            //Map<Object, Object> cache = new HashMap<Object, Object>();
            //gitMemoizer = new GitMemoizer(repoPath, cache, checkCommit);
            //
            //// mock joinPoint, giving it a single method
            //MethodSignature methodSignature = mock(MethodSignature.class);
            //Method method = Foo.class.getMethod("bar", double.class, double.class);
            //when(methodSignature.getMethod()).thenReturn(method);
            //
            //joinPoint = mock(ProceedingJoinPoint.class);
            //when(joinPoint.getSignature()).thenReturn(methodSignature);

        }

        protected void after() {
            gitMemoizer = null;
            joinPoint = null;
        }

    };

    @Test
    public void testCacheHit() throws Throwable {

        // arguments to be memoized
        double a = 3;
        double b = 20;
        Object[] args = {a, b};

        // mock joinPoint further
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
