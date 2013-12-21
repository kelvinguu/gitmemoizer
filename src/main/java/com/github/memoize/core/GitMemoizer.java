package com.github.memoize.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import com.github.memoize.aspect.JoinPointUtils;
import com.github.memoize.aspect.Memoizable;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;

public class GitMemoizer implements Memoizer {

    private Logger logger;
    private GitFacade git;
    private String headSHA;
    private PersistentCache cache;

    public GitMemoizer(File repoPath, boolean checkCommit) throws Exception {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.DEBUG);
        logger = Logger.getLogger(this.getClass());

        // initialize cache
        CacheManager cacheManager = CacheManager.create();
        cacheManager.addCache("memoCache");
        Ehcache ehcache = cacheManager.getCache("memoCache");
        cache = new PersistentCache(ehcache);

        // TODO: handle absence of repo
        git = new GitFacade(repoPath);
        headSHA = git.getCommitSHA("HEAD");
        logger.info("Detected repository at: " + repoPath);

        if (checkCommit) {
            commitCheck();
        }
    }

    public GitMemoizer(File repoPath) throws Exception {
        this(repoPath, true);
    }

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
        GitCacheKey key = new GitCacheKey(targetMethod, methodArgs, commitSHA);
        logger.info("KEY: " + key);

        Object result = cache.get(key);

        if (result == null) {
            if (customCommit) {
                logger.error("Could not find memoized result for commit SHA: " + commitSHA);
                // TODO: error should be thrown
            }
            result = joinPoint.proceed();
            cache.put(key, result);
            logger.info("STORED: " + result);
        } else {
            logger.info("LOADED: " + result);
        }

        return result;
    }

}
