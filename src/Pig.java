import java.util.Random;
import java.util.Scanner;

import static java.lang.System.*;

/*
 * The Pig game
 * See http://en.wikipedia.org/wiki/Pig_%28dice_game%29
 *
 */
public class Pig {

    public static void main(String[] args) {
        new Pig().program();
    }

    // The only allowed instance variables (i.e. declared outside any method)
    // Accessible from any method
    final Scanner sc = new Scanner(in);
    final Random rand = new Random();

    void program() {
        //test();                 // <-------------- Uncomment to run tests!

        final int winPts = 20;    // Points to win (decrease if testing)
        Player[] players;         // The players (array of Player objects)
        Player current;            // Current player for round (must use)
        boolean aborted = false;   // Game aborted by player?

       /* players = new Player[2];   // Hard coded players, replace *last* of all with ... (see below)
        Player p1 = new Player();
        p1.name = "Olle";
        Player p2 = new Player();
        p2.name = "Fia";
        players[0] = p1;
        players[1] = p2;*/

        players = getPlayers();  // ... this (method to read in all players)

        welcomeMsg(winPts);
        statusMsg(players);

        current = players[rand.nextInt(players.length)];

        while ((current.totalPts + current.roundPts) >= 20) {


            } String choice = getPlayerChoice(current);

        if (choice.equals("r")) {
            int roll = rollDice();
            roundMsg(roll, current);

            if (roll == 1) {
                current.roundPts = 0;
                statusMsg(players);
                current = players[nextPlayer(players,current)];


            } else {
                current.roundPts += roll;

            }
        } else if (choice.equals("n")) {
            current.totalPts += current.roundPts;
            current.roundPts = 0;
            statusMsg(players);
            current = players[nextPlayer(players,current)];


        } else if (choice.equals("q")) {
            aborted = true;
            break;
        }
        gameOverMsg(current, aborted);
    }




    // ---- Game logic methods --------------
    int rollDice (){
        int roll;
        roll = rand.nextInt(6) + 1;
        return roll;
    }
    int nextPlayer(Player players[], Player current){
        for (int i =0 ; i < players.length; i++){
            if (players[i]== current){
                i++;
                return i % players.length;
            }
        }
        return 0;
    }


    // ---- IO methods ------------------

    void welcomeMsg(int winPoints) {
        out.println("Welcome to PIG!");
        out.println("First player to get " + winPoints + " points will win!");
        out.println("Commands are: r = roll , n = next, q = quit");
        out.println();
    }

    void statusMsg(Player[] players) {
        out.print("Points: ");
        for (int i = 0; i < players.length; i++) {
            out.print(players[i].name + " = " + players[i].totalPts + " ");
        }
        out.println();
    }

    void roundMsg(int roll, Player current) {
        if (roll > 1) {
            out.println("Got " + roll + " running total are " + (current.roundPts + roll));
        } else {
            out.println("Got 1 lost it all!");
        }
    }

    void gameOverMsg(Player player, boolean aborted) {
        if (aborted) {
            out.println("Aborted");
        } else {
            out.println("Game over! Winner is player " + player.name + " with "
                    + (player.totalPts + player.roundPts) + " points");
        }
    }

    String getPlayerChoice(Player player) {
        out.print("Player is " + player.name + " > ");
        return sc.next();
    }

    Player[] getPlayers() {
        out.println("Enter number of players > "+ " ");
        int nPlayers= sc.nextInt();
        Player[] players = new Player[nPlayers];

        for (int i = 0 ; i< players.length; i++) {
            Player person = new Player();
            out.println("Name for player > " + (i + 1));
            person.name = sc.next();
            players[i] =person;
        }

        return players;

    }

    // ---------- Class -------------
    // Class representing the concept of a player
    // Use the class to create (instantiate) Player objects
    class Player {
        String name;     // Default null
        int totalPts;    // Total points for all rounds, default 0
        int roundPts;    // Points for a single round, default 0
    }

    // ----- Testing -----------------
    // Here you run your tests i.e. call your game logic methods
    // to see that they really work (IO methods not tested here)
    void test() {
        // This is hard coded test data
        // An array of (no name) Players (probably don't need any name to test)
        Player[] players = {new Player(), new Player(), new Player()};

        // TODO Use for testing of logcial methods (i.e. non-IO methods)

        exit(0);   // End program
    }
}



