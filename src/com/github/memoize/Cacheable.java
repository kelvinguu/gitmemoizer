package com.github.memoize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Methods annotated with <code>@Cacheable</code> will have cached results and
 * will be called only if result is not in the cache.
 *
 * @author Igor Urmincek
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cacheable {

}

