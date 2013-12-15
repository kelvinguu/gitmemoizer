package com.github.memoize.core;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.memoize.aspect.JoinPointUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;

public class GitMemoizer implements Memoizer {

    private Logger logger;
    private Map<CacheKey, Object> cache;
    private String commitSHA;

    public GitMemoizer(File repoPath) throws Exception {
        logger = Logger.getLogger(this.getClass());
        cache = new HashMap<CacheKey, Object>();

        // TODO: user warning logic

        GitFacade git = new GitFacade(repoPath);
        commitSHA = git.getCommitSHA("HEAD");
    }

    public Object callWithMemoization(ProceedingJoinPoint joinPoint) throws Throwable {

        // TODO: check for user-specified commitSHA

        Method targetMethod = JoinPointUtils.getMethod(joinPoint);
        List<Object> methodArgs = Arrays.asList(joinPoint.getArgs());

        CacheKey key = new GitCacheKey(targetMethod, methodArgs, commitSHA);
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
