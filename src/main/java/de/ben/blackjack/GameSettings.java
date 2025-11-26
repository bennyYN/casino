package de.ben.blackjack;

import de.ben.ImageArchive;
import de.ben.MainGUI;
import de.ben.sound.Sound;
import de.ben.sound.SoundManager;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

public class GameSettings extends JFrame {

    int startChips = 200;
    MainGUI mainGUI;
    JTextField startChipsField;
    JButton exitButton;

    public GameSettings(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Blackjack Konfigurationen");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(ImageArchive.getImage("background:" + mainGUI.getSelectedTheme()), 0, 0, null);
                if (exitButton != null) {
                    exitButton.setOpaque(exitButton.isEnabled());
                    mainGUI.updateButtonColor(exitButton, false);
                }
                repaint();
            }
        };
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel startChipsLabel = new JLabel("Anzahl der Anfangschips (200-10000):");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        startChipsLabel.setForeground(Color.WHITE);
        panel.add(startChipsLabel, gbc);

        JSlider startChipsSlider = new JSlider(200, 10000, startChips);
        startChipsSlider.setMajorTickSpacing(2000);
        startChipsSlider.setPaintTicks(false);
        startChipsSlider.setPaintLabels(false);
        startChipsSlider.setOpaque(false);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(startChipsSlider, gbc);

        startChipsField = new JTextField(String.valueOf(startChips), 5);
        gbc.gridx = 1;
        gbc.gridy = 1;
        startChipsField.setOpaque(false);
        startChipsField.setForeground(Color.WHITE);
        startChipsField.setCaretColor(new Color(174, 174, 174));
        panel.add(startChipsField, gbc);

        startChipsSlider.addChangeListener(e -> {
            startChips = startChipsSlider.getValue();
            startChipsField.setText(String.valueOf(startChips));
        });

        startChipsField.addActionListener(e -> {
            int value = Integer.parseInt(startChipsField.getText());
            value = Math.max(200, Math.min(10000, value));
            startChipsSlider.setValue(value);
        });

        startChipsSlider.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                int value = startChipsSlider.getValue();
                startChipsSlider.setValue(value - notches * 100); // Increase step size to 100
            }
        });

        exitButton = new JButton("Fortfahren");
        exitButton.setBackground(new Color(78, 136, 174, 255));
        exitButton.setForeground(Color.WHITE);
        styleButton(exitButton);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(exitButton, gbc);

        exitButton.addActionListener(e -> {
            SoundManager.playSound(Sound.BUTTON_CLICK);
            new BlackJackGUI(startChips, mainGUI).setVisible(true);
            this.dispose();
        });

        add(panel);
        setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(78, 136, 174, 255));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(150, 40)); // Größe setzen
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        Border thinBorder = BorderFactory.createLineBorder(new Color(255, 255, 255, 81), 2); // 1 pixel thick
        button.setBorder(thinBorder);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBorderPainted(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBorderPainted(false);
            }
        });
    }
}