package de.unruh.javapatterns;

// DOCUMENT
public final class PatternMatchReject extends Exception {
    @Override
    @org.jetbrains.annotations.Contract(pure = true, value = "-> this")
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
    @org.jetbrains.annotations.Contract(pure = true)
    PatternMatchReject() { super(); }
}
