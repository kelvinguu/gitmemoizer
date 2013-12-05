package com.github.memoize.core;

import java.lang.reflect.Method;
import java.util.List;

/**
 * User: Kelvin
 * Date: 11/30/13
 * Time: 11:10 PM
 */

public class SourceCacheKey extends CacheKey {

    private Method targetMethod;
    private List<Object> methodArgs;
    private String methodSource;

    public SourceCacheKey(Method targetMethod, List<Object> methodArgs, String methodSource) {
        this.targetMethod = targetMethod;
        this.methodArgs = methodArgs;
        this.methodSource = methodSource;
    }

    @Override
    public String toString() {
        // TODO: do something that looks more like .join("\n")
        StringBuilder sb = new StringBuilder();
        String signatureString = targetMethod.toString();

        sb.append(signatureString).append("\n");
        for (Object arg : methodArgs) {
            sb.append(arg).append("\n");
        }
        sb.append(methodSource);
        return sb.toString();
    }
}
