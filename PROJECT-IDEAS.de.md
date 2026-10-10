🇩🇪 Deutsch | 🇬🇧 [English](PROJECT-IDEAS.md)

[← Zurück zur Kursübersicht](README.de.md)

# Der Kursfahrplan dieses Repositorys

Das hier ist keine Liste von Nebenprojekten. Jeder Eintrag unten ist entweder ein
Kurs, den dieses Repository gebaut hat, oder einer, den es bauen will, in der
Reihenfolge, in der die Spiele schwerer werden. Die Schwierigkeit meint das Denken,
das ein Spiel verlangt, nicht die Zahl der Zeilen.

| # | Kurs | Was neu dazukommt | Schwierigkeit | Stand |
|---|---|---|---|---|
| 1 | Grundlagen -- die Sprache selbst, noch ohne Spiel | Typen, Operatoren, Verzweigungen und Schleifen, Methoden, Arrays, Tastatureingabe | ⭐ | ✅ [Gebaut](courses/01-basics/de/00-einfuehrung.md) |
| 2 | Tic-Tac-Toe -- zwei Spieler, ein Feld, neun Kästchen | ein Spielfeld als zweidimensionales Array, verschachtelte Schleifen, ein Array an eine Methode übergeben | ⭐ | ✅ [Gebaut](courses/02-tic-tac-toe/de/01-tic-tac-toe.md) |
| 3 | Türme von Hanoi -- das Rätsel löst sich selbst | Rekursion, der Basisfall, einen Aufrufstapel lesen | ⭐⭐ | ✅ [Gebaut](courses/03-towers-of-hanoi/de/01-tuerme-von-hanoi.md) |
| 4 | Conways Game of Life -- Zellen leben und sterben von allein | den nächsten Zustand in ein zweites Gitter rechnen, Nachbarn am Rand zählen, zwischen den Bildern warten | ⭐⭐ | ✅ [Gebaut](courses/04-game-of-life/de/01-spiel-des-lebens.md) |
| 5 | 2048 -- schieben, verschmelzen, von vorn | ein Gitter umformen, vier Richtungen auf eine Operation zurückführen, die Verschmelzungsregel, die alle falsch machen | ⭐⭐⭐ | ✅ [Gebaut](courses/05-2048/de/01-2048.md) |
| 6 | Snake -- das erste im Fenster | Swing, auf eine Fläche zeichnen, ein Timer als Spielschleife, Tasten im Moment des Drucks lesen | ⭐⭐⭐ | Geplant |
| 7 | Flappy Bird -- Schwerkraft, Röhren, eine Taste | Bewegung als Geschwindigkeit, eine Klasse, die Daten hält, Kollision über Rechtecke | ⭐⭐⭐⭐ | Geplant |
| 8 | Pong -- zwei Schläger und ein Winkel | einen Ball an einer Fläche spiegeln, ein zweiter Spieler an derselben Tastatur | ⭐⭐⭐ | Idee |
| 9 | Minesweeper -- zählen, was man nicht sieht | Mauseingaben, Flutfüllung ab einem Klick, ein zweites Gitter, das das erste verdeckt | ⭐⭐⭐⭐ | Idee |
| 10 | Breakout -- Pong mit Folgen | ein Gitter zerstörbarer Steine, Level, ein Punktestand, der die Runde überlebt | ⭐⭐⭐⭐ | Idee |
| 11 | Tetris -- das Spiel, das seinen Ruf verdient | eine Form um einen Punkt drehen, Kollision vor dem Zug prüfen, Reihen löschen und nachrutschen lassen | ⭐⭐⭐⭐⭐ | Idee |
| 12 | Das blanke JDK verlassen | ein Build-Werkzeug, Abhängigkeiten, und die erste echte Testsuite | ⭐⭐⭐ | Idee |

Ein paar Hinweise, wie diese Liste zu lesen ist:

- **Kurs 1 ist überall die einzige Voraussetzung.** Die Kurse ab 2 lassen sich in
  beliebiger Reihenfolge machen. Wo ein späteres Spiel etwas braucht, das Kurs 1
  nicht vermittelt, erklärt dieser Kurs es noch einmal kurz, mit einem Hinweis, dass
  du weiterspringen kannst, wenn du es schon kennst.
- **Die Reihenfolge richtet sich nach der Schwierigkeit, nicht nach Abhängigkeiten.**
  Tic-Tac-Toe kommt vor den Türmen von Hanoi, weil Rekursion schwerer im Kopf zu
  behalten ist als eine verschachtelte Schleife, obwohl die Hanoi-Lösung fünf Zeilen
  hat und die für Tic-Tac-Toe hundert.
- **Die Kurse 1 und 2 geben dir fertigen Code zum Abtippen.** Ab Kurs 3 bekommst du
  den neuen Baustein vollständig erklärt und setzt den Kern des Spiels dann selbst
  aus beschriebenen Schritten zusammen. Die fertige Fassung liegt immer bereit,
  falls du sie brauchst.
- **Bei Kurs 5 endet der Weg der Konsole.** Java kann aus einem Terminal keinen
  einzelnen Tastendruck lesen, deshalb verlangt 2048 einen Buchstaben und Enter.
  Genau diese Grenze ist der Grund, warum die letzten beiden Kurse in ein Fenster
  umziehen, wo eine Taste im Moment des Drucks ankommt.
- **Nur Kurs 12 würde ein Build-Werkzeug hinzunehmen.** Alles davor läuft mit den
  zwei Befehlen aus Kurs 1, und das ist der Punkt: Zwischen dir und dem Programm
  steht nichts außer einem Compiler.
