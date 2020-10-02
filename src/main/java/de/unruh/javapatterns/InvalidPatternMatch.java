package de.unruh.javapatterns;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class InvalidPatternMatch extends RuntimeException {
    @Contract(pure = true)
    public InvalidPatternMatch(@NotNull String msg) {
        super(msg);
    }
}
