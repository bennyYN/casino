package de.ben.blackjack;

import java.util.*;

public class Deck {
    private List<Card> cards;

    public Deck(int numberOfDecks) {
        cards = new ArrayList<>();
        for (int d = 0; d < numberOfDecks; d++) {
            for (int i = 1; i <= 13; i++) {
                for (Suit suit : Suit.values()) {
                    cards.add(new Card(i, suit));
                }
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        return cards.remove(cards.size() - 1);
    }
}