package de.ben;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class MainWindow extends JFrame {
    public MainWindow() {
        setTitle("Casino - Hauptmenü");
        setSize(600, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            URL iconUrl = new URL("https://bennyyn.xyz/upload/images/icon.png");
            BufferedImage iconImage = ImageIO.read(iconUrl);
            setIconImage(iconImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Hintergrundbild setzen
        try {
            URL imageUrl = new URL("https://bennyyn.xyz/upload/images/background.png");
            BufferedImage backgroundImage = ImageIO.read(imageUrl);
            setContentPane(new JLabel(new ImageIcon(backgroundImage)));
        } catch (IOException e) {
            e.printStackTrace();
            getContentPane().setBackground(Color.BLACK);
        }

        // Layout des Hauptfensters setzen
        setLayout(new BorderLayout());

        // Panel für den Hauptinhalt erstellen
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false); // Hintergrund des Panels transparent machen

        // Großer Text in der Mitte des Menüs
        JLabel bigTextLabel = new JLabel("Willkommen im Casino");
        bigTextLabel.setFont(new Font("Arial", Font.BOLD, 30));
        bigTextLabel.setForeground(Color.WHITE);
        bigTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(bigTextLabel, BorderLayout.CENTER);

        // Buttons am unteren Rand
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false); // Hintergrund des Panels transparent machen

        JButton slotsButton = new JButton("Slots");
        slotsButton.setFont(new Font("Arial", Font.BOLD, 14));
        slotsButton.setForeground(Color.YELLOW);
        slotsButton.setBackground(Color.DARK_GRAY);
        slotsButton.setFocusable(false);

        JButton blackjackButton = new JButton("Blackjack");
        blackjackButton.setFont(new Font("Arial", Font.BOLD, 14));
        blackjackButton.setForeground(Color.YELLOW);
        blackjackButton.setBackground(Color.DARK_GRAY);
        blackjackButton.setFocusable(false);

        JButton settingsButton = new JButton("Einstellungen");
        settingsButton.setFont(new Font("Arial", Font.BOLD, 14));
        settingsButton.setForeground(Color.YELLOW);
        settingsButton.setBackground(Color.DARK_GRAY);
        settingsButton.setFocusable(false);

        buttonPanel.add(slotsButton);
        buttonPanel.add(blackjackButton);
        buttonPanel.add(settingsButton);

        // Text "by Alex & Ben" am unteren linken Rand
        JLabel bottomLeftTextLabel = new JLabel("by Alex & Ben");
        bottomLeftTextLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        bottomLeftTextLabel.setForeground(Color.WHITE);
        bottomLeftTextLabel.setHorizontalAlignment(SwingConstants.LEFT);
        JPanel bottomLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomLeftPanel.setOpaque(false);
        bottomLeftPanel.add(bottomLeftTextLabel);
        contentPanel.add(bottomLeftPanel, BorderLayout.NORTH);

        // Hauptinhalt und Buttons zum Hauptfenster hinzufügen
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        new MainWindow();
    }
}
