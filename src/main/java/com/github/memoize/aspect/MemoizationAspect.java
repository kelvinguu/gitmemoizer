package com.github.memoize.aspect;

import com.github.memoize.core.GitMemoizer;
import com.github.memoize.core.Memoizer;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import java.lang.reflect.Method;

@Aspect
public class MemoizationAspect {

    private Memoizer memoizer;
    private Logger logger;

    public MemoizationAspect() throws Exception {
        memoizer = null;
        logger = Logger.getLogger(MemoizationAspect.class);
    }

    @Before("@annotation(com.github.memoize.aspect.MemoConfig)")
    public void configure(JoinPoint joinPoint) throws Throwable {
        memoizer = new GitMemoizer(joinPoint);
    }

    @Around("@annotation(com.github.memoize.aspect.Memoizable)")
    public Object callWithMemoization(ProceedingJoinPoint joinPoint) throws Throwable {
        if (memoizer == null) {
            Method targetMethod = JoinPointUtils.getMethod(joinPoint);
            logger.warn("Memoizer has not been configured yet. No memoization applied to: "
                    + targetMethod);
            return joinPoint.proceed();
        }

        return memoizer.callWithMemoization(joinPoint);
    }
}
