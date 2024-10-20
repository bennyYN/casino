package de.ben;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class SettingsGUI extends JFrame {

    private MainGUI mainGUI;
    private BufferedImage backgroundImage; // Hintergrundbild

    public SettingsGUI(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
        this.setTitle("Settings");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(400, 300);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        // Versuche, das Hintergrundbild zu laden, das auch in MainGUI verwendet wird
        try {
            backgroundImage = ImageIO.read(SettingsGUI.class.getResource("/background.png"));
        } catch (IOException e) {
            System.out.println("Hintergrundbild für Settings konnte nicht geladen werden.");
        }

        // Panel mit GridBagLayout für die Zentrierung der Komponenten
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    // Zeichne das Hintergrundbild auf das Panel
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    // Setze einen dunkelgrünen Fallback-Hintergrund, wenn das Bild nicht geladen werden kann
                    g.setColor(new Color(0, 51, 0));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        panel.setOpaque(false); // Panel transparent lassen, damit das Hintergrundbild sichtbar bleibt
        this.add(panel);

        JLabel volumeLabel = new JLabel("Volume");
        volumeLabel.setForeground(Color.YELLOW);

        // Musik-Slider erstellen
        JSlider musicSlider = new JSlider(0, 100, (int) mainGUI.getCurrentVolume()); // Verwende die aktuelle Lautstärke
        musicSlider.setMajorTickSpacing(10);
        musicSlider.setMinorTickSpacing(5);
        musicSlider.setPaintTicks(true);
        musicSlider.setPaintLabels(true);
        musicSlider.setForeground(Color.YELLOW);
        musicSlider.setBackground(new Color(0, 0, 0, 0)); // Transparenter Hintergrund
        musicSlider.setOpaque(false); // Slider transparent machen
        musicSlider.setPreferredSize(new Dimension(300, 50));

        // Aktualisiere die Lautstärke, wenn der Slider bewegt wird
        musicSlider.addChangeListener(e -> {
            int volume = musicSlider.getValue();
            mainGUI.setVolume(volume); // Setze die Lautstärke in der MainGUI und speichere sie
        });

        // Rückkehrbutton
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
        button.setOpaque(false);
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
