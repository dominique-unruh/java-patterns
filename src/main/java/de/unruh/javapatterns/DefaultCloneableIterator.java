package de.unruh.javapatterns;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class DefaultCloneableIterator<T> implements CloneableIterator<T> {
    @NotNull
    private StatelessIterator<T> statelessIterator;

    // DOCUMENT
    @NotNull public StatelessIterator<T> getStatelessIterator() {
        return statelessIterator;
    }

    // DOCUMENT
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public DefaultCloneableIterator<T> clone() {
        return new DefaultCloneableIterator<>(statelessIterator);
    }

    private DefaultCloneableIterator(@NotNull StatelessIterator<T> statelessIterator) {
        this.statelessIterator = statelessIterator;
    }

    // DOCUMENT
    @NotNull public static <T> DefaultCloneableIterator<T> from(@NotNull StatelessIterator<T> iterator) {
        return new DefaultCloneableIterator<>(iterator);
    }

    // DOCUMENT
    @NotNull public static <T> DefaultCloneableIterator<T> from(Iterator<T> iterator) {
        return new DefaultCloneableIterator<>(StatelessIterator.from(iterator));
    }

    // DOCUMENT
    @NotNull public static <T> DefaultCloneableIterator<T> fromShared(Iterator<T> iterator) {
        return new DefaultCloneableIterator<>(StatelessIterator.fromShared(iterator));
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
