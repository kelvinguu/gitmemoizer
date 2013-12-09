package com.github.memoize.core;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.memoize.aspect.JoinPointUtils;
import com.github.memoize.git.GitFacade;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;

public class GitMemoizer implements Memoizer {

    private Logger logger;
    private Map<CacheKey, Object> cache;
    private Map<String, String> sourceFiles;

    public GitMemoizer(String repoPath) throws Exception {
        logger = Logger.getLogger(this.getClass());
        cache = new HashMap<CacheKey, Object>();

        // TODO: user warning logic

        GitFacade repo = new GitFacade(repoPath);
        sourceFiles = repo.getSourceFiles();
    }

    public String getMethodSource(Method targetMethod) throws Exception {
        Class targetClass = targetMethod.getDeclaringClass();

        // TODO: what's diff between getName and getCanonicalName
        String classSource = sourceFiles.get(targetClass.getCanonicalName());
        return StaticAnalysisUtils.extractMethodDefinition(targetMethod, classSource);
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
