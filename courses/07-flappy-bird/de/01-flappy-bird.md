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

*(Kommst du aus Kurs 6, kennst du das schon und kannst weiterblättern.)*

Kurs 1 ließ es bei „eine Schachtel mit Namen" bewenden. Vollständig ist die
Antwort erst so: Eine Klasse hält an einer Stelle fest, woraus etwas besteht und
was man damit tun kann. Das eine sind ihre **Felder**, das andere ihre Methoden.
`new` baut ein echtes Exemplar daraus, ein **Objekt**, und jedes Objekt hat seine
eigenen Felder.

Dieses Spiel braucht das gleich zweimal, aus zwei verschiedenen Gründen.

Das Panel braucht es, weil Höhe, Geschwindigkeit und Punktestand des Vogels
zwischen zwei Bildern überleben müssen, und eine `static`-Methode aus Kurs 1 hat
keinen Ort, an dem sie etwas aufbewahren könnte.

Die Röhren brauchen es, weil es drei davon gibt und alle dieselbe Art Ding mit
anderen Zahlen darin sind. Ohne Klasse würdest du mit `pipeX1`, `pipeX2`, `pipeX3`,
`pipeGapY1` und so weiter jonglieren, und jedes Stück Code, das eine Röhre anfasst,
müsste dreimal sagen, welche gemeint ist.

Vier Wörter aus dem Code unten hat Kurs 1 noch nicht gebraucht.

`extends` setzt deine Klasse auf eine bestehende obendrauf. `FlappyPanel` kann
deshalb ab seiner ersten Zeile alles, was ein `JPanel` kann, und im Rumpf steht
nur noch der Unterschied.

`@Override` sagt dem Compiler, dass die Methode darunter eine geerbte ersetzt und
keine neue ist. Freiwillig, aber nützlich: Schreibst du den Namen falsch
(`paintcomponent` mit kleinem C), legst du ohne `@Override` stillschweigend eine
zweite Methode an, die niemand ruft, und dein Fenster bleibt leer. Mit `@Override`
bricht der Compiler ab und sagt dir, warum.

`private` an einem Feld sperrt es für alles außerhalb dieser Klasse. `final` an
einer Klasse verbietet, von ihr zu erben; weshalb es hier steht, kommt im nächsten
Abschnitt an der Stelle, an der es auffällt.

## 🟢 Kern — Die vier Swing-Tatsachen, die dieses Spiel braucht

*(Auch das stand schon in Kurs 6; wer es kennt, springt zum nächsten Abschnitt.)*

Swing ist Javas Werkzeugkasten für Fenster, und er sitzt auf dem älteren AWT auf.
Aus Swing selbst kommen hier drei Klassen: `JFrame`, `JPanel` und `Timer`. Alles,
was zeichnet oder Tasten liest, stammt aus AWT. Deshalb stehen unten
`java.awt`-Importe neben `javax.swing`-Importen, und das ist kein Versehen.

**Etwas, worauf man zeichnet.** Das Fenster selbst, ein `JFrame`, nimmt keine
Zeichenbefehle. Es nimmt ein `JPanel` auf, und dein eigenes entsteht aus
`extends JPanel` und einer überschriebenen Methode:

```java
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public final class FlappyPanel extends JPanel {

    static final int WIDTH = 400;
    static final int HEIGHT = 600;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GREEN);
        g.fillRect(60, 0, 60, 180);
    }
}
```

Mehr braucht die Datei nicht. Tipp sie ab, speicher sie als `FlappyPanel.java`
und lass `javac FlappyPanel.java` darüber laufen: sie übersetzt. Zum Starten fehlt
ihr `main`, und ein Fenster öffnet sie auch noch nicht.

`super.paintComponent(g)` lässt das Original zuerst den Hintergrund löschen.
`Graphics g` zeichnet: `fillRect` für die Röhren, `fillOval` für den Vogel,
`drawString` für den Punktestand. Gezählt wird in Pixeln ab der linken oberen
Ecke, und **y wächst nach unten**, was hier mehr zählt als in jedem anderen Kurs,
denn das ist die Richtung, in die die Schwerkraft zieht.

`protected` vor `paintComponent` heißt „für mich und für jeden, der von mir erbt,
aber nicht für die Allgemeinheit". Das ist nicht deine Entscheidung, sondern die
von `JPanel`: Eine überschriebene Methode darf sich weiter öffnen als das
Original, aber nicht enger werden, und `protected` ist die bequemste Antwort.

Das `final` vor der Klasse ist Vorsicht, kein Muss. Weiter unten ruft der
Konstruktor `setBackground`, und diese Methode stammt von `JPanel`. Erbte jemand
von `FlappyPanel` und ersetzte `setBackground`, liefe seine Fassung zu einem
Zeitpunkt, an dem seine eigenen Felder noch gar nicht angelegt sind. Der Compiler
sieht das, hält den Mund aber so lange, wie es nicht eintreten kann; nötig sind
dafür zwei Dinge auf einmal: keine `final`-Klasse **und** ein Konstruktor, der von
außerhalb erreichbar ist. Probier beides nach:

```
nimm das final weg                            -> still, keine Warnung
nimm es weg und schreib public vor FlappyPanel() ->
warning: [this-escape] possible 'this' escape before subclass is fully initialized
```

Vor dem Konstruktor steht hier kein Sichtbarkeitswort, er bleibt also im eigenen
Paket, und damit schweigt der Compiler selbst ohne `final`. Trotzdem steht es da:
Es räumt die Gefahr dauerhaft ab, selbst gegen den Fall, dass später jemand ein
`public` davorsetzt, um das Panel von anderswo zu erzeugen. Dieses Repository
übersetzt mit Warnungen als Fehlern, und eine Warnung, die erst eine spätere,
harmlos aussehende Änderung hervorholt, kostet mehr Zeit als das eine Wort.

**Etwas, das neu zeichnet.** Der Aufruf von `paintComponent` ist nicht deine
Sache. Deine Seite ist `repaint()`; `paintComponent` holt Swing sich selbst, wenn
es dazu kommt. Die Regel lautet also: erst die Zahlen ändern, dann um ein neues
Bild bitten.

**Etwas, das antreibt.** Ein Spiel, in dem etwas fällt, braucht einen gleichmäßigen
Herzschlag. Das ist ein `javax.swing.Timer`, der dich in festem Abstand zurückruft:

```java
import java.awt.event.ActionListener;
import javax.swing.Timer;

ActionListener step = event -> {
    panel.tick();
    panel.repaint();
};
new Timer(16, step).start();
```

Die Pfeilschreibweise ist eine namenlose Methode. `event -> { ... }` heißt „nimm
einen Parameter, nenn ihn `event`, und führ das hier aus". Dass der Name fehlen
darf, liegt an `ActionListener`: Die Schnittstelle schreibt genau eine Methode
vor, eine Verwechslung ist also ausgeschlossen. Als eigene Klasse, die
`ActionListener` implementiert, wären daraus zehn Zeilen geworden.

Die 16 Millisekunden sind eine Bitte, keine Zusage. Rechnerisch wären sie 62 Bilder
pro Sekunde; gemessen unter Windows kommen etwa 32 heraus, weil die Systemuhr dort
nur alle 15,6 Millisekunden weiterspringt und 16 deshalb auf gut 31 aufgerundet
wird. Das Spiel läuft damit gut, aber es verschiebt die Zahlen: `GRAVITY` und
`FLAP` müssen sich bei der Rate gut anfühlen, die dein Rechner wirklich liefert.
Miss sie einmal, wenn dir die Steuerung zäh vorkommt. Ein Zähler, der bei jedem
Tick hochgeht, und eine Ausgabe jede Sekunde genügen dafür.

Eine `while`-Schleife mit `Thread.sleep` täte es nicht: Der Thread, auf dem du
schläfst, ist derselbe, der das Bild erneuert, und ein schlafendes Spiel ist damit
ein eingefrorenes Fenster.

Schreib `javax.swing.Timer` im Import voll aus. Neben ihm existiert
`java.util.Timer`, und holt ein Stern beide herein, ist der kurze Name mehrdeutig.

**Etwas, das Tasten liest.** Ein Fenster bekommt die Taste im Moment des Drucks,
und genau das kann die Konsole nicht. Drei Dinge gehören dazu:

```java
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

panel.setFocusable(true);

panel.addKeyListener(new KeyAdapter() {
    @Override
    public void keyPressed(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_SPACE) {
            panel.flap();
        }
    }
});
```

`setFocusable(true)` erlaubt dem Panel, Tasten zu bekommen. Bei einem `JPanel` ist
das schon von sich aus erlaubt, die Zeile ändert hier also nichts; sie steht da,
weil es bei anderen Komponenten nicht so ist und man es dann lange sucht.
`addKeyListener` hängt den Horcher an, und ohne ihn kommt nichts an.

In `KeyAdapter` stehen drei Methoden ohne Inhalt; du besetzt davon die eine, die
dich interessiert. `new KeyAdapter() { ... }` zieht an Ort und Stelle eine
Abwandlung daraus, ohne ihr einen Namen zu geben: dieselbe Abkürzung wie beim
`event ->` oben, nur für eine Klasse mit mehreren Methoden. Der Rumpf in
geschweiften Klammern hinter dem `new` ist das, was `extends KeyAdapter` wäre,
wenn du dafür eine eigene Datei anlegen würdest.

`keyPressed` und nicht `keyTyped`. `keyTyped` gibt es nur für Tasten, die ein
Zeichen erzeugen, und es liefert kein `getKeyCode`. Die Leertaste erzeugt zwar
eines, Pfeiltasten nicht, und `keyPressed` funktioniert für beide. Die Tastennamen
sind Konstanten in `KeyEvent`: `VK_SPACE`, `VK_LEFT`, `VK_UP`.

## 🟢 Kern — Die Zahlen, die das Spiel ausmachen

Elf Konstanten stehen oben in der Klasse, und fast jedes Stück Code in diesem
Kapitel benutzt eine davon. Schreib sie ab, bevor du weitermachst, sonst findet
dein Compiler ab dem nächsten Abschnitt Namen nicht:

```java
static final int WIDTH = 400;
static final int HEIGHT = 600;

static final int GAP = 150;        // das Loch zwischen oberer und unterer Röhre
static final int MARGIN = 60;      // hält die Lücke von den Rändern weg
static final int PIPE_WIDTH = 60;
static final int PIPE_SPACING = 200;
static final int PIPE_SPEED = 3;

static final int BIRD_X = 100;
static final int BIRD_SIZE = 24;
static final double GRAVITY = 0.5;
static final double FLAP = -8.0;
```

Ihr Platz ist die Klasse selbst, nicht eine ihrer Methoden, und daraus folgt, dass
jede Methode sie lesen kann. Das `final int` aus Kurs 1 saß dagegen in einer
Methode und endete mit ihr. `static` heißt „gehört der Klasse, nicht einem
einzelnen Objekt", `final` heißt „ändert sich nie".

**Kein `private` davor.** Das ist keine Geschmacksfrage: `Pipe` aus dem Abschnitt
weiter unten liest `MARGIN`, `HEIGHT` und `GAP`, und mit `private` bekommst du
`MARGIN has private access in FlappyPanel`. Felder, die nur das Panel braucht —
`birdY`, `score`, `gameOver` —, dürfen und sollen `private` sein.

**Und lass keine der ersten beiden weg.** Vergisst du `HEIGHT`, sagt der Compiler
nichts. `JPanel` erbt über eine Schnittstelle namens `ImageObserver` selbst ein
`HEIGHT`, und das ist `2`; `WIDTH` ist dort `1`. Dein Spielfeld wäre also zwei
Pixel hoch, die Rechnung für die Lücke ergäbe -74, und ein Rechteck mit negativer
Höhe zeichnet `fillRect` nicht. Das Fenster bliebe leer, ohne eine einzige
Warnung. Diese Sorte Fehler findet man nur, wenn man weiß, dass es sie gibt.

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
Bogen beschreibt. Zwei der Konstanten von oben erzeugen ihn: `GRAVITY` und `FLAP`.

Beide Felder sind `double`, und das ist keine Pingeligkeit:

```java
private double birdY = HEIGHT / 2.0;
private double velocity;
```

Probier es mit `int birdY`, und du bekommst zwei verschiedene Antworten, je
nachdem, wie du die Zeile schreibst. Die von oben gilt dann gar nicht mehr:

```
error: incompatible types: possible lossy conversion from double to int
        birdY = birdY + velocity;
```

Die Kurzform `birdY += velocity` wird dagegen übersetzt, denn `+=` wandelt
stillschweigend nach `int` zurück. Sie wirft dabei in jedem Bild die
Nachkommastellen weg, die Bewegung ruckelt, und der Compiler sagt dazu nur:

```
warning: [lossy-conversions] implicit cast from double to int in compound
assignment is possibly lossy
```

Dieses Repository übersetzt mit `-Werror`, aus dieser Warnung wird hier also
ebenfalls ein Fehler. Bei dir ohne den Schalter ist es eine Zeile Text, die
durchrutscht, und dann suchst du das Ruckeln an der falschen Stelle. Lass beide
`double` und wandle einmal um, ganz am Ende, wo das Zeichnen ganze Pixel braucht:

```java
g.fillOval(BIRD_X, (int) birdY, BIRD_SIZE, BIRD_SIZE);
```

Der Vogel bewegt sich nie zur Seite. Die Röhren kommen zu ihm, und das ist weniger
Arbeit und sieht gleich aus.

## 🟢 Kern — Eine Klasse, die nur Daten ist

Drei Röhren gibt es für das ganze Spiel. Verlässt eine links das Bild, wandert sie
mit einer neuen Lücke zurück nach rechts, statt weggeworfen und ersetzt zu werden.
Das Programm erzeugt also drei Objekte und keines mehr.

Das kommt in eine eigene Datei, `Pipe.java`, und sie ist vollständig:

```java
public final class Pipe {

    int x;
    int gapY;
    boolean passed;

    Pipe(int x) {
        recycle(x);
    }

    void recycle(int x) {
        this.x = x;
        this.gapY = FlappyPanel.MARGIN + (int) (Math.random()
                * (FlappyPanel.HEIGHT - FlappyPanel.GAP - 2 * FlappyPanel.MARGIN));
        this.passed = false;
    }
}
```

Sie braucht keinen einzigen Import: `Math` liegt in `java.lang` und ist immer da.

`x` ist die linke Kante, `gapY` die Oberkante des Lochs, und `passed` merkt sich,
ob diese Röhre schon gezählt wurde, damit eine Röhre nicht zweimal punktet.

Eine Röhre zu platzieren gibt es nur an einer Stelle, deshalb benutzt der
Konstruktor dieselbe Methode wie das späte Recyceln. Dass ein Konstruktor eine
eigene Methode ruft, ist hier unbedenklich, weil die Klasse `final` ist: niemand
kann `recycle` unter ihr austauschen. Das ist genau die Gefahr aus dem
Swing-Abschnitt, hier aber von der nützlichen Seite.

Die Konstanten liegen in `FlappyPanel`, `Pipe` muss also dazusagen, zu welcher
Klasse sie gehören. Innerhalb von `FlappyPanel` schreibst du `MARGIN`, von außen
`FlappyPanel.MARGIN`. Das ist dieselbe Regel, die dich `Math.random()` schreiben
lässt und nicht `random()`.

`this.x = x` sieht beim ersten Mal seltsam aus. Parameter und Feld heißen gleich,
also sagt `this.` „das Feld, nicht der Parameter". Du könntest sie unterschiedlich
benennen und darauf verzichten; die Konvention gibt es, weil die beiden dasselbe
unter zwei Hüten sind.

Die Felder und `recycle` haben kein Sichtbarkeitswort. Das ist nicht Nachlässigkeit:
`FlappyPanel` liest `pipe.x` und ruft `pipe.recycle(...)`, mit `private` ginge
beides nicht. Alles, was eine andere Klasse im selben Paket benutzt, lässt das Wort
weg; alles andere wird `private`.

Warum eine eigene Datei: Eine zweite Klasse, die sich `FlappyPanel.java` teilt,
wäre dort versteckt, und der Compiler warnt vor einer Klasse, die von anderswo
benutzt wird und nicht in einer Datei ihres Namens liegt.

Halt die drei in einem Array, zurück in `FlappyPanel`:

```java
private final Pipe[] pipes = new Pipe[3];
```

`new Pipe[3]` schafft Platz für drei Röhren und keine Röhre. Jeder Platz enthält
zunächst `null`, also „hier ist kein Objekt", und jeder Zugriff darauf endet mit
einer `NullPointerException`. Das ist der eine Unterschied zu den `int[]`-Arrays
aus Kurs 1, wo die Plätze schon mit Nullen ankamen.

Gefüllt wird das Array im Konstruktor, und nur dort:

```java
FlappyPanel() {
    setBackground(new Color(120, 190, 230));

    for (int i = 0; i < pipes.length; i++) {
        pipes[i] = new Pipe(WIDTH + i * PIPE_SPACING);
    }

    reset();
}
```

Die drei stehen damit hintereinander rechts außerhalb des Bildes, `PIPE_SPACING`
Pixel auseinander, und kommen von dort einzeln hereingefahren. `reset` taucht
weiter unten auf und erzeugt bewusst keine Röhren mehr, sondern schiebt die
vorhandenen zurück.

## 🟢 Kernübung — Ein Schritt des Spiels

Ab hier schreibst du. Alles, was unten entsteht, gehört in dieselbe Datei
`FlappyPanel.java`: zuerst die elf Konstanten, dann die Felder, dann der
Konstruktor, dann `reset`, `tick`, `collides`, `flap` und `paintComponent`. Die
Reihenfolge ist Geschmack, nur stehen müssen sie alle innerhalb derselben
geschweiften Klammern.

```java
void tick() {
    // dein Code hier -- die sechs Schritte unten
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
6. **Sterben.** Ruf `collides()` aus dem nächsten Abschnitt auf, und ist die
   Antwort ja, setz `gameOver` auf wahr.

Die Schritte 3 bis 5 gehören in dieselbe Schleife über die Röhren. Schritt 6 steht
danach und läuft einmal pro Bild, nicht einmal pro Röhre.

Lass Schritt 6 nicht bis zum Schluss liegen. Ohne ihn fliegt der Vogel durch die
Röhren hindurch, und du hältst die Methode aus dem nächsten Abschnitt für kaputt,
obwohl sie nur niemand ruft.

Schritt 4 und 5 fragen beide „wo ist die rechte Kante", und das ist
`pipe.x + PIPE_WIDTH` und nicht `pipe.x`. Wer das verwechselt, bekommt beides zu
früh: den Punkt eine Röhrenbreite, bevor der Vogel vorbei ist, und das Recyceln,
während die rechte Hälfte der Röhre noch im Bild steht. Das zweite siehst du, es
ist ein Zucken am linken Rand.

Schritt 5 ist der Grund, warum es `passed` gibt. Ohne das zählt jedes Bild, in dem
der Vogel hinter der Röhre ist, einen weiteren Punkt, und der Punktestand rennt mit
dreißig pro Sekunde davon.

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

Java beantwortet das für dich, mit `import java.awt.Rectangle;`:

```java
Rectangle bird = new Rectangle(BIRD_X, (int) birdY, BIRD_SIZE, BIRD_SIZE);
Rectangle upper = new Rectangle(pipe.x, 0, PIPE_WIDTH, pipe.gapY);

if (bird.intersects(upper)) {
```

`Rectangle` sind vier Zahlen: links, oben, Breite, Höhe. `intersects` beantwortet,
ob zwei davon sich irgendwo überschneiden.

`intersects` ist kein Zauber, und was es tut, sollte man einmal gesehen haben.
Zwei Rechtecke überschneiden sich, wenn sie sich waagerecht **und** senkrecht
überschneiden, und waagerecht überschneiden sie sich, wenn die linke Kante jedes
Rechtecks links von der rechten Kante des anderen liegt. Das sind vier Vergleiche
mit `&&` verbunden, und du könntest sie selbst schreiben. Die Bibliotheksfassung
hier zu nehmen heißt, eine Sache weniger zu haben, die man subtil falsch machen
kann, während man gleichzeitig Schwerkraft lernt.

Das untere Rechteck baust du selbst. Seine Oberkante ist `gapY + GAP`, und seine
Höhe ist, was vom Bild darunter übrig bleibt.

## 🟢 Kern — Was man sieht

Hier steht der Code vollständig, anders als in den beiden Abschnitten davor. Am
Zeichnen ist nichts zu überlegen, es ist Fleißarbeit mit Zahlen, und ohne sie
siehst du nicht, ob deine Schwerkraft stimmt. Das Bild oben in diesem Kapitel ist
genau das, was diese Methode malt:

```java
@Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    g.setColor(new Color(60, 160, 70));
    for (Pipe pipe : pipes) {
        g.fillRect(pipe.x, 0, PIPE_WIDTH, pipe.gapY);
        g.fillRect(pipe.x, pipe.gapY + GAP, PIPE_WIDTH,
                HEIGHT - pipe.gapY - GAP);
    }

    g.setColor(new Color(240, 200, 60));
    g.fillOval(BIRD_X, (int) birdY, BIRD_SIZE, BIRD_SIZE);

    g.setColor(Color.WHITE);
    g.setFont(new Font("SansSerif", Font.BOLD, 24));
    g.drawString(String.valueOf(score), WIDTH / 2 - 8, 40);

    g.setFont(new Font("SansSerif", Font.PLAIN, 14));
    if (!started && !gameOver) {
        g.drawString("Press space to start", WIDTH / 2 - 65, HEIGHT / 2 + 60);
    }

    if (gameOver) {
        g.setFont(new Font("SansSerif", Font.BOLD, 28));
        g.drawString("Game over", WIDTH / 2 - 70, HEIGHT / 2);
        g.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g.drawString("Press space to play again", WIDTH / 2 - 80, HEIGHT / 2 + 30);
    }
}
```

Dazu `import java.awt.Font;`, und `java.awt.Color` hattest du schon.

`new Color(60, 160, 70)` sind Rot, Grün und Blau von 0 bis 255. Die benannten
Konstanten wie `Color.GREEN` gibt es auch, sie sind nur grell; drei Zahlen sind das
ganze Werkzeug, um das zu ändern.

Der Hintergrund wird hier nicht gezeichnet. `setBackground` im Konstruktor sagt
Swing die Farbe, und `super.paintComponent(g)` in der ersten Zeile füllt damit die
Fläche. Lass diese erste Zeile einmal weg, und du siehst, was sie tut: Der Vogel
zieht Schlieren, weil nichts mehr das alte Bild löscht, und der Himmel ist weg.

Die Reihenfolge entscheidet, was oben liegt: Röhren, dann Vogel, dann Schrift.
Umgedreht verschwände der Vogel hinter einer Röhre.

`drawString` setzt die Grundlinie der Schrift, nicht die Oberkante. Deshalb steht
beim Punktestand `40` und nicht `0`; bei `0` wäre er über dem Bildrand.

Die Zahlen wie `WIDTH / 2 - 65` sind grobe Mitte. Eine Zeile wirklich zu
zentrieren, geht über `g.getFontMetrics().stringWidth(text)`, und das ist eine
lohnende kleine Übung, sobald der Rest läuft.

## 🟢 Kern — Starten, sterben, neu anfangen

Der Vogel soll nicht fallen, bevor der Spieler bereit ist, und ein beendetes Spiel
soll sich ohne Programmneustart wieder spielen lassen. Die vier Felder, die dafür
und für das Zeichnen fehlen, stehen bei den anderen oben in der Klasse:

```java
private double birdY = HEIGHT / 2.0;
private double velocity;
private final Pipe[] pipes = new Pipe[3];
private int score;
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
nach dem Tod fängt von vorn an:

```java
void reset() {
    birdY = HEIGHT / 2.0;
    velocity = 0;
    score = 0;
    gameOver = false;
    started = false;
    for (int i = 0; i < pipes.length; i++) {
        pipes[i].recycle(WIDTH + i * PIPE_SPACING);
    }
}
```

Beachte, was hier **nicht** steht: kein `new Pipe`. Die drei Objekte entstehen im
Konstruktor, `reset` schiebt sie nur zurück. Deshalb muss der Konstruktor die
Schleife mit `new` enthalten, und zwar **vor** dem Aufruf von `reset()`. Sonst
greift `recycle` auf `null` zu, und du startest in eine `NullPointerException`
hinein, bevor das Fenster erscheint.

`flap` und `reset` haben wieder kein Sichtbarkeitswort, weil `FlappyBird` sie ruft.
`collides` ist `private`, weil es das nicht tut.

Die Verdrahtung kommt in die dritte Datei, `FlappyBird.java`, und sie ist kurz
genug, um sie ganz zu zeigen:

```java
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class FlappyBird {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FlappyBird::start);
    }

    static void start() {
        FlappyPanel panel = new FlappyPanel();
        panel.setPreferredSize(new Dimension(FlappyPanel.WIDTH, FlappyPanel.HEIGHT));
        panel.setFocusable(true);

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_SPACE) {
                    panel.flap();
                }
            }
        });

        JFrame frame = new JFrame("Flappy Bird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        panel.requestFocusInWindow();

        ActionListener step = event -> {
            panel.tick();
            panel.repaint();
        };
        new Timer(16, step).start();
    }
}
```

Vier Stellen daraus verdienen einen Satz.

`FlappyPanel.WIDTH` trägt hier den Klassennamen vor sich her. Im Panel selbst
reicht `WIDTH`; `FlappyBird` ist eine Klasse weiter, und für sie ist der kurze
Name ohne Präfix ein `cannot find symbol`.

`SwingUtilities.invokeLater` baut das Fenster auf Swings eigenem Thread statt auf
dem, auf dem `main` läuft. Swing verlangt das, und `FlappyBird::start` ist die
Kurzschreibweise für „ruf diese Methode", wie das `event ->` weiter oben, nur ohne
Parameter.

`setPreferredSize` plus `pack()` statt `setSize`: Das Panel sagt, wie groß es sein
will, und das Fenster legt sich darum. Mit `setSize` am Fenster bekäme das Panel
den Platz minus Titelleiste und Rahmen: gemessen 14 Pixel schmaler und 37 Pixel
niedriger als verlangt. Deine Todeslinie unten wäre dann nicht dort, wo du sie
vermutest, und die rechte Röhrenspalte ragte aus dem Bild.

`requestFocusInWindow()` steht **nach** `setVisible(true)`. Davor gibt es kein
Fenster, das den Fokus vergeben könnte. Meistens klappt es auch vorher, weil das
System ein neues Fenster von sich aus in den Vordergrund holt; verlassen kann man
sich aber nur auf diese Reihenfolge.

```
javac *.java
java FlappyBird
```

Diesmal drei Dateien, also wieder `javac *.java`, und wieder nicht
`java FlappyBird.java`. Der Einzeldatei-Start nimmt nur die genannte Datei mit;
`FlappyPanel` bleibt dabei ungesehen.

![Game over, der Vogel an einer Röhre gestoppt, Punktestand 12](../assets/game-over.png)

Für die echten Schalter dieses Repositories lautet der Aufruf
`javac --release 21 -Xlint:all,-serial -Werror *.java`. Ohne `-serial` kommst du
nicht durch: Wer von `JPanel` erbt, erbt dessen Serialisierbarkeit und schuldet
damit ein `serialVersionUID`. `FlappyPanel` hat keines, und die Warnung darüber
hält dich auf, bevor dein eigener Code überhaupt dran ist.

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
meistens `ConcurrentModificationException`. Meistens, nicht immer. Erwischst du das
vorletzte Element, endet die Schleife danach von selbst und merkt nichts: die
nächste Prüfung vergleicht Position und Länge und findet sie zufällig gleich. Ein
Fehler, der bei drei von vier Elementen auffällt und beim vierten schweigt, ist
schlimmer als einer, der immer knallt. Sammle, was weg soll, und entferne es
danach, oder lauf rückwärts über die Indizes.

## 🟡 Optional — Mach es fair

Drei Zahlen entscheiden, ob dieses Spiel angenehm oder zum Verzweifeln ist:
`GRAVITY`, `FLAP` und `GAP`.

Ändere eine nach der anderen und spiel nach jeder Änderung fünf Runden. Schreib
danach in einen Kommentar, warum deine Zahlen die sind, bei denen du geblieben
bist. Die nützliche Frage ist nicht „ist es schwer", sondern „glaube ich beim
Sterben, dass es meine Schuld war".

Lohnt sich auf Papier: Wie viele Bilder vergehen bei `GRAVITY` 0,5 und `FLAP` -8,
bis ein Flattern vollständig aufgezehrt ist? Die Antwort sagt dir, in welchem
Rhythmus das Spiel den Spieler tippen lässt. Rechne sie anschließend in Sekunden
um, und zwar mit der Bildrate, die du gemessen hast, nicht mit den 62 aus der
Theorie. Bei etwa 32 Bildern pro Sekunde ist derselbe Bogen doppelt so lang, und
das ist der Grund, warum dieselben Zahlen auf zwei Rechnern verschieden spielen.

## 🔴 Optional, echte Herausforderung — Ein Punktestand, der das Programm überlebt

Heb den besten Punktestand in einer Datei neben dem Programm auf, lies ihn beim
Start, und schreib ihn, wenn eine Runde ihn schlägt.

```java
import java.io.IOException;
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
    } catch (IOException | NumberFormatException e) {
        return 0;
    }
}
```

`try` führt den riskanten Teil aus, `catch` sagt, was bei einem Fehlschlag zu tun
ist. Hier bedeuten „noch keine Datei" und „die Datei ist unlesbar" ehrlicherweise
beide „noch kein bester Punktestand", also liefern beide 0.

Der senkrechte Strich im `catch` fängt zwei verschiedene Fehler mit demselben
Block. Beide braucht es, und sie kommen aus verschiedenen Zeilen: `readString`
wirft `IOException`, wenn die Datei fehlt, und `Integer.parseInt` wirft
`NumberFormatException`, wenn etwas darin steht, das keine Zahl ist. Mit nur
`catch (IOException e)` übersetzt die Methode zwar, aber sie stürzt beim zweiten
Test unten ab, und zwar an einer Stelle, an der du die Ursache nicht vermutest.

Schreiben braucht denselben Umgang:

```java
static void writeBest(int best) {
    try {
        Files.writeString(Path.of("highscore.txt"), String.valueOf(best));
    } catch (IOException e) {
        // Kein Rekord ist kein Grund, das Spiel anzuhalten.
    }
}
```

Ein leerer `catch`-Block ist normalerweise ein schlechtes Zeichen. Hier ist er
richtig, und der Kommentar sagt, warum. Ohne ihn liest der nächste, der das sieht,
„hier wurde etwas vergessen".

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

Der Boden ist Dekoration, lass den Vogel also nicht mit ihm kollidieren; er stirbt
ohnehin bei `HEIGHT`. Machst du ihn aber hoch genug, um ihn zu sehen, zieh die
Todeslinie in `collides` auf seine Oberkante hoch, sonst versinkt der Vogel sichtbar
darin, bevor das Spiel es merkt.

Und dann hast du ein zweites Problem, das genau daraus entsteht: `recycle` berechnet
`gapY` aus `HEIGHT`, also kann die untere Röhre jetzt in den Boden hineinragen oder
eine Lücke so tief liegen, dass der Vogel sie nicht mehr erreicht, ohne zu sterben.
Führ eine Zahl für die spielbare Höhe ein, benutz sie an allen drei Stellen — Lücke
auslosen, untere Röhre zeichnen, Todeslinie — und `HEIGHT` nur noch für das Fenster.
Das ist die eigentliche Arbeit an dieser Aufgabe, nicht der Streifen.

## Was du gelernt hast

- Bewegung als Geschwindigkeit, die die Schwerkraft ändert, statt als fester Schritt
- Warum `birdY` und `velocity` `double` sind, und die zwei verschiedenen Antworten
  des Compilers auf `=` und `+=`, wenn sie es nicht sind
- Einmal nach `int` umwandeln, an der Stelle des Zeichnens
- Eine Klasse, deren Aufgabe das Halten von Daten ist, und `this.x = x` bei
  gleichen Namen
- Ein Array von Objekten, bei dem `new Pipe[3]` Platz schafft, aber keine Röhren
- Objekte recyceln, statt sie zu erzeugen und wegzuwerfen
- `Rectangle` und `intersects`, und für welche vier Vergleiche sie stehen
- Eine Markierung wie `passed`, damit etwas einmal passiert statt in jedem Bild
- Zwei Booleans für „nicht gestartet" und „vorbei", und eine Taste mit drei
  Bedeutungen
- Eine `reset`-Methode, die aus dem Konstruktor und aus dem Neustart gerufen wird,
  und warum trotzdem nur der Konstruktor `new` sagt
- `extends`, `@Override`, `protected`, und das fehlende Sichtbarkeitswort als
  eigene Stufe
- Eine Methode ohne Namen hinter `->` und eine Klasse ohne Namen hinter `new`
- Tasten über `addKeyListener` und `keyPressed`, und warum der Fokus erst nach
  `setVisible` angefordert wird
- Dass `Timer(16)` eine Bitte ist und unter Windows etwa 32 Bilder pro Sekunde
  liefert
- Dass eine geerbte Konstante wie `ImageObserver.HEIGHT` eine vergessene eigene
  stillschweigend ersetzt
- *(optional)* `ArrayList`, und warum Entfernen während einer Schleife meistens
  wirft
- *(optional)* `try` und `catch`, ein `catch` für zwei Fehlerarten, und eine
  fehlende Datei als Normalfall

## Weiter

Das war der letzte Kurs in diesem Repository. Sieben Spiele, von einem Spielfeld
aus Strichen bis zu einem Vogel, der mit dreißig Bildern pro Sekunde fällt, und
installiert ist nichts außer dem JDK.

Was danach kommt, steht in
[PROJECT-IDEAS.de.md](../../../PROJECT-IDEAS.de.md): Pong, Minesweeper, Breakout
und Tetris liegen alle in Reichweite dessen, was du jetzt kannst, und der letzte
Eintrag ist der, der das blanke JDK für ein Build-Werkzeug und echte Tests
verlässt. Geschrieben ist noch keiner davon. Die Liste steht da, weil das nächste
selbst gebaute Spiel mehr bringt als ein weiteres Kapitel zum Lesen.
