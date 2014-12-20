package org.meds.util;

import java.util.Iterator;

public class ReadOnlyIterator<E> implements Iterator<E> {

    private final Iterator<E> iterator;

    public ReadOnlyIterator(Iterator<E> iterator) {
        this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    @Override
    public E next() {
        return this.iterator.next();
    }

    /**
     * Throws <code>UnsupportedOperationException</code>.
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
