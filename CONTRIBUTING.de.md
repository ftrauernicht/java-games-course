🇩🇪 Deutsch | 🇬🇧 [English](CONTRIBUTING.md)

# Mitwirken bei Java Games Course

Das hier ist ein privates Projekt, das ich allein pflege.

- Issues und Pull Requests sind willkommen, aber es gibt keinen Fahrplan, in den du dich einfügen musst.
- Halte Pull Requests klein und auf eine Sache beschränkt; ein ganzer neuer Kurs auf einmal lässt sich schlecht prüfen.
- Es gibt keinen CLA.
- Die Antwortzeiten schwanken. Das Projekt läuft nebenher.

## Einen neuen Kurs hinzufügen

**Struktur**

- Ein Kurs ist ein Ordner `courses/NN-name/`, nummeriert in der Reihenfolge, in der
  er entstanden ist. Wer einen hinzufügt, aktualisiert die Kurstabelle in `README.md`
  und `README.de.md`, den Verzeichnisbaum in beiden, und den passenden Eintrag in
  `PROJECT-IDEAS.md` und `PROJECT-IDEAS.de.md`.
- Der Ordnername ist englisch, auch wenn der deutsche Titel abweicht.
- **Kurs 1 ist die einzige Voraussetzung.** Jeder Kurs ab 2 setzt Kurs 1 voraus und
  sonst nichts, niemals einen Geschwisterkurs. Braucht ein Kurs etwas, das Kurs 1
  nicht vermittelt, erklärt er es im eigenen Kapitel in einem knappen Abschnitt
  `## 🟢 Kern -- X, kurz erklärt`. Dieser Abschnitt beginnt mit einem
  Überspring-Hinweis, der *alle* Geschwister nennt, die dasselbe lehren, nicht nur
  die früheren: Jemand kann bei Kurs 4 ankommen und vorher nur Kurs 5 gemacht haben.
- Parallele Ordner `en/` und `de/`, inhaltlich und strukturell gleich. Niemals eine
  einzelne zweisprachige Datei. Kapitel-Dateinamen werden übersetzt, die Nummer
  davor nicht, `01-towers-of-hanoi.md` und `01-tuerme-von-hanoi.md` sind also ein
  Paar. Umlaute im Dateinamen werden umschrieben: `ue`, `oe`, `ae`.
- Code und Codekommentare sind immer englisch, in beiden Sprachfassungen. Nur die
  Prosa unterscheidet sich, und die deutsche duzt.
- In jedem Kapitel dasselbe dreistufige System, einmal in Kurs 1 Kapitel 0 eingeführt
  und danach nie wieder erklärt: 🟢 Kern (nötig), 🟡 Optional (mehr Übung),
  🔴 Optional (eine echte Herausforderung).
- In `code/` liegt ein fertiger, lauffähiger Lösungsschlüssel für den 🟢-Pfad. Ein
  optionaler Abschnitt wird dort nur gelöst, wenn er eine eigene Datei mit eigener
  `main` bekommt. So stolpert niemand über die Lösung der Kernübung, während er
  etwas anderes sucht.
- **Screenshots sind die Ausnahme, nicht die Regel.** Ein Konsolenspiel zeigt
  stattdessen einen eingerahmten Block mit Beispielausgabe. `assets/` gibt es nur
  für einen Kurs mit echtem Fenster.
- Das Javadoc gibt es nicht auf Deutsch. Die deutschen Kapitel verlinken dieselben
  englischen Seiten wie die englischen und sagen das einmal nebenbei, statt sich in
  jedem Kapitel dafür zu entschuldigen.

**Java-Regeln, an die sich Kapitel halten müssen**

Das sind keine Geschmacksfragen. In jede dieser Fallen tappt ein Anfänger einmal
und verliert danach einen Nachmittag.

- **Eingaben ausschließlich mit `nextLine()` lesen**, danach
  `Integer.parseInt(zeile.trim())`. `nextInt()` lässt den Zeilenumbruch im Puffer
  stehen, sodass das nächste `nextLine()` eine leere Zeichenkette liefert; und bei
  Buchstaben wirft es, ohne das Wort zu verbrauchen, was eine naive Wiederholschleife
  endlos dreht.
- **Einen `Scanner` auf `System.in` nie schließen.** Damit ist `System.in` für den
  Rest der Programmlaufzeit tot. Die IDE meldet den offenen Scanner als Ressourcenleck;
  diese Warnung ist hier falsch, und das Kapitel sagt das an der Stelle, an der der
  Scanner zuerst auftaucht.
- **Programmausgabe ist reines ASCII.** `System.out` kodiert mit der Codepage der
  Konsole, und die ist unter Windows nicht UTF-8, ein Rahmenzeichen kommt also als
  `?` an. Spielfelder bestehen aus `|`, `-`, `+`, `#` und `.`. Die Kapitel behalten
  ihre Emoji-Marker, die `.java`-Dateien bleiben reines ASCII.
- **Den Bildschirm zu löschen ist nicht portabel.** Die ANSI-Sequenz funktioniert im
  Windows Terminal und scheitert im alten Konsolenfenster. Der 🟢-Pfad druckt die
  Zustände stattdessen nacheinander unter je einer Überschrift. Die ANSI-Variante
  darf als 🟡 auftauchen, mit ausgeschriebenem Vorbehalt.
- **Eine öffentliche Klasse liegt in einer Datei mit genau ihrem Namen**, und eine
  Klasse darf nicht mit einer Ziffer beginnen. Deshalb liefert Kurs 5 `Game2048` in
  `Game2048.java` aus.
- **Mehrdateiige Kurse übersetzt man mit `javac *.java` und startet sie mit
  `java Main`.** Nicht mit `java Main.java`: Der Einzeldatei-Start übersetzt nur die
  Datei, die er bekommt, und mehrdateiiger Quellstart kam erst mit Java 22, während
  dieses Repository Java 21 anspricht.
- Aller Kurscode liegt im Default-Package. Echte Projekte machen das anders, und
  jeder Kurs mit mehr als einer Datei sagt das in einem Satz.
- Keine Preview-Features. Kein instanzbasiertes `main`, keine String-Templates. Java
  kennt keine String-Interpolation; die Kapitel nutzen `+` und `printf`.

**Vor der Veröffentlichung eines Kapitels auf diese wiederkehrenden Fehler prüfen**

Das ist die Checkliste der Geschwister-Repos für JavaScript und C#, ergänzt um die
Java-Punkte.

- Veraltete Verweise auf "Kurs N" und "Kapitel N". Im Singular und im Plural suchen,
  in beiden Sprachen.
- Ein Versprechen, etwas "später" zu erklären, das nie eingelöst wird. Entweder
  einlösen oder anders formulieren.
- Die Behauptung "braucht nur Kurs 1" gegen den tatsächlichen Inhalt von Kurs 1
  prüfen, nicht gegen die Erinnerung daran.
- **Jeder `code/`-Ordner muss unter den CI-Schaltern sauber übersetzen**, und dazu
  gehört `-Werror`. Eine Musterlösung, die mit einer Warnung durchläuft, bringt
  jemandem den falschen Reflex bei, der noch nicht unterscheiden kann, welche
  Warnungen zählen.
- **Jedes Snippet vor der Veröffentlichung ausführen**, nicht nur das fertige
  Programm. Ein Snippet mit `Thread.sleep` lässt sich außerhalb einer Methode, die
  die Ausnahme deklariert, nicht übersetzen, und so etwas findet nur, wer es laufen
  lässt.
- An jeder Stelle, die sich auf zwei Arten lesen lässt, noch einmal sagen, wohin der
  Code gehört.
- **Jedes Snippet muss für sich übersetzen.** Ein Fragment, das mit einer
  öffnenden Klammer endet, liefert `reached end of file while parsing`, und das
  zeigt auf die letzte Zeile der Datei statt auf die Ursache. Braucht ein Snippet
  eine umgebende Variable oder Methode, zeig sie.
- **Ein Kapitel, das eine Klasse aufbaut, druckt sie einmal als vollständiges
  Gerüst** -- mit allen Importen, allen Feldern und allen Methodenköpfen, die
  später einzeln kommen -- bevor das erste Teilstück erklärt wird. „Wohin gehört
  das" ist die Frage, die ein Anfänger allein nicht beantworten kann, und ein
  Feld, das benutzt und nie deklariert wird, hält ihn länger auf als jede
  Erklärung ihm hilft.
- **Importe gehören ins Kapitel, nicht nur nach `code/`.** Kurs 1 zeigt einen
  einzigen Import. Dass `JPanel` in `javax.swing` liegt und `Graphics` in
  `java.awt`, lässt sich nicht erraten.
- **Jedes abgedruckte Listing übersetzt so, wie es dasteht.** Vor allem der
  Zugriff auf eine Konstante einer anderen Klasse: innerhalb der Klasse `COLS`,
  von außen `SnakePanel.COLS`. Ein Listing, das den Leser zwingt, die Zahl ein
  zweites Mal anzulegen, lehrt genau das Falsche.
- **Jedes Schlüsselwort und jede Methode wird dort erklärt, wo sie zuerst
  auftaucht**, auch die kleinen, die man übersieht: `new`, `null`, `isEmpty`.
- **Die Datei in `code/` folgt dem Kapitel**: gleiche Reihenfolge, gleiche Werte,
  gleiche Variablennamen. Wer feststeckt, legt beides nebeneinander.
- **Druck keine Zeilennummer ab, die der Leser nicht reproduzieren kann.** Entweder
  die Meldung ohne sie zeigen, oder dazusagen, dass sie von der Position abhängt.
- **Jede Behauptung der Form „mach es falsch, und du siehst Y" wird vor der
  Veröffentlichung nachgestellt.** Das ist die Sorte Satz, die ein aufmerksamer
  Leser nachprüft, und wenn Y dann nicht eintritt, verliert er das Vertrauen auch
  in die Begründungen, die stimmen. Dasselbe gilt für jede Zahl: Bildrate,
  Fenstergröße, Zeichenfläche.
- **Ein Screenshot wird aus dem ausgelieferten Code erzeugt**, nicht von Hand
  gestellt. Was darauf zu sehen ist, muss zur Bildunterschrift und zum Kapitel
  passen -- die Gliederzahl zum Punktestand, die sichtbaren Teile zu dem, was die
  Zeichenmethode in dieser Lage wirklich malt.
- Jeden Kurs geht ein frischer Leser durch, der nur Kurs 1 kennt, tippt jedes
  Snippet ab und führt es aus. Wo er hängenbleibt, liegen die Fehler des Kapitels.

**Wie viel Code man zeigt**

Die Kurse 1 und 2 geben vollständigen, lauffähigen Code zum Abtippen. Ab Kurs 3
erklärt ein Kapitel den neuen Baustein vollständig und beschreibt die Kernübung --
also genau das, worum es im Kapitel geht -- als nummerierte Schritte mit einem Gerüst:

```java
static int countNeighbours(boolean[][] grid, int row, int col) {
    // dein Code hier -- die vier Schritte oben
}
```

Die fertige Fassung liegt nur in `code/`. Das Kapitel nennt sie als Rückfalloption
für jemanden, der feststeckt, nicht als das Nächste zum Lesen.

**Commit-Nachrichten**

Conventional Commits, im Imperativ und klein geschrieben. Ein neuer Kurs ist meist
ein `feat: add Course 3 - Towers of Hanoi`, das Kapiteltext, Code und die
Navigationsänderungen zusammen trägt. Weitere verwendete Präfixe: `fix:`, `docs:`,
`ci:`, `chore:`.
