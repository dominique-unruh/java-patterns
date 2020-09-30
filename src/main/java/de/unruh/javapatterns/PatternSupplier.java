package de.unruh.javapatterns;

@FunctionalInterface
public interface PatternSupplier<T> {
    T get() throws PatternMatchReject;
}
