package org.visitor.appportal.domain;

public interface Copyable<T> {
    /**
     * Return a copy of the current object
     */
    T copy();

    /**
     * Copy the current properties to the given object
     */
    void copyTo(T t);
}