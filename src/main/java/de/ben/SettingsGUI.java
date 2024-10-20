package de.ben;

import javax.swing.*;
import java.awt.*;

public class SettingsGUI extends JFrame {

    private MainGUI mainGUI;

    public SettingsGUI(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
        this.setTitle("Settings");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        // Panel with GridBagLayout for centering the components
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(0, 51, 0)); // Dark green background
        this.add(panel);

        JLabel volumeLabel = new JLabel("Volume");
        volumeLabel.setForeground(Color.YELLOW);

        // Create music slider
        JSlider musicSlider = new JSlider(0, 100, (int) mainGUI.getCurrentVolume()); // Verwende die aktuelle Lautstärke
        musicSlider.setMajorTickSpacing(10);
        musicSlider.setMinorTickSpacing(5);
        musicSlider.setPaintTicks(true);
        musicSlider.setPaintLabels(true);
        musicSlider.setForeground(Color.YELLOW);
        musicSlider.setBackground(new Color(0, 51, 0));
        musicSlider.setPreferredSize(new Dimension(300, 50));

        // Aktualisiere die Lautstärke, wenn der Slider bewegt wird
        musicSlider.addChangeListener(e -> {
            int volume = musicSlider.getValue();
            mainGUI.setVolume(volume); // Setze die Lautstärke in der MainGUI und speichere sie
        });

        JButton backButton = new JButton("Back");
        styleButton(backButton);
        backButton.addActionListener(e -> {
            this.dispose(); // Schließe SettingsGUI
            mainGUI.setVisible(true); // Zeige MainGUI wieder an
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridy = 0;
        panel.add(volumeLabel, gbc);

        gbc.gridy = 1;
        panel.add(musicSlider, gbc);

        gbc.gridy = 2;
        panel.add(backButton, gbc);

        this.setVisible(true);
    }

    private void styleButton(JButton button) {
        Color normalColor = new Color(0, 100, 0);
        Color pressedColor = new Color(0, 200, 0);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBackground(normalColor);
        button.setForeground(Color.YELLOW);
        button.setPreferredSize(new Dimension(150, 40));
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
