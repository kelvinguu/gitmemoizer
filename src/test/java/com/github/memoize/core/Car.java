package com.github.memoize.core;

import java.util.ArrayList;
import java.util.List;

public class Car {

    private List components;

    public Car() {
        components = new ArrayList();
    }

    public void add(Object component) {
        components.add(component);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Object comp : components) {
            sb.append(comp.toString() + '\n');
        }
        return sb.toString();
    }
}
