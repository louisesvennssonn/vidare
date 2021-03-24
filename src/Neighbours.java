import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Random;

import static java.lang.Math.round;
import static java.lang.Math.sqrt;
import static java.lang.System.*;

/*
 *  Program to simulate segregation.
 *  See : http://nifty.stanford.edu/2014/mccown-schelling-model-segregation/
 *
 * NOTE:
 * - JavaFX first calls method init() and then the method start() far below.
 * - The method updateWorld() is called periodically by a Java timer.
 * - To test uncomment call to test() first in init() method!
 *
 */
// Extends Application because of JavaFX (just accept for now)
public class Neighbours extends Application {

    final Random rand= new Random();


    // Enumeration type for the Actors
    enum Actor {
        BLUE, RED, NONE   // NONE used for empty locations
    }

    // Enumeration type for the state of an Actor
    enum State {
        UNSATISFIED,
        SATISFIED,
        NA     // Not applicable (NA), used for NONEs
    }

    // Below is the *only* accepted instance variable (i.e. variables outside any method)
    // This variable may *only* be used in methods init() and updateWorld()
    Actor[][] world;              // The world is a square matrix of Actors

    // This is the method called by the timer to update the world
    // (i.e move unsatisfied) approx each 1/60 sec.

    @Override
    public void init() {
       // test();    // <---------------- Uncomment to TEST!
        // %-distribution of RED, BLUE and NONE
        double[] dist = {0.25, 0.25, 0.50};
        int nLocations = 900;
        int d = (int) (sqrt(nLocations));
        world = new Actor[d][d];
        Actor[] actors = getActors(nLocations, dist);
        shuffle(actors);
        world = toMatrix(actors, nLocations, world);
        // Should be last
        fixScreenSize(nLocations);
    }
    void updateWorld() {
        // % of surrounding neighbours that are like me
        final double threshold = 0.7;
        State[][] currentState = nextState(world, threshold);
        Integer [] emptyArray = emptyIndices(world);
        shuffle(emptyArray);
        swap(emptyArray, currentState, world);

    }


    Actor[] getActors(int nActors, double dist[]){
        int amountRED = (int) (nActors * dist[0]);
        int amountBLUE = (int) (nActors * dist[1]);
        Actor[] arr = new Actor[nActors];
        for (int i = 0; i < nActors; i++) {
            if (amountBLUE != 0) {
                arr[i] = Actor.BLUE;
                amountBLUE--;
            } else if (amountRED != 0) {
                arr[i] = Actor.RED;
                amountRED--;
            } else {
                arr[i]= Actor.NONE;
            }
        } return arr;
    }
    // shufflar alla platser i array
    <T> void shuffle(T [] arr){
        for(int i= 0; i< arr.length; i++){
            int index = rand.nextInt(i+1);
            T tmp= arr[index];
            arr[index]= arr[i];
            arr[i]= tmp;
        }
    }

    Actor [][] toMatrix(Actor[] actors, int nLocations, Actor[][]world) {
        int d= (int) sqrt(nLocations);
        for(int i= 0; i< actors.length; i++){
            world [i / d][i % d] = actors[i];
        }
        return world;
    }
// går igenom world matrisen och definerar unsatisfied
    State stateActor(Actor[][] world, int row, int col, double threshold) {
        Actor me = world[row][col];
        int sumRed = 0;
        int sumBlue = 0;
        for (int r = (row - 1); r <= (row + 1); r++) {
            for (int c = (col - 1); c <= (col + 1); c++) {
                if (r >= 0 && c >= 0 && r < world.length && c < world.length) {
                    if ((world[r][c] == Actor.RED) && !(r == row && c == col)) {
                    sumRed++;
                    } else if ((world[r][c] == Actor.BLUE) && !(r == row && c == col)) {
                    sumBlue++;
                    }
                }
            }
        }
        int neighbours = sumBlue + sumRed;
        if (me== Actor.NONE) {
            return State.NA;
        }
        if (me == Actor.RED && (sumRed < threshold * neighbours)) {
            return State.UNSATISFIED;

        } else if (me == Actor.BLUE && (sumBlue < threshold * neighbours)) {
            return State.UNSATISFIED;
        }
        return State.SATISFIED;
    }

// Skapar ny matrix med states.
    State[][] nextState(Actor[][] world, double threshold) {
        int size = world.length;
        State[][] state = new State[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                state[row][col] = stateActor(world, row, col, threshold);
            }
        }
        return state;
    }
// skapar en array för alla NONE platser i world-matrisen
    Integer [] emptyIndices (Actor [][] world){
        int nNone = 0;
        for (int row = 0; row < world.length; row++) {
            for (int col = 0; col < world.length; col++) {
                if (world[row][col]== Actor.NONE){
                    nNone++;
                }
            }
        }
        Integer [] arrEmpty = new Integer [nNone];
        int i = 0;
        for (int row = 0; row < world.length; row++) {
            for (int col = 0; col < world.length; col++) {
                if (world [row][col] == Actor.NONE){
                    arrEmpty[i] =world.length* row +col;
                    i++;
                }
            }
        }
        return arrEmpty;
    }
    //swapar unsatisfied med tomma platser i world matrisen
    Actor[][] swap(Integer [] arrEmpty, State[][] newState, Actor[][]world){
        int count = 0;
        for (int row = 0; row< world.length; row++) {
            for (int col = 0; col < world.length; col++) {
                if (newState[row][col] == State.UNSATISFIED) {
                    int newRow = arrEmpty[count] / world.length;
                    int newCol = arrEmpty[count] % world.length;
                    Actor tmp = world[newRow][newCol];
                    world[newRow][newCol] = world[row][col];
                    world[row][col] = tmp;
                    count++;
                }
            }
        }
        return world;
    }
    // ------- Testing -------------------------------------

    // Here you run your tests i.e. call your logic methods
    // to see that they really work
    void test() {

        // A small hard coded world for testing
        Actor[][] testWorld = new Actor[][]{
                {Actor.RED, Actor.RED, Actor.NONE},
                {Actor.NONE, Actor.BLUE, Actor.NONE},
                {Actor.RED, Actor.NONE, Actor.BLUE}
        };
        double th = 0.5;   // Simple threshold used for testing

        int size = testWorld.length;
        out.println(isValidLocation(size, 0, 0));
        out.println(!isValidLocation(size, -1, 0));
        out.println(!isValidLocation(size, 0, 3));
        out.println(isValidLocation(size, 2, 2));

        nextState(testWorld,  0.7);
        out.println(nextState(testWorld, 0.7));
        exit(0);
    }

    // Helper method for testing (NOTE: reference equality)
    <T> int count(T[] arr, T toFind) {
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == toFind) {
                count++;
            }
        }
        return count;
    }
    // Check if inside world
    boolean isValidLocation(int size, int row, int col) {
        return 0 <= row && row < size &&
                0 <= col && col < size;
    }

    // ###########  NOTHING to do below this row, it's JavaFX stuff  ###########

    double width = 400;   // Size for window
    double height = 400;
    long previousTime = nanoTime();
    final long interval = 450000000;
    double dotSize;
    final double margin = 50;

    void fixScreenSize(int nLocations) {
        // Adjust screen window depending on nLocations
        dotSize = (width - 2 * margin) / sqrt(nLocations);
        if (dotSize < 1) {
            dotSize = 2;
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Build a scene graph
        Group root = new Group();
        Canvas canvas = new Canvas(width, height);
        root.getChildren().addAll(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Create a timer
        AnimationTimer timer = new AnimationTimer() {
            // This method called by FX, parameter is the current time
            public void handle(long currentNanoTime) {
                long elapsedNanos = currentNanoTime - previousTime;
                if (elapsedNanos > interval) {
                    updateWorld();
                    renderWorld(gc, world);
                    previousTime = currentNanoTime;
                }
            }
        };

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Segregation Simulation");
        primaryStage.show();

        timer.start();  // Start simulation
    }


    // Render the state of the world to the screen
    public void renderWorld(GraphicsContext g, Actor[][] world) {
        g.clearRect(0, 0, width, height);
        int size = world.length;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                double x = dotSize * col + margin;
                double y = dotSize * row + margin;

                if (world[row][col] == Actor.RED) {
                    g.setFill(Color.RED);
                } else if (world[row][col] == Actor.BLUE) {
                    g.setFill(Color.BLUE);
                } else {
                    g.setFill(Color.WHITE);
                }
                g.fillOval(x, y, dotSize, dotSize);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
