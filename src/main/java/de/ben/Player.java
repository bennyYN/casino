package de.ben;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Player {
    private final List<Card> hand;
    private final Chips chips;
    private boolean folded;
    private final String name;
    private boolean allIn = false;
    private int currentBet;
    public boolean handVisible = false;
    boolean dummy = false;
    static boolean isGameDecided;
    static PokerGUI gui;

    public Player(int initialChips, String name) {
        this.hand = new ArrayList<>();
        this.chips = new Chips(initialChips);
        this.folded = false;
        this.name = name;
    }

    public void receiveCard(Card card1, Card card2) {
        if(hand.size() >= 2){
            hand.set(0, card1);
            hand.set(1, card2);
        }else{
            hand.add(card1);
            hand.add(card2);
        }
    }

    public List<Card> getHand() {
        return hand;
    }

    public Chips getChips() {
        return chips;
    }

    //Methode um die Hand des Spielers zu zeichnen
    public void renderCards(Graphics g){
        if(gui != null){
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

            if(handVisible || isGameDecided){
                if(hand.size()>0){
                    g2d.drawImage(hand.get(0).getImage(), gui.scaleX(481), gui.scaleY((int)(521-Math.pow(gui.getScaleY(), 15))), null);
                }
                if(hand.size()>1){
                    g2d.drawImage(hand.get(1).getImage(), gui.scaleX(605), gui.scaleY((int)(521-Math.pow(gui.getScaleY(), 15))), null);
                }
            }else{
                g2d.drawImage(ImageArchive.getImage("card:back:"+gui.mainGUI.getSelectedTheme()), gui.scaleX(481), gui.scaleY((int)(521-Math.pow(gui.getScaleY(), 15))), null);
                g2d.drawImage(ImageArchive.getImage("card:back:"+gui.mainGUI.getSelectedTheme()), gui.scaleX(605), gui.scaleY((int)(521-Math.pow(gui.getScaleY(), 15))), null);
            }
        }

    }

    public int bet(int amount) {
        if (amount == chips.getAmount()+getCurrentBet()) {
            return goAllIn();
        }
        while (chips.getAmount() < amount) {
            System.out.println("Du hast nicht genug Chips. Du hast nur " + chips.getAmount() + " Chips. Bitte gib einen gültigen Betrag ein.");
            Scanner scanner = new Scanner(System.in);
            amount = scanner.nextInt();
        }

        currentBet += amount;
        chips.removeChips(amount);
        return chips.getAmount();
    }

    public int goAllIn() {
        int allInAmount = chips.getAmount();
        currentBet += allInAmount;
        chips.removeChips(allInAmount);
        allIn = true;
        return 0;
    }

    public void setFolded(boolean folded) {
        this.folded = folded;
    }
    public boolean isFolded() {
        return folded;
    }
    public String getName() {
        return name;
    }
    public boolean isAllIn() {
        return allIn;
    }
    public int getCurrentBet() {
        return currentBet;
    }
    public void setAllIn(boolean b) {
        allIn = b;
    }
    public void resetCurrentBet() {
        currentBet = 0;
    }

    public void call(int highestBet) {
    }


    public void clearHand() {
        this.hand.clear();
    }


    public boolean check() {
        return false;
    }

    public void reset() {
    }
}