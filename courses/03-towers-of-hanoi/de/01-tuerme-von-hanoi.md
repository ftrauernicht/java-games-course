🇩🇪 Deutsch | 🇬🇧 [English](../en/01-towers-of-hanoi.md)

[← Zurück zur Kursübersicht](../../../README.de.md) · Setzt voraus: [Kurs 1 – Grundlagen](../../01-basics/de/00-einfuehrung.md)

# Kurs 3 – Türme von Hanoi

**Ziel:** das kürzeste Programm in diesem Repository, und das, welches am
schwersten zu glauben ist. Drei Stäbe, ein Stapel Scheiben, und eine Lösung aus
drei Zeilen, weil sie sich selbst um Hilfe bittet.

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

Das Programm, das du gleich schreibst, hat drei Zeilen.

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
innere fertig ist. Bei drei Scheiben stehen an der tiefsten Stelle drei davon
übereinander, jeder mit seinem eigenen `n`, `from`, `to` und `via`. Dieser Stapel
ist der Grund, warum die Methode nach dem ersten rekursiven Aufruf „zurückkommen"
und ihre zweite Hälfte erledigen kann.

## 🟢 Kernübung — Schreib move

Drei Zeilen, in dieser Reihenfolge:

1. Bring `n - 1` Scheiben von `from` nach `via`. Überleg, welcher Stab währenddessen
   frei ist: Du willst nach `via`, also steht `to` leer herum.
2. Gib den Zug von Scheibe `n` von `from` nach `to` als eine Textzeile aus.
3. Bring `n - 1` Scheiben von `via` nach `to`. Jetzt ist `from` der freie Stab,
   denn der ist gerade leer geworden.

Und über allen dreien der Basisfall: Wenn `n` gleich 0 ist, kehr sofort zurück.

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
    // dein Code hier -- dieselben drei Schritte, aber zaehl zusammen, was die
    // beiden rekursiven Aufrufe zurueckgeben, und gib die Summe zurueck
}
```

Für die Formel ist `(1 << n) - 1` zwei hoch `n`, minus eins. Das `<<` schiebt die
Binärziffern einer Zahl nach links, und jede Verschiebung verdoppelt sie, `1 << 10`
ist also 1024. `Math.pow(2, n)` liefert dieselbe Antwort als `double`, was danach
einen Cast braucht und für große `n` leicht danebenliegt. Die Verschiebung ist hier
also das ehrliche Werkzeug.

Gib beides aus und prüf, dass sie für jedes `n` von 1 bis 20 übereinstimmen.

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

Bau den Zustand und führ die Züge aus.

```java
static final int[][] TOWERS = new int[3][DISCS];
static final int[] HEIGHTS = new int[3];
```

`TOWERS[peg][0]` ist die unterste Scheibe dieses Stabs, und `HEIGHTS[peg]` sagt,
wie viele Scheiben darauf liegen. Die oberste Scheibe eines Stabs liegt also bei
`HEIGHTS[peg] - 1`. Dieses Arraypaar ist ein Stapel, gebaut aus dem, was Kurs 1 dir
gegeben hat.

Füll Stab 0 mit den Scheiben, die größte unten. Ersetz dann das `println` in der
Mitte von `move` durch den Aufruf einer Methode, die die Arbeit macht:

```java
static void carry(int from, int to) {
    // dein Code hier:
    //   1. lies die oberste Scheibe vom Stab "from"
    //   2. wenn der Stab "to" nicht leer ist und seine oberste Scheibe kleiner
    //      ist, gib eine Beschwerde aus und kehr zurueck, ohne etwas zu bewegen
    //   3. sonst nimm sie vom einen Stab, leg sie auf den anderen, pass beide
    //      Hoehen an, und zeichne die Staebe
}
```

Schritt 2 ist die eigentliche Übung. Es ist eine Prüfung, die nie auslösen sollte:
Wenn die Rekursion stimmt, wird nie ein illegaler Zug versucht. Einen Test für
etwas zu schreiben, von dem du glaubst, dass es nicht passieren kann, fühlt sich
sinnlos an, bis er das erste Mal auslöst.

Damit du ihn auslösen siehst, mach die Rekursion absichtlich kaputt. Vertausch zwei
Argumente in einem der rekursiven Aufrufe und führ es noch einmal aus.

Zum Zeichnen hält `printf` mit einer Breite aus Kurs 1 die drei Stäbe in Spalten.
Eine Scheibe der Größe 3 kann aus drei `#` bestehen, eine leere Ebene aus einem
einzelnen `|`.

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
- *(optional)* Zwei parallele Arrays als Stapel, und eine Prüfung, die nie auslösen
  sollte

## Weiter

Kurs 4 ist Conways Game of Life, wo sich ein Gitter nach vier Regeln selbst
verändert und überhaupt niemand mitspielt. Er setzt Kurs 1 voraus und sonst nichts.

Die vollständige Liste steht in
[PROJECT-IDEAS.de.md](../../../PROJECT-IDEAS.de.md).
