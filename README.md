# java-patterns

[![Build Status](https://travis-ci.com/dominique-unruh/java-patterns.svg?branch=master)](https://travis-ci.com/dominique-unruh/java-patterns)
[![Javadoc](https://javadoc.io/badge2/de.unruh/java-patterns/javadoc.svg)](https://javadoc.io/doc/de.unruh/java-patterns/latest/de/unruh/javapatterns/index.html)
[![Gitter chat](https://img.shields.io/badge/gitter-chat-brightgreen.svg)](https://gitter.im/dominique-unruh/java-patterns?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

## What is this library for?

This library provides support for functional pattern matching in Java.
 
In many functional languages, 
it is possible to explore the structure of a term easily and recursively using pattern matches. 
For example, if we have a term `term = Plus(Minus(a,b),c)`(assuming some reasonable definitions of
`Plus`, `Minus` and a suitable setup), in [Scala](https://www.scala-lang.org/) we can find
`a`, `b`, and `c` using the following code:
```Scala
term match {
  case Plus(Minus(x, y), z) => doSomething(x, y, z)
  case _ => doSomethingElse()
}
```
How do we do the same in Java? Since Java does not support functional pattern matching, we need
to write something like:
```Java
if (term instanceof Plus) {
  Plus plus = (Plus)term;
  Term xy = plus.a;
  Term z = plus.b;
  if (xy instanceof Minus) {
    Minus minus = (Minus)xy;
    Term x = minus.a;
    Term y = minus.b;
    doSomething(x,y,z);
  } else
    doSomethingElse();
} else
  doSomethingElse();
```
This is definintely harder to read. Also note the duplication of `doSomethingElse()` which would
become even more drastic if we had a more complex pattern. And if the pattern match had more than 
two cases, the nesting of `if`'s and `else`s would become even more complex and lead to even more 
duplication. (The duplication can be avoided using clever use of `break`s to simulate `goto`s,
but the code stays very hard to read. See the following implementation of a simple 
[`replace` function](https://github.com/dominique-unruh/scala-isabelle/blob/14e3b85af0825359af82f559a6a59337a336363c/src/test/scala/de/unruh/isabelle/JavaExample.java#L22)
in Java, ~50 lines long even using `break`s, to implement a five line
[`Scala` function](https://github.com/dominique-unruh/scala-isabelle/blob/14e3b85af0825359af82f559a6a59337a336363c/src/test/scala/de/unruh/isabelle/Example.scala#L32)).

This library solves this problem. Using it, we can write the pattern match in Java as:
```Java
Capture<Term> x = capture("x"), y = capture("y"), z = capture("z");

match(term,
  Plus(Minus(x, y), z), () -> doSomething(x.v(), y.v(), z.v()),
  Any, () -> doSomethingElse())
``` 
While there is still unnecessary syntactic noise (the `() ->`, and the explicit declaration of the capture
variables `x`, `y`, `z`), the structure of the
code is now the same as in Scala. (And the implementation of the abovementioned `replace` function
can be [similarly improved](https://github.com/dominique-unruh/scala-isabelle/blob/f6fd2433444a0c2232baebb4a91d5e96c9d8f3a5/src/test/scala/de/unruh/isabelle/experiments/JavaPatterns.java#L68).)

## Notable features

* **Nested pattern matching:** Patterns can describe arbitrarily nested structures, in a syntax
  which mirrors the structure of the term that is matched. This is very common in functional languages.
* **User designed patterns:** It is easy to create own pattern matchers, e.g., for new datatypes,
  or for derived properties (e.g., the API doc for Pattern (TODO: link) shows how to create a pattern
  such that `FullName(first,last)` matches a string consisting of two names). This feature is also
  available, e.g., in Scala. However, our approach gives patterns greater flexibility what to do 
  with subpatterns (e.g., change how they are matched depending on other parts of the match / 
  other arguments to the pattern).
* **Late match failures:** When a pattern has matched and the corresponding action is executed,
  that action can still declare the match as a failure and matching continues with the next available
  pattern. E.g., 
  ```Java
  match(person,
      Person(name, personId), () -> { 
          PersonData data = lookup(personId.v());
          if (data==null) reject();
          dostuff(data); }
      Person(name, Any), () -> reportUnknownPerson(name.v()))
  ```
* **Reading captured values during match:** If a pattern assigns a value to a capture variable `x`,
  then other parts of the pattern can already depend on that value (i.e., `x.v()` may be used).
  For example, `Array(x, Is(x))` matches arrays with two identical entries. (`Is(x)` compares the
  matched value with `x.v()`.)
* **And-patterns:** TODO 
* **Or-patterns and backtracking:**: TODO


**Some limitations:**

* All capture variables need to be explicitly declared (`Capture<T> x = capture("x")`), adding boilerplate.
  At least, this can be done outside the pattern match itself so that the pattern match doesn't become less
  readable.
* Since there is no compiler support, the syntax for pattern matches is not as clean as one would like.
  (E.g., like in Scala.)  
* Certain checks are done only at runtime. E.g., `Array(x,x)` will fail because the same capture variable 
  would be assigned twice. But this is not noticed at compile time.

## Comparison with other approaches

**Caveat:** I have not worked with those approaches, my discussions of pros/cons is based only on
reading their documentation. 

### Java 14 pattern matching

Java 14 has a preview feature supporting a limited form of 
[pattern matching](https://docs.oracle.com/en/java/javase/14/language/pattern-matching-instanceof-operator.html).
The only supported pattern is a type test (`instanceof`). Using that approach, the above example becomes
```Java
if (term instanceof Plus plus && term.a instanceof Minus minus)
  doSomething(minus.a, minus.b, plus.b);
else
  doSomethingElse();
```

**Pros:**
* Needs no extra library.
* No need to declare capture variables (like `Capture<...> x = new Capture<>(...)` in this library).
* Compared to plain Java, the code is much cleaner and avoids code repetitions.

**Cons:**
* The pattern match needs to be unfolded. That is, to match a term of the shape `Plus(Minus(x, y), z)`
  we cannot write a pattern of the same nested structure `Plus(Minus(x, y), z)`, instead we have
  a sequence of accesses (like `minus.a`) interspersed with matches (like `instanceof Minus minus`),
  and we need to use a number of auxiliary variables (like `plus`, `minus`) that are not needed in 
  the action (`doSomething(...)`).
* The pattern match is restricted to dynamic type checks. This works fine for the datatype in 
  our example but will fail if the patterns do not follow the inheritance structure. (E.g.,
  if we want a pattern `BinaryOp(x,y)` that should match both `Plus` and `Minus`.)

### Vavr

TODO

https://github.com/vavr-io/vavr-match

https://www.vavr.io/vavr-docs/#_pattern_matching

```Java
Match(term).of(
  Case( $Plus( $Minus($(),$()), $() ), 
        (xy, z) -> doSomething(xy, z)),
  Case( $(), 
        () -> doSomethingElse())   
)
```
Note about `xy`.

TODO

* Pros: User defined patterns
* Pros: No need to declare capture variables
* Cons: Pattern/name binding loose
* Cons: requires annotation processor
* Cons: cannot capture more deeply

## Prerequisites

TODO

## Installation

TODO

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
        @Override protected void apply(MatchManager mgr, Term term) throws PatternMatchReject {
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
