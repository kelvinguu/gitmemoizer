package com.github.memoize;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * User: Kelvin
 * Date: 11/28/13
 * Time: 2:19 PM
 */
public class MemoizationAspectTests {

    @Test
    public void keyArgsBijection() throws NoSuchMethodException {
        // TODO: testing private methods
        // http://stackoverflow.com/questions/34571/whats-the-proper-way-to-test-a-class-with-private-methods-using-junit

        Method getKeyMethod = MemoizationAspect.class.getDeclaredMethod("getKey", ProceedingJoinPoint.class);
        getKeyMethod.setAccessible(true);
        getKeyMethod.invoke()
    }


}
