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
        kryoInit();

        // this differs from just overloading the constructor
        // because in this case, the code calling this constructor
        // may not know that it is passing an Object that is a byte[]
        if (object instanceof byte[]) {
            objectByteArray = (byte[]) object;
        } else {
            // serialize
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Output output = new Output(stream);
            kryo.writeClassAndObject(output, object);
            output.close();
            objectByteArray = stream.toByteArray();

            // TODO: deal with this in better way
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void kryoInit() {
        // set up Kryo to use Objenesis object initialization strategy
        kryo = new Kryo();
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
    }

    // TODO: simpler way to do serialize in constructor?
    // TODO: if something else had a reference to the serialized object, it won't after reload, right?

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
        ByteArrayInputStream bytesInput = new ByteArrayInputStream(objectByteArray);
        Input input = new Input(bytesInput);
        Object object = kryo.readClassAndObject(input);
        input.close();

        // TODO: deal with this in better way
        try {
            bytesInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return object;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (byte b : objectByteArray) {
            sb.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
        return sb.toString();
    }

}
