// Course 6 - Snake, the window and the wiring.
//
// Everything that sets the game up lives here rather than in the panel's
// constructor. That keeps the whole setup readable in one place, and it
// keeps the panel from handing itself out before it is fully built.
//
// Compile with:  javac *.java
// Run with:      java Snake
//
// Not `java Snake.java`: the single-file launcher compiles only the file it
// is given and would not find SnakePanel.
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Snake {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Snake::start);
    }

    static void start() {
        SnakePanel panel = new SnakePanel();
        panel.setPreferredSize(new Dimension(
                SnakePanel.COLS * SnakePanel.CELL,
                SnakePanel.ROWS * SnakePanel.CELL));
        panel.setFocusable(true);

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                panel.steer(event.getKeyCode());
            }
        });

        JFrame frame = new JFrame("Snake");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        // After setVisible, or the panel never gets the keyboard.
        panel.requestFocusInWindow();

        ActionListener step = event -> {
            panel.tick();
            panel.repaint();
        };
        new Timer(120, step).start();
    }
}
