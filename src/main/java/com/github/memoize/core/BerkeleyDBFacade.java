package com.github.memoize.core;

import com.sleepycat.je.*;
import java.io.File;

public class BerkeleyDBFacade {

    private Database db;
    private Environment dbEnv;
    public BerkeleyDBFacade(File cacheDir) {
        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setAllowCreate(true);
        dbEnv = new Environment(cacheDir, envConfig);

        DatabaseConfig dbconf = new DatabaseConfig();
        dbconf.setAllowCreate(true);
        dbconf.setSortedDuplicates(false);
        db = dbEnv.openDatabase(null, "memoCache", dbconf);
    }

    public void put(Object key, Object value) {
        DatabaseEntry dbKey = new DatabaseEntry(objectToBytes(key));
        DatabaseEntry dbValue = new DatabaseEntry(objectToBytes(value));
        db.put(null, dbKey, dbValue);
    }

    public Object get(Object key) {
        // TODO: what's searchEntry for?
        DatabaseEntry searchEntry = new DatabaseEntry();
        DatabaseEntry dbKey = new DatabaseEntry(objectToBytes(key));

        db.get(null, dbKey, searchEntry, LockMode.DEFAULT);
        if (searchEntry.getSize() == 0) {
            return null;
        }
        return bytesToObject(searchEntry.getData());
    }

    public void close() {
        db.close();
        dbEnv.close();
    }

    public void delete(Object key) {
        DatabaseEntry dbKey = new DatabaseEntry(objectToBytes(key));
        db.delete(null, dbKey);
    }

    private byte[] objectToBytes(Object object) {
        return new CacheWrapper(object).getObjectByteArray();
    }

    private Object bytesToObject(byte[] objectByteArray) {
        CacheWrapper cw = new CacheWrapper(objectByteArray);
        return cw.getWrappedObject();
    }


}
