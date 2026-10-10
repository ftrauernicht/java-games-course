🇬🇧 English | 🇩🇪 [Deutsch](../de/01-flappy-bird.md)

[← Back to course overview](../../../README.md) · Assumes: [Course 1 – Basics](../../01-basics/en/00-introduction.md)

# Course 7 – Flappy Bird

**Goal:** one button, gravity, and a gap to aim at. The last course in this
repository, and the first where movement is not a step on a grid but a speed that
changes every frame.

![The bird mid-flight between two pipes, score 7](../assets/gameplay.png)

Assumes [Course 1](../../01-basics/en/00-introduction.md) and nothing else. If you
have done Course 6, the next two sections will be familiar and you can skip them.
A finished version lives in [`code/`](../code/), in three files.

## 🟢 Core — Classes and objects, in short

*(If you have done Course 6, skip ahead to the next section.)*

Course 1 called a class "a box with a name". The full answer is that a class
describes what something **is** and what it **can do**, in one place: the values it
holds, called **fields**, and the methods that work on them. `new` makes an actual
one, an **object**, and each object has its own copy of the fields.

This game needs that twice over, for two different reasons.

The panel needs it because the bird's height, its speed and the score have to
survive between frames, and a `static` method from Course 1 has nowhere to keep
anything.

The pipes need it because there are three of them and they are all the same kind of
thing with different numbers in it. Without a class you would be juggling
`pipeX1`, `pipeX2`, `pipeX3`, `pipeGapY1` and so on, and every piece of code that
touched a pipe would have to say which one three times over.

Three more words appear below. `private` on a field means only this class may touch
it. `final` on a class means nobody may build a variant on top of it, which here
keeps the compiler quiet about a warning explained in the next section. And
`extends` means "start from that class and change one thing".

## 🟢 Core — The three Swing facts this game needs

*(If you have done Course 6, skip ahead to the next section.)*

Swing is Java's toolkit for windows. This chapter uses four of its classes and
ignores the rest.

**Something to draw on.** A `JFrame` is the window, and you cannot draw on it. You
put a `JPanel` inside, and you make your own by writing `extends JPanel` and
overriding one method:

```java
public final class FlappyPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GREEN);
        g.fillRect(60, 0, 60, 180);
    }
}
```

`super.paintComponent(g)` lets the original clear the background first. `Graphics g`
draws: `fillRect` for the pipes, `fillOval` for the bird, `drawString` for the
score. Coordinates are pixels from the top left, and **y grows downwards**, which
matters more here than in any other course, because that is the direction gravity
pulls.

The class is `final` on purpose. A panel that can be extended from outside makes
the compiler warn about letting `this` escape before the object is finished, and
this repository treats warnings as errors.

**Something to redraw it.** You never call `paintComponent`. You call `repaint()`,
and Swing calls `paintComponent` when it gets round to it. So the rule is: change
the numbers, then ask for a redraw.

**Something to drive it.** A game that falls needs a steady heartbeat. That is a
`javax.swing.Timer`, which calls you back at a fixed interval:

```java
new Timer(16, event -> {
    panel.tick();
    panel.repaint();
}).start();
```

Sixteen milliseconds is about sixty frames a second, which is what makes falling
look like falling rather than like jumping. A `while` loop with `Thread.sleep`
would not do: Swing repaints on the same thread, so a sleeping game is a frozen
window.

Import `javax.swing.Timer` by name. `java.util.Timer` also exists, and importing
both with a wildcard makes the name ambiguous.

## 🟢 Core — Movement as a speed, not a step

Every game so far moved things by whole squares. Here the bird has a **velocity**,
and two lines run the entire physics:

```java
velocity = velocity + GRAVITY;
birdY = birdY + velocity;
```

Gravity does not move the bird. It changes how fast the bird is moving, and the
speed moves the bird. Falling therefore starts slowly and gets quicker on its own,
which is exactly what falling does.

A flap is one assignment:

```java
velocity = FLAP;      // a negative number: upwards
```

Not "move up", but "you are now travelling upwards at this speed". Gravity
immediately starts eating into it, so the bird rises, slows, stops and falls again
without a single line of code describing that arc. Two constants produce it:

```java
static final double GRAVITY = 0.5;
static final double FLAP = -8.0;
```

Both `birdY` and `velocity` are `double`, and that is not fussiness. With
`int birdY`, the line `birdY += velocity` quietly throws away the fraction on every
frame, the motion goes lumpy, and the compiler warns about a lossy conversion,
which this repository treats as an error. Keep both as `double` and cast once, at
the very end, where the drawing needs whole pixels:

```java
g.fillOval(BIRD_X, (int) birdY, BIRD_SIZE, BIRD_SIZE);
```

The bird never moves sideways. The pipes come to it, which is less work and looks
identical.

## 🟢 Core — A class that is only data

Three pipes exist for the whole game. When one leaves on the left it is moved back
to the right with a new gap, rather than thrown away and replaced, so the program
creates three objects and no more.

```java
public final class Pipe {

    int x;
    int gapY;
    boolean passed;

    Pipe(int x, int gapY) {
        this.x = x;
        this.gapY = gapY;
    }

    void recycle(int x) {
        this.x = x;
        this.gapY = FlappyPanel.MARGIN + (int) (Math.random()
                * (FlappyPanel.HEIGHT - FlappyPanel.GAP - 2 * FlappyPanel.MARGIN));
        this.passed = false;
    }
}
```

The constants live in `FlappyPanel`, so `Pipe` has to say which class they belong
to. Inside `FlappyPanel` itself you write `MARGIN`; from outside it is
`FlappyPanel.MARGIN`. That is the same rule that makes you write `Math.random()`
rather than `random()`.

`x` is the left edge, `gapY` the top of the hole, and `passed` remembers whether
this pipe has already been counted, so one pipe cannot score twice.

`this.x = x` looks odd the first time. The parameter and the field have the same
name, so `this.` says "the field, not the parameter". You could name them
differently and avoid it; the convention exists because the two really are the same
thing under two hats.

This goes in its own file, `Pipe.java`. A second class sharing `FlappyPanel.java`
would be hidden there, and the compiler warns about a class used from elsewhere
that is not in a file of its own name.

Hold them in an array:

```java
private final Pipe[] pipes = new Pipe[3];
```

`new Pipe[3]` makes room for three pipes and no pipes. Each slot still has to be
filled with `new Pipe(...)`, which is the one difference from the `int[]` arrays of
Course 1, where the slots arrived already holding zeros.

## 🟢 Core exercise — One frame

```java
void tick() {
    // your code here -- the five steps below
}
```

1. If the game is over, or has not started yet, return and do nothing.
2. **Gravity.** Add `GRAVITY` to the velocity, then add the velocity to `birdY`.
   In that order: the new speed applies to this frame.
3. **Pipes.** For each pipe, subtract `PIPE_SPEED` from its `x`.
4. **Recycle.** If a pipe's right edge has gone past 0, move it back to the right
   and give it a new gap. Put it `pipes.length * PIPE_SPACING` further right than
   where it is now, so it lands behind the other two rather than on top of one.
5. **Score.** If a pipe has not been counted and its right edge is now left of the
   bird, mark it counted and add a point.

Steps 4 and 5 both ask "where is the right-hand edge", which is `pipe.x + PIPE_WIDTH`
and not `pipe.x`. Getting that wrong gives you a point a pipe-width too early and a
recycle a pipe-width too late, and neither is obvious from watching.

Step 5 is why `passed` exists. Without it, every frame in which the bird is past the
pipe scores another point, and the score runs away at sixty a second.

## 🟢 Core exercise — Did it hit anything

```java
private boolean collides() {
    // your code here -- the two parts below
}
```

**Out of the screen.** If `birdY` is below 0, or `birdY + BIRD_SIZE` is beyond
`HEIGHT`, the bird is out.

**Into a pipe.** Each pipe is two rectangles with a hole between them: an upper one
from the top down to `gapY`, and a lower one from `gapY + GAP` to the bottom. The
bird is a third rectangle. The question is whether the bird's rectangle overlaps
either of those.

Java will answer that for you:

```java
Rectangle bird = new Rectangle(BIRD_X, (int) birdY, BIRD_SIZE, BIRD_SIZE);
Rectangle upper = new Rectangle(pipe.x, 0, PIPE_WIDTH, pipe.gapY);

if (bird.intersects(upper)) {
```

`Rectangle` is four numbers: left, top, width, height. `intersects` answers whether
two of them overlap anywhere.

It is worth knowing what it is doing rather than treating it as magic. Two
rectangles overlap when they overlap horizontally **and** vertically, and they
overlap horizontally when each one's left edge is left of the other's right edge.
That is four comparisons joined with `&&`, and you could write it yourself. Using
the library version here means one less thing to get subtly wrong while you are
also learning about gravity.

Build the lower rectangle yourself. Its top is `gapY + GAP` and its height is
whatever is left of the screen below that.

## 🟢 Core — Starting, dying, and starting again

The bird should not fall before the player is ready, and a game that ends should be
playable again without restarting the program. Two `boolean` fields handle both:

```java
private boolean started;
private boolean gameOver;
```

`tick` does nothing unless `started` is true and `gameOver` is false. The space bar
does three different things depending on which state you are in:

```java
void flap() {
    if (gameOver) {
        reset();
        return;
    }
    started = true;
    velocity = FLAP;
}
```

First press starts the game and flaps. Later presses flap. A press after dying
starts over. `reset` puts the bird back in the middle, zeroes the velocity and the
score, and recycles all three pipes to their starting places — which is also what
the constructor needs, so write it once and call it from both.

The wiring in `FlappyBird.java` is the same shape as any Swing program: build the
panel, give it a preferred size, attach a `KeyAdapter`, put it in a frame, `pack`,
show it, ask for focus **after** showing it, then start the timer. If you have done
Course 6, it is the same file with different names.

```
javac *.java
java FlappyBird
```

Three files this time, so `javac *.java` again, and again not `java FlappyBird.java`.

![Game over, the bird stopped against a pipe, score 12](../assets/game-over.png)

## 🟡 Optional — Pipes that come and go

Three recycled pipes is the version that needs nothing beyond Course 1. The grown-up
version creates a pipe when one is due and removes it when it leaves, and for that
you want a list rather than an array, because the number changes while the program
runs:

```java
import java.util.ArrayList;
import java.util.List;

private final List<Pipe> pipes = new ArrayList<>();
```

`add` puts one on the end, `remove` takes one out, `size()` is how many there are,
and the enhanced `for` from Course 1 walks it unchanged. The angle brackets say what
it holds, so the compiler knows a `Pipe` comes back out.

One trap, and it is the reason this is optional rather than the main path: removing
from a list while walking it with a `for` loop throws
`ConcurrentModificationException`. Collect what should go and remove it afterwards,
or walk the list backwards by index.

## 🟡 Optional — Make it fair

Three numbers decide whether this game is pleasant or infuriating: `GRAVITY`,
`FLAP`, and `GAP`.

Change one at a time and play five rounds after each change. Then write down, in a
comment, why your numbers are the ones you kept. The useful question is not "is it
hard" but "when I die, do I believe it was my fault".

Worth working out on paper: with `GRAVITY` at 0.5 and `FLAP` at -8, how many frames
pass before a flap has been entirely cancelled out? The answer tells you the
rhythm the game is asking the player to tap at, and whether that rhythm is
comfortable at sixty frames a second.

## 🔴 Optional, genuine challenge — A score that outlives the program

Keep the best score in a file next to the program, read it when the game starts,
and write it when a round beats it.

```java
import java.nio.file.Files;
import java.nio.file.Path;
```

`Files.writeString(Path.of("highscore.txt"), String.valueOf(score))` writes, and
`Files.readString(Path.of("highscore.txt"))` reads it back. Both can fail, and
unlike `Thread.sleep` in Course 4 this one you have to handle rather than pass on,
because a missing file is normal on a first run and should not stop the game:

```java
static int readBest() {
    try {
        String text = Files.readString(Path.of("highscore.txt"));
        return Integer.parseInt(text.trim());
    } catch (IOException e) {
        return 0;
    }
}
```

`try` runs the risky part; `catch` says what to do when it goes wrong. Here "no
file yet" and "the file is unreadable" both honestly mean "no best score yet", so
both return 0.

Three things to test, and the second is the one everybody forgets. Delete the file
and start the game. Put the word `banana` in it and start the game. And beat your
own record twice in one session, so the write happens more than once.

## 🔴 Optional, genuine challenge — Ground that moves

The sky is empty, so there is nothing to show how fast the bird is travelling.
Draw a strip of ground along the bottom and scroll it leftwards at `PIPE_SPEED`.

The trick is making it endless without an endless number of stripes. Draw them at
regular spacing, keep one offset that counts down from the spacing to 0 and then
jumps back up, and draw from `-spacing` so a stripe arriving from the right is
already on screen when it is needed.

Two sentences of warning. The ground is decoration, so do not let the bird collide
with it; it already dies at `HEIGHT`. And if you make the ground tall, move that
death line up to the top of the ground, or the bird will visibly sink into it
before the game notices.

## What you learned

- Movement as a velocity that gravity changes, rather than a fixed step
- Why `birdY` and `velocity` are `double`, and what the compiler says if they are not
- Casting to `int` once, at the point of drawing
- A class whose job is to hold data, and `this.x = x` when the names collide
- An array of objects, where `new Pipe[3]` makes room but no pipes
- Recycling objects rather than creating and discarding them
- `Rectangle` and `intersects`, and the four comparisons they stand for
- A flag like `passed` to make something happen once instead of every frame
- Two booleans for "not started" and "over", and one key that means three things
- A `reset` method called from both the constructor and the restart
- *(optional)* `ArrayList`, and why removing while looping throws
- *(optional)* `try` and `catch`, and a missing file as a normal case

## Next

That is the last course in this repository. Seven games, from a board you draw with
dashes to a bird falling at sixty frames a second, and nothing installed beyond the
JDK.

What comes next is in [PROJECT-IDEAS.md](../../../PROJECT-IDEAS.md): Pong,
Minesweeper, Breakout and Tetris are all within reach of what you now know, and the
last entry is the one that leaves the plain JDK behind for a build tool and real
tests. None of them are written yet. The list is there because the next thing to
build is more useful than another chapter to read.
