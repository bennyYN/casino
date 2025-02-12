package de.ben.playground.althenpong;

import java.awt.*;

public class Player extends Rectangle {

    public double yOffset = 0, yVelocity = 0;
    public int score = 0;

    public Player(int x) {
        super(x, 0, 30, 200);
    }

    public void render(Graphics g) {
        //update y
        yOffset = Math.min(230, Math.max(-320, yOffset + yVelocity));
        y = 880/2-100 + (int) yOffset;
        //painting
        g.setColor(new Color(255, 255, 255));
        g.fillRect(x, y, width, height);
    }

}
