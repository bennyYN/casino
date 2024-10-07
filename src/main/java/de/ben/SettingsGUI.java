package de.ben;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsGUI extends JFrame {

    public SettingsGUI() {
        this.setTitle("Settings");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        // Panel with GridBagLayout for centering the components
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(0, 51, 0)); // Dark green background
        this.add(panel);

        // Create Volume label
        JLabel volumeLabel = new JLabel("Volume");
        volumeLabel.setForeground(Color.YELLOW); // Yellow text

        // Create music slider
        JSlider musicSlider = new JSlider(0, 100, 50); // Default value set to 50
        musicSlider.setMajorTickSpacing(10);
        musicSlider.setMinorTickSpacing(5);
        musicSlider.setPaintTicks(true);
        musicSlider.setPaintLabels(true);
        musicSlider.setForeground(Color.YELLOW); // Yellow text for labels
        musicSlider.setBackground(new Color(0, 51, 0)); // Dark green background
        musicSlider.setPreferredSize(new Dimension(300, 50)); // Set larger size

        // Create Back button
        JButton backButton = new JButton("Back");
        styleButton(backButton);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close SettingsGUI
                new MainGUI(); // Open MainGUI
            }
        });

        // GridBagConstraints for centering the components
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0); // Padding
        gbc.anchor = GridBagConstraints.CENTER;

        // Add Volume label to the panel
        gbc.gridy = 0;
        panel.add(volumeLabel, gbc);

        // Add music slider to the panel
        gbc.gridy = 1;
        panel.add(musicSlider, gbc);

        // Add Back button to the panel
        gbc.gridy = 2;
        panel.add(backButton, gbc);

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
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                button.setBackground(pressedColor);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                button.setBackground(normalColor);
            }
        });
    }
}