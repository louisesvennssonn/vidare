package exercises;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Scanner;

import static java.lang.System.in;
import static java.lang.System.out;

/*
 * Program to exercise arrays
 *
 * See:
 * - ArrayBasics
 */
public class Ex4Arrays {

    public static void main(String[] args) {
        new Ex4Arrays().program();
    }

    final Scanner sc = new Scanner(in);

    void program() {
        int[] arr = new int[5];   // Declare and create String array

        out.print("Input 5 numbers (enter after each) > ");  // Read strings into array
        for (int i = 0; i < arr.length; i++) {     // Must use loop
            arr[i] = sc.nextInt();
        }
        out.println("Array is"+ Arrays.toString(arr));

        out.println("Input a value to find >");

        int number = sc.nextInt();

        boolean isInArray = false;

        int index = -1;

        for(int i = 0; i < arr.length; i++){
            if(arr[i]== number){
                isInArray = true;
                index = i;
            }
        } if (isInArray == true){
            out.println("Value" + " " + number + " " + "is at index" + " "+ index);
        } else {
            out.print("Value not found");
        }
    }
}

