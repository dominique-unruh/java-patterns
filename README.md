# java-patterns

[![Build Status](https://travis-ci.com/dominique-unruh/java-patterns.svg?branch=master)](https://travis-ci.com/dominique-unruh/java-patterns)
[![Javadoc](https://javadoc.io/badge2/de.unruh/java-patterns/javadoc.svg)](https://javadoc.io/doc/de.unruh/java-patterns/latest/de/unruh/javapatterns/index.html)
[![Gitter chat](https://img.shields.io/badge/gitter-chat-brightgreen.svg)](https://gitter.im/dominique-unruh/java-patterns?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

<!-- toc -->
- __[What is this library for?](#what-is-this-library-for)__
- __[Notable features](#notable-features)__
- __[Prerequisites](#prerequisites)__
- __[Installation](#installation)__
- __[Comparison with other approaches](#comparison-with-other-approaches)__
  - __[Java 14 pattern matching](#java-14-pattern-matching)__
  - __[Vavr](#vavr)__
- __[Example](#example)__
  - __[Defining the datatype](#defining-the-datatype)__
  - __[Defining the patterns](#defining-the-patterns)__
  - __[Implementing the simplifier](#implementing-the-simplifier)__
- __[Further reading](#further-reading)__
<!-- /toc -->

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
can be [similarly improved](https://github.com/dominique-unruh/scala-isabelle/blob/4e8c1c8dbb69797e408be7076caa48cf863180af/src/test/scala/de/unruh/isabelle/JavaExample.java#L84).)

## Notable features

* **Nested pattern matching:** Patterns can describe arbitrarily nested structures, in a syntax
  which mirrors the structure of the term that is matched. This is very common in functional languages.
* **User designed patterns:** It is easy to create own pattern matchers, e.g., for new datatypes,
  or for derived properties (e.g., the API doc for 
  [Pattern](https://javadoc.io/doc/de.unruh/java-patterns/latest/de/unruh/javapatterns/Pattern.html)
  shows an example how to create a pattern
  such that `FullName(first,last)` matches a string consisting of two names). This feature is also
  available, e.g., in Scala. However, our approach gives patterns greater flexibility what to do 
  with subpatterns (e.g., change how they are matched depending on other parts of the match / 
  other arguments to the pattern). See 
  [Pattern](https://javadoc.io/doc/de.unruh/java-patterns/latest/de/unruh/javapatterns/Pattern.html) for instructions.
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
  See [Pattern.reject](https://javadoc.io/doc/de.unruh/java-patterns/latest/de/unruh/javapatterns/Pattern.html#reject()).
* **Reading captured values during match:** If a pattern assigns a value to a capture variable `x`,
  then other parts of the pattern can already depend on that value (i.e., `x.v()` may be used).
  For example, `Array(x, Is(x))` matches arrays with two identical entries. (`Is(x)` compares the
  matched value with `x.v()`.)
* **And-patterns:** And-patterns allow to require a value to match several patterns
  simpultaneously. For example `And(x, Is(s -> s.length() <= 100))` would match a string of length
  `<= 100` and assign it to `x`. See
  [And](https://javadoc.io/doc/de.unruh/java-patterns/latest/de/unruh/javapatterns/Patterns.html#And(de.unruh.javapatterns.Pattern...)).
* **Or-patterns and backtracking:** Or-patterns allow to require that one out of several patterns
  match. For example, `Or(Array(x), Array(x, Any))` would match an array with one or two elements, and
  assign the first element to `x`. Due to the backtracking support, capture variables assigned during a 
  subpattern that failed will not be considered assigned (which makes it possible to use `x` twice in the
  above pattern). See
  [Or](https://javadoc.io/doc/de.unruh/java-patterns/latest/de/unruh/javapatterns/Patterns.html#Or(de.unruh.javapatterns.Pattern...)).
  (And
  [MatchManager.protectedBlock](https://javadoc.io/doc/de.unruh/java-patterns/latest/de/unruh/javapatterns/Patterns.html#protectedBlock(de.unruh.javapatterns.PatternRunnable))
  for how to use this feature in user-defined patterns.) 

**Some limitations:**

* All capture variables need to be explicitly declared (`Capture<T> x = capture("x")`), adding boilerplate.
  At least, this can be done outside the pattern match itself so that the pattern match doesn't become less
  readable.
* Since there is no compiler support, the syntax for pattern matches is not as clean as one would like.
  (E.g., like in Scala.)  
* Certain checks are done only at runtime. E.g., `Array(x,x)` will fail because the same capture variable 
  would be assigned twice. But this is not noticed at compile time.

## Prerequisites

Java 8+ is required during compile and runtime.

Some build system such as [gradle](https://gradle.org/) is recommended.

## Installation

The library is available on Maven Central as [de.unruh/java-patterns](https://mvnrepository.com/artifact/de.unruh/java-patterns).

For example, using gradle, you can include the library in your project like this:
```groovy
repositories {
    mavenCentral()
}
dependencies {
    implementation 'de.unruh:java-patterns:0.1.0-RC1'
}
```
And for the
[development snapshot](https://oss.sonatype.org/content/repositories/snapshots/de/unruh/java-patterns/master-SNAPSHOT/),
use:
```groovy
repositories {
    maven { url 'https://oss.sonatype.org/content/repositories/snapshots' }
}
dependencies {
    implementation 'de.unruh:java-patterns:master-SNAPSHOT'
}
```

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

The [vavr library](https://www.vavr.io/) also has support for [pattern matching in Java](https://www.vavr.io/vavr-docs/#_pattern_matching).
For example, we could match a term with the following syntax:
```Java
Match(term).of(
  Case( $Plus( $Minus($(),$()), $() ), 
        (xy, z) -> doSomething(xy, z)),
  Case( $(), 
        () -> doSomethingElse())   
)
```
Here `$()` matches anything, and patterns `$Plus` and `$Minus` can be user-defined.
Note, however, that we did not write `doSomething(x,y,z)` here but `doSomething(xy,z)`.
This is because vavr does a deep match (i.e., it ensures that there is a `Minus` as the first argument 
to `Plus`), but only the arguments to the toplevel pattern `$Plus` are bound to variables and
available to the match action (according to [Section 3.6.2](https://www.vavr.io/vavr-docs/#_patterns) in
the docs). If we want to gets `x` and `y`, we need to pattern match `xy` again. This makes it difficult to
use vavr for nested terms. 

**Pros:**

* Support for user defined patterns. (Similar to those in Scala, but not as flexible as in our library.)
* No need to explicitly declare capture variables. (In our library, we need to declare them using
  `Capture<T> x = capture("x")`.)

**Cons:**

* Not possible to capture matched values that are deeply embedded in a pattern.
* Requires to use a special annotation processor for defining new patterns.

## Example

We give an full example, from the definition of a datatype for terms, through the definition of corresponding patterns,
to the use of those patterns in the definition of a simple simplifier. The full source code of the example
can be found in [Readme.java](https://github.com/dominique-unruh/java-patterns/blob/master/src/test/java/de/unruh/javapatterns/test/Readme.java).

### Defining the datatype

In our example, we want to operate on simple terms with addition, multiplication, subtraction, division, 
integer variables, and integer literals. We define an (empty) interface `Term` for our terms:
```Java
interface Term {}
```
And then for each possible operation, we define a subclass of `Term`. For example, `Plus` is defined as follows:
```Java
public class Plus implements Term {
    public final Term a, b;
    Plus(Term a, Term b) { this.a = a; this.b = b; }
    public String toString() { return "Plus("+a+","+b+")"; } }

Term Plus(Term a, Term b) { return new Plus(a,b); }
```
(The function `Plus` is for convenience so that we can avoid writing `new` all the time.)

Analogous definitions are given for `Minus(Term,Term)`, `Times(Term,Term)`, `Divide(Term,Term)`. 
And we also define `Variable(String)` and `Number(int)`, both also subclasses of `Term`. 
We omit those definitions here, see the full source code.

With these classes, we can define a term, say `(1-2) * (x + x)`, as follows:
```Java
Term term = Times(Minus(Number(1), Number(2)), Plus(Variable("x"), Variable("x")));
```

### Defining the patterns

Now for each of `Plus`, `Minus`, `Times`, `Divide`, `Variable`, and `Number`, we have to define a pattern.
We show the definition for `Plus`:
```Java
Pattern<Term> Plus(Pattern<? super Term> patternA, Pattern<? super Term> patternB) {
    return new Pattern<>() {
        public void apply(MatchManager mgr, Term term) throws PatternMatchReject {
            if (!(term instanceof Plus)) reject();
            patternA.apply(mgr, ((Plus)term).a);
            patternB.apply(mgr, ((Plus)term).b);
        }
        
        public String toString() {
            return "Plus(" + patternA + "," + patternB + ")";
        }
    };
}
```
This function creates a pattern for matching `Term`s (type: `Pattern<Term>`), given two subpatterns
`patternA`, `patternB` for matching terms. This is done by creating an anonymous subclass of `Pattern<Term>`,
with the logic of the pattern contained in the overwritten `apply` method. The apply method is invoked when
a term `term` should be pattern matched. The first step is:
```Java
if (!(term instanceof Plus)) reject();
```
This rejects `term` if it is not of type `Plus`, i.e., any terms beside `Plus` do not match this pattern.
Then
```Java
patternA.apply(mgr, ((Plus)term).a);
```
applies the subpattern `patternA` to the first argument `((Plus)term).a` of the plus-term. 
The subpattern may also reject the term, but we do not need to write explicit code for handling that
since the rejection is implemented via an exception. 

The next line does the analogous operation for the second argument of the plus-term.

See the API doc for [Pattern](https://javadoc.io/doc/de.unruh/java-patterns/latest/de/unruh/javapatterns/Pattern.html)
for more information how to define new patterns. 

We define similar patterns for `Minus`, `Times`, `Divide`, `Variable`, `Number`.
We omit those definitions here, see the full source code.

This is the setup that needs to be done once and for all to provide pattern matching support for our `Term` class.

### Implementing the simplifier

We now use the patterns to define two functions on terms. First, we define an equality check:
```Java
boolean equal(Term t1, Term t2) throws MatchException {
``` 
This function should check whether `t1` and `t2` are equal. To do pattern matching, we 
first need to declare the capture variables that we need. We will need some variables for terms,
strings, and integers:
```Java
    Capture<Term> a1 = capture("a1"),
                  a2 = capture("a2"),
                  b1 = capture("b1"),
                  b2 = capture("b2");
    Capture<Integer> i1 = capture("i1"),
                     i2 = capture("i2");
    Capture<String> x1 = capture("x1"),
                    x2 = capture("x2");
```
And now we decide equality by returning `true` for the six cases where `t1`, `t2` have the
same shape (and the subterms are equal), and `false` otherwise:
```Java
    return match(
            new Term[] { t1, t2 },

            Array(Plus(a1, b1), Plus(a2, b2)),
                    () -> equal(a1.v(), a2.v()) && equal(b1.v(), b2.v()),
            Array(Minus(a1, b1), Minus(a2, b2)),
                    () -> equal(a1.v(), a2.v()) && equal(b1.v(), b2.v()),
            Array(Times(a1, b1), Times(a2, b2)),
                    () -> equal(a1.v(), a2.v()) && equal(b1.v(), b2.v()),
            Array(Divide(a1, b1), Divide(a2, b2)),
                    () -> equal(a1.v(), a2.v()) && equal(b1.v(), b2.v()),
            Array(Number(i1), Number(i2)),
                    () -> i1.v().equals(i2.v()),
            Array(Variable(x1), Variable(x2)),
                    () -> x1.v().equals(x2.v()),
            Any, () -> false
    );
}
```
We are creating an array containing `t1`, `t2` to do the pattern match, and then for each possible
case, we add one case. For example, `Array(Plus(a1, b1), Plus(a2, b2))` matches
if both terms are plus-terms, and in that case we return true iff the left operands `a1`, `a2` 
are equal and the right operands `b1`, `b2` are equal (that latter equality test is checked by
a recursive call to the `equals` function we are defining).

Now we define a simplifier for terms:
```Java
Term simplify1(Term term) throws MatchException {
```
This function is support to return the term, with at least one simplification operations applied.
(The full simplifier `simplify` comes below.)
As before, we define the capture variables that we use:
```Java
    Capture<Term> a = capture("a"),
                  b = capture("b"),
                  c = capture("c");
    Capture<Integer> i = capture("i"),
                     j = capture("j");
    Capture<String> x = capture("x");
```
and then we implement the actual simplification as a big pattern match:
```Java
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
            Any, () -> term
    );
}
```
We describe only a few selected of those cases. The last five lines descend into the term 
if no simplification rule is applicable. Or return `term` unchanged if even that is not possible.

The rule `Plus(a, Number(Is(0))), () -> a.v()` matches the term if it is of the form 
`Plus(a,Number(0))` (the pattern `Is` checks for equality with its argument), and if so, returns
`a` (we need to write `a.v()` to retrieve the captured value). That is, it implements the 
rewrite rule `a+0 -> a`.

The rule `Plus(Variable(x), Variable(Is(x))), () -> Times(Number(2), Variable(x.v()))`
matches a term of the form `Plus(Variable(x), Variable(x))` (with the same `x`!)
and replaces it by `Times(Number(2), Variable(x))`. That is, it implements the rewrite rule
`x+x -> 2*x` This pattern uses the feature of matching against an already assigned capture variable:
`Is(x)` only matches terms that equal the already assigned value `x`. Thus this pattern will not
match `Plus(Variable(x), Variable(y))` for `x` ≠ `y`.

The rule `Plus(Number(i), Number(j)), () -> Number(i.v() + j.v())` performs a computation:
If two integer literals are added, we replace it by a new literal containing the sum.

The rule
```Java
    Divide(Number(i), Number(j)), () -> {
        if (j.v() != 0 && i.v() % j.v() == 0)
            return Number(i.v() / j.v());
        else { Pattern.reject(); return null; } },
```
matches terms of the form `Divide(Number(i), Number(j))`, i.e., the divison of
integer literals. And then it returns an integer literal `Number(i.v() / j.v())` that
is the quotient. However, we cannot perform this simplification if we would perform a division 
by zero, or not get an integer. So we have the additional check `j.v() != 0 && i.v() % j.v() == 0`
and if it fails, call `Pattern.reject()` to abort the match. In that case, pattern matching will
continue with the next pattern as if `Divide(Number(i), Number(j))` had not matched in the
first place.

This concludes the definition of `simplify1`. However, `simplify1` may not completely simplify the
term after one rule has been applied. So we define a function `simplify` that keeps applying
`simplify1` until the term does not change any more. (To detect change, we use the `equals` 
function defined above.)
```Java
Term simplify(Term term) throws MatchException {
   Term simplified = simplify1(term);
   if (equal(simplified, term))
       return term;
   else
       return simplify(simplified);
}
```

And finally, we test the simplifier:
```
Term term = Times(Minus(Number(1), Number(2)), Plus(Variable("x"), Variable("x")));

out.println(term);
// ==> Times(Minus(1,2),Plus(x,x))
out.println(simplify(term));
// ==> Times(-2,x)
```

## Further reading

For further information, see the [API doc](https://javadoc.io/doc/de.unruh/java-patterns/latest/de/unruh/javapatterns).
See [Match](https://javadoc.io/doc/de.unruh/java-patterns/latest/de/unruh/javapatterns/Match.html) 
for instructions how to do pattern matches, 
[Patterns](https://javadoc.io/doc/de.unruh/java-patterns/latest/de/unruh/javapatterns/Patterns.html)
for the predefined patterns, and
[Pattern](https://javadoc.io/doc/de.unruh/java-patterns/latest/de/unruh/javapatterns/Pattern.html)
for instructions how to define own patterns.
