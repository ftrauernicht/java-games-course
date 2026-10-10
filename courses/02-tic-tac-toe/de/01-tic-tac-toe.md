🇩🇪 Deutsch | 🇬🇧 [English](../en/01-tic-tac-toe.md)

[← Zurück zur Kursübersicht](../../../README.de.md) · Setzt voraus: [Kurs 1 – Grundlagen](../../01-basics/de/00-einfuehrung.md)

# Kurs 2 – Tic-Tac-Toe

**Ziel:** ein vollständiges, spielbares Spiel für zwei Leute an einer Tastatur. Ein
Spielfeld, das sich selbst zeichnet, Züge, die geprüft werden, bevor sie zählen,
ein Sieger, der auch erkannt wird, und ein Punktestand über mehrere Runden.

Das ist das erste Spiel, deshalb bekommst du den ganzen Code. Tipp ihn Stück für
Stück ab und führ ihn nach jedem Abschnitt aus. Ab Kurs 3 bekommst du weniger, mit
Absicht.

Alles, was dieses Kapitel braucht, steht in
[Kurs 1](../../01-basics/de/00-einfuehrung.md) und sonst nirgends. Eine fertige
Fassung liegt in [`code/TicTacToe.java`](../code/TicTacToe.java).

Fang eine Datei namens `TicTacToe.java` an:

```java
import java.util.Scanner;

public class TicTacToe {

    static final int SIZE = 3;

    public static void main(String[] args) {
    }
}
```

`SIZE` ist `final`, weil ein Tic-Tac-Toe-Feld drei mal drei groß ist und bleibt.
Überall `SIZE` statt `3` zu schreiben kostet jetzt nichts und zahlt sich im letzten
optionalen Abschnitt aus.

## 🟢 Kern — Ein Spielfeld ist ein Array aus Arrays

Kurs 1 hat dir ein Array gegeben: eine Reihe von Werten unter einem Namen. Ein
Spielfeld braucht ein Gitter, und Java baut es aus demselben Teil. Ein Array, dessen
Werte selbst Arrays sind:

```java
char[][] board = new char[SIZE][SIZE];
```

Zwei Klammerpaare, zwei Zahlen. Lies `board[1][2]` als „Zeile 1, Spalte 2", und
denk daran, dass beide bei 0 anfangen. Das ist also die mittlere Zeile, rechte
Spalte.

`char` ist der Typ, weil ein Feld eines von drei Dingen enthält: ein `X`, ein `O`,
oder nichts. Für nichts steht ein Leerzeichen.

Ein frisches `char`-Array ist voll von einem Zeichen, das als gar nichts gedruckt
wird und kein Leerzeichen ist. Füll das Feld also selbst. Schreib diese Methode
neben `main`:

```java
static char[][] newBoard() {
    char[][] board = new char[SIZE][SIZE];
    for (int row = 0; row < SIZE; row++) {
        for (int col = 0; col < SIZE; col++) {
            board[row][col] = ' ';
        }
    }
    return board;
}
```

Eine Schleife in einer Schleife. Die äußere sucht eine Zeile aus, und für jede
Zeile läuft die innere durch alle Spalten. Neun Durchläufe insgesamt, und das ist
die Form fast jedes Codestücks in diesem Kapitel.

## 🟢 Kern — Das Spielfeld ausgeben

Ein leeres Feld zeigt seine eigene Nummer. So sieht der Spieler ohne zweite Skizze,
was er tippen muss, und das Spielfeld erklärt seine eigene Bedienung:

```
 1 | 2 | 3 
---+---+---
 4 | 5 | 6 
---+---+---
 7 | 8 | 9 
```

```java
static void printBoard(char[][] board) {
    System.out.println();
    for (int row = 0; row < SIZE; row++) {
        for (int col = 0; col < SIZE; col++) {
            char mark = board[row][col];
            if (mark == ' ') {
                System.out.printf(" %d ", row * SIZE + col + 1);
            } else {
                System.out.printf(" %c ", mark);
            }
            if (col < SIZE - 1) {
                System.out.print("|");
            }
        }
        System.out.println();
        if (row < SIZE - 1) {
            System.out.println("---+---+---");
        }
    }
    System.out.println();
}
```

Zwei Details, bei denen es sich lohnt, langsamer zu lesen.

`row * SIZE + col + 1` macht aus Zeile und Spalte wieder die Nummer auf dem
Bildschirm. Zeile 0 enthält 1, 2, 3; Zeile 1 enthält 4, 5, 6. Jede Zeile tiefer
kommen drei dazu, daher `row * SIZE`, und das `+ 1` ist da, weil Menschen bei eins
zählen und Arrays nicht.

Die beiden `if` mit `SIZE - 1` sind der Grund, warum rechts kein `|` übersteht und
unter der letzten Zeile keine Strichlinie kommt. Ein Trenner steht *zwischen*
Dingen, also gibt es immer einen weniger als Dinge.

Auf den Bildschirm kommt nur reines ASCII. Java kann hübschere Rahmenzeichen
erzeugen, aber eine Windows-Konsole macht Fragezeichen daraus, und ein Spiel, das
auf der Hälfte der Rechner kaputt aussieht, ist nicht fertig.

Schreib das in `main` und führ es aus:

```java
char[][] board = newBoard();
printBoard(board);
```

Du solltest das Zahlengitter von oben sehen.

## 🟢 Kern — Eine Ziffer, ein Feld

Der Spieler tippt eine Zahl von 1 bis 9. Das Spielfeld will eine Zeile und eine
Spalte. Kurs 1 hat dir beide Hälften der Umrechnung in einem Abschnitt gegeben, und
hier verdienen sie sich ihren Platz:

```java
static void place(char[][] board, int cell, char player) {
    int row = (cell - 1) / SIZE;
    int col = (cell - 1) % SIZE;
    board[row][col] = player;
}
```

Zieh 1 ab, damit die Zahlen von 0 bis 8 laufen statt von 1 bis 9. Dann liefert die
ganzzahlige Division durch 3 die Zeile, denn 0, 1 und 2 ergeben alle 0, während 3,
4 und 5 alle 1 ergeben. Der Rest liefert die Spalte, denn er zählt 0, 1, 2, 0, 1, 2
und fängt genau dort von vorn an, wo eine neue Zeile beginnt.

Rechne Feld 6 auf Papier durch: `6 - 1` ist 5, `5 / 3` ist 1, `5 % 3` ist 2. Zeile
1, Spalte 2, also das rechte Feld der mittleren Zeile. Vergleich das mit dem
Spielfeld oben.

Dieselben zwei Zeilen beantworten, ob ein Feld noch frei ist:

```java
static boolean isFree(char[][] board, int cell) {
    int row = (cell - 1) / SIZE;
    int col = (cell - 1) % SIZE;
    return board[row][col] == ' ';
}
```

Achte darauf, was `place` tut: Es ändert das Spielfeld, das ihm übergeben wurde,
und die Änderung ist nach dem Ende der Methode noch da. Das ist der Abschnitt
„Arrays sind Verweise" aus Kurs 1, der zum ersten Mal echte Arbeit leistet.

## 🟢 Kern — Einen sinnvollen Zug einlesen

An einem Zug können zwei Dinge falsch sein. Er ist vielleicht gar keine Feldnummer,
und er ist vielleicht ein Feld, das schon jemand genommen hat. Keines von beiden
soll das Spiel beenden:

```java
static int askForCell(Scanner in, char[][] board, char player) {
    while (true) {
        System.out.print("Player " + player + ", which square? ");
        String line = in.nextLine().trim();

        if (line.length() != 1 || line.charAt(0) < '1' || line.charAt(0) > '9') {
            System.out.println("Type a single digit from 1 to 9.");
            continue;
        }

        int cell = line.charAt(0) - '0';
        if (!isFree(board, cell)) {
            System.out.println("That square is taken.");
            continue;
        }

        return cell;
    }
}
```

Die Prüfung sind drei mit `||` verbundene Tests, es reicht also, wenn einer davon
zutrifft, um die Zeile abzulehnen: nicht genau ein Zeichen, kleiner als `'1'`, oder
größer als `'9'`. Weil die Felder einstellig sind, ist das einfacher als die
Methode `isWholeNumber` aus Kurs 1, und es schließt `10` und `0` gleich mit aus.

`line.charAt(0) - '0'` macht aus dem Zeichen die Zahl. Zeichen haben Zahlenwerte,
`'7'` liegt genau sieben Plätze hinter `'0'`, und zieht man das eine vom anderen
ab, bleibt 7. Das wirst du in viel Java-Code wiedersehen.

`continue` springt an den Anfang der Schleife und fragt erneut. `return` ist der
einzige Ausgang, und der kommt erst, wenn beide Prüfungen bestanden sind. Was diese
Methode zurückgibt, ist also garantiert ein freies Feld.

## 🟢 Kern — Hat jemand gewonnen?

Acht Linien gewinnen das Spiel: drei Zeilen, drei Spalten, zwei Diagonalen. Zeilen
und Spalten sind regelmäßig genug für eine Schleife, die Diagonalen sind zwei
Sonderfälle:

```java
static boolean hasWon(char[][] board, char player) {
    for (int row = 0; row < SIZE; row++) {
        if (board[row][0] == player
                && board[row][1] == player
                && board[row][2] == player) {
            return true;
        }
    }

    for (int col = 0; col < SIZE; col++) {
        if (board[0][col] == player
                && board[1][col] == player
                && board[2][col] == player) {
            return true;
        }
    }

    if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
        return true;
    }

    return board[0][2] == player && board[1][1] == player && board[2][0] == player;
}
```

Schau dir den Unterschied zwischen den beiden Schleifen an. In der Zeilenschleife
ändert sich der erste Index und der zweite ist ausgeschrieben; in der
Spaltenschleife ist es umgekehrt. Dieser Tausch ist der ganze Unterschied zwischen
einer Zeile und einer Spalte.

Die Methode bekommt den Spieler übergeben, den sie prüfen soll, statt selbst
herauszufinden, wer gewonnen haben könnte. Das hält sie einfach, und der Aufrufer
weiß ohnehin, wer gerade gezogen hat.

Ein Spielfeld kann sich auch füllen, ohne dass jemand gewinnt:

```java
static boolean isFull(char[][] board) {
    for (int row = 0; row < SIZE; row++) {
        for (int col = 0; col < SIZE; col++) {
            if (board[row][col] == ' ') {
                return false;
            }
        }
    }
    return true;
}
```

Ein einziges leeres Feld irgendwo, und die Antwort ist sofort nein. Ein `return`
mitten in der Schleife heißt, dass sie aufhört zu suchen, sobald sie es weiß.

## 🟢 Kern — Eine Runde

Jetzt kommen die Teile zusammen. Eine Runde ist eine Schleife: Spielfeld zeigen,
Zug nehmen, prüfen ob das die Partie beendet hat, Spieler tauschen.

```java
static char playOneRound(Scanner in) {
    char[][] board = newBoard();
    char player = 'X';

    while (true) {
        printBoard(board);

        int cell = askForCell(in, board, player);
        place(board, cell, player);

        if (hasWon(board, player)) {
            printBoard(board);
            System.out.println("Player " + player + " wins!");
            return player;
        }

        if (isFull(board)) {
            printBoard(board);
            System.out.println("A draw.");
            return ' ';
        }

        player = player == 'X' ? 'O' : 'X';
    }
}
```

Drei Dinge zur Reihenfolge, und alle drei stehen in irgendjemandes erster Fassung
falsch.

Die Siegprüfung kommt **nach** dem Zug, nicht davor. Niemand gewinnt auf einem
Feld, auf dem er noch nicht gespielt hat.

Die Vollprüfung kommt **nach** der Siegprüfung. Der neunte Zug kann die Partie
gewinnen, und wer zuerst „ist es voll" fragt, nennt das ein Unentschieden.

Der Tausch ist das **Letzte** in der Schleife, damit die Prüfungen darüber noch den
Spieler meinen, der gerade gezogen hat.

Die Methode gibt zurück, wer gewonnen hat, wobei ein Leerzeichen das Unentschieden
meint. Ein `char` reicht dafür; ein Leerzeichen ist kein Spieler und lässt sich
also nicht mit einem verwechseln.

Jetzt kommt `main` mit drei Zeilen aus:

```java
public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    playOneRound(in);
}
```

Führ es aus und spiel eine ganze Partie. Versuch, über die obere Reihe zu gewinnen,
dann starte neu und spiel auf ein Unentschieden. Tipp einen Buchstaben, wenn es
nach einem Feld fragt, und tipp ein Feld, das schon belegt ist, und prüf, dass
dich keins von beidem rauswirft.

## 🟢 Kern — Noch eine Runde, und ein Punktestand

Eine Partie und das Programm endet, und so spielt niemand Tic-Tac-Toe. Pack die
Runde in eine Schleife und zähl mit:

```java
public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    int winsX = 0;
    int winsO = 0;
    int draws = 0;

    while (true) {
        char winner = playOneRound(in);
        if (winner == 'X') {
            winsX++;
        } else if (winner == 'O') {
            winsO++;
        } else {
            draws++;
        }

        System.out.println("X " + winsX + " - O " + winsO + " - draws " + draws);
        System.out.print("Another round? (y/n) ");
        String answer = in.nextLine().trim().toLowerCase();
        if (!"y".equals(answer)) {
            System.out.println("Thanks for playing.");
            return;
        }
    }
}
```

`"y".equals(answer)` und nicht `answer.equals("y")`, mit dem bekannten Wert vorn.
Kurs 1 hat die Angewohnheit erwähnt; hier ist die Art von Stelle, an die sie
gehört. Und `.equals` statt `==`, weil das Text ist, den jemand getippt hat, und
genau in diesem Fall lügt `==`.

`return` aus `main` beendet das Programm. Die Zähler stehen außerhalb der Schleife,
und deshalb überleben sie von einer Runde zur nächsten.

Das ist das Spiel. Spiel ein paar Runden.

## 🟡 Optional — Eine Siegprüfung, der die Feldgröße egal ist

`hasWon` schreibt `board[row][0]`, `board[row][1]`, `board[row][2]` aus. Setz
`SIZE` auf 4, und diese Methode prüft stillschweigend weiter auf drei in einer
Reihe, auf einem vier Felder breiten Brett, was schlimmer ist als ein Fehler.

Schreib sie so um, dass sie nur noch `SIZE` voraussetzt. Ein Weg: eine Hilfsmethode,
die an einem Feld startet und `SIZE` Schritte in eine Richtung geht.

```java
static boolean allMatch(char[][] board, char player, int row, int col,
                        int rowStep, int colStep) {
    // dein Code hier -- geh SIZE Felder weit von (row, col) aus, zaehl dabei
    // jedes Mal rowStep auf die Zeile und colStep auf die Spalte, und
    // antworte, ob in jedem Feld player stand
}
```

Dann ist jede der acht Linien ein Aufruf. Zeile `i` beginnt bei `(i, 0)` und geht
in Schritten von `(0, 1)`. Die absteigende Diagonale beginnt bei `(0, 0)` und geht
`(1, 1)`. Die anderen beiden Formen findest du selbst heraus, und denk daran, dass
eine der Diagonalen rückwärts läuft.

Setz danach `SIZE` auf 4 und prüf, dass jetzt vier in einer Reihe gewinnen. Die
Ausgabe und `place` kommen schon damit klar, und genau das hat dir `SIZE`
eingebracht.

## 🟡 Optional — Lass den Verlierer anfangen

X zieht in jeder Runde zuerst, und das ist ein echter Vorteil. Merk dir, wer
angefangen hat, und wechsle ab, oder besser: Lass den anfangen, der die letzte
Runde verloren hat.

`playOneRound` muss dafür erfahren, wer beginnt. Gib ihr einen zweiten Parameter
und setz `player` daraus, statt immer `'X'` zu nehmen.

## 🔴 Optional, echte Herausforderung — Ein Computergegner

Ersetz Spieler O durch das Programm, damit einer allein spielen kann.

Fang mit dem einfachsten Gegner an, der nicht kaputt ist: ein zufälliges freies Feld.

```java
static int randomFreeCell(char[][] board) {
    // dein Code hier -- wuerfle eine Zahl von 1 bis 9, bis sie auf ein freies
    // Feld faellt, und gib sie zurueck
}
```

Der spielt regelkonform und schlecht. Jetzt gib ihm zwei Regeln, in dieser
Reihenfolge:

1. Gibt es ein Feld, das die Partie in diesem Zug gewinnt, nimm es.
2. Sonst: Gibt es ein Feld, mit dem der Mensch im nächsten Zug gewinnen würde, nimm
   ihm genau das weg.
3. Sonst: zufällig.

Beide Regeln stellen dieselbe Frage für unterschiedliche Spieler: „Gibt es ein
freies Feld, das für dieses Zeichen eine Linie vollmacht?" Also schreib sie einmal:

```java
static int findWinningCell(char[][] board, char mark) {
    // dein Code hier -- fuer jedes freie Feld: setz mark hinein, frag hasWon,
    // und nimm es danach wieder weg. Gib das erste Feld zurueck, das gewinnt,
    // oder 0, wenn es keines gibt.
}
```

Einen Zug probieren und wieder zurücknehmen ist die Idee, die du aus dieser
Herausforderung mitnimmst. `hasWon` hast du schon, du musst also über Linien gar
nicht nachdenken; du lässt die Methode, der du ohnehin vertraust, eine hypothetische
Frage beantworten. Die 0 für „keines" funktioniert, weil die Felder ab 1 nummeriert
sind.

Teste es ordentlich. Nimm die Felder 1 und 2 und prüf, dass das Programm die 3
nimmt, bevor du es kannst. Dann verschaff dir zwei in einer Reihe, während das
Programm ebenfalls zwei in einer Reihe hat, und prüf, dass es seinen eigenen Sieg
nimmt statt dich zu blocken. Regel 1 vor Regel 2 entscheidet darüber, und sie zu
vertauschen ist der häufigste Weg, das hier falsch zu machen.

Ein Programm mit diesen zwei Regeln lässt sich nicht aus Versehen schlagen, nur mit
einem Plan. Schlagbar bleibt es: Es gibt eine dritte Regel über Gabeln, und darüber
hinaus ein Verfahren namens Minimax, das perfekt spielt, indem es jede mögliche
Fortsetzung durchprobiert. Minimax braucht Rekursion, und das ist Kurs 3. Hier
brauchst du es nicht.

Falls du feststeckst, zeigt
[`code/TicTacToeComputer.java`](../code/TicTacToeComputer.java) eine funktionierende
Fassung.

## Was du gelernt hast

- Ein Gitter als `char[][]`, und warum `board[1][2]` Zeile 1, Spalte 2 ist
- Ein zweidimensionales Array mit einer Schleife in einer Schleife füllen
- Aus einer Zahl mit `/` und `%` eine Zeile und eine Spalte machen
- Dass eine Methode das übergebene Spielfeld ändert und keine Kopie davon
- Eingaben ablehnen, ohne das Spiel zu beenden, mit `continue`
- `line.charAt(0) - '0'`, um aus einem Ziffernzeichen eine Zahl zu machen
- Zeilen, Spalten und Diagonalen prüfen, und warum sich die Schleifen in einem
  Index unterscheiden
- Die Reihenfolge, die eine Spielschleife braucht: ziehen, Sieg, voll, tauschen
- Zähler, die außerhalb der Schleife leben und deshalb jeden Durchlauf überstehen
- *(optional)* Eine Prüfung schreiben, die nur von `SIZE` abhängt
- *(optional)* Einen Zug probieren und zurücknehmen, um eine Was-wäre-wenn-Frage zu
  stellen

## Weiter

Kurs 3 sind die Türme von Hanoi, das kürzeste Programm in diesem Repository und
eines der am schwersten zu glaubenden. Er setzt Kurs 1 voraus und sonst nichts, wie
jeder Kurs hier.

Die vollständige Liste steht in
[PROJECT-IDEAS.de.md](../../../PROJECT-IDEAS.de.md).
