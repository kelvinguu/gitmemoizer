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

        // TODO: handle absence of repo
        GitFacade git = new GitFacade(repoPath);
        commitSHA = git.getCommitSHA("HEAD");

        System.out.println("Detected repository at:");
        System.out.println(repoPath);

        System.out.println("HEAD is at: " + commitSHA);
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
                    // TODO: this leaves a nasty error message
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
