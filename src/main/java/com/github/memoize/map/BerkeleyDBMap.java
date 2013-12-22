package com.github.memoize.map;

import com.github.memoize.core.CacheWrapper;
import com.sleepycat.je.*;
import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class BerkeleyDBMap implements Map {

    private Database db;
    private Environment dbEnv;

    public BerkeleyDBMap(File mapDir) {
        // Create mapDir if it doesn't exist
        mapDir.mkdirs();

        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setAllowCreate(true);
        dbEnv = new Environment(mapDir, envConfig);

        DatabaseConfig dbconf = new DatabaseConfig();
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
        return previousValue;
    }

    @Override
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

    @Override
    public Object remove(Object key) {
        DatabaseEntry dbKey = new DatabaseEntry(objectToBytes(key));
        Object previousValue = get(key);
        db.delete(null, dbKey);
        return previousValue;
    }

    private byte[] objectToBytes(Object object) {
        return new CacheWrapper(object).getObjectByteArray();
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
    public Set<Entry> entrySet() {
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
