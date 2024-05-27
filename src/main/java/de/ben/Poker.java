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
    HandRanker HandRanker = new HandRanker();
    private GewinnPot GewinnPot = new GewinnPot();
    private List<SidePot> sidePots = new ArrayList<>();
    private int bigBlind;
    private int smallBlind;
    private boolean SpielerEinsSmallBlind = true;

    public Poker(int AnfangsChips, int bigBlind) {
        this.player = new Player(AnfangsChips, "player");
        this.player2 = new Player(AnfangsChips, "player2");
        this.dealer = new Dealer();
        this.deck = new Deck();
        this.bigBlind = bigBlind;
        this.smallBlind = bigBlind / 2;


    }

    public void KartenAusteilen() {
        for (int i = 0; i < 2; i++) {
            player.receiveCard(dealer.drawCards());
            player2.receiveCard(dealer.drawCards());
        }
        for (int i = 0; i < 3; i++) {
            dealer.receiveCard(dealer.drawCards());
        }
        SpielerKartenAusgabe();
    }

    public void Wetten() {
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
                            currentPlayer.setFolded(true);
                            System.out.println("Spieler " + (i + 1) + " hat gefoldet.");
                            roundComplete = true;
                            break;
                        case "call":
                            if (highestBet != 0) {
                                betAmount = highestBet;
                                currentPlayer.bet(betAmount);
                                GewinnPot.addChips(betAmount);
                                System.out.println("Spieler " + (i + 1) + " hat gecalled mit " + betAmount);
                                roundComplete = true;
                            } else {
                                System.out.println("Unerlaubte Aktion. Du kannst nicht callen, weil der aktuelle höchste Einsatz 0 ist.");
                                i--; // repeat the turn for the same player
                            }
                            break;
                        case "raise":
                            System.out.println("Gib den Betrag ein, um den du erhöhen möchtest:");
                            betAmount = sc.nextInt();
                            if (betAmount > highestBet) {
                                highestBet = betAmount;
                                currentPlayer.bet(betAmount);
                                GewinnPot.addChips(betAmount);
                                System.out.println("Spieler " + (i + 1) + " hat erhöht. Der höchste Einsatz beträgt jetzt " + highestBet);
                            } else {
                                System.out.println("Der Erhöhungsbetrag muss höher sein als der aktuelle höchste Einsatz.");
                            }
                            break;
                        case "allin":
                            System.out.println("Spieler " + (i + 1) + " ist all in.");
                            betAmount = currentPlayer.getChips().getAmount();
                            currentPlayer.bet(betAmount);
                            GewinnPot.addChips(betAmount);
                            allInCounter++;
                            if (allInCounter == 2) {
                                roundComplete = true;
                            }
                            if (betAmount < highestBet) {
                                int difference = highestBet - betAmount;
                                SidePot sidePot = new SidePot(difference, Arrays.asList(player, player2));
                                sidePot.removePlayer(currentPlayer);
                                sidePots.add(sidePot);
                            } else {
                                highestBet = betAmount;
                            }
                            currentPlayer.setAllIn(true);
                            break;
                        case "check":
                            if (highestBet == 0) {
                                System.out.println("Spieler " + (i + 1) + " hat gecheckt.");
                                checkCounter++; // Increment the check counter
                                if (checkCounter == 2) { // If both players have checked
                                    roundComplete = true;
                                }
                            } else {
                                System.out.println("Unerlaubte Aktion. Du kannst nicht checken, weil der aktuelle höchste Einsatz " + highestBet + " ist.");
                                i--; // repeat the turn for the same player
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

        System.out.println("Der Pot beträgt: " + GewinnPot.getAmount());
        System.out.println("Der Pot beträgt: " + GewinnPot.getAmount());
        for (SidePot sidePot : sidePots) {
            sidePot.printAmount();
        }    }

    public void SpielerKartenAusgabe() {
        System.out.println("Karten des Spielers 1: " + player.getHand());
        System.out.println("Karten des Spielers 2: " + player2.getHand());
    }

    public void AusgabeDealerKarten() {
        System.out.println("Karten des Dealers: " + dealer.getHand());
    }

    public void dealerneuekarte() {
        if (dealer.getHand().size() < 5) {
            dealer.receiveCard(dealer.drawCards());
        }
        System.out.println("Karten des Dealers: " + dealer.getHand());
    }

    public void neueRunde() {
        this.deck = new Deck();
        dealer.setDeck(this.deck);
        GewinnPot.clear();
        player.getHand().clear();
        player2.getHand().clear();
    }

    public Player Gewinner() {
        if (HandRanker.rankHand(player.getHand(), dealer.getHand()).getRank() > HandRanker.rankHand(player2.getHand(), dealer.getHand()).getRank()) {
            player.getChips().addChips(GewinnPot.getAmount());
            GewinnPot.clear();
            return player;
        } else {
            player2.getChips().addChips(GewinnPot.getAmount());
            GewinnPot.clear();
            return player2;
        }
    }

    public void getEligiblePlayers() {
        Player sidePotWinner = null;
        int highestRank = -1;

        for (Player player : sidePots.get(0).getEligiblePlayers()) {
            int playerRank = HandRanker.rankHand(player.getHand(), dealer.getHand()).getRank();
            if (playerRank > highestRank) {
                highestRank = playerRank;
                sidePotWinner = player;
            }
        }

        if (sidePotWinner != null) {
            sidePotWinner.getChips().addChips(sidePots.get(0).getAmount());
        }
    }

    public void Blinds() {
        if (SpielerEinsSmallBlind) {
            player.bet(smallBlind);
            player2.bet(bigBlind);
            System.out.println("Player 1 hat den small Blind von " + smallBlind + " bezahlt");
            System.out.println("Player 2 hat den big Blind von " + bigBlind + " bezahlt");
            GewinnPot.addChips(smallBlind + bigBlind);
        } else {
            player.bet(bigBlind);
            player2.bet(smallBlind);
            System.out.println("Player 1 hat den big Blind von " + bigBlind + " bezahlt");
            System.out.println("Player 2 hat den small Blind von " + smallBlind + " bezahlt");
            GewinnPot.addChips(smallBlind + bigBlind);
        }
        SpielerEinsSmallBlind = !SpielerEinsSmallBlind;
    }

    public boolean checkAllIn() {
        if (player.isAllIn() && player2.isAllIn()) {
            // All players are all in, skip to the hand ranking
            while (dealer.getHand().size() < 5) {
                dealerneuekarte();
            }
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Poker poker = new Poker(5000, 50);

        do {
            System.out.println("Player 1 initial chips: " + poker.player.getChips().getAmount());
            System.out.println("Player 2 initial chips: " + poker.player2.getChips().getAmount());
            poker.KartenAusteilen();
            poker.Blinds();
            poker.Wetten();
            if (poker.checkAllIn()) break;
            poker.AusgabeDealerKarten();
            poker.Wetten();
            if (poker.checkAllIn()) break;
            if (poker.dealer.getHand().size() < 5) {
                poker.dealerneuekarte();
            }
            poker.Wetten();
            if (poker.checkAllIn()) break;
            if (poker.dealer.getHand().size() < 5) {
                poker.dealerneuekarte();
            }
            poker.Wetten();
            if (poker.checkAllIn()) break;
            // ...
        } while (poker.player.getChips().getAmount() != 0 && poker.player2.getChips().getAmount() != 0);
        Player winner = poker.Gewinner();
        System.out.println(winner.getName() + " is the winner!");
    }

}