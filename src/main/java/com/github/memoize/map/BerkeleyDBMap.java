package com.github.memoize.map;

import com.github.memoize.core.CacheWrapper;
import com.github.memoize.core.GitCacheKey;
import com.sleepycat.je.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class BerkeleyDBMap implements Map<Object,Object> {

    private Database db;
    private Environment dbEnv;
    private Logger logger;

    public BerkeleyDBMap(File mapDir) {
        logger = Logger.getLogger(BerkeleyDBMap.class);

        // mapDir is a folder that should ONLY contain the database files
        // Create mapDir if it doesn't exist
        mapDir.mkdirs();

        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setAllowCreate(true);
        dbEnv = new Environment(mapDir, envConfig);

        DatabaseConfig dbconf = new DatabaseConfig();
        dbconf.setDeferredWrite(true);
        dbconf.setAllowCreate(true);
        dbconf.setSortedDuplicates(false);
        db = dbEnv.openDatabase(null, "memoCache", dbconf);
    }

    @Override
    public Object put(Object key, Object value) {
        DatabaseEntry dbKey = new DatabaseEntry(objectToBytes(key));
        DatabaseEntry dbValue = new DatabaseEntry(objectToBytes(value));

        Object previousValue = get(key);
        db.put(null, dbKey, dbValue);
        db.sync();
        return previousValue;
    }

    @Override
    public Object get(Object key) {

        // print method bytes
        if (key instanceof GitCacheKey) {
            try {
                GitCacheKey gcKey = (GitCacheKey) key;
                // use setAccessible to open private method
                Field field = GitCacheKey.class.getDeclaredField("targetMethod");
                field.setAccessible(true);
                Method method = (Method) field.get(gcKey);
                logger.debug(new CacheWrapper(method));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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

    @Override
    public void finalize() {
        close();
    }

    @Override
    public Object remove(Object key) {
        DatabaseEntry dbKey = new DatabaseEntry(objectToBytes(key));
        Object previousValue = get(key);
        db.delete(null, dbKey);
        return previousValue;
    }

    private byte[] objectToBytes(Object object) {
        CacheWrapper cw = new CacheWrapper(object);
        return cw.getObjectByteArray();
    }

    private Object bytesToObject(byte[] objectByteArray) {
        CacheWrapper cw = new CacheWrapper(objectByteArray);
        return cw.getWrappedObject();
    }

    // TODO: implement other methods

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
}
