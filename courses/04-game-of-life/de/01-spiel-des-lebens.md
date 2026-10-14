🇩🇪 Deutsch | 🇬🇧 [English](../en/01-game-of-life.md)

[← Zurück zur Kursübersicht](../../../README.de.md) · Setzt voraus: [Kurs 1 – Grundlagen](../../01-basics/de/00-einfuehrung.md)

# Kurs 4 – Conways Game of Life

**Ziel:** ein Spiel ohne Spieler. Du setzt ein paar Zellen, vier Regeln übernehmen,
und ein Muster entwickelt sich von allein — im Fenster, nicht im Terminal.
Unterwegs begegnest du einem Fehler, dessen Ursache im Code unsichtbar und auf dem
Bildschirm offensichtlich ist, und du wirst ihn vermieden haben.

![Ein zufällig gestartetes Gitter nach 60 Generationen: 163 lebende Zellen, zu Ketten und Klumpen zusammengelaufen](../assets/soup.png)

Das ist das erste Fenster in diesem Repository, und es kostet sieben Zeilen und
keine einzige eigene Klasse. Dass Kurs 6 für sein Fenster erheblich mehr braucht,
liegt nicht am Zeichnen, sondern daran, dass dort Tasten ankommen sollen.

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
behandelt. Dieses Kapitel nutzt es überwiegend nicht: Jede Methode unten liest ein
Gitter und gibt ein neues zurück, statt das übergebene zu ändern. Die eine Stelle,
an der es zählt, ist der Fehler, vor dem das Kapitel weiter unten warnt.

Richte deine Datei ein. Die Größen kommen in die Klasse, neben `main` statt hinein,
denn `draw` und `countNeighbours` brauchen sie auch.

Zwei Fehler liegen hier nahe, und sie geben verschiedene Meldungen. Schreib
`final int ROWS = 30;` **in** `main`, und die anderen Methoden sehen die Variable
gar nicht: `cannot find symbol`, achtmal. Lass sie oben stehen, streich aber das
`static`, und sie gehört ab da einem Objekt, das es nicht gibt:
`non-static variable ROWS cannot be referenced from a static context`, neunmal.
Beide Zahlen hier gemessen; bei dir hängen sie davon ab, wie viel du schon
geschrieben hast.

`static` heißt aus demselben Grund `static`, aus dem hier jede Methode `static` ist:
Die Zahl gehört zur Klasse, und erzeugt wurde noch nichts.

```java
public class GameOfLife {

    static final int ROWS = 30;
    static final int COLS = 40;
    static final int CELL = 14;
    static final int GENERATIONS = 100;
    static final int PAUSE_MS = 80;

    public static void main(String[] args) {
        boolean[][] grid = new boolean[ROWS][COLS];
    }
}
```

`CELL` ist die Kantenlänge einer Zelle in Pixeln und taucht erst beim Zeichnen auf.
`GENERATIONS` steht auf 100, weil der Gleiter auf diesem Gitter bis Generation 104
unversehrt bleibt und danach die untere Wand erreicht — gemessen, nicht geschätzt.

## 🟢 Kern — Ein Fenster, das ein Bild zeigt

*(Kurs 5 benutzt dasselbe Fenster. Wer von dort kommt, kann springen.)*

Ein Gitter aus `#` und `.` im Terminal wäre möglich, und für ein paar Zellen reicht
es auch. Nur erkennt man darin schlecht, ob ein Muster wandert, kippt oder
zerfällt, und genau das ist hier die ganze Frage. Also ein Fenster.

Das kostet weniger, als du vermutest. Es braucht **keine eigene Klasse**, nichts
wird überschrieben, niemand horcht auf irgendetwas. Vier Klassen genügen, und drei
davon machen die Arbeit:

```java
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
```

Ein `BufferedImage` ist ein Bild im Arbeitsspeicher, auf das du malen darfst. Ein
`JLabel` ist ein Anzeigeelement, dem man ein Bild geben kann. Und ein `JFrame` ist
das Fenster drumherum. Diese acht Zeilen kommen an den Anfang von `main`:

```java
BufferedImage picture =
        new BufferedImage(COLS * CELL, ROWS * CELL, BufferedImage.TYPE_INT_RGB);
JLabel label = new JLabel(new ImageIcon(picture));
JFrame window = new JFrame("Game of Life");
window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
window.add(label);
window.pack();
window.setLocationRelativeTo(null);
window.setVisible(true);
```

`CELL` ist die Kantenlänge einer Zelle in Pixeln, hier 14. Das Gitter ist 30 mal 40
Zellen, das Bild also 560 mal 420 Pixel. `pack()` legt das Fenster um das Bild
herum, statt dass du die Fenstergröße rätst, `setLocationRelativeTo(null)` setzt es
mittig auf den Bildschirm statt in die linke obere Ecke, und erst `setVisible(true)`
zeigt es an.

Eine vierte Klasse steckt versteckt darin: `ImageIcon`. Ein `JLabel` nimmt ein
`BufferedImage` nicht direkt entgegen, und ein `ImageIcon` tut nichts weiter, als
ein Bild in die Form zu bringen, die Swing dafür erwartet. Daher die Verschachtelung
in `new JLabel(new ImageIcon(picture))`.

`setDefaultCloseOperation` sagt, was das Schließkreuz tun soll. Ohne diese Zeile
verschwindet das Fenster und das Programm läuft weiter — du siehst nichts mehr und
musst es im Terminal abwürgen.

Gemalt wird mit `Graphics`, und das holst du dir aus dem Bild:

```java
static void draw(BufferedImage picture, boolean[][] grid) {
    Graphics g = picture.createGraphics();

    for (int row = 0; row < ROWS; row++) {
        for (int col = 0; col < COLS; col++) {
            g.setColor(grid[row][col] ? ALIVE : DEAD);
            g.fillRect(col * CELL, row * CELL, CELL - 1, CELL - 1);
        }
    }

    g.dispose();
}
```

Dieselbe Doppelschleife wie beim Drucken, und das `? :` aus Kurs 1 wählt wieder,
nur diesmal eine Farbe statt eines Zeichens:

```java
static final Color DEAD = new Color(24, 26, 30);
static final Color ALIVE = new Color(90, 200, 110);
```

Drei Zahlen von 0 bis 255 für Rot, Grün und Blau.

**Achte auf die Reihenfolge in `fillRect`.** Sie ist `(x, y, breite, höhe)`, und x
ist die **Spalte**, y die **Zeile** — also `col` zuerst, obwohl im ganzen übrigen
Kapitel `row` vorne steht. Vertauschst du die beiden, bekommst du ein an der
Diagonale gespiegeltes Gitter und keine einzige Fehlermeldung. Das `- 1` bei Breite
und Höhe lässt einen Pixel Luft und macht die Zellen einzeln erkennbar statt als
zusammenhängende Fläche.

`g.dispose()` gibt das Zeichenwerkzeug zurück, das `createGraphics()` herausgegeben
hat. Bei einem Bild ist das folgenlos, bei hundert Generationen nicht mehr.

Zwei Zeilen fehlen noch, und sie gehören **nicht** in `draw`, sondern dorthin, wo
`label` zu Hause ist, also nach `main`:

```java
draw(picture, grid);
label.repaint();
```

In das Bild zu malen ändert nichts am Bildschirm. `repaint()` bittet das Fenster,
sich das Bild noch einmal anzusehen. Also: erst malen, dann `repaint`. Schreibst du
`label.repaint()` ans Ende von `draw`, bekommst du `cannot find symbol: variable
label` — `label` ist eine lokale Variable von `main` und existiert anderswo nicht.

Ein Hinweis darauf, warum Kurs 6 für sein Fenster deutlich mehr Aufwand treibt:
Dieses Fenster **zeigt** nur. Es nimmt nichts entgegen. Sobald ein Fenster auf
Tasten reagieren soll, brauchst du eine eigene Klasse, überschriebene Methoden und
Horcher — und das ist der Preis für Eingabe, nicht für Ausgabe.

Jetzt etwas zum Anschauen. Ein **Gleiter** sind fünf Zellen, die diagonal wandern.
Pack sie gleich in eine Methode, denn weiter unten gibt es einen zweiten Start zur
Auswahl:

```java
static boolean[][] glider() {
    boolean[][] grid = new boolean[ROWS][COLS];
    grid[1][2] = true;
    grid[2][3] = true;
    grid[3][1] = true;
    grid[3][2] = true;
    grid[3][3] = true;
    return grid;
}
```

Damit hast du zum ersten Mal eine Methode, die ein zweidimensionales Array
**zurückgibt**. Der Rückgabetyp ist genau der Typ der Variablen: `boolean[][]`.

Jetzt reicht es für ein erstes Bild. In `main`, nach den acht Fensterzeilen:

```java
boolean[][] grid = glider();
draw(picture, grid);
label.repaint();
```

Übersetzen, starten, und die fünf Zellen stehen links oben im Fenster. Bewegen wird
sich noch nichts — die Regeln kommen erst.

![Der Gleiter nach 48 Generationen: fünf Zellen, diagonal gewandert](../assets/glider.png)

Fünf grüne Quadrate auf einem großen dunklen Feld sind nicht viel. Genau das ist
ihr Wert: Der Gleiter ist das Muster, an dem du prüfen kannst, ob deine Regeln
stimmen, weil man bei fünf Zellen jede einzelne nachzählen kann. Was die Regeln
wirklich können, siehst du weiter unten unter „Die Suppe".

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
   0 oder mindestens `COLS`. `continue` ist für beides ein Werkzeug; die fertige
   Fassung in `code/` benutzt es nur für die Mitte und erledigt die Grenzen in
   einer zusammengesetzten Bedingung. Beides ist richtig.
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

Bau es einmal falsch und schau zu, was passiert. Der Gleiter wandert nicht und
verschmiert nicht; er zerfällt. Fünf Zellen, dann drei, dann vier, und ab
Generation 2 ein erstarrter Zweier-Block, der sich nie wieder ändert. Das
R-Pentomino weiter unten, das über tausend Generationen in Bewegung bleiben
sollte, ist nach vier Generationen fertig.

Das Symptom ist also laut, und das ist ein Glück. Leise ist die Ursache: Am Code
sieht nichts falsch aus, jede Regel ist richtig umgesetzt, und der Fehler steckt
allein darin, wann geschrieben wird im Verhältnis zum Lesen. Deshalb lohnt es sich,
ihm absichtlich zu begegnen, statt darüber zu lesen.

Jetzt die Schleife in `main`:

```java
for (int generation = 0; generation <= GENERATIONS; generation++) {
    draw(picture, grid);
    label.repaint();

    System.out.println("Generation " + generation + ": "
            + countAlive(grid) + " live cells");

    grid = nextGeneration(grid);
}
```

`grid = nextGeneration(grid)` richtet die Variable auf das neue Gitter, und das
alte wird schlicht vergessen.

Die Zeile in die Konsole ist kein Rest aus der alten Fassung. Ein Fenster zeigt
immer nur die Gegenwart, und sobald etwas schiefläuft, willst du Generation 3 mit
Generation 7 vergleichen können. Die Konsole behält die Spur, das Fenster zeigt das
Bild. `countAlive` musst du noch schreiben; eine Doppelschleife, die `true` zählt:

```java
static int countAlive(boolean[][] grid) {
    // dein Code hier
}
```

Genau diese Zeile ist die Probe für den Gleiter: In jeder Generation stehen dort
fünf lebende Zellen, und zwar jedes Mal. Vergleich dazu im Fenster Generation 0 und
Generation 4 — dieselbe Form, ein Feld tiefer und eines weiter rechts.

## 🟢 Kern — Eine Pause zwischen den Generationen

Hundert Generationen kommen schneller, als du zusehen kannst. Java kann warten:

```java
Thread.sleep(PAUSE_MS);
```

Achtzig Millisekunden. Setz das ans Ende der Schleife, und der Compiler weigert
sich, dein Programm zu bauen:

```
GameOfLife.java:17: error: unreported exception InterruptedException; must be caught or declared to be thrown
            Thread.sleep(PAUSE_MS);
                        ^
1 error
```

Die Zeilennummer hängt davon ab, wohin du es setzt. Beachte außerdem, dass `Thread`
kein `import` gebraucht hat: Eine Handvoll Klassen, die aus `java.lang`, sind immer
verfügbar, und `Thread`, `String`, `Math` und `System` gehören dazu. Alles andere
braucht die Importzeile, die du beim `Scanner` kennengelernt hast.

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

Und eine Zeile hinter die Schleife, sonst kommt dein Terminal nie zurück:

```java
System.out.println("Done.");
System.exit(0);
```

Ein sichtbares Fenster hält das Programm am Leben, auch wenn `main` zu Ende ist:
Swing führt einen eigenen Thread, und der zählt nicht als Hintergrund-Thread. Ohne
`System.exit(0)` läuft die JVM nach der letzten Generation weiter, der Prompt kommt
nicht zurück, und du musst abwürgen oder das Fenster schließen. Die Null heißt „ohne
Fehler". Das `setDefaultCloseOperation` von oben regelt nur das Schließkreuz, nicht
diesen Fall.

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

Lass den Gleiter lange genug laufen, um das zu sehen. Gemessen auf dem Gitter
dieses Kapitels: Er verlässt den Rand bei Generation 105, kommt oben bei 112 wieder
herein und steht bei 480 wieder auf seinem Startfeld. Mit den eingestellten 100
Generationen siehst du also noch gar nichts -- setz `GENERATIONS` auf mindestens
120.

Eine Sache, bei der du danach aufpassen musst: Der Prüfwert aus der Kernübung,
`countNeighbours(grid, 0, 0)` gleich 0, ist mit Umlaufen weiterhin 0, aber nicht
mehr aus demselben Grund. Eckzellen haben jetzt acht Nachbarn wie alle anderen,
und die Antwort ist nur deshalb 0, weil der Gleiter zufällig weit weg von ihnen
lebt. Eine alte Prüfung weiterzuverwenden, nachdem sich geändert hat, was sie
prüft, ist der Weg, auf dem ein grünes Ergebnis aufhört, etwas zu bedeuten.

## 🟡 Optional — Andere Muster

Ersetz den Gleiter und schau, was die vier Regeln sonst so tun.

Ein **Blinker** sind drei Zellen in einer Reihe. Er kippt ewig zwischen waagerecht
und senkrecht und ist damit das kleinste Ding, das sich wiederholt.

Ein **Block** ist ein Quadrat aus zwei mal zwei Zellen. Jede Zelle darin hat genau
drei Nachbarn, also bleibt er unverändert liegen.

Ein **R-Pentomino** sind fünf Zellen in einem kleinen Zickzack. Bezogen auf eine
Zeile `r` und eine Spalte `c` in der Mitte des Gitters sind das `(r, c)`,
`(r, c+1)`, `(r+1, c-1)`, `(r+1, c)` und `(r+2, c)`.

Es ist berühmt dafür, aus fast nichts sehr lange in Bewegung zu bleiben, aber wie
lange, hängt stark davon ab, wie viel Platz es hat, denn die Wände töten, was sie
erreicht. Hier gemessen, gezählt bis sich der Zustand des ganzen Gitters wiederholt:
80 Generationen auf 20 mal 40, 434 auf 30 mal 60, 652 auf 60 mal 120 und 1103 auf
80 mal 160. Darüber hinaus ändert mehr Platz nichts mehr; bei 100 mal 200 und bei
120 mal 240 kam dieselbe 1103 heraus.

Gezählt ist dabei die Generation, in der der Endzyklus **beginnt**. Ein Erkenner,
wie ihn die rote Herausforderung unten baut, schlägt zwei Generationen später an,
weil er den Zustand erst wiedersehen muss, um ihn als Wiederholung zu erkennen. Wer
80 erwartet und 82 abliest, hat also nichts falsch gemacht.

Nimm die Zahlen als Größenordnung und nicht als Eigenschaft des Musters. Sie hängen
davon ab, wo genau du es hinsetzt, weil jede Wand, die es früher erreicht, einen
anderen Teil davon tötet.

Für „über tausend" brauchst du also ungefähr 80 mal 160 Zellen. Setz `CELL` dafür
auf 6, dann ist das Fenster 960 mal 480 Pixel groß und passt auf jeden Bildschirm.
In einem Terminal wäre dieses Gitter nicht darstellbar gewesen, und das ist der
handfeste Gewinn des Fensters: nicht, dass es hübscher ist, sondern dass es Größen
zulässt, bei denen die interessanten Muster überhaupt erst Platz haben.

## 🟡 Optional — Die Suppe

Ein Gleiter zeigt, dass deine Regeln stimmen. Was sie wert sind, zeigt er nicht.

Füll das Gitter stattdessen zufällig, etwa drei Zellen von zehn:

```java
static boolean[][] randomGrid() {
    boolean[][] grid = new boolean[ROWS][COLS];
    for (int row = 0; row < ROWS; row++) {
        for (int col = 0; col < COLS; col++) {
            grid[row][col] = Math.random() < 0.3;
        }
    }
    return grid;
}
```

Das Bild oben am Kapitel ist genau das, nach sechzig Generationen. Aus dem Rauschen
werden in wenigen Dutzend Generationen Blöcke, die liegenbleiben, Blinker, die
kippen, und gelegentlich ein Gleiter, der sich loslöst und davonzieht. Niemand hat
das hineinprogrammiert; es sind dieselben vier Regeln.

Die fertige Fassung hat beide Starts drin und wählt über ein Argument aus:

```java
boolean soup = args.length > 0 && "soup".equals(args[0]);
boolean[][] grid = soup ? randomGrid() : glider();
```

```
java GameOfLife        der Gleiter
java GameOfLife soup   das Zufallsgitter
```

`args` ist der Parameter, den `main` seit Kurs 1 mitschleppt, ohne je gebraucht zu
werden. Hier verdient er sich sein Dasein.

Die Reihenfolge in der Bedingung ist nicht beliebig. `args.length > 0` muss
**vorn** stehen, denn `&&` wertet die rechte Seite gar nicht erst aus, wenn die
linke schon falsch ist. Dreh es um, und ein Start ohne Argument endet mit
`ArrayIndexOutOfBoundsException: Index 0 out of bounds for length 0`. Dass hier
`"soup".equals(args[0])` steht und nicht `args[0].equals("soup")`, ändert daran
übrigens nichts — gemessen fliegen beide. Diese Schreibweise hilft gegen `null`,
und ein Kommandozeilenargument ist nie `null`.

Lass die Zahl 0.3 nicht stehen. Bei 0.05 passiert fast nichts, bei 0.6 stirbt fast
alles sofort an Überbevölkerung. Irgendwo dazwischen liegt das Interessante, und das
selbst zu finden ist die eigentliche Übung.

## 🔴 Optional, echte Herausforderung — Merken, wenn sich nichts mehr tut

Viele Startmuster kommen zur Ruhe. Ein Gitter, das sich nicht mehr ändert, druckt
weiter dasselbe Bild, bis die Schleife ausgeht, und das ist Zeitverschwendung für
alle Beteiligten.

Hör früher auf, wenn eine Generation mit der vorherigen übereinstimmt.

```java
static boolean same(boolean[][] a, boolean[][] b) {
    // dein Code hier -- antworte, ob jedes Feld übereinstimmt
}
```

`same` ist die leichte Hälfte. Die Arbeit ist die Schleife in `main`, die derzeit
keinen Platz für den Vergleich hat: `grid = nextGeneration(grid)` überschreibt das
alte Gitter im selben Atemzug, in dem es das neue baut, es bleibt also nichts zum
Vergleichen übrig. Nimm sie auseinander -- halt das neue Gitter in einer lokalen
Variablen, vergleich, `break` wenn sich nichts geändert hat, und weis erst dann zu.

Ein Vergleich mit `==` tut nicht, was du willst: Kurs 1 hat das für Text erklärt,
und hier gilt derselbe Grund. `a == b` fragt, ob es dasselbe Gitter ist, nicht ob
beide dasselbe enthalten. Du musst beide durchlaufen und Feld für Feld vergleichen.

Und jetzt der ehrliche Teil. Das fängt ein eingefrorenes Gitter, und es fängt
keinen Blinker, der zwischen zwei Bildern wechselt und nie zur Ruhe kommt. Probier
es an einem Blinker aus und sieh zu, wie er bis zum Ende läuft.

Um das auch zu fangen, heb zusätzlich die vorletzte Generation auf und vergleich
gegen beide.

Das bringt seine eigene Falle mit. In Generation 0 gibt es keine vorletzte, die
Variable dafür enthält also noch `null`, und `null` an `same` zu übergeben liefert
dir `NullPointerException: Cannot load from object array`. Kurs 1 hat `null` bei
Text getroffen, wo der bekannte Wert nach vorn gestellt wurde; diesen Trick gibt es
für Arrays nicht. Hier prüfst du, bevor du fragst:
`if (twoAgo != null && same(next, twoAgo))`. Das `&&` hört auf, sobald die linke
Hälfte falsch ist, `same` wird also nie mit leeren Händen erreicht.

Sei genau dabei, was „vorletzte Generation" heißt, denn hier steckt eine zweite
Falle ohne Fehlermeldung. Gemeint ist zwei Schritte hinter **`next`**, nicht hinter
dem aktuellen Gitter. Am Ende der Schleife schiebst du also weiter:
`twoAgo = grid;` und danach `grid = next;`. Liest du es als zwei Schritte hinter
`grid`, baust du einen Erkenner für Periode **drei**: Der Blinker läuft dann
durch, bis die Generationen ausgehen, und ein Muster mit Periode drei bricht ab.
Genau andersherum als gewollt, und nichts sagt dir etwas.

Überleg dir danach, was ein Muster mit Periode drei mit deinem fertigen Programm
machen würde, und entscheide, ob dich das stört. Genau zu wissen, welche Fälle deine
Prüfung verfehlt, ist mehr wert als eine Prüfung, die behauptet, alles zu fangen.

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
- Ein Fenster, das etwas zeigt: `BufferedImage` zum Bemalen, `JLabel` zum Anzeigen,
  `JFrame` als Rahmen, `pack` statt geratener Größen
- `createGraphics`, `setColor` und `fillRect`, und Farben als drei Zahlen
- Dass Malen allein nichts bewirkt und erst `repaint()` das Bild auf den Schirm holt
- Dass ein Fenster zum Zeigen keine eigene Klasse braucht, eines für Tastendrücke
  dagegen schon
- Dass ein Fenster nur die Gegenwart kennt, und warum die Spur trotzdem in die
  Konsole gehört
- *(optional)* `(x + SIZE) % SIZE` zum Umlaufen eines Index, und warum das
  `+ SIZE` nötig ist
- *(optional)* `args` endlich benutzt, und warum `args.length > 0` vor dem Zugriff
  stehen muss
- *(optional)* Zwei Gitter Feld für Feld vergleichen, und wissen, was die Prüfung
  verfehlt
- *(optional)* `null` in einer Array-Variablen, und mit `&&` absichern, bevor man
  fragt

## Weiter

Kurs 5 ist 2048, wo ein Gitter sich nicht von allein entwickelt, sondern unter vier
Tasten schiebt und verschmilzt, und wo die Verschmelzungsregel alle hereinlegt. Er
setzt Kurs 1 voraus und sonst nichts.

Die vollständige Liste steht in
[PROJECT-IDEAS.de.md](../../../PROJECT-IDEAS.de.md).
