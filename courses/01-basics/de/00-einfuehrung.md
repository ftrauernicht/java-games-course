🇩🇪 Deutsch | 🇬🇧 [English](../en/00-introduction.md)

[← Zurück zur Kursübersicht](../../../README.de.md) · Weiter: [Kapitel 1 – Java-Grundlagen](01-java-grundlagen.md) →

# Kapitel 0 – Einführung & Werkzeuge

**Ziel:** Am Ende dieses Kapitels hast du Java installiert, ein Programm
geschrieben, übersetzt und ausgeführt, und du weißt, was jedes Wort darin zu
suchen hat. Außerdem hast du deinen ersten Compilerfehler absichtlich gebaut,
damit der erste unabsichtliche kein Schreck ist.

Eine fertige Fassung beider Programme aus diesem Kapitel liegt in
[`code/Hello.java`](../code/Hello.java) und [`code/Greet.java`](../code/Greet.java).

## Was Java ist, und warum es einen Zwischenschritt gibt

Manche Sprachen führen deine Datei direkt aus. Java schiebt einen Schritt dazwischen:
Ein Programm namens **Compiler** liest, was du geschrieben hast, prüft es und
übersetzt es in eine Zwischenform, den Bytecode. Ein zweites Programm, die
**virtuelle Maschine**, führt diesen Bytecode dann aus.

Dieser Zwischenschritt ist der Grund, warum Java eine ganze Klasse von Fehlern
findet, bevor dein Programm überhaupt startet. Wenn du einen Namen verschreibst
oder einer Methode den falschen Wert übergibst, erfährst du das beim Übersetzen,
in einem vollständigen Satz, statt mitten in einem laufenden Spiel.

Beide Programme kommen in einem Download, dem **JDK**.

## Das JDK installieren

Geh auf [adoptium.net](https://adoptium.net/) und lade das Temurin JDK in Version
21 oder neuer für dein Betriebssystem herunter. Starte das Installationsprogramm
und übernimm, was es vorschlägt.

Öffne danach ein Terminal. Unter Windows ist das Windows Terminal oder PowerShell,
unter macOS und Linux das Terminal. Tipp das hier ein und drück Enter:

```
java -version
```

Du solltest drei Zeilen mit einer Versionsnummer sehen. Jetzt die andere Hälfte:

```
javac -version
```

Darauf sollte `javac 21` und der Rest der Version kommen. Falls `java` funktioniert,
`javac` aber „nicht gefunden" meldet, hast du nur eine Laufzeitumgebung statt eines
vollständigen JDK, oder das Installationsprogramm hat den Pfad nicht eingetragen.
Installier es über den Link oben neu und lass es den Pfad setzen, was es von sich
aus anbietet.

## Den Editor installieren

Lade **IntelliJ IDEA Community Edition** von
[jetbrains.com](https://www.jetbrains.com/idea/download/). Die Community Edition ist
kostenlos und kann alles, was dieser Kurs braucht. Die Downloadseite zeigt zuerst
Ultimate an, Community steht weiter unten auf derselben Seite.

Wenn du ein Projekt anlegst, fragt IntelliJ nach einem **Build-System**. Wähl
**IntelliJ**, nicht Maven und nicht Gradle. Ein Build-System ist ein Werkzeug, um
fremden Code zu verwalten, und dieser Kurs nutzt keinen. Es würde nur eine Schicht
zwischen dich und den Compiler schieben.

Die Installation dauert. Ein guter Moment für einen Kaffee.

Zwei Dinge zum Arbeiten darin, denn der Rest dieses Kapitels benutzt ein Terminal.
IntelliJ legt deine `.java`-Dateien in einem Ordner namens `src` im Projekt ab,
und genau dort müssen die beiden Befehle unten laufen. Ein eigenes Terminalfenster
brauchst du dafür nicht: IntelliJ hat eines eingebaut, unter **Ansicht**,
**Werkzeugfenster**, **Terminal**, und es öffnet im Projektordner, `cd src` bringt
dich also den Rest des Weges.

IntelliJ kann eine Datei auch selbst übersetzen und starten, mit dem grünen Pfeil
neben `main`. Nimm ihn, sobald du dich sicher fühlst. Die Kapitel bleiben bei den
zwei Befehlen, weil sie zeigen, was der Pfeil für dich erledigt, und weil sich aus
einem Fehler, den du selbst ausgelöst hast, leichter lernen lässt als aus einem,
den ein Knopf vor dir versteckt hat.

## Dein erstes Programm

Leg eine Datei namens `Hello.java` an und tipp das hier hinein. Tippen, nicht
einfügen.

```java
public class Hello {
    public static void main(String[] args) {
        System.out.println("Hello!");
    }
}
```

Öffne jetzt ein Terminal in dem Ordner, in dem die Datei liegt, und führ zwei
Befehle aus:

```
javac Hello.java
java Hello
```

```
Hello!
```

Das war's. Du hast gerade ein Programm geschrieben, übersetzt und ausgeführt.

Schau in den Ordner: Dort liegt eine neue Datei, `Hello.class`. Das ist der
Bytecode, den der Compiler erzeugt hat. `javac` baut ihn, `java` führt ihn aus.
Achte darauf, dass am zweiten Befehl weder `.java` noch `.class` steht. Du nennst
die Klasse, nicht die Datei.

## Was all diese Wörter dort zu suchen haben

Das ist viel Zeremonie für eine Zeile Ausgabe, und Java verlangt sie tatsächlich.
Hier ist, was jedes Stück bedeutet.

**`public class Hello`** benennt eine Schachtel, und jedes Stück Java-Code liegt in
einer. Mehr ist eine Klasse vorerst nicht: eine Schachtel mit Namen. Die Kurse 6
und 7 nutzen sie für das, wofür sie eigentlich da ist, nämlich Daten zusammen mit
dem aufzubewahren, was man damit tun kann. Bis dahin ist sie Kulisse.

**`main`** ist die eine Methode, nach der die virtuelle Maschine sucht, wenn du
`java Hello` sagst. Kein `main`, kein Programmlauf. Der Name ist keine Wahl,
sondern eine Verabredung.

**`public static void`** nennt drei Tatsachen über `main`. `public` heißt, dass sie
jeder aufrufen darf, auch die virtuelle Maschine. `static` heißt, dass sie zur
Klasse selbst gehört und aufgerufen werden kann, ohne dass vorher jemand etwas
erzeugt. Das ist wichtig, denn in dem Moment, in dem das Programm startet, gibt es
noch nichts. `void` heißt, dass sie nichts zurückgibt, wenn sie fertig ist.

**`String[] args`** ist eine Liste der Wörter, die jemand nach dem Programmnamen
getippt hat. Meistens steht sie ungenutzt herum, und deshalb hält man sie leicht
für weitere Kulisse. Ist sie nicht. Leg eine zweite Datei an, `Greet.java`:

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

Führ es noch einmal ohne den Namen aus, dann sagt es `Hello, stranger!`. Die Wörter
hinter `java Greet` sind in deinem Programm angekommen. `args.length` und `args[0]`
lernst du in Kapitel 2 richtig kennen. Für den Moment reicht es zu sehen, dass die
Liste echt ist.

## Die Regel für den Dateinamen

`public class Hello` muss in einer Datei namens `Hello.java` liegen. Nicht
`hello.java`, nicht `Hello2.java`. Genau der Klassenname, mit denselben
Großbuchstaben, plus `.java`.

Das ist der häufigste Anfängerfehler in Java, und er lohnt sich absichtlich. Benenn
`Hello.java` in `hello.java` um und übersetz noch einmal:

```
javac hello.java
```

```
hello.java:1: error: class Hello is public, should be declared in a file named Hello.java
public class Hello {
       ^
1 error
```

Die Meldung sagt genau, was falsch ist und wie die Datei heißen muss. Benenn sie
zurück.

Unter Windows kommt eine Eigenheit dazu, die man kennen sollte: Das Dateisystem
unterscheidet keine Groß- und Kleinschreibung, der Compiler schon. Eine Datei, die
im Ordner richtig aussieht, kann diese Meldung trotzdem auslösen. Wenn du sie je
siehst und der Name richtig wirkt, prüf die Großbuchstaben einzeln.

## Compilerfehler sind ein Feature, kein Monster

Bau noch einen Fehler mit Absicht. Lösch in `Hello.java` das Semikolon am Ende der
`System.out.println`-Zeile und übersetz erneut:

```
Hello.java:3: error: ';' expected
        System.out.println("Hello!")
                                    ^
1 error
```

Datei, Zeilennummer, was erwartet wurde, und ein Pfeil auf die Stelle. Die meisten
Java-Fehlermeldungen sind so gebaut. Lies sie, statt zu raten, und setz das
Semikolon zurück.

Eine Sache zum Lesen dieser Meldungen: Wenn du eine Wand aus zwanzig Fehlern
bekommst, behebe den **ersten** und übersetz neu. Eine einzige fehlende Klammer
verwirrt den Compiler über alles, was danach kommt, und neunzehn der zwanzig
verschwinden von allein.

## Eine Abkürzung, und warum dieser Kurs sie auslässt

Vielleicht liest du anderswo, dass man eine einzelne Datei direkt ausführen kann:

```
java Hello.java
```

Das funktioniert. Es übersetzt im Hintergrund und lässt keine `.class`-Datei
zurück, was bei einer Datei wirklich bequem ist.

Dieser Kurs benutzt `javac` und `java` trotzdem als zwei getrennte Schritte. Der
Übersetzungsschritt passiert tatsächlich, und ihn am Anfang zu sehen ist mehr wert
als ein gesparter Befehl. Dazu kommt, dass die Kurse 6 und 7 aus mehreren Dateien
bestehen, und dort hört die Abkürzung auf zu funktionieren: Sie übersetzt nur die
Datei, die du ihr gibst, und findet die anderen dann nicht.

## Wie dieser Kurs aufgebaut ist

Ab hier trägt jeder Abschnitt in jedem Kapitel einen von drei Markern.

🟢 **Kern.** Der Weg durch das Kapitel. Den brauchst du ganz, und was danach kommt,
setzt ihn voraus.

🟡 **Optional.** Mehr Übung zu der Idee, die du gerade kennengelernt hast, oder eine
kleine Zusatzfunktion. Wer sie auslässt, hat später keinen Nachteil.

🔴 **Optional, echte Herausforderung.** Ein richtiges Problem, meist das Schwerste
im Kapitel. Es steht da, weil der Kampf mit einem davon mehr beibringt als drei
weitere grüne Abschnitte. Auch hier hat keinen Nachteil, wer es auslässt.

Jeder Kurs nach diesem ist ein Spiel, und sie lassen sich in beliebiger Reihenfolge
machen. Alle setzen diesen Kurs voraus und sonst nichts. Wo ein Spiel etwas
braucht, das hier nicht vorkam, erklärt das Kapitel es noch einmal, mit einem
Hinweis, dass du weiterspringen kannst, wenn du es schon kennst.

## Bereit?

Du hast einen Compiler, einen Editor und ein Programm, das läuft. Kapitel 1 ist die
Sprache selbst: was du speichern kannst, was du damit machen kannst, und wie ein
Programm entscheidet und wiederholt.

**Weiter:** [Kapitel 1 – Java-Grundlagen](01-java-grundlagen.md)
