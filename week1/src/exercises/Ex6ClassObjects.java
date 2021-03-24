package exercises;

import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

/*
 *  Using class objects to represent heroes
 *  https://en.wikipedia.org/wiki/Gladiators_(1992_UK_TV_series)
 *
 * See:
 * - ClassObjects
 * - MethodsArrObj
 */
public class Ex6ClassObjects {

    public static void main(String[] args) {
        new Ex6ClassObjects().program();
    }

    final Scanner sc = new Scanner(in);

    void program() {
        // TODO
        Hero H1 = new Hero ();
        H1. name = "Hercules";
        H1. strength = 34;
        out.println("How strong is Hercules >" + H1.strength);

        Hero H2 = new Hero();
        H2. name = "Atlas";
        H2. strength = 45;
        out.println("How strong is Atlas >" + H2.strength);

        if (H1. strength > H2.strength){
            out.println("Hercules is stronger");
        } else {
            out.println("Atlas is stronger");
        }

    }

    // ------ The class to use  -----------
    // A class for objects that describes a Hero
    class Hero {
        String name;
        int strength;
    }


}
