package de.unruh.javapatterns;

import org.jetbrains.annotations.NotNull;

public class InvalidPatternMatch extends RuntimeException {
    @org.jetbrains.annotations.Contract(pure = true)
    public InvalidPatternMatch(@NotNull String msg) {
        super(msg);
    }
}
