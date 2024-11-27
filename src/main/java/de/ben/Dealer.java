package de.ben;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Dealer {
    private List<Card> hand;
    private Deck deck; // Declare a Deck instance
    public boolean handVisible = false;

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

    public Card drawCards() {
        return deck.kartenehmen(); // Call Kartenehmen on the Deck instance
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void clearHand() {
        this.hand.clear();
    }

    //TODO -> FERTIG MACHEN!
    public void renderCards(Graphics g){

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        if(handVisible){
            int xStart = 327;

            for(int j = 0; j <= hand.size()-1; j++){
                if(hand.size() == 3){
                    xStart = 435;
                }else if(hand.size() == 4){
                    xStart = 381;
                }else if(hand.size() == 5){
                    xStart = 327;
                }
                g2d.drawImage(hand.get(j).getImage(), xStart+(j*110), 325, 94, 131, null);
            }
        }

    }
}