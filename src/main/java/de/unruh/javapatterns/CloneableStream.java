package de.unruh.javapatterns;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

// DOCUMENT
public interface CloneableStream<T> extends Stream<T>, Cloneable {
    CloneableStream<T> clone();

    // DOCUMENT
    @NotNull
    static <T> CloneableStream<T> from(Stream<T> iterator) {
        if (iterator instanceof CloneableStream)
            return ((CloneableStream<T>) iterator).clone();
        return DefaultCloneableStream.from(iterator);
    }

    // DOCUMENT
    @NotNull
    static <T> CloneableStream<T> fromShared(Stream<T> iterator) {
        if (iterator instanceof CloneableStream)
            return ((CloneableStream<T>) iterator).clone();
        return DefaultCloneableStream.fromShared(iterator);
    }
}
