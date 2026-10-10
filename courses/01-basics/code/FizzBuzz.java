// Course 1, Chapter 1 - the red challenge, in full.
// Read this only after you have tried it yourself.
//
// Compile with:  javac FizzBuzz.java
// Run with:      java FizzBuzz
public class FizzBuzz {
    public static void main(String[] args) {
        for (int i = 1; i <= 15; i++) {
            // Both tests first. Checking divisible-by-3 before divisible-by-15
            // would print "Fizz" for 15 and never reach the combined case.
            if (i % 3 == 0 && i % 5 == 0) {
                System.out.println("FizzBuzz");
            } else if (i % 3 == 0) {
                System.out.println("Fizz");
            } else if (i % 5 == 0) {
                System.out.println("Buzz");
            } else {
                System.out.println(i);
            }
        }
    }
}
