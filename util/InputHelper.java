package util;

import java.util.Scanner;

public class InputHelper {

    private static final Scanner scanner = new Scanner(System.in);

    // Read non-empty string
    public static String readString(String label) {
        String input;
        while (true) {
            System.out.print(label + ": ");
            input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            DisplayHelper.error("Input cannot be empty");
        }
    }

    // Read integer within range
    public static int readInt(String label, int min, int max) {
        while (true) {
            System.out.print(label + ": ");
            try {
                int value = Integer.parseInt(scanner.nextLine());
                if (value >= min && value <= max) {
                    return value;
                }
                DisplayHelper.error("Enter a number between " + min + " and " + max);
            } catch (NumberFormatException e) {
                DisplayHelper.error("Invalid number");
            }
        }
    }

    public static void inputNextLine() {
        scanner.nextLine();
    }
}
