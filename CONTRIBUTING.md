🇬🇧 English | 🇩🇪 [Deutsch](CONTRIBUTING.de.md)

# Contributing to Java Games Course

This is a personal, solo-maintained project.

- Issues and pull requests are welcome, but there is no roadmap you need to fit into.
- Keep pull requests small and focused; a whole new course in one request is hard to review.
- There is no CLA.
- Response times vary. The project is a side project.

## Adding a new course

**Structure**

- A course is a folder `courses/NN-name/`, numbered in the order it was written.
  Adding one means updating the course table in `README.md` and `README.de.md`,
  the repository layout tree in both, and the matching entry in `PROJECT-IDEAS.md`
  and `PROJECT-IDEAS.de.md`.
- The folder slug is English even when the German title differs.
- **Course 1 is the only prerequisite.** Every course from 2 on assumes Course 1
  and nothing else -- never a sibling course. If a course needs something Course 1
  does not teach, it explains it inline in a compact `## 🟢 Core -- X, in short`
  section. That section opens with a skip note naming *every* sibling that teaches
  the same thing, not just the earlier ones: a reader can arrive at Course 4 having
  done only Course 5.
- Parallel `en/` and `de/` folders, identical in content and structure. Never one
  bilingual file. Chapter filenames are translated, the numeric prefix is not, so
  `01-towers-of-hanoi.md` and `01-tuerme-von-hanoi.md` are a pair. Umlauts in
  filenames are transliterated: `ue`, `oe`, `ae`.
- Code and code comments are always English, in both language versions. Only the
  prose differs, and the German prose uses the informal "du".
- The same three-level system in every chapter, introduced once in Course 1
  Chapter 0 and never re-explained: 🟢 Core (required), 🟡 Optional (more practice),
  🔴 Optional (a genuine challenge).
- `code/` holds one finished, runnable answer key for the 🟢 path. An optional
  section is only solved there if it lives in its own file with its own `main`,
  so nobody stumbles over the answer to the core exercise while looking for
  something else.
- **Screenshots are the exception, not the rule.** A console game shows a fenced
  example-output block instead. `assets/` exists only for a course with a real
  window.
- Javadoc has no German translation. German chapters link the same English pages
  as the English ones, and say so once in passing rather than apologising for it
  in every chapter.

**Java rules that chapters must follow**

These are not style preferences. Each one is a trap a beginner falls into once and
then spends an afternoon on.

- **Read input with `nextLine()` only**, then `Integer.parseInt(line.trim())`.
  `nextInt()` leaves the newline in the buffer, so the next `nextLine()` returns
  an empty string; and on letters it throws without consuming the token, which
  spins a naive retry loop forever.
- **Never close a `Scanner` that wraps `System.in`.** Closing it closes `System.in`
  for the rest of the program's life. An IDE will flag the unclosed scanner as a
  resource leak; that warning is wrong here, and the chapter says so where it first
  appears.
- **Program output is ASCII only.** `System.out` encodes with the console's code
  page, which on Windows is not UTF-8, so a box-drawing character arrives as `?`.
  Boards are built from `|`, `-`, `+`, `#` and `.`. Chapter markdown keeps its
  emoji section markers; `.java` files stay pure ASCII.
- **Clearing the console is not portable.** The ANSI sequence works in Windows
  Terminal and fails in a legacy console window. The 🟢 path prints successive
  states under a heading instead. The ANSI variant may appear as 🟡, with the
  caveat written out.
- **A public class lives in a file of exactly its own name**, and a class may not
  start with a digit, which is why Course 5 ships `Game2048` in `Game2048.java`.
- **Multi-file courses compile with `javac *.java` and run with `java Main`.**
  Not `java Main.java`: the single-file launcher compiles only the file it is
  given, and multi-file source launching is Java 22, while this repository targets
  Java 21.
- All course code lives in the default package. Real projects do not, and each
  course that spans more than one file says so in one sentence.
- No preview features. No instance `main` methods, no string templates. Java has
  no string interpolation; chapters use `+` and `printf`.

**Before publishing a new chapter, check for these recurring mistakes**

This is the checklist the sibling JavaScript and C# courses use, plus the Java
additions.

- Stale "Course N" and "Chapter N" cross-references. Grep for the singular and the
  plural, in both languages.
- A promise to explain something "later" that never arrives. Either deliver it or
  reword the promise.
- Re-verify the "only needs Course 1" claim against what Course 1 actually
  contains, not against what you remember putting there.
- **Every `code/` folder must compile clean under the CI flags**, which include
  `-Werror`. A reference solution that compiles with a warning teaches the wrong
  reflex to someone who cannot yet tell which warnings matter.
- **Run every snippet before publishing**, not just the finished program. A snippet
  that calls `Thread.sleep` will not compile outside a method that declares the
  exception, and that is the kind of thing only running it catches.
- Restate where code goes at every point the chapter could be read two ways.
- **Every snippet must compile on its own.** A fragment that ends in an opening
  brace gives `reached end of file while parsing`, which points at the last line
  of the file instead of the cause. If a snippet needs a surrounding variable or
  method, show it.
- **A chapter that builds a class prints it once as a complete skeleton** -- with
  every import, every field and every method header that will arrive separately
  later -- before the first piece is explained. "Where does this go" is the
  question a beginner cannot answer alone, and a field that is used and never
  declared holds them up longer than any explanation helps.
- **Imports belong in the chapter, not only in `code/`.** Course 1 shows a single
  import. That `JPanel` is in `javax.swing` and `Graphics` in `java.awt` cannot
  be guessed.
- **Every printed listing compiles exactly as printed.** Above all when reaching
  for another class's constant: `COLS` inside the class, `SnakePanel.COLS` from
  outside. A listing that forces the reader to declare the number a second time
  teaches precisely the wrong thing.
- **Every keyword and method is explained where it first appears**, including the
  small ones that feel invisible: `new`, `null`, `isEmpty`.
- **The file in `code/` follows the chapter**: same order, same values, same
  variable names. Somebody who is stuck reads them side by side.
- **Do not print a line number the reader cannot reproduce.** Either show the
  message without one, or say that it depends on where the line sits.
- **Every claim of the form "get it wrong and you will see Y" is reproduced before
  publishing.** That is the kind of sentence an attentive reader checks, and when
  Y does not happen they stop trusting the explanations that are right as well.
  The same goes for every number: frame rate, window size, drawing surface.
- **A screenshot is generated from the shipped code**, not staged by hand. What it
  shows has to match the caption and the chapter -- the segment count against the
  score, the visible parts against what the drawing method really paints in that
  situation.
- Each course is walked by a fresh reader who knows only Course 1, who types every
  snippet and runs it. Their sticking points are the chapter's bugs.

**How much code to show**

Courses 1 and 2 hand over complete, working code to type in. From Course 3 on, a
chapter explains the new building block in full and then describes the core
exercise -- the part that is the point of the chapter -- as numbered steps with a
skeleton:

```java
static int countNeighbours(boolean[][] grid, int row, int col) {
    // your code here -- the four steps above
}
```

The finished version lives only in `code/`, named in the chapter as a fallback for
someone who gets stuck, not as the next thing to read.

**Commit messages**

Conventional Commits, imperative and lowercase. A new course is usually one
`feat: add Course 3 - Towers of Hanoi` carrying chapter text, code and the
navigation updates together. Other prefixes in use: `fix:`, `docs:`, `ci:`,
`chore:`.
