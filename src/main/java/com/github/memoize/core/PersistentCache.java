package com.github.memoize.core;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

public class PersistentCache {

    Ehcache cache;

    public PersistentCache(Ehcache cache) {
        this.cache = cache;
    }

    public Object get(Object key) {
        Element element = cache.get(new CacheWrapper(key));
        if (element == null) {
            return null;
        }
        CacheWrapper wrappedResult = (CacheWrapper) element.getValue();
        return wrappedResult.getWrappedObject();
    }

    public void put(Object key, Object value) {
        Element element = new Element(new CacheWrapper(key), new CacheWrapper(value));
        cache.put(element);
    }
}
