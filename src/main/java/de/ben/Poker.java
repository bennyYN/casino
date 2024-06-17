package de.ben;

import java.util.*;

public class Poker {
    private final List<Player> players;
    private final List<Player> blindsOrder;
    private final Dealer dealer;
    private Deck deck;
    HandRanker handRanker = new HandRanker();
    private final GewinnPot GewinnPot = new GewinnPot();
    private int highestBet;
    private Player lastPlayerToRaise;
    private PokerGUI gui;

    public Poker(int AnfangsChips, int bigBlind, int numPlayers, PokerGUI gui) {
        this.players = new ArrayList<>();
        this.gui = gui;
        for (int i = 0; i < numPlayers; i++) {
            this.players.add(new Player(AnfangsChips, "player" + (i + 1)));
        }
        this.blindsOrder = new ArrayList<>(players);
        this.dealer = new Dealer();
        this.deck = new Deck();
        int smallBlind = bigBlind / 2;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void kartenAusteilen() {
        System.out.println("Cards are dealt...");
        for (Player player : players) {
            for (int i = 0; i < 2; i++) {
                player.receiveCard(deck.kartenehmen());
            }
        }
        for (int i = 0; i <= 2; i++) {
            dealer.receiveCard(deck.kartenehmen());
        }
        spielerKartenAusgabe();
    }

    public void fold(int i) {
        players.get(i).setFolded(true);
        System.out.println("Player " + (i + 1) + " folded.");
    }

    public void call(int i, int highestBet) {
        Player currentPlayer = players.get(i);
        int additionalBetNeeded = highestBet - currentPlayer.getCurrentBet();
        if (additionalBetNeeded <= 0) {
            System.out.println(currentPlayer.getName() + " doesn't need to add any more chips. They've already matched the highest bet.");
        } else if (currentPlayer.getChips().getAmount() >= additionalBetNeeded) {
            currentPlayer.bet(additionalBetNeeded);
            GewinnPot.addChips(additionalBetNeeded); // Add the bet amount to the pot
            System.out.println(currentPlayer.getName() + " has called. Remaining chips: " + currentPlayer.getChips().getAmount());
        } else {
            System.out.println(currentPlayer.getName() + " doesn't have enough chips to call. Going all-in instead.");
            allIn(i);
        }
    }

    public void raise(int i, int betAmount) {
        Player player = players.get(i);
        if (betAmount > highestBet) {
            highestBet = betAmount;
            player.bet(betAmount);
            GewinnPot.addChips(betAmount); // Add the bet amount to the pot
            lastPlayerToRaise = players.get(i);
            System.out.println("Player " + (i + 1) + " has increased. The highest bet is now " + highestBet);
        } else {
            System.out.println("The raise amount must be higher than the current highest bet.");
        }
    }

    public void allIn(int i) {
        Player player = players.get(i);
        int betAmount = player.getChips().getAmount() + player.getCurrentBet(); // Add the player's current bet to the bet amount
        player.bet(betAmount);
        GewinnPot.addChips(betAmount);
        if (betAmount > highestBet) {
            highestBet = betAmount;
        }
        player.setAllIn(true);
        System.out.println(player.getName() + " is all in with " + betAmount + " chips.");
    }

    public boolean check(int i) {
        Player player = players.get(i);
        if (highestBet == 0 || player == lastPlayerToRaise) {
            System.out.println("Player " + (i + 1) + " checked.");
            return true;
        } else {
            System.out.println("Unauthorized action. You can't check because the current highest bet " + highestBet + " ist.");
            return false;
        }
    }

    public void wetten() {
    }

    public void spielerKartenAusgabe() {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if (!player.isFolded()) { // Skip players who have folded
                System.out.println("[DEBUG] Karten des Spielers " + (i + 1) + ": ");
                for (Card card : player.getHand()) {
                    System.out.println(card);
                }
            }
        }
    }

    public int getHighestBet() {
        return highestBet;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public void ausgabeDealerKarten() {
        highestBet = 0;
        System.out.println("Dealer's cards: " + dealer.getHand());
    }

    public void dealerneueKarte() {
        if (dealer.getHand().size() < 5) {
            Card drawnCard = dealer.drawCards();
            dealer.receiveCard(drawnCard);
            System.out.println("The dealer drew a new card: " + drawnCard);
            ausgabeDealerKarten();
        }
    }

    private void verarbeitungPot(Player winner) {
        if (winner == null) {
            int halfPot = GewinnPot.getAmount() / players.size();
            for (Player player : players) {
                player.getChips().addChips(halfPot);
            }
        } else {
            winner.getChips().addChips(GewinnPot.getAmount());
        }
        GewinnPot.clear();
    }

    public Player gewinner() {
        Player winner = null;
        int highestRank = -1;

        for (Player player : players) {
            if (player.isFolded()) {
                continue;
            }
            int playerRank = handRanker.rankHand(player.getHand(), dealer.getHand()).getRank();
            if (playerRank > highestRank) {
                highestRank = playerRank;
                winner = player;
            } else if (playerRank == highestRank) {
                int compareResult = handRanker.compareHighestCards(player.getHand(), winner.getHand());
                if (compareResult == 1) {
                    winner = player;
                }
            }
        }

        verarbeitungPot(winner);
        return winner;
    }

    public boolean checkAllIn() {
        boolean allIn = true;
        for (Player player : players) {
            if (!player.isAllIn()) {
                allIn = false;
                break;
            }
        }
        if (allIn) {
            while (dealer.getHand().size() < 5) {
                dealerneueKarte();
            }
        }
        return allIn;
    }

    public void blinds() {
        int smallBlind = 25;
        int bigBlind = 50;

        blindsOrder.get(0).bet(smallBlind);
        System.out.println(blindsOrder.get(0).getName() + " has posted the small blind of " + smallBlind + ". Remaining chips: " + blindsOrder.get(0).getChips().getAmount());

        blindsOrder.get(1).bet(bigBlind);
        lastPlayerToRaise = blindsOrder.get(1);
        System.out.println(blindsOrder.get(1).getName() + " has posted the big blind of " + bigBlind + ". Remaining chips: " + blindsOrder.get(1).getChips().getAmount());

        highestBet = bigBlind;

        Collections.rotate(blindsOrder, -1);
    }

    public void wettRunde() {
        // In der GUI wird diese Methode nicht mehr direkt verwendet
    }

    public void playRunde() {
        if (!checkAllIn()) {
            // Warten auf die nächste Benutzeraktion
        } else {
            while (dealer.getHand().size() < 5) {
                dealerneueKarte();
            }
        }
    }

    public int setAnzahlSpieler() {
        return players.size();
    }

    public Deck getdeck() {
        return deck;
    }

    public void resetPot() {
        this.GewinnPot.clear();
    }

    public void setdeck(Deck deck) {
        this.deck = deck;
    }
}
