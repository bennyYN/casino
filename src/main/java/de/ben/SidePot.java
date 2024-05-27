package de.ben;

import java.util.ArrayList;
import java.util.List;
public class SidePot {
    private int amount;
    private List<Player> eligiblePlayers;

    public SidePot(int amount, List<Player> eligiblePlayers) {
        this.amount = amount;
        this.eligiblePlayers = new ArrayList<>(eligiblePlayers);
    }

    public int getAmount() {
        return amount;
    }
    public void addAmount(int amount) {
        this.amount += amount;
    }

    public void subtractAmount(int amount) {
        this.amount -= amount;
    }

    public List<Player> getEligiblePlayers() {
        return eligiblePlayers;
    }

    public void removePlayer(Player player) {
        eligiblePlayers.remove(player);
    }
    public void printAmount() {
        System.out.println("Side pot amount: " + this.amount);
    }

}