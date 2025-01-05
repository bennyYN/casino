package de.ben;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

public class Lobby extends JFrame {

    int startChips = 200;
    int bigBlind = 20;
    MainGUI mainGUI;
    JLabel startChipsLabel;
    JLabel bigBlindLabel;
    JTextField startChipsField;
    JTextField bigBlindField;
    ArrayList<String> playerNames;
    ArrayList<String> slotState;
    JButton exitButton, confirmButton;
    boolean isLeader;
    int playerCount = 0;
    GameServer gameServer;
    private GameClient gameClient;

    public Lobby(MainGUI mainGUI, boolean isLeader, GameServer gameServer, GameClient client) {
        this.mainGUI = mainGUI;
        this.isLeader = isLeader;
        this.gameServer = gameServer;
        this.gameClient= client;

        if (gameServer != null) {
            gameServer.setLobby(this);
        }

        initializeUI();
        updatePlayerNames();
    }

    public Lobby(MainGUI mainGUI, boolean isLeader, GameClient cliente) {
        this(mainGUI, isLeader, null, cliente);
    }

    private void initializeUI() {
        playerNames = new ArrayList<>();
        slotState = new ArrayList<>(8);
        for (int i = 0; i <= 7; i++) {
            slotState.add("");
        }
        for (int i = 0; i <= 7; i++) {
            playerNames.add("");
        }

        setTitle("Player Settings");
        setSize(850, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        ImageIcon icon = new ImageIcon("img/icon.png");
        Image scaledIcon = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        setIconImage(scaledIcon);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(ImageArchive.getImage("background:" + mainGUI.getSelectedTheme()), 0, 0, null);
                if (exitButton != null) {
                    exitButton.setOpaque(true);
                    mainGUI.updateButtonColor(exitButton, false);
                    if (playerCount >= 2) {
                        exitButton.setEnabled(true);
                    } else {
                        exitButton.setEnabled(false);
                    }
                    if (!isLeader) {
                        exitButton.setText("Leave Room");
                    }
                }
                renderAll(g);
            }
        };
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        if (isLeader) {
            startChipsLabel = new JLabel("Starting Chips (200-10000):");
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            startChipsLabel.setForeground(Color.WHITE);
            panel.add(startChipsLabel, gbc);

            JSlider startChipsSlider = new JSlider(200, 10000, startChips);
            startChipsSlider.setMajorTickSpacing(2000);
            startChipsSlider.setPaintTicks(false);
            startChipsSlider.setPaintLabels(false);
            startChipsSlider.setOpaque(false);
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.gridwidth = 1;
            panel.add(startChipsSlider, gbc);

            startChipsField = new JTextField(String.valueOf(startChips), 5);
            gbc.gridx = 1;
            gbc.gridy = 3;
            startChipsField.setOpaque(false);
            startChipsField.setForeground(Color.WHITE);
            startChipsField.setCaretColor(new Color(174, 174, 174));
            panel.add(startChipsField, gbc);

            startChipsSlider.addChangeListener(e -> {
                startChips = startChipsSlider.getValue();
                startChipsLabel.setText("Starting Chips (200-10000):");
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
                    startChipsSlider.setValue(value - notches * 100);
                }
            });

            bigBlindLabel = new JLabel("Big Blind (20-2000):");
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 2;
            bigBlindLabel.setForeground(Color.WHITE);
            panel.add(bigBlindLabel, gbc);

            JSlider bigBlindSlider = new JSlider(20, 2000, bigBlind);
            bigBlindSlider.setMinorTickSpacing(20);
            bigBlindSlider.setMajorTickSpacing(400);
            bigBlindSlider.setPaintTicks(false);
            bigBlindSlider.setPaintLabels(false);
            bigBlindSlider.setOpaque(false);
            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.gridwidth = 1;
            panel.add(bigBlindSlider, gbc);

            bigBlindField = new JTextField(String.valueOf(bigBlind), 5);
            gbc.gridx = 1;
            gbc.gridy = 5;
            bigBlindField.setOpaque(false);
            bigBlindField.setForeground(Color.WHITE);
            bigBlindField.setCaretColor(new Color(174, 174, 174));
            panel.add(bigBlindField, gbc);

            bigBlindSlider.addChangeListener(e -> {
                bigBlind = bigBlindSlider.getValue();
                if (bigBlind % 2 != 0) {
                    bigBlind -= 1;
                }
                bigBlindLabel.setText("Big Blind (20-2000):");
                bigBlindField.setText(String.valueOf(bigBlind));
                bigBlindSlider.setValue(bigBlind);
            });

            bigBlindField.addActionListener(e -> {
                int value = Integer.parseInt(bigBlindField.getText());
                value = Math.max(20, Math.min(2000, value));
                if (value % 2 != 0) {
                    value = (value > bigBlindSlider.getValue() ? value + 1 : value - 1);
                }
                bigBlindSlider.setValue(value);
            });

            bigBlindSlider.addMouseWheelListener(new MouseAdapter() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    int notches = e.getWheelRotation();
                    int value = bigBlindSlider.getValue();
                    value -= notches * 20;
                    value = Math.max(20, Math.min(2000, value));
                    if (value % 2 != 0) {
                        value = (value > bigBlindSlider.getValue() ? value + 1 : value - 1);
                    }
                    bigBlindSlider.setValue(value);
                }
            });
        } else {
            startChipsLabel = new JLabel("Starting Chips: " + startChips);
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            startChipsLabel.setForeground(Color.WHITE);
            panel.add(startChipsLabel, gbc);

            bigBlindLabel = new JLabel("Big Blind: " + bigBlind);
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 2;
            bigBlindLabel.setForeground(Color.WHITE);
            panel.add(bigBlindLabel, gbc);
        }

        exitButton = new JButton("Continue");
        exitButton.setBackground(new Color(78, 136, 174, 255));
        exitButton.setForeground(Color.WHITE);
        styleButton(exitButton);
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.gridy = 7;
        panel.add(exitButton, gbc);

        exitButton.addActionListener(e -> {
            if (isLeader) {
                MainGUI.playSound("click");
                SwingUtilities.invokeLater(() -> {

                    int startChips = Integer.parseInt(startChipsField.getText());
                    int bigBlind = Integer.parseInt(bigBlindField.getText());
                    String playerNamesString = String.join(",", playerNames);

                    gameClient.sendMessage("START:" + playerCount+ ":" + startChips + ":" + bigBlind + ":" + playerNamesString);

                    this.dispose();
                });
            } else {
                MainGUI.playSound("invalid");
                mainGUI.setVisible(true);
                this.dispose();
            }
        });

        add(panel);
        setVisible(true);
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(78, 136, 174, 255));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(150, 40));
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        Border thinBorder = BorderFactory.createLineBorder(new Color(255, 255, 255, 81), 2);
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

    public void addPlayer(String name) {
        for (int i = 0; i <= 7; i++) {
            if (playerNames.size() <= i || playerNames.get(i).equals("")) {
                if (playerNames.size() <= i) {
                    playerNames.add(name);
                } else {
                    playerNames.set(i, name);
                }
                playerCount++;
                System.out.println("Player added: " + name);
                break;
            }
        }
        repaint();
    }

    public void setPlayerNames(List<String> names) {
        playerNames.clear();
        playerNames.addAll(names);
        playerCount = names.size();
        repaint();
    }

    public void removePlayer(String name) {
        for (int i = 0; i <= 7; i++) {
            if (playerNames.get(i).equals(name)) {
                playerNames.set(i, "");
                playerCount--;
                break;
            }
        }
        repaint();
    }

    public void renderAll(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        updateSlotStates();

        int spacing = 60;
        int x2 = 640;
        int x1 = 35;
        int y = 75;

        fillEmptySlots();

        for (int i = 0; i <= 7; i++) {
            fillEmptySlots();
            if (i <= 3) {
                g2d.drawImage(ImageArchive.getImage("lobby:" + slotState.get(i) + "playerslot"), x1, y + (i * spacing), null);
                if (!playerNames.get(i).equals("")) {
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("TimesRoman", Font.BOLD, 16));
                    g2d.drawString(playerNames.get(i), x1 + 15, 22 + y + (i * spacing));
                }
            } else {
                g2d.drawImage(ImageArchive.getImage("lobby:" + slotState.get(i) + "playerslot"), x2, y + ((i - 4) * spacing), null);
                if (!playerNames.get(i).equals("")) {
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("TimesRoman", Font.BOLD, 16));
                    g2d.drawString(playerNames.get(i), x2 + 15, 22 + y + ((i - 4) * spacing));
                }
            }
        }
    }

    private void fillEmptySlots() {
        //Mögliche leere stellen im array mit leeren Strings füllen
        for (int i = 0; i <= 7; i++) {
            if (playerNames.size() <= i) {
                playerNames.add("");
            }
        }
    }

    public void updateSlotStates() {
        fillEmptySlots();
        for (int i = 0; i <= 7; i++) {
            try {
                if (playerNames.get(i) != null) {
                    if (playerNames.get(i).equals("")) {
                        slotState.set(i, "empty_");
                    } else {
                        slotState.set(i, "");
                        if (0 == i) {
                            slotState.set(i, "host_");
                        }
                    }
                }
                if(i == mainGUI.playerIndex) {
                    slotState.set(i, "you_" + slotState.get(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updatePlayerNames() {
        if (gameServer != null) {
            for (String playerName : gameServer.getPlayerNames()) {
                if (playerName != null && !playerName.isEmpty()) {
                    addPlayer(playerName);
                }
            }
        } else {
            System.out.println("GameServer is null. Skipping player name initialization.");
        }
    }
}