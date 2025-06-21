package de.ben.poker;

import de.ben.ImageArchive;

import javax.swing.*;
import java.awt.*;

public class Card {
    public enum Suit {
        HEARTS, DIAMONDS, CLUBS, SPADES
    }

    private final int value;
    private final Suit suit;
    private final String link;

    public Card(int value, Suit suit) {
        this.value = value;
        this.suit = suit;
        this.link = generateImageLink();
    }

    public Image getImage(){
        return ImageArchive.getImage("card:" + this);
    }

    public ImageIcon getImageIcon(){
        return (new ImageIcon("src/img/cards/" + this + ".png"));
    }

    private String generateImageLink() {
        String suitString = suit.name().toUpperCase();
        return "https://bennyyn.xyz/upload/img/cards/" + value + "-" + suitString + ".png";
    }

    public int getValue() {
        return value;
    }

    public Suit getSuit() {
        return suit;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        String valueStr;
        switch (value) {
            case 11:
                valueStr = "J";
                break;
            case 12:
                valueStr = "Q";
                break;
            case 13:
                valueStr = "K";
                break;
            case 14:
                valueStr = "A";
                break;
            default:
                valueStr = String.valueOf(value);
                break;
        }
        return suit + " " + valueStr;
    }

    /*public static void main(String[] args) {
        for (Suit suit : Suit.values()) {
            for (int value = 2; value <= 14; value++) {
                Card card = new Card(value, suit);
                System.out.println(card + " - " + card.getLink());
            }

        }
    }*/
}
