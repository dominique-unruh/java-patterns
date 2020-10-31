package de.unruh.javapatterns;

import org.jetbrains.annotations.Contract;

/** This exception is intended to signal a pattern match failure. (Inside a pattern match using
 * {@link Match#match(Object, Case[]) Match.match(...)}.)
 * It should only be thrown by the {@link Pattern#apply apply} function of a {@link Pattern},
 * and can be thrown using {@link Pattern#reject()}.<p>
 *
 * This exception should not be caught inside a pattern match because the state of the captures
 * may be undefined then. Instead, to recover from a match failure of a subpattern, use {@link MatchManager#protectedBlock}.
 */
public final class PatternMatchReject extends Exception {
    @Override
    @Contract(pure = true, value = "-> this")
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
    @Contract(pure = true)
    PatternMatchReject() { super(); }
}
