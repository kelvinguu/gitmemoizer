package com.github.memoize.core;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.memoize.aspect.JoinPointUtils;
import com.kelvingu.giterable.Giterable;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;

public class GitMemoizer implements Memoizer {

    private Logger logger;
    private Map<CacheKey, Object> cache;
    private Map<String, String> methodSources;

    public GitMemoizer(String repoPath) throws Exception {
        logger = Logger.getLogger(this.getClass());
        cache = new HashMap<CacheKey, Object>();

        // TODO: user warning logic


        methodSources = getMethodSources(repoPath);
    }

    public Map<String,String> getMethodSources(String repoPath) throws Exception {
        // TODO: fill this in
        return null;
    }

    public Object callWithMemoization(ProceedingJoinPoint joinPoint) throws Throwable {

        Method targetMethod = JoinPointUtils.getMethod(joinPoint);
        List<Object> methodArgs = Arrays.asList(joinPoint.getArgs());
        String methodSource = getMethodSource(targetMethod);

        CacheKey key = new SourceCacheKey(targetMethod, methodArgs, methodSource);
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
