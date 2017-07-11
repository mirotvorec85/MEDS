package org.meds.database;

import java.util.*;
import java.util.function.Function;

public class MapBiRepository<T> implements BiRepository<T> {

    private Map<Integer, Map<Integer, T>> data;

    private int size;

    public MapBiRepository() {
        this.data = Collections.emptyMap();
    }

    public MapBiRepository(List<T> data, Function<T, Integer> firstKeyGetter, Function<T, Integer> secondKeyGetter) {
        setData(data, firstKeyGetter, secondKeyGetter);
    }

    public void setData(List<T> data, Function<T, Integer> firstKeyGetter, Function<T, Integer> secondKeyGetter) {
        this.data = new HashMap<>(data.size());
        this.size = 0;
        for(T element : data) {
            Map<Integer, T> firstItems = this.data.get(firstKeyGetter.apply(element));
            if (firstItems == null) {
                firstItems = new HashMap<>();
                this.data.put(firstKeyGetter.apply(element), firstItems);
            }
            ++this.size;
            firstItems.put(secondKeyGetter.apply(element), element);
        }
    }

    @Override
    public T get(int firstKey, int secondKey) {
        T result = null;
        Map<Integer, T> items = this.data.get(firstKey);
        if (items != null) {
            result = items.get(secondKey);
        }
        return result;
    }

    @Override
    public Collection<T> get(int firstKey) {
        Collection<T> result = Collections.emptyList();
        Map<Integer, T> items = this.data.get(firstKey);
        if (items != null) {
            result = new ArrayList<>(items.values());
        }
        return result;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public int sizeFirst() {
        return this.data.size();
    }

    @Override
    public int sizeSecond(int firstKey) {
        int result = 0;
        Map<Integer, T> items = this.data.get(firstKey);
        if (items != null) {
            result = items.size();
        }
        return result;
    }
}
