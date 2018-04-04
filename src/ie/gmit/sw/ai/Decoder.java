package ie.gmit.sw.ai;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Decoder {
    private HashMap<String, Double> quadgrams;
    private String text;

    private String resultText;
    private char[] resultKey;
    private double resultScore;

    public Decoder(HashMap<String, Double> quadgrams, String text) {
        this.quadgrams = quadgrams;
        this.text = text;
    }

    /**
     * Randomly mixing array of characters
     * @param key - array to shuffle
     */
    private void shuffle(char[] key) {
        int index;
        Random random = ThreadLocalRandom.current();
        for (int i = key.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            if (index != i) {
                key[index] ^= key[i];
                key[i] ^= key[index];
                key[index] ^= key[i];
            }
        }
    }

    /**
     * Randomly modifies key
     * 90% probability - swap letters
     * 2% - swap rows
     * 2% - swap columns
     * 2% - flip all rows
     * 2% - flip all columns
     * 2% - reverse whole key
     * @param key - key to base on
     * @return randomly modified key
     */
    private char[] shuffleKey(char[] key) {
        int randomVal = ThreadLocalRandom.current().nextInt(100);
        char[] result = new char[key.length];
        if (randomVal < 94) {
            // Copy key
            for (int i = 0; i < 25; i++) {
                result[i] = key[i];
            }

            // Swap 2 entities
            int bound = (randomVal < 90) ? 25 : 5;
            int v1 = ThreadLocalRandom.current().nextInt(bound);
            int v2 = ThreadLocalRandom.current().nextInt(bound);
            while (v2 == v1) {
                v2 = ThreadLocalRandom.current().nextInt(bound);
            }

            if (randomVal < 90) {
                // Swap 2 letters
                result[v1] = key[v2];
                result[v2] = key[v1];
            } else if (randomVal < 92) {
                // Swap 2 rows
                for (int i = 0; i < 5; i++) {
                    result[v1 * 5 + i] = key[v2 * 5 + i];
                    result[v2 * 5 + i] = key[v1 * 5 + i];
                }
            } else {
                // Swap 2 columns
                for (int i = 0; i < 5; i++) {
                    result[i * 5 + v1] = key[i * 5 + v2];
                    result[i * 5 + v2] = key[i * 5 + v1];
                }
            }
        } else if (randomVal < 96) {
            // Flip all rows
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    result[i * 5 + j] = key[(4 - i) * 5 + j];
                }
            }
        } else if (randomVal < 98){
            // Flip all columns
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    result[i * 5 + j] = key[i * 5 + 4 - j];
                }
            }
        } else {
            // Reverse the whole key
            for (int i = 0; i < 25; i++) {
                result[i] = key[24 - i];
            }
        }
        return result;
    }

    /**
     * Inits random Playfair key
     * @return
     */
    private char[] initKey() {
        char[] key = new char[25];
        int i = 0;
        for (char c = 'A'; c <= 'Z'; c++) {
            if (c == 'J') {
                continue;
            }
            key[i++] = c;
        }
        shuffle(key);
        return key;
    }

    /**
     * Calculates log10 based heuristic score based on quadgrams
     * @param text - text to score
     * @return text score
     */
    private double scoreText(String text) {
        double score = 0;
        for (int i = 0; i < text.length() - 3; i++) {
            String key = text.substring(i, i + 4);
            if (quadgrams.containsKey(key)) {
                score += quadgrams.get(key);
            } else {
                score += quadgrams.get("");
            }
        }
        return score;
    }

    /**
     * Searches best solution with Simulated Annealing
     */
    public void decipherText() {
        char[] parent = initKey();
        String parentResult = Playfair.decode(parent, text);
        double parentScore = scoreText(parentResult);
        for (double temp = 10; temp > 0; temp -= 0.1) {
            for (int transitions = 50000; transitions > 0; transitions--) {
                char[] child = shuffleKey(parent);
                String childResult = Playfair.decode(child, text);
                double childScore = scoreText(childResult);
                double delta = childScore - parentScore;

                if (delta > 0 ||
                        (delta < 0 && ThreadLocalRandom.current().nextFloat() < 1/Math.exp(-delta / temp))) {
                    System.out.format("New best: temp = %f, transition = %d, score = %f%n", temp, transitions, childScore);
                    parent = child;
                    parentResult = childResult;
                    parentScore = childScore;
                }
            }
        }
        resultKey = parent;
        resultScore = parentScore;
        resultText = parentResult;
    }

    public double getResultScore() {
        return resultScore;
    }

    public char[] getResultKey() {
        return resultKey;
    }

    public String getResultText() {
        return resultText;
    }
}
