package de.ben;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Card> hand;
    private Chips chips;
    private boolean folded;
    private String name;
    private boolean allIn = false;



    public Player(int initialChips, String name) {
        this.hand = new ArrayList<>();
        this.chips = new Chips(initialChips);
        this.folded = false;
        this.name = name;
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

    public void setFolded(boolean folded) {
        this.folded = folded;
    }
    public boolean isFolded() {
        return folded;
    }
    public String getName() {
        return name;
    }
    public boolean isAllIn() {
        return allIn;
    }

    public void setAllIn(boolean b) {
        allIn = b;
    }
}