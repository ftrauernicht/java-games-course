// Course 1, Chapter 1 - a finished version of the green path.
// Each section below matches a section of the chapter, in the same order.
// The yellow section is extra practice rather than part of the path, so it
// is not repeated here.
//
// Compile with:  javac Basics.java
// Run with:      java Basics
public class Basics {
    public static void main(String[] args) {

        // --- Putting something on the screen ---
        System.out.println("A line of its own.");
        System.out.print("No line break ");
        System.out.println("until here.");

        // Java has no string interpolation. You join values with + ...
        String name = "Ada";
        System.out.println("Hello, " + name + "!");

        // ... or you use printf and say where each value goes.
        System.out.printf("%s is %d years old.%n", "Ada", 36);

        // --- What you can store ---
        int whole = 42;
        double fraction = 2.5;
        boolean yes = true;
        char letter = 'J';
        String word = "Java";
        System.out.println(whole + " " + fraction + " " + yes + " " + letter + " " + word);

        // The type is fixed once it is declared. Uncommenting the next line
        // stops the program from compiling at all, which is the point.
        // whole = "forty-two";

        // --- Arithmetic, and one surprise ---
        System.out.println(7 + 2);
        System.out.println(7 - 2);
        System.out.println(7 * 2);
        System.out.println(7 / 2);    // 3, not 3.5: both sides are whole numbers
        System.out.println(7 % 2);    // 1, what is left over
        System.out.println(7 / 2.0);  // 3.5, because one side is a double

        // --- Casting, and your first random number ---
        double exact = 3.9;
        int cut = (int) exact;        // 3: the digits after the point are dropped
        System.out.println(cut);

        int roll = (int) (Math.random() * 6) + 1;
        System.out.println("You rolled a " + roll + ".");

        // --- Comparing, combining, and a shortcut ---
        int a = 10;
        int b = 3;
        System.out.println(a > b);
        System.out.println(a == b);
        System.out.println(a != b);

        System.out.println(a > b && b > 0);
        System.out.println(a > b || b > 100);
        System.out.println(!(a > b));

        a += 5;                       // same as a = a + 5
        System.out.println(a);

        String verdict = a > b ? "bigger" : "smaller";
        System.out.println(verdict);

        // --- Comparing text, which is not what you expect ---
        String typed = "yes";
        String expected = "yes";
        System.out.println(typed.equals(expected));  // true, and this is the one to use

        // A String variable can also hold nothing at all, which Java calls
        // null. Asking null anything stops the program, so put the value you
        // know first.
        String missing = null;
        System.out.println("yes".equals(missing));   // false, and no crash
        // System.out.println(missing.equals("yes"));  // NullPointerException

        // --- Making a decision ---
        int score = 72;
        if (score >= 90) {
            System.out.println("Excellent.");
        } else if (score >= 50) {
            System.out.println("Passed.");
        } else {
            System.out.println("Not this time.");
        }

        // --- Choosing between many fixed values ---
        String command = "w";
        switch (command) {
            case "w" -> System.out.println("up");
            case "s" -> System.out.println("down");
            case "a" -> System.out.println("left");
            case "d" -> System.out.println("right");
            default -> System.out.println("unknown command");
        }

        // --- Repeating a fixed number of times ---
        for (int i = 1; i <= 5; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (int i = 0; i < 5; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        // --- Repeating until something changes ---
        int countdown = 3;
        while (countdown > 0) {
            System.out.println(countdown);
            countdown--;
        }
        System.out.println("Go!");

        // continue skips the rest of a pass, break leaves the loop entirely.
        for (int i = 1; i <= 10; i++) {
            if (i % 2 == 0) {
                continue;
            }
            if (i > 7) {
                break;
            }
            System.out.print(i + " ");
        }
        System.out.println();
    }
}
