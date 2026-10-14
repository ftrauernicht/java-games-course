// Course 5 - 2048.
// A finished version of the green path.
//
// The class cannot be called 2048: a Java name may not start with a digit.
//
// The board is shown in a window, the move is still typed into the console
// and confirmed with Enter. That split is deliberate, and it is the whole
// argument for Course 6: a window can show anything, but reading a single
// key press the moment it happens needs more than this.
//
// Compile with:  javac Game2048.java
// Run with:      java Game2048
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Game2048 {

    static final int SIZE = 4;

    static final int TILE = 90;
    static final int GAP = 8;
    static final int BOARD = SIZE * TILE + (SIZE + 1) * GAP;

    static final Color BACKDROP = new Color(187, 173, 160);
    static final Color EMPTY = new Color(205, 193, 180);
    static final Color DARK_TEXT = new Color(119, 110, 101);

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int[][] board = new int[SIZE][SIZE];

        BufferedImage picture = new BufferedImage(BOARD, BOARD, BufferedImage.TYPE_INT_RGB);
        JLabel label = new JLabel(new ImageIcon(picture));
        JFrame window = new JFrame("2048");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(label);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        spawn(board);
        spawn(board);

        while (true) {
            draw(picture, board);
            label.repaint();

            System.out.print("Move (w/a/s/d, q to quit): ");
            String line = in.nextLine().trim().toLowerCase();

            if ("q".equals(line)) {
                System.out.println("Bye.");
                System.exit(0);
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
                draw(picture, board);
                label.repaint();
                System.out.println("Board full. Game over.");
                System.exit(0);
            }
        }
    }

    // --- Drawing ---

    // Paints the whole board into the picture. Nothing reaches the screen
    // until repaint() asks the window to look at the picture again.
    static void draw(BufferedImage picture, int[][] board) {
        Graphics g = picture.createGraphics();

        g.setColor(BACKDROP);
        g.fillRect(0, 0, BOARD, BOARD);

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                int x = GAP + col * (TILE + GAP);
                int y = GAP + row * (TILE + GAP);
                int value = board[row][col];

                g.setColor(value == 0 ? EMPTY : tileColor(value));
                g.fillRect(x, y, TILE, TILE);

                if (value != 0) {
                    drawNumber(g, value, x, y);
                }
            }
        }

        // Every createGraphics hands out a resource. Giving it back costs one
        // line and matters once draw runs a few hundred times.
        g.dispose();
    }

    // Centres the number on its tile. The font shrinks as the number grows,
    // otherwise 1024 runs off the edge.
    static void drawNumber(Graphics g, int value, int x, int y) {
        String text = String.valueOf(value);

        int size = switch (text.length()) {
            case 1, 2 -> 38;
            case 3 -> 32;
            default -> 26;
        };

        g.setFont(new Font("SansSerif", Font.BOLD, size));
        g.setColor(value <= 4 ? DARK_TEXT : Color.WHITE);

        FontMetrics metrics = g.getFontMetrics();
        int textX = x + (TILE - metrics.stringWidth(text)) / 2;
        int textY = y + (TILE + metrics.getAscent() - metrics.getDescent()) / 2;
        g.drawString(text, textX, textY);
    }

    // Pale for the small tiles, hot for the large ones, so that progress is
    // visible without reading a single number.
    static Color tileColor(int value) {
        return switch (value) {
            case 2 -> new Color(238, 228, 218);
            case 4 -> new Color(237, 224, 200);
            case 8 -> new Color(242, 177, 121);
            case 16 -> new Color(245, 149, 99);
            case 32 -> new Color(246, 124, 95);
            case 64 -> new Color(246, 94, 59);
            case 128 -> new Color(237, 207, 114);
            case 256 -> new Color(237, 204, 97);
            case 512 -> new Color(237, 200, 80);
            case 1024 -> new Color(237, 197, 63);
            default -> new Color(237, 194, 46);
        };
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
}
