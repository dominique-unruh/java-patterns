package de.unruh.javapatterns;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import scala.None;
import scala.Option;
import scala.Some;

// DOCUMENT
// DOCUMENT reference from README etc.
// DOCUMENT crosslink API docs if necessary
public final class ScalaPatterns {
    private ScalaPatterns() {}

    // TODO test case
    public <T> Pattern<Option<T>> Some(Pattern<? super T> pattern) {
        return new Pattern<Option<T>>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Option<T> value) throws PatternMatchReject {
                if (value instanceof Some)
                    pattern.apply(mgr, value.get());
                else
                    reject();
            }

            @Override
            public String toString() {
                return null;
            }
        };
    }

    // TODO test case
    public Pattern<Option<Object>> None = new Pattern<Option<Object>>() {
        @Override
        public void apply(@NotNull MatchManager mgr, @Nullable Option<Object> value) throws PatternMatchReject {
            if (value == null) reject();
            if (value.nonEmpty()) reject();
        }

        @Override
        public String toString() {
            return null;
        }
    };
}
