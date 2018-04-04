package ie.gmit.sw.ai;

public class Playfair {
    /**
     * Finds the index of a 5x5 matrix element in a flat array representation
     * @param row - row of the element
     * @param col - column of the element
     * @return index in a flat array
     */
    private static int getIndexInKey(int row, int col) {
        return row * 5 + col;
    }

    /**
     * Finds row of the letter in the key
     * @param key - key in a flat array
     * @param letter - letter to search
     * @return row of the letter
     */
    private static int getRowInKey(char[] key, char letter) {
        for (int i = 0; i < key.length; i++) {
            if (key[i] == letter) {
                return i / 5;
            }
        }

        // Shouldn't get here
        assert false;
        return 0;
    }

    /**
     * Finds column of the letter in the key
     * @param key - key in a flat array
     * @param letter - letter to search
     * @return column of the letter
     */
    private static int getColInKey(char[] key, char letter) {
        for (int i = 0; i < key.length; i++) {
            if (key[i] == letter) {
                return i % 5;
            }
        }

        // Shouldn't get here
        assert false;
        return 0;
    }

    /**
     * Decodes the text with given key
     * @param key - key to decode with
     * @param text - text to decode
     * @return decoded text
     */
    public static String decode(char[] key, String text) {
        assert text.length() % 2 == 0;
        char[] result = new char[text.length()];
        for (int i = 0; i < text.length(); i += 2) {
            // Find positions
            int col1 = getColInKey(key, text.charAt(i)), row1 = getRowInKey(key, text.charAt(i));
            int col2 = getColInKey(key, text.charAt(i + 1)), row2 = getRowInKey(key, text.charAt(i + 1));

            // Choose strategy
            if (col1 == col2) {
                // On the same column -> shift up
                row1 = (row1 == 0) ? 4 : row1 - 1;
                row2 = (row2 == 0) ? 4 : row2 - 1;

                result[i] = key[getIndexInKey(row1, col1)];
                result[i + 1] = key[getIndexInKey(row2, col2)];
            } else if (row1 == row2) {
                // On the same row -> shift left
                col1 = (col1 == 0) ? 4 : col1 - 1;
                col2 = (col2 == 0) ? 4 : col2 - 1;

                result[i] = key[getIndexInKey(row1, col1)];
                result[i + 1] = key[getIndexInKey(row2, col2)];
            } else {
                // On the box -> take opposite angle in the row
                result[i] = key[getIndexInKey(row1, col2)];
                result[i + 1] = key[getIndexInKey(row2, col1)];
            }
        }
        return new String(result);
    }
}
