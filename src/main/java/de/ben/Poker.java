package de.ben;

import java.util.Scanner;

public class Poker {
    Scanner sc = new Scanner(System.in);
    private Player player;
    private Player player2;
    private Dealer dealer;

    public Poker(int initialChips) {
        this.player = new Player(initialChips);
        this.player2 = new Player(initialChips);
        this.dealer = new Dealer();
    }

    public void dealInitialCards() {
        for (int i = 0; i < 2; i++) {
            player.receiveCard(dealer.drawCard());
            player2.receiveCard(dealer.drawCard());
        }
        for (int i = 0; i < 3; i++) {
            dealer.receiveCard(dealer.drawCard());
        }
        printCards();
    }
    public void roundOfBetting() {
        System.out.println("Player 1:Wie viel willst du setzten:");
        int playerbet1 = sc.nextInt();
        player.bet(playerbet1);

        if(playerbet1==0){
            System.out.println("Du hast gepasst");
        }
        else{
            System.out.println("Du hast "+playerbet1+" gesetzt");
        }

        System.out.println("Player 2: Wie viel willst du setzen:");
        playerbet1 = sc.nextInt();
        player2.bet(playerbet1);


        if(playerbet1==0){
            System.out.println("Du hast gepasst");
        }
        else{
            System.out.println("Du hast "+playerbet1+" gesetzt");
        }

    }
    public void printCards() {
        System.out.println("Karten des Spielers 1: " + player.getHand());
        System.out.println("Karten des Spielers 2: " + player2.getHand());
    }
    public void printDealerCards(){
        System.out.println("Karten des Dealers: " + dealer.getHand());
    }

    public void dealerextracard(){
        dealer.receiveCard(dealer.drawCard());
        System.out.println("Karten des Dealers: " + dealer.getHand());
    }

    public static void main(String[] args) {
        Poker poker = new Poker(5000);
        poker.dealInitialCards();
        poker.roundOfBetting();
        poker.printDealerCards();
        poker.roundOfBetting();
        poker.dealerextracard();
        poker.roundOfBetting();
        poker.dealerextracard();
        poker.roundOfBetting();
    }
}