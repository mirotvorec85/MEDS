package org.meds.database;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MapRepository<T> implements Repository<T> {

    private Map<Integer, T> data;

    public MapRepository(List<T> data, Function<T, Integer> keyGetter) {
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
