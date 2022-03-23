package com.example;

public class Player {
    
    public Player()
    {
        if (counterId % 2 == 0)
            symbol = "o";
        else
            symbol = "x";    

        id = counterId;    
        counterId++;
    }

    public static int counterId = 1;
    public int id;
    public String name;
    public String symbol;
}