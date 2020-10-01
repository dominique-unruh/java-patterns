package de.unruh.javapatterns;

// DOCUMENT
@FunctionalInterface
public interface PatternRunnable {
    void run() throws PatternMatchReject;
}
