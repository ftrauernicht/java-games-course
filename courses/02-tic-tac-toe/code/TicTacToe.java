// Course 2 - Tic-Tac-Toe.
// A finished version of the green path. The yellow sections are practice
// and are not solved here; the red challenge lives in its own file,
// TicTacToeComputer.java, so it is not in the way.
//
// Compile with:  javac TicTacToe.java
// Run with:      java TicTacToe
import java.util.Scanner;

public class TicTacToe {

    static final int SIZE = 3;

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

    // Plays one game and returns 'X', 'O', or ' ' for a draw.
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

    // --- The board ---

    static char[][] newBoard() {
        char[][] board = new char[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                board[row][col] = ' ';
            }
        }
        return board;
    }

    // An empty square shows its number, so the player can see what to type.
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

    // --- Moves ---

    // Square numbers run 1 to 9 across the rows. Integer division gives the
    // row, the remainder gives the column -- both from Course 1.
    static void place(char[][] board, int cell, char player) {
        int row = (cell - 1) / SIZE;
        int col = (cell - 1) % SIZE;
        board[row][col] = player;
    }

    static boolean isFree(char[][] board, int cell) {
        int row = (cell - 1) / SIZE;
        int col = (cell - 1) % SIZE;
        return board[row][col] == ' ';
    }

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

    // --- Deciding the game ---

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
