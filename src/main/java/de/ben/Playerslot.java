package de.ben;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Playerslot {

    static ArrayList<Player> players = new ArrayList<Player>(9);
    static ArrayList<String> slotState = new ArrayList<String>(9);

    public Playerslot(int startChips, ArrayList<String> playerNames){

        for(int i = 0; i <= 8; i++){
            players.set(i, new Player(startChips, playerNames.get(i)));
        }

    }

    public void renderAll(Graphics g){
        //Einzeichnen der Playerslots
        int abstand = 100;
        for (int i = 0; i <= 3; i++) {
            //TODO -> HARDCODE IT!
            //LINKS
            g.drawImage(new ImageIcon("img/playerslot.png").getImage(), 20, 225 + (i * abstand), null);
            //RECHTS
            g.drawImage(new ImageIcon("img/playerslot.png").getImage(), 1008, 225 + (i * abstand), null);
        }
    }

}
