package de.ben;

import java.util.*;

public class Poker {
    Scanner sc = new Scanner(System.in);
    private Player player;
    private Player player2;
    private Dealer dealer;
    private Deck deck;
    HandRanker handRanker = new HandRanker();
    private GewinnPot GewinnPot = new GewinnPot();
    private List<SidePot> sidePots = new ArrayList<>();
    private int bigBlind;
    private int smallBlind;
    private boolean SpielerEinsSmallBlind = true;
    private int highestBet;

    public Poker(int AnfangsChips, int bigBlind) {
        this.player = new Player(AnfangsChips, "player");
        this.player2 = new Player(AnfangsChips, "player2");
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



    public void fold(Player player, int i) {
        player.setFolded(true);
        System.out.println("Spieler " + (i + 1) + " hat gefoldet.");
    }

    public void call(Player player, int i, int highestBet) {
        int betAmount = highestBet;
        player.bet(betAmount);
        GewinnPot.addChips(betAmount);
        System.out.println("Spieler " + (i + 1) + " hat gecalled mit " + betAmount);
    }

    public void raise(Player player, int i, int highestBet) {
        boolean validInput = false;
        while (!validInput) {
            System.out.println("Gib den Betrag ein, um den du erhöhen möchtest:");
            try {
                int betAmount = sc.nextInt();
                if (betAmount > highestBet) {
                    highestBet = betAmount;
                    player.bet(betAmount);
                    GewinnPot.addChips(betAmount);
                    System.out.println("Spieler " + (i + 1) + " hat erhöht. Der höchste Einsatz beträgt jetzt " + highestBet);
                    validInput = true;
                } else {
                    System.out.println("Der Erhöhungsbetrag muss höher sein als der aktuelle höchste Einsatz.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Unerlaubte Eingabe. Bitte gib eine ganze Zahl ein.");
                sc.next();
            }
        }
    } public void raise(Player player, int i) {
        boolean validInput = false;
        while (!validInput) {
            System.out.println("Gib den Betrag ein, um den du erhöhen möchtest:");
            try {
                int betAmount = sc.nextInt();
                if (betAmount > highestBet) {
                    highestBet = betAmount; // Update the instance variable
                    player.bet(betAmount);
                    GewinnPot.addChips(betAmount);
                    System.out.println("Spieler " + (i + 1) + " hat erhöht. Der höchste Einsatz beträgt jetzt " + highestBet);
                    validInput = true;
                } else {
                    System.out.println("Der Erhöhungsbetrag muss höher sein als der aktuelle höchste Einsatz.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Unerlaubte Eingabe. Bitte gib eine ganze Zahl ein.");
                sc.next();
            }
        }
    }

    public void allIn(Player player, int i) {
        System.out.println("Spieler " + (i + 1) + " ist all in.");
        int betAmount = player.getChips().getAmount();
        player.bet(betAmount);
        GewinnPot.addChips(betAmount);
        if (betAmount > highestBet) {
            highestBet = betAmount;
        }
        player.setAllIn(true);
    }

    public void check(Player player, int i, int highestBet) {
        if (highestBet == 0) {
            System.out.println("Spieler " + (i + 1) + " hat gecheckt.");
        } else {
            System.out.println("Unerlaubte Aktion. Du kannst nicht checken, weil der aktuelle höchste Einsatz " + highestBet + " ist.");
        }
    }
    public void wetten() {
        highestBet = 0;
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

                    switch (action.toLowerCase()) {
                        case "fold":
                            fold(currentPlayer, i);
                            roundComplete = true;
                            break;
                        case "call":
                            if (highestBet != 0) {
                                call(currentPlayer, i, highestBet);
                                roundComplete = true;
                            } else {
                                System.out.println("Unerlaubte Aktion. Du kannst nicht callen, weil der aktuelle höchste Einsatz 0 ist.");
                                i--; // repeat the turn for the same player
                            }
                            break;
                        case "raise":
                            raise(currentPlayer, i);
                            break;
                        case "allin":
                            allIn(currentPlayer, i); // Remove the highestBet argument
                            allInCounter++;
                            if (allInCounter == 2) {
                                roundComplete = true;
                            }
                            break;
                        case "check":
                            check(currentPlayer, i, highestBet);
                            checkCounter++; // Increment the check counter
                            if (checkCounter == 2) { // If both players have checked
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

        System.out.println("Der Pot beträgt: " + GewinnPot.getAmount());
        for (SidePot sidePot : sidePots) {
            sidePot.printAmount();
        }
    }

    public void spielerKartenAusgabe() {
        System.out.println("Karten des Spielers 1: " + player.getHand());
        System.out.println("Karten des Spielers 2: " + player2.getHand());
    }

    public void ausgabeDealerKarten() {
        System.out.println("Karten des Dealers: " + dealer.getHand());
    }

    public void dealerneueKarte() {
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

    private void verarbeitungPot(Player winner) {
        if (winner == null) {
            int halfPot = GewinnPot.getAmount() / 2;
            player.getChips().addChips(halfPot);
            player2.getChips().addChips(halfPot);
        } else {
            winner.getChips().addChips(GewinnPot.getAmount());
        }
        GewinnPot.clear();
    }

    public Player gewinner() {
        int player1Rank = (handRanker.rankHand(player.getHand(), dealer.getHand()).getRank());
        int player2Rank = (handRanker.rankHand(player2.getHand(), dealer.getHand()).getRank());

        Player winner = null;
        if (player1Rank > player2Rank) {
            winner = player;
        } else if (player1Rank < player2Rank) {
            winner = player2;
        } else { // Gleiches Ranking
            int compareResult = handRanker.compareHighestCards(player.getHand(), player2.getHand());
            if (compareResult == 1) {
                winner = player;
            } else if (compareResult == -1) {
                winner = player2;
            }
        }

        verarbeitungPot(winner);
        return winner;
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

    private void verarbeitungBlinds(Player smallBlindPlayer, Player bigBlindPlayer) {
        smallBlindPlayer.bet(smallBlind);
        bigBlindPlayer.bet(bigBlind);
        System.out.println(smallBlindPlayer.getName() + " hat den small Blind von " + smallBlind + " bezahlt");
        System.out.println(bigBlindPlayer.getName() + " hat den big Blind von " + bigBlind + " bezahlt");
        GewinnPot.addChips(smallBlind + bigBlind);
    }

    public void blinds() {
        if (SpielerEinsSmallBlind) {
            verarbeitungBlinds(player, player2);
        } else {
            verarbeitungBlinds(player2, player);
        }
        SpielerEinsSmallBlind = !SpielerEinsSmallBlind;
    }

    public boolean checkAllIn() {
        if (player.isAllIn() && player2.isAllIn()) {
            while (dealer.getHand().size() < 5) {
                dealerneueKarte();
            }
            return true;
        }
        return false;
    }

    public void wettRunde() {
        wetten();
        if (checkAllIn()) {
            while (dealer.getHand().size() < 5) {
                dealerneueKarte();
            }
        }
    }

    public void dealerZiehtKarten() {
        if (dealer.getHand().size() < 5) {
            dealerneueKarte();
        }
    }
    public void playRunde() {
        if (!checkAllIn()) {
            wettRunde();
            dealerZiehtKarten();
        }
    }

    public static void main(String[] args) {
        Poker poker = new Poker(5000, 50);

        do {
            System.out.println("Player 1 initial chips: " + poker.player.getChips().getAmount());
            System.out.println("Player 2 initial chips: " + poker.player2.getChips().getAmount());
            poker.kartenAusteilen();
            poker.blinds();
            poker.playRunde();
        } while (poker.player.getChips().getAmount() != 0 && poker.player2.getChips().getAmount() != 0);

        poker.ausgabeDealerKarten();

        Player winner = poker.gewinner();
        if (winner == null) {
            System.out.println("Unentschieden! Der Pot wird geteilt.");
        } else {
            System.out.println(winner.getName() + " hat gewonnen mit " + poker.handRanker.rankHand(winner.getHand(), poker.dealer.getHand()));
        }
    }
}