package de.ben;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Playerslot {

    static ArrayList<Player> players;
    static ArrayList<String> slotState;
    PokerGUI gui;
    byte activePlayerIndex = 0;

    //Konstruktor
    public Playerslot(int startChips, ArrayList<String> playerNames, PokerGUI gui){

        this.gui = gui;

        // ArrayList initialisieren
        players = new ArrayList<Player>(8);
        slotState = new ArrayList<String>(8);

        // ArrayList mit null initialisieren
        for (int i = 0; i < 8; i++) {
            players.add(null);
            slotState.add(null);
        }

        //Arraylists füllen
        for(int i = 0; i <= 7; i++){
            if(playerNames.get(i).equals("‒")) {
                slotState.set(i, "empty_");
            }else{
                players.set(i, new Player(startChips, playerNames.get(i)));
                slotState.set(i, "");
            }
        }


    }

    public void renderAll(Graphics g){

        updateSlotStates();
        
        for(int i = 0; i <= 7; i++){
            if(i <= 3){
                g.drawImage(new ImageIcon("img/" + slotState.get(i) + "playerslot.png").getImage(), 20, 225 + (i * 100), null);
                if(players.get(i)!=null){
                    g.setColor(Color.WHITE);
                    //Spielername
                    g.setFont(new Font("TimesRoman", Font.BOLD, 16));
                    g.drawString(players.get(i).getName(), 35, 247 + (i * 100));
                    //Chips Anzahl
                    g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
                    g.drawImage(new ImageIcon("img/chips.png").getImage(), 35, 254 + (i * 100), 16, 16, null);
                    g.drawString(String.valueOf(players.get(i).getChips().getAmount()), 55, 267 + (i * 100));
                }
            }else{
                g.drawImage(new ImageIcon("img/" + slotState.get(i) + "playerslot.png").getImage(), 1008, 225 + ((i-4) * 100), null);
                if(players.get(i)!=null){
                    g.setColor(Color.WHITE);
                    //Spielername
                    g.setFont(new Font("TimesRoman", Font.BOLD, 16));
                    g.drawString(players.get(i).getName(), 1023, 247 + ((i-4) * 100));
                    //Chips Anzahl
                    g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
                    g.drawImage(new ImageIcon("img/chips.png").getImage(), 1023, 254 + ((i-4) * 100), 16, 16, null);
                    g.drawString(String.valueOf(players.get(i).getChips().getAmount()), 1043, 267 + ((i-4) * 100));
                }
            }
        }
    }

    private void updateSlotStates() {
        for(int i = 0; i <= 7; i++){

            if(players.get(i)!=null){
                if(players.get(i).getName().equals("‒")) {
                    slotState.set(i, "empty_");
                }else{
                    slotState.set(i, "");
                }

                if (players.get(i).isFolded() || players.get(i).isAllIn()) {
                    slotState.set(i, "inactive_");
                }

                if (i == gui.currentPlayerIndex) {
                    slotState.set(i, "active_");
                }
            }
        }
    }
}
