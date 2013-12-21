package com.github.memoize.core;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class CacheWrapper implements Serializable {

    private Kryo kryo;
    private Object object;
    protected boolean testTransients = true;

    public CacheWrapper(Object object) {
        this.object = object;
        kryo = new Kryo();
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, testTransients);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, testTransients);
    }

    // TODO: why readClassAndObject?

    private void readObject(ObjectInputStream objectInputStream)
            throws IOException, ClassNotFoundException {
        Input input = new Input(objectInputStream);
        object = kryo.readClassAndObject(input);
        input.close();
    }

    private void writeObject(ObjectOutputStream objectOutputStream)
            throws IOException {
        Output output = new Output(objectOutputStream);
        kryo.writeClassAndObject(output, object);
        output.close();
    }

    public Object getWrappedObject() {
        return object;
    }

}
