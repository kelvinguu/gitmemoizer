package com.github.memoize;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.apache.commons.io.IOUtils;

@Aspect
public class MemoizeAspect {

    private Logger logger = Logger.getLogger(MemoizeAspect.class);
    private Map<String, Object> cache;

    public MemoizeAspect() {
        cache = new HashMap<String, Object>();
    }

    /**
     * Pointcut for all methods annotated with <code>@Memoize</code>
     */
    @Pointcut("execution(@Memoize * *.*(..))")
    @SuppressWarnings("unused")
    private void memoize() {
    }

    private String getClassFileAsString(ProceedingJoinPoint joinPoint) throws IOException {
        // TODO: when will this fail?
        // TODO: not safe to assume UTF-8 encoding
        // TODO: only return bytecode for method
        // http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
        Class targetClass = joinPoint.getTarget().getClass();
        String classFileString = targetClass.getSimpleName() + ".class";
        return IOUtils.toString(targetClass.getResourceAsStream(classFileString), "UTF-8");
    }

    private String getKey(ProceedingJoinPoint joinPoint) throws IOException {

        StringBuilder keyBuffer = new StringBuilder();

        String className = joinPoint.getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        String codeString = getClassFileAsString(joinPoint);

        keyBuffer.append(className);
        keyBuffer.append(".").append(methodName);
        keyBuffer.append("(");

        // loop through cacheable method arguments
        for (final Object arg : joinPoint.getArgs()) {
            // append argument type and value
            String methodType = arg.getClass().getSimpleName();
            keyBuffer.append(methodType).append("=").append(arg).append(",");
            // TODO: check that the toString for arg is unique
        }

        keyBuffer.append(")\n");
        keyBuffer.append(codeString);

        return keyBuffer.toString();
    }
    @Around("memoize()")
    public Object aroundMemoizedMethod(ProceedingJoinPoint thisJoinPoint) throws Throwable {

        // generate unique memoization key
        String key = getKey(thisJoinPoint);
        logger.info("KEY: " + key.substring(0,key.indexOf('\n')));
        // don't print codeString

        Object result = cache.get(key);
        if (result == null) {
            result = thisJoinPoint.proceed();
            cache.put(key, result);
            logger.info("STORED: " + result);
        } else {
            logger.info("LOADED: " + result);
        }

        return result;
    }

}
