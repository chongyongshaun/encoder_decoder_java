package ie.atu.sw;

import java.io.*;
import java.util.*;

public class Runner {
    private static Scanner scanner = new Scanner(System.in);
    private static boolean running = true;
    private static String mappingFile;
    private static String inputFile;
    private static String inputFileSentence;
    private static String outputFile = "./out.txt";
    private static boolean decodeOutputFile = true;
    private static EncoderDecoder endecoder;

    // all of the menu functions should be O(1) time complexity except for the
    // encode and decode, the whole encoding process is O(n^2), which simplifies
    // from O(n) + O(n^2)
    // decoding is O(n) + O(n) = O(n)
    public static void main(String[] args) throws Exception {
        endecoder = new EncoderDecoder();
        while (running) {
            printMenu();
            int choice = getMenuChoice();
            handleChoice(choice);
        }
        System.out.println(ConsoleColour.GREEN + "Goodbye!");
    }

    private static void printMenu() {
        // You should put the following code into a menu or Menu class
        System.out.println(ConsoleColour.WHITE);
        System.out.println("************************************************************");
        System.out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
        System.out.println("*                                                          *");
        System.out.println("*              Encoding Words with Suffixes                *");
        System.out.println("*                                                          *");
        System.out.println("************************************************************");
        System.out.println("(1) Specify Mapping File");
        System.out.println("(2) Specify Text File to Encode");
        System.out.println("(3) Specify Output File (default: ./out.txt)");
        System.out.println("(4) Configure Options");
        System.out.println("(5) Encode Text File");
        System.out.println("(6) Decode Text File");
        System.out.println("(-1) Quit");

        // Output a menu of options and solicit text from the user
        System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
        System.out.print("Select Option >");
        System.out.println();
    }

    private static int getMenuChoice() {
        while (!scanner.hasNextInt()) {
            System.out.println(ConsoleColour.RED + "Invalid input! Please enter a number.");
            scanner.next(); // Discard invalid input
            System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT + "Select Option > ");
        }

        return scanner.nextInt();
    }

    private static void handleChoice(int choice) {
        switch (choice) {
            case 1:
                handleMappingFile(); // O(1)
                break;
            case 2:
                handleInputFile(); // O(1)
                break;
            case 3:
                handleOutputFile(); // O(1)
                break;
            case 4:
                handleConfig(); // O(1)
                break;
            case 5:
                handleEncode(); // O(n^2)
                break;
            case 6:
                handleDecodeFile(); // O(n)
                break;
            case -1:
                running = false;
                break;
            default:
                System.out.println(ConsoleColour.RED + "Invalid option! Please choose 1-6 or -1 to quit.");
        }
        // display the please press enter... message
        pause();
    }

    private static void handleMappingFile() {
        System.out.print("Enter mapping file name (e.g. 'mappings.csv' for files in the root dir): ");
        scanner.nextLine();
        String mappingFile = scanner.nextLine().trim();
        // check if the file exists, if it doesn't itll jump to the catch block
        try (BufferedReader reader = new BufferedReader(new FileReader(mappingFile))) {
            endecoder = new EncoderDecoder(mappingFile);
            System.out.println(ConsoleColour.GREEN + "Mapping file loaded successfully: " + mappingFile);
        } catch (Exception e) {
            System.out.println(ConsoleColour.RED + "Error loading mapping file:");
            e.printStackTrace();
        }
    }

    private static void handleInputFile() {
        System.out.print("Enter input file path: ");
        scanner.nextLine(); // clear the buffer before reading the next line
        inputFile = scanner.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append(" "); // stuff all lines into one sentence/line cuz my mapping doesn't support
                                // multi-line
            }
            inputFileSentence = sb.toString();
            System.out.println(ConsoleColour.GREEN + "Input file loaded successfully from classpath: " + inputFile);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(ConsoleColour.RED + "Error reading input file:");
            e.printStackTrace();
        }
        // System.out.println("DEBUG: " + inputFileSentence);
    }

    private static void handleOutputFile() {
        System.out.print("Enter output file path: ");
        scanner.nextLine();
        outputFile = scanner.nextLine();
        System.out.println(ConsoleColour.GREEN + "Output file set to: " + outputFile);
    }

    private static void handleConfig() {
        System.out.println(ConsoleColour.YELLOW + "Configuration Options:");
        System.out.println(
                "(1) Set file to be decoded mode (default is the output file)\nCurrent Mode = " + decodeOutputFile);
        System.out.println("(2) Set output file name");
        System.out.print("Select Option (1 or 2) > ");
        int option = -1;
        while (true) {
            if (scanner.hasNextInt()) {
                option = scanner.nextInt();
                if (option == 1 || option == 2) {
                    break;
                } else {
                    System.out.println(ConsoleColour.RED + "Invalid option! Please enter 1 or 2.");
                }
            } else {
                System.out.println(ConsoleColour.RED + "Invalid input! Please enter a number.");
                scanner.next();
            }
            System.out.print("Select Option (1 or 2) > ");
        }

        switch (option) {
            case 1:
                decodeOutputFile = !decodeOutputFile;
                if (decodeOutputFile) {
                    System.out.println(ConsoleColour.GREEN + "Decoding mode set to output file.");
                } else {
                    System.out.println(ConsoleColour.GREEN + "Decoding mode set to custom file.");
                }
                break;
            case 2:
                System.out.println("Enter output file name: ");
                scanner.nextLine();
                String newOutput = scanner.nextLine();
                outputFile = newOutput;
                System.out.println(ConsoleColour.GREEN + "Output file set to: " + outputFile);
                break;
        }
    }

    private static void handleEncode() {
        if (inputFile == null || inputFileSentence == null) {
            System.out.println(ConsoleColour.RED + "Please specify both input file first!");
            return;
        }
        System.out.println(ConsoleColour.BLUE + "Encoding...");
        String[] tokens = endecoder.wordBreaker(inputFileSentence);
        int[] encoded = endecoder.encode(tokens);
        StringBuilder sb = new StringBuilder();
        for (int el : encoded) {
            sb.append(el);
            sb.append(" ");
        }
        String encodedString = sb.toString().trim();
        System.out.println(ConsoleColour.GREEN + "Encoded string: " + encodedString);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            writer.write(encodedString);
            writer.close();
            System.out.println(ConsoleColour.GREEN + "Encoded string written to: " + outputFile);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ConsoleColour.RED + "Error writing to output file:");
        }
    }

    private static void handleDecodeFile() {
        System.out.println(ConsoleColour.BLUE + "Decoding...");
        // File file = new File(outputFile);
        File file;
        if (decodeOutputFile) {
            file = new File(outputFile);
        } else {
            System.out.print("Enter file to decode: ");
            scanner.nextLine();
            String fileName = scanner.nextLine();
            file = new File(fileName);
        }
        if (!file.exists()) {
            System.out.println(ConsoleColour.RED
                    + "Output file doesn't exist. Please encode first or specify a different file in configuration options");
            return;
        }
        System.out.println("Decoding file: " + file.getAbsolutePath());
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append(" ");
            }
            String[] encodedString = sb.toString().trim().split(" ");
            reader.close();
            int[] encoded = new int[encodedString.length];
            for (int i = 0; i < encodedString.length; i++) {
                encoded[i] = Integer.parseInt(encodedString[i]);
            }
            String[] decodedTokens = endecoder.decode(encoded);
            String decodedString = endecoder.wordStringer(decodedTokens);
            System.out.println(ConsoleColour.GREEN + "Decoded string: " + decodedString);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ConsoleColour.RED + "Error reading output file:");
        }
    }

    private static void pause() {
        if (running) {
            System.out.println(ConsoleColour.WHITE + "Press Enter to continue...");
            try {
                System.in.read();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}