package exercises;

import java.util.Random;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

/*
 * The Rock, paper, scissor game.
 * See https://en.wikipedia.org/wiki/Rock%E2%80%93paper%E2%80%93scissors
 *
 * This is exercising smallest step programming (no methods needed)
 *
 * Rules:
 *
 *       -----------  Beats -------------
 *       |                              |
 *       V                              |
 *      Rock (1) --> Scissors (2) --> Paper (3)
 *
 */
public class Ex7RPS {

    public static void main(String[] args) {
        new Ex7RPS().program();
    }

    final Random rand = new Random();
    final Scanner sc = new Scanner(in);

    void program() {

        int maxRounds = 5;
        int human;          // Outcome for player
        int computer;       // Outcome for computer
        int result;         // Result for this round
        int round = 0;      // Number of rounds
        int total = 0;      // Final result after all rounds

        // All code here ... (no method calls)

        out.println("Welcome to Rock, Paper and Scissors");
        out.println("Select 1, 2 or 3 (for R, P or S) > ");
        human = sc.nextInt(3);
        computer = rand.nextInt((2) + 1);
        out.println("Computer choose >" + computer);

        while (round < 5) {
            if (human > computer) {
                round++;
            } else if (computer > human) {
                round++;
            } else if (human == computer) {
                round++;
            }
        }

        if (round == maxRounds) {
            out.println("Game over! ");
            if (total == 0) {
                out.println("Draw");
            } else if (total > 0) {
                out.println("Human won.");
            } else {
                out.println("Computer won.");
            }
        }
    }
}