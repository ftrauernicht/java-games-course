🇬🇧 English | 🇩🇪 [Deutsch](../de/01-snake.md)

[← Back to the course list](../../../README.md) · Requires: [Course 1 – Basics](../../01-basics/en/00-introduction.md)

# Course 6 – Snake

**Goal:** the first game in a window. A key that arrives the moment it is pressed,
a timer that drives the game instead of a loop, and a snake that grows until you
drive it into something.

![The snake coiled above and left of centre, a red piece of food to the upper right, score 6](../assets/gameplay.png)

Requires [Course 1](../../01-basics/en/00-introduction.md) and nothing else. A
finished version is in [`code/`](../code/), in two files.

## 🟢 Core — Why a window at all

A terminal hands your program one line at a time. It waits for Enter, and nothing
you can write in Java changes that, because the waiting happens before your program
is involved. For a game where you steer something continuously, that is the end of
the road.

A window has no such middleman. Press a key and your program hears about it
immediately, key and all, while the game keeps running.

That is the only reason this course opens a window. Everything else about Swing in
this chapter is the minimum needed to get a rectangle onto the screen and a key
into your code.

## 🟢 Core — Classes and objects, in short

*(If you have done Course 7, skip this and carry on at the next section.)*

Course 1 called a class "a box with a name" and promised the real answer later.
Later is now.

A class describes what something **is** and what it **can do**, in one place. The
description names the values it holds, called **fields**, and the methods that work
on them. From the description you build a real thing with `new`, an **object**:

```java
public final class Counter {
    private int count;          // a field: every Counter has its own

    void add() {                // no static: this works on one Counter
        count++;
    }

    int get() {
        return count;
    }
}
```

```java
Counter a = new Counter();
Counter b = new Counter();
a.add();
a.add();
b.add();
System.out.println(a.get() + " " + b.get());
```

```
2 1
```

Two objects, two separate `count` fields. That is the whole difference from the
`static` methods in Course 1: those belong to the class and exist once each, while
these belong to an object and exist once per object.

Five words appear in this chapter that Course 1 never showed.

`extends` means "start from this class and change one thing". Whatever follows it
can do everything the named class can from its first line, and you only write down
what should be different.

`@Override` is a promise to the compiler: "this method already exists above me, I
am replacing it". It is optional and still the right thing to write. Mistype the
name and without `@Override` that is a new method nobody ever calls, and you get an
empty window with no error. With `@Override` the compiler tells you there is
nothing up there by that name.

`private` on a field means only this class may touch it, which keeps the game state
from being changed from outside by accident.

`protected` is the step in between: for the class itself and for anyone inheriting
from it. You will see it on `paintComponent` in a moment, and it is there not
because you chose it but because `JPanel` requires it. An override may make a
method more accessible, never less.

`final` on a class means nobody may build a variation on top of it; why that is the
case here is in the next section.

And one word that is easy to miss because it is not written at all: a method with
**no** visibility keyword belongs to the package. In the game below `spawnFood` is
`private`, because only the panel needs it, while `tick` and `steer` have no
keyword, because the other file calls them. Make `tick` private as an experiment
and the other file stops compiling.

## 🟢 Core — A window, a surface, and one method

Three facts, and you have everything this chapter needs from Swing.

**A window is a `JFrame`, and you do not draw on it.** You put something inside it
that can be drawn on.

**That something is a `JPanel`, and you draw by replacing a method.** You build your
own panel that `extends JPanel` and override `paintComponent`:

```java
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public final class SnakePanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.fillRect(40, 40, 20, 20);
    }
}
```

That is a complete file: save it as `SnakePanel.java` and `javac SnakePanel.java`
compiles it. You cannot run it yet, there is no `main`.

Remember where the names live, because that is the first thing people get stuck on:
`JPanel`, `JFrame` and `Timer` are in `javax.swing`, everything for drawing is in
`java.awt`, and everything about keys is in `java.awt.event`. The full list for this
game is in the next section.

`extends JPanel` means: be a panel in every respect, except for the one method
below. `super.paintComponent(g)` hands back to the original for a moment so that it
can clear the background, and leaving it out keeps the pixels of the last frame.

`Graphics g` is what you draw with. `fillRect(x, y, width, height)` is a filled
rectangle, `setColor` picks the colour, `drawString` writes text. The coordinates
are pixels from the top left, and y grows **downwards**.

**You never call `paintComponent` yourself.** You call `repaint()`, which asks Swing
to redraw when it gets the chance, and Swing then calls `paintComponent` for you.
That is the idea that takes longest to settle, and the rule that comes out of it is
short: change the game state first, then call `repaint`, never the other way round.

The class is `final`, out of caution rather than necessity. Its constructor calls
`setBackground` in a moment, a method inherited from `JPanel`. A subclass could
override it, and its version would then run before its own fields even exist. The
compiler knows that danger, but it only warns when the danger is real, and that
takes two things: a class without `final` **and** a constructor somebody outside the
package can reach. Try both:

```
remove the final                              -> silence, no warning
remove it and write public before SnakePanel() ->
warning: [this-escape] possible 'this' escape before subclass is fully initialized
```

The constructor here has no visibility keyword, so it would stay silent without
`final` too. The word is there anyway, because it rules the danger out for good,
including on the day somebody writes `public` in front of it.

## 🟢 Core — The skeleton, once in full

Everything still to come in this chapter — `tick`, `spawnFood`, `steer`,
`paintComponent` — belongs in **one** file, `SnakePanel.java`, inside the same
braces. Here it is with all the imports and all the fields, and with holes where you
fill them in:

```java
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;

public final class SnakePanel extends JPanel {

    static final int COLS = 20;
    static final int ROWS = 20;
    static final int CELL = 20;

    private final int[] x = new int[COLS * ROWS];
    private final int[] y = new int[COLS * ROWS];
    private int length = 3;

    private int dirX = 1;
    private int dirY = 0;

    private int foodX;
    private int foodY;
    private int score;
    private boolean gameOver;

    SnakePanel() {
        // two sections below
    }

    void tick() {
        // the core exercise
    }

    private void spawnFood() {
        // given in full in the core exercise
    }

    void steer(int keyCode) {
        // given in full in the section on keys
    }

    @Override
    protected void paintComponent(Graphics g) {
        // given in full in the section on drawing
    }
}
```

Two things about the three constants at the top are new.

They sit **inside the class and outside every method**, and that is the only reason
every method can reach them. The `final int` from Course 1 sat inside a method and
counted only there. Put `COLS` in the constructor and `paintComponent` will not find
it.

And they have **no `private`**. The other file, `Snake.java`, needs `COLS` and
`CELL` to work out the window size. With `private` you would get
`COLS has private access in SnakePanel` over there.

## 🟢 Core — The snake as two arrays

A list would be the grown-up answer, and Course 1 covered no lists. It does not have
to: the snake can never grow longer than the board, so a pair of arrays that size is
enough.

Position 0 is the head. `x[0]`, `y[0]` is where it stands, in **cells**, not in
pixels. The board is 20 by 20 cells of 20 pixels each, which keeps every sum in
whole numbers and makes "has the head reached the food" a plain comparison.

The direction is one step per tick. Right is `(1, 0)`, left `(-1, 0)`, up `(0, -1)`,
down `(0, 1)`. Up is negative because y grows downwards.

You build the starting snake in the constructor, a method with the name of the class
that runs when the object is created:

```java
SnakePanel() {
    setBackground(Color.BLACK);
    for (int i = 0; i < length; i++) {
        x[i] = 3 - i;
        y[i] = 3;
    }
    spawnFood();
}
```

Three segments in a row at `(3,3)`, `(2,3)`, `(1,3)`, head first.

## 🟢 Core exercise — One step of the game

```java
void tick() {
    // your code here -- the five steps below
}
```

1. If the game is already over, return and do nothing.
2. **Move the body.** Walk from the tail forwards, `i` from `length - 1` down to 1,
   and copy segment `i - 1` into segment `i`. Then move the head by adding `dirX`
   and `dirY` to `x[0]` and `y[0]`.
3. **Wall.** If the head's column is below 0 or at least `COLS`, or its row is below
   0 or at least `ROWS`, the game is over.
4. **Itself.** Compare the head against every other segment. Walk from 1 **up to and
   including** `length - 1`, that is `for (int i = 1; i < length; i++)`. A match ends
   the game.
5. **Food.** If the head is on the food: first give the new segment the position the
   tail is standing on, then add one to `length` and to the score, and put out new
   food.

Step 2 is where the bug is. Walking **forwards** through the body, from 1 to
`length - 1`, copies the head into the first segment and then the same value into
the second. The whole body ends up on one cell with the head one step ahead of it,
so what you see is two cells rather than a snake:

```
......oH............
```

What you notice in the running game is not "it collapsed" but **"it never gets
longer"**: you eat, the score goes up, and the picture stays the same. Backwards,
each segment takes the place of the one ahead of it before that one has moved. Get
it wrong once and watch.

In step 4, start at 1 and not at 0. The head always equals itself.

Step 5 is the one people do half of. `length++` on its own only makes the number
bigger; the segment that just joined never got a position, so it sits on whatever a
fresh `int[]` is full of, which is `(0, 0)`. A green block then flashes in the top
left corner for one tick, on every point. So first:

```java
x[length] = x[length - 1];
y[length] = y[length - 1];
```

That puts the new segment on the tail. The two lie on top of each other for one
tick, which you cannot see, and on the next tick the tail stays put while the rest
moves on. That is exactly what growing is.

`spawnFood` rolls a cell and tries again if the snake is lying on it:

```java
private void spawnFood() {
    while (true) {
        int fx = (int) (Math.random() * COLS);
        int fy = (int) (Math.random() * ROWS);

        boolean onSnake = false;
        for (int i = 0; i < length; i++) {
            if (x[i] == fx && y[i] == fy) {
                onSnake = true;
            }
        }

        if (!onSnake) {
            foodX = fx;
            foodY = fy;
            return;
        }
    }
}
```

## 🟢 Core — The drawing

```java
@Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    g.setColor(Color.RED);
    g.fillRect(foodX * CELL, foodY * CELL, CELL, CELL);

    for (int i = 0; i < length; i++) {
        g.setColor(i == 0 ? Color.GREEN : new Color(0, 160, 0));
        g.fillRect(x[i] * CELL, y[i] * CELL, CELL - 1, CELL - 1);
    }

    g.setColor(Color.WHITE);
    g.setFont(new Font("SansSerif", Font.PLAIN, 14));
    g.drawString("Score: " + score, 8, 18);

    if (gameOver) {
        g.setFont(new Font("SansSerif", Font.BOLD, 28));
        g.drawString("Game over", COLS * CELL / 2 - 70, ROWS * CELL / 2);
    }
}
```

Cells become pixels by multiplying with `CELL`. The `- 1` on width and height leaves
a pixel of air, and that is what makes the segments readable as segments rather than
as one long bar. The head is lighter than the body so that you can see where it is
going.

The background does not come from here but from `setBackground(Color.BLACK)` in the
constructor; it is painted by `super.paintComponent(g)` in the first line. Leave that
line out once: the snake leaves a solid trail, and the black is gone too, so "Score"
sits white on light grey.

## 🟢 Core — Keys

```java
void steer(int keyCode) {
    if (keyCode == KeyEvent.VK_LEFT && dirX != 1) {
        dirX = -1;
        dirY = 0;
    } else if (keyCode == KeyEvent.VK_RIGHT && dirX != -1) {
        dirX = 1;
        dirY = 0;
    } else if (keyCode == KeyEvent.VK_UP && dirY != 1) {
        dirX = 0;
        dirY = -1;
    } else if (keyCode == KeyEvent.VK_DOWN && dirY != -1) {
        dirX = 0;
        dirY = 1;
    }
}
```

Each branch rejects exactly the opposite of the current heading, because turning
round would drive the head straight into the neck.

The `VK_` names are constants for keys, and `getKeyCode()` gives you one of them.
Use `keyPressed` and not `keyTyped`: `keyTyped` is about characters, and an arrow key
produces none, which is the usual reason somebody's arrow keys appear to do nothing.

## 🟢 Core — The wiring, and the loop that is not one

All of this goes into the second file, `Snake.java`, and it is the only place that
knows there is a window. In full as well, because five imports hang off it that you
cannot guess:

```java
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Snake {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Snake::start);
    }

    static void start() {
        SnakePanel panel = new SnakePanel();
        panel.setPreferredSize(new Dimension(
                SnakePanel.COLS * SnakePanel.CELL,
                SnakePanel.ROWS * SnakePanel.CELL));
        panel.setFocusable(true);

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                panel.steer(event.getKeyCode());
            }
        });

        JFrame frame = new JFrame("Snake");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        panel.requestFocusInWindow();

        ActionListener step = event -> {
            panel.tick();
            panel.repaint();
        };
        new Timer(120, step).start();
    }
}
```

`SnakePanel.COLS` carries the class name here. Inside `SnakePanel` a plain `COLS` is
enough; here it is not, because `Snake` is a different class, and without the prefix
you get `cannot find symbol`. Do not declare the number a second time in
`Snake.java` — two truths for the same size are exactly the kind of bug you notice
three changes later.

Three notations in this file were not in Course 1, and all three are the same theme:
code short enough that a name is not worth it.

`Snake::start` is a **method reference** and means no more than "call this method".
`invokeLater` wants something it can run later, not a call, which is why there are no
parentheses after `start`.

`event -> { ... }` is a **lambda**, a method without a name. It works because
`ActionListener` demands exactly one method, so there is nothing to confuse. Written
out it would be a class of its own and ten lines for the same thing.

`new KeyAdapter() { ... }` is an **anonymous class**. `KeyAdapter` has three empty
methods, of which you override one, and the braces straight after the `new` are the
body of a subclass that never gets a name. A lambda will not do here, because
`KeyAdapter` has more than one method.

And `SwingUtilities.invokeLater` itself: Swing wants its work done on one particular
thread, and this is the way there. Take it as the phrase a Swing program starts with.

Four details about the wiring, and three of them you can break yourself and watch.

`setPreferredSize` on the panel plus `frame.pack()`, not `frame.setSize`. A window's
size includes the title bar and the border. Ask for 400 by 400 for the *window* and
you are left with a measured 386 by 363 pixels of drawing surface: nearly two rows
missing at the bottom, three quarters of a column at the right. Food that lands
there is reachable and invisible. `pack` asks the panel what it wants and builds the
window around it.

`new Timer(120, step)` is the game loop. Every 120 milliseconds it calls `tick` and
then `repaint`. There is no `while (true)` and no `Thread.sleep` anywhere, and that
is deliberate: Swing redraws on the same thread you would be sleeping on. Build it
with `Thread.sleep` once and you get not a slow game but a solid white window in
which no frame is ever drawn.

Import `javax.swing.Timer` by name. There is also a `java.util.Timer`, and with both
imported through a star the word `Timer` becomes ambiguous and the file will not
compile.

`setFocusable(true)` and `requestFocusInWindow()` after `setVisible(true)`. This pair
is precaution rather than rescue: a freshly opened window is active anyway, and on
Windows the arrow keys arrive even if you leave both lines out. They are there for
the case where the panel is not the only control, or the window does not come to the
front by itself, and they cost nothing.

Compile both files together and start the class with `main`:

```
javac *.java
java Snake
```

Not `java Snake.java`. The single-file shortcut from Course 1 compiles only the file
you hand it, and then it does not find `SnakePanel`. The error reads
`cannot find symbol`, which points nowhere near the cause.

If you want to compile with this repository's flags, use
`javac --release 21 -Xlint:all,-serial -Werror *.java`. The `-serial` has to be
there: every subclass of `JPanel` is serializable and has no `serialVersionUID`, and
without that exception you fail on a warning that has nothing to do with your code.

![Game over after a collision with its own body, score 12](../assets/game-over.png)

The head is not visible as a lighter block in that picture, and that is correct: it
is standing on a body segment, and the loop draws the body after the head. That is
exactly what "drove into itself" means.

## 🟡 Optional — Faster over time

Keep hold of the timer and call `setDelay` on it every few points, so that the game
speeds up. Decide what is fair, and remember that a game which never gets harder has
no shape.

Two obstacles stand in the way, and both are worth it, because they show how objects
reach one another.

The lambda in `start` cannot use the timer it creates itself: the lambda needs the
timer, the timer needs the lambda, and the compiler says
`variable timer might not have been initialized`. And the lambda cannot see the score
either, because `score` is `private`.

The way out is the same one as for the pause below: the panel gets a field for the
timer and a setter for it, `start` hands the finished timer in, and the panel
regulates the speed itself inside `tick`, where it has the score anyway.

## 🟡 Optional — One more round

After the game ends, let a key start a new round: reset the length, direction, score
and segment positions, and put out new food. The constructor already does exactly
that, so the honest way is to pull those lines into a `reset` method and call it from
both places.

## 🔴 Optional, genuine challenge — The reversal that gets through anyway

`steer` rejects a reversal, and it can still be beaten.

The snake is going right. Between two ticks, quickly press Up and then Left. Up is
allowed and sets the direction to up. Left is now checked against *up* and not
against *right*, so it is allowed too. The next tick drives the snake left, into its
own neck, and the game ends for no visible reason.

The cause is that `steer` checks against the direction as it is right now, and an
earlier key press in the same gap has already changed it. What it has to check
against is the direction the **last tick actually used**.

Fix it by writing key presses into a separate pending direction and having `tick`
adopt it first thing:

```java
private int nextDirX = 1;
private int nextDirY = 0;
```

Work out for yourself what `steer` then checks against and what `tick` does with the
two pairs. Reproduce the bug first, with two quick key presses, so that you can tell
your fix apart from luck.

A second, smaller challenge: pause on P. Stop the timer, write the word on the
screen, and start it again on the next P. For that the panel has to reach the timer,
so it needs a field for it and a setter called from `start` — the same wiring as for
the speed above.

## What you learned

- A class with fields and a constructor, and that objects hold their own
- `extends`, `@Override`, `private`, `protected`, and what it means when a method has
  no visibility keyword at all
- `static final` as a constant of the class, reachable from every method of it and,
  without `private`, from the other file too
- That `final` on a class is precaution here, and under which two conditions the
  compiler really does warn
- `JFrame` to hold, `JPanel` to draw on, `paintComponent` for the content
- `super.paintComponent(g)`, and what is left without it
- That you call `repaint()` and Swing calls `paintComponent`, never the reverse
- Drawing with `fillRect` and `drawString`, in pixels, with y growing downwards
- A timer as the game loop, and why a Swing game must not sleep
- A method reference, a lambda and an anonymous class, and when each one works
- `setPreferredSize` plus `pack` instead of `setSize`, and how many pixels it costs
- `keyPressed` with `VK_` codes, and why `keyTyped` misses arrow keys
- Moving a body from the tail forwards, and what happens the other way round
- That growing means giving the new segment a position, not just raising the length
- Compiling several files with `javac *.java`
- *(optional)* A pending direction, and a bug only two quick key presses reveal

## Next

Course 7 is Flappy Bird: gravity instead of a grid, a class that holds nothing but
data, and collision between rectangles. It requires Course 1 and nothing else, so it
explains the Swing parts it needs all over again.

The full list is in [PROJECT-IDEAS.md](../../../PROJECT-IDEAS.md).
