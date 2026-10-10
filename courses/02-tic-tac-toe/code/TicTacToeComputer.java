// Course 2 - the red challenge, in full: a computer opponent.
// Read this only after you have tried it yourself.
//
// A separate program with its own main, so the answer to the challenge is
// not sitting in the middle of the answer to the chapter.
//
// Compile with:  javac TicTacToeComputer.java
// Run with:      java TicTacToeComputer
import java.util.Scanner;

public class TicTacToeComputer {

    static final int SIZE = 3;
    static final char HUMAN = 'X';
    static final char COMPUTER = 'O';

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        char[][] board = newBoard();
        char player = HUMAN;

        while (true) {
            printBoard(board);

            int cell;
            if (player == HUMAN) {
                cell = askForCell(in, board);
            } else {
                cell = chooseCell(board);
                System.out.println("The computer takes square " + cell + ".");
            }
            place(board, cell, player);

            if (hasWon(board, player)) {
                printBoard(board);
                if (player == HUMAN) {
                    System.out.println("You win!");
                } else {
                    System.out.println("The computer wins.");
                }
                return;
            }

            if (isFull(board)) {
                printBoard(board);
                System.out.println("A draw.");
                return;
            }

            player = player == HUMAN ? COMPUTER : HUMAN;
        }
    }

    // --- The computer's two rules ---

    // 1. If a square wins the game this turn, take it.
    // 2. If a square would let the human win next turn, take it away.
    // 3. Otherwise pick a free square at random.
    //
    // Both rules are the same question asked twice, which is why they share
    // one helper: "is there a square that completes a line for this mark?"
    static int chooseCell(char[][] board) {
        int winning = findWinningCell(board, COMPUTER);
        if (winning != 0) {
            return winning;
        }

        int blocking = findWinningCell(board, HUMAN);
        if (blocking != 0) {
            return blocking;
        }

        return randomFreeCell(board);
    }

    // Tries every free square in turn, pretends the mark is there, and asks
    // whether that would be a win. Undoes the move either way. Returns 0
    // when no square does it -- 0 is safe as "none", because squares are
    // numbered from 1.
    static int findWinningCell(char[][] board, char mark) {
        for (int cell = 1; cell <= SIZE * SIZE; cell++) {
            if (!isFree(board, cell)) {
                continue;
            }
            place(board, cell, mark);
            boolean wins = hasWon(board, mark);
            clear(board, cell);
            if (wins) {
                return cell;
            }
        }
        return 0;
    }

    static int randomFreeCell(char[][] board) {
        while (true) {
            int cell = (int) (Math.random() * (SIZE * SIZE)) + 1;
            if (isFree(board, cell)) {
                return cell;
            }
        }
    }

    // --- Below here, the same as in TicTacToe.java, with two differences ---
    //
    // clear() is new: the two rules above try a move and take it back, and
    // the chapter's version never had to empty a square.
    //
    // askForCell() has no player parameter, because only one human plays.

    static char[][] newBoard() {
        char[][] board = new char[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                board[row][col] = ' ';
            }
        }
        return board;
    }

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

    static void place(char[][] board, int cell, char player) {
        int row = (cell - 1) / SIZE;
        int col = (cell - 1) % SIZE;
        board[row][col] = player;
    }

    // Puts a square back to empty, so a trial move can be undone.
    static void clear(char[][] board, int cell) {
        int row = (cell - 1) / SIZE;
        int col = (cell - 1) % SIZE;
        board[row][col] = ' ';
    }

    static boolean isFree(char[][] board, int cell) {
        int row = (cell - 1) / SIZE;
        int col = (cell - 1) % SIZE;
        return board[row][col] == ' ';
    }

    static int askForCell(Scanner in, char[][] board) {
        while (true) {
            System.out.print("Your move, which square? ");
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

    // Eight ways to win: three rows, three columns, two diagonals.
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
}
