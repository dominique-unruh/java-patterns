package de.unruh.javapatterns;

import java.util.NoSuchElementException;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Some<?> some = (Some<?>) o;
        return Objects.equals(value, some.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
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

    @Override
    public int hashCode() {
        return 347249645;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof None;
    }
}
