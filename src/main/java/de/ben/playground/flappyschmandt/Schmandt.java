package de.ben.playground.flappyschmandt;

import javax.swing.*;
import java.awt.*;

public class Schmandt {

    public double jumpHeight = 5, velocity = 0, gravity = 0.25;
    public boolean isAlive = true;
    public static int score = 0;
    public int cooldown = 0;
    public Rectangle hitbox1, hitbox2, scoreBox;
    private Image player = new ImageIcon("img/playground/flappyschmandt/player.png").getImage();
    public int x, y, width, height;

    public Schmandt(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        hitbox1 = new Rectangle(x+(int)(width/2), y, (int)(width/2), height);
        hitbox2 = new Rectangle(x, y+(int)(height/2), width, (int)(height/2));
        scoreBox = new Rectangle(x - 20, 0, 20, 1080); // Thicker and behind the player
    }

    public void update() {
        hitbox1 = new Rectangle(x+(int)(width/2), y, (int)(width/2), height);
        hitbox2 = new Rectangle(x, y+(int)(height/2), width, (int)(height/2));
        scoreBox = new Rectangle(x - 20, 0, 20, 1080); // Update position of scoreBox
        if (cooldown > 0) cooldown--;
        velocity += gravity;
        y += velocity;
        if (y > 990 - height) {
            y = 990 - height;
            isAlive = false;
        }
    }

    public void jump() {
        if (cooldown == 0) {
            velocity = -jumpHeight;
            cooldown = 10;
        }
    }

    public void render(Graphics g) {
        g.drawImage(player, x, y, width, height+5, null);
        // Draw hitboxes
        /*g.setColor(Color.RED);
        g.drawRect(hitbox1.x, hitbox1.y, hitbox1.width, hitbox1.height);
        g.drawRect(hitbox2.x, hitbox2.y, hitbox2.width, hitbox2.height);
        // Draw scoreBox (for debugging purposes, can be removed later)
        g.setColor(Color.BLUE);
        g.drawRect(scoreBox.x, scoreBox.y, scoreBox.width, scoreBox.height);*/
    }

    public boolean intersects(Rectangle r) {
        return hitbox1.intersects(r) || hitbox2.intersects(r);
    }

    public boolean intersectsScoreBox(Rectangle r) {
        return scoreBox.intersects(r);
    }
}