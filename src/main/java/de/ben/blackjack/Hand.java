package de.ben.blackjack;

import java.util.*;

public class Hand {
    List<Card> cards;

    public Hand() {
        cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }


    public int getValue() {
        int value = 0;
        int aces = 0;
        for (Card card : cards) {
            value += card.getValue();
            if (card.getValue() == 11) {
                aces++;
            }
        }
        while (value > 21 && aces > 0) {
            value -= 10;
            aces--;
        }
        return value;
    }

    public int getVisibleValue() {
        int value = 0;
        int aces = 0;
        for (Card card : cards) {
            value += card.getVisibleValue();
            if (card.getVisibleValue() == 11) {
                aces++;
            }
        }
        while (value > 21 && aces > 0) {
            value -= 10;
            aces--;
        }
        return value;
    }

    public Card getFirstCard() {
        return cards.get(0);
    }

    @Override
    public String toString() {
        return cards.toString() + " (Total value: " + getValue() + ")";
    }
}