package com.github.memoize.map;

import com.github.memoize.core.CacheWrapper;
import com.github.memoize.core.Car;
import com.github.memoize.core.Wheel;
import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;

import static org.junit.Assert.*;

public class BerkeleyDBMapTests {

    // define test objects to work with
    private BerkeleyDBMap bdb;
    private File mapDir;

    @Rule
    public ExternalResource resource = new ExternalResource() {

        @Override
        protected void before() throws Throwable {
            mapDir = Files.createTempDirectory("mapDir").toFile();
            bdb = new BerkeleyDBMap(mapDir);
        }

        @Override
        protected void after() {
            bdb.close();
            try {
                FileUtils.deleteDirectory(mapDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    @Test
    public void testPut() {
        Car car1 = new Car();
        car1.add(new Wheel(car1, "1st car wheel"));

        Car car2 = new Car();
        car2.add(new Wheel(car2, "2nd car wheel"));

        bdb.put("car1", car1);
        bdb.put("car2", car2);

        Car returnCar = (Car) bdb.get("car1");

        assertEquals("object put in should be same object retrieved", car1, returnCar);
    }

    @Test
    public void testBadGet() {
        Object returnCar = bdb.get("nonexistent");
        assertEquals("database should return null", null, returnCar);
    }

    @Test
    public void testPutReturn() {
        fail("TODO");
    }

    @Test
    public void testPersistence() throws IOException {
        File persistDir = Files.createTempDirectory("persistDir").toFile();
        BerkeleyDBMap origDB = new BerkeleyDBMap(persistDir);
        origDB.put("key", "value");
        origDB.close();

        BerkeleyDBMap reviveDB = new BerkeleyDBMap(persistDir);
        Object value = reviveDB.get("key");
        reviveDB.close();

        try {
            FileUtils.deleteDirectory(persistDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals("reloaded database should still have original entry.", "value", value);
    }

    @Test
    public void testCreateDirectory() {
        fail("TODO");
    }
}
