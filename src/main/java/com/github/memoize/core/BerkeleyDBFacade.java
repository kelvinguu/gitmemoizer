package com.github.memoize.core;


import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.StoreConfig;
import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

import java.io.File;

public class BerkeleyDBFacade {

    @Entity
    private class CacheEntry {

        @PrimaryKey
        private CacheWrapper key;
        private CacheWrapper value;

        public CacheEntry(CacheWrapper key, CacheWrapper value) {
            this.key = key;
            this.value = value;

        }

    }

    public BerkeleyDBFacade(File cachePath) {
        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setAllowCreate(true);
        Environment dbEnv = new Environment(cachePath, envConfig);
        StoreConfig stConf = new StoreConfig(); stConf.setAllowCreate(true);
        EntityStore store = new EntityStore(dbEnv, "memoCache", stConf);


        // TODO: incomplete. Trying MapDB instead.
    }


}
