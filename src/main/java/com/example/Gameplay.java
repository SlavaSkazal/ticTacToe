package com.example;

public class Gameplay {
    public static int counterDoc = 1;
    public String result;
    public String symbol;
    public String []playingField = {"-", "-", "-", "-", "-", "-", "-", "-", "-"};
    public Player player1;
    public Player player2;
    public Step []steps = new Step[9];

    public static void showPlayingField(String []playingField)
    {
        for(int i = 0; i < 9; i++) {
            System.out.print("|" + playingField[i] + "|");
            if (i == 2 || i == 5 || i == 8)
                System.out.println();
        }
    }
}