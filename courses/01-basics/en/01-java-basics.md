🇬🇧 English | 🇩🇪 [Deutsch](../de/01-java-grundlagen.md)

[← Back to course overview](../../../README.md) · Previous: [Chapter 0 – Introduction & Tools](00-introduction.md) · Next: [Chapter 2 – Methods, Arrays and Input](02-methods-arrays-input.md) →

# Chapter 1 – Java Basics

**Goal:** this chapter is the language. By the end you will be able to store
values, calculate with them, compare them, and make a program decide and repeat.
That is everything the games in this repository are built from. Nothing here is
specific to games, and all of it comes back in every course that follows.

Work in one file called `Basics.java` and keep adding to `main` as you go. A
finished version of every section lives in [`code/Basics.java`](../code/Basics.java),
in the same order as the sections below.

## 🟢 Core — Putting something on the screen

You met `System.out.println` in Chapter 0. It has a sibling:

```java
System.out.println("A line of its own.");
System.out.print("No line break ");
System.out.println("until here.");
```

```
A line of its own.
No line break until here.
```

`println` ends the line, `print` does not.

To put a value inside a sentence, you join the pieces with `+`:

```java
String name = "Ada";
System.out.println("Hello, " + name + "!");
```

Some languages let you write the variable directly inside the text. Java does not.
You either join with `+`, or you use `printf`, which takes the sentence first and
the values after it:

```java
System.out.printf("%s is %d years old.%n", "Ada", 36);
```

```
Ada is 36 years old.
```

`%s` is where a piece of text goes, `%d` a whole number, and `%n` a line break.
`printf` earns its keep when you want columns to line up, which is exactly what a
game board needs, so it turns up again in Courses 2 and 5.

Save, compile and run after this section, and after every section from here on.
Each one tells you what you should see. If you see something else, the mistake is
still small and nearby.

## 🟢 Core — What you can store

A variable is a named place to keep one value. In Java you say what kind of value
it will hold, and that never changes afterwards:

```java
int whole = 42;
double fraction = 2.5;
boolean yes = true;
char letter = 'J';
String word = "Java";
```

Five types, and they cover nearly everything in this course. `int` is a whole
number. `double` has digits after the point. `boolean` is true or false, nothing
else. `char` is a single character in single quotes. `String` is text in double
quotes, and it is the one with a capital letter, which is a hint that it is not
quite like the other four.

The quotes matter: `'J'` is one character, `"J"` is text that happens to be one
character long. Java treats them differently and will say so.

Naming the type up front is what lets the compiler catch mistakes before the
program runs. Add this line and compile:

```java
whole = "forty-two";
```

```
Basics.java:12: error: incompatible types: String cannot be converted to int
```

In a language without this check, that line would be fine right up until something
tried to calculate with it. Delete the line again.

## 🟢 Core — Arithmetic, and one surprise

The five operators are the ones you would expect, with one that is less obvious:

```java
System.out.println(7 + 2);
System.out.println(7 - 2);
System.out.println(7 * 2);
System.out.println(7 / 2);
System.out.println(7 % 2);
```

```
9
5
14
3
1
```

`7 / 2` is **3**, not 3.5. When both sides are whole numbers, Java does whole-number
division and throws the rest away. If you want the rest, `%` gives it to you: `7 % 2`
is 1, what was left over.

This catches everyone once. If you want 3.5, make one side a `double`:

```java
System.out.println(7 / 2.0);
```

```
3.5
```

Both halves of this are useful rather than annoying. Course 2 turns a square number
from 1 to 9 into a row and a column with exactly these two operators, and Course 4
uses `%` to wrap a grid around its own edges.

## 🟢 Core — Casting, and your first random number

Going the other way, from a `double` to an `int`, loses information, so Java makes
you ask for it in writing:

```java
double exact = 3.9;
int cut = (int) exact;
System.out.println(cut);
```

```
3
```

`(int)` in front of a value is a **cast**. Note that it cuts rather than rounds:
3.9 becomes 3.

That is exactly what you need for a random number. `Math.random()` hands you a
`double` somewhere from 0 up to, but not including, 1. Multiply it by how many
values you want, cut off the rest, and shift it to where you want it to start:

```java
int roll = (int) (Math.random() * 6) + 1;
System.out.println("You rolled a " + roll + ".");
```

Run it a few times. `Math.random() * 6` lands somewhere from 0 up to just under 6,
the cast turns that into 0, 1, 2, 3, 4 or 5, and the `+ 1` makes it a die. Change
the 6 and the 1 and you can produce any range you like, which is how Course 5
places a new tile and how the challenge at the end of Chapter 2 picks its secret.

## 🟢 Core — Comparing, combining, and a shortcut

Comparisons produce a `boolean`:

```java
int a = 10;
int b = 3;
System.out.println(a > b);
System.out.println(a == b);
System.out.println(a != b);
```

```
true
false
true
```

Two equals signs ask whether something is equal. One equals sign assigns a value.
Writing `=` where you meant `==` is a classic, and in Java it usually fails to
compile rather than quietly doing the wrong thing.

You can combine booleans. `&&` is "both", `||` is "either", `!` flips one over:

```java
System.out.println(a > b && b > 0);
System.out.println(a > b || b > 100);
System.out.println(!(a > b));
```

```
true
true
false
```

There is also a shorthand for assigning. `a += 5` means `a = a + 5`, and the same
works for `-=`, `*=` and `/=`. `a++` adds one, `a--` subtracts one.

Finally, a compact way to choose between two values:

```java
String verdict = a > b ? "bigger" : "smaller";
```

The question mark reads as "if", the colon as "otherwise". It is worth knowing
because you will read it in other people's code, but an `if` is clearer when
anything non-trivial is going on.

## 🟢 Core — Comparing text, which is not what you expect

Here is the Java trap that costs beginners the most time. To compare two pieces of
text, use `.equals`:

```java
String typed = "yes";
String expected = "yes";
System.out.println(typed.equals(expected));
```

```
true
```

Do **not** use `==` for text. `==` asks whether two things are the same object in
memory, not whether they say the same thing. For text written directly into your
source code, Java reuses one object behind the scenes, so `==` often says `true`
and the mistake stays hidden. Then one day the text comes from somewhere else, and
the same comparison says `false` while both sides clearly read `yes`.

Every game in this repository compares text that someone typed, so this rule will
be load-bearing from Course 2 onwards: **for text, always `.equals`.**

The same applies to `.equals` the other way round, with one addition that saves
trouble later. If the text might be missing entirely, put the known value first:

```java
if ("yes".equals(typed)) {
```

That way the comparison still works even when `typed` holds nothing at all.

## 🟢 Core — Making a decision

```java
int score = 72;
if (score >= 90) {
    System.out.println("Excellent.");
} else if (score >= 50) {
    System.out.println("Passed.");
} else {
    System.out.println("Not this time.");
}
```

```
Passed.
```

The condition in the brackets has to be a `boolean`. Java checks the branches from
the top and runs the first one that fits, then skips the rest. Change `score` and
run it again until you have seen all three.

The braces are not optional in this course, even when a branch is one line long.
Leaving them out is legal and is how a one-line fix quietly becomes a two-line bug.

## 🟢 Core — Choosing between many fixed values

When one value decides between several known cases, a chain of `else if` gets
tiring. A `switch` says the same thing more plainly:

```java
String command = "w";
switch (command) {
    case "w" -> System.out.println("up");
    case "s" -> System.out.println("down");
    case "a" -> System.out.println("left");
    case "d" -> System.out.println("right");
    default -> System.out.println("unknown command");
}
```

```
up
```

`default` catches everything that matched no case. The arrow form is the modern
one, and this course uses nothing else. An older form with colons exists and needs
a `break` after every case, which is a famous source of bugs. You will meet it in
other people's code; you do not need to write it.

Courses 2 and 5 both read a letter and decide what to do with it, so this is the
shape their input handling has.

## 🟢 Core — Repeating a fixed number of times

```java
for (int i = 1; i <= 5; i++) {
    System.out.print(i + " ");
}
System.out.println();
```

```
1 2 3 4 5 
```

The brackets hold three things, separated by semicolons: where to start, how long
to keep going, and what to do after each pass. Read it as "start `i` at 1, keep
going while `i` is at most 5, and add one each time".

Counting from 0 is the other common shape, and from Chapter 2 on it is the usual
one, because that is where lists start:

```java
for (int i = 0; i < 5; i++) {
```

Five passes again, with `i` taking 0, 1, 2, 3, 4. Note `<` rather than `<=`.

## 🟢 Core — Repeating until something changes

When you do not know in advance how many passes you need, `while` keeps going as
long as its condition holds:

```java
int countdown = 3;
while (countdown > 0) {
    System.out.println(countdown);
    countdown--;
}
System.out.println("Go!");
```

```
3
2
1
Go!
```

Something inside the loop has to move the condition towards false. Forget the
`countdown--` and the program prints 3 forever. If that happens, press Ctrl+C in
the terminal to stop it. It will happen to you at least once, and it is not a
disaster.

`while` is the shape every game loop in this repository has: keep playing while
nobody has won. `break` leaves a loop immediately, and `continue` skips the rest of
the current pass and starts the next one.

## 🟡 Optional — Notes to yourself, values that cannot move, neater output

Anything after `//` is ignored by the compiler and is there for whoever reads the
code next, which is usually you in three weeks:

```java
// Two slashes end the line.
/* This form spans
   several lines. */
```

Good comments say why, not what. `i++ // add one to i` helps nobody.

If a value should never change, say so with `final`:

```java
final int BOARD_SIZE = 3;
```

Assigning to it afterwards stops the program from compiling. The capital letters
are a convention for values fixed once and used everywhere, and every game here
declares its board size this way.

Back to `printf`, which can do more than fill in blanks. A number between the `%`
and the letter is a minimum width:

```java
System.out.printf("%5d|%n", 42);
System.out.printf("%5d|%n", 1234);
```

```
   42|
 1234|
```

Both lines are the same width, so the right edges line up. That one detail is what
makes a 2048 board readable in Course 5.

## 🔴 Optional, genuine challenge — FizzBuzz

Print the numbers from 1 to 15, one per line, with two substitutions: for a number
divisible by 3 print `Fizz`, for one divisible by 5 print `Buzz`, and for one
divisible by both print `FizzBuzz`.

```
1
2
Fizz
4
Buzz
...
```

Everything you need is in this chapter: a `for` loop, `%`, and `if`/`else if`.

One warning, because it is the whole point of the exercise. The obvious order of
tests gives the wrong answer for 15. Work out why before you start writing, and the
problem solves itself; write first and you will be puzzled for a while.

If you get stuck, [`code/FizzBuzz.java`](../code/FizzBuzz.java) has one way to
write it.

## What you learned

- Printing with `println`, `print` and `printf`, and joining values with `+`
- The five types: `int`, `double`, `boolean`, `char`, `String`
- That the type is fixed at declaration, and that the compiler enforces it
- Whole-number division and `%`, and why `7 / 2` is 3
- Casting with `(int)`, and random numbers with `Math.random()`
- Comparison and logical operators, `+=`, `++`, and the `? :` shorthand
- **`.equals` for text, never `==`**
- `if` / `else if` / `else`, and `switch` with arrows
- `for` when you know the count, `while` when you do not
- *(optional)* Comments, `final`, and widths in `printf`

## Next

Chapter 2 adds the last three things every game here needs: your own methods, lists
of values, and reading what someone types.

**Next:** [Chapter 2 – Methods, Arrays and Input](02-methods-arrays-input.md)
