package de.ben.blackjack;

import de.ben.MainGUI;

import java.awt.*;
import java.util.Scanner;

public class BlackJack {

    public String leftHandWinner = "void", rightHandWinner = "void";
    int chips;
    Scanner scanner;
    Deck deck;
    MainGUI mainGUI;
    Graphics gobj;
    boolean betConfirmed = false;
    int bet = 10;
    BlackJackGUI gui;
    boolean canSplit;
    Hand playerHand;
    Hand dealerHand;
    Hand splitHand1;
    Hand splitHand2;
    String winner = "void";
    boolean dealerCardIsHidden = true;
    boolean playerTurn;
    boolean hasSplit = false, isSplitting = false;
    boolean selectedLeftSplitHand = true;
    public int bet1 = 0, bet2 = 0;
    public int betMultiplier = 1, betMultiplier1 = 1, betMultiplier2 = 1;
    boolean canDoubleDown;

    public BlackJack(MainGUI mainGUI, int startChips, BlackJackGUI gui) {
        scanner = new Scanner(System.in);
        this.gui = gui;
        deck = new Deck(6);
        deck.shuffle();
        chips = startChips;
        this.mainGUI = mainGUI;
    }

    public void startGame() {
        betConfirmed = false;
        while (chips >= 10) {
            System.out.println("You have " + chips + " chips.");
            boolean gotBet = false;
            while (!gotBet) {
                if (betConfirmed || !gui.state.equals("betting")) {
                    betConfirmed = false;
                    gotBet = true;
                    System.out.println("You bet " + bet + " chips.");
                } else {
                    System.out.println("waiting for bet input");
                }
            }
            System.out.println("games moves on ahhahahahhhhhhahahaa");

            playerHand = new Hand();
            dealerHand = new Hand();

            playerHand.addCard(deck.drawCard());
            playerHand.addCard(deck.drawCard());
            dealerHand.addCard(deck.drawCard());
            dealerHand.addCard(deck.drawCard());
            playerHand.cards.get(0).setEntered(true);
            playerHand.cards.get(1).setEntered(true);
            dealerHand.cards.get(0).setEntered(true);
            dealerHand.cards.get(1).setEntered(true);

            playerTurn = true;
            canSplit = playerHand.cards.size() == 2 && playerHand.cards.get(0).getValue() == playerHand.cards.get(1).getValue();
            canDoubleDown = playerHand.cards.size() == 2;

            //reset winners
            winner = "void";
            leftHandWinner = "void";
            rightHandWinner = "void";

            while (playerTurn) {

                betMultiplier = 1;
                betMultiplier1 = 1;
                betMultiplier2 = 1;

                System.out.println("Your hand: " + playerHand);
                System.out.println("Dealer's hand: " + dealerHand.getFirstCard() + " [hidden]");

                if (playerHand.cards.size() == 2 && playerHand.getValue() == 21) {
                    System.out.println("Blackjack! You win with a 1.5x payout!");
                    winner = "blackjack";
                    chips += bet * 1.5;
                    playerTurn = false;
                } else {
                    System.out.print("Do you want to (h)it, (s)tand");
                    if (canSplit) {
                        System.out.print(", s(p)lit");
                    }
                    if (canDoubleDown) {
                        System.out.print(", (d)ouble down");
                    }
                    System.out.print("? ");
                    canDoubleDown = playerHand.cards.size() == 2;
                    while (gui.getAction().equals("idle")) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    String action = gui.getAction();
                    System.out.println("action selected: " + gui.getAction());
                    gui.setAction("idle");

                    if (action.equalsIgnoreCase("h")) {
                        playerHand.addCard(deck.drawCard());
                        if (playerHand.getValue() > 21) {
                            System.out.println("Your hand: " + playerHand);
                            boolean allPlayerCardsEntered = false;
                            while(!allPlayerCardsEntered){
                                int enteredPlayerCards = 0;
                                for(Card card : playerHand.cards){
                                    if(card.entered){
                                        enteredPlayerCards++;
                                    }
                                }
                                if(enteredPlayerCards == playerHand.cards.size()){
                                    allPlayerCardsEntered = true;
                                }
                                try {
                                    Thread.sleep(2);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            playerTurn = false;
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            dealerHand.cards.get(1).flipping = true;
                            while(dealerHand.cards.get(1).flipping || !dealerHand.cards.get(1).flipped){
                                try {
                                    Thread.sleep(2);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            dealerCardIsHidden = false;
                            System.out.println("You bust! Dealer wins.");
                            winner = "bust";
                            chips -= bet;
                        }

                    } else if (action.equalsIgnoreCase("s")) {
                        playerTurn = false;
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        dealerHand.cards.get(1).flipping = true;
                        while(dealerHand.cards.get(1).flipping || !dealerHand.cards.get(1).flipped){
                            try {
                                Thread.sleep(2);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        dealerCardIsHidden = false;
                    } else if (canSplit && action.equalsIgnoreCase("p")) {
                        splitHand1 = new Hand();
                        splitHand2 = new Hand();
                        splitHand1.addCard(playerHand.cards.get(0));
                        splitHand2.addCard(playerHand.cards.get(1));
                        isSplitting = true;
                        while(!gui.seperatedBevels){
                            try {
                                Thread.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        isSplitting = false;
                        splitHand1.addCard(deck.drawCard());
                        splitHand2.addCard(deck.drawCard());
                        splitHand1.cards.get(0).setEntered(true);
                        //splitHand1.cards.get(1).setEntered(true);
                        splitHand2.cards.get(0).setEntered(true);
                        //splitHand2.cards.get(1).setEntered(true);
                        bet1 = bet;
                        bet2 = bet;
                        hasSplit = true;
                        System.out.println("First split hand: " + splitHand1);
                        System.out.println("Second split hand: " + splitHand2);
                        selectedLeftSplitHand = true;
                        handleSplitHand(splitHand1);
                        selectedLeftSplitHand = false;
                        handleSplitHand(splitHand2);
                        playerTurn = false;
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        dealerHand.cards.get(1).flipping = true;
                        while(dealerHand.cards.get(1).flipping || !dealerHand.cards.get(1).flipped){
                            try {
                                Thread.sleep(2);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        dealerCardIsHidden = false;
                    } else if (canDoubleDown && action.equalsIgnoreCase("d")) {
                        bet *= 2;
                        playerHand.addCard(deck.drawCard());
                        if (playerHand.getValue() > 21) {
                            System.out.println("Your hand: " + playerHand);
                            boolean allPlayerCardsEntered = false;
                            while(!allPlayerCardsEntered){
                                int enteredPlayerCards = 0;
                                for(Card card : playerHand.cards){
                                    if(card.entered){
                                        enteredPlayerCards++;
                                    }
                                }
                                if(enteredPlayerCards == playerHand.cards.size()){
                                    allPlayerCardsEntered = true;
                                }
                                try {
                                    Thread.sleep(2);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            playerTurn = false;
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            dealerHand.cards.get(1).flipping = true;
                            while(dealerHand.cards.get(1).flipping || !dealerHand.cards.get(1).flipped){
                                try {
                                    Thread.sleep(2);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            dealerCardIsHidden = false;
                            System.out.println("You bust! Dealer wins.");
                            winner = "bust";
                            chips -= bet;
                        }else{
                            playerTurn = false;
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            dealerHand.cards.get(1).flipping = true;
                            while(dealerHand.cards.get(1).flipping || !dealerHand.cards.get(1).flipped){
                                try {
                                    Thread.sleep(2);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            dealerCardIsHidden = false;
                        }

                    }
                }
                boolean allPlayerCardsEntered = false;
                while(!allPlayerCardsEntered){
                    int enteredPlayerCards = 0;
                    for(Card card : playerHand.cards){
                        if(card.entered){
                            enteredPlayerCards++;
                        }
                    }
                    if(enteredPlayerCards == playerHand.cards.size()){
                        allPlayerCardsEntered = true;
                    }
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (playerHand.getValue() == 21 && playerHand.cards.size() == 2) {
                //Skip dealer's turn if player has a de.ben.blackjack.blackjack
                continue;
            }

            if (playerHand.getValue() <= 21) {
                dealerCardIsHidden = false;
                System.out.println("Dealer's hand: " + dealerHand);
                while (dealerHand.getValue() < 17) {
                    try {
                        Thread.sleep(1100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    dealerHand.addCard(deck.drawCard());
                    System.out.println("Dealer's hand: " + dealerHand);
                }

                if(!hasSplit){
                    boolean allPlayerCardsEntered = false;
                    while(!allPlayerCardsEntered){
                        int enteredPlayerCards = 0;
                        for(Card card : playerHand.cards){
                            if(card.entered){
                                enteredPlayerCards++;
                            }
                        }
                        if(enteredPlayerCards == playerHand.cards.size()){
                            allPlayerCardsEntered = true;
                        }
                        try {
                            Thread.sleep(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (dealerHand.getValue() > 21) {
                        System.out.println("Dealer busts! You win.");
                        winner = "player";
                        chips += bet;
                    } else if (dealerHand.getValue() > playerHand.getValue()) {
                        System.out.println("Dealer wins.");
                        winner = "dealer";
                        chips -= bet;
                    } else if (dealerHand.getValue() < playerHand.getValue()) {
                        System.out.println("You win!");
                        winner = "player";
                        chips += bet;
                    } else {
                        System.out.println("It's a tie!");
                        winner = "tie";
                    }
                }else{
                    //Auswertung, wenn der Spieler gesplittet hat
                    //1. Hand
                    if(leftHandWinner.equals("void")){
                        if (dealerHand.getValue() > 21) {
                            System.out.println("Dealer busts! You win.");
                            leftHandWinner = "player";
                            chips += bet1;
                        } else if (dealerHand.getValue() > splitHand1.getValue()) {
                            System.out.println("Dealer wins.");
                            leftHandWinner = "dealer";
                            chips -= bet1;
                        } else if (dealerHand.getValue() < splitHand1.getValue()) {
                            System.out.println("You win!");
                            leftHandWinner = "player";
                            chips += bet1;
                        } else{
                            leftHandWinner = "tie";
                        }
                    }

                    //2. Hand
                    if(rightHandWinner.equals("void")) {
                        if (dealerHand.getValue() > 21) {
                            System.out.println("Dealer busts! You win.");
                            rightHandWinner = "player";
                            chips += bet2;
                        } else if (dealerHand.getValue() > splitHand2.getValue()) {
                            System.out.println("Dealer wins.");
                            rightHandWinner = "dealer";
                            chips -= bet2;
                        } else if (dealerHand.getValue() < splitHand2.getValue()) {
                            System.out.println("You win!");
                            rightHandWinner = "player";
                            chips += bet2;
                        } else {
                            rightHandWinner = "tie";
                        }
                    }
                }
            }
            boolean allDealerCardsEntered = false;
            while(!allDealerCardsEntered){
                int enteredDealerCards = 0;
                for(Card card : dealerHand.cards){
                    if(card.entered){
                        enteredDealerCards++;
                    }
                }
                if(enteredDealerCards == dealerHand.cards.size()){
                    allDealerCardsEntered = true;
                }
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (chips <= 9) {
                System.out.println("You are out of chips! Game over.");
                gui.state = "gameover";
                break;
            }

            System.out.print("Do you want to play another round? (y/n): ");

            gui.state = "inactive";
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            while (gui.getAction().equals("idle") || gui.state.equals("betting")) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String playAgain = gui.getAction();
            gui.setAction("idle");
            while(!gui.getAction().equals("idle")){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (!playAgain.equalsIgnoreCase("c")) {
                break;
            }else{
                dealerCardIsHidden = true;
                hasSplit = false;
            }
            gui.setAction("idle");
        }
        System.out.println("You leave the game with " + chips + " chips.");
    }

    private void handleSplitHand(Hand hand) {
        boolean playerTurn = true;
        canDoubleDown = hand.cards.size() == 2 && chips >= bet;
        while (playerTurn) {
            System.out.println("Your hand: " + hand);
            if (hand.cards.size() == 2 && hand.getValue() == 21) {
                System.out.println("Blackjack! You win with a 1.5x payout!");
                if(selectedLeftSplitHand){
                    leftHandWinner = "blackjack";
                    chips += bet1 * 1.5;
                }else{
                    rightHandWinner = "blackjack";
                    chips += bet2 * 1.5;
                }
                playerTurn = false;
            } else {
                System.out.print("Do you want to (h)it, (s)tand");
                if (canDoubleDown) {
                    System.out.print(", (d)ouble down");
                }
                System.out.print("? ");
                while (gui.getAction().equals("idle")) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                String action = gui.getAction();
                System.out.println("action selected: " + gui.getAction());
                gui.setAction("idle");

                if (action.equalsIgnoreCase("h")) {
                    hand.addCard(deck.drawCard());
                    if (hand.getValue() > 21) {
                        System.out.println("Your hand: " + hand);
                        System.out.println("You bust! Dealer wins.");
                        if(selectedLeftSplitHand){
                            leftHandWinner = "bust";
                            chips -= bet1;
                        }else{
                            rightHandWinner = "bust";
                            chips -= bet2;
                        }
                        playerTurn = false;
                    }
                } else if (action.equalsIgnoreCase("s")) {
                    playerTurn = false;
                } else if (canDoubleDown && action.equalsIgnoreCase("d")) {
                    if(selectedLeftSplitHand){
                        bet1 *= 2;
                    }else{
                        bet2 *= 2;
                    }
                    hand.addCard(deck.drawCard());
                    if (hand.getValue() > 21) {
                        System.out.println("Your hand: " + hand);
                        System.out.println("You bust! Dealer wins.");
                        if(selectedLeftSplitHand){
                            leftHandWinner = "bust";
                        }else{
                            rightHandWinner = "bust";
                        }
                        chips -= bet;
                    }
                    playerTurn = false;
                }
            }
            if(selectedLeftSplitHand){
                boolean allPlayerLeftHandCardsEntered = false;
                while(!allPlayerLeftHandCardsEntered){
                    System.out.println("waiting for left hand cards to be entered");
                    int enteredPlayerLeftHandCards = 0;
                    for(Card card : splitHand1.cards){
                        if(card.entered){
                            enteredPlayerLeftHandCards++;
                        }
                    }
                    if(enteredPlayerLeftHandCards == splitHand1.cards.size()){
                        allPlayerLeftHandCardsEntered = true;
                    }
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                boolean allPlayerRightHandCardsEntered = false;
                while(!allPlayerRightHandCardsEntered){
                    System.out.println("waiting for RIGHT hand cards to be entered");

                    int enteredPlayerRightHandCards = 0;
                    for(Card card : splitHand2.cards){
                        if(card.entered){
                            enteredPlayerRightHandCards++;
                        }
                    }
                    if(enteredPlayerRightHandCards == splitHand2.cards.size()){
                        allPlayerRightHandCardsEntered = true;
                    }
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    public void setGraphics(Graphics g) {
        gobj = g;
    }
}