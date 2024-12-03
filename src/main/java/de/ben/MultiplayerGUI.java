package de.ben;

import javax.swing.*;
import java.awt.*;

public class MultiplayerGUI extends JFrame {

    private MainGUI mainGUI;

    public MultiplayerGUI(MainGUI mainGUI, boolean usedInMainGUI) {
        this.mainGUI = mainGUI;
        this.setTitle("Multiplayer");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        // Panel mit GridBagLayout zum Zentrieren der Komponenten
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false); // Panel transparent halten, um Hintergrundbild anzuzeigen
        this.add(panel);

        // Erstelle "Spiel starten" Button
        JButton startGameButton = new JButton("Spiel starten");
        styleButton(startGameButton);
        startGameButton.addActionListener(e -> {
        });

        // Erstelle "Spiel beitreten" Button
        JButton joinGameButton = new JButton("Spiel beitreten");
        styleButton(joinGameButton);
        joinGameButton.addActionListener(e -> {

        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 100, 10, 100);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridy = 0;
        panel.add(startGameButton, gbc);

        gbc.gridy = 1;
        panel.add(joinGameButton, gbc);

        this.setVisible(true);
    }

    // Methode zum Stylen des Buttons
    private void styleButton(JButton button) {
        button.setBackground(new Color(78, 136, 174, 255));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(150, 40));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(e -> {
            mainGUI.playSound("click");
        });
    }
}