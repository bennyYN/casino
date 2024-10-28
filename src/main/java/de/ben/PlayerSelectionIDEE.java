package de.ben;
import javax.swing.*;
import java.awt.*;

public class PlayerSelectionIDEE extends JFrame {

    private PokerGUI pokerGUI;

    public PlayerSelectionIDEE(PokerGUI pokerGUI) {
        //this.pokerGUI = pokerGUI;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Spieler Einstellungen");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel playerLabel = new JLabel("Wie viele Gegner? (2-10)");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(playerLabel, gbc);

        // Dropdown für Spieleranzahl (1 bis 9 Gegner)
        Integer[] playerOptions = {2, 3, 4, 5, 6, 7, 8, 9, 10};
        JComboBox<Integer> playerDropdown = new JComboBox<>(playerOptions);
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(playerDropdown, gbc);

        JButton confirmButton = new JButton("Namen eingeben");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(confirmButton, gbc);

        confirmButton.addActionListener(e -> {
            int numPlayers = (int) playerDropdown.getSelectedItem();
            enterPlayerNames(numPlayers);
        });

        add(panel);
        setVisible(true);
    }

    private void enterPlayerNames(int numPlayers) {
        JFrame nameFrame = new JFrame("Spielernamen eingeben");
        nameFrame.setSize(400, 300);
        nameFrame.setLayout(new GridLayout(numPlayers, 2));

        JTextField[] nameFields = new JTextField[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            JLabel nameLabel = new JLabel("Spieler " + (i + 1) + ":");
            nameFrame.add(nameLabel);
            nameFields[i] = new JTextField("Spieler " + (i + 1)); // Standardnamen vorgeben
            nameFrame.add(nameFields[i]);
        }

        JButton submitButton = new JButton("Namen bestätigen");
        nameFrame.add(submitButton);

        submitButton.addActionListener(e -> {
            String[] playerNames = new String[numPlayers];
            for (int i = 0; i < numPlayers; i++) {
                playerNames[i] = nameFields[i].getText();
                System.out.println("Spieler " + (i + 1) + " Name: " + playerNames[i]);
            }
            nameFrame.dispose();
            //Starten
        });

        nameFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PlayerSelectionIDEE(new PokerGUI()));
    }
}