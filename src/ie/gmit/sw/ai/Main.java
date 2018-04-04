package ie.gmit.sw.ai;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static HashMap<String, Double> quadgramsLog = new HashMap<>();

    /**
     *  "Load quadgrams" menu
     */
    private static void loadQuadgramsMenu() {
        try {
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            // Prompt user
            System.out.println("Please select the path to load quadgrams from:");
            String inputPath = scanner.nextLine();

            // Open the file for reading
            Scanner fileScanner = new Scanner(new FileReader(inputPath));

            // Read quadgrams
            quadgramsLog.clear();
            long totalQuadgrams = 0;
            while (fileScanner.hasNext()) {
                String quadgram = fileScanner.next().toUpperCase();
                int count = fileScanner.nextInt();
                totalQuadgrams += count;
                quadgramsLog.put(quadgram, (double) count);
            }

            // Get log probabilities
            for (Map.Entry<String, Double> kvp: quadgramsLog.entrySet()) {
                kvp.setValue(Math.log10(kvp.getValue()) - Math.log10(totalQuadgrams));
            }

            // Put "non-existing" quadgram
            quadgramsLog.put("", Math.log10(0.001 / totalQuadgrams));


            // Close the file
            fileScanner.close();

            // Report success
            System.out.format("4grams file successfully imported (%d 4rams)%n", quadgramsLog.size());
        } catch (Exception e) {
            // Handle file reading exception
            System.out.println("Can't open the 4grams file: " + e.getMessage());
        }
    }

    /**
     * Reads file or URL content
     * @param path - path to the file to load
     * @return file content as single string
     * @throws IOException when reading failed
     */
    static String readFile(String path) throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, Charset.defaultCharset());
    }

    /**
     * Handles decipher menu
     */
    private static void decipherMenu() {
        // Validate quadgrams
        if (quadgramsLog.size() <= 1) {
            System.out.println("Can't decipher without quadgrams. Load them first!");
        }

        // Prompt user for the input path
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
        System.out.println("Please enter the path or URL of the ciphered text:");
        String inputPath = scanner.nextLine();

        // Read ciphered text
        String cipheredText;
        try {
            cipheredText = readFile(inputPath).toUpperCase().replaceAll("[^A-Za-z0-9 ]", "");
        } catch (IOException e) {
            // Handle file reading exception
            System.out.println("Failed to read the ciphered file: " + e.getMessage());
            return;
        }


        // Prompt user for the output path
        System.out.println("Please select the path to store deciphered text:");
        String outputPath = scanner.nextLine();

        try {
            // Decipher text
            Decoder decoder = new Decoder(quadgramsLog, cipheredText);
            decoder.decipherText();


            // Write output
            FileWriter fileWriter = new FileWriter(outputPath);
            fileWriter.write(decoder.getResultText());
            fileWriter.close();
            System.out.format("Found key %s with log10(P) = %f%n", new String(decoder.getResultKey()), decoder.getResultScore());
        } catch (Exception e) {
            // Handle file writing exception
            System.out.println("Failed to write deciphered text to file: " + e.getMessage());
        }

    }

    /**
     * Main program code
     * @param args - command line arguments
     */
    public static void main(String[] args) {
        int inputChoice;
        do {
            System.out.println("Please choose an operation:");
            System.out.println("1. Load an quadgrams file.");
            System.out.println("2. Decipher the text.");
            System.out.println("3. Quit.");
            try
            {
                inputChoice = scanner.nextInt();
                if (inputChoice < 1 || inputChoice > 3) {
                    throw new IllegalArgumentException();
                }
            } catch (Exception e) {
                inputChoice = -1;
                while (scanner.hasNext()) {
                    scanner.next();
                }
                System.out.println("Invalid choice. Please try again.");
            }
            switch (inputChoice) {
                case 1:
                    loadQuadgramsMenu();
                    break;
                case 2:
                    decipherMenu();
                    break;
                default:
                    break;
            }
        } while (inputChoice != 3);

    }
}
