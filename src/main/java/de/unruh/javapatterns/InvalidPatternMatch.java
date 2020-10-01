package de.unruh.javapatterns;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

// DOCUMENT
public class InvalidPatternMatch extends RuntimeException {
    @Contract(pure = true)
    public InvalidPatternMatch(@NotNull String msg) {
        super(msg);
    }
}
