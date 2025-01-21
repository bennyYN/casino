package de.ben.poker;

import javax.swing.*;
import java.awt.*;

public class FadingLabel extends JLabel {

    private float alpha = 1.0f; // Start opacity (100%)
    private boolean frozen;

    public FadingLabel(String text) {
        super(text);
    }

    @Override
    public void setText(String text) {
        frozen = false;
        super.setText(text);
        alpha = 1.0f; // Reset opacity to 100%
        new Thread(new Fader()).start(); // Start the fading effect in a new thread
    }

    public void killText(){
        alpha = 0f;
    }

    public void setText(String text, boolean fadingOut) {
        super.setText(text);
        frozen = false;
        alpha = 1.0f; // Reset opacity to 100%
        if(fadingOut){
            new Thread(new Fader()).start(); // Start the fading effect in a new thread
        }else{
            frozen = true;
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        super.paintComponent(g2d);
        g2d.dispose();
    }

    private class Fader implements Runnable {
        @Override
        public void run() {
            try {
                while (alpha > 0 && !frozen) {
                    alpha -= 0.01f; // Decrease opacity
                    if (alpha < 0) {
                        alpha = 0;
                    }
                    repaint(); // Repaint the label with updated opacity
                    Thread.sleep(40); // Wait for 40 milliseconds
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}