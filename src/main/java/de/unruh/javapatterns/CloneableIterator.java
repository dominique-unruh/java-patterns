package de.unruh.javapatterns;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.stream.Stream;

// DOCUMENT
public interface CloneableIterator<T> extends Iterator<T>, Cloneable {
    CloneableIterator<T> clone();

    // DOCUMENT
    @NotNull
    static <T> CloneableIterator<T> from(Iterator<T> iterator) {
        if (iterator instanceof CloneableIterator)
            return ((CloneableIterator<T>) iterator).clone();
        return DefaultCloneableIterator.from(iterator);
    }

    // DOCUMENT
    @NotNull
    static <T> CloneableIterator<T> fromShared(Iterator<T> iterator) {
        if (iterator instanceof CloneableIterator)
            return ((CloneableIterator<T>) iterator).clone();
        return DefaultCloneableIterator.fromShared(iterator);
    }


    // DOCUMENT
    @NotNull
    static <T> CloneableIterator<T> from(Stream<T> stream) {
        return DefaultCloneableIterator.from(stream);
    }

    // DOCUMENT
    @NotNull
    static <T> CloneableIterator<T> fromShared(Stream<T> stream) {
        return DefaultCloneableIterator.fromShared(stream);
    }
}
