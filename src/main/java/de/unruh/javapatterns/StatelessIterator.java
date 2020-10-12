package de.unruh.javapatterns;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.WeakHashMap;

// DOCUMENT
public class StatelessIterator<T> {
    // TODO: Use a WeakHashMap based on reference equality!
    private final static WeakHashMap<Object, StatelessIterator<?>> iterators =
            new WeakHashMap<Object, StatelessIterator<?>>();

    // DOCUMENT
    @NotNull public static <T> StatelessIterator<T> from(@NotNull Iterator<T> iterator) {
        if (iterator instanceof StatelessIterator) //noinspection unchecked
            return (StatelessIterator<T>) iterator;
        synchronized (iterators) {
            final StatelessIterator<?> existingInstance = iterators.get(iterator);
            if (existingInstance == null) {
                StatelessIterator<T> instance = new StatelessIterator<>(iterator);
                iterators.put(iterator, instance);
                return instance;
            } else
                //noinspection unchecked
                return (StatelessIterator<T>) existingInstance;
        }
    }

    private static enum State { uninitialized, empty, nonEmpty }

    private State state = State.uninitialized;
    private Iterator<T> iterator;
    private T head = null;
    private StatelessIterator<T> tail = null;

    private StatelessIterator(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    private void initialize() {
        synchronized (this) {
            if (state==State.uninitialized) {
                if (iterator.hasNext()) {
                    state = State.empty;
                    iterator = null;
                } else {
                    state = State.nonEmpty;
                    head = iterator.next();
                    tail = new StatelessIterator<>(iterator);
                }
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
