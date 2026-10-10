// Course 4 - Conway's Game of Life.
// A finished version of the green path. Reads nothing; stops after a fixed
// number of generations.
//
// Compile with:  javac GameOfLife.java
// Run with:      java GameOfLife
public class GameOfLife {

    static final int ROWS = 10;
    static final int COLS = 20;
    static final int GENERATIONS = 8;
    static final int PAUSE_MS = 300;

    public static void main(String[] args) throws InterruptedException {
        boolean[][] grid = new boolean[ROWS][COLS];

        // A glider: five live cells that walk diagonally across the grid.
        grid[1][2] = true;
        grid[2][3] = true;
        grid[3][1] = true;
        grid[3][2] = true;
        grid[3][3] = true;

        for (int generation = 0; generation <= GENERATIONS; generation++) {
            print(grid, generation);
            Thread.sleep(PAUSE_MS);
            grid = nextGeneration(grid);
        }
    }

    // Builds the next generation in a NEW grid. Writing into the old one
    // while still reading from it means later cells see their neighbours
    // already updated, which produces a plausible-looking wrong simulation.
    static boolean[][] nextGeneration(boolean[][] grid) {
        boolean[][] next = new boolean[ROWS][COLS];

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int neighbours = countNeighbours(grid, row, col);

                if (grid[row][col]) {
                    next[row][col] = neighbours == 2 || neighbours == 3;
                } else {
                    next[row][col] = neighbours == 3;
                }
            }
        }

        return next;
    }

    // Counts the live cells in the eight squares around (row, col). Squares
    // outside the grid count as dead, which is what the bounds check does.
    static int countNeighbours(boolean[][] grid, int row, int col) {
        int count = 0;

        for (int dRow = -1; dRow <= 1; dRow++) {
            for (int dCol = -1; dCol <= 1; dCol++) {
                if (dRow == 0 && dCol == 0) {
                    continue;
                }

                int r = row + dRow;
                int c = col + dCol;

                if (r < 0 || r >= ROWS || c < 0 || c >= COLS) {
                    continue;
                }

                if (grid[r][c]) {
                    count++;
                }
            }
        }

        return count;
    }

    // Prints generations one under another rather than clearing the screen.
    // Clearing needs terminal escape codes that a plain Windows console
    // ignores, and a scrollback you can read beats an animation you cannot.
    static void print(boolean[][] grid, int generation) {
        System.out.println("Generation " + generation);
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                System.out.print(grid[row][col] ? '#' : '.');
            }
            System.out.println();
        }
        System.out.println();
    }
}
