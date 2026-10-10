🇩🇪 Deutsch | 🇬🇧 [English](../en/01-game-of-life.md)

[← Zurück zur Kursübersicht](../../../README.de.md) · Setzt voraus: [Kurs 1 – Grundlagen](../../01-basics/de/00-einfuehrung.md)

# Kurs 4 – Conways Game of Life

**Ziel:** ein Spiel ohne Spieler. Du setzt ein paar Zellen, vier Regeln übernehmen,
und ein Muster wandert von allein über den Bildschirm. Am Ende hast du den
lehrreichsten Fehler dieses Repositorys kennengelernt und vermieden.

Setzt [Kurs 1](../../01-basics/de/00-einfuehrung.md) voraus und sonst nichts. Eine
fertige Fassung liegt in [`code/GameOfLife.java`](../code/GameOfLife.java).

## 🟢 Kern — Vier Regeln

Ein Gitter aus Feldern. Jedes Feld ist entweder lebendig oder tot. Jedes Feld hat
bis zu acht Nachbarn, die Diagonalen mitgezählt.

Ein Schritt erzeugt eine ganze neue Generation, und jedes Feld entscheidet
gleichzeitig, nach diesen vier Regeln:

- Ein lebendes Feld mit weniger als zwei lebenden Nachbarn stirbt.
- Ein lebendes Feld mit zwei oder drei lebenden Nachbarn bleibt am Leben.
- Ein lebendes Feld mit mehr als drei lebenden Nachbarn stirbt.
- Ein totes Feld mit genau drei lebenden Nachbarn wird lebendig.

Das ist das ganze Spiel. Es wurde 1970 erfunden, und bis heute finden Leute neue
Muster darin.

Die vier Regeln schrumpfen auf zwei Zeilen Code zusammen, und das ist jetzt schon
eine Bemerkung wert, damit du es später wiedererkennst: Ein lebendes Feld überlebt
bei 2 oder 3, ein totes wird bei genau 3 geboren.

## 🟢 Kern — Zweidimensionale Arrays, kurz erklärt

*(Wenn du Kurs 2 oder Kurs 5 gemacht hast, überspring das und lies beim nächsten
Abschnitt weiter.)*

Kurs 1 hat dir ein Array gegeben: eine Reihe von Werten unter einem Namen. Ein
Gitter ist ein Array, dessen Werte selbst Arrays sind:

```java
boolean[][] grid = new boolean[ROWS][COLS];
```

Zwei Klammerpaare, zwei Größen. `grid[3][7]` ist Zeile 3, Spalte 7, und beide
zählen ab 0. `boolean` ist der Typ, weil ein Feld lebendig oder tot ist und sonst
nichts, und ein frisches `boolean`-Array ist bereits voller `false`, es gibt also
nichts auszufüllen.

Das ganze Gitter durchläuft man mit einer Schleife in einer Schleife:

```java
for (int row = 0; row < ROWS; row++) {
    for (int col = 0; col < COLS; col++) {
        // grid[row][col]
    }
}
```

Übergibst du das Gitter an eine Methode, übergibst du das Gitter selbst und keine
Kopie, die Methode kann es also ändern. Kurs 1 hat das unter „Arrays sind Verweise"
behandelt, und dieses Kapitel stützt sich zweimal darauf.

Richte deine Datei ein:

```java
public class GameOfLife {

    static final int ROWS = 10;
    static final int COLS = 20;
    static final int GENERATIONS = 8;

    public static void main(String[] args) {
        boolean[][] grid = new boolean[ROWS][COLS];
    }
}
```

## 🟢 Kern — Das Gitter zeichnen

Ein lebendes Feld ist ein `#`, ein totes ein `.`:

```java
static void print(boolean[][] grid, int generation) {
    System.out.println("Generation " + generation);
    for (int row = 0; row < ROWS; row++) {
        for (int col = 0; col < COLS; col++) {
            System.out.print(grid[row][col] ? '#' : '.');
        }
        System.out.println();
    }
    System.out.println();
}
```

Das `? :` aus Kurs 1 wählt das Zeichen. Die innere Schleife druckt eine Zeile ohne
Umbrüche, und das `println` danach beendet die Zeile. Deshalb kommt das Gitter als
Gitter heraus und nicht als eine lange Zeichenkette.

Die Generationen werden untereinander gedruckt, mit Überschrift, statt an Ort und
Stelle neu gezeichnet. Ein Terminal zu löschen braucht Steuerzeichen, die das
Windows Terminal versteht und ein altes Konsolenfenster als Buchstabensalat
ausgibt. Die portable Wahl ist also zu scrollen. Außerdem kannst du so noch einmal
auf Generation 3 zurückschauen, und das wirst du wollen.

Jetzt etwas zum Anschauen. Ein **Gleiter** sind fünf Zellen, die ewig diagonal
wandern:

```java
grid[1][2] = true;
grid[2][3] = true;
grid[3][1] = true;
grid[3][2] = true;
grid[3][3] = true;
```

```java
print(grid, 0);
```

```
Generation 0
....................
..#.................
...#................
.###................
....................
```

## 🟢 Kernübung — Nachbarn zählen

Jede Regel braucht dieselbe Zahl: Wie viele der acht Felder rundherum leben?

```java
static int countNeighbours(boolean[][] grid, int row, int col) {
    // dein Code hier -- die vier Schritte unten
}
```

1. Fang mit einem Zähler bei 0 an.
2. Lauf über die neun Felder um dieses herum, es eingeschlossen, mit zwei Schleifen
   von -1 bis 1. Nenn die Versätze `dRow` und `dCol`; das betrachtete Feld ist
   `row + dRow`, `col + dCol`.
3. Überspring zwei Arten von Feldern: das in der Mitte, wo beide Versätze 0 sind,
   denn ein Feld ist nicht sein eigener Nachbar; und jedes Feld außerhalb des
   Gitters, wo die Zeile unter 0 oder mindestens `ROWS` ist, oder die Spalte unter
   0 oder mindestens `COLS`. `continue` ist für beides das Werkzeug.
4. Für den Rest zähl 1 dazu, wenn das Feld lebt, und gib am Ende den Zähler zurück.

Schritt 3 ist die eigentliche Übung. Eine Zelle in der Mitte hat acht Nachbarn,
eine am Rand fünf, eine in der Ecke drei. Verrechne dich bei den Grenzen, und das
Programm bricht beim ersten Blick auf Zeile 0 mit
`ArrayIndexOutOfBoundsException` ab, was dir immerhin sofort Bescheid sagt.

Teste sie, bevor du weitermachst. Auf dem Gleiter oben muss
`countNeighbours(grid, 2, 2)` gleich 5 sein und `countNeighbours(grid, 0, 0)`
gleich 0.

## 🟢 Kernübung — Die nächste Generation

```java
static boolean[][] nextGeneration(boolean[][] grid) {
    // dein Code hier -- die drei Schritte unten
}
```

1. Bau ein **neues** Gitter derselben Größe.
2. Zähl für jedes Feld die Nachbarn im **alten** Gitter und schreib das Ergebnis
   ins **neue**: Ein lebendes Feld überlebt bei 2 oder 3 Nachbarn, ein totes wird
   bei genau 3 geboren.
3. Gib das neue Gitter zurück.

Schritt 1 ist keine Optimierung und keine Stilfrage. Schreibst du die Ergebnisse in
das Gitter, aus dem du noch liest, dann enthalten die Zeilen 0 bis 4 schon Werte
der nächsten Generation, wenn du bei Zeile 5 ankommst, und Zeile 5 zählt die
falschen Nachbarn. Jedes Feld nach dem ersten entscheidet dann gegen eine Mischung
aus zwei verschiedenen Zeitpunkten.

Das Ergebnis sieht trotzdem nach Life aus. Zellen erscheinen und verschwinden,
Muster wachsen und schrumpfen, und offensichtlich kaputt ist nichts. Genau das
macht es zum lehrreichsten Fehler hier: Er ist falsch und sieht gut aus. Der
Gleiter ist der Weg, ihn zu fangen. Ein korrekter Gleiter behält genau fünf Zellen
und wiederholt seine Form alle vier Generationen, ein Feld tiefer und eines weiter
rechts. Ein kaputter verschmiert.

Jetzt die Schleife in `main`:

```java
for (int generation = 0; generation <= GENERATIONS; generation++) {
    print(grid, generation);
    grid = nextGeneration(grid);
}
```

`grid = nextGeneration(grid)` richtet die Variable auf das neue Gitter, und das
alte wird schlicht vergessen. Führ es aus und zähl die `#` in jeder Generation:
fünf, jedes Mal. Vergleich Generation 0 und Generation 4, und du solltest dieselbe
Form sehen, ein Feld tiefer und eines weiter rechts.

## 🟢 Kern — Eine Pause zwischen den Generationen

Acht Generationen kommen schneller, als du lesen kannst. Java kann warten:

```java
Thread.sleep(300);
```

Dreihundert Millisekunden. Setz das zwischen `print` und `nextGeneration`, und der
Compiler weigert sich, dein Programm zu bauen:

```
GameOfLife.java:28: error: unreported exception InterruptedException; must be caught or declared to be thrown
```

Das ist eine neue Art von Beschwerde, und es lohnt sich, sie zu verstehen statt sie
zu umgehen. Manche Methoden warnen in ihrer Signatur, dass während ihrer Arbeit
etwas schiefgehen kann. `Thread.sleep` sagt, dass das Warten von außen abgebrochen
werden kann. Java lässt dich so eine Warnung nicht ignorieren: Du kümmerst dich
darum, oder du reichst sie an den weiter, der dich aufgerufen hat.

Weiterreichen ist ein Zusatz an `main`:

```java
public static void main(String[] args) throws InterruptedException {
```

Lies es als „falls das je passiert, soll das Programm anhalten und es sagen". Für
eine Pause zwischen zwei Bildern ist das die richtige Antwort, und dieses Programm
wird ohnehin niemand unterbrechen.

Den anderen Weg, `try` und `catch`, lernst du in einem späteren Kurs kennen, wo er
sich lohnt. Hier wären es vier Zeilen Zeremonie um etwas, das nicht passieren kann.

## 🟡 Optional — Eine Welt ohne Ränder

Der Gleiter wandert, bis er an eine Wand kommt, und zerfällt dann, weil Felder
außerhalb des Gitters als tot zählen.

Lass das Gitter umlaufen: Das Feld rechts neben der letzten Spalte ist die erste
Spalte, und das Feld über Zeile 0 ist die unterste Zeile. Das Gitter wird zur
Oberfläche eines Doughnuts, und der Gleiter wandert für immer.

Die Änderung steckt in `countNeighbours`. Statt Felder außerhalb zu überspringen,
hol sie zurück in den gültigen Bereich:

```java
int r = (row + dRow + ROWS) % ROWS;
int c = (col + dCol + COLS) % COLS;
```

Überleg, wofür das `+ ROWS` da ist. Der Restoperator behält in Java das Vorzeichen
der linken Seite, `-1 % 10` ist also `-1` und nicht `9`. Das `ROWS` vorher dazu zu
addieren macht die Zahl positiv, bevor der Rest gebildet wird, und über den oberen
Rand schieben kann es nichts, weil `dRow` nie größer als 1 ist.

Lass den Gleiter vierzig Generationen laufen und schau zu, wie er wieder
hereinkommt.

## 🟡 Optional — Andere Muster

Ersetz den Gleiter und schau, was die vier Regeln sonst so tun.

Ein **Blinker** sind drei Zellen in einer Reihe. Er kippt ewig zwischen waagerecht
und senkrecht und ist damit das kleinste Ding, das sich wiederholt.

Ein **Block** ist ein Quadrat aus zwei mal zwei Zellen. Jede Zelle darin hat genau
drei Nachbarn, also bleibt er unverändert liegen.

Ein **R-Pentomino** sind fünf Zellen in einem kleinen Zickzack, und es bleibt aus
fast nichts über tausend Generationen lang interessant. Schlag es nach, setz es in
die Mitte eines größeren Gitters, und erhöh `GENERATIONS`.

## 🟡 Optional — An Ort und Stelle animieren

Wenn dein Terminal es kann, darfst du über das vorige Bild zeichnen, statt zu
scrollen. Gib das vor jeder Generation aus:

```java
System.out.print("\u001B[H\u001B[2J");
System.out.flush();
```

Zwei Steuersequenzen: Cursor nach links oben, dann Bildschirm löschen. Sie
funktionieren im Windows Terminal, im Terminal der meisten Editoren, und unter
macOS und Linux. Sie funktionieren **nicht** in einem alten Windows-Konsolenfenster,
wo stattdessen `[2J` als Text erscheint, und genau deshalb nutzt der grüne Pfad sie
nicht.

Probier es aus und behalt die Fassung, die sich auf deinem Rechner besser liest.

## 🔴 Optional, echte Herausforderung — Merken, wenn sich nichts mehr tut

Viele Startmuster kommen zur Ruhe. Ein Gitter, das sich nicht mehr ändert, druckt
weiter dasselbe Bild, bis die Schleife ausgeht, und das ist Zeitverschwendung für
alle Beteiligten.

Hör früher auf, wenn eine Generation mit der vorherigen übereinstimmt.

```java
static boolean same(boolean[][] a, boolean[][] b) {
    // dein Code hier -- antworte, ob jedes Feld uebereinstimmt
}
```

Ein Vergleich mit `==` tut nicht, was du willst: Kurs 1 hat das für Text erklärt,
und hier gilt derselbe Grund. `a == b` fragt, ob es dasselbe Gitter ist, nicht ob
beide dasselbe enthalten. Du musst beide durchlaufen und Feld für Feld vergleichen.

Und jetzt der ehrliche Teil. Das fängt ein eingefrorenes Gitter, und es fängt
keinen Blinker, der zwischen zwei Bildern wechselt und nie zur Ruhe kommt. Probier
es an einem Blinker aus und sieh zu, wie er bis zum Ende läuft.

Um das auch zu fangen, heb zusätzlich die vorletzte Generation auf und vergleich
gegen beide. Überleg dir danach, was ein Muster mit Periode drei mit deinem
Programm machen würde, und entscheide, ob dich das stört. Genau zu wissen, welche
Fälle deine Prüfung verfehlt, ist mehr wert als eine Prüfung, die behauptet, alles
zu fangen.

## Was du gelernt hast

- Ein Gitter als `boolean[][]`, und das Durchlaufen mit einer Schleife in einer
  Schleife
- Nachbarn zählen mit zwei Versatzschleifen von -1 bis 1
- Das mittlere Feld und alles außerhalb überspringen, mit `continue`
- **Warum die nächste Generation in einem neuen Gitter entstehen muss**
- Dass eine falsche Simulation völlig plausibel aussehen kann, und dass ein
  bekanntes Muster der Weg ist, sie zu überführen
- `Thread.sleep`, und was eine geprüfte Ausnahme ist
- `throws` an `main` als die ehrliche kleine Antwort darauf
- *(optional)* `(x + SIZE) % SIZE` zum Umlaufen eines Index, und warum das
  `+ SIZE` nötig ist
- *(optional)* Warum Terminal-Steuerzeichen nicht portabel sind
- *(optional)* Zwei Gitter Feld für Feld vergleichen, und wissen, was die Prüfung
  verfehlt

## Weiter

Kurs 5 ist 2048, wo ein Gitter sich nicht von allein entwickelt, sondern unter vier
Tasten schiebt und verschmilzt, und wo die Verschmelzungsregel alle hereinlegt. Er
setzt Kurs 1 voraus und sonst nichts.

Die vollständige Liste steht in
[PROJECT-IDEAS.de.md](../../../PROJECT-IDEAS.de.md).
