package de.unruh.javapatterns;

import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface PatternSupplier<T> {
    @Nullable
    T get() throws PatternMatchReject;
}
