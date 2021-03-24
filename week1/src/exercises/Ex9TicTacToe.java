package exercises;

import java.util.Random;
import java.util.Scanner;

import static java.lang.System.*;

/*
 *  The TicTacToe Game
 *  See https://en.wikipedia.org/wiki/Tic-tac-toe.
 *
 *  This is exercising functional decomposition and testing
 *  - Any non trivial method should be tested (in test() method far below)
 *  - IO methods never tested.
 *
 *  NOTE: Just use an array for the board (we print it to look square, see plotBoard())
 *
 */
public class Ex9TicTacToe {

    public static void main(String[] args) {
        new Ex9TicTacToe().program();
    }

    final Scanner sc = new Scanner(in);
    final Random rand = new Random();
    final char EMPTY = '-';        // This is so that we easy can change the value in one place

    void program() {
        //test();       // <--------- Comment out to test

        Player p1 = new Player("olle", 'X');
        Player p2 = new Player("fia", 'O');
        Player current;         // For now
        Player winner = null;
        char[] board = createBoard();  // alt. { EMPTY, EMPTY, ... }

        out.println("Welcome to Tic Tac Toe, board is ...");
        plotBoard(board);

        // TODO Add game logic here (use smallest step and functional decomposition)
        if (rand.nextBoolean()) {
            current = p1;
        } else {
            current = p2;
        }
        while (!isFull(board)) {
            int index;
            while (true) {
                index = getPlayerSelection(current);
                if (isEmpty(board, index)) {
                    break;
                }
            }
            setMark(board, index, current.mark);

            if (hasWinner(board, current.mark)) {
                winner = current;
                break;
            }
        plotBoard(board);

        if (current == p1) {
            current = p2;
        } else {
            current = p1;
        }
    }
        out.println("Game over!");
        plotBoard(board);
        if (winner != null) {
            out.println("Winner is " + current.name);
        } else {
            out.println("Draw");
        }
    }


    // ---------- Methods below this ----------------
    char[] createBoard() {
        char[] board = new char[9];
        for (int i = 0; i < board.length; i++) {
            board[i] = EMPTY;
        }
        return board;
    }

    void setMark (char[] board, int index, char mark){
        board [index] = mark;
    }

    boolean isFull (char [] board ){
        for(int i = 0; i <board.length; i++){
            if (board[i] == EMPTY){
                return false;
            }
        }
        return true;
    }

    boolean hasWinner (char [ ] board, char mark) {
      return  hasUpDiagonal(board, mark) ||  hasDownDiagonal(board, mark) || hasRow (board, mark) ||
                hasColumn(board, mark);
    }

    boolean hasColumn(char[] board, char mark) {
        for (int i = 0; i < 3; i++) {
            if (board[i] == mark && board[i + 3] == mark && board[i + 6] == mark){
                return true;
            }
        }
        return false;
    }

    boolean hasRow(char[] board, char mark) {
        for (int i = 0; i < 3; i++) {
            if (board[i * 3] == mark && board[i * 3 + 1] == mark && board[i * 3 +2] == mark) {
                return true;
            }
        }
        return false;
    }

    boolean hasDownDiagonal(char[] board, char mark) {

        return board[0] == mark && board[4] == mark && board[8] == mark;
    }

    boolean hasUpDiagonal(char[] board, char mark) {
        return board[6] == mark && board[4] == mark && board[2] == mark;
    }

    boolean isEmpty(char [] board, int index) {
       return board [index] == EMPTY;
    }

    int getPlayerSelection(Player player) {
        int selection;
        while (true) {
            out.println("Player is " + player.name + "(" + player.mark + ")");
            out.print("Select position to put mark (0-8) > ");
            selection = sc.nextInt();
            if (0 <= selection && selection <= 8) {
                break;
            }
            out.println("Bad choice (0-8 allowed)");
        }
        return selection;
    }

    void plotBoard(char[] board) {
        for (int i = 0; i < board.length; i++) {
            out.print(board[i] + " ");
            if ((i + 1) % 3 == 0) {
                out.println();
            }
        }
    }

    // A class (blueprint) for players.
    class Player {
        String name;
        char mark;
        Player(String name, char mark) {
            this.name = name;
            this.mark = mark;
        }
    }

    // This is used to test methods in isolation
    // Any non trivial method should be tested.
    // If not ... can't build a solution out of possible failing parts!
    void test() {
        char[] b = createBoard();
        out.println(b.length == 9);

        // TODO More tests

        exit(0);
    }
}