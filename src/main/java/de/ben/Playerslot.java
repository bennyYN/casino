package de.ben;
//TODO -> push player class, playerslot img & class

import java.awt.*;
import java.util.ArrayList;

public class Playerslot {

    static ArrayList<Player> players = new ArrayList<Player>(9);

    public Playerslot(ArrayList<String> playerNames){

        for(int i = 0; i <= 8; i++){
            players.set(i, new Player(int startChips, playerNames.get(i)))
        }

    }

    public void renderAll(Graphics g){

    }

}
