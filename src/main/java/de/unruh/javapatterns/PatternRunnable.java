package de.unruh.javapatterns;

@FunctionalInterface
public interface PatternRunnable {
    void run() throws PatternMatchReject;
}
