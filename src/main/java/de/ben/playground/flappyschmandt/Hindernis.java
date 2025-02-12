package de.ben.playground.flappyschmandt;

import java.awt.*;

public class Hindernis {
    int x, y, width, height;
    boolean isPassed = false;
    boolean isScored = false;

    public Hindernis(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void update(int speed) {
        x -= speed;
    }

    public void render(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(x, y, width, height);
    }

    public boolean intersects(Rectangle rect) {
        Rectangle obstacleRect = new Rectangle(x, y, width, height);
        return obstacleRect.intersects(rect);
    }

    public Rectangle getBox() {
        return new Rectangle(x, y, width, height);
    }
}