// Course 7 - Flappy Bird, the window and the wiring.
//
// As in Course 6, everything that sets the game up lives here rather than in
// the panel's constructor.
//
// Compile with:  javac *.java
// Run with:      java FlappyBird
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class FlappyBird {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FlappyBird::start);
    }

    static void start() {
        FlappyPanel panel = new FlappyPanel();
        panel.setPreferredSize(new Dimension(FlappyPanel.WIDTH, FlappyPanel.HEIGHT));
        panel.setFocusable(true);

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_SPACE) {
                    panel.flap();
                }
            }
        });

        JFrame frame = new JFrame("Flappy Bird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        panel.requestFocusInWindow();

        ActionListener step = event -> {
            panel.tick();
            panel.repaint();
        };
        new Timer(16, step).start();
    }
}
