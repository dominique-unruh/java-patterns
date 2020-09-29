package de.unruh.javapatterns;

public class MatchException extends Exception {
    public final Object value;

    public MatchException(Object value) {
        super("Match failure");
        this.value = value;
    }
}
