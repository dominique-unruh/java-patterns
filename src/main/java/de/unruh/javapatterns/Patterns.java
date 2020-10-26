package de.unruh.javapatterns;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

// TODO Java-streams

/**
 * This class contains static methods for constructing a number of different patterns. <p>
 *
 * (This class itself cannot be instantiated.)<p>
 *
 * Throughout the documentation of the patterns in this class, we refer to the value that
 * is matched against the pattern simply the "matched value". (Or "matched array", "matched collection",
 * "matched iterator", etc. if we want to highlight its type.)
 */
// TODO reference ScalaPatterns
public final class Patterns {
    @Contract(pure = true)
    private Patterns() {}

    /** Pattern that matches if the matched value equals {@code expected}.<p>
     *
     * Equality is tested using {@link Objects#equals}.
     * If a different equality test is required, use {@link #Is(Predicate)}.<p>
     *
     * Since {@code expected} is executed during pattern construction,
     * {@code expected} cannot depend on the value of capture variables.
     * Use {@link #Is(Supplier)} if delayed execution is desired. (Or {@link #Is(Capture)}
     * to match a single captured value.)
     *
     * @param expected the value that the matched value is compared to
     * @param <T> type of the matched value
     * @return the pattern
     */
    @NotNull
    @Contract(pure = true, value = "_ -> new")
    public static <T> Pattern<T> Is(@Nullable T expected) {
        return new Pattern<T>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable T value) throws PatternMatchReject {
                if (!Objects.equals(expected,value)) reject();
            }

            @Override
            public String toString() {
                return "=" + expected;
            }
        };
    }

    /** Pattern that matches if the matched value equals the value computed by {@code expected}.<p>
     *
     * Equality is tested using {@link Objects#equals}.
     * If a different equality test is required, use {@link #Is(Predicate)}.
     *
     * @param expected lambda expression computing the expected value
     * @param <T> type of the matched value
     * @return the pattern
     */
    @NotNull
    @Contract(pure = true, value = "_ -> new")
    public static <T> Pattern<T> Is(@NotNull Supplier<T> expected) {
        return new Pattern<T>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable T value) throws PatternMatchReject {
                if (!Objects.equals(expected.get(), value)) reject();
            }

            @Override
            public String toString() {
                return "=" + expected;
            }
        };
    }

    /** Pattern that matches if the matched value equals the value in the captured variable
     * {@code expected}.<p>
     *
     * Equality is tested using {@link Objects#equals}.
     * If a different equality test is required, use {@link #Is(Predicate)}.
     *
     * @param expected lambda expression computing the expected value
     * @param <T> type of the matched value
     * @return the pattern
     */
    @NotNull
    @Contract(pure = true, value = "_ -> new")
    public static <T> Pattern<T> Is(@NotNull Capture<T> expected) {
        return Is(expected::v);
    }

    /** Pattern that matches if the matched value satisfies a predicate.
     *
     * @param predicate lambda expression testing whether the matched value should be accepted
     * @param <T> type of the matched value
     * @return the pattern
     */
    @NotNull
    @Contract(pure = true, value = "_ -> new")
    public static <T> Pattern<T> Is(@NotNull Predicate<? super T> predicate) {
        return new Pattern<T>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable T value) throws PatternMatchReject {
                if (!predicate.test(value)) reject();
            }

            @Override
            public String toString() {
                return "Pred(...)";
            }
        };
    }

    /** Pattern that matches everything (including {@code null}). */
    @NotNull
    public static final Pattern<Object> Any = new Pattern<Object>() {
        @Override
        public void apply(@NotNull MatchManager mgr, @Nullable Object value) {
        }

        @Override
        public String toString() {
            return "_";
        }
    };

    /** Pattern that matches only {@code null}. */
    @NotNull
    public static final Pattern<Object> Null = new Pattern<Object>() {
        @Override
        public void apply(@NotNull MatchManager mgr, @Nullable Object value) throws PatternMatchReject {
            if (value != null) reject();
        }

        @Override
        public String toString() {
            return "null";
        }
    };

    /** Pattern that matches non-null values.<p>
     *
     * This pattern succeeds if the matched value is not {@code null},
     * and the subpattern {@code pattern} matches the matched value.<p>
     *
     * Typical use cases would be <code>NotNull({@link #Any})</code> or <code>NotNull(x)</code>
     * for a capture variable {@code x}. Both forms would match any non-null value, and the latter
     * assigns it to the capture {@code x}.
     *
     * @param pattern the subpattern that also needs to match the matched value
     * @param <T> type of the matched value
     * @return the pattern that matched only non-null values
     **/
    @NotNull
    @Contract(pure = true, value = "_ -> new")
    public static <T> Pattern<T> NotNull(@NotNull Pattern<@NotNull ? super T> pattern) {
        return new Pattern<T>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable T value) throws PatternMatchReject {
                if (value == null) reject();
                pattern.apply(mgr, value);
            }

            @Override
            public String toString() {
                return "null";
            }
        };
    }


    /** Pattern that combined several subpatterns that all need to be match.<p>
     *
     * This pattern matches if all subpatterns in {@code patterns} match the matched value.<p>
     *
     * All captures assigned by the subpatterns will be assigned by this pattern.
     * Consequently, the subpatterns must not assign the same captures.
     *
     * @param patterns subpatterns that all should match
     * @param <T> type of the matched value
     * @return the combined pattern
     */
    @NotNull
    @Contract(pure = true, value = "_ -> new")
    @SafeVarargs
    public static <T> Pattern<T> And(@NotNull Pattern<? super T>... patterns) {
        return new Pattern<T>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable T value) throws PatternMatchReject {
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

    /** Pattern that combined several subpatterns of which one needs to match.<p>
     *
     * This pattern matches if at least one subpattern in {@code patterns} matches the matched value.<p>
     *
     * Only the captures assigned by the first matching subpattern will be assigned by this pattern.
     * Consequently, the subpatterns are allowed to assign the same values.
     *
     * @param patterns subpatterns that all should match
     * @param <T> type of the matched value
     * @return the combined pattern
     */
    @NotNull
    @Contract(pure = true, value = "_ -> new")
    @SafeVarargs
    public static <T> Pattern<T> Or(@NotNull Pattern<? super T>... patterns) {
        return new Pattern<T>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable T value) throws PatternMatchReject {
                if (patterns.length == 0) reject();
                for (int i=0; i<patterns.length-1; i++) {
                    Pattern<? super T> pattern = patterns[i];
                    if (mgr.protectedBlock(() -> pattern.apply(mgr, value))) return;
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

    /** Pattern that matches if the matched value has a specific type {@code U}.<p>
     *
     * This pattern matches if the matched value has type {@code U} (runtime type check)
     * and the subpattern {@code pattern} matches as well.<p>
     *
     * Example: <code>Instance(String.class, x)</code> will match a string and assign it to the capture {@code x}
     * which can be of type <code>{@link Capture}&lt;String&gt;</code>.<p>
     *
     * If the type we want to check is a generic type, this pattern is somewhat problematic:
     * For example, if we have a capture {@code x} of type <code>{@link Capture}&lt;{@link List}&lt;String&gt;&gt;</code>,
     * then <code>Instance({@link List List}.class, x)</code> will not type check because it expects {@code x}
     * to match values of raw type {@link List}, not of type {@link List}{@literal <String>}. See {@link Instance Instance}
     * for a variant of this pattern suitable for that case.
     *
     * @param clazz class tag for type {@code U}
     * @param pattern the subpattern that also need to match the matched value after being type cast
     * @param <U> the type that the matched value should have
     * @return the type checking pattern
     */
    @NotNull
    @Contract(pure = true, value = "_, _ -> new")
    public static <U> Pattern<Object> Instance(@NotNull Class<U> clazz, @NotNull Pattern<? super U> pattern) {
        return new Pattern<Object>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Object value) throws PatternMatchReject {
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

    /** Pattern that matches if the matched value has a specific type {@code U}.<p>
     *
     * This pattern matches if the matched value has type {@code U} (runtime type check)
     * and the subpattern {@code pattern} matches as well.<p>
     *
     * Note: If {@code U} is a generic type, then only the raw type will be checked.
     * (E.g., If {@code U} is <code>{@link Capture}&lt;{@link List}&lt;String&gt;&gt;</code>,
     * then it will only be checked that the matched value is a {@link List}.
     * However, the subpattern is allowed to have type parameter <code>{@link List}&lt;String&gt;</code>.
     * This is analogous to how the Java type cast works (e.g., <code>({@link List}&lt;String&gt;)object</code>)).
     * <p>
     *
     * For technical reasons, constructing this pattern has somewhat uncommon invocation syntax:
     * {@code new Instance<U>(pattern) {}} where {@code pattern} is the subpattern.<p>
     *
     * Example: <code>new Instance&lt;{@link List}&lt;String&gt;&gt;(x) {}</code> will match any {@link List} and assign
     * it to the capture {@code x}
     * which can be of type <code>{@link Capture}&lt;{@link List}&lt;String&gt;&gt;</code>.
     *
     * @param <U> the type that the matched value should have
     */
    public abstract static class Instance<U> extends Pattern<U> {
        private final Pattern<Object> instancePattern;
        private final Pattern<? super U> pattern;
        private final Type typeU;

        /** See the {@linkplain Instance class description} for how to create this pattern.
         * @param pattern the subpattern
         */
        @SuppressWarnings("unchecked")
        @Contract(pure = true)
        protected Instance(@NotNull Pattern<? super U> pattern) {
            // Based on https://stackoverflow.com/a/64138964/2646248, https://www.baeldung.com/java-super-type-tokens
            if (getClass().getSuperclass() != Instance.class)
                throw new InvalidPatternMatch("A subclass of a subclass of " + Instance.class +
                        " was created. This is not the intended use.");
            Type superclass = getClass().getGenericSuperclass();
            typeU = ((ParameterizedType) superclass).getActualTypeArguments()[0];
            Class<U> clazz;
            if (typeU instanceof ParameterizedType)
                clazz = (Class<U>) ((ParameterizedType) typeU).getRawType();
            else if (typeU instanceof Class)
                clazz = (Class<U>) typeU;
            else
                throw new InvalidPatternMatch("Type parameter of " + Instance.class.getName() +
                        " must be a class, not " + typeU + " " + typeU.getClass());
            instancePattern = Instance(clazz, pattern);
            this.pattern = pattern;
        }

        /** @hidden */
        @Override
        public void apply(@NotNull MatchManager mgr, @Nullable U value) throws PatternMatchReject {
            instancePattern.apply(mgr, value);
        }

        /** @hidden */
        @Override
        public String toString() {
            return "("+pattern+" : "+typeU+")";
        }
    }

    @NotNull
    @Contract(pure = true, value = "_ -> new")
    public static <T> Pattern<T> NoMatch(@NotNull Pattern<? super T> pattern) {
        return new Pattern<T>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable T value) throws PatternMatchReject {
                boolean matched = mgr.protectedBlock(() -> pattern.apply(mgr, value));
                if (matched) reject();
            }

            @Override
            public String toString() {
                return "NoMatch("+pattern+")";
            }
        };
    }

    /** Pattern that matches an array. <p>
     *
     * The pattern matches if the matched value is an array of length {@code patterns.length},
     * and the i-th element of the matched value matches the i-th pattern in {@code patterns}. <p>
     *
     * All captures assigned by the subpatterns {@code patterns} will be assigned by this pattern.
     * Consequently, the subpatterns must assign distinct captures.
     *
     * @param patterns the patterns for the array elements
     * @param <T> the element type of the array (i.e., the matched value has type {@code T[]})
     * @return the array pattern
     */
    @NotNull
    @Contract(pure = true, value = "_ -> new")
    @SafeVarargs
    public static <T> Pattern<T[]> Array(@NotNull Pattern<? super T> @NotNull ... patterns) {
        return new Pattern<T[]>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable T @Nullable [] value) throws PatternMatchReject {
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


    /** Pattern that matches an array. <p>
     *
     * This function is invoked as
     * <pre>
     * Array({@link #these these}(p1,...,pn),rest)
     * </pre>
     * where {@code p}1, …, {@code p}<i>n</i> are patterns
     * matching values of type {@code T}
     * and {@code rest} is a pattern matching values of type {@code T[]}.<p>
     *
     * The pattern matches if the matched value is an array of length ≥<i>n</i>,
     * and the <i>i</i>-th element of the matched value matches {@code p}<i>i</i> for
     * <i>i</i>=1,…,<i>n</i>, and the remaining elements of the array match {@code rest}.<p>
     *
     * Example: {@code Array(these(Is(1),Is(2)), x)} will match {@code {1,2,3,4,5}} and
     * assign {@code {3,4,5}} to the capture `x`, but it will not match {@code {1,1,3,4,5}}
     * nor {@code {1}}.<p>
     *
     * All captures assigned by the subpatterns {@code patterns} will be assigned by this pattern.
     * Consequently, the subpatterns must assign distinct captures.
     *
     * @param these the patterns for the prefix of the matched array
     * @param more the pattern for the rest of the matched array
     * @param <T> the element type of the array (i.e., the matched value has type {@code T[]})
     * @return the array pattern
     */
    public static <T> Pattern<T[]> Array(@NotNull Pattern<? super T> @NotNull [] these,
                                         @NotNull Pattern<? super T[]> more) {
        return new Pattern<T[]>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable T @Nullable [] value) throws PatternMatchReject {
                if (value == null) reject();
                if (value.length < these.length) reject();
                for (int i=0; i<these.length; i++)
                    these[i].apply(mgr, value[i]);
                T[] rest = Arrays.copyOfRange(value, these.length, value.length);
                more.apply(mgr, rest);
            }

            @Override
            public String toString() {
                StringJoiner joiner = new StringJoiner(", ");
                for (Pattern<?> pattern : these)
                    joiner.add(pattern.toString());
                return "Array(these(" + joiner + "), " + more + ")";
            }
        };
    }

    /** Returns {@code patterns} as an array.<p>
     *
     * “{@code these(p1,…,pn)}” is equivalent to “{@code new Pattern<? super T>[] { p1,…,pn }}”.
     * This function is intended for notational convenience in pattern-creating functions such as
     * {@link #Array(Pattern[], Pattern) Array}.
     *
     * @param patterns The patterns to be wrapped in an array.
     * @return {@code patterns} as an array
     */
    @SafeVarargs
    public static <T> @NotNull Pattern<? super T> @NotNull []
    these(@NotNull Pattern<? super T> @NotNull ... patterns) {
        return patterns;
    }


    // DOCUMENT
    // TODO test case
    public static <T> @NotNull Pattern<Optional<@NotNull T>> Optional(@NotNull Pattern<@NotNull T> pattern) {
        return new Pattern<Optional<T>>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Optional<T> value) throws PatternMatchReject {
                //noinspection OptionalAssignedToNull
                if (value==null) reject();
                if (!value.isPresent()) reject();
                pattern.apply(mgr, value.get());
            }

            @Override
            public String toString() {
                return "Optional(" + pattern + ")";
            }
        };
    }

    // DOCUMENT
    // TODO test case
    public static <T> @NotNull Pattern<Optional<@NotNull T>> Optional() {
        return new Pattern<Optional<T>>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Optional<T> value) throws PatternMatchReject {
                //noinspection OptionalAssignedToNull
                if (value==null) reject();
                if (value.isPresent()) reject();
            }

            @Override
            public String toString() {
                return "Optional.empty";
            }
        };
    }

    // DOCUMENT
    // TODO test case
    @SafeVarargs
    public static <K,V> @NotNull Pattern<Map<K,V>> Map(@NotNull Map.Entry<K,Pattern<? super V>> ... patterns) {
        return new Pattern<Map<K, V>>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Map<K, V> map) throws PatternMatchReject {
                if (map==null) reject();
                for (Map.Entry<K, Pattern<? super V>> entry : patterns) {
                    final K key = entry.getKey();
                    if (!map.containsKey(key)) reject();
                    final V val = map.get(key);
                    final Pattern<? super V> pat = entry.getValue();
                    pat.apply(mgr, val);
                }
            }

            @Override
            public String toString() {
                StringJoiner joiner = new StringJoiner(", ");
                for (Map.@NotNull Entry<K, Pattern<? super V>> entry : patterns)
                    joiner.add(entry.getKey().toString())
                            .add("=")
                            .add(entry.getValue().toString());
                return "Map(" + joiner + ")";
            }
        };
    }


    // DOCUMENT (also: exceptions are thrown, only NullPointerException in function leads to rejection)
    // TODO test case
    @NotNull public static <T,U> Pattern<T> After(@NotNull Function<T,U> function, @NotNull Pattern<? super U> pattern) {
        return new Pattern<T>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable T value) throws PatternMatchReject {
                U newValue = null;
                try {
                    newValue = function.apply(value);
                } catch (NullPointerException e) {
                    reject();
                }
                pattern.apply(mgr, newValue);
            }

            @Override
            public String toString() {
                return "After(…," + pattern + ")";
            }
        };
    }

    /** Pattern that matches an iterator ({@link Iterator}). <p>
     *
     * The pattern matches if the matched value is an iterator that contains {@code patterns.length} elements,
     * and the i-th element of the matched value matches the i-th pattern in {@code patterns}. <p>
     *
     * All captures assigned by the subpatterns {@code patterns} will be assigned by this pattern.
     * Consequently, the subpatterns must assign distinct captures.<p>
     *
     * As iterators can only be traversed once, this pattern clones the matched value using a {@link CloneableIterator}.
     * This leads to the following rules:
     * <ul>
     * <li>The same iterator can be matched (or rejected) in several subpatterns.</li>
     * <li>All subpatterns will effectively use the original content of the iterator.</li>
     * <li>The original matched iterator must not be used any more after the matching (it is in an undefined state)
     * whether the match failed or not.</li>
     * <li>The original matched iterator can, however, be used as an argument to
     * {@link CloneableIterator#from(Iterator) CloneableIterator.from}/{@link CloneableIterator#fromShared fromShared} or
     * {@link StatelessIterator#from(Iterator) StatelessIterator.from}/{@link CloneableIterator#fromShared fromShared}
     * in which case the resulting cloneable/stateless iterator will contain the original content of the matched iterator.</li>
     * </ul><p>
     *
     * Infinite iterators are allowed (but will never match).
     *
     * @param patterns the patterns for the iterator elements
     * @param <T> the element type of the iterator (i.e., the matched value has type {@link Iterator}{@code <T>})
     * @return the iterator-matching pattern
     */
    @NotNull
    @Contract(pure = true, value = "_ -> new")
    @SafeVarargs
    public static <T> Pattern<Iterator<T>> Iterator(@NotNull Pattern<? super T> @NotNull ... patterns) {
        return new Pattern<Iterator<T>>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Iterator<@Nullable T> iterator) throws PatternMatchReject {
                if (iterator == null) reject();
                iterator = CloneableIterator.fromShared(iterator);
                for (Pattern<? super T> pattern : patterns) {
                    if (!iterator.hasNext()) reject();
                    T value = iterator.next();
                    pattern.apply(mgr, value);
                }
                if (iterator.hasNext()) reject();
            }

            @Override
            public String toString() {
                StringJoiner joiner = new StringJoiner(", ");
                for (Pattern<?> pattern : patterns)
                    joiner.add(pattern.toString());
                return "Iterator(" + joiner + ")";
            }
        };
    }


    /** Pattern that matches an iterator ({@link Iterator}). <p>
     *
     * This function is invoked as
     * <pre>
     * Iterator({@link #these these}(p1,...,pn),rest)
     * </pre>
     * where {@code p}1, …, {@code p}<i>n</i> are patterns
     * matching values of type {@code T}
     * and {@code rest} is a pattern matching values of type {@code Iterator<T>}.<p>
     *
     * The pattern matches if the matched value is an iterator containing ≥<i>n</i> elements,
     * and the <i>i</i>-th element of the matched value matches {@code p}<i>i</i> for
     * <i>i</i>=1,…,<i>n</i>, and the iterator containing the remaining elements matches {@code rest}.<p>
     *
     * All captures assigned by the subpatterns {@code patterns} will be assigned by this pattern.
     * Consequently, the subpatterns must assign distinct captures.<p>
     *
     * As iterators can only be traversed once, this pattern clones the matched iterator using a {@link CloneableIterator}.
     * The same rules as described in {@link #Iterator(Pattern[])} apply.
     *
     * Infinite iterators are allowed.
     *
     * @param these the patterns for the prefix of the matched iterator
     * @param more the pattern for the rest of the matched iterator
     * @param <T> the element type of the iterator (i.e., the matched value has type {@link Iterator}{@code <T>})
     * @return the iterator-matching pattern
     */
    @NotNull public static <T> Pattern<Iterator<T>> Iterator(@NotNull Pattern<? super T> @NotNull [] these,
                                                             @NotNull Pattern<? super CloneableIterator<T>> more) {
        return new Pattern<Iterator<T>>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Iterator<@Nullable T> iterator) throws PatternMatchReject {
                if (iterator == null) reject();
                CloneableIterator<T> cloneableIterator = CloneableIterator.fromShared(iterator);
                for (Pattern<? super T> pattern : these) {
                    if (!cloneableIterator.hasNext()) reject();
                    T value = cloneableIterator.next();
                    pattern.apply(mgr, value);
                }
                more.apply(mgr, cloneableIterator);
            }

            @Override
            public String toString() {
                StringJoiner joiner = new StringJoiner(", ");
                for (Pattern<?> pattern : these)
                    joiner.add(pattern.toString());
                return "Iterator(these(" + joiner + "), " + more + ")";
            }
        };
    }
}
