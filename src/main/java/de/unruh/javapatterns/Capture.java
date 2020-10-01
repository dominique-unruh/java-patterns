package de.unruh.javapatterns;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A capturing variable in a pattern match.<p>
 *
 * A {@link Capture}{@code <T>} is a pattern that matches everything and, when
 * matched, stores the value that it matched. Within a single pattern match,
 * the capture must not be matched twice. (It is, however, possible to have the
 * same capture occur in mutually exclusive parts of the pattern, see, e.g.,
 * {@link Patterns#Or}.)<p>
 *
 * The value that was assigned to the capture {@code x} can be read using
 * <code>x.{@link #v()}</code>. This is possible both in the action of a pattern match
 * (see {@link Case}) and in the pattern itself. This makes it possible to make
 * parts of the pattern depend on values that have been captured already. (E.g.,
 * <code>{@link Patterns#Array}(x, {@link Patterns#Is}(x))</code> matches only
 * arrays with two identical entries.)<p>
 *
 * Captures need to be declared before the pattern match with a given type, e.g.,
 * <pre>
 * Capture&lt;String> x = new Capture<>("x");
 * ...
 * match(value, ... patterns using x ...)
 * </pre>
 * However, the value of the capture is local to one case of the pattern match.
 * (So, logically, a capture is a local variable within the case.) A capture can
 * be used in several cases in the same match, or in different matches, but it is
 * not thread-safe (i.e., not in concurrent matches).
 *
 * @param <T> The type of the value captured by this capture variable
 */
final public class Capture<T> extends Pattern<T> {
    private final String name;

    @Override
    public String toString() {
        return name;
    }

    /** Creates a new capture variable.
     * @param name Name of the capture. Used only for informative purposes
     *             (printing patterns, error messages). It is recommended
     *             that this is the name of the variable holds this capture.
     */
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

    /** The current value of the capture.
     * @throws InvalidPatternMatch if the capture has not been assigned in the current pattern
     */
    @org.jetbrains.annotations.Contract(pure = true)
    public T v() {
//        out.println("Reading "+name+" "+value+" "+assigned);
        if (!assigned)
            throw new InvalidPatternMatch("Reading undefined capture variable " + name);
        return value;
    }

    // TODO Make protected?
    @Override
    public void apply(@NotNull MatchManager mgr, @Nullable T value) {
        if (assigned)
            throw new InvalidPatternMatch("Re-assigned " + name + " in pattern match");
        mgr.assigned(this);
        assigned = true;
        this.value = value;
    }
}
