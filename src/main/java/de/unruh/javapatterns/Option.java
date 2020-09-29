package de.unruh.javapatterns;

import java.util.NoSuchElementException;

public interface Option<T> {
    boolean nonEmpty();
    T get();
}
final class Some<T> implements Option<T> {
    private final T value;

    public Some(T value) {
        this.value = value;
    }

    @Override
    public boolean nonEmpty() {
        return true;
    }

    @Override
    public T get() {
        return value;
    }
}
final class None<T> implements Option<T> {
    @Override
    public boolean nonEmpty() {
        return false;
    }

    @Override
    public T get() {
        throw new NoSuchElementException();
    }
}
