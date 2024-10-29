package de.ben;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Player {
    private List<Card> hand;
    private Chips chips;
    private boolean folded;
    private String name;
    private boolean allIn = false;
    private int currentBet;
    protected String username;



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
    public int bet(int amount) {
        if (amount == chips.getAmount()+getCurrentBet()) {
            return goAllIn();
        }
        while (chips.getAmount() < amount) {
            System.out.println("Du hast nicht genug Chips. Du hast nur " + chips.getAmount() + " Chips. Bitte gib einen gültigen Betrag ein.");
            Scanner scanner = new Scanner(System.in);
            amount = scanner.nextInt();
        }
        currentBet += amount;
        chips.removeChips(amount);
        return chips.getAmount();
    }

    public int goAllIn() {
        int allInAmount = chips.getAmount();
        currentBet += allInAmount;
        chips.removeChips(allInAmount);
        allIn = true;
        return 0;
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
    public int getCurrentBet() {
        return currentBet;
    }
    public void setAllIn(boolean b) {
        allIn = b;
    }
    public void resetCurrentBet() {
        currentBet = 0;
    }

    public void call(int highestBet) {
    }

    public void clearHand() {
        this.hand.clear();
    }


    public boolean check() {
        return false;
    }

    public void reset() {
    }
}