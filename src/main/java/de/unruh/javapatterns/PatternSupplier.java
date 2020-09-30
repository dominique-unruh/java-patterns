package de.unruh.javapatterns;

public interface PatternSupplier<T> {
    T get() throws PatternMatchReject;
}
