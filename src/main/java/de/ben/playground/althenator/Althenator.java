package de.ben.playground.althenator;

import java.awt.*;

public class Althenator {

    private static final Image ACTIVE_ALTHENATOR = Toolkit.getDefaultToolkit().getImage(Althenator.class.getClassLoader().getResource("img/playground/althenator/active_althenator.png"));
    private static final Image INACTIVE_ALTHENATOR = Toolkit.getDefaultToolkit().getImage(Althenator.class.getClassLoader().getResource("img/playground/althenator/inactive_althenator.png"));
    private static final Image ELECTRIFIED_ALTHENATOR = Toolkit.getDefaultToolkit().getImage(Althenator.class.getClassLoader().getResource("img/playground/althenator/electrified_althenator.png"));
    private static final Image DEAD_ALTHENATOR = Toolkit.getDefaultToolkit().getImage(Althenator.class.getClassLoader().getResource("img/playground/althenator/dead_althenator.png"));
    public static final int WIDTH = ACTIVE_ALTHENATOR.getWidth(null), HEIGHT = ACTIVE_ALTHENATOR.getHeight(null);

    private Image currentImage;
    private int x, y, health = 1, score = 0;
    private AlthenatorGUI gui;

    public Althenator(int x, int y, AlthenatorGUI gui) {
        this.gui = gui;
        currentImage = INACTIVE_ALTHENATOR;
        this.x = x;
        this.y = y;
    }

    public void render(Graphics g) {
        if(health <= 0){
            currentImage = DEAD_ALTHENATOR;
            gui.gameover = true;
        }
        g.drawImage(currentImage, x, y, null);
    }

    public void triggerElectrification() {
        if(isAlive()){
            currentImage = ELECTRIFIED_ALTHENATOR;
            new Thread(() -> {
                try {
                    Thread.sleep(350);
                    health--;
                    if(health <= 0){
                        currentImage = DEAD_ALTHENATOR;
                    }else{
                        currentImage = INACTIVE_ALTHENATOR;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public void triggerAction() {
        gui.infoPanel.repaint();
        if(isAlive()){
            currentImage = ACTIVE_ALTHENATOR;
            score++;
            new Thread(() -> {
                try {
                    Thread.sleep(200);
                    currentImage = INACTIVE_ALTHENATOR;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public int getHealth() {
        return health;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void setPosition(int x, int y) {
        if(isAlive()){
            this.x = x;
            this.y = y;
        }

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getScore() {
        return score;
    }

}
