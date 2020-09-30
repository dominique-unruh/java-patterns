package de.unruh.javapatterns;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.StringJoiner;
import java.util.function.Predicate;

// DOCUMENT, mention (somewhere): can access captures already in match, can fail match in action
// TODO can we create a merged class for static import from Patterns and Match?
public final class Patterns {
    @org.jetbrains.annotations.Contract(pure = true)
    private Patterns() {}

    public static <T> Pattern<T> Is(T expected) {
        return new Pattern<>() {
            @Override
            public void apply(MatchManager mgr, T value) throws PatternMatchReject {
                if (!expected.equals(value)) reject();
            }

            @Override
            public String toString() {
                return "=" + expected;
            }
        };
    }

    public static <T> Pattern<T> Is(PatternSupplier<T> expected) {
        return new Pattern<>() {
            @Override
            public void apply(MatchManager mgr, T value) throws PatternMatchReject {
                if (!expected.get().equals(value)) reject();
            }

            @Override
            public String toString() {
                return "=" + expected;
            }
        };
    }

    public static <T> Pattern<T> Is(Capture<T> expected) {
        return Is(expected::v);
    }

    public static final Pattern<Object> Any = new Pattern<>() {
        @Override
        public void apply(MatchManager mgr, Object value) {
        }

        @Override
        public String toString() {
            return "_";
        }
    };

    public static final Pattern<Object> Null = new Pattern<>() {
        @Override
        public void apply(MatchManager mgr, Object value) throws PatternMatchReject {
            if (value != null) reject();
        }

        @Override
        public String toString() {
            return "null";
        }
    };

    public static <T> Pattern<T> NotNull(Pattern<? super T> pattern) {
        return new Pattern<>() {
            @Override
            public void apply(MatchManager mgr, T value) throws PatternMatchReject {
                if (value == null) reject();
                pattern.apply(mgr, value);
            }

            @Override
            public String toString() {
                return "null";
            }
        };
    }

    @SafeVarargs
    public static <T> Pattern<T> And(Pattern<? super T>... patterns) {
        return new Pattern<>() {
            @Override
            public void apply(MatchManager mgr, T value) throws PatternMatchReject {
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

    @SafeVarargs
    public static <T> Pattern<T> Or(Pattern<? super T>... patterns) {
        return new Pattern<T>() {
            @Override
            public void apply(MatchManager mgr, T value) throws PatternMatchReject {
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

    public static <U> Pattern<Object> Instance(Class<U> clazz, Pattern<? super U> pattern) {
        return new Pattern<>() {
            @Override
            public void apply(MatchManager mgr, Object value) throws PatternMatchReject {
                U castValue = null;
                try {
                    // TODO: use clazz.isInstance to avoid creation of ClassCastException (stack trace construction)
                    castValue = clazz.cast(value);
                } catch (ClassCastException e) {
                    reject();
                }
                pattern.apply(mgr,castValue);
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
        public Instance(Pattern<? super U> pattern) {
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
        public void apply(MatchManager mgr, U value) throws PatternMatchReject {
            instancePattern.apply(mgr, value);
        }

        @Override
        public String toString() {
            return "("+pattern+" : "+typeU+")";
        }
    }

    public static <T> Pattern<T> Pred(Predicate<? super T> predicate) {
        return new Pattern<T>() {
            @Override
            public void apply(MatchManager mgr, T value) throws PatternMatchReject {
                if (!predicate.test(value)) reject();
            }

            @Override
            public String toString() {
                return "Pred(...)";
            }
        };
    }

    public static <T> Pattern<T> NoMatch(Pattern<? super T> pattern) {
        return new Pattern<T>() {
            @Override
            public void apply(MatchManager mgr, T value) throws PatternMatchReject {
                boolean matched = mgr.excursion(() -> pattern.apply(mgr, value));
                if (matched) reject();
            }

            @Override
            public String toString() {
                return "NoMatch("+pattern+")";
            }
        };
    }

    @SafeVarargs
    public static <T> Pattern<T[]> Array(Pattern<? super T> ... patterns) {
        return new Pattern<T[]>() {
            @Override
            public void apply(MatchManager mgr, T[] value) throws PatternMatchReject {
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