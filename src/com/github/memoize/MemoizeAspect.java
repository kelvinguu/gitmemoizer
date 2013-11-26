package com.github.memoize;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class MemoizeAspect {

    private Logger logger = Logger.getLogger(MemoizeAspect.class);
    private Map<String, Object> cache;

    public MemoizeAspect() {
        cache = new HashMap<String, Object>();
    }

    /**
     * Pointcut for all methods annotated with <code>@Memoize</code>
     */
    @Pointcut("execution(@Memoize * *.*(..))")
    @SuppressWarnings("unused")
    private void memoize() {
    }

    @Around("memoize()")
    public Object aroundMemoizedMethod(ProceedingJoinPoint thisJoinPoint) throws Throwable {

        // generate the key under which cached value is stored
        // will look like caching.aspectj.Calculator.sum(Integer=1;Integer=2;)
        StringBuilder keyBuff = new StringBuilder();

        // append name of the class
        keyBuff.append(thisJoinPoint.getTarget().getClass().getName());

        // append name of the method
        keyBuff.append(".").append(thisJoinPoint.getSignature().getName());

        keyBuff.append("(");
        // loop through method arguments
        for (final Object arg : thisJoinPoint.getArgs()) {
            // append argument type and value
            keyBuff.append(arg.getClass().getSimpleName() + "=" + arg + ";");
        }
        keyBuff.append(")");
        String key = keyBuff.toString();

        logger.info("Key = " + key);
        Object result = cache.get(key);
        if (result == null) {
            logger.info("Result not cached. Calculating...");
            result = thisJoinPoint.proceed();
            logger.info("Result stored: " + result);
            cache.put(key, result);
        } else {
            logger.info("Result reloaded: " + result);
        }

        return result;
    }

}
