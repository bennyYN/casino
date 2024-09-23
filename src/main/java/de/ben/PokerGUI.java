/*package de.ben;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class PokerGUI extends JFrame {
    private Poker pokerGame;
    private JButton foldButton;
    private JButton checkButton;
    private JButton callButton;
    private JButton raiseButton;
    private JButton allInButton;
    private JTextField raiseAmountField;
    private JTextField inputField;
    private JButton inputButton;
    private Player player1;
    private Player player2;
    private JLabel player1Label;
    private JLabel player2Label;
    private JLabel dealerCardsLabel;
    private JLabel player1CardsLabel;
    private JLabel player2CardsLabel;
    private final int chips = 5000;
    private int currentPlayerIndex;
    int countertemp = 0;

    public PokerGUI() {
        pokerGame = new Poker(chips, 50, 2, this); // Hard-coded for 2 players
        currentPlayerIndex = 0; // Start with player 1
        initComponents();
        pokerGame.kartenAusteilen();
        pokerGame.blinds();
        updateLabels();
    }

    private void initComponents() {
        setTitle("Poker Game");
        setSize(1500, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(new BackgroundPanel("https://bennyyn.xyz/upload/img/backgrounding.png"));

        try {
            URL iconUrl = new URL("https://bennyyn.xyz/upload/img/icon.png");
            ImageIcon icon = new ImageIcon(iconUrl);
            setIconImage(icon.getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setLayout(new BorderLayout());

        player1 = pokerGame.getPlayers().get(0);
        player2 = pokerGame.getPlayers().get(1);

        // Dealer Label Panel
        JPanel dealerLabelPanel = new JPanel();
        dealerLabelPanel.setOpaque(false);
        JLabel dealerLabel = new JLabel("Dealer");
        dealerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        dealerLabel.setHorizontalAlignment(JLabel.CENTER);
        dealerLabel.setForeground(Color.WHITE);
        dealerLabelPanel.add(dealerLabel);

        // Dealer Cards Panel
        dealerCardsLabel = new JLabel();
        dealerCardsLabel.setFont(new Font("Arial", Font.BOLD, 18));
        dealerCardsLabel.setForeground(Color.WHITE);
        dealerCardsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dealerCardsLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));

        JPanel dealerPanel = new JPanel(new BorderLayout());
        dealerPanel.setOpaque(false);
        dealerPanel.add(dealerLabelPanel, BorderLayout.NORTH);
        dealerPanel.add(dealerCardsLabel, BorderLayout.SOUTH);

        // Spieler Information Panel
        JPanel playerInfoPanel = new JPanel();
        playerInfoPanel.setOpaque(false);
        player1Label = new JLabel("Player 1 - Chips: " + player1.getChips().getAmount());
        player1Label.setFont(new Font("Arial", Font.BOLD, 18));
        player1Label.setForeground(Color.WHITE);
        player1Label.setHorizontalAlignment(SwingConstants.CENTER);
        player1Label.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        player2Label = new JLabel("Player 2 - Chips: " + player2.getChips().getAmount());
        player2Label.setFont(new Font("Arial", Font.BOLD, 18));
        player2Label.setForeground(Color.WHITE);
        player2Label.setHorizontalAlignment(SwingConstants.CENTER);
        player2Label.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        player1CardsLabel = new JLabel();
        player1CardsLabel.setFont(new Font("Arial", Font.BOLD, 18));
        player1CardsLabel.setForeground(Color.WHITE);
        player1CardsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        player1CardsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        player2CardsLabel = new JLabel();
        player2CardsLabel.setFont(new Font("Arial", Font.BOLD, 18));
        player2CardsLabel.setForeground(Color.WHITE);
        player2CardsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        player2CardsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel player1Panel = new JPanel(new BorderLayout());
        player1Panel.setOpaque(false);
        player1Panel.add(player1Label, BorderLayout.NORTH);
        player1Panel.add(player1CardsLabel, BorderLayout.SOUTH);

        JPanel player2Panel = new JPanel(new BorderLayout());
        player2Panel.setOpaque(false);
        player2Panel.add(player2Label, BorderLayout.NORTH);
        player2Panel.add(player2CardsLabel, BorderLayout.SOUTH);

        playerInfoPanel.setLayout(new GridLayout(1, 2));
        playerInfoPanel.add(player1Panel);
        playerInfoPanel.add(player2Panel);

        // Control Panel
        JPanel controlPanel = new JPanel();
        controlPanel.setOpaque(false);
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        foldButton = createPokerButton("Fold");
        checkButton = createPokerButton("Check");
        callButton = createPokerButton("Call");
        raiseButton = createPokerButton("Raise");
        allInButton = createPokerButton("All In");
        inputButton = createPokerButton("Submit"); // Button für die Eingabe
        raiseAmountField = new JTextField(5);
        inputField = new JTextField(10); // Textfeld für die Eingabe
        raiseAmountField.setFont(new Font("Arial", Font.PLAIN, 14));
        raiseAmountField.setForeground(Color.white);
        raiseAmountField.setOpaque(false);
        raiseAmountField.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputField.setForeground(Color.white);
        inputField.setOpaque(false);
        inputField.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        controlPanel.add(foldButton);
        controlPanel.add(checkButton);
        controlPanel.add(callButton);
        controlPanel.add(new JLabel("Raise Amount:"));
        controlPanel.add(raiseAmountField);
        controlPanel.add(raiseButton);
        controlPanel.add(allInButton);
        controlPanel.add(new JLabel("Input Action:")); // Label für die Eingabe
        controlPanel.add(inputField); // Textfeld für die Eingabe
        controlPanel.add(inputButton); // Button für die Eingabe

        // South Panel containing Player Info and Control Panel
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setOpaque(false);
        southPanel.add(playerInfoPanel, BorderLayout.NORTH);
        southPanel.add(controlPanel, BorderLayout.SOUTH);

        add(dealerPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.SOUTH);

        // Adding Action Listeners to Buttons
        addListeners();
    }

    private void addListeners() {
        if(!allPlayersFolded()){
            foldButton.addActionListener(e -> foldAction());
            checkButton.addActionListener(e -> checkAction());
            callButton.addActionListener(e -> callAction());
            raiseButton.addActionListener(e -> raiseAction());
            allInButton.addActionListener(e -> allInAction());
            inputButton.addActionListener(e -> inputAction());
        }// ActionListener für den Input-Button
    }

    private void foldAction() {
        if (!pokerGame.getPlayers().get(currentPlayerIndex).isFolded()) {
            pokerGame.fold(currentPlayerIndex);
            switchTurn();
            updateLabels();
            if (allPlayersFolded()) {
                pokerGame.playRunde();
            }
        }
    }

    private void checkAction() {
        if (pokerGame.check(currentPlayerIndex)) {
            switchTurn();
            updateLabels();
            pokerGame.playRunde();
        }
    }

    private void callAction() {
        pokerGame.call(currentPlayerIndex, pokerGame.getHighestBet());
        switchTurn();
        updateLabels();
        pokerGame.playRunde();
    }

    private void raiseAction() {
        try {
            int raiseAmount = Integer.parseInt(raiseAmountField.getText());
            pokerGame.raise(currentPlayerIndex, raiseAmount);
            switchTurn();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid raise amount", "Error", JOptionPane.ERROR_MESSAGE);
        }
        updateLabels();
        pokerGame.playRunde();
    }

    private void allInAction() {
        pokerGame.allIn(currentPlayerIndex);
        switchTurn();
        updateLabels();
        pokerGame.playRunde();
    }

    // Neue Methode für die Eingabeaktion
    private void inputAction() {
        String input = inputField.getText();
        Scanner scanner = new Scanner(input); // Übergabe des Strings an einen Scanner

        // Beispiel: Verarbeitung des gescannten Textes
        if (scanner.hasNext()) {
            String action = scanner.next();
            switch (action.toLowerCase()) {
                case "fold":
                    foldAction();
                    break;
                case "check":
                    checkAction();
                    break;
                case "call":
                    callAction();
                    break;
                case "raise":
                    if (scanner.hasNextInt()) {
                        int raiseAmount = scanner.nextInt();
                        raiseAmountField.setText(String.valueOf(raiseAmount));
                        raiseAction();
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid raise amount", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;
                case "allin":
                    allInAction();
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Invalid action", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        }
    }

    private void switchTurn() {
        countertemp++;
        if(countertemp%2==0){
            for(Player player : pokerGame.getPlayers()){
                player.resetCurrentBet();
                pokerGame.setHighestBet(0);
            }
        }

        currentPlayerIndex = (currentPlayerIndex + 1) % pokerGame.getPlayers().size();

    }

    private void updateLabels() {
        System.out.println("Updating labels...");

        System.out.println("Player 1 Hand: " + player1.getHand());

        System.out.println("Player 2 Hand: " + player2.getHand());

        // Aktualisiere die Labels auf dem GUI-Thread, um sicherzustellen, dass die Änderungen sichtbar werden
        SwingUtilities.invokeLater(() -> {
            player1Label.setText("Player 1 - Chips: " + player1.getChips().getAmount());
            player2Label.setText("Player  2 - Chips: " + player2.getChips().getAmount());

            // Setze den Text für die Kartenlabels
            player1CardsLabel.setText("Cards: " + getCardsText(player1.getHand()));
            player2CardsLabel.setText("Cards: " + getCardsText(player2.getHand()));
            dealerCardsLabel.setText("Dealer's cards: " + getCardsText(pokerGame.getDealer().getHand()));

            // Erzwinge die Neuberechnung der Größe und das Neuzeichnen der betroffenen Komponenten
            player1CardsLabel.revalidate();
            player1CardsLabel.repaint();
            player2CardsLabel.revalidate();
            player2CardsLabel.repaint();
            dealerCardsLabel.revalidate();
            dealerCardsLabel.repaint();
        });
    }

    private String getCardsText(List<Card> cards) {
        StringBuilder cardsText = new StringBuilder();
        for (Card card : cards) {
            // Füge jede Karte als String zum StringBuilder hinzu
            cardsText.append(card.toString()).append(" ");
        }
        return cardsText.toString().trim();
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

    private static class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imageUrl) {
            try {
                backgroundImage = new ImageIcon(new URL(imageUrl)).getImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public String getInput() {
        return inputField.getText();
    }

    public void endRound() {
        for (Player player : this.pokerGame.getPlayers()) {
            player.reset();
            player.clearHand();
        }
        Deck deck = new Deck();
        pokerGame.setdeck(deck);
        pokerGame.resetPot();
        pokerGame.getDealer().clearHand();
    }

    private boolean allPlayersFolded() {
        for (Player player : pokerGame.getPlayers()) {
            if (!player.isFolded()) {
                return false;
            }
        }
        endRound();
        pokerGame.playRunde();
        return true;
    }

    public int getRaiseAmount() {
        try {
            return Integer.parseInt(raiseAmountField.getText());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PokerGUI().setVisible(true));
    }
}*/