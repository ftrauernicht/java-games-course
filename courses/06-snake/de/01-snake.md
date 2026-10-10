🇩🇪 Deutsch | 🇬🇧 [English](../en/01-snake.md)

[← Zurück zur Kursübersicht](../../../README.de.md) · Setzt voraus: [Kurs 1 – Grundlagen](../../01-basics/de/00-einfuehrung.md)

# Kurs 6 – Snake

**Ziel:** das erste Spiel in einem Fenster. Eine Taste, die im Moment des Drucks
ankommt, ein Timer, der das Spiel antreibt statt einer Schleife, und eine Schlange,
die wächst, bis du sie irgendwo hineinfährst.

![Die Schlange zusammengerollt in der Mitte des Feldes, rechts oben ein rotes Stück Futter, Punktestand 6](../assets/gameplay.png)

Setzt [Kurs 1](../../01-basics/de/00-einfuehrung.md) voraus und sonst nichts. Eine
fertige Fassung liegt in [`code/`](../code/), in zwei Dateien.

## 🟢 Kern — Warum überhaupt ein Fenster

Ein Terminal gibt deinem Programm eine Zeile auf einmal. Es wartet auf Enter, und
daran ändert nichts, was du in Java schreiben kannst, denn das Warten passiert,
bevor dein Programm beteiligt ist. Für ein Spiel, in dem man etwas fortlaufend
steuert, ist das das Ende des Weges.

Ein Fenster hat diesen Mittelsmann nicht. Drück eine Taste, und dein Programm
erfährt es sofort, samt Taste, während das Spiel weiterläuft.

Das ist der einzige Grund, warum dieser Kurs ein Fenster öffnet. Alles andere an
Swing in diesem Kapitel ist das Mindeste, um ein Rechteck auf den Bildschirm und
eine Taste in deinen Code zu bekommen.

## 🟢 Kern — Klassen und Objekte, kurz erklärt

*(Wenn du Kurs 7 gemacht hast, überspring das und lies beim nächsten Abschnitt
weiter.)*

Kurs 1 nannte eine Klasse „eine Schachtel mit Namen" und versprach die richtige
Antwort später. Jetzt ist später.

Eine Klasse beschreibt, was etwas **ist** und was es **kann**, an einer Stelle. Die
Beschreibung nennt die Werte, die es enthält, genannt **Felder**, und die Methoden,
die damit arbeiten. Aus der Beschreibung baust du mit `new` ein echtes Ding, ein
**Objekt**:

```java
public final class Counter {
    private int count;          // ein Feld: jeder Counter hat sein eigenes

    void add() {                // kein static: das arbeitet an einem Counter
        count++;
    }

    int get() {
        return count;
    }
}
```

```java
Counter a = new Counter();
Counter b = new Counter();
a.add();
a.add();
b.add();
System.out.println(a.get() + " " + b.get());
```

```
2 1
```

Zwei Objekte, zwei getrennte `count`-Felder. Das ist der ganze Unterschied zu den
`static`-Methoden aus Kurs 1: Die gehören zur Klasse und es gibt sie je einmal,
während diese zu einem Objekt gehören und es sie einmal pro Objekt gibt.

Zwei weitere Wörter, die du in diesem Kapitel siehst. `private` an einem Feld
heißt, dass nur diese Klasse es anfassen darf, und das hält den Spielzustand davon
ab, versehentlich von außen geändert zu werden. `final` an einer Klasse heißt, dass
niemand eine Abwandlung darauf aufbauen darf; gleich steht, warum das hier wichtig
ist.

Und eines, das du schon kennst, mit neuem Zweck: `extends`. Es heißt „fang bei
dieser Klasse an und ändere eine Sache".

## 🟢 Kern — Ein Fenster, eine Fläche, und eine Methode

Drei Tatsachen, und du hast alles, was dieses Kapitel von Swing braucht.

**Ein Fenster ist ein `JFrame`, und darauf zeichnest du nicht.** Du setzt etwas
hinein, auf das sich zeichnen lässt.

**Dieses Etwas ist ein `JPanel`, und gezeichnet wird, indem du eine Methode
ersetzt.** Du baust dein eigenes Panel, das `extends JPanel`, und überschreibst
`paintComponent`:

```java
public final class SnakePanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.fillRect(40, 40, 20, 20);
    }
}
```

`extends JPanel` heißt: Sei in jeder Hinsicht ein Panel, außer bei der einen
Methode, die darunter steht. `super.paintComponent(g)` gibt für einen Moment an das
Original zurück, damit es den Hintergrund löschen kann, und wer es weglässt, behält
die Pixel des letzten Bildes.

`Graphics g` ist das, womit du zeichnest. `fillRect(x, y, breite, hoehe)` ist ein
gefülltes Rechteck, `setColor` wählt die Farbe, `drawString` schreibt Text. Die
Koordinaten sind Pixel von links oben, und y wächst **nach unten**.

**`paintComponent` rufst du nie selbst auf.** Du rufst `repaint()`, womit du Swing
bittest, bei Gelegenheit neu zu zeichnen, und Swing ruft dann `paintComponent` für
dich. Das ist die Idee, die am längsten braucht, bis sie sitzt, und die Regel
daraus ist kurz: Erst den Spielzustand ändern, dann `repaint` rufen, nie andersherum.

Die Klasse ist aus einem konkreten Grund `final`. Ein Panel, das nicht final ist,
lässt sich von anderswo erweitern, und der Compiler warnt dann, dass `this` im
Konstruktor weiterzureichen ein halbfertiges Objekt preisgeben könnte. Dieses
Repository baut mit Warnungen als Fehlern, die Warnung würde den Bau also stoppen.
`final` sagt, dass niemand erweitern wird, und die Warnung verschwindet.

## 🟢 Kern — Die Schlange als zwei Arrays

Eine Liste wäre die erwachsene Antwort, und Kurs 1 hat keine Listen behandelt. Er
muss auch nicht: Die Schlange kann nie länger werden als das Feld, ein Paar Arrays
dieser Größe reicht also.

```java
private final int[] x = new int[COLS * ROWS];
private final int[] y = new int[COLS * ROWS];
private int length = 3;
```

Stelle 0 ist der Kopf. `x[0]`, `y[0]` ist, wo er steht, in **Feldern**, nicht in
Pixeln. Das Spielfeld ist 20 mal 20 Felder zu je 20 Pixeln, und das hält jede
Rechnung in ganzen Zahlen und macht „hat der Kopf das Futter erreicht" zu einem
schlichten Vergleich.

Die Richtung ist ein Schritt pro Takt:

```java
private int dirX = 1;
private int dirY = 0;
```

Rechts ist `(1, 0)`, links `(-1, 0)`, hoch `(0, -1)`, runter `(0, 1)`. Hoch ist
negativ, weil y nach unten wächst.

Die Startschlange baust du im Konstruktor auf, einer Methode mit dem Namen der
Klasse, die beim Erzeugen des Objekts läuft:

```java
SnakePanel() {
    setBackground(Color.BLACK);
    for (int i = 0; i < length; i++) {
        x[i] = 3 - i;
        y[i] = 3;
    }
    spawnFood();
}
```

Drei Glieder in einer Reihe bei `(3,3)`, `(2,3)`, `(1,3)`, Kopf zuerst.

## 🟢 Kernübung — Ein Schritt des Spiels

```java
void tick() {
    // dein Code hier -- die fünf Schritte unten
}
```

1. Wenn das Spiel schon vorbei ist, kehr zurück und tu nichts.
2. **Beweg den Körper.** Lauf vom Schwanz nach vorn, `i` von `length - 1` herunter
   bis 1, und kopier Glied `i - 1` in Glied `i`. Beweg danach den Kopf, indem du
   `dirX` und `dirY` auf `x[0]` und `y[0]` addierst.
3. **Wand.** Ist die Spalte des Kopfes unter 0 oder mindestens `COLS`, oder seine
   Zeile unter 0 oder mindestens `ROWS`, ist das Spiel vorbei.
4. **Sich selbst.** Vergleich den Kopf mit jedem anderen Glied, von 1 bis
   `length - 1`. Eine Übereinstimmung beendet das Spiel.
5. **Futter.** Steht der Kopf auf dem Futter, zähl eins auf `length` und auf den
   Punktestand, und leg neues Futter aus.

In Schritt 2 steckt der Fehler. Läuft man **vorwärts** durch den Körper, von 1 bis
`length - 1`, kopiert man den Kopf ins erste Glied und denselben Wert dann ins
zweite, und die ganze Schlange fällt auf ein Feld zusammen. Rückwärts nimmt jedes
Glied den Platz des Gliedes vor ihm ein, bevor dieses sich bewegt hat. Mach es
einmal falsch und schau zu.

Fang in Schritt 4 bei 1 an und nicht bei 0. Der Kopf ist immer gleich sich selbst.

`spawnFood` würfelt ein Feld aus und versucht es erneut, wenn die Schlange darauf
liegt:

```java
private void spawnFood() {
    while (true) {
        int fx = (int) (Math.random() * COLS);
        int fy = (int) (Math.random() * ROWS);

        boolean onSnake = false;
        for (int i = 0; i < length; i++) {
            if (x[i] == fx && y[i] == fy) {
                onSnake = true;
            }
        }

        if (!onSnake) {
            foodX = fx;
            foodY = fy;
            return;
        }
    }
}
```

## 🟢 Kern — Das Zeichnen

```java
@Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    g.setColor(Color.RED);
    g.fillRect(foodX * CELL, foodY * CELL, CELL, CELL);

    for (int i = 0; i < length; i++) {
        g.setColor(i == 0 ? Color.GREEN : new Color(0, 160, 0));
        g.fillRect(x[i] * CELL, y[i] * CELL, CELL - 1, CELL - 1);
    }

    g.setColor(Color.WHITE);
    g.setFont(new Font("SansSerif", Font.PLAIN, 14));
    g.drawString("Score: " + score, 8, 18);

    if (gameOver) {
        g.setFont(new Font("SansSerif", Font.BOLD, 28));
        g.drawString("Game over", COLS * CELL / 2 - 70, ROWS * CELL / 2);
    }
}
```

Aus Feldern werden Pixel durch die Multiplikation mit `CELL`. Das `- 1` bei Breite
und Höhe lässt einen Pixel Luft, und genau das macht die Glieder als Glieder
erkennbar statt als einen langen Balken. Der Kopf ist heller als der Körper, damit
du siehst, wohin es geht.

## 🟢 Kern — Tasten

```java
void steer(int keyCode) {
    if (keyCode == KeyEvent.VK_LEFT && dirX != 1) {
        dirX = -1;
        dirY = 0;
    } else if (keyCode == KeyEvent.VK_RIGHT && dirX != -1) {
        dirX = 1;
        dirY = 0;
    } else if (keyCode == KeyEvent.VK_UP && dirY != 1) {
        dirX = 0;
        dirY = -1;
    } else if (keyCode == KeyEvent.VK_DOWN && dirY != -1) {
        dirX = 0;
        dirY = 1;
    }
}
```

Jeder Zweig lehnt genau das Gegenteil der aktuellen Fahrtrichtung ab, denn
umzudrehen würde den Kopf direkt in den Hals fahren.

Die `VK_`-Namen sind Konstanten für Tasten, und `getKeyCode()` liefert eine davon.
Nimm `keyPressed` und nicht `keyTyped`: `keyTyped` geht um Zeichen, und eine
Pfeiltaste erzeugt keines, was der übliche Grund dafür ist, dass jemandes
Pfeiltasten scheinbar nichts tun.

## 🟢 Kern — Die Verdrahtung, und die Schleife, die keine ist

Das alles kommt in die zweite Datei, `Snake.java`, und sie ist die einzige Stelle,
die weiß, dass es ein Fenster gibt.

```java
public static void main(String[] args) {
    SwingUtilities.invokeLater(Snake::start);
}
```

Swing will seine Arbeit auf einem bestimmten Thread erledigen, und `invokeLater`
ist der Weg dorthin. Nimm es als die Wendung, mit der ein Swing-Programm anfängt.

```java
static void start() {
    SnakePanel panel = new SnakePanel();
    panel.setPreferredSize(new Dimension(COLS * CELL, ROWS * CELL));
    panel.setFocusable(true);

    panel.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent event) {
            panel.steer(event.getKeyCode());
        }
    });

    JFrame frame = new JFrame("Snake");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(panel);
    frame.pack();
    frame.setVisible(true);
    panel.requestFocusInWindow();

    ActionListener step = event -> {
        panel.tick();
        panel.repaint();
    };
    new Timer(120, step).start();
}
```

Vier Details, und jedes kostet einen Abend, wenn man es falsch macht.

`setPreferredSize` am Panel plus `frame.pack()`, nicht `frame.setSize`. Die Größe
eines Fensters schließt Titelleiste und Rahmen ein, 400 mal 400 für das Fenster zu
verlangen gibt dir also eine kleinere Zeichenfläche, und die unterste Reihe deines
Spielfelds verschwindet stillschweigend. `pack` fragt das Panel, was es will, und
baut das Fenster darum.

`setFocusable(true)` und `requestFocusInWindow()`, und das zweite **nach**
`setVisible(true)`. Ein Panel ohne Tastaturfokus bekommt keine Tasten, und ein
Fenster, das noch nicht auf dem Bildschirm ist, kann niemandem den Fokus geben.

`new Timer(120, step)` ist die Spielschleife. Alle 120 Millisekunden ruft er `tick`
und danach `repaint`. Nirgends steht ein `while (true)` und nirgends ein
`Thread.sleep`, und das ist Absicht: Swing zeichnet auf demselben Thread neu, auf
dem du schlafen würdest, ein schlafendes Spiel ist also ein eingefrorenes Fenster.

Importier `javax.swing.Timer` namentlich. Es gibt auch einen `java.util.Timer`, und
sind beide über einen Stern importiert, wird das Wort `Timer` mehrdeutig und die
Datei lässt sich nicht übersetzen.

Übersetz beide Dateien zusammen und starte die Klasse mit `main`:

```
javac *.java
java Snake
```

Nicht `java Snake.java`. Die Einzeldatei-Abkürzung aus Kurs 1 übersetzt nur die
Datei, die du ihr gibst, und findet `SnakePanel` dann nicht. Der Fehler lautet
`cannot find symbol` und zeigt damit überhaupt nicht auf die Ursache.

![Game over, die Schlange an der rechten Wand gestoppt, Punktestand 11](../assets/game-over.png)

## 🟡 Optional — Schneller mit der Zeit

Heb dir den Timer auf und ruf alle paar Punkte `setDelay` darauf, damit das Spiel
schneller wird. Entscheide, was fair ist, und merk dir, dass ein Spiel, das nie
schwerer wird, keine Form hat.

## 🟡 Optional — Noch eine Runde

Lass nach dem Spielende eine Taste eine neue Runde starten: Länge, Richtung,
Punktestand und die Gliederpositionen zurücksetzen, und neues Futter auslegen. Der
Konstruktor macht genau das schon, der ehrliche Weg ist also, diese Zeilen in eine
Methode `reset` zu ziehen und sie von beiden Stellen aus aufzurufen.

## 🔴 Optional, echte Herausforderung — Die Umkehr, die trotzdem durchkommt

`steer` lehnt eine Umkehr ab, und schlagen lässt sie sich trotzdem.

Die Schlange fährt nach rechts. Drück zwischen zwei Takten schnell Hoch und dann
Links. Hoch ist erlaubt und setzt die Richtung auf hoch. Links wird jetzt gegen
*hoch* geprüft und nicht gegen *rechts*, ist also auch erlaubt. Der nächste Takt
fährt die Schlange nach links, in den eigenen Hals, und das Spiel endet ohne
sichtbaren Grund.

Die Ursache ist, dass `steer` gegen die Richtung prüft, wie sie gerade ist, und die
hat ein früherer Tastendruck in derselben Lücke bereits geändert. Geprüft werden
muss gegen die Richtung, die der **letzte Takt tatsächlich benutzt** hat.

Reparier es, indem du die Tastendrücke in eine getrennte wartende Richtung
schreibst und `tick` sie als Erstes übernimmt:

```java
private int nextDirX = 1;
private int nextDirY = 0;
```

Überleg dir selbst, wogegen `steer` dann prüft und was `tick` mit den beiden Paaren
macht. Stell den Fehler zuerst nach, mit zwei schnellen Tastendrücken, damit du
deine Reparatur von Glück unterscheiden kannst.

Eine zweite, kleinere Herausforderung: Pause auf P. Stopp den Timer, schreib das
Wort auf den Bildschirm, und starte ihn beim nächsten P wieder. Dafür muss das
Panel an den Timer herankommen, also braucht es ein Feld dafür und einen Setzer,
der aus `start` gerufen wird.

## Was du gelernt hast

- Eine Klasse mit Feldern und einem Konstruktor, und dass Objekte je eigene halten
- `private`, um Zustand innen zu halten, `final` an einer Klasse, und warum diese
  final ist
- `JFrame` zum Halten, `JPanel` zum Zeichnen, `paintComponent` für den Inhalt
- `super.paintComponent(g)`, und was ohne es übrig bleibt
- Dass du `repaint()` rufst und Swing `paintComponent`, nie umgekehrt
- Zeichnen mit `fillRect` und `drawString`, in Pixeln, mit y nach unten wachsend
- Ein Timer als Spielschleife, und warum ein Swing-Spiel nicht schlafen darf
- `setPreferredSize` plus `pack` statt `setSize`
- Fokus, und `requestFocusInWindow` nach dem Sichtbarmachen
- `keyPressed` mit `VK_`-Codes, und warum `keyTyped` Pfeiltasten verpasst
- Einen Körper vom Schwanz nach vorn bewegen, und was andersherum passiert
- Mehrere Dateien mit `javac *.java` übersetzen
- *(optional)* Eine wartende Richtung, und ein Fehler, den nur zwei schnelle
  Tastendrücke zeigen

## Weiter

Kurs 7 ist Flappy Bird: Schwerkraft statt Gitter, eine Klasse, die nichts als Daten
hält, und Kollision zwischen Rechtecken. Er setzt Kurs 1 voraus und sonst nichts,
erklärt die Swing-Teile, die er braucht, also noch einmal.

Die vollständige Liste steht in
[PROJECT-IDEAS.de.md](../../../PROJECT-IDEAS.de.md).
