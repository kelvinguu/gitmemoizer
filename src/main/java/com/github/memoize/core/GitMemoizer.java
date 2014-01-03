package com.github.memoize.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.github.memoize.aspect.JoinPointUtils;
import com.github.memoize.aspect.MemoConfig;
import com.github.memoize.aspect.Memoizable;
import com.github.memoize.map.BerkeleyDBMap;
import org.apache.log4j.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

public class GitMemoizer implements Memoizer {

    private Logger logger;
    private GitFacade git;
    private String headSHA;
    private Map cache;

    public GitMemoizer(JoinPoint joinPoint) throws IOException {
        // load user-specified configs
        Method targetMethod = JoinPointUtils.getMethod(joinPoint);
        MemoConfig annotation = targetMethod.getAnnotation(MemoConfig.class);
        boolean checkCommit = annotation.checkCommit();
        File repoPath = new File(annotation.repoPath());
        File cachePath = new File(annotation.cachePath());

        String logPathStr = annotation.logPath();
        File logPath;
        if (logPathStr.equals("not specified")) {
            // by default, save log next to cachePath
            logPath = new File(cachePath.getParentFile(), "memolog.txt");
        } else {
            logPath = new File(logPathStr);
        }

        // configure logger
        logger = Logger.getLogger(this.getClass());
        Layout logLayout = new SimpleLayout();
        logger.addAppender(new FileAppender(logLayout, logPath.getCanonicalPath(), true));

        // TODO: handle absence of repo
        git = new GitFacade(repoPath);
        headSHA = git.getCommitSHA("HEAD");
        cache = new BerkeleyDBMap(cachePath);

        logger.info("Repository: " + repoPath);
        logger.info("Cache: " + cachePath);
        logger.info("Log file: " + logPath);

        if (checkCommit) {
            commitCheck();
        }
    }

    // TODO: enable default options

    private void commitCheck() throws IOException {
        System.out.println("HEAD is at: " + headSHA);
        System.out.println("Is the currently executing code compiled from this commit? (y/n):");

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
        boolean customCommit = false;

        // determine commit SHA. Defaults to HEAD.
        String commitSHA = headSHA;
        String revisionStr = targetMethod.getAnnotation(Memoizable.class).version();
        if (!revisionStr.equals("HEAD")) {
            commitSHA = git.getCommitSHA(revisionStr);
            logger.warn("Using custom commit SHA: " + commitSHA);
            customCommit = true;
        }

        List<Object> methodArgs = Arrays.asList(joinPoint.getArgs());
        // if the joinPoint has no args, methodArgs is just a list with 0 elements

        GitCacheKey key = new GitCacheKey(targetMethod, methodArgs, commitSHA);
        Object result = cache.get(key);

        if (result == null) {
            if (customCommit) {
                logger.error("Could not find memoized result for commit SHA: " + commitSHA);
                // TODO: error should be thrown
            }
            result = joinPoint.proceed();
            cache.put(key, result);
            logger.info("STORED: " + key);
        } else {
            logger.info("LOADED: " + key);
        }

        return result;
    }

}
