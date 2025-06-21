package de.ben.playground.althenator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Sicherung extends JButton {

    private static final Image DEFAULT_SICHERUNG = Toolkit.getDefaultToolkit().getImage("src/main/resources/img/playground/althenator/sicherung.png");
    private static final Image RAUSGEFLOGENE_SICHERUNG = Toolkit.getDefaultToolkit().getImage("src/main/resources/img/playground/althenator/rausgeflogene_sicherung.png");
    private static final int WIDTH = 150, HEIGHT = 264;

    private boolean rausgeflogen = false;
    private int gridX, gridY;
    private Althenator althenator;

    public Sicherung(int gridX, int gridY, Althenator althenator) {
        this.althenator = althenator;
        this.gridX = gridX;
        this.gridY = gridY;
        setBounds(gridX * WIDTH, gridY * HEIGHT, WIDTH, HEIGHT);
        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setFocusable(false);
        setVisible(true);
        setIcon(new ImageIcon(DEFAULT_SICHERUNG));

        //10% Chance dass die Sicherung von anfang an draussen ist
        if(Math.random() < 0.1){
            rausgeflogen = true;
        }

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleRausgeflogen();
            }
        });
    }

    private void toggleRausgeflogen() {
        if(rausgeflogen) {
            rausgeflogen = false;
            althenator.triggerAction();
        }else{
            rausgeflogen = true;
            althenator.triggerElectrification();
        }
        setIcon(new ImageIcon(rausgeflogen ? RAUSGEFLOGENE_SICHERUNG : DEFAULT_SICHERUNG));
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Image image = rausgeflogen ? RAUSGEFLOGENE_SICHERUNG : DEFAULT_SICHERUNG;
        g.drawImage(image, 0, 0, WIDTH, HEIGHT, this);
    }

    public boolean isRausgeflogen() {
        return rausgeflogen;
    }

    public void setRausgeflogen(boolean rausgeflogen) {
        this.rausgeflogen = rausgeflogen;
        setIcon(new ImageIcon(rausgeflogen ? RAUSGEFLOGENE_SICHERUNG : DEFAULT_SICHERUNG));
        this.repaint();
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                this.rausgeflogen = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}