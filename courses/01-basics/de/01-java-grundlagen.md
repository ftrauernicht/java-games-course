🇩🇪 Deutsch | 🇬🇧 [English](../en/01-java-basics.md)

[← Zurück zur Kursübersicht](../../../README.de.md) · Zurück: [Kapitel 0 – Einführung & Werkzeuge](00-einfuehrung.md) · Weiter: [Kapitel 2 – Methoden, Arrays und Eingaben](02-methoden-arrays-eingaben.md) →

# Kapitel 1 – Java-Grundlagen

**Ziel:** Dieses Kapitel ist die Sprache. Am Ende kannst du Werte speichern, mit
ihnen rechnen, sie vergleichen, und ein Programm entscheiden und wiederholen
lassen. Mehr steckt in den Spielen dieses Repositorys nicht drin. Nichts davon ist
spielespezifisch, und alles kommt in jedem folgenden Kurs wieder.

Arbeite in einer Datei namens `Basics.java`. Fang sie mit demselben Rahmen an wie
in Kapitel 0, nur mit der Klasse passend zum Dateinamen:

```java
public class Basics {
    public static void main(String[] args) {

    }
}
```

Alles aus diesem Kapitel kommt in `main`, zwischen die innersten geschweiften
Klammern, und du hängst beim Durchgehen immer mehr an. Eine fertige Fassung des
grünen Pfads liegt in [`code/Basics.java`](../code/Basics.java), in derselben
Reihenfolge wie unten. Der gelbe Abschnitt ist Übung und steht deshalb nicht dort.

## 🟢 Kern — Etwas auf den Bildschirm bringen

`System.out.println` kennst du aus Kapitel 0. Es hat ein Geschwister:

```java
System.out.println("A line of its own.");
System.out.print("No line break ");
System.out.println("until here.");
```

```
A line of its own.
No line break until here.
```

`println` beendet die Zeile, `print` nicht.

Um einen Wert mitten in einen Satz zu setzen, verbindest du die Teile mit `+`:

```java
String name = "Ada";
System.out.println("Hello, " + name + "!");
```

Manche Sprachen erlauben es, die Variable direkt in den Text zu schreiben. Java
nicht. Du verbindest entweder mit `+`, oder du nimmst `printf`, das zuerst den Satz
bekommt und die Werte danach:

```java
System.out.printf("%s is %d years old.%n", "Ada", 36);
```

```
Ada is 36 years old.
```

`%s` ist die Stelle für Text, `%d` für eine ganze Zahl, `%n` für einen
Zeilenumbruch. `printf` verdient sich sein Geld, wenn Spalten untereinander stehen
sollen, und genau das braucht ein Spielfeld. Deshalb taucht es in den Kursen 2 und
5 wieder auf.

Speichere, übersetze und führ aus, nach diesem Abschnitt und nach jedem weiteren.
Jeder sagt dir, was du sehen solltest. Siehst du etwas anderes, ist der Fehler noch
klein und in der Nähe.

## 🟢 Kern — Was du speichern kannst

Eine Variable ist ein benannter Platz für einen Wert. In Java sagst du dazu, welche
Art von Wert sie aufnimmt, und das ändert sich danach nicht mehr:

```java
int whole = 42;
double fraction = 2.5;
boolean yes = true;
char letter = 'J';
String word = "Java";
```

Fünf Typen, und sie decken fast alles in diesem Kurs ab. `int` ist eine ganze Zahl.
`double` hat Nachkommastellen. `boolean` ist wahr oder falsch, mehr nicht. `char`
ist ein einzelnes Zeichen in einfachen Anführungszeichen. `String` ist Text in
doppelten Anführungszeichen, und er ist der mit dem Großbuchstaben, was ein Hinweis
darauf ist, dass er nicht ganz wie die anderen vier funktioniert.

Die Anführungszeichen sind wichtig: `'J'` ist ein Zeichen, `"J"` ist Text, der
zufällig ein Zeichen lang ist. Java behandelt beides unterschiedlich und sagt das
auch.

Den Typ vorher zu nennen ist genau das, was dem Compiler erlaubt, Fehler vor dem
Programmlauf zu finden. Füg diese Zeile ein und übersetz:

```java
whole = "forty-two";
```

```
Basics.java:14: error: incompatible types: String cannot be converted to int
        whole = "forty-two";
                ^
1 error
```

Die Zeilennummer hängt davon ab, wo in der Datei die Zeile steht; der Rest der
Meldung ist überall gleich.

In einer Sprache ohne diese Prüfung wäre die Zeile in Ordnung, bis irgendwann etwas
damit rechnen will. Lösch sie wieder.

## 🟢 Kern — Rechnen, und eine Überraschung

Die fünf Operatoren sind die erwartbaren, mit einem, der weniger offensichtlich ist:

```java
System.out.println(7 + 2);
System.out.println(7 - 2);
System.out.println(7 * 2);
System.out.println(7 / 2);
System.out.println(7 % 2);
```

```
9
5
14
3
1
```

`7 / 2` ist **3**, nicht 3,5. Wenn auf beiden Seiten ganze Zahlen stehen, teilt
Java ganzzahlig und wirft den Rest weg. Willst du den Rest, gibt `%` ihn dir:
`7 % 2` ist 1, das, was übrig blieb.

Darauf fällt jeder einmal herein. Wenn du 3,5 willst, mach eine Seite zu einem
`double`:

```java
System.out.println(7 / 2.0);
```

```
3.5
```

Beide Hälften davon sind nützlich und nicht lästig. Kurs 2 macht aus einer
Feldnummer von 1 bis 9 mit genau diesen zwei Operatoren eine Zeile und eine Spalte,
und Kurs 4 nutzt `%`, um ein Gitter um seine eigenen Ränder herum zu schließen.

## 🟢 Kern — Casting, und deine erste Zufallszahl

Der Weg zurück, von `double` zu `int`, verliert Information. Deshalb verlangt Java,
dass du schriftlich danach fragst:

```java
double exact = 3.9;
int cut = (int) exact;
System.out.println(cut);
```

```
3
```

Ein `(int)` vor einem Wert ist ein **Cast**. Achte darauf, dass er abschneidet und
nicht rundet: Aus 3,9 wird 3.

Genau das brauchst du für eine Zufallszahl. `Math.random()` liefert dir ein
`double` irgendwo von 0 bis knapp unter 1. Multiplizier es mit der Zahl der Werte,
die du willst, schneid den Rest ab, und verschieb es dahin, wo es beginnen soll:

```java
int roll = (int) (Math.random() * 6) + 1;
System.out.println("You rolled a " + roll + ".");
```

Führ es ein paar Mal aus. `Math.random() * 6` landet zwischen 0 und knapp unter 6,
der Cast macht daraus 0, 1, 2, 3, 4 oder 5, und das `+ 1` macht einen Würfel
daraus. Ändere die 6 und die 1, und du bekommst jeden Bereich, den du willst. So
setzt Kurs 5 ein neues Plättchen, und so würfelt die Herausforderung am Ende von
Kapitel 2 ihr Geheimnis aus.

## 🟢 Kern — Vergleichen, verknüpfen, und eine Kurzform

Vergleiche liefern einen `boolean`:

```java
int a = 10;
int b = 3;
System.out.println(a > b);
System.out.println(a == b);
System.out.println(a != b);
```

```
true
false
true
```

Zwei Gleichheitszeichen fragen, ob etwas gleich ist. Ein Gleichheitszeichen weist
zu. Ein `=` zu schreiben, wo `==` gemeint war, ist ein Klassiker, und in Java
scheitert das meistens schon beim Übersetzen, statt still das Falsche zu tun.

Booleans lassen sich verknüpfen. `&&` heißt „beides", `||` heißt „eines von
beiden", `!` dreht einen um:

```java
System.out.println(a > b && b > 0);
System.out.println(a > b || b > 100);
System.out.println(!(a > b));
```

```
true
true
false
```

Fürs Zuweisen gibt es eine Kurzschreibweise. `a += 5` heißt `a = a + 5`, und
genauso geht es mit `-=`, `*=` und `/=`. `a++` zählt eins hoch, `a--` eins runter.

Zuletzt eine knappe Art, zwischen zwei Werten zu wählen:

```java
String verdict = a > b ? "bigger" : "smaller";
```

Das Fragezeichen liest sich als „wenn", der Doppelpunkt als „sonst". Das zu kennen
lohnt sich, weil du es im Code anderer Leute lesen wirst, aber sobald etwas
Komplizierteres passiert, ist ein `if` klarer.

## 🟢 Kern — Text vergleichen, und das geht anders als gedacht

Hier ist die Java-Falle, die Anfänger die meiste Zeit kostet. Um zwei Texte zu
vergleichen, nimmst du `.equals`:

```java
String typed = "yes";
String expected = "yes";
System.out.println(typed.equals(expected));
```

```
true
```

Nimm für Text **kein** `==`. `==` fragt, ob zwei Dinge dasselbe Objekt im Speicher
sind, nicht ob sie dasselbe sagen. Bei Text, der direkt im Quelltext steht,
verwendet Java im Hintergrund ein einziges Objekt wieder, also sagt `==` oft `true`
und der Fehler bleibt verborgen. Und irgendwann kommt der Text von woanders her,
und derselbe Vergleich sagt `false`, obwohl auf beiden Seiten sichtbar `yes` steht.

Jedes Spiel in diesem Repository vergleicht Text, den jemand getippt hat. Diese
Regel trägt also ab Kurs 2 mit: **für Text immer `.equals`.**

Eine Ergänzung spart später Ärger. Eine `String`-Variable kann auch **überhaupt
nichts** enthalten, und das nennt Java `null`. Das ist etwas anderes als leerer
Text: `""` ist Text, der zufällig keine Zeichen hat, während `null` heißt, dass da
gar kein Text ist, den man fragen könnte. Nichts etwas zu fragen beendet das
Programm:

```java
String missing = null;
System.out.println(missing.equals("yes"));
```

```
Exception in thread "main" java.lang.NullPointerException: Cannot invoke
"String.equals(Object)" because "<local1>" is null
```

Der Teil in Anführungszeichen ist Javas Versuch, die Variable zu benennen. Aus
einem Editor übersetzt steht dort meist `"missing"`, aus einem blanken `javac`
ein Platzhalter wie `<local1>`, weil der Name nicht mitgespeichert wurde.

Stell den Wert, den du kennst, nach vorn, und das Problem verschwindet. Dann
fragt nämlich der bekannte Text, und der ist mit Sicherheit da:

```java
String missing = null;
System.out.println("yes".equals(missing));
```

```
false
```

Nichts in diesem Kurs drückt dir absichtlich ein `null` in die Hand. Die
Angewohnheit kostet eine Umstellung und nimmt eine ganze Fehlerklasse weg, sie
lohnt sich also jetzt schon.

## 🟢 Kern — Eine Entscheidung treffen

```java
int score = 72;
if (score >= 90) {
    System.out.println("Excellent.");
} else if (score >= 50) {
    System.out.println("Passed.");
} else {
    System.out.println("Not this time.");
}
```

```
Passed.
```

Die Bedingung in den Klammern muss ein `boolean` sein. Java prüft die Zweige von
oben und führt den ersten aus, der passt, danach überspringt es den Rest. Ändere
`score` und führ es noch einmal aus, bis du alle drei gesehen hast.

Die geschweiften Klammern sind in diesem Kurs nicht optional, auch wenn ein Zweig
nur eine Zeile lang ist. Sie wegzulassen ist erlaubt und genau der Weg, auf dem aus
einer einzeiligen Korrektur klammheimlich ein zweizeiliger Fehler wird.

## 🟢 Kern — Zwischen vielen festen Werten wählen

Wenn ein Wert über mehrere bekannte Fälle entscheidet, wird eine Kette aus
`else if` mühsam. Ein `switch` sagt dasselbe deutlicher:

```java
String command = "w";
switch (command) {
    case "w" -> System.out.println("up");
    case "s" -> System.out.println("down");
    case "a" -> System.out.println("left");
    case "d" -> System.out.println("right");
    default -> System.out.println("unknown command");
}
```

```
up
```

`default` fängt alles, worauf kein Fall passte. Die Pfeilform ist die moderne, und
dieser Kurs nutzt keine andere. Es gibt eine ältere Form mit Doppelpunkten, die
nach jedem Fall ein `break` braucht, und das ist eine berühmte Fehlerquelle. Du
wirst ihr im Code anderer Leute begegnen; schreiben musst du sie nicht.

Kurs 5 liest einen Buchstaben und entscheidet, was damit zu tun ist. Genau diese
Form hat dort die Eingabebehandlung.

## 🟢 Kern — Eine feste Anzahl von Malen wiederholen

```java
for (int i = 1; i <= 5; i++) {
    System.out.print(i + " ");
}
System.out.println();
```

```
1 2 3 4 5 
```

In den Klammern stehen drei Dinge, getrennt durch Semikolons: wo es losgeht, wie
lange es weitergeht, und was nach jedem Durchlauf passiert. Lies es als „setz `i`
auf 1, mach weiter solange `i` höchstens 5 ist, und zähl jedes Mal eins dazu".

Bei 0 anzufangen ist die andere gängige Form, und ab Kapitel 2 ist sie die übliche,
weil Listen dort anfangen:

```java
for (int i = 0; i < 5; i++) {
    System.out.print(i + " ");
}
System.out.println();
```

```
0 1 2 3 4 
```

Wieder fünf Durchläufe, mit `i` als 0, 1, 2, 3, 4. Beachte `<` statt `<=`.

## 🟢 Kern — Wiederholen, bis sich etwas ändert

Wenn du vorher nicht weißt, wie viele Durchläufe du brauchst, macht `while` weiter,
solange seine Bedingung gilt:

```java
int countdown = 3;
while (countdown > 0) {
    System.out.println(countdown);
    countdown--;
}
System.out.println("Go!");
```

```
3
2
1
Go!
```

Irgendetwas in der Schleife muss die Bedingung Richtung falsch bewegen. Vergiss das
`countdown--`, und das Programm druckt ewig 3. Falls das passiert, drück Strg+C im
Terminal, um es zu beenden. Es wird dir mindestens einmal passieren, und es ist
kein Drama.

`while` ist die Form, die jede Spielschleife in diesem Repository hat:
weiterspielen, solange niemand gewonnen hat.

Zwei Wörter steuern eine Schleife von innen. `break` verlässt sie sofort, und
`continue` bricht den aktuellen Durchlauf ab und beginnt den nächsten:

```java
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
```

```
1 3 5 7 
```

Die geraden Zahlen überspringt `continue`, und bei 9 beendet `break` die ganze
Schleife. Beide kommen in jedem Spiel hier wieder vor: `break` beendet eine Runde,
die jemand gewonnen hat, und `continue` wirft eine unsinnige Eingabe weg und fragt
noch einmal.

## 🟡 Optional — Notizen an dich selbst, unverrückbare Werte, saubere Ausgabe

Alles nach `//` ignoriert der Compiler. Es steht da für den, der den Code als
Nächstes liest, und das bist meistens du in drei Wochen:

```java
// Zwei Schrägstriche gelten bis zum Zeilenende.
/* Diese Form geht über
   mehrere Zeilen. */
```

Gute Kommentare sagen warum, nicht was. `i++ // zählt i um eins hoch` hilft
niemandem.

Soll ein Wert sich nie ändern, sag das mit `final`:

```java
final int BOARD_SIZE = 3;
```

Ihm danach etwas zuzuweisen verhindert das Übersetzen. Die Großbuchstaben sind eine
Konvention für Werte, die einmal festgelegt und überall benutzt werden, und jedes
Spiel hier legt seine Feldgröße so fest.

Zurück zu `printf`, das mehr kann, als Lücken zu füllen. Eine Zahl zwischen dem `%`
und dem Buchstaben ist eine Mindestbreite:

```java
System.out.printf("%5d|%n", 42);
System.out.printf("%5d|%n", 1234);
```

```
   42|
 1234|
```

Beide Zeilen sind gleich breit, also stehen die rechten Kanten untereinander. Genau
dieses Detail macht in Kurs 5 ein 2048-Feld lesbar.

## 🔴 Optional, echte Herausforderung — FizzBuzz

Gib die Zahlen von 1 bis 15 aus, eine pro Zeile, mit zwei Ersetzungen: Für eine
Zahl, die durch 3 teilbar ist, gib `Fizz` aus, für eine durch 5 teilbare `Buzz`,
und für eine, die durch beide teilbar ist, `FizzBuzz`.

```
1
2
Fizz
4
Buzz
...
```

Alles, was du brauchst, steht in diesem Kapitel: eine `for`-Schleife, `%`, und
`if`/`else if`.

Eine Warnung, denn darum geht es bei dieser Übung. Die naheliegende Reihenfolge der
Prüfungen liefert für 15 das falsche Ergebnis. Überleg dir vor dem Schreiben,
warum, dann löst sich das Problem von selbst; schreib zuerst, und du rätselst eine
Weile.

Falls du feststeckst, zeigt [`code/FizzBuzz.java`](../code/FizzBuzz.java) einen
Weg, es zu schreiben.

## Was du gelernt hast

- Ausgeben mit `println`, `print` und `printf`, und Werte mit `+` verbinden
- Die fünf Typen: `int`, `double`, `boolean`, `char`, `String`
- Dass der Typ bei der Deklaration festgelegt wird und der Compiler darauf besteht
- Ganzzahlige Division und `%`, und warum `7 / 2` gleich 3 ist
- Casting mit `(int)`, und Zufallszahlen mit `Math.random()`
- Vergleichs- und Logikoperatoren, `+=`, `++`, und die Kurzform `? :`
- **`.equals` für Text, niemals `==`**
- `if` / `else if` / `else`, und `switch` mit Pfeilen
- `for`, wenn du die Anzahl kennst, `while`, wenn nicht
- *(optional)* Kommentare, `final`, und Breiten in `printf`

## Weiter

Kapitel 2 ergänzt die letzten drei Dinge, die jedes Spiel hier braucht: eigene
Methoden, Listen von Werten, und das Lesen dessen, was jemand tippt.

**Weiter:** [Kapitel 2 – Methoden, Arrays und Eingaben](02-methoden-arrays-eingaben.md)
