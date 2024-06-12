package de.ben;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

public class Poker {
    Scanner sc = new Scanner(System.in);
    private Player player;
    private Player player2;
    private Dealer dealer;
    private Deck deck;
    HandRanker handRanker = new HandRanker();
    private GewinnPot gewinnPot = new GewinnPot();
    private List<SidePot> sidePots = new ArrayList<>();
    private int bigBlind;
    private int smallBlind;
    private boolean spielerEinsSmallBlind = true;

    public Poker(int anfangsChips, int bigBlind) {
        this.player = new Player(anfangsChips, "player");
        this.player2 = new Player(anfangsChips, "player2");
        this.dealer = new Dealer();
        this.deck = new Deck();
        this.bigBlind = bigBlind;
        this.smallBlind = bigBlind / 2;
    }

    public void kartenAusteilen() {
        for (int i = 0; i < 2; i++) {
            player.receiveCard(dealer.drawCards());
            player2.receiveCard(dealer.drawCards());
        }
        for (int i = 0; i < 3; i++) {
            dealer.receiveCard(dealer.drawCards());
        }
        spielerKartenAusgabe();
    }

    public void wetten() {
        int highestBet = 0;
        boolean roundComplete = false;
        int checkCounter = 0;
        int allInCounter = 0;
        while (!roundComplete) {
            for (int i = 0; i < 2; i++) {
                Player currentPlayer = (i == 0) ? player : player2;
                if (!currentPlayer.isFolded()) {
                    if (highestBet == 0) {
                        System.out.println("Spieler " + (i + 1) + ": Höchster Einsatz ist " + highestBet + ". Du kannst folden, checken oder erhöhen.");
                    } else {
                        System.out.println("Spieler " + (i + 1) + ": Höchster Einsatz ist " + highestBet + ". Du kannst folden, callen oder erhöhen.");
                    }
                    String action = sc.next();
                    int betAmount = 0;

                    switch (action.toLowerCase()) {
                        case "fold":
                            handleFold(currentPlayer, i + 1);
                            roundComplete = true;
                            break;
                        case "call":
                            handleCall(currentPlayer, highestBet, i + 1);
                            roundComplete = true;
                            break;
                        case "raise":
                            handleRaise(currentPlayer, highestBet, i + 1);
                            break;
                        case "allin":
                            handleAllIn(currentPlayer, highestBet, allInCounter, i + 1);
                            allInCounter++;
                            if (allInCounter == 2) {
                                roundComplete = true;
                            }
                            break;
                        case "check":
                            handleCheck(currentPlayer, highestBet, checkCounter, i + 1);
                            checkCounter++;
                            if (checkCounter == 2) {
                                roundComplete = true;
                            }
                            break;
                        default:
                            System.out.println("Unerlaubte Aktion. Bitte gib fold, call oder raise ein.");
                            i--; // repeat the turn for the same player
                            break;
                    }
                }
                if (roundComplete) {
                    break;
                }
            }
        }

        System.out.println("Der Pot beträgt: " + gewinnPot.getAmount());
        for (SidePot sidePot : sidePots) {
            sidePot.printAmount();
        }
    }

    public void handleFold(Player currentPlayer, int playerNumber) {
        currentPlayer.setFolded(true);
        System.out.println("Spieler " + playerNumber + " hat gefoldet.");
    }

    public void handleCall(Player currentPlayer, int highestBet, int playerNumber) {
        if (highestBet != 0) {
            currentPlayer.bet(highestBet);
            gewinnPot.addChips(highestBet);
            System.out.println("Spieler " + playerNumber + " hat gecalled mit " + highestBet);
        } else {
            System.out.println("Unerlaubte Aktion. Du kannst nicht callen, weil der aktuelle höchste Einsatz 0 ist.");
        }
    }

    public void handleRaise(Player currentPlayer, int highestBet, int playerNumber) {
        System.out.println("Gib den Betrag ein, um den du erhöhen möchtest:");
        int betAmount = sc.nextInt();
        if (betAmount > highestBet) {
            highestBet = betAmount;
            currentPlayer.bet(betAmount);
            gewinnPot.addChips(betAmount);
            System.out.println("Spieler " + playerNumber + " hat erhöht. Der höchste Einsatz beträgt jetzt " + highestBet);
        } else {
            System.out.println("Der Erhöhungsbetrag muss höher sein als der aktuelle höchste Einsatz.");
        }
    }

    public void handleAllIn(Player currentPlayer, int highestBet, int allInCounter, int playerNumber) {
        System.out.println("Spieler " + playerNumber + " ist all in.");
        int betAmount = currentPlayer.getChips().getAmount();
        currentPlayer.bet(betAmount);
        gewinnPot.addChips(betAmount);
        if (betAmount < highestBet) {
            int difference = highestBet - betAmount;
            SidePot sidePot = new SidePot(difference, Arrays.asList(player, player2));
            sidePot.removePlayer(currentPlayer);
            sidePots.add(sidePot);
        } else {
            highestBet = betAmount;
        }
        currentPlayer.setAllIn(true);
    }

    public void handleCheck(Player currentPlayer, int highestBet, int checkCounter, int playerNumber) {
        if (highestBet == 0) {
            System.out.println("Spieler " + playerNumber + " hat gecheckt.");
        } else {
            System.out.println("Unerlaubte Aktion. Du kannst nicht checken, weil der aktuelle höchste Einsatz " + highestBet + " ist.");
        }
    }

    public Player getCurrentPlayer(int playerIndex) {
        if (playerIndex == 1) {
            return player;
        } else {
            return player2;
        }
    }

    public void spielerKartenAusgabe() {
        System.out.println("Karten des Spielers 1: " + player.getHand());
        System.out.println("Karten des Spielers 2: " + player2.getHand());
    }

    public void ausgabeDealerKarten() {
        System.out.println("Karten des Dealers: " + dealer.getHand());
    }

    public void dealerNeueKarte() {
        if (dealer.getHand().size() < 5) {
            dealer.receiveCard(dealer.drawCards());
        }
        System.out.println("Karten des Dealers: " + dealer.getHand());
    }

    public void neueRunde() {
        this.deck = new Deck();
        dealer.setDeck(this.deck);
        gewinnPot.clear();
        player.getHand().clear();
        player2.getHand().clear();
    }

    public Player gewinner() {
        if (handRanker.rankHand(player.getHand(), dealer.getHand()).getRank() > handRanker.rankHand(player2.getHand(), dealer.getHand()).getRank()) {
            player.getChips().addChips(gewinnPot.getAmount());
            gewinnPot.clear();
            return player;
        } else {
            player2.getChips().addChips(gewinnPot.getAmount());
            gewinnPot.clear();
            return player2;
        }
    }

    public void getEligiblePlayers() {
        Player sidePotWinner = null;
        int highestRank = -1;

        for (Player player : sidePots.get(0).getEligiblePlayers()) {
            int playerRank = handRanker.rankHand(player.getHand(), dealer.getHand()).getRank();
            if (playerRank > highestRank) {
                highestRank = playerRank;
                sidePotWinner = player;
            }
        }

        if (sidePotWinner != null) {
            sidePotWinner.getChips().addChips(sidePots.get(0).getAmount());
        }
    }

    public void blinds() {
        if (spielerEinsSmallBlind) {
            player.bet(smallBlind);
            player2.bet(bigBlind);
            System.out.println("Player 1 hat den small Blind von " + smallBlind + " bezahlt");
            System.out.println("Player 2 hat den big Blind von " + bigBlind + " bezahlt");
            gewinnPot.addChips(smallBlind + bigBlind);
        } else {
            player.bet(bigBlind);
            player2.bet(smallBlind);
            System.out.println("Player 1 hat den big Blind von " + bigBlind + " bezahlt");
            System.out.println("Player 2 hat den small Blind von " + smallBlind + " bezahlt");
            gewinnPot.addChips(smallBlind + bigBlind);
        }
        spielerEinsSmallBlind = !spielerEinsSmallBlind;
    }

    public boolean checkAllIn() {
        if (player.isAllIn() && player2.isAllIn()) {
            // All players are all in, skip to the hand ranking
            while (dealer.getHand().size() < 5) {
                dealerNeueKarte();
            }
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Poker poker = new Poker(5000, 50);
        poker.startGame();
    }

    public void startGame() {
        do {
            System.out.println("Player 1 initial chips: " + player.getChips().getAmount());
            System.out.println("Player 2 initial chips: " + player2.getChips().getAmount());
            kartenAusteilen();
            blinds();
            wetten();
            if (checkAllIn()) break;
            ausgabeDealerKarten();
            wetten();
            if (checkAllIn()) break;
            if (dealer.getHand().size() < 5) {
                dealerNeueKarte();
            }
            wetten();
            if (checkAllIn()) break;
            if (dealer.getHand().size() < 5) {
                dealerNeueKarte();
            }
            wetten();
            if (checkAllIn()) break;
        } while (player.getChips().getAmount() != 0 && player2.getChips().getAmount() != 0);
        Player winner = gewinner();
        System.out.println(winner.getName() + " is the winner!");
    }
}
