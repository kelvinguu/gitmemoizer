package com.github.memoize;

import org.apache.log4j.Logger;
import java.lang.reflect.Method;
import java.util.List;

/**
 * User: Kelvin
 * Date: 11/30/13
 * Time: 11:10 PM
 */

public class DefaultCacheKey extends CacheKey {

    private Method targetMethod;
    private byte[] methodBytes;
    private List<Object> methodArgs;

    public DefaultCacheKey(Method targetMethod, List<Object> methodArgs) {
        this.targetMethod = targetMethod;
        this.methodArgs = methodArgs;
        methodBytes = IntrospectUtils.getMethodBytes(targetMethod);
    }

    @Override
    public String toString() {
        // TODO: do something that looks more like .join("\n")
        StringBuffer sb = new StringBuffer();
        String signatureString = targetMethod.toString();

        sb.append(signatureString).append("\n");
        for (Object arg : methodArgs) {
            sb.append(arg).append("\n");
        }
        return sb.toString();
    }
}
