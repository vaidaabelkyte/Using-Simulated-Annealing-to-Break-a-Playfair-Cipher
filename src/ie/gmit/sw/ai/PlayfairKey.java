package ie.gmit.sw.ai;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PlayfairKey {
    private char[] key;
    private int[] letterCol, letterRow;

    /**
     * Inits random Playfair key
     */
    public PlayfairKey() {
        key = new char[25];
        int i = 0;
        for (char c = 'A'; c <= 'Z'; c++) {
            if (c == 'J') {
                continue;
            }
            key[i++] = c;
        }
        shuffle();
        buildCache();
    }

    /**
     * Inits Playfair key based on given array
     */
    public PlayfairKey(char[] key) {
        this.key = key;
        buildCache();
    }

    /**
     * Randomly shuffles array of characters
     */
    private void shuffle() {
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
     * Builds cache to accelerate access to the key elements
     */
    private void buildCache() {
        letterCol = new int[255];
        letterRow = new int[255];
        for (int i = 0; i < key.length; i++) {
            letterCol[key[i]] = i % 5;
            letterRow[key[i]] = i / 5;
        }
    }

    /**
     * Gets row index of letter in the key matrix.
     * @param letter - letter to search for.
     * @return row index
     */
    public int getLetterRow(char letter) {
        return letterRow[letter];
    }

    /**
     * Gets column index of letter in the key matrix.
     * @param letter - letter to search for.
     * @return column index
     */
    public int getLetterCol(char letter) {
        return letterCol[letter];
    }

    /**
     * Finds the letter of a 5x5 matrix element in a flat array representation
     * @param row - row of the element
     * @param col - column of the element
     * @return value at that position array
     */
    public char getLetter(int row, int col) {
        return key[row * 5 + col];
    }

    @Override
    public String toString() {
        return new String(key);
    }

    /**
     * Randomly modifies key
     * 90% probability - swap letters
     * 2% - swap rows
     * 2% - swap columns
     * 2% - flip all rows
     * 2% - flip all columns
     * 2% - reverse whole key
     * @return randomly modified key
     */
    public PlayfairKey shuffleKey() {
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
        return new PlayfairKey(result);
    }

}
