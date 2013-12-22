package com.github.memoize.map;

import com.github.memoize.core.CacheWrapper;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.*;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class EhcacheMap implements Map<Object,Object> {

    Ehcache cache;

    public EhcacheMap(File cachePath) throws IOException {
        // initialize cache
        Configuration cacheManagerConfig = new Configuration()
                .diskStore(new DiskStoreConfiguration().path(cachePath.getCanonicalPath()));
        CacheConfiguration cacheConfig = new CacheConfiguration()
                .name("memoCache")
                .maxBytesLocalHeap(1024, MemoryUnit.MEGABYTES)
                .maxBytesLocalOffHeap(2048, MemoryUnit.MEGABYTES)
                .persistence(new PersistenceConfiguration().strategy(PersistenceConfiguration.Strategy.LOCALRESTARTABLE));

        cacheManagerConfig.addCache(cacheConfig);

        CacheManager cacheManager = new CacheManager(cacheManagerConfig);
        cache = cacheManager.getEhcache("memoCache");
    }

    public Object get(Object key) {
        Element element = cache.get(new CacheWrapper(key));
        if (element == null) {
            return null;
        }
        CacheWrapper wrappedResult = (CacheWrapper) element.getValue();
        return wrappedResult.getWrappedObject();
    }

    public Object put(Object key, Object value) {
        Element element = new Element(new CacheWrapper(key), new CacheWrapper(value));
        cache.put(element);

        // TODO: fill this in
        return null;
    }

    // TODO: implement remaining methods

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public Object remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set keySet() {
        return null;
    }

    @Override
    public Collection values() {
        return null;
    }

    @Override
    public Set<Entry<Object, Object>> entrySet() {
        return null;
    }
}
