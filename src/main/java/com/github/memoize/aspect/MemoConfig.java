package com.github.memoize.aspect;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MemoConfig {
    String repoPath();
    String cacheDir();
    String logPath() default "not specified";
    boolean checkCommit() default true;
}

