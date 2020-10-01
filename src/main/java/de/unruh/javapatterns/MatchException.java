package de.unruh.javapatterns;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public class MatchException extends Exception {
    @Nullable
    public final Object value;

    @Contract(pure = true)
    public MatchException(@Nullable Object value) {
        super("Match failure");
        this.value = value;
    }
}
