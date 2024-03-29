package de.unruh.javapatterns.statelessiterators;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.stream.Stream;

/** An implementation of a {@link CloneableIterator} that wraps a {@link StatelessIterator}.
 * A {@link StatelessIterator} can be constructed from any {@link Iterator},
 * thus this class allows to make arbitrary iterators cloneable.
 *
 * @param <T> the type of the elements in the iterator
 */
// DOCUMENT GC rules
// DOCUMENT from/fromShared
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
    @NotNull public static <T> DefaultCloneableIterator<T> from(Stream<T> stream) {
        return new DefaultCloneableIterator<>(StatelessIterator.from(stream));
    }

    // DOCUMENT
    @NotNull public static <T> DefaultCloneableIterator<T> fromShared(Iterator<T> iterator) {
        return new DefaultCloneableIterator<>(StatelessIterator.fromShared(iterator));
    }

    // DOCUMENT
    @NotNull public static <T> DefaultCloneableIterator<T> fromShared(Stream<T> stream) {
        return new DefaultCloneableIterator<>(StatelessIterator.fromShared(stream));
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
