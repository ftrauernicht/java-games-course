// Course 4 - Conway's Game of Life.
// A finished version of the green path. Reads nothing; stops after a fixed
// number of generations.
//
//   java GameOfLife        a glider, the pattern you can check your rules against
//   java GameOfLife soup   a random start, which is what the rules are really for
//
// The grid is shown in a window rather than printed. That needs no class of
// your own: a BufferedImage is a picture you paint into, and a JLabel is a
// component that shows one. Nothing here overrides a method or listens for
// anything -- Course 6 is where a window has to answer the keyboard, and that
// is what costs the extra machinery.
//
// Compile with:  javac GameOfLife.java
// Run with:      java GameOfLife
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GameOfLife {

    static final int ROWS = 30;
    static final int COLS = 40;
    static final int CELL = 14;
    static final int GENERATIONS = 100;
    static final int PAUSE_MS = 80;

    static final Color DEAD = new Color(24, 26, 30);
    static final Color ALIVE = new Color(90, 200, 110);

    public static void main(String[] args) throws InterruptedException {
        boolean soup = args.length > 0 && "soup".equals(args[0]);
        boolean[][] grid = soup ? randomGrid() : glider();

        BufferedImage picture =
                new BufferedImage(COLS * CELL, ROWS * CELL, BufferedImage.TYPE_INT_RGB);
        JLabel label = new JLabel(new ImageIcon(picture));
        JFrame window = new JFrame("Game of Life");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(label);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        for (int generation = 0; generation <= GENERATIONS; generation++) {
            draw(picture, grid);
            label.repaint();

            // The window shows only the present. The console keeps the trace,
            // so two generations can still be compared after the fact.
            System.out.println("Generation " + generation + ": "
                    + countAlive(grid) + " live cells");

            Thread.sleep(PAUSE_MS);
            grid = nextGeneration(grid);
        }
    }

    // Five live cells that walk diagonally across the grid, keeping their
    // shape and repeating it every four generations.
    static boolean[][] glider() {
        boolean[][] grid = new boolean[ROWS][COLS];
        grid[1][2] = true;
        grid[2][3] = true;
        grid[3][1] = true;
        grid[3][2] = true;
        grid[3][3] = true;
        return grid;
    }

    // Roughly three cells in ten alive, placed at random.
    static boolean[][] randomGrid() {
        boolean[][] grid = new boolean[ROWS][COLS];
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                grid[row][col] = Math.random() < 0.3;
            }
        }
        return grid;
    }

    // Paints the whole grid into the picture. The window learns nothing about
    // it until repaint() asks it to show the picture again.
    static void draw(BufferedImage picture, boolean[][] grid) {
        Graphics g = picture.createGraphics();

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                g.setColor(grid[row][col] ? ALIVE : DEAD);
                g.fillRect(col * CELL, row * CELL, CELL - 1, CELL - 1);
            }
        }
    }

    static int countAlive(boolean[][] grid) {
        int count = 0;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (grid[row][col]) {
                    count++;
                }
            }
        }
        return count;
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

                if (r >= 0 && r < ROWS && c >= 0 && c < COLS && grid[r][c]) {
                    count++;
                }
            }
        }

        return count;
    }
}
