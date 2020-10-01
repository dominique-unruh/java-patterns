package de.unruh.javapatterns;

import java.util.NoSuchElementException;
import java.util.Objects;

// DOCUMENT
interface PatternResult<T> {
    @org.jetbrains.annotations.Contract(pure = true)
    boolean isEmpty();
    @org.jetbrains.annotations.Contract(pure = true)
    T get();
}

final class PatternResultSome<T> implements PatternResult<T> {
    private final T value;

    @org.jetbrains.annotations.Contract(pure = true)
    public PatternResultSome(T value) {
        this.value = value;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatternResultSome<?> some = (PatternResultSome<?>) o;
        return Objects.equals(value, some.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

final class PatternResultNone<T> implements PatternResult<T> {
    @Override
    public boolean isEmpty() {
        return true;
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
        return obj instanceof PatternResultNone;
    }
}
