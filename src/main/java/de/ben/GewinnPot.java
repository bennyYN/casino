package de.ben;

import java.util.ArrayList;
import java.util.List;

public class GewinnPot {
    private int amount;
    private List<SidePot> sidePots;

    public GewinnPot() {
        this.amount = 0;
        this.sidePots = new ArrayList<>();
    }

    public void addChips(int chips) {
        this.amount += chips;
    }

    public void removeChips(int chips) {
        this.amount -= chips;
    }

    public void addSidePot(SidePot sidePot) {
        sidePots.add(sidePot);
    }

    public int getAmount() {
        return amount;
    }

    public List<SidePot> getSidePots() {
        return sidePots;
    }

    public void clear() {
        this.amount = 0;
        this.sidePots.clear();
    }
}