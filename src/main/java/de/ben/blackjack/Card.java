package de.ben.blackjack;

public class Card {

    CardAnimation cardAnimation;

    private int rank;
    private Suit suit;
    public int xPosition = -1000, yPosition = -1000;
    float alpha = 0;
    boolean entered = false;
    boolean entering = false;
    boolean flipped = false, flipping = false;

    public Card(int rank, Suit suit) {
        cardAnimation = new CardAnimation(this);
        this.rank = rank;
        this.suit = suit;
    }

    public void triggerEnteranceAnimation(int xTargetPosition, int yTargetPosition, int xTravel, int yTravel) {
        if (!entered && !entering) {
            cardAnimation.startEntranceAnimation(xTargetPosition, yTargetPosition, xTravel, yTravel);
            entering = true;
        }
    }

    public int getValue() {
        if (rank > 10) {
            return 10;
        } else if (rank == 1) {
            return 11;
        } else {
            return rank;
        }
    }

    public void setEntered(boolean entered) {
        this.entered = entered;
    }

    public int getVisibleValue(){
        if(entered){
            if (rank > 10) {
                return 10;
            } else if (rank == 1) {
                return 11;
            } else {
                return rank;
            }
        }else{
            return 0;
        }
    }

    @Override
    public String toString() {
        String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        return suit + " " + ranks[rank - 1];
    }
}