🇩🇪 Deutsch | 🇬🇧 [English](../en/01-flappy-bird.md)

[← Zurück zur Kursübersicht](../../../README.de.md) · Setzt voraus: [Kurs 1 – Grundlagen](../../01-basics/de/00-einfuehrung.md)

# Kurs 7 – Flappy Bird

**Ziel:** eine Taste, Schwerkraft, und eine Lücke, auf die man zielt. Der letzte
Kurs in diesem Repository, und der erste, in dem Bewegung kein Schritt auf einem
Gitter ist, sondern eine Geschwindigkeit, die sich in jedem Bild ändert.

![Der Vogel im Flug zwischen zwei Röhren, Punktestand 7](../assets/gameplay.png)

Setzt [Kurs 1](../../01-basics/de/00-einfuehrung.md) voraus und sonst nichts. Wenn
du Kurs 6 gemacht hast, kommen dir die nächsten beiden Abschnitte bekannt vor und
du kannst sie überspringen. Eine fertige Fassung liegt in [`code/`](../code/), in
drei Dateien.

## 🟢 Kern — Klassen und Objekte, kurz erklärt

*(Wenn du Kurs 6 gemacht hast, überspring das und lies beim nächsten Abschnitt
weiter.)*

Kurs 1 nannte eine Klasse „eine Schachtel mit Namen". Die vollständige Antwort ist,
dass eine Klasse an einer Stelle beschreibt, was etwas **ist** und was es **kann**:
die Werte, die es enthält, genannt **Felder**, und die Methoden, die damit
arbeiten. `new` baut ein echtes daraus, ein **Objekt**, und jedes Objekt hat seine
eigenen Felder.

Dieses Spiel braucht das gleich zweimal, aus zwei verschiedenen Gründen.

Das Panel braucht es, weil Höhe, Geschwindigkeit und Punktestand des Vogels
zwischen zwei Bildern überleben müssen, und eine `static`-Methode aus Kurs 1 hat
keinen Ort, an dem sie etwas aufbewahren könnte.

Die Röhren brauchen es, weil es drei davon gibt und alle dieselbe Art Ding mit
anderen Zahlen darin sind. Ohne Klasse würdest du mit `pipeX1`, `pipeX2`, `pipeX3`,
`pipeGapY1` und so weiter jonglieren, und jedes Stück Code, das eine Röhre anfasst,
müsste dreimal sagen, welche gemeint ist.

Drei weitere Wörter kommen unten vor. `private` an einem Feld heißt, dass nur diese
Klasse es anfassen darf. `final` an einer Klasse heißt, dass niemand eine
Abwandlung darauf aufbauen darf, und das hält hier eine Warnung ruhig, die im
nächsten Abschnitt erklärt wird. Und `extends` heißt „fang bei dieser Klasse an und
ändere eine Sache".

## 🟢 Kern — Die drei Swing-Tatsachen, die dieses Spiel braucht

*(Wenn du Kurs 6 gemacht hast, überspring das und lies beim nächsten Abschnitt
weiter.)*

Swing ist Javas Werkzeugkasten für Fenster. Dieses Kapitel benutzt vier seiner
Klassen und ignoriert den Rest.

**Etwas, worauf man zeichnet.** Ein `JFrame` ist das Fenster, und darauf zeichnest
du nicht. Du setzt ein `JPanel` hinein, und dein eigenes baust du mit
`extends JPanel` und einer überschriebenen Methode:

```java
public final class FlappyPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GREEN);
        g.fillRect(60, 0, 60, 180);
    }
}
```

`super.paintComponent(g)` lässt das Original zuerst den Hintergrund löschen.
`Graphics g` zeichnet: `fillRect` für die Röhren, `fillOval` für den Vogel,
`drawString` für den Punktestand. Die Koordinaten sind Pixel von links oben, und
**y wächst nach unten**, was hier mehr zählt als in jedem anderen Kurs, denn das
ist die Richtung, in die die Schwerkraft zieht.

Die Klasse ist mit Absicht `final`. Ein Panel, das sich von außen erweitern lässt,
bringt den Compiler dazu, vor einem entwischenden `this` zu warnen, bevor das
Objekt fertig ist, und dieses Repository behandelt Warnungen als Fehler.

**Etwas, das neu zeichnet.** `paintComponent` rufst du nie. Du rufst `repaint()`,
und Swing ruft `paintComponent`, wenn es dazu kommt. Die Regel lautet also: erst
die Zahlen ändern, dann um ein neues Bild bitten.

**Etwas, das antreibt.** Ein Spiel, in dem etwas fällt, braucht einen gleichmäßigen
Herzschlag. Das ist ein `javax.swing.Timer`, der dich in festem Abstand zurückruft:

```java
new Timer(16, event -> {
    panel.tick();
    panel.repaint();
}).start();
```

Sechzehn Millisekunden sind etwa sechzig Bilder pro Sekunde, und das lässt Fallen
nach Fallen aussehen statt nach Springen. Eine `while`-Schleife mit `Thread.sleep`
täte es nicht: Swing zeichnet auf demselben Thread neu, ein schlafendes Spiel ist
also ein eingefrorenes Fenster.

Importier `javax.swing.Timer` namentlich. Es gibt auch `java.util.Timer`, und beide
mit Stern zu importieren macht den Namen mehrdeutig.

## 🟢 Kern — Bewegung als Geschwindigkeit, nicht als Schritt

Jedes Spiel bisher hat Dinge um ganze Felder bewegt. Hier hat der Vogel eine
**Geschwindigkeit**, und zwei Zeilen tragen die gesamte Physik:

```java
velocity = velocity + GRAVITY;
birdY = birdY + velocity;
```

Die Schwerkraft bewegt den Vogel nicht. Sie ändert, wie schnell er sich bewegt, und
die Geschwindigkeit bewegt ihn. Fallen beginnt deshalb langsam und wird von allein
schneller, und genau das tut Fallen.

Ein Flattern ist eine Zuweisung:

```java
velocity = FLAP;      // eine negative Zahl: nach oben
```

Nicht „geh nach oben", sondern „du bewegst dich ab jetzt mit dieser Geschwindigkeit
nach oben". Die Schwerkraft knabbert sofort daran, der Vogel steigt also, wird
langsamer, bleibt stehen und fällt wieder, ohne dass eine einzige Zeile diesen
Bogen beschreibt. Zwei Konstanten erzeugen ihn:

```java
static final double GRAVITY = 0.5;
static final double FLAP = -8.0;
```

`birdY` und `velocity` sind beide `double`, und das ist keine Pingeligkeit. Mit
einem `int birdY` wirft die Zeile `birdY += velocity` in jedem Bild still die
Nachkommastellen weg, die Bewegung wird ruckelig, und der Compiler warnt vor einer
verlustbehafteten Umwandlung, was dieses Repository als Fehler behandelt. Lass
beide `double` und wandle einmal um, ganz am Ende, wo das Zeichnen ganze Pixel
braucht:

```java
g.fillOval(BIRD_X, (int) birdY, BIRD_SIZE, BIRD_SIZE);
```

Der Vogel bewegt sich nie zur Seite. Die Röhren kommen zu ihm, und das ist weniger
Arbeit und sieht gleich aus.

## 🟢 Kern — Eine Klasse, die nur Daten ist

Drei Röhren gibt es für das ganze Spiel. Verlässt eine links das Bild, wandert sie
mit einer neuen Lücke zurück nach rechts, statt weggeworfen und ersetzt zu werden.
Das Programm erzeugt also drei Objekte und keines mehr.

```java
public final class Pipe {

    int x;
    int gapY;
    boolean passed;

    Pipe(int x, int gapY) {
        this.x = x;
        this.gapY = gapY;
    }

    void recycle(int x) {
        this.x = x;
        this.gapY = FlappyPanel.MARGIN + (int) (Math.random()
                * (FlappyPanel.HEIGHT - FlappyPanel.GAP - 2 * FlappyPanel.MARGIN));
        this.passed = false;
    }
}
```

Die Konstanten liegen in `FlappyPanel`, `Pipe` muss also dazusagen, zu welcher
Klasse sie gehören. Innerhalb von `FlappyPanel` schreibst du `MARGIN`, von außen
`FlappyPanel.MARGIN`. Das ist dieselbe Regel, die dich `Math.random()` schreiben
lässt und nicht `random()`.

`x` ist die linke Kante, `gapY` die Oberkante des Lochs, und `passed` merkt sich,
ob diese Röhre schon gezählt wurde, damit eine Röhre nicht zweimal punktet.

`this.x = x` sieht beim ersten Mal seltsam aus. Parameter und Feld heißen gleich,
also sagt `this.` „das Feld, nicht der Parameter". Du könntest sie unterschiedlich
benennen und darauf verzichten; die Konvention gibt es, weil die beiden wirklich
dasselbe unter zwei Hüten sind.

Das kommt in eine eigene Datei, `Pipe.java`. Eine zweite Klasse, die sich
`FlappyPanel.java` teilt, wäre dort versteckt, und der Compiler warnt vor einer
Klasse, die von anderswo benutzt wird und nicht in einer Datei ihres Namens liegt.

Halt sie in einem Array:

```java
private final Pipe[] pipes = new Pipe[3];
```

`new Pipe[3]` schafft Platz für drei Röhren und keine Röhre. Jeder Platz muss noch
mit `new Pipe(...)` gefüllt werden, und das ist der eine Unterschied zu den
`int[]`-Arrays aus Kurs 1, wo die Plätze schon mit Nullen ankamen.

## 🟢 Kernübung — Ein Bild

```java
void tick() {
    // dein Code hier -- die fünf Schritte unten
}
```

1. Ist das Spiel vorbei oder noch nicht gestartet, kehr zurück und tu nichts.
2. **Schwerkraft.** Addier `GRAVITY` auf die Geschwindigkeit, danach die
   Geschwindigkeit auf `birdY`. In dieser Reihenfolge: Die neue Geschwindigkeit
   gilt für dieses Bild.
3. **Röhren.** Zieh von jeder Röhre `PIPE_SPEED` von ihrem `x` ab.
4. **Recyceln.** Ist die rechte Kante einer Röhre links an der 0 vorbei, schieb sie
   zurück nach rechts und gib ihr eine neue Lücke. Setz sie
   `pipes.length * PIPE_SPACING` weiter rechts als dort, wo sie gerade ist, damit
   sie hinter den anderen beiden landet und nicht auf einer.
5. **Punkte.** Ist eine Röhre noch nicht gezählt und ihre rechte Kante jetzt links
   vom Vogel, markier sie als gezählt und zähl einen Punkt dazu.

Schritt 4 und 5 fragen beide „wo ist die rechte Kante", und das ist
`pipe.x + PIPE_WIDTH` und nicht `pipe.x`. Wer das verwechselt, bekommt den Punkt
eine Röhrenbreite zu früh und das Recyceln eine Röhrenbreite zu spät, und beides
sieht man beim Zuschauen nicht.

Schritt 5 ist der Grund, warum es `passed` gibt. Ohne das zählt jedes Bild, in dem
der Vogel hinter der Röhre ist, einen weiteren Punkt, und der Punktestand rennt mit
sechzig pro Sekunde davon.

## 🟢 Kernübung — Hat er etwas getroffen

```java
private boolean collides() {
    // dein Code hier -- die beiden Teile unten
}
```

**Aus dem Bild.** Ist `birdY` unter 0 oder `birdY + BIRD_SIZE` jenseits von
`HEIGHT`, ist der Vogel draußen.

**In eine Röhre.** Jede Röhre sind zwei Rechtecke mit einem Loch dazwischen: ein
oberes von ganz oben bis `gapY`, und ein unteres von `gapY + GAP` bis ganz unten.
Der Vogel ist ein drittes Rechteck. Die Frage ist, ob sich das Rechteck des Vogels
mit einem der beiden überschneidet.

Java beantwortet das für dich:

```java
Rectangle bird = new Rectangle(BIRD_X, (int) birdY, BIRD_SIZE, BIRD_SIZE);
Rectangle upper = new Rectangle(pipe.x, 0, PIPE_WIDTH, pipe.gapY);

if (bird.intersects(upper)) {
```

`Rectangle` sind vier Zahlen: links, oben, Breite, Höhe. `intersects` beantwortet,
ob zwei davon sich irgendwo überschneiden.

Es lohnt sich zu wissen, was dabei passiert, statt es für Magie zu halten. Zwei
Rechtecke überschneiden sich, wenn sie sich waagerecht **und** senkrecht
überschneiden, und waagerecht überschneiden sie sich, wenn die linke Kante jedes
Rechtecks links von der rechten Kante des anderen liegt. Das sind vier Vergleiche
mit `&&` verbunden, und du könntest sie selbst schreiben. Die Bibliotheksfassung
hier zu nehmen heißt, eine Sache weniger zu haben, die man subtil falsch machen
kann, während man gleichzeitig Schwerkraft lernt.

Das untere Rechteck baust du selbst. Seine Oberkante ist `gapY + GAP`, und seine
Höhe ist, was vom Bild darunter übrig bleibt.

## 🟢 Kern — Starten, sterben, neu anfangen

Der Vogel soll nicht fallen, bevor der Spieler bereit ist, und ein beendetes Spiel
soll sich ohne Programmneustart wieder spielen lassen. Zwei `boolean`-Felder
erledigen beides:

```java
private boolean started;
private boolean gameOver;
```

`tick` tut nichts, solange `started` nicht wahr und `gameOver` nicht falsch ist.
Die Leertaste tut drei verschiedene Dinge, je nachdem, in welchem Zustand du bist:

```java
void flap() {
    if (gameOver) {
        reset();
        return;
    }
    started = true;
    velocity = FLAP;
}
```

Der erste Druck startet das Spiel und flattert. Spätere Drücke flattern. Ein Druck
nach dem Tod fängt von vorn an. `reset` setzt den Vogel in die Mitte, nullt
Geschwindigkeit und Punktestand, und recycelt alle drei Röhren auf ihre Startplätze
— und genau das braucht auch der Konstruktor, also schreib es einmal und ruf es von
beiden Stellen.

Die Verdrahtung in `FlappyBird.java` hat die Form jedes Swing-Programms: Panel
bauen, Wunschgröße geben, `KeyAdapter` anhängen, in ein Fenster setzen, `pack`,
zeigen, den Fokus **danach** anfordern, dann den Timer starten. Wenn du Kurs 6
gemacht hast, ist es dieselbe Datei mit anderen Namen.

```
javac *.java
java FlappyBird
```

Diesmal drei Dateien, also wieder `javac *.java`, und wieder nicht
`java FlappyBird.java`.

![Game over, der Vogel an einer Röhre gestoppt, Punktestand 12](../assets/game-over.png)

## 🟡 Optional — Röhren, die kommen und gehen

Drei recycelte Röhren sind die Fassung, die nichts über Kurs 1 hinaus braucht. Die
erwachsene Fassung erzeugt eine Röhre, wenn eine fällig ist, und entfernt sie, wenn
sie das Bild verlässt, und dafür willst du eine Liste statt eines Arrays, weil sich
die Anzahl während des Laufs ändert:

```java
import java.util.ArrayList;
import java.util.List;

private final List<Pipe> pipes = new ArrayList<>();
```

`add` hängt eine hinten an, `remove` nimmt eine heraus, `size()` ist die Anzahl, und
das erweiterte `for` aus Kurs 1 läuft unverändert darüber. Die spitzen Klammern
sagen, was darin liegt, der Compiler weiß also, dass eine `Pipe` herauskommt.

Eine Falle, und sie ist der Grund, warum das optional ist und nicht der Hauptweg:
Aus einer Liste zu entfernen, während eine `for`-Schleife darüber läuft, wirft
`ConcurrentModificationException`. Sammle, was weg soll, und entferne es danach,
oder lauf rückwärts über die Indizes.

## 🟡 Optional — Mach es fair

Drei Zahlen entscheiden, ob dieses Spiel angenehm oder zum Verzweifeln ist:
`GRAVITY`, `FLAP` und `GAP`.

Ändere eine nach der anderen und spiel nach jeder Änderung fünf Runden. Schreib
danach in einen Kommentar, warum deine Zahlen die sind, bei denen du geblieben
bist. Die nützliche Frage ist nicht „ist es schwer", sondern „glaube ich beim
Sterben, dass es meine Schuld war".

Lohnt sich auf Papier: Wie viele Bilder vergehen bei `GRAVITY` 0,5 und `FLAP` -8,
bis ein Flattern vollständig aufgezehrt ist? Die Antwort sagt dir, in welchem
Rhythmus das Spiel den Spieler tippen lässt, und ob dieser Rhythmus bei sechzig
Bildern pro Sekunde angenehm ist.

## 🔴 Optional, echte Herausforderung — Ein Punktestand, der das Programm überlebt

Heb den besten Punktestand in einer Datei neben dem Programm auf, lies ihn beim
Start, und schreib ihn, wenn eine Runde ihn schlägt.

```java
import java.nio.file.Files;
import java.nio.file.Path;
```

`Files.writeString(Path.of("highscore.txt"), String.valueOf(score))` schreibt, und
`Files.readString(Path.of("highscore.txt"))` liest zurück. Beides kann schiefgehen,
und anders als `Thread.sleep` in Kurs 4 musst du das hier behandeln statt
weiterzureichen, denn eine fehlende Datei ist beim ersten Start normal und darf das
Spiel nicht anhalten:

```java
static int readBest() {
    try {
        String text = Files.readString(Path.of("highscore.txt"));
        return Integer.parseInt(text.trim());
    } catch (IOException e) {
        return 0;
    }
}
```

`try` führt den riskanten Teil aus, `catch` sagt, was bei einem Fehlschlag zu tun
ist. Hier bedeuten „noch keine Datei" und „die Datei ist unlesbar" ehrlicherweise
beide „noch kein bester Punktestand", also liefern beide 0.

Drei Dinge zu testen, und das zweite ist das, was alle vergessen. Lösch die Datei
und starte das Spiel. Schreib das Wort `banane` hinein und starte das Spiel. Und
schlag deinen eigenen Rekord zweimal in einer Sitzung, damit das Schreiben mehr als
einmal passiert.

## 🔴 Optional, echte Herausforderung — Boden, der sich bewegt

Der Himmel ist leer, es zeigt also nichts, wie schnell der Vogel unterwegs ist.
Zeichne unten einen Bodenstreifen und lass ihn mit `PIPE_SPEED` nach links
scrollen.

Der Kniff ist, ihn endlos zu machen ohne endlos viele Striche. Zeichne sie in
festem Abstand, halt einen Versatz, der vom Abstand auf 0 herunterzählt und dann
zurückspringt, und zeichne ab `-abstand`, damit ein von rechts kommender Strich
schon im Bild ist, wenn er gebraucht wird.

Zwei Sätze Warnung. Der Boden ist Dekoration, lass den Vogel also nicht mit ihm
kollidieren; er stirbt ohnehin bei `HEIGHT`. Und wenn du den Boden hoch machst,
zieh diese Todeslinie auf die Oberkante des Bodens hoch, sonst versinkt der Vogel
sichtbar darin, bevor das Spiel es merkt.

## Was du gelernt hast

- Bewegung als Geschwindigkeit, die die Schwerkraft ändert, statt als fester Schritt
- Warum `birdY` und `velocity` `double` sind, und was der Compiler sagt, wenn nicht
- Einmal nach `int` umwandeln, an der Stelle des Zeichnens
- Eine Klasse, deren Aufgabe das Halten von Daten ist, und `this.x = x` bei
  gleichen Namen
- Ein Array von Objekten, bei dem `new Pipe[3]` Platz schafft, aber keine Röhren
- Objekte recyceln, statt sie zu erzeugen und wegzuwerfen
- `Rectangle` und `intersects`, und für welche vier Vergleiche sie stehen
- Eine Markierung wie `passed`, damit etwas einmal passiert statt in jedem Bild
- Zwei Booleans für „nicht gestartet" und „vorbei", und eine Taste mit drei
  Bedeutungen
- Eine `reset`-Methode, die aus dem Konstruktor und aus dem Neustart gerufen wird
- *(optional)* `ArrayList`, und warum Entfernen während einer Schleife wirft
- *(optional)* `try` und `catch`, und eine fehlende Datei als Normalfall

## Weiter

Das war der letzte Kurs in diesem Repository. Sieben Spiele, von einem Spielfeld
aus Strichen bis zu einem Vogel, der mit sechzig Bildern pro Sekunde fällt, und
installiert ist nichts außer dem JDK.

Was danach kommt, steht in
[PROJECT-IDEAS.de.md](../../../PROJECT-IDEAS.de.md): Pong, Minesweeper, Breakout
und Tetris liegen alle in Reichweite dessen, was du jetzt kannst, und der letzte
Eintrag ist der, der das blanke JDK für ein Build-Werkzeug und echte Tests
verlässt. Geschrieben ist noch keiner davon. Die Liste steht da, weil das nächste
selbst gebaute Spiel mehr bringt als ein weiteres Kapitel zum Lesen.
