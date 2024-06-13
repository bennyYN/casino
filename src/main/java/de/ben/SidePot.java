package de.ben;

import java.util.ArrayList;
import java.util.List;

public class SidePot {
    private int amount;
    private List<Player> eligiblePlayers;
    private HandRanker handRanker;
    private Dealer dealer; // Add a Dealer reference

    public SidePot(int amount, List<Player> eligiblePlayers) {
        this.amount = amount;
        this.eligiblePlayers = new ArrayList<>();
        this.handRanker = new HandRanker();
        this.dealer = dealer;
    }

    public void addChips(int chips) {
        this.amount += chips;
    }

    public void addPlayer(Player player) {
        this.eligiblePlayers.add(player);
    }

    public int getAmount() {
        return this.amount;
    }

    public Player entscheidungGewinner() {
        Player winner = null;
        HandRanker.HandRanking highestRank = null;
        for (Player player : eligiblePlayers) {
            HandRanker.HandRanking currentRank = handRanker.rankHand(player.getHand(), dealer.getHand());
            if (highestRank == null || currentRank.getRank() > highestRank.getRank()) {
                highestRank = currentRank;
                winner = player;
            }
        }

        return winner;
    }

    public void preisAnGewinner() {
        Player winner = entscheidungGewinner();
        if (winner != null) {
            winner.getChips().addChips(this.amount);
            this.amount = 0;
        }
    }
    public void removePlayer(Player player) {
        this.eligiblePlayers.remove(player);
    }
    public List<Player> getEligiblePlayers() {
        return this.eligiblePlayers;
    }
    public void printAmount() {
        System.out.println("Sidepot amount: " + this.amount);
    }
}