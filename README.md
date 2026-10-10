🇬🇧 English | 🇩🇪 [Deutsch](README.de.md)

# Java Games Course

A course for people who have never programmed, built around games you can actually
play when you are done. Every course is one game, and the games get harder in the
order they are numbered: two players on a paper grid first, a bird falling past
pipes at the end.

Java is the language, and the whole course gets by with what the JDK already gives
you. No build tool, no framework, no dependency to install. You write a file, you
compile it, you run it. That is the entire toolchain, and it stays that way for all
seven courses.

Everything exists twice, in English and in German. The prose is translated; the code
is not. Variable names, method names and comments are English everywhere, because
that is what you will read in every real codebase and in every error message.

## Get this course onto your computer

**Option A — no Git required**

1. Click the green **Code** button at the top of this page.
2. Choose **Download ZIP**.
3. Unpack the archive wherever you keep your projects.
4. Open the folder in your editor.

**Option B — with Git**

```
git clone https://github.com/ftrauernicht/java-games-course.git
```

## What you need before you start

| Tool | Why you need it | Link |
|---|---|---|
| A JDK, version 21 or newer | Compiles and runs Java. Nothing works without it. | [adoptium.net](https://adoptium.net/) |
| IntelliJ IDEA Community Edition | A free editor that understands Java, finishes your typing and shows mistakes as you make them. | [jetbrains.com](https://www.jetbrains.com/idea/download/) |

Course 1 walks you through installing both, and through compiling your first program
on the command line before you let the editor do it for you.

## Courses

Course 1 teaches the language. It is the only course the others require. Everything
after it is a game, and you can play them in any order you like: Course 5 does not
assume Course 4, and Course 7 does not assume Course 6. Where a game needs something
Course 1 did not cover, that chapter explains it again in a few paragraphs and tells
you to skip ahead if you already know it.

### Course 1 – Basics (`courses/01-basics/`)

The language itself, in three chapters and without a game. It is the only course
the others require, so it covers everything they lean on: types and control flow,
your own methods, arrays, and reading what somebody types.

| # | Chapter | What you'll learn |
|---|---|---|
| 0 | [Introduction & Tools](courses/01-basics/en/00-introduction.md) | Installing the JDK and an editor, compiling and running, what every word of `public static void main` means, reading a compiler error |
| 1 | [Java Basics](courses/01-basics/en/01-java-basics.md) | Types, arithmetic and the surprise of `7 / 2`, casting and random numbers, `.equals` for text, `if`, `switch`, `for`, `while` |
| 2 | [Methods, Arrays and Input](courses/01-basics/en/02-methods-arrays-input.md) | Your own methods, arrays and why they behave differently from an `int`, `Scanner`, and a number-guessing game |

### Course 2 – Tic-Tac-Toe (`courses/02-tic-tac-toe/`)

Two players at one keyboard, on a board that prints its own controls. The first
game, so it hands you every line of code.

| # | Chapter | What you'll learn |
|---|---|---|
| 1 | [Tic-Tac-Toe](courses/02-tic-tac-toe/en/01-tic-tac-toe.md) | A grid as `char[][]`, a loop inside a loop, turning one digit into a row and a column, rejecting bad moves without ending the game, checking eight win lines, and the order a game loop has to use |

### Course 3 – Towers of Hanoi (`courses/03-towers-of-hanoi/`)

Three pegs, a stack of discs, and a solution three lines long. The first chapter
that explains the idea in full and leaves the writing to you.

| # | Chapter | What you'll learn |
|---|---|---|
| 1 | [Towers of Hanoi](courses/03-towers-of-hanoi/en/01-towers-of-hanoi.md) | Recursion, the base case, why calls pile up and wait, trusting a call to a method that is not finished yet, and two arrays used as a stack |

## Project ideas

The full roadmap, including the games that are not built yet, lives in
[PROJECT-IDEAS.md](PROJECT-IDEAS.md).

## How to use this course

1. **Type the examples instead of copy-pasting them.** Typing is what builds the
   muscle memory; copy-pasting only builds a scrollbar.
2. **Follow the three markers.** 🟢 is the path through the chapter and you need all
   of it. 🟡 is extra practice on the same idea. 🔴 is a real challenge, and skipping
   it costs you nothing later.
3. **Run the program after every section.** Each one ends by telling you what you
   should see. If you see something else, the mistake is still small and close by.
4. **Use `code/` as an answer key, not as a shortcut.** It holds a finished version
   of the 🟢 path for when you are stuck, which is a different thing from reading it
   first.
5. **Read the compiler errors.** Java writes them in full sentences and they are
   usually right. Course 1 makes one on purpose so the first real one is not a
   surprise.

## Repository layout

```
courses/
  01-basics/
    en/  de/                 three chapters: tools, the language, methods and input
    code/                    Hello.java, Basics.java, GuessTheNumber.java
  02-tic-tac-toe/
    en/  de/                 one chapter
    code/                    the finished game
  03-towers-of-hanoi/
  04-game-of-life/
  05-2048/
  06-snake/                  adds assets/ for screenshots, since it has a window
  07-flappy-bird/
  08-.../                    future courses, same pattern
scripts/                     the checks CI runs
```

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md).

## License

MIT, see [LICENSE](LICENSE).
