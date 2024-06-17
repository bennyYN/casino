package de.ben;

import java.util.ArrayList;
import java.util.List;

public class Dealer {
    private List<Card> hand;
    private Deck deck; // Declare a Deck instance

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

}