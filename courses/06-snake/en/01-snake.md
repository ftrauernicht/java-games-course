🇬🇧 English | 🇩🇪 [Deutsch](../de/01-snake.md)

[← Back to course overview](../../../README.md) · Assumes: [Course 1 – Basics](../../01-basics/en/00-introduction.md)

# Course 6 – Snake

**Goal:** the first game in a window. A key that arrives the moment it is pressed,
a timer that drives the game instead of a loop, and a snake that grows until you
drive it into something.

![The snake coiled in the middle of the board, a red piece of food to the upper right, score 6](../assets/gameplay.png)

Assumes [Course 1](../../01-basics/en/00-introduction.md) and nothing else. A
finished version lives in [`code/`](../code/), in two files.

## 🟢 Core — Why a window at all

A terminal hands your program a line at a time. It waits for Enter, and nothing
you can write in Java changes that, because the waiting happens before your
program is involved. For a game where you steer something continuously, that is
the end of the road.

A window has no such middleman. Press a key and your program is told at once,
which key, while the game keeps running.

That is the only reason this course opens a window. Everything else about Swing in
this chapter is the smallest amount needed to get a rectangle on screen and a key
into your code.

## 🟢 Core — Classes and objects, in short

*(If you have done Course 7, skip ahead to the next section.)*

Course 1 called a class "a box with a name" and promised the real answer later.
This is later.

A class describes what something **is** and what it **can do**, in one place. The
description lists the values it holds, called **fields**, and the methods that work
on them. From the description you can make an actual thing, an **object**, with
`new`:

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
`static` methods of Course 1: those belong to the class and there is one of each,
while these belong to an object and there is one per object.

Two more words you will see in this chapter. `private` on a field means only this
class may touch it, which keeps the game state from being changed by accident from
outside. `final` on a class means nobody may build a variant of it on top; this
chapter explains in a moment why that matters here.

And one you already know, with a new use: `extends`. It means "start from that
class and change one thing".

## 🟢 Core — A window, a panel, and one method

Three facts and you have everything this chapter needs from Swing.

**A window is a `JFrame`, and you do not draw on it.** You put something inside it
that can be drawn on.

**That something is a `JPanel`, and you draw by replacing one method.** You make
your own panel that `extends JPanel` and overrides `paintComponent`:

```java
public final class SnakePanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.fillRect(40, 40, 20, 20);
    }
}
```

`extends JPanel` means: be a panel in every respect, except for the one method
written below. `super.paintComponent(g)` hands control back to the original for a
moment so it can clear the background, and leaving it out leaves last frame's
pixels behind.

`Graphics g` is what you draw with. `fillRect(x, y, width, height)` is a filled
rectangle, `setColor` picks the colour, and `drawString` writes text. Coordinates
are pixels from the top left, with y growing **downwards**.

**You never call `paintComponent` yourself.** You call `repaint()`, which asks
Swing to redraw when it next can, and Swing calls `paintComponent` for you. This is
the idea that takes longest to settle, and the rule that follows from it is short:
change the game state, then call `repaint`, and never the other way round.

The class is `final` for a concrete reason. A panel that is not final can be
extended from elsewhere, and the compiler then warns that handing `this` to
anything inside the constructor could expose a half-built object. This repository
builds with warnings as errors, so the warning would stop the build. `final` says
nobody will extend it, and the warning goes away.

## 🟢 Core — The snake, as two arrays

A list would be the grown-up answer, and Course 1 did not cover lists. It does not
need to: the snake can never be longer than the board, so a pair of arrays of that
size is enough.

```java
private final int[] x = new int[COLS * ROWS];
private final int[] y = new int[COLS * ROWS];
private int length = 3;
```

Position 0 is the head. `x[0]`, `y[0]` is where it is, in **cells**, not pixels.
The board is 20 by 20 cells of 20 pixels each, which keeps every calculation in
whole numbers and makes "did the head reach the food" a plain comparison.

Direction is one step per tick:

```java
private int dirX = 1;
private int dirY = 0;
```

Right is `(1, 0)`, left `(-1, 0)`, up `(0, -1)`, and down `(0, 1)`. Up is negative
because y grows downwards.

Set up the starting snake in the constructor, a method with the class's own name
that runs when the object is made:

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
3. **Wall.** If the head's column is below 0 or at least `COLS`, or its row below 0
   or at least `ROWS`, the game is over.
4. **Itself.** Compare the head against every other segment, from 1 up to
   `length - 1`. A match ends the game.
5. **Food.** If the head is on the food, add one to `length` and to the score, and
   put new food out.

Step 2 is where the bug lives. Walking the body **forwards**, from 1 to
`length - 1`, copies the head into the first segment and then that same value into
the second, and the whole snake collapses onto one square. Backwards, each segment
takes the place of the one ahead of it before that one has moved. Try it the wrong
way once and watch.

In step 4, start at 1 and not at 0. The head is always equal to itself.

`spawnFood` picks a random cell and tries again if the snake is on it:

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

## 🟢 Core — Drawing it

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

Cells become pixels by multiplying by `CELL`. The `- 1` on the width and height
leaves a one-pixel gap, which is what makes the segments readable as segments
rather than one long bar. The head is brighter than the body so you can see which
way it is going.

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

Each branch refuses the exact opposite of where the snake is going, because
reversing would drive the head straight into the neck.

The `VK_` names are constants for keys, and `getKeyCode()` produces one of them.
Use `keyPressed` rather than `keyTyped`: `keyTyped` is about characters, and an
arrow key does not produce one, which is the usual reason somebody's arrow keys
appear to do nothing.

## 🟢 Core — The wiring, and the loop that is not a loop

All of this goes in the second file, `Snake.java`, and it is the only place that
knows a window exists.

```java
public static void main(String[] args) {
    SwingUtilities.invokeLater(Snake::start);
}
```

Swing wants all its work on one particular thread, and `invokeLater` is how you
get there. Treat it as the phrase a Swing program starts with.

```java
static void start() {
    SnakePanel panel = new SnakePanel();
    panel.setPreferredSize(new Dimension(COLS * CELL, ROWS * CELL));
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
```

Four details, each of which costs an evening if you get it wrong.

`setPreferredSize` on the panel plus `frame.pack()`, not `frame.setSize`. A frame's
size includes its title bar and borders, so asking the frame for 400 by 400 gives
you a drawing area smaller than that, and the bottom row of your board quietly
disappears. `pack` asks the panel what it wants and sizes the frame around it.

`setFocusable(true)` and `requestFocusInWindow()`, and the second one **after**
`setVisible(true)`. A panel that does not have the keyboard focus receives no keys,
and a window that is not on screen yet cannot give focus to anything.

`new Timer(120, step)` is the game loop. Every 120 milliseconds it calls `tick`
and then `repaint`. There is no `while (true)` and no `Thread.sleep` anywhere,
and that is deliberate: Swing redraws on the same thread you would be sleeping on,
so a sleeping game is a frozen window.

Import `javax.swing.Timer` by name. There is also a `java.util.Timer`, and with
both imported by wildcard the word `Timer` becomes ambiguous and the file will not
compile.

Compile both files together and run the class with `main`:

```
javac *.java
java Snake
```

Not `java Snake.java`. The single-file shortcut from Course 1 compiles only the
file you hand it, and then cannot find `SnakePanel`. The error says
`cannot find symbol`, which does not point at the cause at all.

![Game over, the snake stopped at the right-hand wall, score 11](../assets/game-over.png)

## 🟡 Optional — Faster as you go

Keep a reference to the timer and call `setDelay` on it every few points, so the
game speeds up. Decide how much is fair, and notice that a game which never gets
harder has no shape to it.

## 🟡 Optional — Play again

On game over, let a key start a new round: reset the length, the direction, the
score and the segment positions, and put new food out. The constructor already
does all of that, so the honest move is to pull those lines into a `reset` method
and call it from both places.

## 🔴 Optional, genuine challenge — The reversal that still gets through

`steer` refuses a reversal, and it can still be beaten.

The snake is moving right. Between two ticks, press Up and then Left, quickly.
Up is allowed and sets the direction to up. Left is now compared against *up*, not
against *right*, so it is allowed too. The next tick moves the snake left, into its
own neck, and the game ends for no visible reason.

The cause is that `steer` compares against the direction as it is right now, which
a previous key press in the same gap has already changed. It needs to compare
against the direction the **last tick actually used**.

Fix it by writing the key presses into a separate pending direction, and having
`tick` take that over as the first thing it does:

```java
private int nextDirX = 1;
private int nextDirY = 0;
```

Work out for yourself what `steer` then compares against, and what `tick` does with
the two pairs. Reproduce the bug first, with two fast presses, so you can tell your
fix apart from luck.

A second challenge, smaller: pause on P. Stop the timer, draw the word on screen,
and start it again on the next P. You will need the panel to be able to reach the
timer, which means a field for it and a setter called from `start`.

## What you learned

- A class with fields and a constructor, and that objects each hold their own
- `private` to keep state inside, and `final` on a class, and why this one is final
- `JFrame` to hold, `JPanel` to draw on, `paintComponent` to decide what appears
- `super.paintComponent(g)`, and what is left behind without it
- That you call `repaint()` and Swing calls `paintComponent`, never the reverse
- Drawing with `fillRect` and `drawString`, in pixels, with y growing downwards
- A timer as the game loop, and why a Swing game must not sleep
- `setPreferredSize` plus `pack` instead of `setSize`
- Focus, and `requestFocusInWindow` after the window is visible
- `keyPressed` with `VK_` codes, and why `keyTyped` misses arrow keys
- Moving a body from the tail forwards, and what happens if you go the other way
- Compiling several files with `javac *.java`
- *(optional)* A pending direction, and a bug that only two fast key presses show

## Next

Course 7 is Flappy Bird: gravity instead of a grid, a class that holds nothing but
data, and collision between rectangles. It assumes Course 1 and nothing else, so it
re-explains the Swing parts it needs.

The full list is in [PROJECT-IDEAS.md](../../../PROJECT-IDEAS.md).
