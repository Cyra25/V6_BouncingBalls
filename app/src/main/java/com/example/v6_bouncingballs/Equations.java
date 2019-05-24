package com.example.v6_bouncingballs;

public class Equations {
    String[] ones;
    String[] twos;
    String[] threes;
    String[] fours;
    String[] fives;
    String[] sixs;
    String[] sevens;
    String[][] equations;
    public Equations(){
        ones = new String[]{"4 * 2 - 7","5 * 2 - 9","6 * 2 - 11", "7 * 3 - 20"};

        twos = new String[]{"4 * 2 - 6","5 * 2 - 8","6 * 2 - 10", "7 * 3 - 19"};

        threes = new String[]{"4 * 2 - 5","5 * 2 - 7","6 * 2 - 9", "7 * 3 - 18"};

        fours = new String[]{"4 * 2 - 4","5 * 2 - 6","6 * 2 - 8", "7 * 3 - 17"};

        fives = new String[]{"4 * 2 - 3","5 * 2 - 5","6 * 2 - 7", "7 * 3 - 16"};

        sixs = new String[]{"4 * 2 - 2","5 * 2 - 4","6 * 2 - 6", "7 * 3 - 15"};

        sevens = new String[]{"4 * 2 - 1","5 * 2 - 3","6 * 2 - 5", "7 * 3 - 14"};

        equations = new String[][]{ones, twos, threes,fours,fives,sixs, sevens};
    }
    public String[][] getEquations() {
        return equations;
    }
}
