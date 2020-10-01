package de.unruh.javapatterns;

import org.jetbrains.annotations.Contract;

// DOCUMENT
public final class PatternMatchReject extends Exception {
    @Override
    @Contract(pure = true, value = "-> this")
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
    @Contract(pure = true)
    PatternMatchReject() { super(); }
}
