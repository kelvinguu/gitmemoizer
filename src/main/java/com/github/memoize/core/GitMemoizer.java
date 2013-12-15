package com.github.memoize.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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

        GitFacade git = new GitFacade(repoPath);
        commitSHA = git.getCommitSHA("HEAD");

        //  prompt the user to enter their name
        System.out.println("Detected repository at:");
        System.out.println(repoPath);

        System.out.println("HEAD is at: " + commitSHA);
        System.out.println("Is the currently executing code compiled from this commit? (y/n):");
        System.out.flush();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String response = null;

        try {
            response = br.readLine();
        } catch (IOException e) {
            System.out.println("IO error trying to read your response!");
            System.exit(1);
        }

        // TODO: format response
        System.out.println(response);
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
