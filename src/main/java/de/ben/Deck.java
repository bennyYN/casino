package de.ben;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        Card.Suit[] suits = {Card.Suit.HEARTS, Card.Suit.DIAMONDS, Card.Suit.CLUBS, Card.Suit.SPADES};
        String[] values = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        for (Card.Suit suit : suits) {
            for (String value : values) {
                int intValue;
                switch (value) {
                    case "A":
                        intValue = 14;
                        break;
                    case "K":
                        intValue = 13;
                        break;
                    case "Q":
                        intValue = 12;
                        break;
                    case "J":
                        intValue = 11;
                        break;
                    default:
                        intValue = Integer.parseInt(value);
                        break;
                }
                Card card = new Card(intValue, suit);
                if (!cards.contains(card)) {
                    cards.add(card);
                }

            }
        }
        Collections.shuffle(cards);
    }




    public Card kartenehmen(){
        return cards.remove(cards.size()-1);
    }

    public List<Card> shuffleDeck() {
        Collections.shuffle(cards);
        return cards;
    }
}