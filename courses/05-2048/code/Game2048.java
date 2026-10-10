// Course 5 - 2048.
// A finished version of the green path.
//
// The class cannot be called 2048: a Java name may not start with a digit.
//
// Compile with:  javac Game2048.java
// Run with:      java Game2048
import java.util.Scanner;

public class Game2048 {

    static final int SIZE = 4;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int[][] board = new int[SIZE][SIZE];

        spawn(board);
        spawn(board);

        while (true) {
            print(board);

            System.out.print("Move (w/a/s/d, q to quit): ");
            String line = in.nextLine().trim().toLowerCase();

            if ("q".equals(line)) {
                System.out.println("Bye.");
                return;
            }

            int rotations = switch (line) {
                case "a" -> 0;
                case "s" -> 1;
                case "d" -> 2;
                case "w" -> 3;
                default -> -1;
            };

            if (rotations < 0) {
                System.out.println("Type w, a, s or d, or q to quit.");
                continue;
            }

            int[][] before = copy(board);
            move(board, rotations);

            // A move that changes nothing is not a move, and it must not
            // hand the player a free tile.
            if (same(before, board)) {
                System.out.println("Nothing moves that way.");
                continue;
            }

            spawn(board);

            if (isFull(board)) {
                print(board);
                System.out.println("Board full. Game over.");
                return;
            }
        }
    }

    // --- Moving ---

    // Every direction is "slide left" on a rotated board. Rotate, slide,
    // rotate back. One direction to get right instead of four.
    static void move(int[][] board, int rotations) {
        for (int i = 0; i < rotations; i++) {
            rotateClockwise(board);
        }

        for (int row = 0; row < SIZE; row++) {
            board[row] = slideRowLeft(board[row]);
        }

        for (int i = 0; i < (4 - rotations) % 4; i++) {
            rotateClockwise(board);
        }
    }

    // Compact, merge once, compact again. The second compaction is what
    // closes the gaps the merging left behind.
    static int[] slideRowLeft(int[] row) {
        int[] packed = new int[SIZE];
        int next = 0;
        for (int i = 0; i < SIZE; i++) {
            if (row[i] != 0) {
                packed[next] = row[i];
                next++;
            }
        }

        // Each tile may merge at most once per move. The test for 0 is what
        // enforces that: a merged partner is set to 0, so the next step
        // cannot pick it up again. The extra i++ only skips a comparison
        // that could never match anyway -- measured over every row of
        // {0,2,4,8,16}, leaving it out changes nothing.
        for (int i = 0; i < SIZE - 1; i++) {
            if (packed[i] != 0 && packed[i] == packed[i + 1]) {
                packed[i] = packed[i] * 2;
                packed[i + 1] = 0;
                i++;
            }
        }

        int[] result = new int[SIZE];
        next = 0;
        for (int i = 0; i < SIZE; i++) {
            if (packed[i] != 0) {
                result[next] = packed[i];
                next++;
            }
        }

        return result;
    }

    // The square at (row, col) ends up at (col, SIZE - 1 - row).
    static void rotateClockwise(int[][] board) {
        int[][] turned = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                turned[col][SIZE - 1 - row] = board[row][col];
            }
        }
        for (int row = 0; row < SIZE; row++) {
            board[row] = turned[row];
        }
    }

    // --- The board ---

    // Nine in ten new tiles are a 2, the rest a 4.
    static void spawn(int[][] board) {
        while (true) {
            int row = (int) (Math.random() * SIZE);
            int col = (int) (Math.random() * SIZE);
            if (board[row][col] == 0) {
                board[row][col] = Math.random() < 0.9 ? 2 : 4;
                return;
            }
        }
    }

    static boolean isFull(int[][] board) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    static int[][] copy(int[][] board) {
        int[][] result = new int[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                result[row][col] = board[row][col];
            }
        }
        return result;
    }

    static boolean same(int[][] a, int[][] b) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (a[row][col] != b[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    // %5d gives every number the same width, so the columns line up however
    // large the tiles get.
    static void print(int[][] board) {
        System.out.println();
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    System.out.printf("%5s", ".");
                } else {
                    System.out.printf("%5d", board[row][col]);
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
