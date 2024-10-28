package de.ben;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PokerGUI extends JFrame {
    // Attribute & Objekte
    //protected Poker game;
    private JPanel panel;
    private JTextPane dialogPane; // Dialogbox oben rechts
    private JTextField raiseField; // Textfeld zur Eingabe vom Wert zum Erhöhen
    private JLabel raiseLabel; // Informationstext über dem Eingabefeld zum Erhöhen
    private JLabel bigBlindLabel; // Infotext oben rechts für Big Blind
    private JLabel smallBlindLabel; // Infotext oben rechts für Small Blind
    private JLabel chipsLabel; // Infotext für die Anzahl der Chips
    private JLabel currentPlayerLabel; // Infotext für den aktuellen Spieler
    private JButton foldButton;
    private JButton checkButton;
    private JButton callButton;
    private JButton raiseButton;
    private JButton allInButton;
    private List<String> messages; // String Liste (speichert Nachrichten für Dialogbox)
    private List<Integer> playerChips; // Liste für die Chips jedes Spielers
    private int currentPlayerIndex = 0;
    private int totalPlayers;

    // Konstruktor
    public PokerGUI(int numPlayers, ArrayList<String> playerNames, int startChips, int bigBlind) {
        totalPlayers = numPlayers; // Set total players
        //game = new Poker(startChips, bigBlind, numPlayers, this);
        playerChips = new ArrayList<>();
        for (int i = 0; i < totalPlayers; i++) {
            playerChips.add(1000); // Beispiel: Jeder Spieler startet mit 1000 Chips
        }

        setTitle("Poker Game"); // Fenster Titel zuweisen
        setSize(1200, 850); // Größe des Fensters setzen
        setResizable(false); // Größe des Fensters festsetzen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(new ImageIcon("img/icon.png").getImage()); // Titlebar Icon hinzufügen

        //Spieler-Slots


        // Hintergrundbild auf JPanel zeichnen
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(new ImageIcon("img/background.jpg").getImage(), 0, 0, null);
                g.drawImage(new ImageIcon("img/table.png").getImage(), 45, 45, null);

                //Einzeichnen der Playerslots
                int abstand = 100;
                for (int i = 0; i <= 3; i++) {
                    //LINKS
                    g.drawImage(new ImageIcon("img/playerslot.png").getImage(), 20, 225 + (i * abstand), null);
                    //RECHTS
                    g.drawImage(new ImageIcon("img/playerslot.png").getImage(), 1008, 225 + (i * abstand), null);
                }
            }
        };
        panel.setLayout(null);

        messages = new ArrayList<>();
        dialogPane = new JTextPane();
        dialogPane.setEditable(false);
        dialogPane.setOpaque(false); // Make the dialog box transparent
        dialogPane.setForeground(Color.WHITE); // Set the text color to white
        dialogPane.setFont(new Font("Arial", Font.PLAIN, 16)); // Slightly bigger text
        JScrollPane scrollPane = new JScrollPane(dialogPane);
        scrollPane.setBounds(10, 10, 350, 170); // Adjusted bounds for the larger window
        scrollPane.setOpaque(false); // Make the scroll pane transparent
        scrollPane.getViewport().setOpaque(false); // Make the viewport transparent
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove the border
        // Hide scrollbars but keep functionality
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        panel.add(scrollPane);

        bigBlindLabel = new JLabel("Big Blind: 100");
        bigBlindLabel.setBounds(1000, 10, 150, 30);
        bigBlindLabel.setForeground(Color.WHITE);
        bigBlindLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(bigBlindLabel);

        smallBlindLabel = new JLabel("Small Blind: 50");
        smallBlindLabel.setBounds(1000, 50, 150, 30);
        smallBlindLabel.setForeground(Color.WHITE);
        smallBlindLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(smallBlindLabel);

        chipsLabel = new JLabel("Chips: " + playerChips.get(currentPlayerIndex));
        chipsLabel.setBounds(1000, 90, 150, 30);
        chipsLabel.setForeground(Color.WHITE);
        chipsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(chipsLabel);

        currentPlayerLabel = new JLabel("Player 1");
        currentPlayerLabel.setBounds((getWidth() - 150) / 2, 10, 150, 30);
        currentPlayerLabel.setForeground(Color.WHITE);
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        currentPlayerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(currentPlayerLabel);

        // Buttons
        foldButton = createButton("Fold");
        checkButton = createButton("Check");
        callButton = createButton("Call");
        raiseButton = createButton("Raise");
        allInButton = createButton("All In");

        // Hardcoded but centered positions
        int buttonWidth = 140;
        int buttonHeight = 70;
        int yPosition = 700;
        int spacing = 30;
        int startX = (getWidth() - (buttonWidth * 5 + spacing * 4)) / 2;
        foldButton.setBounds(startX, yPosition, buttonWidth, buttonHeight);
        checkButton.setBounds(startX + (buttonWidth + spacing) * 1, yPosition, buttonWidth, buttonHeight);
        callButton.setBounds(startX + (buttonWidth + spacing) * 2, yPosition, buttonWidth, buttonHeight);
        raiseButton.setBounds(startX + (buttonWidth + spacing) * 3, yPosition, buttonWidth, buttonHeight);
        allInButton.setBounds(startX + (buttonWidth + spacing) * 4, yPosition, buttonWidth, buttonHeight);
        panel.add(foldButton);
        panel.add(checkButton);
        panel.add(callButton);
        panel.add(raiseButton);
        panel.add(allInButton);

        raiseField = new JTextField();
        raiseField.setBounds(raiseButton.getX(), raiseButton.getY() - 40, buttonWidth, 30); // Position above the raise button
        raiseField.setVisible(false);
        panel.add(raiseField);

        raiseLabel = new JLabel("Raise Amount:");
        raiseLabel.setBounds(raiseField.getX(), raiseField.getY() - 20, buttonWidth, 20); // Position directly above the input field
        raiseLabel.setForeground(Color.WHITE);
        raiseLabel.setFont(new Font("Arial", Font.BOLD, 12));
        raiseLabel.setVisible(false);
        panel.add(raiseLabel);

        // Initialize the action listeners for the buttons
        foldButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addMessageToDialogBox("Player " + (currentPlayerIndex + 1) + " folds.");
                updateChips(-10); // Beispielwert für folden
                hideRaiseField();
                nextPlayer();
            }
        });
        checkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addMessageToDialogBox("Player " + (currentPlayerIndex + 1) + " checks.");
                hideRaiseField();
                nextPlayer();
            }
        });
        callButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addMessageToDialogBox("Player " + (currentPlayerIndex + 1) + " calls.");
                updateChips(-20); // Beispielwert für callen
                hideRaiseField();
                nextPlayer();
            }
        });
        raiseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                raiseField.setVisible(true);
                raiseLabel.setVisible(true);
                if (!raiseField.getText().isEmpty()) {
                    int raiseAmount = Integer.parseInt(raiseField.getText());
                    addMessageToDialogBox("Player " + (currentPlayerIndex + 1) + " raises " + raiseAmount);
                    updateChips(-raiseAmount); // Beispielwert für raisen
                    raiseField.setText(""); // Clear the field after submission
                    hideRaiseField();
                    nextPlayer();
                }
            }
        });
        allInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addMessageToDialogBox("Player " + (currentPlayerIndex + 1) + " goes all in!");
                updateChips(-playerChips.get(currentPlayerIndex)); // Setzt die Chips auf 0
                hideRaiseField();
                nextPlayer();
            }
        });

        add(panel);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(170, 0, 0)); // Wine red color
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setOpaque(true);
        button.setBorderPainted(true);
        return button;
    }

    private void addMessageToDialogBox(String message) {
        messages.add(message);
        dialogPane.setText(String.join("\n", messages));
    }

    private void hideRaiseField() {
        raiseField.setVisible(false);
        raiseLabel.setVisible(false);
    }

    private void updateChips(int amount) {
        int newChips = playerChips.get(currentPlayerIndex) + amount;
        playerChips.set(currentPlayerIndex, newChips);
        chipsLabel.setText("Chips: " + playerChips.get(currentPlayerIndex));
    }

    private void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % totalPlayers;
        currentPlayerLabel.setText("Player " + (currentPlayerIndex + 1));
        chipsLabel.setText("Chips: " + playerChips.get(currentPlayerIndex));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Assume another class handles the setup and passes the player count here
                ArrayList<String> playerNames = new ArrayList<>();
                playerNames.add("Player 1");
                playerNames.add("Player 2");
                playerNames.add("Player 3");

                new PokerGUI(3, playerNames, 5000, 50).setVisible(true);
            }
        });
    }
}