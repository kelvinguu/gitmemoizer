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
import com.github.memoize.aspect.Memoizable;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;

public class GitMemoizer implements Memoizer {

    private Logger logger;
    private Map<CacheKey, Object> cache;
    private GitFacade git;
    private String headSHA;

    public GitMemoizer(File repoPath) throws Exception {
        logger = Logger.getLogger(this.getClass());
        cache = new HashMap<CacheKey, Object>();

        // TODO: handle absence of repo
        git = new GitFacade(repoPath);
        headSHA = git.getCommitSHA("HEAD");

        System.out.println("Detected repository at:");
        System.out.println(repoPath);

        System.out.println("HEAD is at: " + headSHA);
        System.out.println("Is the currently executing code compiled from this commit? (y/n):");
        System.out.flush();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String response = br.readLine();
            boolean proceed = true;

            switch (response.toLowerCase()) {
                case "y":
                    System.out.println("Good. Proceeding with execution.");
                    break;
                case "n":
                    System.out.println("Commit your code before proceeding. Execution canceled.");
                    System.exit(1);
                    // TODO: continue execution, but no memoizing
                    break;
                default:
                    System.out.println("Invalid input. Must be 'y' or 'n'. Try again.");
                    proceed = false;
                    break;
            }

            if (proceed) {
                break;
            }
        }

    }

    public Object callWithMemoization(ProceedingJoinPoint joinPoint) throws Throwable {

        Method targetMethod = JoinPointUtils.getMethod(joinPoint);

        // determine commit SHA. Defaults to HEAD.
        String commitSHA = headSHA;

        // check for user overriding SHA
        String revisionStr = targetMethod.getAnnotation(Memoizable.class).version();
        if (!revisionStr.equals("HEAD")) {
            logger.warn("Commit SHA override: " + commitSHA);
            // TODO: give more detailed message
            commitSHA = git.getCommitSHA(revisionStr);
        }

        // TODO: what if nothing for this version is found?

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
