package de.unruh.javapatterns;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// DOCUMENT
final public class Capture<T> extends Pattern<T> {
    private final String name;

    @Override
    public String toString() {
        return name;
    }

    @org.jetbrains.annotations.Contract(pure = true)
    public Capture(@NotNull String name) {
        this.name = name;
    }

    private T value;
    private boolean assigned = false;

    void clear() {
//        out.println("Resetting "+name+" "+value+" "+assigned);
        assigned = false;
    }

    @org.jetbrains.annotations.Contract(pure = true)
    public T v() {
//        out.println("Reading "+name+" "+value+" "+assigned);
        if (!assigned)
            throw new InvalidPatternMatch("Reading undefined capture variable " + name);
        return value;
    }

    @Override
    public void apply(@NotNull MatchManager mgr, @Nullable T value) {
//        out.println("Assigning "+name+" "+value+" "+assigned);
        if (assigned)
            throw new InvalidPatternMatch("Re-assigned " + name + " in pattern match");
        mgr.assigned(this);
        assigned = true;
        this.value = value;
    }
}
