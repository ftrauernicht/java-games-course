// Course 6 - Snake, the game itself.
//
// The class is final out of caution. The constructor calls setBackground,
// an inherited method, and the compiler warns about handing `this` out
// before a subclass is finished -- but only when the class is non-final AND
// the constructor is reachable from outside the package. This one is not,
// so removing final alone changes nothing; final keeps it that way for the
// day somebody makes the constructor public.
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;

public final class SnakePanel extends JPanel {

    static final int COLS = 20;
    static final int ROWS = 20;
    static final int CELL = 20;

    // The snake as two parallel arrays plus a length. Position 0 is the
    // head. No list needed: the body can never be longer than the board.
    private final int[] x = new int[COLS * ROWS];
    private final int[] y = new int[COLS * ROWS];
    private int length = 3;

    // One step per tick, in cells.
    private int dirX = 1;
    private int dirY = 0;

    private int foodX;
    private int foodY;
    private int score;
    private boolean gameOver;

    SnakePanel() {
        setBackground(Color.BLACK);
        for (int i = 0; i < length; i++) {
            x[i] = 3 - i;
            y[i] = 3;
        }
        spawnFood();
    }

    // --- One step of the game ---

    void tick() {
        if (gameOver) {
            return;
        }

        // Move the body from the tail forwards, so each segment takes the
        // place of the one ahead of it. Going the other way would copy the
        // head into every segment.
        for (int i = length - 1; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        x[0] = x[0] + dirX;
        y[0] = y[0] + dirY;

        if (x[0] < 0 || x[0] >= COLS || y[0] < 0 || y[0] >= ROWS) {
            gameOver = true;
            return;
        }

        for (int i = 1; i < length; i++) {
            if (x[0] == x[i] && y[0] == y[i]) {
                gameOver = true;
                return;
            }
        }

        if (x[0] == foodX && y[0] == foodY) {
            // The new segment starts where the tail is now, so the tail
            // stays put for one tick and the snake grows from behind.
            // Without these two lines it would sit at (0, 0) for one tick
            // -- a green block flashing in the corner -- because a fresh
            // int[] is full of zeroes.
            x[length] = x[length - 1];
            y[length] = y[length - 1];

            length++;
            score++;
            spawnFood();
        }
    }

    // Never on the snake, so keep trying until the cell is free.
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

    // --- Keys ---

    // Turning straight back on itself would run the head into the neck, so
    // a reversal is ignored.
    //
    // This check compares against the direction as it is right now, not
    // against the one the last tick actually used. Two key presses between
    // two ticks can therefore still reverse the snake. Fixing that is the
    // red challenge in the chapter.
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

    // --- Drawing ---

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
}
