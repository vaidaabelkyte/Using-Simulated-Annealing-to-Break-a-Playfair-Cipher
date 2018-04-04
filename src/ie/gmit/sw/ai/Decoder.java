package ie.gmit.sw.ai;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Decoder {
    private HashMap<String, Double> quadgrams;
    private String text;

    private String resultText;
    private PlayfairKey resultKey;
    private double resultScore;


    private double startTemp;
    private double tempDecrease;
    private int iterationsCount;

    public Decoder(HashMap<String, Double> quadgrams, String text) {
        this.quadgrams = quadgrams;
        this.text = text;
        // Load defaults
        this.startTemp = 10;
        this.tempDecrease = 1;
        this.iterationsCount = 50000;
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
        PlayfairKey parent = new PlayfairKey();
        String parentResult = Playfair.decode(parent, text);
        double parentScore = scoreText(parentResult);
        resultScore = Double.NEGATIVE_INFINITY;
        for (double temp = startTemp; temp >= 0; temp -= tempDecrease) {
            System.out.format("New temp: %f%n", temp);
            for (int transitions = iterationsCount; transitions > 0; transitions--) {
                PlayfairKey child = parent.shuffleKey();
                String childResult = Playfair.decode(child, text);
                double childScore = scoreText(childResult);
                double delta = childScore - parentScore;

                if (delta > 0 ||
                        (delta < 0 && ThreadLocalRandom.current().nextFloat() < 1/Math.exp(-delta / temp))) {
                    //System.out.format("New current best: temp = %f, transition = %d, score = %f%n", temp, transitions, childScore);
                    parent = child;
                    parentResult = childResult;
                    parentScore = childScore;
                }
                if (parentScore > resultScore) {
                    System.out.format("New best: temp = %f, transition = %d, score = %f%n", temp, transitions, childScore);
                    resultKey = parent;
                    resultScore = parentScore;
                    resultText = parentResult;
                }
            }
        }
    }

    public double getResultScore() {
        return resultScore;
    }

    public PlayfairKey getResultKey() {
        return resultKey;
    }

    public String getResultText() {
        return resultText;
    }

    public void setStartTemp(double startTemp) {
        this.startTemp = startTemp;
    }

    public void setTempDecrease(double tempDecrease) {
        this.tempDecrease = tempDecrease;
    }

    public void setIterationsCount(int iterationsCount) {
        this.iterationsCount = iterationsCount;
    }}
