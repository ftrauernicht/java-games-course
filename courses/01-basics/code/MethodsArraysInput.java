// Course 1, Chapter 2 - a finished version of the green path.
// Each section below matches a section of the chapter, in the same order.
// The yellow section is extra practice rather than part of the path, so it
// is not repeated here.
//
// Compile with:  javac MethodsArraysInput.java
// Run with:      java MethodsArraysInput
import java.util.Scanner;

public class MethodsArraysInput {

    public static void main(String[] args) {

        // --- Your own methods ---
        greet("Frank");
        greet("Ada");

        // --- Methods that hand something back ---
        System.out.println(add(2, 3));
        System.out.println(larger(9, 4));
        System.out.println(isEven(10));   // the "try it yourself" in that section

        // --- Arrays, many values under one name ---
        int[] scores = new int[3];
        scores[0] = 10;
        scores[1] = 20;
        scores[2] = 30;

        int[] primes = {2, 3, 5, 7, 11};
        System.out.println(primes.length);
        System.out.println(primes[0]);
        System.out.println(primes[4]);

        // Asking for a position that does not exist stops the program. Try it:
        // System.out.println(primes[5]);

        // --- Walking an array ---
        for (int i = 0; i < primes.length; i++) {
            System.out.print(primes[i] + " ");
        }
        System.out.println();

        int total = 0;
        for (int prime : primes) {
            total += prime;
        }
        System.out.println("Sum: " + total);

        // --- Arrays are references, single values are not ---
        int number = 1;
        tryToChange(number);
        System.out.println(number);        // still 1

        int[] box = {1};
        changeFirst(box);
        System.out.println(box[0]);        // 99

        // That difference is why every later course can hand its board to a
        // method and let the method change it.

        // --- Reading what someone types ---
        // One Scanner for the whole program, created here and passed on to
        // any method that needs it. It is never closed: closing a Scanner on
        // System.in closes System.in for good, and nothing can reopen it.
        Scanner in = new Scanner(System.in);

        System.out.print("What is your name? ");
        String typedName = in.nextLine();
        System.out.println("Hello, " + typedName + "!");

        int age = askForNumber(in, "How old are you? ");
        System.out.println("In ten years you will be " + (age + 10) + ".");
    }

    // Takes something, hands nothing back. That is what void means.
    static void greet(String who) {
        System.out.println("Hello, " + who + "!");
    }

    // Takes two numbers, hands one back.
    static int add(int first, int second) {
        return first + second;
    }

    static int larger(int first, int second) {
        if (first > second) {
            return first;
        }
        return second;
    }

    static boolean isEven(int number) {
        return number % 2 == 0;
    }

    static void tryToChange(int value) {
        value = 99;
    }

    static void changeFirst(int[] values) {
        values[0] = 99;
    }

    // Keeps asking until the answer is a number. Reading with nextLine and
    // converting afterwards is the only reading pattern this course uses:
    // nextInt leaves the line break behind and breaks the next nextLine.
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

    // Checked before parsing, so Integer.parseInt is only ever handed
    // something it can read. Digits only, which is all any game here asks
    // for; a minus sign would need one more case.
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
