package de.ben;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

public class PlayerSelectionIDEE extends JFrame {

    int numPlayers = 2;
    int startChips = 200;
    int bigBlind = 20;

    JLabel startChipsLabel;
    JLabel bigBlindLabel;
    ArrayList<String> playerNames;

    public PlayerSelectionIDEE() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Spieler Einstellungen");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        playerNames = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            playerNames.add("---");
        }

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel playerLabel = new JLabel("Anzahl der Spieler (2-10):");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(playerLabel, gbc);

        Integer[] playerOptions = {2, 3, 4, 5, 6, 7, 8, 9};
        JComboBox<Integer> playerDropdown = new JComboBox<>(playerOptions);
        playerDropdown.setSelectedItem(numPlayers);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(playerDropdown, gbc);

        playerDropdown.addActionListener(e -> numPlayers = (int) playerDropdown.getSelectedItem());
        playerDropdown.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                int newIndex = playerDropdown.getSelectedIndex() + notches;
                if (newIndex >= 0 && newIndex < playerDropdown.getItemCount()) {
                    playerDropdown.setSelectedIndex(newIndex);
                }
            }
        });

        startChipsLabel = new JLabel("Anzahl der Anfangschips (200-10000):  " + startChips);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(startChipsLabel, gbc);

        JSlider startChipsSlider = new JSlider(200, 10000, startChips);
        startChipsSlider.setMajorTickSpacing(2000);
        startChipsSlider.setPaintTicks(false);
        startChipsSlider.setPaintLabels(false);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(startChipsSlider, gbc);

        startChipsSlider.addChangeListener(e -> {
            startChips = startChipsSlider.getValue();
            startChipsLabel.setText("Anzahl der Anfangschips (200-10000):  " + startChips);
        });

        startChipsSlider.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                int value = startChipsSlider.getValue();
                startChipsSlider.setValue(value - notches);
            }
        });

        bigBlindLabel = new JLabel("Big Blind (20-2000):  " + bigBlind);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(bigBlindLabel, gbc);

        JSlider bigBlindSlider = new JSlider(20, 2000, bigBlind);
        bigBlindSlider.setMinorTickSpacing(20);
        bigBlindSlider.setMajorTickSpacing(400);
        bigBlindSlider.setPaintTicks(false);
        bigBlindSlider.setPaintLabels(false);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(bigBlindSlider, gbc);

        bigBlindSlider.addChangeListener(e -> {
            bigBlind = bigBlindSlider.getValue();
            if (bigBlind % 2 != 0) {
                bigBlind -= 1;
            }
            bigBlindLabel.setText("Big Blind (20-2000):  " + bigBlind);
            bigBlindSlider.setValue(bigBlind);
        });

        bigBlindSlider.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                int value = bigBlindSlider.getValue();
                value -= notches;
                value = Math.max(20, Math.min(2000, value));
                if (value % 2 != 0) {
                    value = (value > bigBlindSlider.getValue() ? value + 1 : value - 1);
                }

                bigBlindSlider.setValue(value);
            }
        });

        JButton confirmButton = new JButton("Namen eingeben");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(confirmButton, gbc);

        JButton exitButton = new JButton("Fortfahren");
        gbc.gridy = 7;
        panel.add(exitButton, gbc);

        confirmButton.addActionListener(e -> enterPlayerNames(numPlayers));

        exitButton.addActionListener(e -> {
            this.dispose();
            new PokerGUI(numPlayers, playerNames, startChips, bigBlind).setVisible(true);
        });

        add(panel);
        setVisible(true);
    }

    private void enterPlayerNames(int numPlayers) {
        JFrame nameFrame = new JFrame("Spielernamen eingeben");
        nameFrame.setSize(400, 300);
        nameFrame.setLocationRelativeTo(null);

        String[] columnNames = {"Spieler", "Name"};
        Object[][] data = new Object[numPlayers][2];

        for (int i = 0; i < numPlayers; i++) {
            data[i][0] = "Spieler " + (i + 1);
            data[i][1] = playerNames.get(i);
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        nameFrame.add(scrollPane, BorderLayout.CENTER);

        table.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                if (column == 1) {
                    String playerName = (String) table.getValueAt(row, column);
                    updatePlayerNames(row, playerName);
                }
            }
        });

        JButton submitButton = new JButton("Bestätigen");
        submitButton.addActionListener(e -> nameFrame.dispose());

        nameFrame.add(submitButton, BorderLayout.SOUTH);
        nameFrame.setVisible(true);
    }

    private void updatePlayerNames(int index, String newName) {
        playerNames.set(index, newName);
    }

    public static void main(String[] args) {
        new PlayerSelectionIDEE();
    }
}