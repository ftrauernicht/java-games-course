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

    // There is only one way to place a pipe, so the constructor uses it too.
    // Calling an own method from a constructor is safe here because the class
    // is final: nobody can override recycle underneath us.
    Pipe(int x) {
        recycle(x);
    }

    // Puts this pipe at x with a fresh gap, ready to come round again.
    void recycle(int x) {
        this.x = x;
        this.gapY = FlappyPanel.MARGIN
                + (int) (Math.random() * (FlappyPanel.HEIGHT
                        - FlappyPanel.GAP - 2 * FlappyPanel.MARGIN));
        this.passed = false;
    }
}
