package de.unruh.javapatterns;

// DOCUMENT
@FunctionalInterface
public interface MatchRunnable<Exn extends Throwable> {
    void run() throws Exn, PatternMatchReject;
}
