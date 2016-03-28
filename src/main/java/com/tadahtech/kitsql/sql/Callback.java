package com.tadahtech.kitsql.sql;

/**
 * @author Timothy Andis (TadahTech) on 3/27/2016.
 */
public class Callback<T> {

    /**
     * Simple Utility Class
     */

    private T t;

    public T get() {
        return t;
    }

    public void call(T t) {
        this.t = t;
    }
}
