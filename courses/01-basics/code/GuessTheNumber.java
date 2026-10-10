// Course 1, Chapter 2 - the red challenge, in full.
// Read this only after you have tried it yourself.
//
// Compile with:  javac GuessTheNumber.java
// Run with:      java GuessTheNumber
import java.util.Scanner;

public class GuessTheNumber {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        int secret = (int) (Math.random() * 100) + 1;
        int tries = 0;

        System.out.println("I am thinking of a number between 1 and 100.");

        while (true) {
            int guess = askForNumber(in, "Your guess: ");
            tries++;

            if (guess < secret) {
                System.out.println("Higher.");
            } else if (guess > secret) {
                System.out.println("Lower.");
            } else {
                System.out.println("That's it. You needed " + tries + " tries.");
                break;
            }
        }
    }

    static int askForNumber(Scanner in, String question) {
        while (true) {
            System.out.print(question);
            String line = in.nextLine().trim();
            if (isWholeNumber(line)) {
                return Integer.parseInt(line);
            }
            System.out.println("That was not a whole number. Try again.");
        }
    }

    static boolean isWholeNumber(String text) {
        if (text.isEmpty()) {
            return false;
        }
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
