package com.github.memoize;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;

public class DefaultMemoizer implements Memoizer {

    private Logger logger;
    private Map<CacheKey, Object> cache;

    public DefaultMemoizer() {
        logger = Logger.getLogger(this.getClass());
        cache = new HashMap<CacheKey, Object>();
    }

    public Object callWithMemoization(ProceedingJoinPoint joinPoint) throws Throwable {

        Method targetMethod = IntrospectUtils.getMethod(joinPoint);
        List<Object> methodArgs = Arrays.asList(joinPoint.getArgs());
        CacheKey key = new DefaultCacheKey(targetMethod, methodArgs);
        logger.info("KEY: " + key);

        Object result = cache.get(key);
        if (result == null) {
            result = joinPoint.proceed();
            cache.put(key, result);
            logger.info("STORED: " + result);
        } else {
            logger.info("LOADED: " + result);
        }

        return result;
    }

}
