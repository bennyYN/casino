package de.ben.blackjack;

import de.ben.MainGUI;

public class CardAnimation extends Thread {

    Card card;
    Vector2D startPosition, targetPosition, currentPosition, velocity;
    private final double speed = 1.0; // Adjust the speed as needed
    private int altenator = 0;
    private static boolean soundAlternator = false;

    public CardAnimation(Card card) {
        super();
        this.card = card;
    }

    public void run() {
        double totalDistance = startPosition.distanceTo(targetPosition);
        double thresholdDistance = speed * 0.5; // Distance to print "JETZT" before ending

        while (currentPosition.distanceTo(targetPosition) > 0.5) {
            currentPosition = currentPosition.add(velocity);

            card.xPosition = (int) currentPosition.x;
            card.yPosition = (int) currentPosition.y;

            // Calculate the alpha value based on the distance traveled using a quartic curve
            double currentDistance = currentPosition.distanceTo(startPosition);
            double progress = currentDistance / totalDistance;
            card.alpha = (float) Math.max(0.0, Math.min(1.0, Math.pow(progress, 5))); //exponentialfunktion für alpha

            if (currentPosition.distanceTo(targetPosition) <= thresholdDistance) {
                if(soundAlternator){
                    // Play sound
                    MainGUI.playSound("toggle1");
                }else{
                    // Play sound
                    MainGUI.playSound("toggle2");
                }
                soundAlternator = !soundAlternator;
            }

            if (altenator >= 3) {
                try {
                    Thread.sleep(1); // Adjust the sleep time as needed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                altenator = 0;
            } else {
                altenator++;
            }
        }
        // Ensure the card ends exactly at the target position
        card.xPosition = (int) targetPosition.x;
        card.yPosition = (int) targetPosition.y;
        // Mark the card as entered
        card.entered = true;
    }

    public void startEntranceAnimation(int xTargetPosition, int yTargetPosition, int xStartPosition, int yStartPosition) {
        this.startPosition = new Vector2D(xStartPosition, yStartPosition);
        this.targetPosition = new Vector2D(xTargetPosition, yTargetPosition);
        this.currentPosition = new Vector2D(xStartPosition, yStartPosition);

        // Calculate the direction vector
        Vector2D direction = targetPosition.subtract(startPosition).normalize();
        this.velocity = direction.multiply(speed);

        this.start(); // Start the thread
    }
}
class Vector2D {
    double x, y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D add(Vector2D other) {
        return new Vector2D(this.x + other.x, this.y + other.y);
    }

    public Vector2D subtract(Vector2D other) {
        return new Vector2D(this.x - other.x, this.y - other.y);
    }

    public Vector2D multiply(double scalar) {
        return new Vector2D(this.x * scalar, this.y * scalar);
    }

    public Vector2D normalize() {
        double length = Math.sqrt(this.x * this.x + this.y * this.y);
        return new Vector2D(this.x / length, this.y / length);
    }

    public double distanceTo(Vector2D other) {
        return Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
    }
}