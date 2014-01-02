package com.github.memoize.core;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class CacheWrapperTests {

    // define test objects to work with
    private List<Car> cars;
    private List<CacheWrapper> wrappedCars;

    @Rule
    public ExternalResource resource = new ExternalResource() {

        @Override
        protected void before() throws Throwable {

            cars = new ArrayList<Car>();
            for (int k = 0; k < 3; k++) {
                Car car = new Car();
                car.add(new Wheel(car, "Bridgestone"));
                cars.add(car);
            }

            // car0 and car1 are the same
            // car2 is different. It has an extra wheel.
            cars.get(2).add(new Wheel(cars.get(2), "Michelin"));

            // wrap the cars
            wrappedCars = new ArrayList<CacheWrapper>();
            for (Car car : cars) {
                wrappedCars.add(new CacheWrapper(car));
            }
        }

        @Override
        protected void after() {
            cars = null;
            wrappedCars = null;
        }

    };

    @Test
    public void testByteArrayAsID() throws IOException {

        List<byte[]> carBytes = new ArrayList<byte[]>();
        for (CacheWrapper cw : wrappedCars) {
            carBytes.add(cw.getObjectByteArray());
        }

        boolean sameBytes = Arrays.equals(carBytes.get(0), carBytes.get(1));
        boolean diffBytes = Arrays.equals(carBytes.get(0), carBytes.get(2));

        assertTrue("Semantically equal objects should be serialized same", sameBytes);
        assertFalse("Semantically different objects should be serialized different", diffBytes);
    }

    @Test
    public void testByteArrayPreserved() {
        byte[] byteArray = "some bytes".getBytes();

        // this should hold even if an object is not explicitly declared to be a byte array
        Object keyObj = (Object) byteArray;
        boolean preserved = Arrays.equals(byteArray, new CacheWrapper(keyObj).getObjectByteArray());
        assertTrue("A byte array wrapped by CacheWrapper should be the same byte array when you request CacheWrapper's byte array", preserved);
    }

    @Test
    public void testEquals() throws NoSuchMethodException {
        assertEquals("two CacheWrappers with semantically same objects should equal",
                wrappedCars.get(0), wrappedCars.get(1));
        assertNotSame("two CacheWrappers with semantically different objects should not equal",
                wrappedCars.get(0), wrappedCars.get(2));
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {

        CacheWrapper cw = wrappedCars.get(0);
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objectOut = new ObjectOutputStream(byteOut);

        // serialize
        objectOut.writeObject(cw);
        objectOut.close();
        byte[] serialized = byteOut.toByteArray();

        // deserialize
        ByteArrayInputStream byteIn = new ByteArrayInputStream(serialized);
        ObjectInputStream objectIn = new ObjectInputStream(byteIn);
        CacheWrapper cwReload = (CacheWrapper) objectIn.readObject();
        objectIn.close();

        assertEquals("Objects should be equal after saving and reloading", cw, cwReload);
    }

    @Test
    public void testGetWrappedObject() {
        CacheWrapper cw = wrappedCars.get(0);
        Car car = (Car) cw.getWrappedObject();
        Car carUnwrapped = (Car) cw.getWrappedObject();
        assertEquals("Object should be same after unwrapping", car, carUnwrapped);
    }
}
