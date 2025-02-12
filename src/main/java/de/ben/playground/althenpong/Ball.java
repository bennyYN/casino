package de.ben.playground.althenpong;

import java.awt.*;

public class Ball extends Rectangle {

    private Image ballIcon = Toolkit.getDefaultToolkit().getImage("img/playground/althenpong/althenos.png");
    public double speedMultiplier = 4.6, xVelocity = 1, yVelocity = 1, xPos = 1920/2, yPos = 1080/2;
    private int savedX, savedY;

    public Ball() {
        super(1920/2, 1080/2, 60, 60);
    }

    public void move() {
        xPos += (int) (xVelocity * speedMultiplier);
        yPos += (int) (yVelocity * speedMultiplier);
        x = (int) xPos;
        y = (int) yPos;
        speedMultiplier = speedMultiplier + 0.0021;
    }

    public void render(Graphics g) {
        g.drawImage(ballIcon, x-11, y-13, width+22, height+26, null);
    }

    public void saveCurrentPosition(){
        savedX = x;
        savedY = y;
    }

    public int getSavedX(){
        return savedX;
    }

    public int getSavedY(){
        return savedY;
    }

}
