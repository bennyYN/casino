package de.ben;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainGUI extends JFrame implements ActionListener {

    // ATTRIBUTE
    Poker pokerGame;
    JButton startButton;
    JButton settingsButton;
    JButton exitButton;
    JPanel panel;

    // KONSTRUKTOR
    public MainGUI() {

        this.setTitle("Casino");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        // Panel mit GridBagLayout und Hintergrundfarbe festlegen
        panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(0, 51, 0)); // Dunkelgrün
        this.add(panel);

        // GridBagConstraints für die Zentrierung der Buttons
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 0, 10, 0); // Abstand zwischen den Buttons
        gbc.anchor = GridBagConstraints.CENTER;

        // Start Button
        startButton = new JButton("Start");
        styleButton(startButton);
        panel.add(startButton, gbc);

        // Settings Button
        settingsButton = new JButton("Settings");
        styleButton(settingsButton);
        panel.add(settingsButton, gbc);

        // Exit Button
        exitButton = new JButton("Exit");
        styleButton(exitButton);
        panel.add(exitButton, gbc);

        this.setVisible(true);
    }

    private void styleButton(JButton button) {
        Color normalColor = new Color(0, 100, 0); // Dark green
        Color pressedColor = new Color(0, 200, 0); // Lighter green
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false); // Disable the focus border
        button.setBackground(normalColor);
        button.setForeground(Color.YELLOW); // Yellow text
        button.setPreferredSize(new Dimension(150, 40)); // Set size
        button.addActionListener(this);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(pressedColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(normalColor);
            }
        });
    }

    public static void main(String[] args) {
        new MainGUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton sourceButton = (JButton) e.getSource();
        if (sourceButton == startButton) {
            System.out.println("Start");
        } else if (sourceButton == settingsButton) {
            this.dispose(); // Close the current window
            new SettingsGUI(); // Open SettingsGUI
        } else if (sourceButton == exitButton) {
            System.exit(0);
        }
    }
}