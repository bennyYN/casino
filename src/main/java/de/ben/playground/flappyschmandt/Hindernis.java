package de.ben.playground.flappyschmandt;

import javax.swing.*;
import java.awt.*;

public class Hindernis {
    int x, y, width, height;
    boolean isPassed = false;
    boolean isScored = false;
    Image texture;

    public Hindernis(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        //random obstacle image from 1-4
        int random = (int) (Math.random() * 4 + 1);
        texture = new ImageIcon("img/playground/flappyschmandt/obstacle" + random + ".png").getImage();
        texture = texture.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public void update(int speed) {
        x -= speed;
    }

    public void render(Graphics g) {
        g.setColor(Color.GREEN);
        g.drawImage(texture, x, y, width, height, null);
    }

    public boolean intersects(Rectangle rect) {
        Rectangle obstacleRect = new Rectangle(x, y, width, height);
        return obstacleRect.intersects(rect);
    }

    public Rectangle getBox() {
        return new Rectangle(x, y, width, height);
    }
}