package com.github.memoize.core;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.*;

public class CacheWrapper implements Serializable {

    private Object object;
    protected boolean testTransients = true;

    public CacheWrapper(Object object) {
        this.object = object;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(object, testTransients);
    }

    @Override
    public boolean equals(Object otherObject) {
        if (!(otherObject instanceof CacheWrapper)) {
            return false;
        }

        CacheWrapper otherWrapper = (CacheWrapper) otherObject;
        return EqualsBuilder.reflectionEquals(object, otherWrapper.getWrappedObject(), testTransients);
    }

    // TODO: why readClassAndObject?

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        Input input = new Input(objectInputStream);
        object = new Kryo().readClassAndObject(input);
        input.close();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        Output output = new Output(objectOutputStream);
        new Kryo().writeClassAndObject(output, object);
        output.close();
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Output output = new Output(stream);
        new Kryo().writeObject(output, object);
        output.close();
        return stream.toByteArray();
    }

    public Object getWrappedObject() {
        return object;
    }

}
