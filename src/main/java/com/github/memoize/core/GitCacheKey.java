package com.github.memoize.core;

import java.lang.reflect.Method;
import java.util.List;

public class GitCacheKey {

    // It is CRITICAL that targetMethod be marked as transient
    // Kryo can serialize the same method differently on different runs
    // targetMethod is captured mostly for debugging purposes
    private transient Method targetMethod;
    private String methodSignature;
    private List<Object> methodArgs;
    private String commitSHA;

    public GitCacheKey(Method targetMethod, List<Object> methodArgs, String commitSHA) {
        this.targetMethod = targetMethod;
        this.methodSignature = targetMethod.toString();
        // TODO: find a better unique identifier for targetMethod. Not it's toString
        this.methodArgs = methodArgs;
        this.commitSHA = commitSHA;
    }

    @Override
    public String toString() {
        // TODO: do something that looks more like .join("\n")
        StringBuilder sb = new StringBuilder();

        sb.append(methodSignature).append("\t");
        for (Object arg : methodArgs) {
            sb.append(arg).append("\t");
        }
        sb.append(commitSHA);
        return sb.toString();
    }
}
