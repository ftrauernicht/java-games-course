// Course 3 - the red challenge, in full: the three pegs as actual state.
// Read this only after you have tried it yourself.
//
// The green version prints instructions and nothing more. This one keeps the
// pegs in arrays, carries out every move, draws the result, and refuses to
// put a larger disc on a smaller one. If the recursion is right, that refusal
// never fires -- which is the point of putting it in.
//
// Compile with:  javac TowersOfHanoiTowers.java
// Run with:      java TowersOfHanoiTowers
public class TowersOfHanoiTowers {

    static final int PEGS = 3;
    static final int DISCS = 3;

    // towers[peg][0] is the bottom disc. heights[peg] is how many are on it.
    static final int[][] TOWERS = new int[PEGS][DISCS];
    static final int[] HEIGHTS = new int[PEGS];

    public static void main(String[] args) {
        // Largest disc at the bottom of peg A, smallest on top.
        for (int i = 0; i < DISCS; i++) {
            TOWERS[0][i] = DISCS - i;
        }
        HEIGHTS[0] = DISCS;

        System.out.println("Start:");
        draw();

        move(DISCS, 0, 2, 1);

        System.out.println("Done in " + moveCount + " moves.");
    }

    static int moveCount = 0;

    static void move(int n, int from, int to, int via) {
        if (n == 0) {
            return;
        }
        move(n - 1, from, via, to);
        carry(from, to);
        move(n - 1, via, to, from);
    }

    // Takes the top disc off one peg and puts it on another, complaining
    // loudly if that would break the rule.
    static void carry(int from, int to) {
        int disc = TOWERS[from][HEIGHTS[from] - 1];

        if (HEIGHTS[to] > 0 && TOWERS[to][HEIGHTS[to] - 1] < disc) {
            System.out.println("ILLEGAL: disc " + disc + " onto a smaller one.");
            return;
        }

        HEIGHTS[from]--;
        TOWERS[to][HEIGHTS[to]] = disc;
        HEIGHTS[to]++;
        moveCount++;

        System.out.println("Move disc " + disc + " from "
                + (char) ('A' + from) + " to " + (char) ('A' + to));
        draw();
    }

    // Draws the three pegs side by side, widest disc at the bottom.
    static void draw() {
        for (int level = DISCS - 1; level >= 0; level--) {
            for (int peg = 0; peg < PEGS; peg++) {
                if (level < HEIGHTS[peg]) {
                    System.out.printf("  %-" + DISCS + "s", bar(TOWERS[peg][level]));
                } else {
                    System.out.printf("  %-" + DISCS + "s", "|");
                }
            }
            System.out.println();
        }
        System.out.println("  A    B    C");
        System.out.println();
    }

    static String bar(int size) {
        String bar = "";
        for (int i = 0; i < size; i++) {
            bar = bar + "#";
        }
        return bar;
    }
}
