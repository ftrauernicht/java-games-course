// Course 7 - Flappy Bird, the game itself.
//
// final, like the panel in Course 6 and for the same reason: a public panel
// that can be extended makes the compiler warn about handing `this` out
// before the object is finished, and this repository builds with warnings
// treated as errors.
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JPanel;

public final class FlappyPanel extends JPanel {

    static final int WIDTH = 400;
    static final int HEIGHT = 600;

    static final int GAP = 150;        // the hole between upper and lower pipe
    static final int MARGIN = 60;      // keeps the gap away from the edges
    static final int PIPE_WIDTH = 60;
    static final int PIPE_SPACING = 200;
    static final int PIPE_SPEED = 3;

    static final int BIRD_X = 100;
    static final int BIRD_SIZE = 24;
    static final double GRAVITY = 0.5;
    static final double FLAP = -8.0;

    // Both double, so that `birdY += velocity` does not quietly lose the
    // fraction. Mixing an int and a double here is a compiler warning under
    // the flags this repository builds with, and a jerky bird besides.
    private double birdY = HEIGHT / 2.0;
    private double velocity;

    private final Pipe[] pipes = new Pipe[3];
    private int score;
    private boolean gameOver;
    private boolean started;

    FlappyPanel() {
        setBackground(new Color(120, 190, 230));
        reset();
    }

    void reset() {
        birdY = HEIGHT / 2.0;
        velocity = 0;
        score = 0;
        gameOver = false;
        started = false;
        for (int i = 0; i < pipes.length; i++) {
            pipes[i] = new Pipe(WIDTH + i * PIPE_SPACING, 0);
            pipes[i].recycle(WIDTH + i * PIPE_SPACING);
        }
    }

    // --- One step of the game ---

    void tick() {
        if (gameOver || !started) {
            return;
        }

        velocity = velocity + GRAVITY;
        birdY = birdY + velocity;

        for (Pipe pipe : pipes) {
            pipe.x = pipe.x - PIPE_SPEED;

            if (pipe.x + PIPE_WIDTH < 0) {
                pipe.recycle(pipe.x + pipes.length * PIPE_SPACING);
            }

            // Scored once the bird is past the right edge of the pipe.
            if (!pipe.passed && pipe.x + PIPE_WIDTH < BIRD_X) {
                pipe.passed = true;
                score++;
            }
        }

        if (collides()) {
            gameOver = true;
        }
    }

    // The bird is out if it leaves the screen, or if its box overlaps either
    // half of a pipe. Rectangle.intersects does the four comparisons that an
    // overlap test needs, in one call.
    private boolean collides() {
        if (birdY < 0 || birdY + BIRD_SIZE > HEIGHT) {
            return true;
        }

        Rectangle bird = new Rectangle(BIRD_X, (int) birdY, BIRD_SIZE, BIRD_SIZE);

        for (Pipe pipe : pipes) {
            Rectangle upper = new Rectangle(pipe.x, 0, PIPE_WIDTH, pipe.gapY);
            Rectangle lower = new Rectangle(pipe.x, pipe.gapY + GAP,
                    PIPE_WIDTH, HEIGHT - pipe.gapY - GAP);

            if (bird.intersects(upper) || bird.intersects(lower)) {
                return true;
            }
        }

        return false;
    }

    // --- Keys ---

    void flap() {
        if (gameOver) {
            reset();
            return;
        }
        started = true;
        velocity = FLAP;
    }

    // --- Drawing ---

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(60, 160, 70));
        for (Pipe pipe : pipes) {
            g.fillRect(pipe.x, 0, PIPE_WIDTH, pipe.gapY);
            g.fillRect(pipe.x, pipe.gapY + GAP, PIPE_WIDTH,
                    HEIGHT - pipe.gapY - GAP);
        }

        g.setColor(new Color(240, 200, 60));
        g.fillOval(BIRD_X, (int) birdY, BIRD_SIZE, BIRD_SIZE);

        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 24));
        g.drawString(String.valueOf(score), WIDTH / 2 - 8, 40);

        g.setFont(new Font("SansSerif", Font.PLAIN, 14));
        if (!started && !gameOver) {
            g.drawString("Press space to start", WIDTH / 2 - 65, HEIGHT / 2 + 60);
        }

        if (gameOver) {
            g.setFont(new Font("SansSerif", Font.BOLD, 28));
            g.drawString("Game over", WIDTH / 2 - 70, HEIGHT / 2);
            g.setFont(new Font("SansSerif", Font.PLAIN, 14));
            g.drawString("Press space to play again", WIDTH / 2 - 80, HEIGHT / 2 + 30);
        }
    }
}
