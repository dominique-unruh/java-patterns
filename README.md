# java-patterns

[![Build Status](https://travis-ci.com/dominique-unruh/java-patterns.svg?branch=master)](https://travis-ci.com/dominique-unruh/java-patterns)
[![Javadoc](https://javadoc.io/badge2/de.unruh/java-patterns/javadoc.svg)](https://javadoc.io/doc/de.unruh/java-patterns/latest/de/unruh/javapatterns/index.html)
[![Gitter chat](https://img.shields.io/badge/gitter-chat-brightgreen.svg)](https://gitter.im/dominique-unruh/java-patterns?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

Library for functional pattern matching in Java

## Example

```Java
interface Term {}

public class Plus implements Term {
    public final Term a, b;
    Plus(Term a, Term b) { this.a = a; this.b = b; }
    public String toString() { return "Plus("+a+","+b+")"; } }

Term Plus(Term a, Term b) { return new Plus(a,b); }
```
And similar for `Minus(Term,Term)`, `Times(Term,Term)`, `Divide(Term,Term)`, `Variable(String)`, `Number(int)`.

```Java
Pattern<Term> Plus(Pattern<? super Term> patternA, Pattern<? super Term> patternB) {
    return new Pattern<>() {
        @Override public void apply(MatchManager mgr, Term term) throws PatternMatchReject {
            if (!(term instanceof Plus)) reject();
            patternA.apply(mgr, ((Plus)term).a);
            patternB.apply(mgr, ((Plus)term).b);
        }
        @Override public String toString() {
            return "Plus("+patternA+","+patternB+")";
        }
    };
}
```
And similar for `Minus(Term,Term)`, `Times(Term,Term)`, `Divide(Term,Term)`, `Variable(String)`, `Number(int)`.

```Java
boolean equal(Term t1, Term t2) throws MatchException {
    Capture<Term> a1 = new Capture<>("a1");
    Capture<Term> a2 = new Capture<>("a2");
    Capture<Term> b1 = new Capture<>("b1");
    Capture<Term> b2 = new Capture<>("b2");
    Capture<Integer> i1 = new Capture<>("i1");
    Capture<Integer> i2 = new Capture<>("i2");
    Capture<String> x1 = new Capture<>("x1");
    Capture<String> x2 = new Capture<>("x2");

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
```

```Java
Term simplify1(Term t) throws MatchException {
    Capture<Term> a = new Capture<>("a");
    Capture<Term> b = new Capture<>("b");
    Capture<Term> c = new Capture<>("c");
    Capture<Integer> i = new Capture<>("i");
    Capture<Integer> j = new Capture<>("j");
    Capture<String> x = new Capture<>("x");

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
```

```Java
Term simplify(Term term) throws MatchException {
   Term simplified = simplify1(term);
   if (equal(simplified, term))
       return term;
   else
       return simplify(simplified);
}
```

```Java
Term term = new Times(new Minus(new Number(1), new Number(2)), new Plus(new Variable("x"), new Variable("x")));

out.println(term);
// ==> Times(Minus(1,2),Plus(x,x))
out.println(simplify(term));
// ==> Times(-2,x)
```
