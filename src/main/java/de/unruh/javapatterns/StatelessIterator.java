package de.unruh.javapatterns;

import com.google.common.collect.MapMaker;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentMap;

// DOCUMENT
public class StatelessIterator<T> {
    @NotNull private final static ConcurrentMap<Object, StatelessIterator<?>> iterators =
            new MapMaker().weakKeys().concurrencyLevel(1).makeMap();

    // DOCUMENT
    @NotNull public static <T> StatelessIterator<T> from(@NotNull Iterator<T> iterator) {
        if (iterator instanceof CloneableIterator)
            return ((CloneableIterator<T>)iterator).getStatelessIterator();
        //noinspection unchecked
        return (StatelessIterator<T>) iterators.computeIfAbsent(iterator, i -> new StatelessIterator<>(iterator));
    }

    private enum State { uninitialized, empty, nonEmpty }

    private volatile State state = State.uninitialized;
    private Iterator<T> iterator;
    private T head = null;
    private StatelessIterator<T> tail = null;

    private StatelessIterator(@NotNull Iterator<T> iterator) {
        this.iterator = iterator;
    }

    private synchronized void initialize() {
        if (state==State.uninitialized) {
            if (iterator.hasNext()) {
                head = iterator.next();
                tail = new StatelessIterator<>(iterator);
                state = State.nonEmpty;
            } else {
                iterator = null;
                state = State.empty;
            }
        }
    }

    // DOCUMENT
    public T getHead() {
        if (state==State.uninitialized)
            initialize();
        if (state==State.empty)
            throw new NoSuchElementException();
        return head;
    }

    // DOCUMENT
    @NotNull public StatelessIterator<T> getTail() {
        if (state==State.uninitialized)
            initialize();
        if (state==State.empty)
            throw new NoSuchElementException();
        return tail;
    }

    // DOCUMENT
    public boolean nonEmpty() {
        if (state==State.uninitialized)
            initialize();
        return state == State.nonEmpty;
    }
}
