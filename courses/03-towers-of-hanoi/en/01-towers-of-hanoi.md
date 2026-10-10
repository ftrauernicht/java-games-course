🇬🇧 English | 🇩🇪 [Deutsch](../de/01-tuerme-von-hanoi.md)

[← Back to course overview](../../../README.md) · Assumes: [Course 1 – Basics](../../01-basics/en/00-introduction.md)

# Course 3 – Towers of Hanoi

**Goal:** the shortest program in this repository, and the one that is hardest to
believe. Three pegs, a stack of discs, and a solution whose body is three lines
plus a stopping condition, because it asks itself for help.

This is the first chapter that does not hand you the finished method. The idea
behind it is explained in full; writing it is yours. That is how every course from
here on works.

Assumes [Course 1](../../01-basics/en/00-introduction.md) and nothing else. A
finished version lives in [`code/TowersOfHanoi.java`](../code/TowersOfHanoi.java).

## 🟢 Core — The puzzle

Three pegs, called A, B and C. On peg A sits a stack of discs, largest at the
bottom, each one smaller than the one below it:

```
  #    |    |
  ##   |    |
  ###  |    |
  A    B    C
```

Move the whole stack to peg C. Two rules: one disc at a time, and a larger disc may
never rest on a smaller one.

Try it on paper with three discs before you read on. It takes seven moves, and
working them out by hand is worth more than reading about it.

With four discs it takes fifteen moves, with ten it takes 1023, and the general
answer is two to the power of the number of discs, minus one. A program that
worked out each move by planning ahead would be a serious piece of work.

The working part of the program you are about to write is three lines. It needs a
fourth line to know when to stop, and that line turns out to be the one everything
hangs on.

## 🟢 Core — The idea: let a smaller version do the work

Stop thinking about discs and think about the biggest one.

To get disc 3 from A to C, nothing may be on top of it, and nothing may be on C. So
the two discs above it have to be somewhere, and the only place left is B. If they
were already on B, the whole thing is easy: move disc 3 across, then bring those
two back on top of it.

So moving three discs from A to C is:

1. Move the top two discs from A to B, somehow.
2. Move disc 3 from A to C.
3. Move those two discs from B to C, somehow.

Steps 1 and 3 are the same puzzle with two discs instead of three. And moving two
discs is the same puzzle with one. And moving one disc is just moving it.

That is the whole solution. You never work out the moves; you keep handing a
smaller version of the problem to something that already knows how to solve it, and
the something is the method you are writing.

The word for this is **recursion**: a method that calls itself.

## 🟢 Core — A method that calls itself

Here is the shape, with the body left out:

```java
static void move(int n, char from, char to, char via) {
    ...
}
```

Four things it needs to know: how many discs to move, where they are, where they
should end up, and which peg is free to use as a parking space.

Two things make recursion work, and leaving either one out breaks it completely.

**It has to get smaller.** Every call the method makes to itself must be about
fewer discs than the call it is in. Otherwise the same problem goes round forever.

**It has to stop.** There must be a case the method answers directly, without
calling itself. Here it is `n == 0`: moving no discs means doing nothing. That case
is called the **base case**, and without it the program runs until Java gives up
with a `StackOverflowError`, which is the computer saying it has run out of room
for pending calls.

The pending calls are worth a sentence. When `move` calls itself, the outer call
does not go away; it waits, halfway through, for the inner one to finish. With
three discs, three calls that have work to do are stacked up at the deepest point,
each remembering its own `n`, `from`, `to` and `via`. (Count the `n == 0` calls as
well and it is four, but those return without doing anything.) That stack is why
the method can "come back" and do its second half after the first recursive call
returns.

If that still feels like a trick, the clearest way to see it is to print the calls
themselves. Here is what happens for three discs, indented by depth, with each
call showing the four values it was handed:

```
move(n=3, from=A, to=C, via=B)
  move(n=2, from=A, to=B, via=C)
    move(n=1, from=A, to=C, via=B)
      -> Move disc 1 from A to C
    -> Move disc 2 from A to B
    move(n=1, from=C, to=B, via=A)
      -> Move disc 1 from C to B
  -> Move disc 3 from A to C
  move(n=2, from=B, to=C, via=A)
    move(n=1, from=B, to=A, via=C)
      -> Move disc 1 from B to A
    -> Move disc 2 from B to C
    move(n=1, from=A, to=C, via=B)
      -> Move disc 1 from A to C
```

Follow the second line. The outer call wanted to reach C, but this inner one was
handed `to=B`, and so it prints B. Nothing is shared between the calls: each has
its own four values, handed to it by whoever called it. That is the answer to the
question the explanation above leaves hanging, which is not "why does it get
smaller" but "how does the inner call know where to go".

Build this trace yourself once you have the method working. Pass an extra `depth`
parameter, indent by it, and print the call before the recursion.

## 🟢 Core exercise — Write move

Three lines, in this order:

1. Move `n - 1` discs from `from` to `via`. Think about which peg is spare during
   that move: you are heading for `via`, so the one standing free is `to`.
2. Print the move of disc `n` from `from` to `to`, as one line of text.
3. Move `n - 1` discs from `via` to `to`. Now the spare peg is `from`, because it
   has just been emptied.

And above all three, the base case: if `n` is 0, return immediately.

Course 1 only ever used `return` with a value after it, because every method there
handed something back. `move` returns nothing, so here it is `return;` on its own,
with no value and a semicolon straight after. It means "stop now" and nothing
else. Writing `return 0;` in a `void` method gives
`incompatible types: unexpected return value`.

```java
static void move(int n, char from, char to, char via) {
    // your code here -- the base case, then the three steps above
}
```

Call it from `main` with three discs:

```java
public static void main(String[] args) {
    int discs = 3;
    System.out.println("Towers of Hanoi with " + discs + " discs.");
    System.out.println();
    move(discs, 'A', 'C', 'B');
}
```

Expected output:

```
Towers of Hanoi with 3 discs.

Move disc 1 from A to C
Move disc 2 from A to B
Move disc 1 from C to B
Move disc 3 from A to C
Move disc 1 from B to A
Move disc 2 from B to C
Move disc 1 from A to C
```

The hardest part is trusting step 1. You write "move two discs from A to B" without
knowing how that happens, because working out how is the method's own job, and at
the moment you write the line, the method is not finished yet. Write it anyway.
That leap is the thing this chapter is for.

If you get the order of the arguments wrong, the output is a sequence of illegal
moves that looks almost right. Check it against the seven lines above.

If you get stuck, [`code/TowersOfHanoi.java`](../code/TowersOfHanoi.java) has one
way to write it.

## 🟢 Core — Reading the trace

Take the output and follow it on paper with three coins of different sizes. Seven
moves, and the stack ends up on C in the right order.

Then look at where disc 3 appears: exactly once, in the middle, as the fourth of
seven moves. Three moves before it to clear the way, three after it to rebuild.
With four discs, disc 4 would appear exactly once, as the eighth of fifteen, with
seven either side. The pattern repeats all the way down, and that is where the
doubling comes from: each extra disc means doing all the previous work twice, plus
one move.

Change `discs` to 4 and check that you get fifteen lines. Then try 20, and notice
that a million moves print rather quickly and that this is the first program here
that could keep a computer busy.

## 🟡 Optional — Count the moves

Have the program report how many moves it made, and compare that against the
formula.

The cleanest way with what Course 1 gave you is to make `move` return a number
instead of returning nothing: the count is one, plus whatever the two recursive
calls report.

```java
static int move(int n, char from, char to, char via) {
    // your code here -- same three steps, but add up what the two recursive
    // calls hand back and return the total
}
```

The base case changes with it. A method that promises an `int` has to produce one
on every path, so `return;` becomes `return 0;` -- no discs, no moves. Leave it as
it was and the compiler says `missing return value`.

For the formula, `(1 << n) - 1` is two to the power of `n`, minus one. The `<<`
shifts a number's binary digits left, and each shift doubles it, so `1 << 10` is
1024. `Math.pow(2, n)` gives the same answer as a `double`, which then needs a cast
and is slightly wrong for large `n`, so the shift is the honest tool here.

Print both and check they agree for every `n` from 1 to 20. Comment out the
`println` inside `move` first, or the twenty comparison lines will be buried under
two million move lines.

One limit worth knowing: `1 << n` is an `int`, and an `int` runs out of room at
31 shifts. `1 << 31` is already negative and `1 << 64` is 0, so the trick that is
exact up to 30 is silently wrong beyond it. For this chapter the cap of 20 settles
it; for larger numbers you would reach for a type that holds more.

## 🟡 Optional — Ask how many discs

Read the number of discs with a `Scanner` instead of fixing it at 3, reusing the
input method from Course 1.

Put a limit on it. At 30 discs the program prints over a billion lines, and at 64
it would still be running long after everyone has lost interest, which is the point
of the legend the puzzle comes from. Refuse anything above 20 and say why.

## 🔴 Optional, genuine challenge — Actually move the discs

The program prints instructions. It never checks them, because it holds no pegs and
no discs: a bug in the recursion would print nonsense with complete confidence.

Build the state and carry the moves out. This one needs three things Course 1 did
not cover, so each gets a paragraph before the exercise starts.

**The pegs stop being letters.** So far `from`, `to` and `via` are `char`, which is
fine for printing and useless for looking something up: you cannot write
`TOWERS['A']`. Change all four parameters of `move` to `int` and call it with
`move(DISCS, 0, 2, 1)`, with 0, 1 and 2 standing for A, B and C. Turn a number back
into a letter only when printing, with `(char) (65 + peg)`, because 65 is the
numeric value of `'A'`.

Do not skip that change and hope. Java converts a `char` to an `int` silently, so a
method taking `int` accepts your `char` pegs without a word of complaint, and the
first lookup then explodes at run time with
`ArrayIndexOutOfBoundsException: Index 65 out of bounds for length 3`. The 65 is
the letter `A`, arriving where a peg number was expected.

**Two arrays, side by side, outside any method.** Course 1 kept every variable
inside `main`. These two have to be reachable from `move` and from the method that
carries a disc, so they go in the class, next to `main` rather than inside it, and
they are `static` for the same reason every method here is:

```java
static final int DISCS = 3;
static final int PEGS = 3;
static final int[][] TOWERS = new int[PEGS][DISCS];
static final int[] HEIGHTS = new int[PEGS];
```

**`int[][]` is an array of arrays**, which Course 1 did not need. `new int[PEGS][DISCS]`
makes three arrays of `DISCS` slots each, one per peg. `TOWERS[peg][0]` is the
bottom disc of that peg, `HEIGHTS[peg]` says how many discs are on it, so the top
disc sits at `TOWERS[peg][HEIGHTS[peg] - 1]`. That pair is a stack: push by writing
at `HEIGHTS[peg]` and adding one, pop by subtracting one and reading.

Note that `final` here means the array itself is never swapped for another one. The
numbers inside it change constantly, and that is allowed.

One warning before you start: this section and the yellow one above do not combine.
A `static final` field cannot be given a value somebody types, so if you made the
disc count a `Scanner` question, keep that version in a separate file.

Fill peg 0 with the discs, largest at the bottom, then replace the `println` in the
middle of `move` with a call to a method that does the work:

```java
static void carry(int from, int to) {
    // your code here:
    //   1. if the "from" peg is empty, print a complaint and return
    //   2. read the top disc off the "from" peg
    //   3. if the "to" peg is not empty and its top disc is smaller, print a
    //      complaint and return without moving anything
    //   4. otherwise take it off one peg, put it on the other, adjust both
    //      heights, and draw the pegs
}
```

Steps 1 and 3 are the real exercise. They are checks that should never fire: if the
recursion is right, no illegal move is ever attempted. Writing a test for something
you believe cannot happen feels pointless right up to the first time it fires.

Step 1 looks redundant next to step 3, and it is not. Once a move has been refused,
the sequence carries on regardless and will sooner or later try to lift a disc off
a peg that is empty, which without the check is `HEIGHTS[from] - 1` equal to `-1`
and a crash. The check turns a crash into a second complaint, which is what you
want from something whose job is to report.

To see them fire, break the recursion on purpose. Swap two arguments in one of the
recursive calls and run it again. You should get a handful of complaints and a
program that still finishes.

For drawing, `printf` with a width from Course 1 keeps the three pegs in columns.
A disc of size 3 can be three `#` characters, and an empty level a single `|`.
Build the row of peg letters from `PEGS` and the same width, rather than typing
`A    B    C`, or it stops lining up the moment `DISCS` changes.

```
  |    |    |
  ##   |    |
  ###  |    #
  A    B    C
```

If you get stuck,
[`code/TowersOfHanoiTowers.java`](../code/TowersOfHanoiTowers.java) has a working
version.

## What you learned

- Recursion: a method that calls itself with a smaller version of the problem
- The base case, and that `StackOverflowError` is what a missing one looks like
- That calls pile up and wait, which is how a method continues after its own
  recursive call returns
- Writing a call to something that is not finished yet, and trusting it
- Why the number of moves doubles with each disc
- *(optional)* Returning a count from a recursive method by adding up the parts
- *(optional)* `1 << n` as the integer way to say two to the power of n
- *(optional)* Pegs as numbers rather than letters, and the silent `char` to `int`
  conversion that hides the mistake until it crashes
- *(optional)* Fields outside a method, and `int[][]` as an array of arrays
- *(optional)* Two parallel arrays as a stack, and checks that should never fire

## Next

Course 4 is Conway's Game of Life, where a grid changes itself according to four
rules and nobody plays at all. It assumes Course 1 and nothing else.

The full list is in [PROJECT-IDEAS.md](../../../PROJECT-IDEAS.md).
