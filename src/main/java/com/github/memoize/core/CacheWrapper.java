package com.github.memoize.core;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.*;
import java.util.Arrays;

public class CacheWrapper implements Serializable {

    private byte[] objectByteArray;
    private transient Kryo kryo;

    public CacheWrapper(Object object) {

        // set up Kryo to use Objenesis object initialization strategy
        kryo = new Kryo();
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());

        // serialize
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Output output = new Output(stream);
        kryo.writeClassAndObject(output, object);
        output.close();
        objectByteArray = stream.toByteArray();
    }

    // TODO: simpler way to do serialize in constructor?
    // TODO: if something else had a reference to the serialized object, it won't after reload, right?
    // TODO: keep one Kryo instance at all times?

    @Override
    public boolean equals(Object object) {
        // TODO: more elegant way to do this?
        if (!(object instanceof CacheWrapper)) {
            return false;
        }
        CacheWrapper cw = (CacheWrapper) object;
        return Arrays.equals(objectByteArray, cw.getObjectByteArray());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(objectByteArray);
    }

    public byte[] getObjectByteArray() {
        return objectByteArray;
    }

    public Object getWrappedObject() {
        Input input = new Input(new ByteArrayInputStream(objectByteArray));
        Object object = kryo.readClassAndObject(input);
        input.close();
        return object;
    }

}
