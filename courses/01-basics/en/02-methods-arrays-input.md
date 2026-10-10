🇬🇧 English | 🇩🇪 [Deutsch](../de/02-methoden-arrays-eingaben.md)

[← Back to course overview](../../../README.md) · Previous: [Chapter 1 – Java Basics](01-java-basics.md)

# Chapter 2 – Methods, Arrays and Input

**Goal:** three things stand between Chapter 1 and a playable game. You need to
split a program into named pieces, you need to hold many values instead of one,
and you need to read what somebody types. By the end of this chapter you will have
all three, and you will have written a game with them.

Work in a file called `MethodsArraysInput.java`, with the same class frame as
before and the class renamed to match. A finished version of the green path lives
in [`code/MethodsArraysInput.java`](../code/MethodsArraysInput.java); the yellow
section is practice, so it is not repeated there.

## 🟢 Core — Your own methods

You have been writing everything inside `main`. That works until it does not: a
few hundred lines in one method is unreadable, and anything you want to do twice
has to be written twice.

A method is a named piece of program. Here is one that greets somebody:

```java
static void greet(String who) {
    System.out.println("Hello, " + who + "!");
}
```

It goes **inside** the class but **outside** `main`, next to it. Your file now
looks like this:

```java
public class MethodsArraysInput {

    public static void main(String[] args) {
        greet("Frank");
        greet("Ada");
    }

    static void greet(String who) {
        System.out.println("Hello, " + who + "!");
    }
}
```

```
Hello, Frank!
Hello, Ada!
```

Reading the declaration from the left: `static` means it belongs to the class
rather than to any particular object, which is the same `static` that sits on
`main` and for the same reason. `void` means it hands nothing back. `greet` is the
name. `(String who)` is what it needs in order to do its job, and inside the
method you use `who` like any other variable.

Called twice, written once. That is the whole point.

## 🟢 Core — Methods that hand something back

More often you want an answer rather than an action. Then you replace `void` with
the type of the answer and use `return`:

```java
static int add(int first, int second) {
    return first + second;
}
```

```java
System.out.println(add(2, 3));
```

```
5
```

`return` does two things at once: it names the answer and it ends the method
immediately. That second half is useful on its own:

```java
static int larger(int first, int second) {
    if (first > second) {
        return first;
    }
    return second;
}
```

No `else` needed. If the first `return` runs, nothing after it does.

A method with a return type has to return something on every path through it.
Leave out the last line and the compiler says `missing return statement`, which is
it refusing to let you write a method that sometimes answers nothing.

**Try it yourself:** write `static boolean isEven(int number)` that answers whether
a number is even. One line of body is enough, and `%` from Chapter 1 does the work.

## 🟢 Core — Arrays, many values under one name

A variable holds one value. An array holds a fixed number of them, all the same
type, each at a numbered position.

Two ways to make one. Either say how many you want and fill them in later:

```java
int[] scores = new int[3];
scores[0] = 10;
scores[1] = 20;
scores[2] = 30;
```

`new` is the word for "make me one of these". It turns up wherever something has
to be built rather than just written down, and you will meet it again with
`Scanner` at the end of this chapter.

Or list the values straight away:

```java
int[] primes = {2, 3, 5, 7, 11};
```

The square brackets after the type mean "an array of these". Positions are
numbered **from 0**, so the five primes sit at 0, 1, 2, 3 and 4. The last position
is always one less than the count, and `primes.length` tells you the count:

```java
System.out.println(primes.length);
System.out.println(primes[0]);
System.out.println(primes[4]);
```

```
5
2
11
```

Ask for a position that does not exist and the program stops:

```java
System.out.println(primes[5]);
```

```
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: Index 5 out of bounds for length 5
```

Try it once on purpose. The message names the position you asked for and the
length it actually has, which between them usually point straight at the mistake:
a loop that ran one pass too far.

## 🟢 Core — Walking an array

The counting loop from Chapter 1 is built for this, and now the "start at 0" shape
earns its place:

```java
for (int i = 0; i < primes.length; i++) {
    System.out.print(primes[i] + " ");
}
System.out.println();
```

```
2 3 5 7 11 
```

`i < primes.length` and not `i <= primes.length`. The last valid position is
`length - 1`, so `<=` is exactly the off-by-one that produces the error you just
saw.

When you want every value and do not care about the positions, there is a shorter
form:

```java
int total = 0;
for (int prime : primes) {
    total += prime;
}
System.out.println("Sum: " + total);
```

```
Sum: 28
```

Read the colon as "in". It hands you each value in turn, and it cannot run off the
end, because it never deals in positions at all. Use it when you only need the
values; use the counting form when you need to know where you are, or when you
want to change what is stored.

This also settles the promise from Chapter 0. `String[] args` is an array like any
other, holding text instead of numbers, so `args.length` is how many words were
typed after the program name and `args[0]` is the first of them. `Greet.java`
checked `args.length == 0` for exactly the reason you now know: asking for
`args[0]` when nothing was typed is reaching past the end.

## 🟢 Core — Arrays are references, single values are not

This section is short and it is the one that makes every later course work.

Pass an `int` to a method and the method gets a copy:

```java
static void tryToChange(int value) {
    value = 99;
}
```

```java
int number = 1;
tryToChange(number);
System.out.println(number);
```

```
1
```

Nothing happened. The method changed its own copy and then the copy went away.

Now the same thing with an array:

```java
static void changeFirst(int[] values) {
    values[0] = 99;
}
```

```java
int[] box = {1};
changeFirst(box);
System.out.println(box[0]);
```

```
99
```

That one stuck. An array variable does not hold the values; it holds the location
where the values live. Hand it to a method and the method gets the same location,
not a copy of the contents. So it is looking at your array, and what it writes,
you see.

Remember this shape, because every game in this repository is built on it. Course
2 passes its board to a method that fills in a move. Course 4 passes a grid to a
method that counts neighbours. None of that would work if arrays behaved like
`int`.

## 🟢 Core — Reading what someone types

Everything so far has had its values written into the source. A game needs them
from a person.

The tool is `Scanner`, and it lives in a part of the library that is not available
by default, so you ask for it at the very top of the file, above `public class`:

```java
import java.util.Scanner;
```

Then, as the first thing in `main`:

```java
Scanner in = new Scanner(System.in);

System.out.print("What is your name? ");
String typedName = in.nextLine();
System.out.println("Hello, " + typedName + "!");
```

`System.in` is the keyboard. `nextLine()` waits until Enter is pressed and hands
back everything typed before it, as a `String`.

Two rules about `Scanner` in this course, both of which exist because the
alternative wastes an afternoon.

**Read with `nextLine()` and nothing else.** `Scanner` has `nextInt()`, and it
looks like the obvious way to read a number. It reads the digits and leaves the
line break sitting in the buffer, so the next `nextLine()` finds it, returns an
empty string, and does not wait. The symptom is a question that seems to skip
itself. Read lines, convert afterwards.

**Never close it.** An editor will suggest closing the `Scanner`, or wrapping it
so it closes itself. Closing a `Scanner` on `System.in` closes `System.in`, for
good, for the rest of the program. Nothing can reopen it. One `Scanner`, made in
`main`, never closed.

To get a number, convert the line:

```java
String line = in.nextLine().trim();
int age = Integer.parseInt(line);
```

`trim()` removes spaces at both ends, because people press space. `Integer.parseInt`
turns digits into an `int` and stops the program if the text is not a number, so
check first:

```java
static boolean isWholeNumber(String text) {
    if (text.isEmpty()) {
        return false;
    }
    for (int i = 0; i < text.length(); i++) {
        char c = text.charAt(i);
        if (c < '0' || c > '9') {
            return false;
        }
    }
    return true;
}
```

`text.isEmpty()` answers whether there are no characters at all, which has to be
rejected separately: an empty line is not a number, but the loop below would never
notice, because it would have nothing to look at and fall straight through to
`true`.

`text.length()` is how many characters, and `text.charAt(i)` is the one at that
position, which is a `char` and so can be compared with `<` and `>`. Any character
outside `'0'` to `'9'` and the answer is no. (Digits only, so a minus sign counts
as "not a number". Nothing in this course asks for a negative.)

Put the two together and you have the shape every game here uses for input:

```java
static int askForNumber(Scanner in, String question) {
    while (true) {
        System.out.print(question);
        String line = in.nextLine().trim();
        if (isWholeNumber(line)) {
            return Integer.parseInt(line);
        }
        System.out.println("That was not a whole number. Try again.");
    }
}
```

`while (true)` looks alarming and is fine: the `return` is the way out, and there
is no way past it except a valid number. Note that the `Scanner` is handed in as a
parameter rather than made again inside, because there is one keyboard and so there
is one `Scanner`.

```java
int age = askForNumber(in, "How old are you? ");
System.out.println("In ten years you will be " + (age + 10) + ".");
```

```
How old are you? twelve
That was not a whole number. Try again.
How old are you? 34
In ten years you will be 44.
```

## 🟡 Optional — More things text can do

`String` has a drawer full of useful methods. The ones worth knowing now:

```java
String text = "  Hello World  ";
System.out.println(text.trim());
System.out.println(text.trim().length());
System.out.println(text.toLowerCase().trim());
System.out.println(text.contains("World"));
System.out.println(text.trim().charAt(0));
```

```
Hello World
11
hello world
true
H
```

`toLowerCase()` is the practical way to accept both `Y` and `y` without writing
every case twice. Combined with `.equals` from Chapter 1:

```java
String answer = "  YES  ";
if ("yes".equals(answer.trim().toLowerCase())) {
    System.out.println("Accepted.");
}
```

```
Accepted.
```

Reading from the inside out: trim the spaces, lower the case, compare. That one
line handles `Yes`, `YES` and `  yes  ` alike.

None of these change the original. `text.trim()` hands back a new piece of text and
leaves `text` exactly as it was, which surprises people once. If you want to keep
the result, assign it to something.

## 🔴 Optional, genuine challenge — Guess the number

Write a game. The program picks a secret whole number from 1 to 100 and asks the
player to guess. After each guess it gives the player an instruction: `Higher.`
when the secret is above the guess, `Lower.` when it is below. When the guess is
right it says so and reports how many tries it took.

```
I am thinking of a number between 1 and 100.
Your guess: 50
Lower.
Your guess: 25
Higher.
Your guess: 37
That's it. You needed 3 tries.
```

The pieces are all in Chapters 1 and 2:

1. Make the secret with `Math.random()` and a cast, shifted so it starts at 1.
2. Count the tries in an `int` that goes up once per guess.
3. Loop with `while (true)`, reading a guess each time with `askForNumber`.
4. Compare, print the hint, and `break` out when the guess is right.

Write `askForNumber` and `isWholeNumber` into this file too, since this is a new
program with its own `main`.

Two things worth doing while you build it. Print the secret just after you make it
so you can test quickly, then delete that line when the game works. And check the
edges: can the secret ever be 0, or 101? Work that out from the formula rather than
by playing a hundred rounds.

If you get stuck, [`code/GuessTheNumber.java`](../code/GuessTheNumber.java) has one
way to write it.

## What you learned

- Writing your own methods, with parameters and with a return type
- That `return` also ends the method, which often replaces an `else`
- Arrays: `new int[3]`, the `{1, 2, 3}` form, `length`, and counting from 0
- Walking an array with a counting loop, and with the shorter `for (int x : xs)`
- **That an array passed to a method is the same array, not a copy**
- `ArrayIndexOutOfBoundsException` and what it tells you
- `import`, `Scanner`, and reading with `nextLine()`
- Checking a line before `Integer.parseInt` rather than after
- Why a `Scanner` on `System.in` is never closed
- *(optional)* `trim`, `length`, `charAt`, `toLowerCase`, `contains`

## Next

That is the whole language this course needs. Everything from here on is a game.

[Course 2 - Tic-Tac-Toe](../../02-tic-tac-toe/en/01-tic-tac-toe.md) builds a
board, two players, and a check for three in a row. It assumes this course and
nothing else, and so does every course after it, so you can take them in whatever
order appeals to you. The full list is in
[PROJECT-IDEAS.md](../../../PROJECT-IDEAS.md).

**Next:** [Course 2 - Tic-Tac-Toe](../../02-tic-tac-toe/en/01-tic-tac-toe.md)
