package de.unruh.javapatterns;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// DOCUMENT PRIORITY
// TODO: Should this be an interface?
public abstract class Pattern<T> {
    protected abstract void apply(@NotNull MatchManager mgr, @Nullable T value) throws PatternMatchReject;

    // TODO: keep?
    @org.jetbrains.annotations.Contract(pure = true, value = "-> this")
    public final <U extends T> Pattern<U> contravariance() {
        //noinspection unchecked
        return (Pattern<U>) this;
    }

    @Override
    @org.jetbrains.annotations.Contract(pure = true)
    public abstract String toString();

    @org.jetbrains.annotations.Contract(pure = true, value = "-> fail")
    public static void reject() throws PatternMatchReject {
        throw new PatternMatchReject();
    }
}
