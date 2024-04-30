package de.ben;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Slots extends JFrame {
    private JLabel[][] slotLabels;
    private JPanel slotsPanel;

    public Slots() {
        setTitle("Slots");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel für die Slots erstellen
        JPanel contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    URL backgroundImageUrl = new URL("https://bennyyn.xyz/upload/images/background.png");
                    BufferedImage backgroundImage = ImageIO.read(backgroundImageUrl);
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } catch (IOException e) {
                    e.printStackTrace();
                    // Falls das Bild nicht geladen werden kann, fällt es auf ein einfaches Hintergrundfarbe zurück
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        contentPane.setLayout(new BorderLayout());

        slotsPanel = new JPanel(new GridLayout(3, 3));
        slotsPanel.setOpaque(false); // Panel transparent machen, damit der Hintergrund sichtbar ist
        slotLabels = new JLabel[3][3];

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                try {
                    URL imageUrl = new URL("https://bennyyn.xyz/upload/images/slot_image_" + row + "_" + col + ".png");
                    BufferedImage image = ImageIO.read(imageUrl);
                    ImageIcon icon = new ImageIcon(image);
                    slotLabels[row][col] = new JLabel(icon);
                    slotsPanel.add(slotLabels[row][col]);
                } catch (IOException e) {
                    e.printStackTrace();
                    slotLabels[row][col] = new JLabel("Slot " + row + "-" + col);
                    slotsPanel.add(slotLabels[row][col]);
                }
            }
        }

        contentPane.add(slotsPanel, BorderLayout.CENTER);

        JButton spinButton = new JButton("Spin");
        spinButton.setOpaque(false); // Button-Hintergrund transparent machen
        spinButton.setContentAreaFilled(false); // Inhalt des Buttons transparent machen
        spinButton.setBorderPainted(false); // Rand des Buttons ausblenden
        spinButton.setFont(new Font("Arial", Font.BOLD, 14));
        spinButton.setForeground(Color.YELLOW);
        spinButton.setFocusable(false);
        spinButton.setFocusPainted(false);
        spinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shuffleSlots();
            }
        });
        contentPane.add(spinButton, BorderLayout.SOUTH);

        setContentPane(contentPane);

        setVisible(true);
    }

    // Methode zum Mischen der Slot-Bilder
    private void shuffleSlots() {
        List<ImageIcon> images = new ArrayList<>();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                // Sammle alle Bilder
                ImageIcon icon = (ImageIcon) slotLabels[row][col].getIcon();
                images.add(icon);
            }
        }
        // Mische die Bilder
        Collections.shuffle(images);
        // Setze die gemischten Bilder zurück in die Slots
        int index = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                slotLabels[row][col].setIcon(images.get(index++));
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Slots::new);
    }
}
