package ie.gmit.sw.ai;

public class Playfair {
    /**
     * Decodes the text with given key
     * @param key - key to decode with
     * @param text - text to decode
     * @return decoded text
     */
    public static String decode(PlayfairKey key, String text) {
        assert text.length() % 2 == 0;
        char[] result = new char[text.length()];
        for (int i = 0; i < text.length(); i += 2) {
            // Find positions
            char l1 = text.charAt(i);
            char l2 = text.charAt(i + 1);
            int col1 = key.getLetterCol(l1), row1 = key.getLetterRow(l1);
            int col2 = key.getLetterCol(l2), row2 = key.getLetterRow(l2);

            // Choose strategy
            if (col1 == col2) {
                // On the same column -> shift up
                row1 = (row1 == 0) ? 4 : row1 - 1;
                row2 = (row2 == 0) ? 4 : row2 - 1;

                result[i] = key.getLetter(row1, col1);
                result[i + 1] = key.getLetter(row2, col2);
            } else if (row1 == row2) {
                // On the same row -> shift left
                col1 = (col1 == 0) ? 4 : col1 - 1;
                col2 = (col2 == 0) ? 4 : col2 - 1;

                result[i] = key.getLetter(row1, col1);
                result[i + 1] = key.getLetter(row2, col2);
            } else {
                // On the box -> take opposite angle in the row
                result[i] = key.getLetter(row1, col2);
                result[i + 1] = key.getLetter(row2, col1);
            }
        }
        return new String(result);
    }
}
