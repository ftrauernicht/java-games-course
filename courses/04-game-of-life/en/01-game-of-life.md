🇬🇧 English | 🇩🇪 [Deutsch](../de/01-spiel-des-lebens.md)

[← Back to course overview](../../../README.md) · Assumes: [Course 1 – Basics](../../01-basics/en/00-introduction.md)

# Course 4 – Conway's Game of Life

**Goal:** a game with no players. You place a few cells, four rules take over, and
a pattern develops on its own — in a window, not in the terminal. On the way you
will meet a mistake whose cause is invisible in the code and obvious on the screen,
and you will have avoided it.

![A randomly seeded grid after 60 generations: 163 live cells, run together into chains and clumps](../assets/soup.png)

This is the first window in this repository, and it costs seven lines and not one
class of your own. Course 6 needs considerably more for its window, and that is not
because of the drawing. It is because keys have to arrive there.

Assumes [Course 1](../../01-basics/en/00-introduction.md) and nothing else. A
finished version lives in [`code/GameOfLife.java`](../code/GameOfLife.java).

## 🟢 Core — Four rules

A grid of squares. Each square is either alive or dead. Every square has up to
eight neighbours, counting the diagonals.

One step produces a whole new generation, and every square decides at the same
time, using these four rules:

- A live square with fewer than two live neighbours dies.
- A live square with two or three live neighbours stays alive.
- A live square with more than three live neighbours dies.
- A dead square with exactly three live neighbours comes alive.

That is the entire game. It was invented in 1970 and people are still finding new
patterns in it.

The four rules collapse into two lines of code, which is worth noticing now so you
recognise it later: a live square survives on 2 or 3, a dead one is born on exactly
3.

## 🟢 Core — Two-dimensional arrays, in short

*(If you have done Course 2 or Course 5, skip ahead to the next section.)*

Course 1 gave you an array: a row of values under one name. A grid is an array
whose values are themselves arrays:

```java
boolean[][] grid = new boolean[ROWS][COLS];
```

Two pairs of brackets, two sizes. `grid[3][7]` is row 3, column 7, and both count
from 0. `boolean` is the type because a square is alive or dead and nothing else,
and a fresh `boolean` array is already full of `false`, so there is nothing to fill
in.

Walking the whole grid is a loop inside a loop:

```java
for (int row = 0; row < ROWS; row++) {
    for (int col = 0; col < COLS; col++) {
        // grid[row][col]
    }
}
```

Handing the grid to a method hands over the grid itself and not a copy, so a method
can change it. Course 1 covered that under "arrays are references". This chapter
mostly avoids using it: every method below reads a grid and returns a new one
rather than changing what it was given. The one place it matters is the mistake
the chapter warns about, further down.

Set up your file. The three sizes go in the class, next to `main` rather than
inside it, because `draw` and `countNeighbours` need them too. They are `static`
for the same reason every method here is `static`: they belong to the class, and
nothing has been created yet. Write `final int ROWS = 30;` inside `main` instead
and you get `non-static variable ROWS cannot be referenced from a static context`,
eleven times over.

```java
public class GameOfLife {

    static final int ROWS = 30;
    static final int COLS = 40;
    static final int CELL = 14;
    static final int GENERATIONS = 100;
    static final int PAUSE_MS = 80;

    public static void main(String[] args) {
        boolean[][] grid = new boolean[ROWS][COLS];
    }
}
```

`CELL` is the side of one cell in pixels and does not appear until the drawing.
`GENERATIONS` is 100 because the glider stays intact on this grid until generation
104 and then reaches the bottom wall — measured, not guessed.

## 🟢 Core — A window that shows a picture

*(Course 5 uses the same window. If you came from there, skip ahead.)*

A grid of `#` and `.` in the terminal would work, and for a handful of cells it is
enough. It just makes it hard to tell whether a pattern is travelling, flipping or
falling apart, and that is the entire question here. So: a window.

It costs less than you would think. It needs **no class of your own**, nothing is
overridden, nobody listens for anything. Three things are enough:

```java
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
```

A `BufferedImage` is a picture in memory that you are allowed to paint on. A
`JLabel` is a component you can hand a picture to. And a `JFrame` is the window
around it. These seven lines go at the top of `main`:

```java
BufferedImage picture =
        new BufferedImage(COLS * CELL, ROWS * CELL, BufferedImage.TYPE_INT_RGB);
JLabel label = new JLabel(new ImageIcon(picture));
JFrame window = new JFrame("Game of Life");
window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
window.add(label);
window.pack();
window.setVisible(true);
```

`CELL` is the side of a cell in pixels, 14 here. The grid is 30 by 40 cells, so the
picture is 560 by 420 pixels. `pack()` lays the window around the picture instead
of leaving you to guess a window size.

`setDefaultCloseOperation` says what the close button should do. Without that line
the window disappears and the program keeps running — you see nothing and have to
kill it from the terminal.

You paint with `Graphics`, and you get one from the picture:

```java
static void draw(BufferedImage picture, boolean[][] grid) {
    Graphics g = picture.createGraphics();

    for (int row = 0; row < ROWS; row++) {
        for (int col = 0; col < COLS; col++) {
            g.setColor(grid[row][col] ? ALIVE : DEAD);
            g.fillRect(col * CELL, row * CELL, CELL - 1, CELL - 1);
        }
    }
}
```

The same double loop as printing, and the `? :` from Course 1 picks again, only now
a colour rather than a character:

```java
static final Color DEAD = new Color(24, 26, 30);
static final Color ALIVE = new Color(90, 200, 110);
```

Three numbers from 0 to 255 for red, green and blue. Cells become pixels by
multiplying with `CELL`. The `- 1` on width and height leaves a pixel of air, and
that is what makes the cells readable one by one rather than as a solid field.

One line is still missing, and it is the one people forget:

```java
label.repaint();
```

Painting into the picture changes nothing on screen. `repaint()` asks the window to
look at the picture again. So: paint first, then `repaint`.

A hint about why Course 6 works much harder for its window: this one only **shows**
things. It takes nothing in. The moment a window has to react to keys you need a
class of your own, overridden methods and listeners — and that is the price of
input, not of output.

Now something to look at. A **glider** is five cells that travel diagonally:

```java
grid[1][2] = true;
grid[2][3] = true;
grid[3][1] = true;
grid[3][2] = true;
grid[3][3] = true;
```

![The glider after 48 generations: five cells, travelled diagonally](../assets/glider.png)

Five green squares on a large dark field are not much. That is exactly their worth:
the glider is the pattern you can check your rules against, because with five cells
you can count every one of them. What the rules can really do is further down,
under "The soup".

## 🟢 Core exercise — Counting neighbours

Every rule needs the same number: how many of the eight squares around this one
are alive.

```java
static int countNeighbours(boolean[][] grid, int row, int col) {
    // your code here -- the four steps below
}
```

1. Start a counter at 0.
2. Loop over the nine squares around and including this one, with two loops
   running from -1 to 1. Call the offsets `dRow` and `dCol`; the square being
   looked at is `row + dRow`, `col + dCol`.
3. Skip two kinds of square: the one in the middle, where both offsets are 0,
   because a square is not its own neighbour; and any square outside the grid,
   where the row is below 0 or at least `ROWS`, or the column is below 0 or at
   least `COLS`. `continue` is the tool for both.
4. For the rest, add 1 to the counter if that square is alive, then return the
   counter.

Step 3 is the whole exercise. A cell in the middle has eight neighbours, a cell on
an edge has five, and a corner has three. Get the bounds wrong and the program
stops with `ArrayIndexOutOfBoundsException` the first time it looks at row 0,
which at least tells you immediately.

Test it before going on. On the glider above, `countNeighbours(grid, 2, 2)` should
be 5, and `countNeighbours(grid, 0, 0)` should be 0.

## 🟢 Core exercise — The next generation

```java
static boolean[][] nextGeneration(boolean[][] grid) {
    // your code here -- the three steps below
}
```

1. Make a **new** grid of the same size.
2. For every square, count its neighbours in the **old** grid and write the result
   into the **new** one: a live square survives with 2 or 3 neighbours, a dead one
   is born with exactly 3.
3. Return the new grid.

Step 1 is not an optimisation and it is not a style preference. Writing results
into the grid you are still reading from means that by the time you reach row 5,
rows 0 to 4 already hold next-generation values, so row 5 counts the wrong
neighbours. Every square after the first is then deciding against a mixture of two
different moments in time.

Build it the wrong way once and watch what happens. The glider does not drift or
smear; it falls apart. Five cells, then three, then four, and from generation 2 on
a frozen two-by-two block that never changes again. The r-pentomino further down,
which should stay busy for over a thousand generations, is finished after four.

So the symptom is loud, which is lucky. What is quiet is the cause: nothing about
the code looks wrong, every rule is implemented correctly, and the mistake is
entirely in when the writing happens relative to the reading. That is why this is
worth meeting on purpose rather than reading about.

Now the loop in `main`:

```java
for (int generation = 0; generation <= GENERATIONS; generation++) {
    draw(picture, grid);
    label.repaint();

    System.out.println("Generation " + generation + ": "
            + countAlive(grid) + " live cells");

    grid = nextGeneration(grid);
}
```

`grid = nextGeneration(grid)` points the variable at the new grid, and the old one
is simply forgotten.

The line going to the console is not a leftover from an older version. A window
only ever shows the present, and the moment something goes wrong you will want to
compare generation 3 against generation 7. The console keeps the trace, the window
shows the picture. `countAlive` is a double loop that counts `true`, and writing it
takes a minute.

That line is also the test for the glider: it reads five live cells in every
generation, every time. Alongside it, compare generation 0 and generation 4 in the
window — the same shape, one square down and one to the right.

## 🟢 Core — A pause between generations

A hundred generations arrive faster than you can watch them. Java can wait:

```java
Thread.sleep(PAUSE_MS);
```

Eighty milliseconds. Put it at the end of the loop, and the compiler refuses to
build your program:

```
GameOfLife.java:17: error: unreported exception InterruptedException; must be caught or declared to be thrown
            Thread.sleep(PAUSE_MS);
                        ^
1 error
```

The line number depends on where you put it. Note also that `Thread` needed no
`import`: a handful of classes, the ones in `java.lang`, are always available, and
`Thread`, `String`, `Math` and `System` are among them. Everything else needs the
import line you met with `Scanner`.

This is a new kind of complaint, and it is worth understanding rather than working
around. Some methods warn in their signature that something can go wrong while they
run. `Thread.sleep` says that the waiting can be cut short from outside. Java does
not let you ignore such a warning: you either deal with it, or you pass it on to
whoever called you.

Passing it on is one phrase on `main`:

```java
public static void main(String[] args) throws InterruptedException {
```

Read it as "if that ever happens, let the program stop and say so". For a pause
between two frames that is the right answer, and nobody is going to interrupt this
program anyway.

You will meet the other way of dealing with it, `try` and `catch`, in a later
course where it earns its place. Here it would be four lines of ceremony around a
thing that cannot happen.

## 🟡 Optional — A world without edges

The glider walks until it reaches a wall and then falls apart, because squares
outside the grid count as dead.

Make the grid wrap: the square to the right of the last column is the first column,
and the square above row 0 is the bottom row. The grid becomes the surface of a
doughnut, and the glider walks forever.

The change is in `countNeighbours`. Instead of skipping out-of-range squares, bring
them back in range:

```java
int r = (row + dRow + ROWS) % ROWS;
int c = (col + dCol + COLS) % COLS;
```

Work out why the `+ ROWS` is there. The remainder operator in Java keeps the sign
of the left-hand side, so `-1 % 10` is `-1` and not `9`. Adding `ROWS` first makes
the number positive before the remainder is taken, and it cannot push anything past
the top because `dRow` is never more than 1.

Run the glider for forty generations -- raise `GENERATIONS` -- and watch it come
back around.

One thing to be careful of afterwards: the check value from the core exercise,
`countNeighbours(grid, 0, 0)` being 0, is still 0 with wrapping, but no longer for
the reason it was. Corner cells now have eight neighbours like everybody else, and
the answer is 0 only because the glider happens to live nowhere near them. Reusing
an old check after changing what it tests is how a green result stops meaning
anything.

## 🟡 Optional — Other patterns

Replace the glider and see what else the four rules do.

A **blinker** is three cells in a row. It flips between horizontal and vertical
forever, which makes it the smallest thing that repeats.

A **block** is a two by two square. Every cell in it has exactly three neighbours,
so it sits there unchanged.

An **r-pentomino** is five cells in a small zigzag. Relative to some row `r` and
column `c` in the middle of the grid, it is `(r, c)`, `(r, c+1)`, `(r+1, c-1)`,
`(r+1, c)` and `(r+2, c)`.

It is famous for staying busy for a very long time from almost nothing, but how
long depends sharply on how much room it has, because the walls kill what reaches
them. Measured here, counting generations until the state of the whole grid
repeats: 80 generations on 20 by 40, 434 on 30 by 60, 652 on 60 by 120 and 1103 on
80 by 160. Beyond that more room changes nothing; 100 by 200 and 120 by 240 both
came out at the same 1103.

Take the numbers as an order of magnitude and not as a property of the pattern.
They depend on exactly where you place it, because every wall it reaches sooner
kills a different part of it.

So "over a thousand" needs roughly 80 by 160 cells. Set `CELL` to 6 for that and
the window is 960 by 480 pixels, which fits on any screen. A grid that size could
not have been shown in a terminal, and that is the concrete gain from the window:
not that it looks nicer, but that it allows sizes at which the interesting patterns
have room in the first place.

## 🟡 Optional — The soup

A glider shows that your rules are right. It does not show what they are worth.

Fill the grid at random instead, about three cells in ten:

```java
static boolean[][] randomGrid() {
    boolean[][] grid = new boolean[ROWS][COLS];
    for (int row = 0; row < ROWS; row++) {
        for (int col = 0; col < COLS; col++) {
            grid[row][col] = Math.random() < 0.3;
        }
    }
    return grid;
}
```

The picture at the top of this chapter is exactly that, after sixty generations.
Within a few dozen generations the noise turns into blocks that sit still, blinkers
that flip, and now and then a glider that breaks loose and sails away. Nobody
programmed any of it in; they are the same four rules.

The finished version has both starts in it and picks one from an argument:

```java
boolean soup = args.length > 0 && "soup".equals(args[0]);
boolean[][] grid = soup ? randomGrid() : glider();
```

```
java GameOfLife        the glider
java GameOfLife soup   the random grid
```

`args` is the parameter `main` has been carrying since Course 1 without ever being
used. Here it earns its place.

The order inside the condition is not free. `args.length > 0` has to come **first**,
because `&&` does not evaluate its right-hand side at all once the left is false.
Turn it around and a start without an argument ends in
`ArrayIndexOutOfBoundsException: Index 0 out of bounds for length 0`. Writing
`"soup".equals(args[0])` rather than `args[0].equals("soup")` makes no difference to
that — measured, both throw. That form guards against `null`, and a command-line
argument is never `null`.

Do not leave the 0.3 alone. At 0.05 almost nothing happens, at 0.6 almost everything
dies of overcrowding at once. The interesting range is somewhere between, and
finding it yourself is the real exercise.

## 🔴 Optional, genuine challenge — Notice when nothing changes

Many starting patterns settle down. A grid that has stopped changing will keep
printing the same picture until the loop runs out, which is a waste of everyone's
time.

Stop early when a generation is identical to the one before it.

```java
static boolean same(boolean[][] a, boolean[][] b) {
    // your code here -- answer whether every square matches
}
```

`same` is the easy half. The work is the loop in `main`, which currently has
nowhere to put the comparison: `grid = nextGeneration(grid)` overwrites the old
grid in the same breath as making the new one, so there is nothing left to compare
against. Take it apart -- hold the new grid in a local variable, compare, `break`
if nothing changed, and only then assign.

Comparing with `==` will not do what you want: Course 1 explained that for text,
and the same reason applies here. `a == b` asks whether they are the same grid, not
whether they hold the same thing. You have to walk both and compare square by
square.

Then the honest part. That catches a grid frozen solid, and it does not catch a
blinker, which alternates between two pictures and never settles. Try it on a
blinker and watch it run to the end.

To catch that too, keep the generation before last as well and compare against
both.

That brings its own trap. In generation 0 there is no generation before last, so
the variable holding it is still `null`, and handing `null` to `same` gives you
`NullPointerException: Cannot load from object array`. Course 1 met `null` for
text, where the fix was to put the known value first; that trick does not exist
for arrays. Here you check before you ask: `if (twoAgo != null && same(next, twoAgo))`.
The `&&` stops as soon as the left half is false, so `same` is never reached with
nothing in hand.

Then ask yourself what a pattern with a period of three would do to your program,
and decide whether you care. Knowing exactly which cases your check misses is worth
more than a check that claims to catch everything.

## What you learned

- A grid as `boolean[][]`, and walking it with a loop inside a loop
- Counting neighbours with two offset loops from -1 to 1
- Skipping the centre square and anything outside the grid, with `continue`
- **Why the next generation has to be built in a new grid**
- That a wrong simulation can look entirely plausible, and that a known pattern is
  how you catch it
- `Thread.sleep`, and what a checked exception is
- `throws` on `main` as the honest small answer to one
- A window that shows something: `BufferedImage` to paint on, `JLabel` to display
  it, `JFrame` as the frame, `pack` instead of guessed sizes
- `createGraphics`, `setColor` and `fillRect`, and colours as three numbers
- That painting alone does nothing, and that `repaint()` is what puts it on screen
- That a window which only shows needs no class of your own, while one that takes
  key presses does
- That a window knows only the present, and why the trace still belongs in the
  console
- *(optional)* `(x + SIZE) % SIZE` to wrap an index, and why the `+ SIZE` is needed
- *(optional)* `args` finally used, and why `args.length > 0` has to come before the
  access
- *(optional)* Comparing two grids square by square, and knowing what the check
  misses
- *(optional)* `null` in an array variable, and guarding with `&&` before asking

## Next

Course 5 is 2048, where a grid does not evolve on its own but slides and merges
under four keys, and where the merge rule catches everybody out. It assumes Course
1 and nothing else.

The full list is in [PROJECT-IDEAS.md](../../../PROJECT-IDEAS.md).
