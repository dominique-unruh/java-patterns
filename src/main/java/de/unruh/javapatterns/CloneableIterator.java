package de.unruh.javapatterns;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

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
}
