package de.ben;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Poker extends JFrame {
    private Player player;
    private Player dealer;
    private Player bot1;
    private Player bot2;
    private Deck deck;
    private Chips playerChips;
    private Chips botChips1;
    private Chips botChips2;
    private JButton callButton;
    private JButton checkButton;
    private JButton raiseButton;
    private JButton foldButton;
    private JButton confirmButton;
    private JSlider raiseSlider;
    private JLabel raiseAmountLabel;
    private JLabel playerChipsLabel;
    private JLabel botChipsLabel1;
    private JLabel botChipsLabel2;
    private JPanel buttonPanel;
    private JPanel chipsPanel;
    private boolean hasCurrentBet = false;

    public Poker() {
        setTitle("Poker");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); // Use BorderLayout

        buttonPanel = new JPanel(new FlowLayout()); // Initialize buttonPanel here
        chipsPanel = new JPanel(); // Initialize chipsPanel here

        player = new Player();
        dealer = new Player();
        bot1 = new Player();
        bot2 = new Player();
        deck = new Deck();
        playerChips = new Chips(5000);
        botChips1 = new Chips(5000);
        botChips2 = new Chips(5000);

        add(chipsPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
        initializeButtons();
        initializeLabels();
        setupGame();

        revalidate();
        repaint();

        setVisible(true);
    }

    private void initializeButtons() {
        callButton = new JButton("Call");
        checkButton = new JButton("Check");
        raiseButton = new JButton("Raise");
        foldButton = new JButton("Fold");
        confirmButton = new JButton("Confirm");
        raiseSlider = new JSlider(JSlider.HORIZONTAL, 0, playerChips.getAmount(), 0);
        raiseSlider.setVisible(false);

        buttonPanel.add(callButton);
        buttonPanel.add(checkButton);
        buttonPanel.add(raiseButton);
        buttonPanel.add(foldButton);
        buttonPanel.add(confirmButton);

        confirmButton.setVisible(false); // Initially hide confirm button
        setupButtonListeners();

        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    private void initializeLabels() {
        raiseAmountLabel = new JLabel("Raise Amount: 0");
        raiseAmountLabel.setVisible(false); // Versetze das Setzen der Sichtbarkeit hierhin

        playerChipsLabel = new JLabel("Your Chips: " + playerChips.getAmount());
        botChipsLabel1 = new JLabel("Bot 1 Chips: " + botChips1.getAmount());
        botChipsLabel2 = new JLabel("Bot 2 Chips: " + botChips2.getAmount());

        chipsPanel.add(playerChipsLabel);
        chipsPanel.add(botChipsLabel1);
        chipsPanel.add(botChipsLabel2);

        addLabelsToFrame();
    }

    private void setupButtonListeners() {
        checkButton.addActionListener(e -> check());
        raiseButton.addActionListener(e -> {
            raiseSlider.setMaximum(playerChips.getAmount());
            raiseSlider.setVisible(true);
            raiseAmountLabel.setVisible(true);
            buttonPanel.add(raiseSlider); // Add the raiseSlider to the buttonPanel
            buttonPanel.add(raiseAmountLabel); // Add the raiseAmountLabel to the buttonPanel
            buttonPanel.revalidate(); // Inform the layout manager about the changes
            buttonPanel.repaint(); // Repaint the buttonPanel
            switchToConfirmButton();
        });
        foldButton.addActionListener(e -> fold());
        confirmButton.addActionListener(e -> {
            raise(raiseSlider.getValue());
            hasCurrentBet = true;
            switchToInitialButtons();
        });
        raiseSlider.addChangeListener(e -> raiseAmountLabel.setText("Raise Amount: " + raiseSlider.getValue()));
    }

    private void addLabelsToFrame() {
        buttonPanel.add(raiseSlider);
        buttonPanel.add(raiseAmountLabel);
    }

    private void setupGame() {
        dealCards(player);
        dealCards(dealer);
        dealCards(bot1);
        dealCards(bot2);
    }

    private void dealCards(Player p) {
        p.receiveCard(deck.drawCard());
        p.receiveCard(deck.drawCard());
    }

    private void check() {
        if (hasCurrentBet) {
            JOptionPane.showMessageDialog(this, "Cannot check, there is a bet. You must call, raise, or fold.");
        } else {
            // Proceed with the game
        }
    }

    private void raise(int amount) {
        playerChips.removeChips(amount);
        simulateBotActions();
    }

    private void fold() {
        JOptionPane.showMessageDialog(this, "You have folded.");
    }

    private void switchToConfirmButton() {
        buttonPanel.removeAll();
        buttonPanel.add(confirmButton);
        confirmButton.setVisible(true);
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    private void switchToInitialButtons() {
        buttonPanel.removeAll();
        buttonPanel.add(callButton);
        buttonPanel.add(checkButton);
        buttonPanel.add(raiseButton);
        buttonPanel.add(foldButton);
        raiseSlider.setVisible(false);
        raiseAmountLabel.setVisible(false);
        buttonPanel.revalidate();
        buttonPanel.repaint();
    }


    private void simulateBotActions() {
        Random random = new Random();
        int decision1 = random.nextInt(3);
        int decision2 = random.nextInt(3);
        botDecision(bot1, botChips1, decision1);
        botDecision(bot2, botChips2, decision2);
    }

    private void botDecision(Player bot, Chips chips, int decision) {
        switch (decision) {
            case 0 -> chips.removeChips(100);
            case 1 -> {}
            case 2 -> {}
        }
    }

    public static void main(String[] args) {
        new Poker();
    }
}
