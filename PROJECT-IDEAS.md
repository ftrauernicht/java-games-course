🇬🇧 English | 🇩🇪 [Deutsch](PROJECT-IDEAS.de.md)

[← Back to course overview](README.md)

# This repository's course roadmap

This is not a list of side projects. Every entry below is either a course this
repository has built or one it intends to build, in the order the games get harder.
The difficulty rating is about the thinking each game demands, not the number of
lines it takes.

| # | Course | New skills, on top of what you already have | Difficulty | Status |
|---|---|---|---|---|
| 1 | Basics -- the language itself, no game yet | types, operators, control flow, methods, arrays, keyboard input | ⭐ | ✅ [Built](courses/01-basics/en/00-introduction.md) |
| 2 | Tic-Tac-Toe -- two players, one board, nine squares | a board as a two-dimensional array, nested loops, passing an array to a method | ⭐ | Planned |
| 3 | Towers of Hanoi -- the puzzle solves itself | recursion, the base case, reading a call stack | ⭐⭐ | Planned |
| 4 | Conway's Game of Life -- cells live and die on their own | computing the next state into a second grid, counting neighbours at the edges, pausing between frames | ⭐⭐ | Planned |
| 5 | 2048 -- slide, merge, repeat | transforming a grid, reducing four directions to one operation, the merge rule everyone gets wrong | ⭐⭐⭐ | Planned |
| 6 | Snake -- the first one in a window | Swing, drawing on a panel, a timer as the game loop, reading keys as they are pressed | ⭐⭐⭐ | Planned |
| 7 | Flappy Bird -- gravity, pipes, one button | movement as velocity, a class that holds data, rectangle collision | ⭐⭐⭐⭐ | Planned |
| 8 | Pong -- two paddles and an angle | reflecting a ball off a surface, a second player on the same keyboard | ⭐⭐⭐ | Idea |
| 9 | Minesweeper -- counting what you cannot see | mouse input, flood fill from a single click, a second grid that hides the first | ⭐⭐⭐⭐ | Idea |
| 10 | Breakout -- Pong with consequences | a grid of destructible bricks, levels, scoring that survives a round | ⭐⭐⭐⭐ | Idea |
| 11 | Tetris -- the one that earns its reputation | rotating a shape around a point, collision before the move, clearing and dropping rows | ⭐⭐⭐⭐⭐ | Idea |
| 12 | Leaving the plain JDK behind | a build tool, dependencies, and the first real test suite | ⭐⭐⭐ | Idea |

A few notes on how to read this list:

- **Course 1 is the only prerequisite anywhere.** Courses 2 and up can be done in
  any order. Where a later game needs something Course 1 does not teach, that course
  teaches it again, briefly, with a note telling you to skip ahead if you have seen
  it already.
- **The order is by difficulty, not by dependency.** Tic-Tac-Toe comes before Towers
  of Hanoi because recursion is harder to hold in your head than a nested loop, even
  though the Hanoi solution is five lines and the Tic-Tac-Toe one is a hundred.
- **Courses 1 and 2 hand you finished code to type in.** From Course 3 on, you get
  the new building block explained in full and then assemble the heart of the game
  yourself from described steps. The finished version is always there if you need it.
- **The console runs out of road at Course 5.** Java cannot read a single keypress
  from a terminal, so 2048 asks you to type a letter and press Enter. That limit is
  exactly why the last two courses move into a window, where a key arrives the moment
  it is pressed.
- **Course 12 is the only one that would add a build tool.** Everything before it
  runs with the two commands you learn in Course 1, which is the point: nothing
  stands between you and the program except a compiler.
