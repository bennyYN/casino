package de.ben;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PokerGUI extends JFrame {

    // Attribute & Objekte
    public final JLabel betLabel;
    protected Poker game;
    public JPanel panel;
    public int raiseAmount;
    public JTextPane dialogPane; // Dialogbox oben rechts
    public JTextField raiseField; // Textfeld zur Eingabe vom Wert zum Erhöhen
    public JLabel raiseLabel; // Informationstext über dem Eingabefeld zum Erhöhen
    public JLabel bigBlindLabel; // Infotext oben rechts für Big Blind
    public JLabel smallBlindLabel; // Infotext oben rechts für Small Blind
    public JLabel chipsLabel; // Infotext für die Anzahl der Chips
    public JLabel currentPlayerLabel; // Infotext für den aktuellen Spieler
    public JButton foldButton;
    public JButton checkButton;
    public JButton callButton;
    public JButton raiseButton;
    public JButton allInButton;
    public JButton toggleButton; // Neuer Button zum Toggeln der Test-Boolean-Variable
    private boolean testVariable = false; // Test-Boolean-Variable
    public List<String> messages; // String Liste (speichert Nachrichten für Dialogbox)
    public List<Integer> playerChips; // Liste für die Chips jedes Spielers
    public int currentPlayerIndex = 0;
    public int totalPlayers;
    public int actualPlayerCount;
    public int startChips;
    public int bigBlind;
    ArrayList<String> playerNames;
    Playerslot slots;
    private String action = "idle";
    FadingLabel fadingLabel;

    // Konstruktor
    public PokerGUI(int numPlayers, ArrayList<String> playerNames, int startChips, int bigBlind, int actualPlayerCount) {

        totalPlayers = numPlayers; // Set total players
        this.actualPlayerCount = actualPlayerCount;
        this.startChips = startChips;
        this.bigBlind = bigBlind;
        playerChips = new ArrayList<>();
        this.playerNames = playerNames;
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
        slots = new Playerslot(startChips, playerNames, this);

        // Hintergrundbild auf JPanel zeichnen
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(new ImageIcon("img/background.jpg").getImage(), 0, 0, null);
                g.drawImage(new ImageIcon("img/table.png").getImage(), 45, 45, null);

                slots.renderAll(g);
                if(game != null) {
                    //Spieler-Karten
                    if (game.currentPlayer != null) {
                        game.currentPlayer.renderCards(g);
                    }
                    //Dealer-Karten
                    if (game.dealer != null) {
                        game.dealer.renderCards(g);
                    }
                    //Gewinnpot
                    g.drawImage(new ImageIcon("img/pot.png").getImage(), 495, 70, null);
                    g.setFont(new Font("TimesRoman", Font.BOLD, 30));
                    g.setColor(Color.WHITE);
                    g.drawString(String.valueOf(game.GewinnPot.getAmount()), 585, 132);
                }
                updateBetLabel();
                PokerGUI.this.update();
                repaint();
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

        //JLabel mit Fading-Animation


        bigBlindLabel = new JLabel("Big Blind: " + bigBlind);
        bigBlindLabel.setBounds(1000, 10, 150, 30);
        bigBlindLabel.setForeground(Color.WHITE);
        bigBlindLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(bigBlindLabel);

        smallBlindLabel = new JLabel("Small Blind: " + (bigBlind/2));
        smallBlindLabel.setBounds(1000, 50, 150, 30);
        smallBlindLabel.setForeground(Color.WHITE);
        smallBlindLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(smallBlindLabel);

        chipsLabel = new JLabel("Chips: " + playerChips.get(currentPlayerIndex));
        chipsLabel.setBounds(1000, 90, 150, 30);
        chipsLabel.setForeground(Color.WHITE);
        chipsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        //panel.add(chipsLabel);

        betLabel = new JLabel();
        betLabel.setBounds(1000, 90, 150, 30);
        betLabel.setForeground(Color.WHITE);
        betLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(betLabel);

        //FadingLabel
        fadingLabel = new FadingLabel("");
        fadingLabel.setForeground(Color.WHITE);
        fadingLabel.setBounds(200, 20, 800, 30);
        fadingLabel.setHorizontalAlignment(FadingLabel.CENTER);
        fadingLabel.setFont(new Font("Arial", Font.BOLD, 17));
        panel.add(fadingLabel);

        // Create the Help button
        JButton helpButton = new JButton("?");
        helpButton.setBounds(1150, 10, 30, 30); // Position oben rechts
        helpButton.setBackground(new Color(170, 0, 0)); // Wine red color
        helpButton.setForeground(Color.WHITE);
        helpButton.setFont(new Font("Arial", Font.BOLD, 20));
        helpButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        helpButton.setFocusPainted(false);
        panel.add(helpButton);

        // Button ActionListener to open ImageWindow
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ImageWindow().setVisible(true);
            }
        });

        // Buttons
        foldButton = createButton("Fold");
        checkButton = createButton("Check");
        callButton = createButton("Call");
        raiseButton = createButton("Raise");
        allInButton = createButton("All In");
        toggleButton = createButton("Show/Hide Hand");

        // Hardcoded but centered positions
        int buttonWidth = 140;
        int buttonHeight = 70;
        int yPosition = 700;
        int spacing = 30;
        int startX = ((getWidth() - (buttonWidth * 6 + spacing * 5)) / 2) - 6;
        raiseButton.setBounds(startX, yPosition, buttonWidth, buttonHeight);
        checkButton.setBounds(startX + (buttonWidth + spacing) * 1, yPosition, buttonWidth, buttonHeight);
        callButton.setBounds(startX + (buttonWidth + spacing) * 2, yPosition, buttonWidth, buttonHeight);
        foldButton.setBounds(startX + (buttonWidth + spacing) * 3, yPosition, buttonWidth, buttonHeight);
        allInButton.setBounds(startX + (buttonWidth + spacing) * 4, yPosition, buttonWidth, buttonHeight);
        toggleButton.setBounds(startX + (buttonWidth + spacing) * 5, yPosition, buttonWidth, buttonHeight);
        panel.add(foldButton);
        panel.add(checkButton);
        panel.add(callButton);
        panel.add(raiseButton);
        panel.add(allInButton);
        panel.add(toggleButton);

        raiseField = new JTextField();
        raiseField.setBounds(raiseButton.getX(), raiseButton.getY() - 40, buttonWidth, 30); // Position above the raise button
        raiseField.setVisible(false);
        panel.add(raiseField);

        raiseLabel = new JLabel("Raise To:");
        raiseLabel.setBounds(raiseField.getX(), raiseField.getY() - 20, buttonWidth, 20); // Position direkt über dem Eingabefeld
        raiseLabel.setForeground(Color.WHITE);
        raiseLabel.setFont(new Font("Arial", Font.BOLD, 12));
        raiseLabel.setVisible(false);
        panel.add(raiseLabel);

        // Initialize the action listeners for the buttons
        foldButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //addMessageToDialogBox(playerNames.get(currentPlayerIndex) + " folds.");
                updateChips(-10); // Beispielwert für folden
                hideRaiseField();
                action = "fold";
                nextPlayer();
            }
        });
        checkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //addMessageToDialogBox(game.currentPlayer.getName() + " checks.");
                hideRaiseField();
                action = "check";
                nextPlayer();
            }
        });
        callButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //addMessageToDialogBox(playerNames.get(currentPlayerIndex) + " calls.");
                updateChips(-20); // Beispielwert für callen
                hideRaiseField();
                action = "call";
                nextPlayer();
            }
        });
        raiseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                raiseField.setVisible(true);
                raiseLabel.setVisible(true);
                if (!raiseField.getText().isEmpty()) {
                    raiseAmount = Integer.parseInt(raiseField.getText());
                    //addMessageToDialogBox(playerNames.get(currentPlayerIndex) + " raises " + raiseAmount);
                    updateChips(-raiseAmount); // Beispielwert für raisen
                    raiseField.setText(""); // Clear the field after submission
                    hideRaiseField();
                    action = "raise";
                    nextPlayer();
                }
            }
        });
        allInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //addMessageToDialogBox(playerNames.get(currentPlayerIndex) + " goes all in!");
                updateChips(-playerChips.get(currentPlayerIndex)); // Setzt die Chips auf 0
                slots.players.get(currentPlayerIndex).setAllIn(true);
                hideRaiseField();
                action = "allin";
                nextPlayer();
            }
        });
        toggleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(game != null){
                    game.currentPlayer.handVisible = !game.currentPlayer.handVisible;
                }
            }
        });

        add(panel);
        game = new Poker(startChips, bigBlind, numPlayers, slots.actualPlayer, this);
        new Thread(game::startGame).start();
    }

    public void update(){

        if(game.isGameOver){
            raiseButton.setEnabled(false);
            callButton.setEnabled(false);
            checkButton.setEnabled(false);
            foldButton.setEnabled(false);
            allInButton.setEnabled(false);

        }

        if(game != null){
            if(raiseField.isVisible() && raiseLabel.isVisible() && !game.isGameOver){
                if(!raiseField.getText().isEmpty()){
                    if(Integer.parseInt(raiseField.getText()) <= game.highestBet){
                        raiseButton.setEnabled(false);
                    }else{
                        if(Integer.parseInt(raiseField.getText()) <= game.currentPlayer.getChips().getAmount()){
                            raiseButton.setEnabled(true);
                        }else{
                            raiseButton.setEnabled(false);
                        }

                    }
                }else{
                    raiseButton.setEnabled(false);
                }
            }else{
                if(!game.isGameOver)
                raiseButton.setEnabled(true);
            }
        }
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

    public void addMessageToDialogBox(String message) {
        messages.add(message);
        dialogPane.setText(String.join("\n", messages));
        dialogPane.setCaretPosition(dialogPane.getDocument().getLength());
    }

    public void hideRaiseField() {
        raiseField.setVisible(false);
        raiseLabel.setVisible(false);
    }



    private void updateChips(int amount) {

    }

    private void updateBetLabel(){
        if(game != null) {
            betLabel.setText("Highest Bet: " + String.valueOf(game.highestBet));
        }
    }

    public void nextPlayer() {
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % totalPlayers;
        } while (slots.players.get(currentPlayerIndex).isFolded() || slots.players.get(currentPlayerIndex).isAllIn());

        if (slots.players.get(currentPlayerIndex) != null) {
            slots.players.get(currentPlayerIndex).handVisible = false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Assume another class handles the setup and passes the player count here
                ArrayList<String> playerNames = new ArrayList<>();
                playerNames.add("Player 1");
                playerNames.add("Player 2");
                playerNames.add("Player 3");

                new PokerGUI(3, playerNames, 5000, 50, 3).setVisible(true);
            }
        });
    }

    public String getAction() {
       return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}