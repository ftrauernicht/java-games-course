🇩🇪 Deutsch | 🇬🇧 [English](../en/01-towers-of-hanoi.md)

[← Zurück zur Kursübersicht](../../../README.de.md) · Setzt voraus: [Kurs 1 – Grundlagen](../../01-basics/de/00-einfuehrung.md)

# Kurs 3 – Türme von Hanoi

**Ziel:** das kürzeste Programm in diesem Repository, und das, welches am
schwersten zu glauben ist. Drei Stäbe, ein Stapel Scheiben, und eine Lösung, deren
Rumpf aus drei Zeilen plus einer Abbruchbedingung besteht, weil sie sich selbst um
Hilfe bittet.

![Die Stäbe nach jedem Zug gezeichnet, gebaut von der Herausforderung am Ende dieses Kapitels](../assets/towers.png)

Das ist das erste Kapitel, das dir die fertige Methode nicht in die Hand gibt. Die
Idee dahinter wird vollständig erklärt; das Schreiben gehört dir. So funktioniert
jeder Kurs ab hier.

Setzt [Kurs 1](../../01-basics/de/00-einfuehrung.md) voraus und sonst nichts. Eine
fertige Fassung liegt in [`code/TowersOfHanoi.java`](../code/TowersOfHanoi.java).

## 🟢 Kern — Das Rätsel

Drei Stäbe, genannt A, B und C. Auf Stab A liegt ein Stapel Scheiben, die größte
unten, jede kleiner als die darunter:

```
  #    |    |
  ##   |    |
  ###  |    |
  A    B    C
```

Bring den ganzen Stapel auf Stab C. Zwei Regeln: immer nur eine Scheibe auf einmal,
und eine größere Scheibe darf nie auf einer kleineren liegen.

Probier es mit drei Scheiben auf Papier, bevor du weiterliest. Es braucht sieben
Züge, und sie von Hand herauszufinden ist mehr wert, als darüber zu lesen.

Mit vier Scheiben sind es fünfzehn Züge, mit zehn sind es 1023, und die allgemeine
Antwort ist zwei hoch Anzahl der Scheiben, minus eins. Ein Programm, das jeden Zug
durch Vorausplanen bestimmt, wäre ein ernsthaftes Stück Arbeit.

Der arbeitende Teil des Programms, das du gleich schreibst, hat drei Zeilen. Er
braucht eine vierte, um zu wissen, wann Schluss ist, und an dieser Zeile hängt
dann alles.

## 🟢 Kern — Die Idee: lass eine kleinere Fassung die Arbeit machen

Hör auf, an Scheiben zu denken, und denk an die größte.

Damit Scheibe 3 von A nach C kommt, darf nichts auf ihr liegen und nichts auf C
liegen. Die zwei Scheiben über ihr müssen also irgendwo sein, und der einzige Platz,
der bleibt, ist B. Lägen sie schon auf B, wäre alles leicht: Scheibe 3 hinüber,
dann die zwei wieder obendrauf.

Drei Scheiben von A nach C zu bringen heißt also:

1. Bring die oberen zwei Scheiben irgendwie von A nach B.
2. Bring Scheibe 3 von A nach C.
3. Bring die zwei Scheiben irgendwie von B nach C.

Schritt 1 und 3 sind dasselbe Rätsel mit zwei Scheiben statt drei. Und zwei
Scheiben zu bewegen ist dasselbe Rätsel mit einer. Und eine Scheibe zu bewegen ist
einfach sie zu bewegen.

Das ist die ganze Lösung. Du bestimmst die Züge nie; du reichst immer wieder eine
kleinere Fassung des Problems an etwas weiter, das es schon lösen kann, und dieses
Etwas ist die Methode, die du gerade schreibst.

Das Wort dafür ist **Rekursion**: eine Methode, die sich selbst aufruft.

## 🟢 Kern — Eine Methode, die sich selbst aufruft

Hier ist die Form, ohne Rumpf:

```java
static void move(int n, char from, char to, char via) {
    ...
}
```

Vier Dinge muss sie wissen: wie viele Scheiben zu bewegen sind, wo sie liegen, wo
sie landen sollen, und welcher Stab als Parkplatz frei ist.

Zwei Dinge lassen Rekursion funktionieren, und eines davon wegzulassen macht sie
vollständig kaputt.

**Es muss kleiner werden.** Jeder Aufruf, den die Methode an sich selbst richtet,
muss weniger Scheiben betreffen als der Aufruf, in dem sie gerade steckt. Sonst
dreht sich dasselbe Problem ewig im Kreis.

**Es muss aufhören.** Es muss einen Fall geben, den die Methode direkt beantwortet,
ohne sich selbst aufzurufen. Hier ist das `n == 0`: keine Scheiben zu bewegen heißt
nichts zu tun. Dieser Fall heißt **Basisfall**, und ohne ihn läuft das Programm,
bis Java mit einem `StackOverflowError` aufgibt, was der Computer dafür sagt, dass
ihm der Platz für wartende Aufrufe ausgegangen ist.

Die wartenden Aufrufe sind einen Satz wert. Wenn `move` sich selbst aufruft,
verschwindet der äußere Aufruf nicht; er wartet, mitten in seiner Arbeit, bis der
innere fertig ist. Bei drei Scheiben stehen an der tiefsten Stelle drei Aufrufe
übereinander, die noch Arbeit vor sich haben, jeder mit seinem eigenen `n`,
`from`, `to` und `via`. (Zählt man die `n == 0`-Aufrufe mit, sind es vier, aber
die kehren zurück, ohne etwas zu tun.) Dieser Stapel ist der Grund, warum die
Methode nach dem ersten rekursiven Aufruf „zurückkommen" und ihre zweite Hälfte
erledigen kann.

Falls sich das noch nach einem Trick anfühlt: Am deutlichsten sieht man es, wenn
man die Aufrufe selbst ausgibt. So sieht es für drei Scheiben aus, nach Tiefe
eingerückt, und jeder Aufruf zeigt die vier Werte, die er bekommen hat:

```
move(n=3, from=A, to=C, via=B)
  move(n=2, from=A, to=B, via=C)
    move(n=1, from=A, to=C, via=B)
      -> Move disc 1 from A to C
    -> Move disc 2 from A to B
    move(n=1, from=C, to=B, via=A)
      -> Move disc 1 from C to B
  -> Move disc 3 from A to C
  move(n=2, from=B, to=C, via=A)
    move(n=1, from=B, to=A, via=C)
      -> Move disc 1 from B to A
    -> Move disc 2 from B to C
    move(n=1, from=A, to=C, via=B)
      -> Move disc 1 from A to C
```

Folge der zweiten Zeile. Der äußere Aufruf wollte nach C, aber dieser innere hat
`to=B` bekommen, und deshalb druckt er B. Zwischen den Aufrufen wird nichts
geteilt: Jeder hat seine eigenen vier Werte, überreicht von dem, der ihn aufgerufen
hat. Das ist die Antwort auf die Frage, die die Erklärung oben offen lässt, und die
lautet nicht „warum wird es kleiner", sondern „woher weiß der innere Aufruf, wohin
er soll".

Bau dir diese Ausgabe selbst, sobald deine Methode läuft. Reich einen zusätzlichen
Parameter `depth` durch, rück danach ein, und gib den Aufruf vor der Rekursion aus.

## 🟢 Kernübung — Schreib move

Drei Zeilen, in dieser Reihenfolge:

1. Bring `n - 1` Scheiben von `from` nach `via`. Überleg, welcher Stab währenddessen
   frei ist: Du willst nach `via`, also steht `to` leer herum.
2. Gib den Zug von Scheibe `n` von `from` nach `to` als eine Textzeile aus.
3. Bring `n - 1` Scheiben von `via` nach `to`. Jetzt ist `from` der freie Stab,
   denn der ist gerade leer geworden.

Und über allen dreien der Basisfall: Wenn `n` gleich 0 ist, kehr sofort zurück.

Kurs 1 hat `return` immer nur mit einem Wert dahinter benutzt, weil dort jede
Methode etwas zurückgab. `move` gibt nichts zurück, hier heißt es also `return;`
allein, ohne Wert und mit Semikolon direkt dahinter. Es bedeutet „hör jetzt auf"
und sonst nichts. Wer in einer `void`-Methode `return 0;` schreibt, bekommt
`incompatible types: unexpected return value`.

```java
static void move(int n, char from, char to, char via) {
    // dein Code hier -- der Basisfall, dann die drei Schritte oben
}
```

Ruf sie aus `main` mit drei Scheiben auf:

```java
public static void main(String[] args) {
    int discs = 3;
    System.out.println("Towers of Hanoi with " + discs + " discs.");
    System.out.println();
    move(discs, 'A', 'C', 'B');
}
```

Erwartete Ausgabe:

```
Towers of Hanoi with 3 discs.

Move disc 1 from A to C
Move disc 2 from A to B
Move disc 1 from C to B
Move disc 3 from A to C
Move disc 1 from B to A
Move disc 2 from B to C
Move disc 1 from A to C
```

Das Schwerste ist, Schritt 1 zu vertrauen. Du schreibst „bring zwei Scheiben von A
nach B", ohne zu wissen, wie das passiert, denn das herauszufinden ist die Aufgabe
der Methode selbst, und in dem Moment, in dem du die Zeile schreibst, ist die
Methode noch nicht fertig. Schreib sie trotzdem. Für diesen Sprung ist dieses
Kapitel da.

Wenn du die Reihenfolge der Argumente verwechselst, kommt eine Folge illegaler Züge
heraus, die fast richtig aussieht. Vergleich sie mit den sieben Zeilen oben.

Falls du feststeckst, zeigt [`code/TowersOfHanoi.java`](../code/TowersOfHanoi.java)
einen Weg, es zu schreiben.

## 🟢 Kern — Die Zugfolge lesen

Nimm die Ausgabe und folge ihr auf Papier mit drei verschieden großen Münzen.
Sieben Züge, und der Stapel liegt am Ende in der richtigen Reihenfolge auf C.

Dann schau, wo Scheibe 3 auftaucht: genau einmal, in der Mitte, als vierter von
sieben Zügen. Drei Züge davor, um Platz zu schaffen, drei danach, um wieder
aufzubauen. Bei vier Scheiben käme Scheibe 4 genau einmal vor, als achter von
fünfzehn, mit sieben auf jeder Seite. Das Muster wiederholt sich bis ganz nach
unten, und daher kommt die Verdopplung: Jede zusätzliche Scheibe bedeutet, die
ganze bisherige Arbeit zweimal zu machen, plus einen Zug.

Setz `discs` auf 4 und prüf, dass fünfzehn Zeilen kommen. Dann probier 20 und
beachte, dass eine Million Züge ziemlich zügig gedruckt werden und dass das hier
das erste Programm ist, das einen Computer beschäftigen könnte.

## 🟡 Optional — Die Züge zählen

Lass das Programm berichten, wie viele Züge es gemacht hat, und vergleich das mit
der Formel.

Der sauberste Weg mit dem, was Kurs 1 dir gegeben hat: Lass `move` eine Zahl
zurückgeben statt nichts. Die Anzahl ist eins, plus das, was die beiden rekursiven
Aufrufe melden.

```java
static int move(int n, char from, char to, char via) {
    // dein Code hier -- dieselben drei Schritte, aber zähl zusammen, was die
    // beiden rekursiven Aufrufe zurückgeben, und gib die Summe zurück
}
```

Der Basisfall ändert sich mit. Eine Methode, die ein `int` verspricht, muss auf
jedem Weg eines liefern, aus `return;` wird also `return 0;` -- keine Scheiben,
keine Züge. Lass ihn, wie er war, und der Compiler sagt `missing return value`.

Für die Formel ist `(1 << n) - 1` zwei hoch `n`, minus eins. Das `<<` schiebt die
Binärziffern einer Zahl nach links, und jede Verschiebung verdoppelt sie, `1 << 10`
ist also 1024. `Math.pow(2, n)` liefert dieselbe Antwort als `double`, was danach
einen Cast braucht und für große `n` leicht danebenliegt. Die Verschiebung ist hier
also das ehrliche Werkzeug.

Gib beides aus und prüf, dass sie für jedes `n` von 1 bis 20 übereinstimmen.
Kommentier vorher das `println` in `move` aus, sonst gehen die zwanzig
Vergleichszeilen in zwei Millionen Zugzeilen unter.

Eine Grenze, die man kennen sollte: `1 << n` ist ein `int`, und einem `int` geht
nach 31 Verschiebungen der Platz aus. `1 << 31` ist schon negativ und `1 << 64`
ist 0, der Trick, der bis 30 exakt ist, liegt darüber also still daneben. Für
dieses Kapitel erledigt die Obergrenze 20 die Sache; für größere Zahlen würdest du
zu einem Typ greifen, der mehr fasst.

## 🟡 Optional — Nach der Scheibenzahl fragen

Lies die Zahl der Scheiben mit einem `Scanner` ein, statt sie auf 3 festzunageln,
und nutz dafür die Eingabemethode aus Kurs 1 wieder.

Setz eine Grenze. Bei 30 Scheiben gibt das Programm über eine Milliarde Zeilen aus,
und bei 64 liefe es noch, lange nachdem alle das Interesse verloren haben, was
genau der Witz der Legende ist, aus der das Rätsel stammt. Lehn alles über 20 ab
und sag warum.

## 🔴 Optional, echte Herausforderung — Die Scheiben wirklich bewegen

Das Programm druckt Anweisungen. Es prüft sie nie, denn es hält weder Stäbe noch
Scheiben: Ein Fehler in der Rekursion würde mit vollkommener Überzeugung Unsinn
ausgeben.

Bau den Zustand und führ die Züge aus. Dafür braucht es drei Dinge, die Kurs 1
nicht behandelt hat, und jedes bekommt deshalb einen Absatz, bevor die Übung
losgeht.

**Die Stäbe hören auf, Buchstaben zu sein.** Bisher sind `from`, `to` und `via`
ein `char`, und das ist gut zum Ausgeben und nutzlos zum Nachschlagen: `TOWERS['A']`
kannst du nicht schreiben. Mach aus allen vier Parametern von `move` ein `int` und
ruf sie mit `move(DISCS, 0, 2, 1)` auf, wobei 0, 1 und 2 für A, B und C stehen. Erst
beim Ausgeben machst du wieder einen Buchstaben daraus, mit `(char) (65 + peg)`,
denn 65 ist der Zahlenwert von `'A'`.

Überspring diese Änderung nicht und hoff auf das Beste. Java wandelt ein `char`
stillschweigend in ein `int` um, eine Methode mit `int`-Parametern nimmt deine
`char`-Stäbe also ohne ein Wort der Beschwerde an, und das erste Nachschlagen
fliegt dir dann zur Laufzeit um die Ohren, mit
`ArrayIndexOutOfBoundsException: Index 65 out of bounds for length 3`. Die 65 ist
der Buchstabe `A`, angekommen dort, wo eine Stabnummer erwartet wurde.

**Zwei Arrays nebeneinander, außerhalb jeder Methode.** Kurs 1 hat jede Variable in
`main` gehalten. Diese beiden müssen von `move` und von der Methode erreichbar
sein, die eine Scheibe trägt, sie kommen also in die Klasse, neben `main` statt
hinein, und sie sind `static` aus demselben Grund, aus dem hier jede Methode es ist:

```java
static final int DISCS = 3;
static final int PEGS = 3;
static final int[][] TOWERS = new int[PEGS][DISCS];
static final int[] HEIGHTS = new int[PEGS];
```

**`int[][]` ist ein Array aus Arrays**, und das brauchte Kurs 1 nicht.
`new int[PEGS][DISCS]` baut drei Arrays mit je `DISCS` Plätzen, eines pro Stab.
`TOWERS[peg][0]` ist die unterste Scheibe dieses Stabs, `HEIGHTS[peg]` sagt, wie
viele darauf liegen, die oberste sitzt also bei `TOWERS[peg][HEIGHTS[peg] - 1]`.
Dieses Paar ist ein Stapel: ablegen heißt, bei `HEIGHTS[peg]` schreiben und eins
dazuzählen, herunternehmen heißt, eins abziehen und lesen.

Beachte, dass `final` hier heißt, dass das Array nie gegen ein anderes getauscht
wird. Die Zahlen darin ändern sich ständig, und das ist erlaubt.

Eine Warnung vorweg: Dieser Abschnitt und der gelbe darüber vertragen sich nicht.
Ein `static final`-Feld kann keinen Wert bekommen, den jemand tippt. Wenn du die
Scheibenzahl also per `Scanner` erfragt hast, heb dir diese Fassung in einer
eigenen Datei auf.

Füll Stab 0 mit den Scheiben, die größte unten, und ersetz dann das `println` in
der Mitte von `move` durch den Aufruf einer Methode, die die Arbeit macht:

```java
static void carry(int from, int to) {
    // dein Code hier:
    //   1. wenn der Stab "from" leer ist, gib eine Beschwerde aus und kehr zurück
    //   2. lies die oberste Scheibe vom Stab "from"
    //   3. wenn der Stab "to" nicht leer ist und seine oberste Scheibe kleiner
    //      ist, gib eine Beschwerde aus und kehr zurück, ohne etwas zu bewegen
    //   4. sonst nimm sie vom einen Stab, leg sie auf den anderen, pass beide
    //      Höhen an, und zeichne die Stäbe
}
```

Schritt 1 und 3 sind die eigentliche Übung. Es sind Prüfungen, die nie auslösen
sollten: Wenn die Rekursion stimmt, wird nie ein illegaler Zug versucht. Einen Test
für etwas zu schreiben, von dem du glaubst, dass es nicht passieren kann, fühlt
sich sinnlos an, bis er das erste Mal auslöst.

Schritt 1 sieht neben Schritt 3 überflüssig aus und ist es nicht. Ist ein Zug erst
einmal abgelehnt, läuft die Folge trotzdem weiter und will früher oder später eine
Scheibe von einem leeren Stab heben, und das ist ohne die Prüfung ein
`HEIGHTS[from] - 1` von `-1` und ein Absturz. Die Prüfung macht aus dem Absturz
eine zweite Beschwerde, und genau das will man von etwas, dessen Aufgabe das
Berichten ist.

Damit du sie auslösen siehst, mach die Rekursion absichtlich kaputt. Vertausch
zwei Argumente in einem der rekursiven Aufrufe und führ es noch einmal aus. Du
solltest eine Handvoll Beschwerden bekommen und ein Programm, das trotzdem bis zum
Ende läuft.

Zum Zeichnen hält `printf` mit einer Breite aus Kurs 1 die drei Stäbe in Spalten.
Eine Scheibe der Größe 3 kann aus drei `#` bestehen, eine leere Ebene aus einem
einzelnen `|`. Bau die Zeile mit den Stabbuchstaben aus `PEGS` und derselben
Breite, statt `A    B    C` hinzuschreiben, sonst steht sie schief, sobald sich
`DISCS` ändert.

```
  |    |    |
  ##   |    |
  ###  |    #
  A    B    C
```

Falls du feststeckst, zeigt
[`code/TowersOfHanoiTowers.java`](../code/TowersOfHanoiTowers.java) eine
funktionierende Fassung.

## Was du gelernt hast

- Rekursion: eine Methode, die sich selbst mit einer kleineren Fassung des Problems
  aufruft
- Den Basisfall, und dass ein `StackOverflowError` so aussieht, wenn er fehlt
- Dass Aufrufe sich stapeln und warten, und dass eine Methode deshalb nach ihrem
  eigenen rekursiven Aufruf weiterarbeiten kann
- Einen Aufruf an etwas zu schreiben, das noch nicht fertig ist, und ihm zu
  vertrauen
- Warum sich die Zahl der Züge mit jeder Scheibe verdoppelt
- *(optional)* Aus einer rekursiven Methode eine Anzahl zurückgeben, indem man die
  Teile zusammenzählt
- *(optional)* `1 << n` als den ganzzahligen Weg, zwei hoch n zu sagen
- *(optional)* Stäbe als Zahlen statt als Buchstaben, und die stille Umwandlung von
  `char` zu `int`, die den Fehler bis zum Absturz verdeckt
- *(optional)* Felder außerhalb einer Methode, und `int[][]` als Array aus Arrays
- *(optional)* Zwei parallele Arrays als Stapel, und Prüfungen, die nie auslösen
  sollten

## Weiter

Kurs 4 ist Conways Game of Life, wo sich ein Gitter nach vier Regeln selbst
verändert und überhaupt niemand mitspielt. Er setzt Kurs 1 voraus und sonst nichts.

Die vollständige Liste steht in
[PROJECT-IDEAS.de.md](../../../PROJECT-IDEAS.de.md).
