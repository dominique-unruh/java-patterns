package de.unruh.javapatterns;

import java.util.Iterator;

// DOCUMENT
public class CloneableIterator<T> implements Iterator<T>, Cloneable {
    private StatelessIterator<T> statelessIterator;

    // DOCUMENT
    @Override
    protected CloneableIterator<T> clone() throws CloneNotSupportedException {
        //noinspection unchecked
        return (CloneableIterator<T>) super.clone();
    }

    // DOCUMENT
    public CloneableIterator(StatelessIterator<T> statelessIterator) {
        this.statelessIterator = statelessIterator;
    }

    // DOCUMENT
    public CloneableIterator(CloneableIterator<T> cloneableIterator) {
        this.statelessIterator = cloneableIterator.statelessIterator;
    }

    // DOCUMENT
    public CloneableIterator(Iterator<T> iterator) {
        statelessIterator = StatelessIterator.from(iterator);
    }

    @Override
    public boolean hasNext() {
        return statelessIterator.nonEmpty();
    }

    @Override
    public T next() {
        T value = statelessIterator.getHead();
        statelessIterator = statelessIterator.getTail();
        return value;
    }
}
