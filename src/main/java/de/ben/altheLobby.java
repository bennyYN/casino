/*package de.ben;

import javax.swing.*;
import java.awt.*;

public class altheLobby extends JFrame{

    MainGUI mainGUI;
    private int playerCount;

    public altheLobby(boolean leader, MainGUI mainGUI, int startChips, int bigBlind) {

        this.mainGUI = mainGUI;
        System.out.println("Lobby created");

        setTitle("Poker Lobby");
        setSize(1200, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //Titlebar-Icon mit Skalierung setzen
        ImageIcon icon = new ImageIcon("img/icon.png");
        Image scaledIcon = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH); // glatte Skalierung
        setIconImage(scaledIcon);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                //Hintergrundbild zeichnen
                g.drawImage(ImageArchive.getImage("background:"+mainGUI.getSelectedTheme()), 0, 0, null);

            }
        };

        add(panel);
        setVisible(true);
    }



}*/
