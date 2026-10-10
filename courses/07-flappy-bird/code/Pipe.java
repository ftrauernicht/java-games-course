// Course 7 - one pair of pipes.
//
// A class that holds data and almost nothing else. Three pipes exist for the
// whole game; when one leaves on the left it is moved back to the right with
// a new gap, rather than being thrown away and a new one made.
//
// It lives in its own file because a second class sharing FlappyPanel.java
// would be hidden there, and the compiler warns about exactly that.
public final class Pipe {

    int x;
    int gapY;
    boolean passed;

    Pipe(int x, int gapY) {
        this.x = x;
        this.gapY = gapY;
    }

    // Puts this pipe back on the right with a fresh gap, ready to come round
    // again.
    void recycle(int x) {
        this.x = x;
        this.gapY = FlappyPanel.MARGIN
                + (int) (Math.random() * (FlappyPanel.HEIGHT
                        - FlappyPanel.GAP - 2 * FlappyPanel.MARGIN));
        this.passed = false;
    }
}
