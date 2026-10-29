import java.lang.Math;
import java.util.Scanner;

static void main(String[] args) {

    // Scans the console for the desired length of the challenge
    Scanner scanner = new Scanner(System.in);

    System.out.print("Enter desired length: ");
    int length = scanner.nextInt();

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

    // Creates the appropriate solution
    System.out.println("\nSolution: ");
    for (int i = 0; i<length; System.out.println(solution[i]), i++) {
        double midpoint = Math.ceil(length/2);

        if (i < midpoint) {
            solution[i] = "left-facing";
        } else if (i > midpoint) {
            solution[i] = "right-facing";
        } else {
            solution[i] = "";
        }
    }

    Boolean solved = false;

    // Attempts to solve the puzzle of given length
    while (solved != true && attempts < 100000 && duckChallenge[0] != "failed") {
        if (attempts == 0) {
            duckChallenge = move(duckChallenge, (int)Math.floor(length/2)-1);
        } else {
            String action = "none";
            int midpoint = (int)Math.ceil(length/2);
            int position = midpoint;
            for (int i = 0; i<length; i++) {
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
                    // System.err.println("[ERROR] Out of Bounds Exception");
                } 

                // System.out.println("[DEBUG] Action Desired is " + action + " at " + position);
            }
            
            if (action == "right-jump") {
                jump(duckChallenge, position);
            } else if (action == "left-jump") {
                jump(duckChallenge, position);
            } else if (action == "right-move" || action == "priority-right-move") {
                move(duckChallenge, position);
            } else if (action == "left-move") {
                move(duckChallenge, position);
            } else {
                // System.out.println("\n[DEBUG BEGIN]" + 
                //     "\nType: " + action +
                //     "\nPosition: " + position + 
                //     "\nDirection: " + duckChallenge[position] + 
                // "\n[DEBUG END]");
                duckChallenge[0] = "failed";
            }
        }

        attempts++;

        System.out.println("\nTurn " + attempts);
        for (int i = 0; i<length; i++) {
            System.out.println(duckChallenge[i]);
            if (duckChallenge[i] != solution[i]) {
                solved = false;
            } else if (i == Math.ceil(length/2)) {
                solved = true; 
            }
        }

        if (solved == true) {
            System.out.println("\n\n\n[PUZZLE SOLVED]");
        }
    }

}

// Moves the ducks 1 space over into an empty space
private static String[] move(String[] input, int position) {

    String output[] = input;
    String direction = input[position];

    // Debug thread for trouble shooting
    System.out.println("\n[DEBUG BEGIN]" + 
        "\nType: move" + 
        "\nPosition: " + position + 
        "\nDirection: " + direction + 
    "\n[DEBUG END]");

    // Moves the duck 1 space only if it is empty
    if (direction == "right-facing" && input[position+1] == "") {
        output[position] = "";
        output[position+1] = direction;
    } else if (direction == "left-facing" && input[position-1] == "") {
        output[position] = "";
        output[position-1] = direction;
    } else {
        output[0] = "failed";
    }

    return output;
}

// Jumps the duck 2 spaces over into an empty space
private static String[] jump(String[] input, int position) {

    String output[] = input;
    String direction = input[position];

    // Debug thread for trouble shooting
    System.out.println("\n[DEBUG BEGIN]" + 
        "\nType: jump" + 
        "\nPosition: " + position + 
        "\nDirection: " + direction + 
    "\n[DEBUG END]");

    // Jumps the duck over another duck if the duck it's trying to jump over is opposite facing
    if (direction == "right-facing" && input[position+1] == "left-facing" && input[position+2] == "") {
        output[position] = "";
        output[position+2] = direction;
    } else if (direction == "left-facing" && input[position-1] == "right-facing" && input[position-2] == "") {
        output[position] = "";
        output[position-2] = direction;
    } else {
        output[0] = "failed";
    } 

    return output;

}