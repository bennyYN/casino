package de.ben.poker;

import de.ben.MainGUI;

import java.util.*;

/**
 * @author Alexander Fischer, Maurice Steimer
 * @version 1.0
 * @Description: Logikklasse, die das Spiel steuert und die Spieler, Dealer und Karten verwaltet.
 */
public class Poker extends Thread {
    public boolean playerWon = false;
    boolean alreadyDidThatFlag = false;
    private final ArrayList<Boolean> actualPlayers;
    Scanner sc = new Scanner(System.in);
    List<Player> players;
    final List<Player> blindsOrder;
    public final Dealer dealer;
    private Deck deck;
    HandRanker handRanker = new HandRanker();
    public final GewinnPot GewinnPot = new GewinnPot();
    public Player bigBlindPlayer, smallBlindPlayer;
    public int highestBet;
    private Player lastPlayerToRaise;
    int anzahlSpieler;
    int bigBlind, smallBlind;
    public volatile boolean isGameOver = false;
    PokerGUI gui;
    Player currentPlayer;
    boolean ending = false;

    //Konstruktor
    public Poker(int AnfangsChips, int bigBlind, int numPlayers, ArrayList<Boolean> actualPlayers, PokerGUI gui) {
        this.actualPlayers = actualPlayers;
        this.gui = gui;

        this.players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            this.players.add(Playerslot.players.get(i));
        }
        this.blindsOrder = new ArrayList<>(players);
        this.dealer = new Dealer();
        if(this.deck == null){
            this.deck = new Deck();
        }
        this.bigBlind = bigBlind;
        smallBlind = bigBlind / 2;
        anzahlSpieler = numPlayers;

    }

    //Methode zum Austeilen der Karten an den Dealer und jeden Spieler
    public void kartenAusteilen() {
        for (Player player : players) {
            if(player != null) {
                player.receiveCard(deck.kartenehmen(), deck.kartenehmen());
            }
        }
        if(dealer.getHand().size() >= 5){
            dealer.clearHand();
            for (int i = 0; i <= 2; i++) {
                dealer.receiveCard(deck.kartenehmen());
            }
        }else{
            for (int i = 0; i <= 2; i++) {
                dealer.receiveCard(deck.kartenehmen());
            }
        }
        spielerKartenAusgabe();
    }

    //Methoden für die verschiedenen Aktionen der Spieler
    public void fold(int i) {
        players.get(i).setFolded(true);
        System.out.println("Spieler " + (i + 1) + " hat gefoldet.");
        gui.addMessageToDialogBox(currentPlayer.getName() + " folds.");
    }

    public void call(int i, int highestBet) {
        Player currentPlayer = players.get(i);
        int additionalBetNeeded = highestBet - currentPlayer.getCurrentBet();
        if (additionalBetNeeded <= 0) {
            System.out.println(currentPlayer.getName() + " der Spieler brauch keine weiteren Chips callen. Er hat den momentanen Einsatz schon gesetzt.");
            gui.fadingLabel.setText("Du brauchst keine weiteren Chips callen, da du den momentanen Einsatz schon gesetzt hast.");
            gui.addMessageToDialogBox(currentPlayer.getName() + " calls.");
        } else if (currentPlayer.getChips().getAmount() >= additionalBetNeeded) {
            currentPlayer.bet(additionalBetNeeded);
            GewinnPot.addChips(additionalBetNeeded); // Add the bet amount to the pot
            System.out.println(currentPlayer.getName() + " hat gecalled. Übrige Chips: " + currentPlayer.getChips().getAmount());
            gui.addMessageToDialogBox(currentPlayer.getName() + " calls.");
        } else {
            System.out.println(currentPlayer.getName() + " hat nicht genügend Chips zum callen. Der Spieler geht stattdessen all in.");
            gui.fadingLabel.setText("Nicht genug Chips zum callen. Du gehst stattdessen all in!");
            gui.addMessageToDialogBox(currentPlayer.getName() + " goes all in!");
            allIn(i);
        }
    }

    public void raise(int i) {
        Player player = players.get(i);
        boolean validInput = false;
        while (!validInput) {
            //System.out.println("Gib den Betrag ein, um den du erhöhen möchtest:");
            try {

                int betAmount = gui.raiseAmount;
                if (betAmount > highestBet) {
                    highestBet = betAmount + player.getCurrentBet();
                    player.bet(betAmount);
                    GewinnPot.addChips(betAmount); // Add the bet amount to the pot
                    lastPlayerToRaise = players.get(i);
                    System.out.println("Spieler " + (i + 1) + " hat erhöht. Der höchste Einsatz beträgt jetzt " + highestBet);
                    gui.addMessageToDialogBox(currentPlayer.getName() + " raises to " + highestBet + ".");
                    validInput = true;
                    gui.hideRaiseField();
                } else {
                    System.out.println("Der Erhöhungsbetrag muss höher sein als der aktuelle höchste Einsatz.");
                    gui.fadingLabel.setText("Der Erhöhungsbetrag muss höher sein als der aktuelle höchste Einsatz.");
                }
            } catch (Exception e) {
                System.out.println("Unerlaubte Eingabe. Bitte gib eine ganze Zahl ein.");
                gui.fadingLabel.setText("Unerlaubte Eingabe! Bitte gib eine ganze Zahl ein.");

            }
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
        gui.addMessageToDialogBox(currentPlayer.getName() + " goes all in!");
    }

    public boolean check(int i) {
        Player player = players.get(i);
        if (highestBet == 0 || player == lastPlayerToRaise) {
            System.out.println("Spieler " + (i + 1) + " hat gecheckt.");
            gui.addMessageToDialogBox(currentPlayer.getName() + " checks.");
            return true;
        } else if(highestBet == player.getCurrentBet()){
            System.out.println("Spieler " + (i + 1) + " hat gecheckt.");
            gui.addMessageToDialogBox(currentPlayer.getName() + " checks.");
            return true;
        }
        else {
            System.out.println("Unerlaubte Aktion. Du kannst nicht checken, weil der aktuelle höchste Einsatz " + highestBet + " ist.");
            gui.fadingLabel.setText("Unerlaubte Aktion! Du kannst nicht checken, weil der aktuelle höchste Einsatz " + highestBet + " ist.");
            return false;
        }
    }

    public void wetten() {
        boolean gamecomplete = false;
        boolean roundComplete = false;
        int checkCounter = 0;
        while (!roundComplete && !gamecomplete) {
            int i = 0;
            while (i < players.size()) {
                currentPlayer = players.get(i);
                if (!currentPlayer.isFolded() && !currentPlayer.isAllIn()) {
                    if (highestBet == 0) {
                        System.out.println("Spieler " + (i + 1) + ": Höchster Einsatz ist " + highestBet + ". Du kannst folden, checken oder erhöhen.");
                    } else {
                        System.out.println("Spieler " + (i + 1) + ": Höchster Einsatz ist " + highestBet + ". Du kannst folden, callen oder erhöhen.");
                    }

                    boolean validAction = false;
                    String action;

                    // Warte bis eine Aktion kommt, die nicht "idle" ist
                    while (true) {
                        action = gui.getAction();
                        if (!"idle".equals(action)) {
                            break;
                        }
                        try {
                            // Kurze Pause bevor erneut geprüft wird
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }

                    switch (action.toLowerCase()) {
                        case "fold":
                            fold(i);
                            validAction = true;
                            break;
                        case "call":
                            if (highestBet != 0) {
                                call(i, highestBet);
                                validAction = true;
                            } else {
                                System.out.println("Unerlaubte Aktion. Du kannst nicht callen, weil der aktuelle höchste Einsatz 0 ist oder du nicht genug Chips hast.");
                                gui.fadingLabel.setText("Du kannst nicht callen, weil der aktuelle höchste Einsatz 0 ist oder du nicht genug Chips hast.");
                            }
                            break;
                        case "raise":
                            if (currentPlayer.getChips().getAmount() > highestBet) {
                                raise(i);
                                validAction = true;
                            } else {
                                System.out.println("Unerlaubte Aktion. Du kannst nicht erhöhen, weil du nicht genug Chips hast.");
                                gui.fadingLabel.setText("Unerlaubte Aktion! Du kannst nicht erhöhen, weil du nicht genug Chips hast.");
                            }
                            break;
                        case "allin":
                            allIn(i);
                            if (checkAllIn()) {
                                roundComplete = true;
                                gamecomplete = true;
                            }
                            validAction = true;
                            break;
                        case "check":
                            if (check(i)) {
                                validAction = true;
                                checkCounter++;
                                if (checkCounter == players.size()) {
                                    roundComplete = true;
                                }
                            }
                            break;
                        default:
                            try {
                                int betAmount = Integer.parseInt(action);
                                currentPlayer.bet(betAmount);
                                validAction = true;
                            } catch (NumberFormatException e) {
                                System.out.println("Unerlaubte Eingabe. Bitte gib eine ganze Zahl ein oder 'allin'.");
                                gui.fadingLabel.setText("Ungültige Eingabe!");
                            }
                            break;
                    }
                    gui.setAction("idle");
                    if (validAction) {
                        // Setze den action String auf "idle" zurück und bewege zum nächsten Spieler
                        MainGUI.playSound("valid");
                        i++;
                    } else {
                        MainGUI.playSound("invalid");
                    }
                } else {
                    i++;
                }

                //Check ob alle außer einer gefolded haben
                long activePlayers = players.stream().filter(player -> !player.isFolded()).count();
                if (activePlayers == 1) {
                    Player winner = players.stream().filter(player -> !player.isFolded()).findFirst().orElse(null);
                    if (winner != null) {
                        System.out.println(winner.getName() + " gewinnt, da alle anderen Spieler gefoldet haben.");
                        gui.fadingLabel.setText(winner.getName() + " gewinnt, da alle anderen Spieler gefoldet haben.");
                        verarbeitungPot(winner);
                        roundComplete = true;
                        gamecomplete = true;
                    }
                }

                if (roundComplete) {
                    break;
                }
            }
            boolean allFoldedOrMatched = true;
            for (Player player : players) {
                if (!player.isFolded() && player.getCurrentBet() != highestBet) {
                    allFoldedOrMatched = false;
                    break;
                }
            }
            if (allFoldedOrMatched) {
                roundComplete = true;
            }
        }
        for (Player player : players) {
            player.resetCurrentBet();
        }
        System.out.println("Der Pot beträgt: " + GewinnPot.getAmount());
    }

    //Methode zur Ausgabe der Karten an die Spieler
    public void spielerKartenAusgabe() {
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if (player != null && !player.isFolded()) { // Skip players who have folded
                System.out.println("Karten des Spielers " + (i + 1) + ": ");
                for (Card card : player.getHand()) {
                    System.out.println(card);
                }
            }
        }
    }

    //Methode zum Ausgeben der Karten an den Dealer
    public void ausgabeDealerKarten() {
        highestBet = 0;
        System.out.println("Karten des Dealers: " + dealer.getHand());
    }

    //Methode zum Ziehen einer neuen Karte für den Dealer
    public void dealerneueKarte() {
        if (dealer.getHand().size() < 5) {
            Card drawnCard = deck.kartenehmen();
            dealer.receiveCard(drawnCard);
            System.out.println("Dealer hat eine neue Karte gezogen: " + drawnCard);
            gui.fadingLabel.setText("Dealer hat eine neue Karte gezogen: " + drawnCard);
            ausgabeDealerKarten();
        }
    }

    //Methode zum Verwalten des Gewinnpots
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

    //Methode um den Gewinner zu ermitteln
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


        if(winner != null && alreadyDidThatFlag == false){
            if(gui.mainGUI.playerIndex != -1){
                gui.viewCardButtons.get(gui.mainGUI.playerIndex).doClick();
            }else{
                for(int i = 0; i < players.size(); i++){
                    if(players.get(i).equals(winner)){
                        gui.viewCardButtons.get(i).doClick();
                    }
                }
            }
            alreadyDidThatFlag = true;
        }
        return winner;
    }

    //Methode um zu prüfen, ob alle Spieler all in sind
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

    //Methode zum Verwalten der Blinds
    public void blinds() {
        blindsOrder.get(0).bet(smallBlind);
        GewinnPot.addChips(smallBlind);
        System.out.println(blindsOrder.get(0).getName() + " hat den Small Blind von " + smallBlind + " gesetzt. Übrige Chips: " + blindsOrder.get(0).getChips().getAmount());

        blindsOrder.get(1).bet(bigBlind);
        GewinnPot.addChips(bigBlind);
        //lastPlayerToRaise = blindsOrder.get(1);
        System.out.println(blindsOrder.get(1).getName() + " hat den Big Blind von " + bigBlind + " gesetzt. Übrige Chips: " + blindsOrder.get(1).getChips().getAmount());

        // Set the highestBet to the value of the bigBlind
        highestBet = bigBlind;

        // BB und SB Spieler abspeichern für anzeige
        smallBlindPlayer = blindsOrder.get(0);
        bigBlindPlayer = blindsOrder.get(1);

        // Rotate the blindsOrder list so that the next player is at the start of the list
        Collections.rotate(blindsOrder, -1);
    }

    public void wettRunde() {
        wetten();
        if (checkAllIn()) {
            while (dealer.getHand().size() < 5) {
                dealerneueKarte();
            }
        }
    }

    public void playRunde() {
        if (!checkAllIn()) {
            wettRunde();
        } else {
            while (dealer.getHand().size() < 5) {
                dealerneueKarte();
            }
        }
    }

    //Methode zum Aktualisieren des Spielstatus
    private void updateGameState(){
        int temp = 0;
        for(Player player : players){
            if(!player.isFolded()){
                temp++;
            }
        }
        if(temp == 1){
            ending = true;
        }else if(temp <= 0){
            ending = true;
            playerWon = true;
        }
    }

    //Methode zum Zurücksetzen der Spielerzustände
    private void resetPlayerStates(){
        for(Player player : players){
            if(player.isFolded() && !player.dummy){
                player.setFolded(false);
            }
            if(player.isAllIn() && !player.dummy){
                player.setAllIn(false);
            }
        }
    }

    //Methode zum starten des Spiels (wird von der GUI im seperaten Thread aufgerufen)
    public void startGame() {

        Poker poker = this;

        System.out.println("Ihr habt alle zu Beginn " + gui.startChips + " Chips.");

        for (Player player : poker.players) player.setFolded(false);

        for(int i = 0; i <= (poker.players.size()-1); i++){
            if(!poker.actualPlayers.get(i)){
                poker.players.get(i).setFolded(true);
                poker.players.get(i).dummy = (true);
            }
        }

        while (!isGameOver) {

            //Game- & Playerstates zurücksetzen
            playerWon = false;
            dealer.handVisible = false;
            ending = false;
            resetPlayerStates();

            //(Neue) Karten ausgeben
            poker.kartenAusteilen();

            updateGameState();

            //Blinds rotieren lassen und bezahlen
            poker.blinds();

            updateGameState();
            if(!ending) {
                // Betting
                poker.playRunde();
            }

            // set dealer hand visible
            dealer.handVisible = true;

            // Dealer revealing his cards
            poker.ausgabeDealerKarten();

            updateGameState();
            if(!ending) {
                // Betting
                poker.playRunde();
            }

            // Dealer getting a card
            poker.dealerneueKarte();

            updateGameState();
            if(!ending) {
                // Betting
                poker.playRunde();
            }

            // Dealer getting the last card
            poker.dealerneueKarte();

            updateGameState();
            if(!ending){
                // Betting
                poker.playRunde();
            }

            // Winner reveal
            MainGUI.playSound("win");
            Player winner = poker.gewinner();
            if (winner == null) {
                System.out.println("Unentschieden! Der Pot wird geteilt.");
                gui.fadingLabel.setText("Unentschieden! Der Pot wird geteilt.", false);
                playerWon = true;

            } else {
                System.out.println(winner.getName() + " hat gewonnen mit " + poker.handRanker.rankHand(winner.getHand(), poker.dealer.getHand()));
                gui.fadingLabel.setText(winner.getName() + " hat gewonnen mit " + poker.handRanker.rankHand(winner.getHand(), poker.dealer.getHand()), false);
                playerWon = true;
                //Spielernummer des Gewinners suchen und in die GUI speichern
                int temp = 0;
                for(Player player : players){
                    if(player.equals(winner)){
                        gui.playerShowing = temp;
                        ViewCardButton.setWinner(temp);
                    }else{
                        temp++;
                    }
                }
            }

            //WARTEN BIS IM GUI WEITERGEKLICKT WIRD
            if(!isGameOver){
                while(playerWon){
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            // Check if any player has no chips left and end the game if true
            if (poker.players.stream().anyMatch(player -> player.getChips().getAmount() <= 0)) {
                System.out.println("Spiel Ende. Ein Spieler hat keine Chips mehr.");
                //gui.fadingLabel.setText("Spiel Ende. Ein Spieler hat keine Chips mehr.", false);
                isGameOver = true;
                MainGUI.playSound("KSHMR_Hans_Zimmer_Horn_05__Cm_");
            }
        }
    }

    public Deck getDeck(){
        return deck;
    }

}