package com.github.memoize.core;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;

public class BerkeleyDBFacadeTests {

    // define test objects to work with
    private BerkeleyDBFacade bdb;
    private File cacheDir;

    @Rule
    public ExternalResource resource = new ExternalResource() {

        @Override
        protected void before() throws Throwable {
            cacheDir = Files.createTempDirectory("cacheDir").toFile();
            bdb = new BerkeleyDBFacade(cacheDir);
        }

        @Override
        protected void after() {
            bdb.close();
            try {
                FileUtils.deleteDirectory(cacheDir);
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
}
