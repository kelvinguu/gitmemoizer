package com.github.memoize;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public abstract class CacheKey {

    protected boolean testTransients = true;

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, testTransients);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, testTransients);
    }

}
