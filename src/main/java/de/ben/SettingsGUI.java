package de.ben;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

public class SettingsGUI extends JFrame {

    private MainGUI mainGUI;
    private BufferedImage backgroundImage; // Hintergrundbild
    private boolean usedInMainGUI;
    String[] themes = {"Original", "Dark", "Darkblue", "Light", "Scarlet"};
    JComboBox<String> themeDropdown = new JComboBox<>(themes);;

    public SettingsGUI(MainGUI mainGUI, boolean usedInMainGUI) {
        this.mainGUI = mainGUI;
        this.setTitle("Settings");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(600, 400); // Set the same window size as PlayerSelectionGUI
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        // Panel with GridBagLayout for centering components
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(new ImageIcon("img/background.jpg").getImage(), 0, 0, null);
            }
        };
        panel.setOpaque(false); // Keep panel transparent to show background image
        this.add(panel);

        JLabel volumeLabel = new JLabel("Music-Volume");
        volumeLabel.setForeground(Color.WHITE);
        volumeLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // Create music slider
        JSlider musicSlider = new JSlider(0, 100, (int) mainGUI.getCurrentVolume());
        musicSlider.setMajorTickSpacing(10);
        musicSlider.setMinorTickSpacing(5);
        musicSlider.setPaintTicks(true);
        musicSlider.setPaintLabels(true);
        musicSlider.setForeground(Color.WHITE);
        musicSlider.setBackground(new Color(0, 0, 0, 0)); // Transparent background
        musicSlider.setOpaque(false); // Make slider transparent

        //Show current theme
        for(int i = 0; i < themes.length; i++) {
            if (themes[i].equals(mainGUI.getSelectedTheme())) {
                themeDropdown.setSelectedIndex(i);
                break;
            }
        }

        // Update volume when slider is moved
        musicSlider.addChangeListener(e -> {
            int volume = musicSlider.getValue();
            mainGUI.setVolume(volume); // Set volume in MainGUI and save it
        });

        // Add mouse wheel listener to music slider
        musicSlider.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                musicSlider.setValue(musicSlider.getValue() - notches);
            }
        });

        // Create game sounds slider
        JLabel gameSoundsLabel = new JLabel("Game Sounds:");
        gameSoundsLabel.setForeground(Color.WHITE);
        gameSoundsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JSlider soundSlider = new JSlider(0, 100, 30);
        soundSlider.setMajorTickSpacing(10);
        soundSlider.setPaintTicks(true);
        soundSlider.setPaintLabels(true);
        soundSlider.setForeground(Color.WHITE);
        soundSlider.setBackground(new Color(0, 0, 0, 0)); // Transparent background
        soundSlider.setOpaque(false); // Make slider transparent

        // Add mouse wheel listener to sound slider
        soundSlider.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                soundSlider.setValue(soundSlider.getValue() - notches);
            }
        });

        // Create theme dropdown menu
        JLabel themeLabel = new JLabel("Select Theme:");
        themeLabel.setForeground(Color.WHITE);
        themeLabel.setFont(new Font("Arial", Font.BOLD, 16));


        for(int i = 0; i < themes.length; i++) {
            if (themes[i].equals(mainGUI.getSelectedTheme())) {
                themeDropdown.setSelectedIndex(i);
                break;
            }
        }
        //themeDropdown.setSelectedIndex(0); // Default to "Original"

        // Add mouse wheel listener to theme dropdown
        themeDropdown.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                int newIndex = themeDropdown.getSelectedIndex() - notches;
                if (newIndex >= 0 && newIndex < themeDropdown.getItemCount()) {
                    themeDropdown.setSelectedIndex(newIndex);
                }
            }
        });

        // Update theme when selected
        themeDropdown.addActionListener(e -> {
            String selectedTheme = (String) themeDropdown.getSelectedItem();
            mainGUI.saveSelectedTheme(selectedTheme); // Set theme in MainGUI and save it
        });
        // Create back button
        JButton backButton = new JButton("Back");
        styleButton(backButton);
        backButton.addActionListener(e -> {
            this.dispose(); // Close SettingsGUI
            if (usedInMainGUI) {
                mainGUI.setVisible(true); // Show MainGUI again
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 100, 10, 100); // Add more space around components
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make components fill horizontally
        gbc.weightx = 1.0; // Allow components to grow horizontally

        gbc.gridy = 0;
        panel.add(themeLabel, gbc);

        gbc.gridy = 1;
        panel.add(themeDropdown, gbc);

        gbc.gridy = 2;
        panel.add(volumeLabel, gbc);

        gbc.gridy = 3;
        panel.add(musicSlider, gbc);

        gbc.gridy = 4;
        panel.add(gameSoundsLabel, gbc);

        gbc.gridy = 5;
        panel.add(soundSlider, gbc);

        gbc.gridy = 6;
        panel.add(backButton, gbc);

        this.setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(78, 136, 174, 255));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(150, 40)); // Set size
        button.setBorderPainted(false);
        button.setFocusPainted(false);
    }
}