package com.github.memoize.core;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

import java.io.*;
import java.util.Arrays;

import static org.junit.Assert.*;

public class CacheWrapperTests {

    // initialize test objects to work with
    private Car car1;
    private Car car2;
    private Car car3;
    private CacheWrapper cw1;
    private CacheWrapper cw2;
    private CacheWrapper cw3;

    @Rule
    // TODO: is this right syntax? Needs to be static?
    public ExternalResource resource = new ExternalResource() {

        @Override
        protected void before() throws Throwable {

            // car1 and car2 are the same
            car1 = new Car();
            car1.add(new Wheel(car1, "Firestone"));
            car1.add(new Wheel(car1, "Bridgestone"));

            car2 = new Car();
            car2.add(new Wheel(car2, "Firestone"));
            car2.add(new Wheel(car2, "Bridgestone"));

            // car3 is different
            car3 = new Car();
            car3.add(new Wheel(car3, "Michelin"));
            car3.add(new Wheel(car3, "Bridgestone"));

            cw1 = new CacheWrapper(car1);
            cw2 = new CacheWrapper(car2);
            cw3 = new CacheWrapper(car3);
        }

    };

    @Test
    public void testByteArrayAsID() throws IOException {

        byte[] car1Bytes = cw1.getObjectByteArray();
        byte[] car2Bytes = cw2.getObjectByteArray();
        byte[] car3Bytes = cw3.getObjectByteArray();

        boolean sameBytes = Arrays.equals(car1Bytes,car2Bytes);
        boolean diffBytes = Arrays.equals(car1Bytes,car3Bytes);

        assertTrue("Semantically equal objects should be serialized same", sameBytes);
        assertFalse("Semantically different objects should be serialized different", diffBytes);
    }

    @Test
    public void testEquals() throws NoSuchMethodException {
        assertEquals("two CacheWrappers with semantically same objects should equal", cw1, cw2);
        assertNotSame("two CacheWrappers with semantically different objects should not equal", cw1, cw3);
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objectOut = new ObjectOutputStream(byteOut);

        // serialize
        objectOut.writeObject(cw1);
        objectOut.close();
        byte[] serialized = byteOut.toByteArray();

        // deserialize
        ByteArrayInputStream byteIn = new ByteArrayInputStream(serialized);
        ObjectInputStream objectIn = new ObjectInputStream(byteIn);
        CacheWrapper cw1Reloaded = (CacheWrapper) objectIn.readObject();
        objectIn.close();

        assertEquals("Objects should be equal after saving and reloading", cw1, cw1Reloaded);
    }

    @Test
    public void testGetWrappedObject() {
        Car car1Unwrapped = (Car) cw1.getWrappedObject();
        assertEquals("Object should be same after unwrapping", car1.toString(), car1Unwrapped.toString());
    }
}
