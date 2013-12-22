package com.github.memoize.aspect;

import com.github.memoize.core.GitMemoizer;
import com.github.memoize.core.Memoizer;
import com.github.memoize.map.BerkeleyDBMap;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.io.File;
import java.util.Map;

@Aspect
public class MemoizationAspect {

    private Memoizer memoizer;
    public MemoizationAspect() throws Exception {
        // TODO: look in JSON-based config file to get user-selected
        // implementation of Memoizer and any String arguments to pass to the
        // Memoizer's constructor
        File repoPath = new File("/Users/Kelvin/Dropbox/projects/memoize/code/.git");
        File mapDir = new File("/Users/Kelvin/Desktop/memo_cache");
        Map cache = new BerkeleyDBMap(mapDir);

        boolean checkCommit = true;
        memoizer = new GitMemoizer(repoPath, cache, checkCommit);
    }

    @Around("@annotation(com.github.memoize.aspect.Memoizable)")
    public Object callWithMemoization(ProceedingJoinPoint joinPoint) throws Throwable {
        return memoizer.callWithMemoization(joinPoint);
    }
}
