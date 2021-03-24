package exercises;

import static java.lang.System.out;

/*
 *  Implement methods to make program produce correct output (normally print true)
 *
 * See:
 * - MethodsBasics
 * - MethodsArrObj  (just arrays)
 */
public class Ex5Methods {

    public static void main(String[] args) {
        new Ex5Methods().program();
    }

    void program() {
        // All, except last,  should print true
        out.println(sumTo(5) == 15);     // 1 + 2 + ... + 5 = 15
        out.println(sumTo(23) == 276);
        out.println(factorial(3) == 6);    // 3 * 2 * 1 = 6
        out.println(factorial(5) == 120);
        out.println(digitSum(1111) == 4);   // 1 + 1 + 1 + 1 = 4
        out.println(digitSum(12345) == 15);

        int[] arr = {10, 20, 30, 40, 50};
        out.println(next(arr, 2) == 40);    // Find *next* value given index
                                                  // Index 2 is 30 so next is 40.
        out.println(next(arr, 4) == 10);    // Index 4 is 50 so next is 10 (circular).

        String winner = "Olle";
        // A special case, should print: "Winner is Olle" (or whatever name)
        winnerMsg(winner);
    }

    // ------ Write methods below this  -----------

    // Declaration
    int sumTo(int n) {
        int sum = 0;
        for ( int i = 1 ; i <=n ; i++ ) {
            sum = sum + i;
        }
        return sum;
    }
    int factorial (int num) {
        int prod = 1;
        for (int i =1 ; i<=num ; i++) {
            prod = prod * i;
        } return prod;
    }
    int digitSum (int digit) {
        int sum = 0;
        while (digit > 0) {
            sum = digit % 10 + sum;
            digit = digit / 10;
        } return sum;
    }
    int next (int arr[], int n) {
        int next = 0;
        int x = arr [(n+1) % arr.length]; {

        }return x;


        }
    void winnerMsg(String s) {
        out.println("winner is " + s);
    }

    // TODO More methods here

}
