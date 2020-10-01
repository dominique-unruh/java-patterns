package de.unruh.javapatterns;

import org.jetbrains.annotations.Nullable;

// DOCUMENT
public class MatchException extends Exception {
    @Nullable
    public final Object value;

    @org.jetbrains.annotations.Contract(pure = true)
    public MatchException(@Nullable Object value) {
        super("Match failure");
        this.value = value;
    }
}
