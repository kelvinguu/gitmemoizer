package com.github.memoize.core;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Created by Kelvin on 12/20/13.
 */
public class CacheWrapperTests {


    @Test
    public void testEquals() throws NoSuchMethodException {
        Method method = Math.class.getMethod("pow", double.class, double.class);
        GitCacheKey key1 = new GitCacheKey(method, Arrays.<Object>asList(1, 2, 3), "HEAD");
        GitCacheKey key2 = new GitCacheKey(method, Arrays.<Object>asList(1, 2, 3), "HEAD");
        GitCacheKey key3 = new GitCacheKey(method, Arrays.<Object>asList(1, 3), "HEAD");

        CacheWrapper cw1 = new CacheWrapper(key1);
        CacheWrapper cw2 = new CacheWrapper(key2);
        CacheWrapper cw3 = new CacheWrapper(key3);

        assertEquals("two CacheWrappers with semantically same objects should equal", cw1, cw2);
        assertNotSame("two CacheWrappers with semantically diff objects should not equal", cw1, cw3);
    }
}
