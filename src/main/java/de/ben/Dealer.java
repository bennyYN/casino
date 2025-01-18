package de.ben;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Dealer {
    private final List<Card> hand;
    private Deck deck; // Declare a Deck instance
    public boolean handVisible = false;
    static PokerGUI gui;

    public Dealer() {
        this.hand = new ArrayList<>();
        this.deck = new Deck(); // Initialize the Deck instance
    }

    public void receiveCard(Card card) {
        hand.add(card);
    }

    public List<Card> getHand() {
        return hand;
    }

    public void clearHand() {
        this.hand.clear();
    }

    //TODO -> FERTIG MACHEN!
    public void renderCards(Graphics g){
        if(gui != null){
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

            if(handVisible){
                int xStart = gui.scaleX(327);
                double size = 0.9;

                for(int j = 0; j <= hand.size()-1; j++){
                    if(hand.size() == 3){
                        xStart = gui.scaleX(440);
                    }else if(hand.size() == 4){
                        xStart = gui.scaleX(383);
                    }else if(hand.size() == 5){
                        xStart = gui.scaleX(330);
                    }
                    g2d.drawImage(hand.get(j).getImage(), xStart+(j*gui.scaleX(110)), gui.scaleY((int)(325-Math.pow(gui.getScaleY(), 15))), gui.scaleX((int)(size*104)), (int)(((145+(145*Math.pow(gui.getScaleY(), 3.7)))/2)*size), null);
                }
            }
        }


    }
}