/*package de.ben;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class LobbySlots {

    static ArrayList<Player> players;
    static ArrayList<String> slotState;
    static ArrayList<Boolean> actualPlayer;

    PokerGUI gui;
    byte activePlayerIndex = 0;
    int actualPlayerCount = 0;

    //Konstruktor
    public LobbySlots(int startChips, ArrayList<String> playerNames, PokerGUI gui){

        this.gui = gui;

        // ArrayList initialisieren
        players = new ArrayList<Player>(8);
        slotState = new ArrayList<String>(8);
        actualPlayer = new ArrayList<Boolean>(8);

        // ArrayList initialisieren
        for (int i = 0; i < 8; i++) {
            players.add(null);
            slotState.add(null);
            actualPlayer.add(false);
        }

        //Arraylists füllen
        for(int i = 0; i <= 7; i++){
            if(playerNames.get(i).equals("")) {
                slotState.set(i, "empty_");
                actualPlayer.set(i, false);
            }else{
                players.set(i, new Player(startChips, playerNames.get(i)));
                slotState.set(i, "");
                actualPlayer.set(i, true);
                actualPlayerCount++;
            }
        }


    }

    public void renderAll(Graphics g){

        //Convert the graphics to Graphics2D to allow more advanced rendering
        Graphics2D g2d = (Graphics2D) g;

        // Set rendering hints for high-quality image rendering
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        updateSlotStates();

        for(int i = 0; i <= 7; i++){
            if(i <= 3){
                g2d.drawImage(ImageArchive.getImage(slotState.get(i) + "playerslot"), gui.scaleX(20), gui.scaleY(225 + (i * 100)), null);
                if(players.get(i)!=null){
                    g2d.setColor(Color.WHITE);
                    //Spielername
                    g2d.setFont(new Font("TimesRoman", Font.BOLD, gui.scaleFont(16)));
                    g2d.drawString(players.get(i).getName(), gui.scaleX(35), gui.scaleY(247 + (i * 100)));
                }
            }else{
                g2d.drawImage(ImageArchive.getImage(slotState.get(i) + "playerslot"), gui.scaleX(1008), gui.scaleY(225 + ((i-4) * 100)), null);
                if(players.get(i)!=null){
                    g2d.setColor(Color.WHITE);
                    //Spielername
                    g2d.setFont(new Font("TimesRoman", Font.BOLD, gui.scaleFont(16)));
                    g2d.drawString(players.get(i).getName(), gui.scaleX(1023), gui.scaleY(247 + ((i-4) * 100)));
                }
            }
        }
    }

    public String getAction(){
        return "4";
    }

    private void updateSlotStates() {
        for(int i = 0; i <= 7; i++){

            if(players.get(i)!=null){
                if(players.get(i).getName().equals("")) {
                    slotState.set(i, "empty_");
                }else{
                    slotState.set(i, "");
                }

                if (!gui.game.playerWon){
                    if (players.get(i) == gui.game.currentPlayer) {
                        slotState.set(i, "active_");
                    }
                }else{
                    if(players.get(i) == gui.game.gewinner()){
                        slotState.set(i, "winning_");
                    }
                }

                if (players.get(i).isFolded() || players.get(i).isAllIn()) {
                    slotState.set(i, "inactive_");
                }

                if((gui.game.playerWon) && (players.get(i) == gui.game.gewinner())){
                    slotState.set(i, "winning_");
                }

                if(gui.game.isGameOver){
                    if(gui.game.players.get(i) != null){
                        if(gui.game.players.get(i).getChips().getAmount() <= 0){
                            slotState.set(i, "zero_");
                        }
                    }
                }
            }
        }
    }

    public void showPlayerCardButtons(){

    }

    public void hidePlayerCardButtons(){

    }
}*/
