package de.ben;

import javax.swing.*;
import java.awt.*;

public class ImageWindow extends JFrame {

    public ImageWindow() {
        setTitle("Hand Ranking");
        // Berechne die neue skalierte Größe
        int originalWidth = 800;
        int originalHeight = 1291;
        int scaledWidth = (int) (originalWidth * 0.7);
        int scaledHeight = (int) (originalHeight * 0.7);
        setSize(scaledWidth, scaledHeight);
        setResizable(false); // Größe des Fensters festsetzen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Hintergrundbild auf JPanel zeichnen
        JPanel imagePanel = new JPanel() {
            private Image scaledImage;

            {
                // Bild laden und skalieren
                ImageIcon imageIcon = new ImageIcon("img/handranking.png");
                Image image = imageIcon.getImage();
                // Skaliere das Bild auf die neue Größe
                scaledImage = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(scaledImage, 0, 0, this);
            }
        };

        add(imagePanel);
    }

    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ImageWindow().setVisible(true);
            }
        });
    }*/
}