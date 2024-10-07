/*package de.ben;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class altheMainGUI extends JFrame {
    private JButton b1, b2;
    private JLabel backgroundLabel;

    public altheMainGUI() {
        setTitle("Poker Casino Game");
        setSize(768, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            URL backgroundURL = new URL("https://bennyyn.xyz/upload/img/background.png");
            ImageIcon backgroundIcon = new ImageIcon(backgroundURL);
            backgroundLabel = new JLabel(backgroundIcon);
            setContentPane(backgroundLabel);
            backgroundLabel.setLayout(new GridBagLayout());

            try {
                URL iconUrl = new URL("https://bennyyn.xyz/upload/img/icon.png");
                ImageIcon icon = new ImageIcon(iconUrl);
                setIconImage(icon.getImage());
            } catch (Exception e) {
                e.printStackTrace();
            }


            // Button-Initialisierung
            b1 = createPokerButton("Start");
            b2 = createPokerButton("Quit");

            // Aktionen für Buttons
            b1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openPokerGUI();
                }
            });

            b2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);  // Beendet das Programm
                }
            });

            JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
            buttonPanel.setOpaque(false);
            buttonPanel.add(b1);
            buttonPanel.add(b2);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            backgroundLabel.add(buttonPanel, gbc);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading images.", "Image loading error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openPokerGUI() {
        EventQueue.invokeLater(() -> {
            PokerGUI pokerGUI = new PokerGUI();
            pokerGUI.setVisible(true);
            this.setVisible(false);
        });
    }


    private JButton createPokerButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(100, 40));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 128, 0));
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        button.setFocusPainted(false);
        return button;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            MainGUI frame = new MainGUI();
            frame.setVisible(true);
        });
    }
}*/