package com.example;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ParcerJson implements IParcer {
    @Override
    public void saveGame (Player player1, Player player2, Gameplay gameplay, int step, int gameStatus) {
        try {
            String nameDocJson = "ticTacToeGame" + Gameplay.counterDoc + ".json";

            JSONObject gameplayJSON = new JSONObject();

            JSONArray players = new JSONArray();

            JSONObject player1JSON = new JSONObject();
            player1JSON.put("id", player1.id);
            player1JSON.put("name", player1.name);
            player1JSON.put("symbol", player1.symbol);
            JSONObject player2JSON = new JSONObject();
            player2JSON.put("id", player2.id);
            player2JSON.put("name", player2.name);
            player2JSON.put("symbol", player2.symbol);

            players.add(player1JSON);
            players.add(player2JSON);

            gameplayJSON.put("players", players);

            JSONArray gameSteps = new JSONArray();

            for (int i = 0; i < step; i++) {
                JSONObject stepJSON = new JSONObject();
                stepJSON.put("num", gameplay.steps[i].num);
                stepJSON.put("playerId", gameplay.steps[i].playerId);
                stepJSON.put("symbol", gameplay.steps[i].symbol);
                stepJSON.put("cell", gameplay.steps[i].cell);
                gameSteps.add(stepJSON);
            }

            gameplayJSON.put("gameSteps", gameSteps);

            JSONObject gameResult = new JSONObject();

            if (gameStatus == 1) {
                gameResult.put("winner", player1JSON);
            } else if (gameStatus == 2) {
                gameResult.put("winner", player2JSON);
            } else if (gameStatus == 3) {   
                JSONObject drawJSON = new JSONObject();
                drawJSON.put("id", 0);
                drawJSON.put("name", "draw");
                drawJSON.put("symbol", "draw");
                gameResult.put("winner", drawJSON);
            } else {
                JSONObject unforeseenJSON = new JSONObject();
                unforeseenJSON.put("id", 0);
                unforeseenJSON.put("name", "unforeseen");
                unforeseenJSON.put("symbol", "unforeseen");
                gameResult.put("winner", unforeseenJSON);
            }
            gameplayJSON.put("gameResult", gameResult);

            Files.write(Paths.get(nameDocJson), gameplayJSON.toString().getBytes());
        }
        catch (Exception ex) {
            ex.printStackTrace();   
        }
    }

    @Override
    public void reproduceGame(int gameNum) {
        try {
            String nameDocJson = "ticTacToeGame" + gameNum + ".json";
            Gameplay gameplayShow = new Gameplay();

            FileReader reader = new FileReader(nameDocJson);
            JSONParser jsonParser = new JSONParser();
            JSONObject gameplayJSON = (JSONObject) jsonParser.parse(reader);
            
            JSONArray gameSteps = (JSONArray) gameplayJSON.get("gameSteps");
            Object[] arrGameSteps = gameSteps.toArray();

            System.out.println("Game playback (JSON):");

            for (int i = 0; i < arrGameSteps.length; i++) {
                JSONObject step =  (JSONObject) arrGameSteps[i];
                gameplayShow.playingField[Integer.parseInt(step.get("cell").toString())-1] = step.get("symbol").toString();
                Gameplay.showPlayingField(gameplayShow.playingField);
                System.out.println();
            }
            
            JSONObject gameResult = (JSONObject) gameplayJSON.get("gameResult");
            JSONObject winner = (JSONObject) gameResult.get("winner");

            if (winner.get("id") == "0")
                System.out.println(winner.get("name"));     
            else {    
                System.out.println("Player " + winner.get("id") + " -> "
                + winner.get("name")+ " is winner as " + "\'" + winner.get("symbol") + "\'");
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();   
        }
    }
}