package com.example;

public interface IParcer {   
    public void saveGame(Player player1, Player player2, Gameplay gameplay, int step, int gameStatus);
    public void reproduceGame(int gameNum);
}