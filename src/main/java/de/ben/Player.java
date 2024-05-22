package de.ben;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Card> hand;
    private Chips chips;

    public Player(int initialChips) {
        this.hand = new ArrayList<>();
        this.chips = new Chips(initialChips);
    }

    public void receiveCard(Card card) {
        hand.add(card);
    }

    public List<Card> getHand() {
        return hand;
    }

    public Chips getChips() {
        return chips;
    }
    public void bet(int amount) {
        if (chips.getAmount() >= amount) {
            chips.removeChips(amount);
        } else {
            throw new IllegalArgumentException("Not enough chips to bet");
        }
    }
}