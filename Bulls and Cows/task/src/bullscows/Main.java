package bullscows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static String code;
    private static final Scanner scanner = new Scanner(System.in);
    private static final char sentinelValue = '_';
    private static final String possibleCharacters = "0123456789abcdefghijklmnopqrstuvwxyz";

    public static void main(String[] args) {
        if (!generateCode()) {
            return;
        }

        System.out.println("Okay, let's start a game!");

        int bulls = 0;
        int cows;
        int turnCounter = 1;
        while (bulls != code.length()) {
            System.out.printf("Turn %d:\n", turnCounter);
            String guess = scanner.next();
            char[] guessArray = guess.toCharArray();
            bulls = findBulls(code.toCharArray(), guessArray);
            cows = findCows(code.toCharArray(), guessArray);
            printResults(bulls, cows);
            turnCounter++;
        }

        System.out.println("Congratulations! You guessed the secret code.");
    }

    public static int findCows(char[] codeArray, char[] guessArray) {
        int cows = 0;
        for (int i = 0; i < codeArray.length && i < guessArray.length; i++) {
            if (codeArray[i] == sentinelValue) {
                continue;
            }

            for (int j = 0; j < guessArray.length; j++) {
                if (codeArray[i] == guessArray[j]) {
                    cows++;
                    guessArray[i] = sentinelValue;
                    break;
                }
            }
        }

        return cows;
    }

    public static int findBulls(char[] codeArray, char[] guessArray) {
        int bulls = 0;
        for (int i = 0; i < codeArray.length && i < guessArray.length; i++) {
            if (codeArray[i] == guessArray[i]) {
                bulls++;
                codeArray[i] = sentinelValue;
                guessArray[i] = sentinelValue;
            }
        }

        return bulls;
    }

    public static boolean generateCode() {
        String validNumberError = "Error: \"%s\" isn't a valid number.\n";
        System.out.println("Please, enter the secret code's length:");
        String userInputSecret = scanner.nextLine();
        int secretLength;
        try {
            secretLength = Integer.parseInt(userInputSecret);
        } catch (NumberFormatException nfe) {
            System.out.printf(validNumberError, userInputSecret);
            return false;
        }
        if (secretLength == 0) {
            System.out.printf(validNumberError, userInputSecret);
            return false;
        }

        System.out.println("Input the number of possible symbols in the code:");
        String userInputMaxSymbols = scanner.nextLine();
        int maxSymbols;
        try {
            maxSymbols = Integer.parseInt(userInputMaxSymbols);
        } catch (NumberFormatException nfe) {
            System.out.printf(validNumberError, userInputMaxSymbols);
            return false;
        }
        if (maxSymbols > possibleCharacters.length()) {
            String text = "Error: maximum number of possible symbols in the code is 36 (0-9, a-z).";
            System.out.println(text);
            return false;
        }
        if (maxSymbols < secretLength) {
            String errorText;
            errorText = "Error: it's not possible to generate a code with a length of %d with %d unique symbols.\n";
            System.out.printf(errorText, secretLength, maxSymbols);
            return false;
        }

        generatedCodeMessage(maxSymbols, secretLength);

        String selectedCharacterRange = possibleCharacters.substring(0, maxSymbols);
        ArrayList<String> numbers = new ArrayList<>(Arrays.asList(selectedCharacterRange.split("")));
        StringBuilder secret = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < secretLength; i++) {
            int numberIndex = random.nextInt(numbers.size());
            secret.append(numbers.get(numberIndex));
            numbers.remove(numberIndex);
        }

        code = secret.toString();
        return true;
    }

    public static void generatedCodeMessage(int maxSymbols, int secretLength) {
        String range;
        if (maxSymbols <= 10) {
            range = " (0-9)";
        } else {
            range = String.format(" (0-9, a-%s)", possibleCharacters.charAt(maxSymbols - 1));
        }

        System.out.println("The secret prepared: " + "*".repeat(secretLength) + range);
    }

    public static void printResults(int bulls, int cows) {
        if (bulls != 0 && cows != 0) {
            System.out.printf("Grade: %d bull(s) and %d cow(s).\n", bulls, cows);
        } else if (bulls == 0 && cows == 0) {
            System.out.println("Grade: None.");
        } else if (bulls == 0) {
            System.out.printf("Grade: %d cow(s).\n", cows);
        } else {
            System.out.printf("Grade: %d bulls(s).\n", bulls);
        }
    }
}
