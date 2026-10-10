// Course 1, Chapter 1 - a finished version of everything the chapter builds.
// Each section below matches a section of the chapter, in the same order.
//
// Compile with:  javac Basics.java
// Run with:      java Basics
public class Basics {
    public static void main(String[] args) {

        // --- Printing ---
        System.out.println("A line of its own.");
        System.out.print("No line break ");
        System.out.println("until here.");
        System.out.printf("%s is %d years old.%n", "Ada", 36);

        // Java has no string interpolation. You join values with + ...
        String name = "Ada";
        System.out.println("Hello, " + name + "!");
        // ... or you use printf and say where each value goes.

        // --- Types ---
        int whole = 42;
        double fraction = 2.5;
        boolean yes = true;
        char letter = 'J';
        String word = "Java";
        System.out.println(whole + " " + fraction + " " + yes + " " + letter + " " + word);

        // The type is fixed once it is declared. Uncommenting the next line
        // stops the program from compiling at all, which is the point.
        // whole = "forty-two";

        // --- Integer division and the remainder ---
        System.out.println(7 / 2);    // 3, not 3.5: both sides are whole numbers
        System.out.println(7 % 2);    // 1, what is left over
        System.out.println(7 / 2.0);  // 3.5, because one side is a double

        // --- Casting, and a random number ---
        double exact = 3.9;
        int cut = (int) exact;        // 3: the digits after the point are dropped
        System.out.println(cut);

        int roll = (int) (Math.random() * 6) + 1;
        System.out.println("You rolled a " + roll + ".");

        // --- Operators ---
        int a = 10;
        int b = 3;
        System.out.println(a + b);
        System.out.println(a - b);
        System.out.println(a * b);
        a += 5;                        // same as a = a + 5
        System.out.println(a);
        System.out.println(a > b);
        System.out.println(a > b && b > 0);
        System.out.println(a > b || b > 100);
        System.out.println(!(a > b));
        String verdict = a > b ? "bigger" : "smaller";
        System.out.println(verdict);

        // --- Comparing text: == is not the right tool ---
        String typed = "yes";
        String expected = "yes";
        System.out.println(typed.equals(expected));  // true, and this is the one to use

        // == asks "is this the same object", not "is this the same text". For
        // text you typed into the source it can still say true, which is what
        // makes it such a convincing trap. Use .equals for text. Always.
        String built = "ye" + buildS();
        System.out.println(built == expected);       // false
        System.out.println(built.equals(expected));  // true

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

        // --- Repeating until something changes ---
        int countdown = 3;
        while (countdown > 0) {
            System.out.println(countdown);
            countdown--;
        }
        System.out.println("Go!");
    }

    // Only here so the string above is built while the program runs rather
    // than written out in the source. That difference is what == notices.
    private static String buildS() {
        return "s";
    }
}
