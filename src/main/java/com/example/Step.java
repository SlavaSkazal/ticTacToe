package com.example;

public class Step {

    public Step(int num, int playerId, int cell, String symbol)
    {
        this.num = num;
        this.playerId = playerId;
        this.cell = cell;
        this.symbol = symbol;
    }

    public int num;
    public int playerId;
    public int cell; 
    public String symbol;
}