package org.meds.database;

import java.util.*;
import java.util.function.Function;

public class MapRepository<T> implements Repository<T> {

    private Map<Integer, T> data;

    public MapRepository() {
        this.data = Collections.emptyMap();
    }

    public MapRepository(List<T> data, Function<T, Integer> keyGetter) {
        setData(data, keyGetter);
    }

    public void setData(List<T> data, Function<T, Integer> keyGetter) {
        this.data = new HashMap<>(data.size());
        for(T element : data) {
            this.data.put(keyGetter.apply(element), element);
        }
    }

    @Override
    public T get(int key) {
        return data.get(key);
    }

    @Override
    public boolean contains(int key) {
        return this.data.containsKey(key);
    }

    @Override
    public int size() {
        return this.data.size();
    }

    @Override
    public Iterator<T> iterator() {
        return data.values().iterator();
    }
}
