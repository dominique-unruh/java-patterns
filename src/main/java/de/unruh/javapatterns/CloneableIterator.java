package de.unruh.javapatterns;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

// DOCUMENT
public class CloneableIterator<T> implements Iterator<T>, Cloneable {
    @NotNull private StatelessIterator<T> statelessIterator;

    // DOCUMENT
    @NotNull public StatelessIterator<T> getStatelessIterator() {
        return statelessIterator;
    }

    // DOCUMENT
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public CloneableIterator<T> clone() {
        return new CloneableIterator<>(statelessIterator);
    }

    private CloneableIterator(@NotNull StatelessIterator<T> statelessIterator) {
        this.statelessIterator = statelessIterator;
    }

    // DOCUMENT
    @NotNull public static <T> CloneableIterator<T> from(@NotNull StatelessIterator<T> iterator) {
        return new CloneableIterator<>(iterator);
    }

    // DOCUMENT
    @NotNull public static <T> CloneableIterator<T> from(Iterator<T> iterator) {
        return new CloneableIterator<>(StatelessIterator.from(iterator));
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
