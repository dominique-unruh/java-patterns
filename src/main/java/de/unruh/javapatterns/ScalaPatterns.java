package de.unruh.javapatterns;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import scala.*;
import scala.collection.Iterable;
import scala.collection.Iterator;
import scala.collection.JavaConverters;
import scala.collection.Seq;
import scala.collection.immutable.LazyList;
import scala.collection.immutable.List;

import java.util.Arrays;
import java.util.StringJoiner;

// DOCUMENT
// DOCUMENT that you need to import scala-library
// DOCUMENT reference from README etc.

public final class ScalaPatterns {
    private ScalaPatterns() {}


    /** Pattern that matches a Scala sequence ({@link Seq}).
     * This includes in particular lists ({@link List}).<p>
     *
     * The pattern matches if the matched value is a sequence of {@code patterns.length},
     * and the i-th element of the matched value matches the i-th pattern in {@code patterns}. <p>
     *
     * All captures assigned by the subpatterns {@code patterns} will be assigned by this pattern.
     * Consequently, the subpatterns must assign distinct captures.<p>
     *
     * Infinite sequences will be rejected. (But not lead to infinite loops.)
     *
     * @param patterns the patterns for the sequence elements
     * @param <T> the element type of the sequence (i.e., the matched value has type {@code Seq<T>})
     * @return the sequence pattern
     */
    @SafeVarargs
    public static <T> @NotNull Pattern<Seq<T>> Seq(@NotNull Pattern<? super T> @NotNull ... patterns) {
        return new Pattern<Seq<T>>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Seq<T> value) throws PatternMatchReject {
                if (value==null) reject();
                if (value.lengthCompare(patterns.length) != 0) reject();
                int idx = 0;
                for (Iterator<T> it = value.iterator(); it.hasNext(); ) {
                    patterns[idx].apply(mgr, it.next());
                    idx ++;
                }
            }

            @Override
            public String toString() {
                StringJoiner joiner = new StringJoiner(", ");
                for (Pattern<?> pattern : patterns)
                    joiner.add(pattern.toString());
                return "Seq(" + joiner + ")";
            }
        };
    }


    /** Pattern that matches a Scala sequence ({@link Seq}).
     * This includes in particular lists ({@link List}).<p>
     *
     * This function is invoked as
     * <pre>
     * Seq({@link Patterns#these these}(p1,...,pn),rest)
     * </pre>
     * where {@code p}1, …, {@code p}<i>n</i> are patterns
     * matching values of type {@code T}
     * and {@code rest} is a pattern matching values of type {@link Seq}{@code <T>}.<p>
     *
     * The pattern matches if the matched value is a sequence of length ≥<i>n</i>,
     * and the <i>i</i>-th element of the matched value matches {@code p}<i>i</i> for
     * <i>i</i>=1,…,<i>n</i>, and the remaining elements of the sequence match {@code rest}.<p>
     *
     * Infinite sequences are permitted. (In this case, {@code rest} will be applied to
     * the infinite suffix of the matched value.)<p>
     *
     * Example: {@code Seq(these(Is(1),Is(2)), x)} will match {@code List(1,2,3,4,5)} and
     * assign {@code List(3,4,5)} to the capture `x`, but it will not match {@code List(1,1,3,4,5)}
     * nor {@code List(1)}. It will also match the infinite {@link LazyList#from(int) LazyList.from}{@code (1)},
     * assigning the infinite sequence {@code 3,4,5,…} to {@code x}.<p>
     *
     * All captures assigned by the subpatterns {@code patterns} will be assigned by this pattern.
     * Consequently, the subpatterns must assign distinct captures.
     *
     * @param these the patterns for the prefix of the matched sequence
     * @param more the pattern for the rest of the matched sequence
     * @param <T> the element type of the sequence (i.e., the matched value has type {@code Seq<T>})
     * @return the sequence pattern
     */
    public static <T> Pattern<Seq<T>> Seq(@NotNull Pattern<? super T> @NotNull [] these,
                                          @NotNull Pattern<? super Seq<T>> more) {
        return new Pattern<Seq<T>>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Seq<@Nullable T> value) throws PatternMatchReject {
                if (value == null) reject();
                if (value.lengthCompare(these.length) < 0) reject();
                Tuple2<?, ?> split = value.splitAt(these.length); // Java is confused by precise type of splitAt
                @SuppressWarnings("unchecked") Iterable<T> valueThese = (Iterable<T>)split._1;
                @SuppressWarnings("unchecked") Iterable<T> valueMore = (Iterable<T>)split._2;
                int idx = 0;
                for (Iterator<T> it = valueThese.iterator(); it.hasNext(); ) {
                    these[idx].apply(mgr, it.next());
                    idx ++;
                }
                more.apply(mgr, valueMore.toSeq());
            }

            @Override
            public String toString() {
                StringJoiner joiner = new StringJoiner(", ");
                for (Pattern<?> pattern : these)
                    joiner.add(pattern.toString());
                return "Seq(these(" + joiner + "), " + more + ")";
            }
        };

    }

        public static <T> @NotNull Pattern<Option<T>> Some(@NotNull Pattern<? super T> pattern) {
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

    public static @NotNull Pattern<Option<Object>> None = new Pattern<Option<Object>>() {
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

    /** Pattern that matches a Scala 1-tuple.
     * (More precisely, a value of type {@link Product1}, of which {@link Tuple1} is a subclass.)
     **/
    public static <T1> Pattern<Product1<T1>> Tuple(Pattern<T1> pattern1) {
        return new Pattern<Product1<T1>>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Product1<T1> value) throws PatternMatchReject {
                if (value == null) reject();
                pattern1.apply(mgr, value._1());
            }

            @Override
            public String toString() {
                return "Tuple(" + pattern1 + ")";
            }
        };
    }


    /** Pattern that matches a Scala 2-tuple.
     * (More precisely, a value of type {@link Product2}, of which {@link Tuple2} is a subclass.)
     **/
    public static <T1,T2> Pattern<Product2<T1,T2>> Tuple(Pattern<T1> pattern1, Pattern<T2> pattern2) {
        return new Pattern<Product2<T1,T2>>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Product2<T1,T2> value) throws PatternMatchReject {
                if (value == null) reject();
                pattern1.apply(mgr, value._1());
                pattern2.apply(mgr, value._2());
            }

            @Override
            public String toString() {
                return "Tuple(" + pattern1 + "," + pattern2 + ")";
            }
        };
    }


    /** Pattern that matches a Scala 3-tuple.
     * (More precisely, a value of type {@link Product3}, of which {@link Tuple3} is a subclass.)
     **/
    public static <T1,T2,T3> Pattern<Product3<T1,T2,T3>> Tuple(Pattern<T1> pattern1, Pattern<T2> pattern2, Pattern<T3> pattern3) {
        return new Pattern<Product3<T1,T2,T3>>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Product3<T1,T2,T3> value) throws PatternMatchReject {
                if (value == null) reject();
                pattern1.apply(mgr, value._1());
                pattern2.apply(mgr, value._2());
                pattern3.apply(mgr, value._3());
            }

            @Override
            public String toString() {
                return "Tuple(" + pattern1 + "," + pattern2 + "," + pattern3 + ")";
            }
        };
    }


    /** Pattern that matches a Scala 4-tuple.
     * (More precisely, a value of type {@link Product4}, of which {@link Tuple4} is a subclass.)
     **/
    public static <T1,T2,T3,T4> Pattern<Product4<T1,T2,T3,T4>> Tuple(Pattern<T1> pattern1, Pattern<T2> pattern2, Pattern<T3> pattern3, Pattern<T4> pattern4) {
        return new Pattern<Product4<T1,T2,T3,T4>>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Product4<T1,T2,T3,T4> value) throws PatternMatchReject {
                if (value == null) reject();
                pattern1.apply(mgr, value._1());
                pattern2.apply(mgr, value._2());
                pattern3.apply(mgr, value._3());
                pattern4.apply(mgr, value._4());
            }

            @Override
            public String toString() {
                return "Tuple(" + pattern1 + "," + pattern2 + "," + pattern3 + "," + pattern4 + ")";
            }
        };
    }


    /** Pattern that matches a Scala 5-tuple.
     * (More precisely, a value of type {@link Product5}, of which {@link Tuple5} is a subclass.)
     **/
    public static <T1,T2,T3,T4,T5> Pattern<Product5<T1,T2,T3,T4,T5>> Tuple(Pattern<T1> pattern1, Pattern<T2> pattern2, Pattern<T3> pattern3, Pattern<T4> pattern4, Pattern<T5> pattern5) {
        return new Pattern<Product5<T1,T2,T3,T4,T5>>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Product5<T1,T2,T3,T4,T5> value) throws PatternMatchReject {
                if (value == null) reject();
                pattern1.apply(mgr, value._1());
                pattern2.apply(mgr, value._2());
                pattern3.apply(mgr, value._3());
                pattern4.apply(mgr, value._4());
                pattern5.apply(mgr, value._5());
            }

            @Override
            public String toString() {
                return "Tuple(" + pattern1 + "," + pattern2 + "," + pattern3 + "," + pattern4 + "," + pattern5 + ")";
            }
        };
    }


    /** Pattern that matches a Scala 6-tuple.
     * (More precisely, a value of type {@link Product6}, of which {@link Tuple6} is a subclass.)
     **/
    public static <T1,T2,T3,T4,T5,T6> Pattern<Product6<T1,T2,T3,T4,T5,T6>> Tuple(Pattern<T1> pattern1, Pattern<T2> pattern2, Pattern<T3> pattern3, Pattern<T4> pattern4, Pattern<T5> pattern5, Pattern<T6> pattern6) {
        return new Pattern<Product6<T1,T2,T3,T4,T5,T6>>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Product6<T1,T2,T3,T4,T5,T6> value) throws PatternMatchReject {
                if (value == null) reject();
                pattern1.apply(mgr, value._1());
                pattern2.apply(mgr, value._2());
                pattern3.apply(mgr, value._3());
                pattern4.apply(mgr, value._4());
                pattern5.apply(mgr, value._5());
                pattern6.apply(mgr, value._6());
            }

            @Override
            public String toString() {
                return "Tuple(" + pattern1 + "," + pattern2 + "," + pattern3 + "," + pattern4 + "," + pattern5 + "," + pattern6 + ")";
            }
        };
    }


    /** Pattern that matches a Scala 7-tuple.
     * (More precisely, a value of type {@link Product7}, of which {@link Tuple7} is a subclass.)
     **/
    public static <T1,T2,T3,T4,T5,T6,T7> Pattern<Product7<T1,T2,T3,T4,T5,T6,T7>> Tuple(Pattern<T1> pattern1, Pattern<T2> pattern2, Pattern<T3> pattern3, Pattern<T4> pattern4, Pattern<T5> pattern5, Pattern<T6> pattern6, Pattern<T7> pattern7) {
        return new Pattern<Product7<T1,T2,T3,T4,T5,T6,T7>>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Product7<T1,T2,T3,T4,T5,T6,T7> value) throws PatternMatchReject {
                if (value == null) reject();
                pattern1.apply(mgr, value._1());
                pattern2.apply(mgr, value._2());
                pattern3.apply(mgr, value._3());
                pattern4.apply(mgr, value._4());
                pattern5.apply(mgr, value._5());
                pattern6.apply(mgr, value._6());
                pattern7.apply(mgr, value._7());
            }

            @Override
            public String toString() {
                return "Tuple(" + pattern1 + "," + pattern2 + "," + pattern3 + "," + pattern4 + "," + pattern5 + "," + pattern6 + "," + pattern7 + ")";
            }
        };
    }


    /** Pattern that matches a Scala 8-tuple.
     * (More precisely, a value of type {@link Product8}, of which {@link Tuple8} is a subclass.)
     **/
    public static <T1,T2,T3,T4,T5,T6,T7,T8> Pattern<Product8<T1,T2,T3,T4,T5,T6,T7,T8>> Tuple(Pattern<T1> pattern1, Pattern<T2> pattern2, Pattern<T3> pattern3, Pattern<T4> pattern4, Pattern<T5> pattern5, Pattern<T6> pattern6, Pattern<T7> pattern7, Pattern<T8> pattern8) {
        return new Pattern<Product8<T1,T2,T3,T4,T5,T6,T7,T8>>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Product8<T1,T2,T3,T4,T5,T6,T7,T8> value) throws PatternMatchReject {
                if (value == null) reject();
                pattern1.apply(mgr, value._1());
                pattern2.apply(mgr, value._2());
                pattern3.apply(mgr, value._3());
                pattern4.apply(mgr, value._4());
                pattern5.apply(mgr, value._5());
                pattern6.apply(mgr, value._6());
                pattern7.apply(mgr, value._7());
                pattern8.apply(mgr, value._8());
            }

            @Override
            public String toString() {
                return "Tuple(" + pattern1 + "," + pattern2 + "," + pattern3 + "," + pattern4 + "," + pattern5 + "," + pattern6 + "," + pattern7 + "," + pattern8 + ")";
            }
        };
    }


    /** Pattern that matches a Scala 9-tuple.
     * (More precisely, a value of type {@link Product9}, of which {@link Tuple9} is a subclass.)
     **/
    public static <T1,T2,T3,T4,T5,T6,T7,T8,T9> Pattern<Product9<T1,T2,T3,T4,T5,T6,T7,T8,T9>> Tuple(Pattern<T1> pattern1, Pattern<T2> pattern2, Pattern<T3> pattern3, Pattern<T4> pattern4, Pattern<T5> pattern5, Pattern<T6> pattern6, Pattern<T7> pattern7, Pattern<T8> pattern8, Pattern<T9> pattern9) {
        return new Pattern<Product9<T1,T2,T3,T4,T5,T6,T7,T8,T9>>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Product9<T1,T2,T3,T4,T5,T6,T7,T8,T9> value) throws PatternMatchReject {
                if (value == null) reject();
                pattern1.apply(mgr, value._1());
                pattern2.apply(mgr, value._2());
                pattern3.apply(mgr, value._3());
                pattern4.apply(mgr, value._4());
                pattern5.apply(mgr, value._5());
                pattern6.apply(mgr, value._6());
                pattern7.apply(mgr, value._7());
                pattern8.apply(mgr, value._8());
                pattern9.apply(mgr, value._9());
            }

            @Override
            public String toString() {
                return "Tuple(" + pattern1 + "," + pattern2 + "," + pattern3 + "," + pattern4 + "," + pattern5 + "," + pattern6 + "," + pattern7 + "," + pattern8 + "," + pattern9 + ")";
            }
        };
    }


    /** Pattern that matches a Scala 10-tuple.
     * (More precisely, a value of type {@link Product10}, of which {@link Tuple10} is a subclass.)
     **/
    public static <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10> Pattern<Product10<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10>> Tuple(Pattern<T1> pattern1, Pattern<T2> pattern2, Pattern<T3> pattern3, Pattern<T4> pattern4, Pattern<T5> pattern5, Pattern<T6> pattern6, Pattern<T7> pattern7, Pattern<T8> pattern8, Pattern<T9> pattern9, Pattern<T10> pattern10) {
        return new Pattern<Product10<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10>>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Product10<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10> value) throws PatternMatchReject {
                if (value == null) reject();
                pattern1.apply(mgr, value._1());
                pattern2.apply(mgr, value._2());
                pattern3.apply(mgr, value._3());
                pattern4.apply(mgr, value._4());
                pattern5.apply(mgr, value._5());
                pattern6.apply(mgr, value._6());
                pattern7.apply(mgr, value._7());
                pattern8.apply(mgr, value._8());
                pattern9.apply(mgr, value._9());
                pattern10.apply(mgr, value._10());
            }

            @Override
            public String toString() {
                return "Tuple(" + pattern1 + "," + pattern2 + "," + pattern3 + "," + pattern4 + "," + pattern5 + "," + pattern6 + "," + pattern7 + "," + pattern8 + "," + pattern9 + "," + pattern10 + ")";
            }
        };
    }


    /** Pattern that matches a Scala 11-tuple.
     * (More precisely, a value of type {@link Product11}, of which {@link Tuple11} is a subclass.)
     **/
    public static <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11> Pattern<Product11<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11>> Tuple(Pattern<T1> pattern1, Pattern<T2> pattern2, Pattern<T3> pattern3, Pattern<T4> pattern4, Pattern<T5> pattern5, Pattern<T6> pattern6, Pattern<T7> pattern7, Pattern<T8> pattern8, Pattern<T9> pattern9, Pattern<T10> pattern10, Pattern<T11> pattern11) {
        return new Pattern<Product11<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11>>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Product11<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11> value) throws PatternMatchReject {
                if (value == null) reject();
                pattern1.apply(mgr, value._1());
                pattern2.apply(mgr, value._2());
                pattern3.apply(mgr, value._3());
                pattern4.apply(mgr, value._4());
                pattern5.apply(mgr, value._5());
                pattern6.apply(mgr, value._6());
                pattern7.apply(mgr, value._7());
                pattern8.apply(mgr, value._8());
                pattern9.apply(mgr, value._9());
                pattern10.apply(mgr, value._10());
                pattern11.apply(mgr, value._11());
            }

            @Override
            public String toString() {
                return "Tuple(" + pattern1 + "," + pattern2 + "," + pattern3 + "," + pattern4 + "," + pattern5 + "," + pattern6 + "," + pattern7 + "," + pattern8 + "," + pattern9 + "," + pattern10 + "," + pattern11 + ")";
            }
        };
    }


    /** Pattern that matches a Scala 12-tuple.
     * (More precisely, a value of type {@link Product12}, of which {@link Tuple12} is a subclass.)
     **/
    public static <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12> Pattern<Product12<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12>> Tuple(Pattern<T1> pattern1, Pattern<T2> pattern2, Pattern<T3> pattern3, Pattern<T4> pattern4, Pattern<T5> pattern5, Pattern<T6> pattern6, Pattern<T7> pattern7, Pattern<T8> pattern8, Pattern<T9> pattern9, Pattern<T10> pattern10, Pattern<T11> pattern11, Pattern<T12> pattern12) {
        return new Pattern<Product12<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12>>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Product12<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12> value) throws PatternMatchReject {
                if (value == null) reject();
                pattern1.apply(mgr, value._1());
                pattern2.apply(mgr, value._2());
                pattern3.apply(mgr, value._3());
                pattern4.apply(mgr, value._4());
                pattern5.apply(mgr, value._5());
                pattern6.apply(mgr, value._6());
                pattern7.apply(mgr, value._7());
                pattern8.apply(mgr, value._8());
                pattern9.apply(mgr, value._9());
                pattern10.apply(mgr, value._10());
                pattern11.apply(mgr, value._11());
                pattern12.apply(mgr, value._12());
            }

            @Override
            public String toString() {
                return "Tuple(" + pattern1 + "," + pattern2 + "," + pattern3 + "," + pattern4 + "," + pattern5 + "," + pattern6 + "," + pattern7 + "," + pattern8 + "," + pattern9 + "," + pattern10 + "," + pattern11 + "," + pattern12 + ")";
            }
        };
    }


    /** Pattern that matches a Scala 13-tuple.
     * (More precisely, a value of type {@link Product13}, of which {@link Tuple13} is a subclass.)
     **/
    public static <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13> Pattern<Product13<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13>> Tuple(Pattern<T1> pattern1, Pattern<T2> pattern2, Pattern<T3> pattern3, Pattern<T4> pattern4, Pattern<T5> pattern5, Pattern<T6> pattern6, Pattern<T7> pattern7, Pattern<T8> pattern8, Pattern<T9> pattern9, Pattern<T10> pattern10, Pattern<T11> pattern11, Pattern<T12> pattern12, Pattern<T13> pattern13) {
        return new Pattern<Product13<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13>>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Product13<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13> value) throws PatternMatchReject {
                if (value == null) reject();
                pattern1.apply(mgr, value._1());
                pattern2.apply(mgr, value._2());
                pattern3.apply(mgr, value._3());
                pattern4.apply(mgr, value._4());
                pattern5.apply(mgr, value._5());
                pattern6.apply(mgr, value._6());
                pattern7.apply(mgr, value._7());
                pattern8.apply(mgr, value._8());
                pattern9.apply(mgr, value._9());
                pattern10.apply(mgr, value._10());
                pattern11.apply(mgr, value._11());
                pattern12.apply(mgr, value._12());
                pattern13.apply(mgr, value._13());
            }

            @Override
            public String toString() {
                return "Tuple(" + pattern1 + "," + pattern2 + "," + pattern3 + "," + pattern4 + "," + pattern5 + "," + pattern6 + "," + pattern7 + "," + pattern8 + "," + pattern9 + "," + pattern10 + "," + pattern11 + "," + pattern12 + "," + pattern13 + ")";
            }
        };
    }


    /** Pattern that matches a Scala 14-tuple.
     * (More precisely, a value of type {@link Product14}, of which {@link Tuple14} is a subclass.)
     **/
    public static <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14> Pattern<Product14<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14>> Tuple(Pattern<T1> pattern1, Pattern<T2> pattern2, Pattern<T3> pattern3, Pattern<T4> pattern4, Pattern<T5> pattern5, Pattern<T6> pattern6, Pattern<T7> pattern7, Pattern<T8> pattern8, Pattern<T9> pattern9, Pattern<T10> pattern10, Pattern<T11> pattern11, Pattern<T12> pattern12, Pattern<T13> pattern13, Pattern<T14> pattern14) {
        return new Pattern<Product14<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14>>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Product14<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14> value) throws PatternMatchReject {
                if (value == null) reject();
                pattern1.apply(mgr, value._1());
                pattern2.apply(mgr, value._2());
                pattern3.apply(mgr, value._3());
                pattern4.apply(mgr, value._4());
                pattern5.apply(mgr, value._5());
                pattern6.apply(mgr, value._6());
                pattern7.apply(mgr, value._7());
                pattern8.apply(mgr, value._8());
                pattern9.apply(mgr, value._9());
                pattern10.apply(mgr, value._10());
                pattern11.apply(mgr, value._11());
                pattern12.apply(mgr, value._12());
                pattern13.apply(mgr, value._13());
                pattern14.apply(mgr, value._14());
            }

            @Override
            public String toString() {
                return "Tuple(" + pattern1 + "," + pattern2 + "," + pattern3 + "," + pattern4 + "," + pattern5 + "," + pattern6 + "," + pattern7 + "," + pattern8 + "," + pattern9 + "," + pattern10 + "," + pattern11 + "," + pattern12 + "," + pattern13 + "," + pattern14 + ")";
            }
        };
    }


    /** Pattern that matches a Scala 15-tuple.
     * (More precisely, a value of type {@link Product15}, of which {@link Tuple15} is a subclass.)
     **/
    public static <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15> Pattern<Product15<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15>> Tuple(Pattern<T1> pattern1, Pattern<T2> pattern2, Pattern<T3> pattern3, Pattern<T4> pattern4, Pattern<T5> pattern5, Pattern<T6> pattern6, Pattern<T7> pattern7, Pattern<T8> pattern8, Pattern<T9> pattern9, Pattern<T10> pattern10, Pattern<T11> pattern11, Pattern<T12> pattern12, Pattern<T13> pattern13, Pattern<T14> pattern14, Pattern<T15> pattern15) {
        return new Pattern<Product15<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15>>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Product15<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15> value) throws PatternMatchReject {
                if (value == null) reject();
                pattern1.apply(mgr, value._1());
                pattern2.apply(mgr, value._2());
                pattern3.apply(mgr, value._3());
                pattern4.apply(mgr, value._4());
                pattern5.apply(mgr, value._5());
                pattern6.apply(mgr, value._6());
                pattern7.apply(mgr, value._7());
                pattern8.apply(mgr, value._8());
                pattern9.apply(mgr, value._9());
                pattern10.apply(mgr, value._10());
                pattern11.apply(mgr, value._11());
                pattern12.apply(mgr, value._12());
                pattern13.apply(mgr, value._13());
                pattern14.apply(mgr, value._14());
                pattern15.apply(mgr, value._15());
            }

            @Override
            public String toString() {
                return "Tuple(" + pattern1 + "," + pattern2 + "," + pattern3 + "," + pattern4 + "," + pattern5 + "," + pattern6 + "," + pattern7 + "," + pattern8 + "," + pattern9 + "," + pattern10 + "," + pattern11 + "," + pattern12 + "," + pattern13 + "," + pattern14 + "," + pattern15 + ")";
            }
        };
    }


    /** Pattern that matches a Scala 16-tuple.
     * (More precisely, a value of type {@link Product16}, of which {@link Tuple16} is a subclass.)
     **/
    public static <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16> Pattern<Product16<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16>> Tuple(Pattern<T1> pattern1, Pattern<T2> pattern2, Pattern<T3> pattern3, Pattern<T4> pattern4, Pattern<T5> pattern5, Pattern<T6> pattern6, Pattern<T7> pattern7, Pattern<T8> pattern8, Pattern<T9> pattern9, Pattern<T10> pattern10, Pattern<T11> pattern11, Pattern<T12> pattern12, Pattern<T13> pattern13, Pattern<T14> pattern14, Pattern<T15> pattern15, Pattern<T16> pattern16) {
        return new Pattern<Product16<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16>>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Product16<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16> value) throws PatternMatchReject {
                if (value == null) reject();
                pattern1.apply(mgr, value._1());
                pattern2.apply(mgr, value._2());
                pattern3.apply(mgr, value._3());
                pattern4.apply(mgr, value._4());
                pattern5.apply(mgr, value._5());
                pattern6.apply(mgr, value._6());
                pattern7.apply(mgr, value._7());
                pattern8.apply(mgr, value._8());
                pattern9.apply(mgr, value._9());
                pattern10.apply(mgr, value._10());
                pattern11.apply(mgr, value._11());
                pattern12.apply(mgr, value._12());
                pattern13.apply(mgr, value._13());
                pattern14.apply(mgr, value._14());
                pattern15.apply(mgr, value._15());
                pattern16.apply(mgr, value._16());
            }

            @Override
            public String toString() {
                return "Tuple(" + pattern1 + "," + pattern2 + "," + pattern3 + "," + pattern4 + "," + pattern5 + "," + pattern6 + "," + pattern7 + "," + pattern8 + "," + pattern9 + "," + pattern10 + "," + pattern11 + "," + pattern12 + "," + pattern13 + "," + pattern14 + "," + pattern15 + "," + pattern16 + ")";
            }
        };
    }


    /** Pattern that matches a Scala 17-tuple.
     * (More precisely, a value of type {@link Product17}, of which {@link Tuple17} is a subclass.)
     **/
    public static <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17> Pattern<Product17<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17>> Tuple(Pattern<T1> pattern1, Pattern<T2> pattern2, Pattern<T3> pattern3, Pattern<T4> pattern4, Pattern<T5> pattern5, Pattern<T6> pattern6, Pattern<T7> pattern7, Pattern<T8> pattern8, Pattern<T9> pattern9, Pattern<T10> pattern10, Pattern<T11> pattern11, Pattern<T12> pattern12, Pattern<T13> pattern13, Pattern<T14> pattern14, Pattern<T15> pattern15, Pattern<T16> pattern16, Pattern<T17> pattern17) {
        return new Pattern<Product17<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17>>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Product17<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17> value) throws PatternMatchReject {
                if (value == null) reject();
                pattern1.apply(mgr, value._1());
                pattern2.apply(mgr, value._2());
                pattern3.apply(mgr, value._3());
                pattern4.apply(mgr, value._4());
                pattern5.apply(mgr, value._5());
                pattern6.apply(mgr, value._6());
                pattern7.apply(mgr, value._7());
                pattern8.apply(mgr, value._8());
                pattern9.apply(mgr, value._9());
                pattern10.apply(mgr, value._10());
                pattern11.apply(mgr, value._11());
                pattern12.apply(mgr, value._12());
                pattern13.apply(mgr, value._13());
                pattern14.apply(mgr, value._14());
                pattern15.apply(mgr, value._15());
                pattern16.apply(mgr, value._16());
                pattern17.apply(mgr, value._17());
            }

            @Override
            public String toString() {
                return "Tuple(" + pattern1 + "," + pattern2 + "," + pattern3 + "," + pattern4 + "," + pattern5 + "," + pattern6 + "," + pattern7 + "," + pattern8 + "," + pattern9 + "," + pattern10 + "," + pattern11 + "," + pattern12 + "," + pattern13 + "," + pattern14 + "," + pattern15 + "," + pattern16 + "," + pattern17 + ")";
            }
        };
    }


    /** Pattern that matches a Scala 18-tuple.
     * (More precisely, a value of type {@link Product18}, of which {@link Tuple18} is a subclass.)
     **/
    public static <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18> Pattern<Product18<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18>> Tuple(Pattern<T1> pattern1, Pattern<T2> pattern2, Pattern<T3> pattern3, Pattern<T4> pattern4, Pattern<T5> pattern5, Pattern<T6> pattern6, Pattern<T7> pattern7, Pattern<T8> pattern8, Pattern<T9> pattern9, Pattern<T10> pattern10, Pattern<T11> pattern11, Pattern<T12> pattern12, Pattern<T13> pattern13, Pattern<T14> pattern14, Pattern<T15> pattern15, Pattern<T16> pattern16, Pattern<T17> pattern17, Pattern<T18> pattern18) {
        return new Pattern<Product18<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18>>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Product18<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18> value) throws PatternMatchReject {
                if (value == null) reject();
                pattern1.apply(mgr, value._1());
                pattern2.apply(mgr, value._2());
                pattern3.apply(mgr, value._3());
                pattern4.apply(mgr, value._4());
                pattern5.apply(mgr, value._5());
                pattern6.apply(mgr, value._6());
                pattern7.apply(mgr, value._7());
                pattern8.apply(mgr, value._8());
                pattern9.apply(mgr, value._9());
                pattern10.apply(mgr, value._10());
                pattern11.apply(mgr, value._11());
                pattern12.apply(mgr, value._12());
                pattern13.apply(mgr, value._13());
                pattern14.apply(mgr, value._14());
                pattern15.apply(mgr, value._15());
                pattern16.apply(mgr, value._16());
                pattern17.apply(mgr, value._17());
                pattern18.apply(mgr, value._18());
            }

            @Override
            public String toString() {
                return "Tuple(" + pattern1 + "," + pattern2 + "," + pattern3 + "," + pattern4 + "," + pattern5 + "," + pattern6 + "," + pattern7 + "," + pattern8 + "," + pattern9 + "," + pattern10 + "," + pattern11 + "," + pattern12 + "," + pattern13 + "," + pattern14 + "," + pattern15 + "," + pattern16 + "," + pattern17 + "," + pattern18 + ")";
            }
        };
    }


    /** Pattern that matches a Scala 19-tuple.
     * (More precisely, a value of type {@link Product19}, of which {@link Tuple19} is a subclass.)
     **/
    public static <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19> Pattern<Product19<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19>> Tuple(Pattern<T1> pattern1, Pattern<T2> pattern2, Pattern<T3> pattern3, Pattern<T4> pattern4, Pattern<T5> pattern5, Pattern<T6> pattern6, Pattern<T7> pattern7, Pattern<T8> pattern8, Pattern<T9> pattern9, Pattern<T10> pattern10, Pattern<T11> pattern11, Pattern<T12> pattern12, Pattern<T13> pattern13, Pattern<T14> pattern14, Pattern<T15> pattern15, Pattern<T16> pattern16, Pattern<T17> pattern17, Pattern<T18> pattern18, Pattern<T19> pattern19) {
        return new Pattern<Product19<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19>>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Product19<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19> value) throws PatternMatchReject {
                if (value == null) reject();
                pattern1.apply(mgr, value._1());
                pattern2.apply(mgr, value._2());
                pattern3.apply(mgr, value._3());
                pattern4.apply(mgr, value._4());
                pattern5.apply(mgr, value._5());
                pattern6.apply(mgr, value._6());
                pattern7.apply(mgr, value._7());
                pattern8.apply(mgr, value._8());
                pattern9.apply(mgr, value._9());
                pattern10.apply(mgr, value._10());
                pattern11.apply(mgr, value._11());
                pattern12.apply(mgr, value._12());
                pattern13.apply(mgr, value._13());
                pattern14.apply(mgr, value._14());
                pattern15.apply(mgr, value._15());
                pattern16.apply(mgr, value._16());
                pattern17.apply(mgr, value._17());
                pattern18.apply(mgr, value._18());
                pattern19.apply(mgr, value._19());
            }

            @Override
            public String toString() {
                return "Tuple(" + pattern1 + "," + pattern2 + "," + pattern3 + "," + pattern4 + "," + pattern5 + "," + pattern6 + "," + pattern7 + "," + pattern8 + "," + pattern9 + "," + pattern10 + "," + pattern11 + "," + pattern12 + "," + pattern13 + "," + pattern14 + "," + pattern15 + "," + pattern16 + "," + pattern17 + "," + pattern18 + "," + pattern19 + ")";
            }
        };
    }


    /** Pattern that matches a Scala 20-tuple.
     * (More precisely, a value of type {@link Product20}, of which {@link Tuple20} is a subclass.)
     **/
    public static <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20> Pattern<Product20<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20>> Tuple(Pattern<T1> pattern1, Pattern<T2> pattern2, Pattern<T3> pattern3, Pattern<T4> pattern4, Pattern<T5> pattern5, Pattern<T6> pattern6, Pattern<T7> pattern7, Pattern<T8> pattern8, Pattern<T9> pattern9, Pattern<T10> pattern10, Pattern<T11> pattern11, Pattern<T12> pattern12, Pattern<T13> pattern13, Pattern<T14> pattern14, Pattern<T15> pattern15, Pattern<T16> pattern16, Pattern<T17> pattern17, Pattern<T18> pattern18, Pattern<T19> pattern19, Pattern<T20> pattern20) {
        return new Pattern<Product20<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20>>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Product20<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20> value) throws PatternMatchReject {
                if (value == null) reject();
                pattern1.apply(mgr, value._1());
                pattern2.apply(mgr, value._2());
                pattern3.apply(mgr, value._3());
                pattern4.apply(mgr, value._4());
                pattern5.apply(mgr, value._5());
                pattern6.apply(mgr, value._6());
                pattern7.apply(mgr, value._7());
                pattern8.apply(mgr, value._8());
                pattern9.apply(mgr, value._9());
                pattern10.apply(mgr, value._10());
                pattern11.apply(mgr, value._11());
                pattern12.apply(mgr, value._12());
                pattern13.apply(mgr, value._13());
                pattern14.apply(mgr, value._14());
                pattern15.apply(mgr, value._15());
                pattern16.apply(mgr, value._16());
                pattern17.apply(mgr, value._17());
                pattern18.apply(mgr, value._18());
                pattern19.apply(mgr, value._19());
                pattern20.apply(mgr, value._20());
            }

            @Override
            public String toString() {
                return "Tuple(" + pattern1 + "," + pattern2 + "," + pattern3 + "," + pattern4 + "," + pattern5 + "," + pattern6 + "," + pattern7 + "," + pattern8 + "," + pattern9 + "," + pattern10 + "," + pattern11 + "," + pattern12 + "," + pattern13 + "," + pattern14 + "," + pattern15 + "," + pattern16 + "," + pattern17 + "," + pattern18 + "," + pattern19 + "," + pattern20 + ")";
            }
        };
    }


    /** Pattern that matches a Scala 21-tuple.
     * (More precisely, a value of type {@link Product21}, of which {@link Tuple21} is a subclass.)
     **/
    public static <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20,T21> Pattern<Product21<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20,T21>> Tuple(Pattern<T1> pattern1, Pattern<T2> pattern2, Pattern<T3> pattern3, Pattern<T4> pattern4, Pattern<T5> pattern5, Pattern<T6> pattern6, Pattern<T7> pattern7, Pattern<T8> pattern8, Pattern<T9> pattern9, Pattern<T10> pattern10, Pattern<T11> pattern11, Pattern<T12> pattern12, Pattern<T13> pattern13, Pattern<T14> pattern14, Pattern<T15> pattern15, Pattern<T16> pattern16, Pattern<T17> pattern17, Pattern<T18> pattern18, Pattern<T19> pattern19, Pattern<T20> pattern20, Pattern<T21> pattern21) {
        return new Pattern<Product21<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20,T21>>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Product21<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20,T21> value) throws PatternMatchReject {
                if (value == null) reject();
                pattern1.apply(mgr, value._1());
                pattern2.apply(mgr, value._2());
                pattern3.apply(mgr, value._3());
                pattern4.apply(mgr, value._4());
                pattern5.apply(mgr, value._5());
                pattern6.apply(mgr, value._6());
                pattern7.apply(mgr, value._7());
                pattern8.apply(mgr, value._8());
                pattern9.apply(mgr, value._9());
                pattern10.apply(mgr, value._10());
                pattern11.apply(mgr, value._11());
                pattern12.apply(mgr, value._12());
                pattern13.apply(mgr, value._13());
                pattern14.apply(mgr, value._14());
                pattern15.apply(mgr, value._15());
                pattern16.apply(mgr, value._16());
                pattern17.apply(mgr, value._17());
                pattern18.apply(mgr, value._18());
                pattern19.apply(mgr, value._19());
                pattern20.apply(mgr, value._20());
                pattern21.apply(mgr, value._21());
            }

            @Override
            public String toString() {
                return "Tuple(" + pattern1 + "," + pattern2 + "," + pattern3 + "," + pattern4 + "," + pattern5 + "," + pattern6 + "," + pattern7 + "," + pattern8 + "," + pattern9 + "," + pattern10 + "," + pattern11 + "," + pattern12 + "," + pattern13 + "," + pattern14 + "," + pattern15 + "," + pattern16 + "," + pattern17 + "," + pattern18 + "," + pattern19 + "," + pattern20 + "," + pattern21 + ")";
            }
        };
    }

    /** Pattern that matches a Scala 22-tuple.
     * (More precisely, a value of type {@link Product22}, of which {@link Tuple22} is a subclass.)
     **/
    public static <T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20,T21,T22> Pattern<Product22<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20,T21,T22>> Tuple(Pattern<T1> pattern1, Pattern<T2> pattern2, Pattern<T3> pattern3, Pattern<T4> pattern4, Pattern<T5> pattern5, Pattern<T6> pattern6, Pattern<T7> pattern7, Pattern<T8> pattern8, Pattern<T9> pattern9, Pattern<T10> pattern10, Pattern<T11> pattern11, Pattern<T12> pattern12, Pattern<T13> pattern13, Pattern<T14> pattern14, Pattern<T15> pattern15, Pattern<T16> pattern16, Pattern<T17> pattern17, Pattern<T18> pattern18, Pattern<T19> pattern19, Pattern<T20> pattern20, Pattern<T21> pattern21, Pattern<T22> pattern22) {
        return new Pattern<Product22<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20,T21,T22>>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Product22<T1,T2,T3,T4,T5,T6,T7,T8,T9,T10,T11,T12,T13,T14,T15,T16,T17,T18,T19,T20,T21,T22> value) throws PatternMatchReject {
                if (value == null) reject();
                pattern1.apply(mgr, value._1());
                pattern2.apply(mgr, value._2());
                pattern3.apply(mgr, value._3());
                pattern4.apply(mgr, value._4());
                pattern5.apply(mgr, value._5());
                pattern6.apply(mgr, value._6());
                pattern7.apply(mgr, value._7());
                pattern8.apply(mgr, value._8());
                pattern9.apply(mgr, value._9());
                pattern10.apply(mgr, value._10());
                pattern11.apply(mgr, value._11());
                pattern12.apply(mgr, value._12());
                pattern13.apply(mgr, value._13());
                pattern14.apply(mgr, value._14());
                pattern15.apply(mgr, value._15());
                pattern16.apply(mgr, value._16());
                pattern17.apply(mgr, value._17());
                pattern18.apply(mgr, value._18());
                pattern19.apply(mgr, value._19());
                pattern20.apply(mgr, value._20());
                pattern21.apply(mgr, value._21());
                pattern22.apply(mgr, value._22());
            }

            @Override
            public String toString() {
                return "Tuple(" + pattern1 + "," + pattern2 + "," + pattern3 + "," + pattern4 + "," + pattern5 + "," + pattern6 + "," + pattern7 + "," + pattern8 + "," + pattern9 + "," + pattern10 + "," + pattern11 + "," + pattern12 + "," + pattern13 + "," + pattern14 + "," + pattern15 + "," + pattern16 + "," + pattern17 + "," + pattern18 + "," + pattern19 + "," + pattern20 + "," + pattern21 + "," + pattern22 + ")";
            }
        };
    }
}
