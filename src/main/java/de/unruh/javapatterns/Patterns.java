package de.unruh.javapatterns;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Predicate;

// DOCUMENT priority
// DOCUMENT, mention (somewhere): can access captures already in match, can fail match in action
// TODO can we create a merged class for static import from Patterns and Match?
public final class Patterns {
    @Contract(pure = true)
    private Patterns() {}

    @NotNull
    @Contract(pure = true, value = "_ -> new")
    public static <T> Pattern<T> Is(@Nullable T expected) {
        return new Pattern<>() {
            @Override
            protected void apply(@NotNull MatchManager mgr, @Nullable T value) throws PatternMatchReject {
                if (!Objects.equals(expected,value)) reject();
            }

            @Override
            public String toString() {
                return "=" + expected;
            }
        };
    }

    @NotNull
    @Contract(pure = true, value = "_ -> new")
    public static <T> Pattern<T> Is(@NotNull PatternSupplier<T> expected) {
        return new Pattern<>() {
            @Override
            protected void apply(@NotNull MatchManager mgr, @Nullable T value) throws PatternMatchReject {
                if (!Objects.equals(expected.get(), value)) reject();
            }

            @Override
            public String toString() {
                return "=" + expected;
            }
        };
    }

    @NotNull
    @Contract(pure = true, value = "_ -> new")
    public static <T> Pattern<T> Is(@NotNull Capture<T> expected) {
        return Is(expected::v);
    }

    @NotNull
    public static final Pattern<Object> Any = new Pattern<>() {
        @Override
        protected void apply(@NotNull MatchManager mgr, @Nullable Object value) {
        }

        @Override
        public String toString() {
            return "_";
        }
    };

    @NotNull
    public static final Pattern<Object> Null = new Pattern<>() {
        @Override
        protected void apply(@NotNull MatchManager mgr, @Nullable Object value) throws PatternMatchReject {
            if (value != null) reject();
        }

        @Override
        public String toString() {
            return "null";
        }
    };

    @NotNull
    @Contract(pure = true, value = "_ -> new")
    public static <T> Pattern<T> NotNull(@NotNull Pattern<? super T> pattern) {
        return new Pattern<>() {
            @Override
            protected void apply(@NotNull MatchManager mgr, @Nullable T value) throws PatternMatchReject {
                if (value == null) reject();
                pattern.apply(mgr, value);
            }

            @Override
            public String toString() {
                return "null";
            }
        };
    }

    @NotNull
    @Contract(pure = true, value = "_ -> new")
    @SafeVarargs
    public static <T> Pattern<T> And(@NotNull Pattern<? super T>... patterns) {
        return new Pattern<>() {
            @Override
            protected void apply(@NotNull MatchManager mgr, @Nullable T value) throws PatternMatchReject {
                for (Pattern<? super T> pattern : patterns)
                    pattern.apply(mgr, value);
            }

            @Override
            public String toString() {
                StringJoiner joiner = new StringJoiner(", ");
                for (Pattern<?> pattern : patterns)
                    joiner.add(pattern.toString());
                return "And(" + joiner + ")";
            }
        };
    }

    @NotNull
    @Contract(pure = true, value = "_ -> new")
    @SafeVarargs
    public static <T> Pattern<T> Or(@NotNull Pattern<? super T>... patterns) {
        return new Pattern<T>() {
            @Override
            protected void apply(@NotNull MatchManager mgr, @Nullable T value) throws PatternMatchReject {
                if (patterns.length == 0) reject();
                for (int i=0; i<patterns.length-1; i++) {
                    Pattern<? super T> pattern = patterns[i];
                    if (mgr.excursion(() -> pattern.apply(mgr, value))) return;
                }
                patterns[patterns.length-1].apply(mgr, value);
            }

            @Override
            public String toString() {
                StringJoiner joiner = new StringJoiner(", ");
                for (Pattern<?> pattern : patterns)
                    joiner.add(pattern.toString());
                return "Or(" + joiner + ")";
            }
        };
    }

    @NotNull
    @Contract(pure = true, value = "_, _ -> new")
    public static <U> Pattern<Object> Instance(@NotNull Class<U> clazz, @NotNull Pattern<? super U> pattern) {
        return new Pattern<>() {
            @Override
            protected void apply(@NotNull MatchManager mgr, @Nullable Object value) throws PatternMatchReject {
                if (!clazz.isInstance(value)) reject();
                //noinspection unchecked
                pattern.apply(mgr, (U)value);
                // we could use Class.cast(value) instead of (U)value, but that probably just duplicates the dynamic type check
            }

            @Override
            public String toString() {
                return "Instance("+clazz.getSimpleName()+","+pattern+")";
            }
        };
    }

    public abstract static class Instance<U> extends Pattern<U> {
        private final Pattern<Object> instancePattern;
        private final Pattern<? super U> pattern;
        private final Type typeU;
        @SuppressWarnings("unchecked")
        @Contract(pure = true)
        public Instance(@NotNull Pattern<? super U> pattern) {
            // Based on https://stackoverflow.com/a/64138964/2646248, https://www.baeldung.com/java-super-type-tokens
            if (getClass().getSuperclass() != Instance.class)
                throw new InvalidPatternMatch("A subclass of a subclass of "+Instance.class+" was created. This is not the intended use.");
            Type superclass = getClass().getGenericSuperclass();
            typeU = ((ParameterizedType) superclass).getActualTypeArguments()[0];
            Class<U> clazz;
            if (typeU instanceof ParameterizedType)
                clazz = (Class<U>) ((ParameterizedType) typeU).getRawType();
            else if (typeU instanceof Class)
                clazz = (Class<U>) typeU;
            else
                throw new InvalidPatternMatch("Type parameter of "+Instance.class.getName()+" must be a class, not "+typeU+" "+typeU.getClass());
            instancePattern = Instance(clazz, pattern);
            this.pattern = pattern;
        };

        @Override
        protected void apply(@NotNull MatchManager mgr, @Nullable U value) throws PatternMatchReject {
            instancePattern.apply(mgr, value);
        }

        @Override
        public String toString() {
            return "("+pattern+" : "+typeU+")";
        }
    }

    @NotNull
    @Contract(pure = true, value = "_ -> new")
    public static <T> Pattern<T> Pred(@NotNull Predicate<? super T> predicate) {
        return new Pattern<T>() {
            @Override
            protected void apply(@NotNull MatchManager mgr, @Nullable T value) throws PatternMatchReject {
                if (!predicate.test(value)) reject();
            }

            @Override
            public String toString() {
                return "Pred(...)";
            }
        };
    }

    @NotNull
    @Contract(pure = true, value = "_ -> new")
    public static <T> Pattern<T> NoMatch(@NotNull Pattern<? super T> pattern) {
        return new Pattern<T>() {
            @Override
            protected void apply(@NotNull MatchManager mgr, @Nullable T value) throws PatternMatchReject {
                boolean matched = mgr.excursion(() -> pattern.apply(mgr, value));
                if (matched) reject();
            }

            @Override
            public String toString() {
                return "NoMatch("+pattern+")";
            }
        };
    }

    @NotNull
    @Contract(pure = true, value = "_ -> new")
    @SafeVarargs
    public static <T> Pattern<T[]> Array(@NotNull Pattern<? super T> ... patterns) {
        return new Pattern<T[]>() {
            @Override
            protected void apply(@NotNull MatchManager mgr, T @Nullable [] value) throws PatternMatchReject {
                if (value == null) reject();
                if (value.length != patterns.length) reject();
                for (int i=0; i<patterns.length; i++)
                    patterns[i].apply(mgr, value[i]);
            }

            @Override
            public String toString() {
                StringJoiner joiner = new StringJoiner(", ");
                for (Pattern<?> pattern : patterns)
                    joiner.add(pattern.toString());
                return "Array(" + joiner + ")";
            }
        };
    }
}
