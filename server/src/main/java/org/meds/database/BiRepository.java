package org.meds.database;

import java.util.Collection;

public interface BiRepository<T> {

    T get(int firstKey, int secondKey);

    Collection<T> get(int firstKey);

    int size();

    int sizeFirst();

    int sizeSecond(int firstKey);
}
