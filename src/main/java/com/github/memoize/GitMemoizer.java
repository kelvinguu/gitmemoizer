package com.github.memoize;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;

public class GitMemoizer implements Memoizer {

    private Logger logger;
    private Map<CacheKey, Object> cache;
    private RepositoryFacade repo;

    public GitMemoizer(String repoPath) throws IOException {
        logger = Logger.getLogger(this.getClass());
        cache = new HashMap<CacheKey, Object>();
        repo = new RepositoryFacade(repoPath);

        // TODO: when object is destroyed, call: repo.close();
    }

    public Object callWithMemoization(ProceedingJoinPoint joinPoint) throws Throwable {

        Method targetMethod = JoinPointUtils.getMethod(joinPoint);
        List<Object> methodArgs = Arrays.asList(joinPoint.getArgs());
        Class targetClass = targetMethod.getDeclaringClass();

        String classSource = repo.getFileAsString(targetClass.getSimpleName() + ".java");

        // TODO: extract method source
        String methodSource = classSource;

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
