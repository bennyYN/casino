package de.ben;

import javax.swing.*;
import java.awt.*;

public class PokerGUI extends JFrame {

    // Konstruktor
    public PokerGUI() {
        this.setTitle("Poker Game");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fenster schließen, ohne die gesamte Anwendung zu beenden
        this.setSize(800, 600); // Fenstergröße setzen
        this.setLocationRelativeTo(null); // Fenster zentrieren
        this.setResizable(false); // Größe des Fensters nicht veränderbar

        // Hintergrundfarbe festlegen (optional)
        this.getContentPane().setBackground(new Color(34, 139, 34)); // Forest Green

        this.setVisible(true); // Fenster anzeigen
    }

    public static void main(String[] args) {
        new PokerGUI(); // Öffnet das leere Poker-Fenster
    }
}