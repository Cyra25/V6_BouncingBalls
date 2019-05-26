package com.example.v6_bouncingballs;

public class Equations {
    String[] ones;
    String[] twos;
    String[] threes;
    String[] fours;
    String[] fives;
    String[] sixs;
    String[] sevens;
    String[] group1, group2, group3, group4, group5, group6, group7;
    String[][] numbersGrp;
    String[][] equations;
    public Equations(){
        ones = new String[]{"4 * 2 - 7","5 * 2 - 9","6 * 2 - 11", "7 * 3 - 20","4 * 3 - 11","8 * 6 - 47",
                "9 * 7 - 62", "2 * 6 - 11"};

        twos = new String[]{"4 * 2 - 6","5 * 2 - 8","6 * 2 - 10", "7 * 3 - 19","4 * 3 - 10","8 * 6 - 46",
                "9 * 7 - 61", "2 * 6 - 10"};

        threes = new String[]{"4 * 2 - 5","5 * 2 - 7","6 * 2 - 9", "7 * 3 - 18","4 * 3 - 9","8 * 6 - 45",
                "9 * 7 - 60", "2 * 6 - 9"};

        fours = new String[]{"4 * 2 - 4","5 * 2 - 6","6 * 2 - 8", "7 * 3 - 17","4 * 3 - 8","8 * 6 - 44",
                "9 * 7 - 59", "2 * 6 - 8"};

        fives = new String[]{"4 * 2 - 3","5 * 2 - 5","6 * 2 - 7", "7 * 3 - 16","4 * 3 - 7","8 * 6 - 43",
                "9 * 7 - 58", "2 * 6 - 7"};

        sixs = new String[]{"4 * 2 - 2","5 * 2 - 4","6 * 2 - 6", "7 * 3 - 15","4 * 3 - 6","8 * 6 - 42",
                "9 * 7 - 57", "2 * 6 - 6"};

        sevens = new String[]{"4 * 2 - 1","5 * 2 - 3","6 * 2 - 5", "7 * 3 - 14","4 * 3 - 5","8 * 6 - 41",
                "9 * 7 - 56", "2 * 6 - 5"};

        group1 = new String[]{"-10","-9","-8","-7","-6","-5","-4","-3","-2","-1"};

        group2 = new String[]{"0","1","2","3","4","5","6","7","8","9"};

        group3 = new String[]{"10","11","12","13","14","15","16","17","18","19"};

        group4 = new String[]{"20","21","22","23","24","25","26","27","28","29"};

        group5 = new String[]{"30","31","32","33","34","35","36","37","38","39"};

        group6 = new String[]{"40","41","42","43","44","45","46","47","48","49"};

        group7 = new String[]{"50","51","52","53","54","55","56","57","58","59"};

        equations = new String[][]{ones, twos, threes,fours,fives,sixs, sevens};
        numbersGrp = new String[][]{group1,group2,group3,group4,group5,group6,group7};
    }
    public String[][] getEquations() {
        return equations;
    }
    public String[][] getNumbersGrp(){
        return numbersGrp;
    }
}
