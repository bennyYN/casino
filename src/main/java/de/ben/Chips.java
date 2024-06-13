package de.ben;

public class Chips {
    private int amount;
    private int intialAmount;

    public Chips(int initialAmount) {
        this.amount = initialAmount;

    }

    public void addChips(int amount) {
        this.amount += amount;
    }

    public void removeChips(int amount) {
        if (this.amount >= amount) {
            this.amount -= amount;
        } else {
            throw new IllegalArgumentException("Not enough chips");
        }
    }

    public int getAmount() {
        return amount;
    }
    public void subtractChips(int amount) {
        this.amount -= amount;
    }
}