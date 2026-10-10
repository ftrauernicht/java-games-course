🇩🇪 Deutsch | 🇬🇧 [English](../en/02-methods-arrays-input.md)

[← Zurück zur Kursübersicht](../../../README.de.md) · Zurück: [Kapitel 1 – Java-Grundlagen](01-java-grundlagen.md)

# Kapitel 2 – Methoden, Arrays und Eingaben

**Ziel:** Zwischen Kapitel 1 und einem spielbaren Spiel stehen drei Dinge. Du musst
ein Programm in benannte Teile zerlegen können, du musst viele Werte statt einem
halten können, und du musst lesen können, was jemand tippt. Am Ende dieses Kapitels
hast du alle drei, und du hast damit ein Spiel geschrieben.

Arbeite in einer Datei namens `MethodsArraysInput.java`, mit demselben
Klassenrahmen wie vorher und der Klasse passend zum Dateinamen. Eine fertige
Fassung des grünen Pfads liegt in
[`code/MethodsArraysInput.java`](../code/MethodsArraysInput.java); der gelbe
Abschnitt ist Übung und steht deshalb nicht dort.

## 🟢 Kern — Eigene Methoden

Bisher hast du alles in `main` geschrieben. Das geht gut, bis es nicht mehr geht:
Ein paar hundert Zeilen in einer Methode liest niemand, und alles, was du zweimal
tun willst, musst du zweimal schreiben.

Eine Methode ist ein benanntes Stück Programm. Hier ist eine, die jemanden begrüßt:

```java
static void greet(String who) {
    System.out.println("Hello, " + who + "!");
}
```

Sie steht **innerhalb** der Klasse und **außerhalb** von `main`, daneben. Deine
Datei sieht damit so aus:

```java
public class MethodsArraysInput {

    public static void main(String[] args) {
        greet("Frank");
        greet("Ada");
    }

    static void greet(String who) {
        System.out.println("Hello, " + who + "!");
    }
}
```

```
Hello, Frank!
Hello, Ada!
```

Die Deklaration von links gelesen: `static` heißt, dass sie zur Klasse gehört und
nicht zu einem bestimmten Objekt, und das ist dasselbe `static`, das auf `main`
sitzt, aus demselben Grund. `void` heißt, dass sie nichts zurückgibt. `greet` ist
der Name. `(String who)` ist das, was sie für ihre Arbeit braucht, und innerhalb
der Methode benutzt du `who` wie jede andere Variable.

Zweimal aufgerufen, einmal geschrieben. Darum geht es.

## 🟢 Kern — Methoden, die etwas zurückgeben

Häufiger willst du eine Antwort statt einer Handlung. Dann ersetzt du `void` durch
den Typ der Antwort und benutzt `return`:

```java
static int add(int first, int second) {
    return first + second;
}
```

```java
System.out.println(add(2, 3));
```

```
5
```

`return` tut zwei Dinge auf einmal: Es benennt die Antwort und es beendet die
Methode sofort. Diese zweite Hälfte ist für sich schon nützlich:

```java
static int larger(int first, int second) {
    if (first > second) {
        return first;
    }
    return second;
}
```

Kein `else` nötig. Wenn das erste `return` läuft, läuft nichts dahinter.

Eine Methode mit Rückgabetyp muss auf jedem Weg durch sie etwas zurückgeben. Lass
die letzte Zeile weg, und der Compiler sagt `missing return statement`. Er weigert
sich, dich eine Methode schreiben zu lassen, die manchmal nichts antwortet.

**Probier es selbst:** Schreib `static boolean isEven(int number)`, die antwortet,
ob eine Zahl gerade ist. Eine Zeile Rumpf reicht, und `%` aus Kapitel 1 erledigt
die Arbeit.

## 🟢 Kern — Arrays, viele Werte unter einem Namen

Eine Variable hält einen Wert. Ein Array hält eine feste Anzahl davon, alle vom
selben Typ, jeden an einer nummerierten Stelle.

Es gibt zwei Wege, eines zu bauen. Entweder du sagst, wie viele du willst, und
füllst sie später:

```java
int[] scores = new int[3];
scores[0] = 10;
scores[1] = 20;
scores[2] = 30;
```

Oder du zählst die Werte gleich auf:

```java
int[] primes = {2, 3, 5, 7, 11};
```

`new` ist das Wort für „bau mir eins davon". Es taucht überall dort auf, wo etwas
gebaut statt nur hingeschrieben werden muss, und du begegnest ihm am Ende dieses
Kapitels beim `Scanner` wieder.

Die eckigen Klammern hinter dem Typ heißen „ein Array davon". Die Stellen sind
**ab 0** nummeriert, die fünf Primzahlen sitzen also auf 0, 1, 2, 3 und 4. Die
letzte Stelle ist immer eins weniger als die Anzahl, und `primes.length` nennt dir
die Anzahl:

```java
System.out.println(primes.length);
System.out.println(primes[0]);
System.out.println(primes[4]);
```

```
5
2
11
```

Fragst du nach einer Stelle, die es nicht gibt, bricht das Programm ab:

```java
System.out.println(primes[5]);
```

```
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: Index 5 out of bounds for length 5
```

Probier das einmal mit Absicht. Die Meldung nennt die Stelle, nach der du gefragt
hast, und die Länge, die es tatsächlich gibt, und zusammen zeigen die beiden
meistens direkt auf den Fehler: eine Schleife, die einen Durchlauf zu weit lief.

## 🟢 Kern — Ein Array durchlaufen

Die Zählschleife aus Kapitel 1 ist dafür gebaut, und jetzt verdient sich die Form
mit der 0 ihren Platz:

```java
for (int i = 0; i < primes.length; i++) {
    System.out.print(primes[i] + " ");
}
System.out.println();
```

```
2 3 5 7 11 
```

`i < primes.length`, nicht `i <= primes.length`. Die letzte gültige Stelle ist
`length - 1`, und `<=` ist genau der Fehler um eins, der die Meldung von eben
erzeugt.

Wenn du jeden Wert willst und die Stellen dich nicht interessieren, gibt es eine
kürzere Form:

```java
int total = 0;
for (int prime : primes) {
    total += prime;
}
System.out.println("Sum: " + total);
```

```
Sum: 28
```

Lies den Doppelpunkt als „in". Sie reicht dir jeden Wert der Reihe nach, und sie
kann nicht über das Ende hinauslaufen, weil sie mit Stellen überhaupt nicht
hantiert. Nimm sie, wenn du nur die Werte brauchst; nimm die zählende Form, wenn
du wissen musst, wo du bist, oder wenn du ändern willst, was gespeichert ist.

Damit ist auch das Versprechen aus Kapitel 0 eingelöst. `String[] args` ist ein
Array wie jedes andere, nur mit Text statt Zahlen. `args.length` ist also die Zahl
der Wörter hinter dem Programmnamen, und `args[0]` ist das erste davon.
`Greet.java` hat `args.length == 0` genau aus dem Grund geprüft, den du jetzt
kennst: Nach `args[0]` zu fragen, wenn nichts getippt wurde, greift über das Ende
hinaus.

## 🟢 Kern — Arrays sind Verweise, einzelne Werte nicht

Dieser Abschnitt ist kurz, und er ist der, der jeden späteren Kurs funktionieren
lässt.

Übergibst du ein `int` an eine Methode, bekommt die Methode eine Kopie:

```java
static void tryToChange(int value) {
    value = 99;
}
```

```java
int number = 1;
tryToChange(number);
System.out.println(number);
```

```
1
```

Nichts ist passiert. Die Methode hat ihre eigene Kopie geändert, und danach war die
Kopie weg.

Jetzt dasselbe mit einem Array:

```java
static void changeFirst(int[] values) {
    values[0] = 99;
}
```

```java
int[] box = {1};
changeFirst(box);
System.out.println(box[0]);
```

```
99
```

Das ist geblieben. Eine Array-Variable hält nicht die Werte, sie hält den Ort, an
dem die Werte liegen. Gibst du sie an eine Methode, bekommt die Methode denselben
Ort und keine Kopie des Inhalts. Sie schaut also auf dein Array, und was sie
hineinschreibt, siehst du.

Merk dir diese Form, denn auf ihr steht jedes Spiel in diesem Repository. Kurs 2
übergibt sein Spielfeld an eine Methode, die einen Zug einträgt. Kurs 4 übergibt
ein Gitter an eine Methode, die Nachbarn zählt. Nichts davon ginge, wenn Arrays
sich wie `int` verhielten.

## 🟢 Kern — Lesen, was jemand tippt

Bisher standen alle Werte im Quelltext. Ein Spiel braucht sie von einem Menschen.

Das Werkzeug heißt `Scanner`, und es liegt in einem Teil der Bibliothek, der nicht
von vornherein bereitsteht. Deshalb forderst du es ganz oben in der Datei an, über
`public class`:

```java
import java.util.Scanner;
```

Dann, als Erstes in `main`:

```java
Scanner in = new Scanner(System.in);

System.out.print("What is your name? ");
String typedName = in.nextLine();
System.out.println("Hello, " + typedName + "!");
```

`System.in` ist die Tastatur. `nextLine()` wartet, bis Enter gedrückt wird, und
gibt alles zurück, was davor getippt wurde, als `String`.

Zwei Regeln zum `Scanner` in diesem Kurs, beide da, weil die Alternative einen
Nachmittag kostet.

**Lies mit `nextLine()` und mit nichts anderem.** Der `Scanner` hat `nextInt()`,
und das sieht nach dem naheliegenden Weg aus, eine Zahl zu lesen. Es liest die
Ziffern und lässt den Zeilenumbruch im Puffer liegen, sodass das nächste
`nextLine()` ihn findet, eine leere Zeichenkette liefert und gar nicht erst wartet.
Das Symptom ist eine Frage, die sich selbst zu überspringen scheint. Lies Zeilen,
wandle danach um.

**Schließ ihn nie.** Ein Editor schlägt dir vor, den `Scanner` zu schließen oder
ihn so einzupacken, dass er sich selbst schließt. Einen `Scanner` auf `System.in`
zu schließen schließt `System.in`, endgültig, für den Rest des Programms. Nichts
kann es wieder öffnen. Ein `Scanner`, in `main` gebaut, nie geschlossen.

Für eine Zahl wandelst du die Zeile um:

```java
String line = in.nextLine().trim();
int age = Integer.parseInt(line);
```

`trim()` entfernt Leerzeichen an beiden Enden, weil Leute die Leertaste drücken.
`Integer.parseInt` macht aus Ziffern ein `int` und bricht das Programm ab, wenn der
Text keine Zahl ist. Also prüf vorher:

```java
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
```

`text.isEmpty()` beantwortet, ob überhaupt keine Zeichen da sind, und das muss
separat abgelehnt werden: Eine leere Zeile ist keine Zahl, aber die Schleife
darunter würde das nie merken, weil sie nichts anzuschauen hätte und direkt bis
zum `true` durchfiele.

`text.length()` ist die Anzahl der Zeichen, und `text.charAt(i)` ist das an dieser
Stelle, ein `char`, der sich also mit `<` und `>` vergleichen lässt. Jedes Zeichen
außerhalb von `'0'` bis `'9'`, und die Antwort ist nein. (Nur Ziffern, ein
Minuszeichen zählt also als „keine Zahl". Nichts in diesem Kurs fragt nach einer
negativen.)

Setz die beiden zusammen, und du hast die Form, die jedes Spiel hier für Eingaben
benutzt:

```java
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
```

`while (true)` sieht beunruhigend aus und ist in Ordnung: Das `return` ist der
Ausgang, und daran vorbei kommt nur eine gültige Zahl. Beachte, dass der `Scanner`
als Parameter hereingereicht und nicht drinnen noch einmal gebaut wird. Es gibt
eine Tastatur, also gibt es einen `Scanner`.

```java
int age = askForNumber(in, "How old are you? ");
System.out.println("In ten years you will be " + (age + 10) + ".");
```

```
How old are you? twelve
That was not a whole number. Try again.
How old are you? 34
In ten years you will be 44.
```

## 🟡 Optional — Was Text sonst noch kann

`String` hat eine Schublade voll nützlicher Methoden. Die, die sich jetzt zu kennen
lohnen:

```java
String text = "  Hello World  ";
System.out.println(text.trim());
System.out.println(text.trim().length());
System.out.println(text.toLowerCase().trim());
System.out.println(text.contains("World"));
System.out.println(text.trim().charAt(0));
```

```
Hello World
11
hello world
true
H
```

`toLowerCase()` ist der praktische Weg, `Y` und `y` zu akzeptieren, ohne jeden Fall
doppelt zu schreiben. Zusammen mit `.equals` aus Kapitel 1:

```java
String answer = "  YES  ";
if ("yes".equals(answer.trim().toLowerCase())) {
    System.out.println("Accepted.");
}
```

```
Accepted.
```

Von innen nach außen gelesen: Leerzeichen weg, kleinschreiben, vergleichen. Diese
eine Zeile verarbeitet `Yes`, `YES` und `  yes  ` gleichermaßen.

Keine davon ändert das Original. `text.trim()` gibt einen neuen Text zurück und
lässt `text` genau so, wie er war, was einen einmal überrascht. Willst du das
Ergebnis behalten, weis es etwas zu.

## 🔴 Optional, echte Herausforderung — Zahlenraten

Schreib ein Spiel. Das Programm wählt eine geheime ganze Zahl von 1 bis 100 und
lässt den Spieler raten. Nach jedem Versuch gibt es dem Spieler eine Anweisung:
`Higher.`, wenn das Geheimnis über dem Tipp liegt, `Lower.`, wenn es darunter
liegt. Stimmt der Tipp, sagt es das und nennt die Zahl der Versuche.

```
I am thinking of a number between 1 and 100.
Your guess: 50
Lower.
Your guess: 25
Higher.
Your guess: 37
That's it. You needed 3 tries.
```

Die Bausteine stehen alle in Kapitel 1 und 2:

1. Bau das Geheimnis mit `Math.random()` und einem Cast, verschoben, sodass es bei
   1 beginnt.
2. Zähl die Versuche in einem `int`, das pro Tipp um eins steigt.
3. Schleife mit `while (true)` und lies jedes Mal einen Tipp mit `askForNumber`.
4. Vergleich, gib den Hinweis aus, und `break` heraus, wenn der Tipp stimmt.

Schreib `askForNumber` und `isWholeNumber` auch in diese Datei, denn das ist ein
neues Programm mit eigener `main`.

Zwei Dinge lohnen sich beim Bauen. Gib das Geheimnis direkt nach dem Auswürfeln
aus, damit du schnell testen kannst, und lösch die Zeile, wenn das Spiel läuft. Und
prüf die Ränder: Kann das Geheimnis jemals 0 sein, oder 101? Rechne das an der
Formel nach, statt hundert Runden zu spielen.

Falls du feststeckst, zeigt
[`code/GuessTheNumber.java`](../code/GuessTheNumber.java) einen Weg, es zu
schreiben.

## Was du gelernt hast

- Eigene Methoden schreiben, mit Parametern und mit Rückgabetyp
- Dass `return` die Methode auch beendet, was oft ein `else` ersetzt
- Arrays: `new int[3]`, die Form `{1, 2, 3}`, `length`, und das Zählen ab 0
- Ein Array mit einer Zählschleife durchlaufen, und mit dem kürzeren
  `for (int x : xs)`
- **Dass ein an eine Methode übergebenes Array dasselbe Array ist, keine Kopie**
- `ArrayIndexOutOfBoundsException` und was sie dir sagt
- `import`, `Scanner`, und Lesen mit `nextLine()`
- Eine Zeile vor `Integer.parseInt` prüfen statt danach
- Warum ein `Scanner` auf `System.in` nie geschlossen wird
- `trim`, `length` und `charAt`, die die Eingabeprüfung gebraucht hat
- *(optional)* `toLowerCase` und `contains`

## Weiter

Das ist die ganze Sprache, die dieser Kurs braucht. Alles ab hier ist ein Spiel.

[Kurs 2 - Tic-Tac-Toe](../../02-tic-tac-toe/de/01-tic-tac-toe.md) baut ein
Spielfeld, zwei Spieler, und eine Prüfung auf drei in einer Reihe. Er setzt diesen
Kurs voraus und sonst nichts, und das gilt für jeden Kurs danach genauso. Du
kannst sie also in der Reihenfolge nehmen, die dich reizt. Die vollständige Liste
steht in [PROJECT-IDEAS.de.md](../../../PROJECT-IDEAS.de.md).

**Weiter:** [Kurs 2 - Tic-Tac-Toe](../../02-tic-tac-toe/de/01-tic-tac-toe.md)
