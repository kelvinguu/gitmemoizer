package com.github.memoize.core;

public class Wheel {

    private Object parent;
    private String brand;

    public Wheel(Object parent, String brand) {
        this.parent = parent;
        this.brand = brand;

    }

    @Override
    public String toString() {
        return brand;
    }

    public Object getParent() {
        return parent;
    }
}
