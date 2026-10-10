🇬🇧 English | 🇩🇪 [Deutsch](../de/00-einfuehrung.md)

[← Back to course overview](../../../README.md) · Next: [Chapter 1 – Java Basics](01-java-basics.md) →

# Chapter 0 – Introduction & Tools

**Goal:** by the end of this chapter you will have Java installed, you will have
written a program, compiled it and run it, and you will know what every word in
that program is doing there. You will also have made your first compiler error on
purpose, so the first accidental one is not a shock.

A finished version of both programs in this chapter lives in
[`code/Hello.java`](../code/Hello.java) and [`code/Greet.java`](../code/Greet.java).

## What Java is, and why there is an extra step

Some languages run your file directly. Java puts one step in between: a program
called the **compiler** reads what you wrote, checks it, and translates it into an
intermediate form called bytecode. A second program, the **virtual machine**, then
runs that bytecode.

The extra step is the reason Java catches a whole class of mistakes before your
program ever starts. If you misspell a name or hand a method the wrong kind of
value, you find out at compile time with a full sentence explaining it, rather than
halfway through a running game.

Both programs come in one download, the **JDK**.

## Installing the JDK

Go to [adoptium.net](https://adoptium.net/) and download the Temurin JDK, version
21 or newer, for your operating system. Run the installer and accept what it
suggests.

Then open a terminal. On Windows that is Windows Terminal or PowerShell, on macOS
and Linux it is Terminal. Type this and press Enter:

```
java -version
```

You should see three lines naming a version number. Now the other half:

```
javac -version
```

That should answer with `javac 21` and the rest of the version. If `java` works but
`javac` says "not recognised" or "command not found", you have a runtime but not a
full JDK, or the installer did not add it to your PATH. Reinstall from the link
above and let the installer set the PATH, which it offers by default.

## Installing the editor

Download **IntelliJ IDEA Community Edition** from
[jetbrains.com](https://www.jetbrains.com/idea/download/). The Community Edition is
free and does everything this course needs. The download page offers Ultimate
first, with Community further down the same page.

When you create a project, IntelliJ asks for a **build system**. Choose
**IntelliJ**, not Maven and not Gradle. A build system is a tool for managing other
people's code, and this course uses none, so adding one would only put a layer
between you and the compiler.

The install takes a while. A good moment to get a coffee.

## Your first program

Create a file called `Hello.java` and type this into it. Type it, do not paste it.

```java
public class Hello {
    public static void main(String[] args) {
        System.out.println("Hello!");
    }
}
```

Now open a terminal in the folder that file is in, and run two commands:

```
javac Hello.java
java Hello
```

```
Hello!
```

That is it. You just wrote, compiled and ran a program.

Look at the folder: there is a new file, `Hello.class`. That is the bytecode the
compiler produced. `javac` makes it, `java` runs it. Notice that the second command
has no `.java` and no `.class` on the end. You are naming the class, not the file.

## What all those words are doing there

That is a lot of ceremony for one line of output, and Java does make you write it.
Here is what each piece means.

**`public class Hello`** names a box, and every piece of Java code lives inside one.
For now that is all a class is: a box with a name. Courses 6 and 7 use the box for
what it is really for, holding data together with the things you can do to it.
Until then it is scenery.

**`main`** is the one method the virtual machine looks for when you say
`java Hello`. No `main`, nothing runs. The name is not a choice, it is an
appointment.

**`public static void`** states three facts about `main`. `public` means anything
may call it, including the virtual machine. `static` means it belongs to the class
itself and can be called without anyone creating anything first, which matters
because at the moment the program starts, nothing exists yet. `void` means it hands
nothing back when it is finished.

**`String[] args`** is a list of the words someone typed after the program name.
Most of the time it sits there unused, so it is easy to write off as more scenery.
It is not. Make a second file, `Greet.java`:

```java
public class Greet {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Hello, stranger!");
        } else {
            System.out.println("Hello, " + args[0] + "!");
        }
    }
}
```

```
javac Greet.java
java Greet Frank
```

```
Hello, Frank!
```

Run it again without the name and it says `Hello, stranger!`. The words after
`java Greet` arrived in your program. You will meet `args.length` and `args[0]`
properly in Chapter 2. For now it is enough to see that the list is real.

## The filename rule

`public class Hello` has to live in a file called `Hello.java`. Not `hello.java`,
not `Hello2.java`. Exactly the class name, with the same capital letters, plus
`.java`.

This is the most common beginner error in Java, and it is worth meeting on purpose.
Rename `Hello.java` to `hello.java` and compile it again:

```
javac hello.java
```

```
hello.java:1: error: class Hello is public, should be declared in a file named Hello.java
public class Hello {
       ^
1 error
```

The message says exactly what is wrong and what to call the file. Rename it back.

On Windows there is a twist worth knowing: the filesystem does not care about
capital letters, but the compiler does. A file that looks right in the folder can
still produce this message. If you ever see it and the name looks correct, check
the capital letters one by one.

## Compiler errors are a feature, not a monster

Make another mistake on purpose. In `Hello.java`, delete the semicolon at the end
of the `System.out.println` line and compile again:

```
Hello.java:3: error: ';' expected
        System.out.println("Hello!")
                                    ^
1 error
```

File, line number, what was expected, and an arrow pointing at the spot. Most Java
error messages are built like this. Read them instead of guessing, then put the
semicolon back.

One thing about reading them: when you get a wall of twenty errors, fix the
**first** one and compile again. A single missing brace confuses the compiler about
everything that follows, and nineteen of those twenty will vanish on their own.

## One shortcut, and why this course skips it

You may read elsewhere that you can run a single file directly:

```
java Hello.java
```

That works. It compiles in the background and leaves no `.class` file behind, which
is genuinely convenient for one file.

This course uses `javac` and `java` as two separate steps anyway. The compile step
is a real thing that happens, and seeing it is worth more early on than saving one
command. Courses 6 and 7 are also built from several files at once, and there the
shortcut stops working: it compiles only the file you hand it and then cannot find
the others.

## How this course is structured

From here on, every section in every chapter carries one of three markers.

🟢 **Core.** The path through the chapter. You need all of it, and what follows
assumes you did it.

🟡 **Optional.** More practice on the idea you just met, or a small extra feature.
Skipping it costs you nothing later.

🔴 **Optional, genuine challenge.** A real problem, usually the hardest thing in the
chapter. It is there because struggling with one of these teaches more than three
more green sections would. Skipping it also costs you nothing later.

Every course after this one is a game, and they can be done in any order. All of
them assume this course and nothing else. Where a game needs something this course
did not cover, that chapter explains it again, with a note telling you to skip
ahead if you have already seen it.

## Ready?

You have a compiler, an editor, and a program that runs. Chapter 1 is the language
itself: what you can store, what you can do with it, and how to make a program
decide and repeat.

**Next:** [Chapter 1 – Java Basics](01-java-basics.md)
