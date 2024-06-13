package de.ben;

import java.util.ArrayList;
import java.util.List;

public class GewinnPot {
    private int amount;
    public GewinnPot() {
        this.amount = 0;}

    public void addChips(int chips) {
        this.amount += chips;
    }

    public void removeChips(int chips) {
        this.amount -= chips;
    }

    public int getAmount() {
        return amount;
    }


    public void clear() {
        this.amount = 0;
    }
}