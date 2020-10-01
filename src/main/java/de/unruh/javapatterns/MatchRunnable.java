package de.unruh.javapatterns;

@FunctionalInterface
public interface MatchRunnable<Exn extends Throwable> {
    void run() throws Exn, PatternMatchReject;
}
