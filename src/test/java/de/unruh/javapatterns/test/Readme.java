package de.unruh.javapatterns.test;

import de.unruh.javapatterns.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import static de.unruh.javapatterns.Patterns.*;
import static de.unruh.javapatterns.Match.*;

import static java.lang.System.out;

@SuppressWarnings({"Convert2MethodRef", "InnerClassMayBeStatic"})
public class Readme {

    interface Term {}
    public class Plus implements Term {
        public final Term a, b;
        Plus(Term a, Term b) { this.a = a; this.b = b; }
        public String toString() { return "Plus("+a+","+b+")"; } }
    Term Plus(Term a, Term b) { return new Plus(a,b); }

    public class Minus implements Term {
        public final Term a, b;
        Minus(Term a, Term b) { this.a = a; this.b = b; }
        public String toString() { return "Minus("+a+","+b+")"; } }
    Term Minus(Term a, Term b) { return new Minus(a,b); }

    public class Times implements Term {
        public final Term a, b;
        Times(Term a, Term b) { this.a = a; this.b = b; }
        public String toString() { return "Times("+a+","+b+")"; } }
    Term Times(Term a, Term b) { return new Times(a,b); }

    public class Divide implements Term {
        public final Term a, b;
        Divide(Term a, Term b) { this.a = a; this.b = b; }
        public String toString() { return "Divide("+a+","+b+")"; } }
    Term Divide(Term a, Term b) { return new Divide(a,b); }

    public class Variable implements Term {
        public final String name;
        Variable(String name) { this.name = name; }
        public String toString() { return name; } }
    Term Variable(String name) { return new Variable(name); }

    public class Number implements Term {
        public final int value;
        Number(int value) { this.value = value; }
        public String toString() { return Integer.toString(value); } }
    Term Number(int i) { return new Number(i); }

    Pattern<Term> Plus(Pattern<? super Term> patternA, Pattern<? super Term> patternB) {
        return new Pattern<>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Term term) throws PatternMatchReject {
                if (!(term instanceof Plus)) reject();
                patternA.apply(mgr, ((Plus)term).a);
                patternB.apply(mgr, ((Plus)term).b);
            }
            @Override public String toString() {
                return "Plus("+patternA+","+patternB+")";
            }
        };
    }

    Pattern<Term> Minus(Pattern<? super Term> patternA, Pattern<? super Term> patternB) {
        return new Pattern<>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Term term) throws PatternMatchReject {
                if (!(term instanceof Minus)) reject();
                patternA.apply(mgr, ((Minus)term).a);
                patternB.apply(mgr, ((Minus)term).b);
            }
            @Override public String toString() {
                return "Minus("+patternA+","+patternB+")";
            }
        };
    }

    Pattern<Term> Times(Pattern<? super Term> patternA, Pattern<? super Term> patternB) {
        return new Pattern<>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Term term) throws PatternMatchReject {
                if (!(term instanceof Times)) reject();
                patternA.apply(mgr, ((Times)term).a);
                patternB.apply(mgr, ((Times)term).b);
            }
            @Override public String toString() {
                return "Times("+patternA+","+patternB+")";
            }
        };
    }

    Pattern<Term> Divide(Pattern<? super Term> patternA, Pattern<? super Term> patternB) {
        return new Pattern<>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Term term) throws PatternMatchReject {
                if (!(term instanceof Divide)) reject();
                patternA.apply(mgr, ((Divide)term).a);
                patternB.apply(mgr, ((Divide)term).b);
            }
            @Override public String toString() {
                return "Divide("+patternA+","+patternB+")";
            }
        };
    }

    Pattern<Term> Variable(Pattern<? super String> pattern) {
        return new Pattern<>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Term term) throws PatternMatchReject {
                if (!(term instanceof Variable)) reject();
                pattern.apply(mgr, ((Variable)term).name);
            }
            @Override public String toString() {
                return "Variable("+pattern+")";
            }
        };
    }

    Pattern<Term> Number(Pattern<? super Integer> pattern) {
        return new Pattern<>() {
            @Override
            public void apply(@NotNull MatchManager mgr, @Nullable Term term) throws PatternMatchReject {
                if (!(term instanceof Number)) reject();
                pattern.apply(mgr, ((Number)term).value);
            }
            @Override public String toString() {
                return "Number("+pattern+")";
            }
        };
    }

    boolean equal(Term t1, Term t2) throws MatchException {
        Capture<Term> a1 = Capture("a1");
        Capture<Term> a2 = Capture("a2");
        Capture<Term> b1 = Capture("b1");
        Capture<Term> b2 = Capture("b2");
        Capture<Integer> i1 = Capture("i1");
        Capture<Integer> i2 = Capture("i2");
        Capture<String> x1 = Capture("x1");
        Capture<String> x2 = Capture("x2");

        return match(
                new Term[] { t1, t2 },

                Array(Plus(a1, b1), Plus(a2, b2)), ()
                        -> equal(a1.v(), a2.v()) && equal(b1.v(), b2.v()),
                Array(Minus(a1, b1), Minus(a2, b2)), ()
                        -> equal(a1.v(), a2.v()) && equal(b1.v(), b2.v()),
                Array(Times(a1, b1), Times(a2, b2)), ()
                        -> equal(a1.v(), a2.v()) && equal(b1.v(), b2.v()),
                Array(Divide(a1, b1), Divide(a2, b2)), ()
                        -> equal(a1.v(), a2.v()) && equal(b1.v(), b2.v()),
                Array(Number(i1), Number(i2)), ()
                        -> i1.v().equals(i2.v()),
                Array(Variable(x1), Variable(x2)), ()
                        -> x1.v().equals(x2.v()),
                Any, () -> false
        );
    };

    Term simplify1(Term t) throws MatchException {
        Capture<Term> a = Capture("a");
        Capture<Term> b = Capture("b");
        Capture<Term> c = Capture("c");
        Capture<Integer> i = Capture("i");
        Capture<Integer> j = Capture("j");
        Capture<String> x = Capture("x");

        return match(t,
                Plus(a, Number(Is(0))), () -> a.v(),
                Plus(Number(Is(0)), a), () -> a.v(),
                Minus(a, Number(Is(0))), () -> a.v(),
                Times(a, Number(Is(1))), () -> a.v(),
                Times(Number(Is(1)), a), () -> a.v(),
                Times(a, Number(Is(0))), () -> Number(0),
                Times(Number(Is(0)), a), () -> Number(0),
                Plus(Variable(x), Variable(Is(x))), () -> Times(Number(2), Variable(x.v())),
                Divide(a, Number(Is(1))), () -> a.v(),
                Divide(Number(Is(0)), a), () -> Number(0),
                Plus(Number(i), Number(j)), () -> Number(i.v() + j.v()),
                Times(Number(i), Number(j)), () -> Number(i.v() * j.v()),
                Minus(Number(i), Number(j)), () -> Number(i.v() - j.v()),
                Divide(Number(i), Number(j)), () -> {
                    if (j.v() != 0 && i.v() % j.v() == 0)
                        return Number(i.v() / j.v());
                    else { Pattern.reject(); return null; } },
                Times(a,Times(b,c)), () -> Times(Times(a.v(),b.v()),c.v()),
                Plus(a,Plus(b,c)), () -> Plus(Plus(a.v(),b.v()),c.v()),

                Plus(a, b), () -> Plus(simplify1(a.v()), simplify1(b.v())),
                Times(a, b), () -> Times(simplify1(a.v()), simplify1(b.v())),
                Minus(a, b), () -> Minus(simplify1(a.v()), simplify1(b.v())),
                Divide(a, b), () -> Divide(simplify1(a.v()), simplify1(b.v())),
                Any, () -> t
        );
    }

    Term simplify(Term term) throws MatchException {
       Term simplified = simplify1(term);
       if (equal(simplified, term))
           return term;
       else
           return simplify(simplified);
    }

    @Test
    void readmeExample() throws MatchException {

        Term term = new Times(new Minus(new Number(1), new Number(2)), new Plus(new Variable("x"), new Variable("x")));

        out.println(term);
        // ==> Times(Minus(1,2),Plus(x,x))
        out.println(simplify(term));
        // ==> Times(-2,x)
    }
}
