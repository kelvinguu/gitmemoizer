package com.github.memoize.core;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.*;

import java.io.File;
import java.io.IOException;

public class EhcacheFacade {

    Ehcache cache;

    public EhcacheFacade(File cachePath) throws IOException {
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

    public void put(Object key, Object value) {
        Element element = new Element(new CacheWrapper(key), new CacheWrapper(value));
        cache.put(element);
    }
}
