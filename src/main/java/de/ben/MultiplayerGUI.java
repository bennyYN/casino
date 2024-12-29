package de.ben;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class MultiplayerGUI extends JFrame {

    private final MainGUI mainGUI;
    JPanel panel;
    JButton startGameButton, joinGameButton, returnButton;

    public MultiplayerGUI(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
        this.setTitle("Multiplayer");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        //Titlebar-Icon mit Skalierung setzen
        ImageIcon icon = new ImageIcon("img/icon.png");
        Image scaledIcon = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH); // glatte Skalierung
        setIconImage(scaledIcon);

        // Panel mit GridBagLayout zum Zentrieren der Komponenten
        panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(ImageArchive.getImage("background:"+mainGUI.getSelectedTheme()), 0, 0, null);
                //Button Farbe regelmäßig updaten um ausgewähltem Theme zu matchen
                mainGUI.updateButtonColor(startGameButton, false);
                mainGUI.updateButtonColor(joinGameButton, false);
                mainGUI.updateButtonColor(returnButton, false);
            }
        };
        panel.setOpaque(false); // Panel transparent halten, um Hintergrundbild anzuzeigen
        this.add(panel);

        // Erstelle "Spiel starten" Button
        startGameButton = new JButton("Spiel starten");
        styleButton(startGameButton);
        startGameButton.addActionListener(e -> {
            new Thread(() -> {
                try {
                    // Start the GameServer
                    GameServer server = new GameServer(12345, mainGUI);
                    new Thread(server::start).start();
                    System.out.println("GameServer started.");

                    // Create the GameClient
                    GameClient client = new GameClient("localhost", 12345, mainGUI.getMultiplayerName());
                    System.out.println("GameClient created.");

                    // Show the Lobby
                    SwingUtilities.invokeLater(() -> {
                        Lobby lobby = new Lobby(mainGUI, true, server);
                        lobby.setVisible(true);
                        this.dispose();
                    });
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }).start();
        });

        // Erstelle "Spiel beitreten" Button
        joinGameButton = new JButton("Spiel beitreten");
        styleButton(joinGameButton);
        joinGameButton.addActionListener(e -> {
            new Thread(() -> {
                try {
                    GameClient client = new GameClient("localhost", 12345, mainGUI.getMultiplayerName());
                    System.out.println("GameClient created.");

                    SwingUtilities.invokeLater(() -> {
                        Lobby lobby = new Lobby(mainGUI, false, client.getGameServer());
                        client.setLobby(lobby);
                        lobby.setVisible(true);
                        this.dispose();
                    });
                } catch (IOException ex) {
                    System.err.println("Failed to connect to server: " + ex.getMessage());
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(mainGUI, "Failed to connect to server.", "Connection Error", JOptionPane.ERROR_MESSAGE);
                    });
                }
            }).start();
        });

        // Erstelle "Zurück zum Menü" Button
        returnButton = new JButton("Zurück zum Menü");
        styleButton(returnButton);
        returnButton.addActionListener(e -> {
            mainGUI.setVisible(true);
            this.dispose();
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

        gbc.gridy = 2;
        panel.add(returnButton, gbc);

        this.setVisible(true);
    }



    // Methode zum Stylen des Buttons
    private void styleButton(JButton button) {
        button.setBackground(new Color(78, 136, 174, 255));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(150, 40)); // Größe setzen
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(e -> {
            MainGUI.playSound("click");
        });

        // Create a thin line border
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