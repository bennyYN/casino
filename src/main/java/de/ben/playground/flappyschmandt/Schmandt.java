package de.ben.playground.flappyschmandt;

import javax.swing.*;
import java.awt.*;

public class Schmandt {

    public double jumpHeight = 10, velocity = 0, gravity = 0.85;
    public boolean isAlive = true;
    public static int score = 0;
    public int cooldown = 0;
    public Rectangle hitbox1, hitbox2, scoreBox;
    private Image player1 = new ImageIcon("img/playground/flappyschmandt/player1.png").getImage();
    private Image player2 = new ImageIcon("img/playground/flappyschmandt/player2.png").getImage();
    private Image currentPlayerTexture = player1;
    public int x, y, width, height;
    int switches = 0;

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
            cooldown = 3;
            triggerJumpAnimation();
        }
    }

    private void triggerJumpAnimation() {
        //neue synchroner thread zum kurzen flackern der textur
        new Thread(() -> {

            while(switches < 3) {
                currentPlayerTexture = player2;
                try {
                    Thread.sleep(75);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                currentPlayerTexture = player1;
                try {
                    Thread.sleep(75);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                switches++;
            }
            switches = 0;
        }).start();
        if (currentPlayerTexture == player1) {
            currentPlayerTexture = player2;
        } else {
            currentPlayerTexture = player1;
        }
    }

    public void render(Graphics g) {
        g.drawImage(currentPlayerTexture, x, y, width, height+5, null);
    }

    public boolean intersects(Rectangle r) {
        return hitbox1.intersects(r) || hitbox2.intersects(r);
    }

    public boolean intersectsScoreBox(Rectangle r) {
        return scoreBox.intersects(r);
    }
}