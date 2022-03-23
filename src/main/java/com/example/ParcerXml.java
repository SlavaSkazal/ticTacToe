package com.example;
import java.io.*;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.XMLStreamReader;

public class ParcerXml implements IParcer {
    
    @Override
    public void saveGame (Player player1, Player player2, Gameplay gameplay, int step, int gameStatus) {
        try {
            String nameDocXML = "ticTacToeGame" + Gameplay.counterDoc + ".xml";

            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            XMLStreamWriter writer = factory.createXMLStreamWriter(new FileWriter(nameDocXML));

            writer.writeStartDocument("1.0");
            writer.writeStartElement("Gameplay");

            writer.writeStartElement("Player");
            writer.writeAttribute("id", Integer.toString(player1.id));
            writer.writeAttribute("name", player1.name);
            writer.writeAttribute("symbol", "x");
            writer.writeEndElement();
            
            writer.writeStartElement("Player");
            writer.writeAttribute("id", Integer.toString(player2.id));
            writer.writeAttribute("name", player2.name);
            writer.writeAttribute("symbol", "o");
            writer.writeEndElement();

            writer.writeStartElement("Game");

            for (int i = 0; i < step; i++) {
                writer.writeStartElement("Step");
                writer.writeAttribute("num", Integer.toString(gameplay.steps[i].num));
                writer.writeAttribute("plyaerId", Integer.toString(gameplay.steps[i].playerId));
                writer.writeAttribute("symbol", gameplay.steps[i].symbol);
                writer.writeCharacters(Integer.toString(gameplay.steps[i].cell));
                writer.writeEndElement();
            }
            writer.writeEndElement();

            writer.writeStartElement("GameResult");
            if (gameStatus == 1) {
                writer.writeAttribute("id", Integer.toString(player1.id));
                writer.writeAttribute("name", player1.name);
                writer.writeAttribute("symbol", "x");
            } else if (gameStatus == 2) {
                writer.writeAttribute("id", Integer.toString(player2.id));
                writer.writeAttribute("name", player2.name);
                writer.writeAttribute("symbol", "o");
            } else if (gameStatus == 3) {    
                writer.writeAttribute("id", "draw");
                writer.writeAttribute("name", "draw");
                writer.writeAttribute("symbol", "draw");
            } else {
                writer.writeAttribute("id", "unforeseen");
                writer.writeAttribute("name", "unforeseen");
                writer.writeAttribute("symbol", "unforeseen");
            }
            writer.writeEndElement();
        
            writer.writeEndElement();
            writer.writeEndDocument();
            writer.flush();
        }
        catch (XMLStreamException | IOException ex) {
            ex.printStackTrace();
        }
        
    }

    @Override
    public void reproduceGame(int gameNum) {
        try {
            String nameDocXML = "ticTacToeGame" + gameNum + ".xml";
            XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new FileInputStream(nameDocXML));

            Gameplay gameplayShow = new Gameplay();
            String symbol = "";
            boolean readerIsStep = false;
            int cellNum = 0;

            System.out.println("\nGame playback:");

            while (reader.hasNext()) { 
                reader.next();

                if (readerIsStep
                    && !reader.isStartElement()
                    && !reader.isEndElement()
                    && reader.hasText()) {

                    cellNum = Integer.parseInt(reader.getText()); 
                    gameplayShow.playingField[cellNum-1] = symbol;
                    Gameplay.showPlayingField(gameplayShow.playingField);
                    readerIsStep = false;
                    System.out.println();
                } 

                if (reader.isStartElement()) {
                    if (reader.getLocalName() == "Step") {
                        readerIsStep = true;
                        symbol = reader.getAttributeValue(2);
                    } else if (reader.getLocalName() == "GameResult") {
                        if (reader.getAttributeValue(0) == "draw"
                            || reader.getAttributeValue(0) == "unforeseen")
                            System.out.println(reader.getAttributeValue(0));     
                        else {    
                            System.out.println("Player " + reader.getAttributeValue(0) + " -> "
                                + reader.getAttributeValue(1) + " is winner as " + "\'" + reader.getAttributeValue(2) + "\'");
                        }
                    }
                }
            } 
        } catch (FileNotFoundException | XMLStreamException ex) {
            ex.printStackTrace();
        }
    }
}
