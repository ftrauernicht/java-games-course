🇬🇧 English | 🇩🇪 [Deutsch](../de/01-tic-tac-toe.md)

[← Back to course overview](../../../README.md) · Assumes: [Course 1 – Basics](../../01-basics/en/00-introduction.md)

# Course 2 – Tic-Tac-Toe

**Goal:** a complete, playable game for two people at one keyboard. A board that
draws itself, moves that are checked before they are accepted, a winner that is
actually detected, and a scoreline across several rounds.

This is the first game, so you get all the code. Type it in piece by piece and run
it after every section. From Course 3 on you will get less, on purpose.

Everything this chapter needs is in [Course 1](../../01-basics/en/00-introduction.md)
and nothing else. A finished version lives in
[`code/TicTacToe.java`](../code/TicTacToe.java).

Start a file called `TicTacToe.java`:

```java
import java.util.Scanner;

public class TicTacToe {

    static final int SIZE = 3;

    public static void main(String[] args) {
    }
}
```

`SIZE` is `final` because a Tic-Tac-Toe board is three by three and will stay that
way. Writing `SIZE` instead of `3` everywhere costs nothing now and pays off in the
last optional section.

## 🟢 Core — A board is an array of arrays

Course 1 gave you an array: a row of values under one name. A board needs a grid,
and Java builds one out of the same part. An array whose values are themselves
arrays:

```java
char[][] board = new char[SIZE][SIZE];
```

Two pairs of brackets, two numbers. Read `board[1][2]` as "row 1, column 2", and
remember both count from 0, so that is the middle row, right-hand column.

`char` is the type because a square holds one of three things: an `X`, an `O`, or
nothing. A space will stand for nothing.

A fresh `char` array is full of a character that prints as nothing at all and is
not a space, so fill the board yourself. Add this method next to `main`:

```java
static char[][] newBoard() {
    char[][] board = new char[SIZE][SIZE];
    for (int row = 0; row < SIZE; row++) {
        for (int col = 0; col < SIZE; col++) {
            board[row][col] = ' ';
        }
    }
    return board;
}
```

A loop inside a loop. The outer one picks a row, and for each row the inner one
walks every column of it. Nine passes in total, which is the shape of almost every
piece of code in this chapter.

## 🟢 Core — Printing the board

An empty square shows its own number. That way the player can see what to type
without a separate diagram, and the board teaches its own controls:

```
 1 | 2 | 3 
---+---+---
 4 | 5 | 6 
---+---+---
 7 | 8 | 9 
```

```java
static void printBoard(char[][] board) {
    System.out.println();
    for (int row = 0; row < SIZE; row++) {
        for (int col = 0; col < SIZE; col++) {
            char mark = board[row][col];
            if (mark == ' ') {
                System.out.printf(" %d ", row * SIZE + col + 1);
            } else {
                System.out.printf(" %c ", mark);
            }
            if (col < SIZE - 1) {
                System.out.print("|");
            }
        }
        System.out.println();
        if (row < SIZE - 1) {
            System.out.println("---+---+---");
        }
    }
    System.out.println();
}
```

Two details worth slowing down for.

`row * SIZE + col + 1` turns a row and a column back into the number on screen.
Row 0 holds 1, 2, 3; row 1 holds 4, 5, 6. Each row down adds three, hence
`row * SIZE`, and the `+ 1` is because people count from one and arrays do not.

The two `if`s with `SIZE - 1` are why there is no trailing `|` on the right and no
line of dashes under the bottom row. A separator goes *between* things, so there is
always one fewer of them than of the things themselves.

Only plain ASCII goes on screen here. Java can produce prettier box-drawing
characters, but a Windows console will turn them into question marks, and a game
that looks broken on half the machines that run it is not finished.

Put this in `main` and run it:

```java
char[][] board = newBoard();
printBoard(board);
```

You should see the grid of numbers above.

## 🟢 Core — One digit, one square

The player types a number from 1 to 9. The board wants a row and a column. Course 1
gave you both halves of the conversion in one section, and here is where they earn
their place:

```java
static void place(char[][] board, int cell, char player) {
    int row = (cell - 1) / SIZE;
    int col = (cell - 1) % SIZE;
    board[row][col] = player;
}
```

Subtract 1 so the numbers run 0 to 8 instead of 1 to 9. Then integer division by 3
gives the row, because 0, 1 and 2 all divide to 0, while 3, 4 and 5 divide to 1.
The remainder gives the column, because it counts 0, 1, 2, 0, 1, 2 and starts over
exactly where a new row begins.

Work through square 6 on paper: `6 - 1` is 5, `5 / 3` is 1, `5 % 3` is 2. Row 1,
column 2, which is the right-hand square of the middle row. Check it against the
board above.

The same two lines answer whether a square is still free:

```java
static boolean isFree(char[][] board, int cell) {
    int row = (cell - 1) / SIZE;
    int col = (cell - 1) % SIZE;
    return board[row][col] == ' ';
}
```

Notice what `place` does: it changes the board that was handed to it, and the
change is still there after the method ends. That is the "arrays are references"
section of Course 1, doing real work for the first time.

## 🟢 Core — Reading a move that makes sense

Two things can be wrong with a move. It might not be a square number at all, and it
might be a square somebody already took. Neither should end the game:

```java
static int askForCell(Scanner in, char[][] board, char player) {
    while (true) {
        System.out.print("Player " + player + ", which square? ");
        String line = in.nextLine().trim();

        if (line.length() != 1 || line.charAt(0) < '1' || line.charAt(0) > '9') {
            System.out.println("Type a single digit from 1 to 9.");
            continue;
        }

        int cell = line.charAt(0) - '0';
        if (!isFree(board, cell)) {
            System.out.println("That square is taken.");
            continue;
        }

        return cell;
    }
}
```

The check is three tests joined with `||`, so any one of them failing is enough to
reject the line: not exactly one character, below `'1'`, or above `'9'`. Because
the squares are a single digit, this is simpler than the `isWholeNumber` method
from Course 1, and it rules out `10` and `0` at the same time.

`line.charAt(0) - '0'` turns the character into the number. Characters have numeric
values, `'7'` sits exactly seven places after `'0'`, and subtracting one from the
other leaves 7. You will see this in a lot of Java code.

`continue` jumps back to the top of the loop and asks again. `return` is the only
way out, and it only happens once both checks have passed, so whatever comes back
from this method is guaranteed to be a free square.

## 🟢 Core — Has somebody won?

Eight lines win the game: three rows, three columns, two diagonals. The rows and
columns are regular enough to loop over, the diagonals are two special cases:

```java
static boolean hasWon(char[][] board, char player) {
    for (int row = 0; row < SIZE; row++) {
        if (board[row][0] == player
                && board[row][1] == player
                && board[row][2] == player) {
            return true;
        }
    }

    for (int col = 0; col < SIZE; col++) {
        if (board[0][col] == player
                && board[1][col] == player
                && board[2][col] == player) {
            return true;
        }
    }

    if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
        return true;
    }

    return board[0][2] == player && board[1][1] == player && board[2][0] == player;
}
```

Look at the difference between the two loops. In the row loop the first index
changes and the second is written out; in the column loop it is the other way
round. That swap is the whole difference between a row and a column.

The method takes the player to check, rather than working out for itself who might
have won. That keeps it simple, and the caller already knows who just moved.

A board can also fill up with nobody winning:

```java
static boolean isFull(char[][] board) {
    for (int row = 0; row < SIZE; row++) {
        for (int col = 0; col < SIZE; col++) {
            if (board[row][col] == ' ') {
                return false;
            }
        }
    }
    return true;
}
```

One empty square anywhere and the answer is no, immediately. `return` inside the
loop means it stops looking the moment it knows.

## 🟢 Core — One round

Now the pieces go together. A round is a loop: show the board, take a move, check
whether that ended it, swap players.

```java
static char playOneRound(Scanner in) {
    char[][] board = newBoard();
    char player = 'X';

    while (true) {
        printBoard(board);

        int cell = askForCell(in, board, player);
        place(board, cell, player);

        if (hasWon(board, player)) {
            printBoard(board);
            System.out.println("Player " + player + " wins!");
            return player;
        }

        if (isFull(board)) {
            printBoard(board);
            System.out.println("A draw.");
            return ' ';
        }

        player = player == 'X' ? 'O' : 'X';
    }
}
```

Three things about the order here, all of which are wrong in somebody's first
version.

The win check comes **after** the move, not before. Nobody can win on a board they
have not played on yet.

The full check comes **after** the win check. The ninth move can win the game, and
asking "is it full" first would call that a draw.

The swap is the **last** thing in the loop, so the checks above it are still asking
about the player who just moved.

The method hands back who won, with a space meaning a draw. Returning a `char` is
enough here; a space is not a player, so it cannot be confused with one.

Now `main` can be three lines:

```java
public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    playOneRound(in);
}
```

Run it and play a full game. Try to win along the top row, then run it again and
play to a draw. Type a letter when it asks for a square, and type a square that is
already taken, and check that neither throws you out.

## 🟢 Core — Play again, and keep score

One game and the program exits, which is not how anybody plays Tic-Tac-Toe. Wrap
the round in a loop and count:

```java
public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    int winsX = 0;
    int winsO = 0;
    int draws = 0;

    while (true) {
        char winner = playOneRound(in);
        if (winner == 'X') {
            winsX++;
        } else if (winner == 'O') {
            winsO++;
        } else {
            draws++;
        }

        System.out.println("X " + winsX + " - O " + winsO + " - draws " + draws);
        System.out.print("Another round? (y/n) ");
        String answer = in.nextLine().trim().toLowerCase();
        if (!"y".equals(answer)) {
            System.out.println("Thanks for playing.");
            return;
        }
    }
}
```

`"y".equals(answer)` rather than `answer.equals("y")`, with the known value first.
Course 1 mentioned the habit; this is the kind of place it belongs. And `.equals`
rather than `==`, because this is text somebody typed, which is exactly the case
where `==` lies.

`return` from `main` ends the program. The counters live outside the loop, which is
why they survive from one round to the next.

That is the game. Play a few rounds.

## 🟡 Optional — A win check that does not care how big the board is

`hasWon` writes out `board[row][0]`, `board[row][1]`, `board[row][2]`. Change
`SIZE` to 4 and that method silently keeps checking three in a row on a four-wide
board, which is worse than failing.

Rewrite it so the only thing it assumes is `SIZE`. One way: a helper that starts at
some square and walks `SIZE` steps in a given direction.

```java
static boolean allMatch(char[][] board, char player, int row, int col,
                        int rowStep, int colStep) {
    // your code here -- walk SIZE squares from (row, col), adding rowStep to
    // the row and colStep to the column each time, and answer whether every
    // square held player
}
```

Then each of the eight lines is one call. Row `i` starts at `(i, 0)` and steps
`(0, 1)`. The downward diagonal starts at `(0, 0)` and steps `(1, 1)`. Work out the
other two shapes yourself, and mind that one of the diagonals steps backwards.

Set `SIZE` to 4 afterwards and check that four in a row is what wins. The board
printer and `place` already cope, which is what `SIZE` bought you.

## 🟡 Optional — Let the loser start

X moves first every round, which is a real advantage. Keep track of who started and
alternate it, or better, let whoever lost the previous round go first.

`playOneRound` will need to be told who begins, so give it a second parameter and
set `player` from it instead of always `'X'`.

## 🔴 Optional, genuine challenge — A computer opponent

Replace player O with the program, so one person can play alone.

Start with the simplest opponent that is not broken: pick a free square at random.

```java
static int randomFreeCell(char[][] board) {
    // your code here -- pick a number from 1 to 9 until it lands on a free
    // square, then return it
}
```

That plays legally and badly. Now give it two rules, in this order:

1. If there is a square that wins the game this turn, take it.
2. Otherwise, if there is a square the human would win with next turn, take that
   one away.
3. Otherwise, random.

Both rules ask the same question about different players: "is there a free square
that would complete a line for this mark?" So write it once:

```java
static int findWinningCell(char[][] board, char mark) {
    // your code here -- for every free square: put mark there, ask hasWon,
    // then take it back off again. Return the first square that wins, or 0
    // if none does.
}
```

Trying a move and undoing it is the idea worth taking away from this challenge. You
already have `hasWon`, so you do not need to reason about lines at all; you let the
method you already trust answer a hypothetical question. Returning 0 for "none"
works because squares are numbered from 1.

Test it properly. Take squares 1 and 2, and check the program takes 3 before you
can. Then give yourself two in a row while the program also has two in a row, and
check it takes its own win rather than blocking you. Rule 1 before rule 2 is what
decides that, and swapping them is the most common way to get this wrong.

A program with these two rules cannot be beaten by accident, only by a plan. It can
still be beaten: there is a third rule about forks, and beyond that an approach
called minimax that plays perfectly by trying every possible continuation. Minimax
needs recursion, which is Course 3, and it is not needed here.

If you get stuck, [`code/TicTacToeComputer.java`](../code/TicTacToeComputer.java)
has a working version.

## What you learned

- A grid as `char[][]`, and why `board[1][2]` is row 1, column 2
- Filling a two-dimensional array with a loop inside a loop
- Turning one number into a row and a column with `/` and `%`
- That a method changes the board it is handed, rather than a copy of it
- Rejecting input without ending the game, using `continue`
- `line.charAt(0) - '0'` to turn a digit character into a number
- Checking rows, columns and diagonals, and why the loops differ by one index
- The order a game loop has to use: move, win, full, swap
- Counters that live outside a loop so they survive each pass
- *(optional)* Writing a check that depends only on `SIZE`
- *(optional)* Trying a move and undoing it to ask a what-if question

## Next

Course 3 is Towers of Hanoi, and it is the shortest program in this repository and
one of the hardest to believe. It assumes Course 1 and nothing else, like every
course here.

The full list is in [PROJECT-IDEAS.md](../../../PROJECT-IDEAS.md).
