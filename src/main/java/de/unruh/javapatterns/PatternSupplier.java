package de.unruh.javapatterns;

import org.jetbrains.annotations.Nullable;

// DOCUMENT
@FunctionalInterface
public interface PatternSupplier<T> {
    @Nullable
    T get() throws PatternMatchReject;
}
