// Course 1, Chapter 0 - what String[] args is for.
// Run with:  java Greet Frank
// Run with no name to see the length check do its job.
public class Greet {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Hello, stranger!");
        } else {
            System.out.println("Hello, " + args[0] + "!");
        }
    }
}
