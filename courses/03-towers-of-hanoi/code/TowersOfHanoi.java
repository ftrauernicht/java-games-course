// Course 3 - Towers of Hanoi.
// A finished version of the green path. Reads nothing, ends by itself.
//
// Compile with:  javac TowersOfHanoi.java
// Run with:      java TowersOfHanoi
public class TowersOfHanoi {

    public static void main(String[] args) {
        int discs = 3;
        System.out.println("Towers of Hanoi with " + discs + " discs.");
        System.out.println();
        move(discs, 'A', 'C', 'B');
    }

    // Moves n discs from peg "from" to peg "to", using "via" as the spare.
    //
    // The whole method is three steps, and the first and third are the same
    // problem one disc smaller. That is what makes it recursion: it solves a
    // smaller version of itself and trusts the answer.
    static void move(int n, char from, char to, char via) {
        if (n == 0) {
            return;
        }

        move(n - 1, from, via, to);
        System.out.println("Move disc " + n + " from " + from + " to " + to);
        move(n - 1, via, to, from);
    }
}
