package com.example;
import java.util.*;
import java.io.*;

public class App {
    public static void main(String[] args) throws IOException {

        boolean usersWantToPlay = true;
        int gameNum = 0, cellNum = 0;
        Scanner scanner = new Scanner(System.in);

        while(usersWantToPlay) {
            Gameplay gameplay = new Gameplay();
            Player player1 = new Player();
            Player player2 = new Player();

            System.out.print("Enter name 1: ");
            player1.name = scanner.nextLine();
            System.out.print("Enter name 2: ");
            player2.name = scanner.nextLine();

            int step = 1, gameStatus = 0;
            boolean correctCellNum = false;
            
            while(gameStatus == 0) {
                Gameplay.showPlayingField(gameplay.playingField);

                while(!correctCellNum) {
                    System.out.print((step % 2 == 0 ? player2.name : player1.name) + ", enter cell number: ");
                    cellNum = scanner.nextInt();

                    if (cellNum < 1 || cellNum  > 9)
                        System.out.println("Incorrect cell number");
                    else if (!gameplay.playingField[cellNum-1].equals("-"))
                        System.out.println("This place is already taken");
                    else {
                        gameplay.playingField[cellNum-1] = step % 2 == 0 ? "o" : "x";
                        gameplay.steps[step-1] = new Step(step, step % 2 == 0 ? player2.id : player1.id, cellNum, step % 2 == 0 ? "o" : "x");
                        correctCellNum = true;
                    }
                }

                gameStatus = checkGameOver(gameplay.playingField);

                if(gameStatus == 0) {       
                    correctCellNum = false;
                    step++;
                } else if (step >= 9)
                    break;
            }

            Gameplay.showPlayingField(gameplay.playingField);

            if (gameStatus == 1)
                System.out.println("Winner: " + player1.name + ", id: " + player1.id + ", symbol x.");
            else if (gameStatus == 2)
                System.out.println("Winner: " + player2.name + ", id: " + player2.id + ", symbol o.");
            else if (gameStatus == 3)
                System.out.println("Draw");
            else
                System.out.println("Unforeseen result"); 

            ParcerXml parcerXml = new ParcerXml();
            parcerXml.saveGame(player1, player2, gameplay, step, gameStatus);

            ParcerJson parcerJson = new ParcerJson();
            parcerJson.saveGame(player1, player2, gameplay, step, gameStatus);

            Gameplay.counterDoc++;

            for (int i = 0; i < Gameplay.counterDoc - 1; i++) {
                System.out.println("ticTacToeGame" + (i+1));
            }
            System.out.print("Do you want to reproduce the game? Enter number of game, otherwise enter 0: ");
            gameNum = scanner.nextInt();

            if (gameNum > 0 && gameNum <= Gameplay.counterDoc) {
                parcerXml.reproduceGame(gameNum);
                System.out.println();
                parcerJson.reproduceGame(gameNum);
            } else if (gameNum != 0) {
                System.out.println("You enter incorrect number, game will not reproduce");
            }

            System.out.print("If you want to start a new game, enter 1, if not - any number: ");
            if (scanner.nextInt() != 1) {
                usersWantToPlay = false;
                scanner.close();
            }
        }
    }

    //gameStatus == 0 - continue
    //gameStatus == 1 - x winner
    //gameStatus == 1 - o winner
    //gameStatus == 3 - draw
    public static int checkGameOver(String []playingField)
    {
        int gameStatus = 0;

        if (!playingField[0].equals("-")
            && (playingField[0].equals(playingField[1]) && playingField[0].equals(playingField[2])
                || playingField[0].equals(playingField[3]) && playingField[0].equals(playingField[6])
                || playingField[0].equals(playingField[4]) && playingField[0].equals(playingField[8]))) 
            gameStatus = playingField[0].equals("x") ? 1 : 2;

        if (gameStatus == 0) {
            if (!playingField[4].equals("-")
                && (playingField[4].equals(playingField[1]) && playingField[4].equals(playingField[7])
                    || playingField[4].equals(playingField[3]) && playingField[4].equals(playingField[5]))) 
                gameStatus = playingField[4].equals("x") ? 1 : 2;
        }

        if (gameStatus == 0) {
            if (!playingField[8].equals("-")
                && (playingField[8].equals(playingField[2]) && playingField[8].equals(playingField[5])
                    || playingField[8].equals(playingField[6]) && playingField[8].equals(playingField[7]))) 
                gameStatus = playingField[8].equals("x") ? 1 : 2;
        }
        
        if (gameStatus == 0) {
            if (!playingField[2].equals("-")
                && (playingField[2].equals(playingField[4]) && playingField[2].equals(playingField[6]))) 
                gameStatus = playingField[2].equals("x") ? 1 : 2;
        }

        if (gameStatus == 0
            && playingFieldIsFullness(playingField))
            gameStatus = 3;

        return gameStatus;
    }

    public static boolean playingFieldIsFullness(String []playingField)
    {
        boolean gameContinue = false, containsX = false, containsO = false;
        int y = 0;
    
        for(int i = 0; i < 3; i++) {
            if (playingField[y].equals("x")
                || playingField[y+1].equals("x")
                || playingField[y+2].equals("x"))
                containsX = true;
            if (playingField[y].equals("o")
                || playingField[y+1].equals("o")
                || playingField[y+2].equals("o"))
                containsO = true;    

            if (!containsX || !containsO) {
                gameContinue = true;
                break;
            }    
            else {
                containsX = false;
                containsO = false;
                y += 3;
            }
        }

        if (!gameContinue) {
            y = 0;
    
            for(int i = 0; i < 3; i++) {
                if (playingField[y].equals("x")
                    || playingField[y+3].equals("x")
                    || playingField[y+6].equals("x"))
                    containsX = true;
                if (playingField[y].equals("o")
                    || playingField[y+3].equals("o")
                    || playingField[y+6].equals("o"))
                    containsO = true;    
    
                if (!containsX || !containsO) {
                    gameContinue = true;
                    break;
                }    
                else {
                    containsX = false;
                    containsO = false;
                    y++;
                }
            }
        }

        if (!gameContinue) {
            y = 0;
    
            for(int i = 0; i < 2; i += 2) {
                if (playingField[y].equals("x")
                    || playingField[y+4].equals("x")
                    || playingField[y+8].equals("x"))
                    containsX = true;
                if (playingField[y].equals("o")
                    || playingField[y+4].equals("o")
                    || playingField[y+8].equals("o"))
                    containsO = true;    
    
                if (!containsX || !containsO) {
                    gameContinue = true;
                    break;
                }    
                else {
                    containsX = false;
                    containsO = false;
                    y += 2;
                }
            }
        }
        return !gameContinue;
    }
}