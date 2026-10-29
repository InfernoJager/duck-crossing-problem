import java.lang.Math;
import java.util.Scanner;

public class duckCrossingChallenge {
    static void main(String[] args) {

        // Scans the console for the desired length of the challenge, and other configurations.
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enable debug? [y/n]");
        final Boolean debug = convertToBoolean(scanner.next());

        System.out.println("Enable output? [y/n]");
        final Boolean displayOutput = convertToBoolean(scanner.next());

        System.out.println("Enter desired length (must be positive): ");
        int length = scanner.nextInt();
        while (length < 0) {
            System.out.println("Please enter an appropriate length: ");
            length = scanner.nextInt();
        }

        scanner.close();

        // Variable declaration to keep track of attempts, current progress and the final solution
        int attempts = 0;
        String duckChallenge[] = new String[length];
        String solution[] = new String[length];

        // Creates the challenge board
        System.out.println("Starting: ");
        for (int i = 0; i<length; System.out.println(duckChallenge[i]), i++) {
            double midpoint = Math.ceil(length/2);

            if (i < midpoint) {
                duckChallenge[i] = "right-facing";
            } else if (i > midpoint) {
                duckChallenge[i] = "left-facing";
            } else {
                duckChallenge[i] = "";
            }
        }
        System.out.println("\n\n\n");

        // Creates the appropriate solution
        if (debug) {System.out.println("[DEBUG] Solution: ");}
        for (int i = 0; i<length; i++) {
            solution[i] = duckChallenge[(length-i)-1];

            if (debug) {System.out.println(solution[i]);}
        }
        if (debug) {System.out.print("\n\n\n");}

        Boolean solved = false;

        // The proper equation for this for both odd and even is ((x-1)/2)^2-1-(((x+1)%2)/4)
        // The equation of equivalence for odd alone is ((x-1)/2)^2-1, for even numbers it adds another -0.25
        // This is due to how powers work, (4+1)/2 is 5/2, when squared becomes 25/4, or 6.25
        // This means the answer would be 5.25 after subtracting 1, 0.25 or 1/4 off of where it should be
        // Due to the code, when converting to a long, rounding down, there is no need to add this extra calculation here, as it is a constant -0.25
        long estimatedAttempts = (long)Math.pow(((length+1)/2.0), 2) - 1;

        if (debug) {System.out.println("\n[DEBUG] Estimated Turns: " + estimatedAttempts);}

        // Attempts to solve the puzzle of given length
        while (solved != true && attempts < estimatedAttempts && duckChallenge[0] != "failed") {
            String action = "none";
            int midpoint = (int)Math.ceil(length/2);
            int position = midpoint;
            for (int i = 0; i<length; i++) {
                // Trys to do check which move is the optimal, out of bound exceptions are caught and ignored to allow the game to reach completion
                try {
                    if (duckChallenge[i] == "right-facing" && duckChallenge[i+1] == "left-facing" && duckChallenge[i+2] == "") {
                        action = "right-jump";
                        position = i;
                    } else if (duckChallenge[i] == "left-facing" && duckChallenge[i-1] == "right-facing" && duckChallenge[i-2] == "" && (action != "right-jump")) {
                        action = "left-jump";
                        position = i;
                    } else if (duckChallenge[i] == "left-facing" && duckChallenge[i-1] == "" && (action != "right-jump" && action != "left-jump" && action != "priority-right-move")) {
                        action = "left-move";
                        position = i;
                    } else if (duckChallenge[i] == "right-facing" && duckChallenge[i+1] == "" && (action != "right-jump" && action != "left-jump" && action != "left-move")) {
                        action = "right-move";
                        position = i;
                        if (position < midpoint && duckChallenge[i+2] == "left-facing") {
                            action = "priority-right-move";
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    if (debug) {System.err.println("[ERROR] Out of Bounds Exception");}
                } 

                if (debug) {System.out.println("[DEBUG] Action Desired is " + action + " at " + position);}
            }

            if (action != "none") {
                move(duckChallenge, action, position, debug);
            } else {
                if (debug) {
                    System.out.println("\n[DEBUG BEGIN]" + 
                        "\nType: " + action +
                        "\nPosition: " + position + 
                        "\nDirection: " + duckChallenge[position] + 
                    "\n[DEBUG END]");
                }

                duckChallenge[0] = "failed";
            }

            attempts++;

            // Checks the solve condition and displayes the step if display output is on
            if (displayOutput) {System.out.println("\nTurn " + attempts + "/" + estimatedAttempts);}
            for (int i = 0; i<length; i++) {
                if (displayOutput) {System.out.println(duckChallenge[i]);}
                if (duckChallenge[i] == solution[i] && i == 0) {
                    solved = true;
                } else if (duckChallenge[i] != solution[i]) {
                    solved = false; 
                }
            }

            // If there is no output displayed, shows percentage of completion
            if (!displayOutput) {System.out.print("[" + Math.floor(((double)attempts/estimatedAttempts)*100) + "%]\r");}

            // Displays that the puzzle is solved if true
            if (solved) {
                System.out.println("\n[PUZZLE SOLVED]");
            }
        }

    }

    // Moves the ducks into an empty space by moving or jumping
    private static String[] move(String[] input, String action, int position, Boolean debug) {

        String output[] = input;
        String direction = input[position];

        // Debug thread for trouble shooting
        if (debug) {
            System.out.println("\n[DEBUG BEGIN]" + 
                "\nType: move" + 
                "\nPosition: " + position + 
                "\nDirection: " + direction + 
            "\n[DEBUG END]");
        }

        // Moves the duck according to the action given, such as move, moves it.
        if (action == "right-move" || action == "priority-right-move") {
            output[position] = "";
            output[position+1] = direction;
        } else if (action == "left-move") {
            output[position] = "";
            output[position-1] = direction;
        } else if (action == "right-jump") {
            output[position] = "";
            output[position+2] = direction;
        } else if (action == "left-jump") {
            output[position] = "";
            output[position-2] = direction;
        } else {
            output[0] = "failed";
        }

        return output;
    }

    // Method to turn y/n input into a boolean value
    private static Boolean convertToBoolean(String input) {

        if (input == null) {
            return false;
        }

        input = input.trim();
        return "Y".equalsIgnoreCase(input);

    } 
}
