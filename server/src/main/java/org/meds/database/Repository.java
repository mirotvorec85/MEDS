package org.meds.database;

public interface Repository<T> extends Iterable<T> {

    T get(int key);

    boolean contains(int key);

    int size();
}
