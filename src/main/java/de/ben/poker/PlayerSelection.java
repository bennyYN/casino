package de.ben.poker;

import de.ben.ImageArchive;
import de.ben.MainGUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class PlayerSelection extends JFrame {

    int numPlayers = 2;
    int actualPlayerCount = 0;
    int startChips = 200;
    int bigBlind = 20;
    MainGUI mainGUI;
    JLabel startChipsLabel;
    JLabel bigBlindLabel;
    JTextField startChipsField;
    JTextField bigBlindField;
    ArrayList<String> playerNames;
    JButton exitButton, confirmButton;

    public PlayerSelection(MainGUI mainGUI) {
        this.mainGUI = mainGUI;
        initializeUI();
        checkPlayerNames();
    }

    private void initializeUI() {
        setTitle("Spieler Einstellungen");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //Titlebar-Icon mit Skalierung setzen
        ImageIcon icon = new ImageIcon("img/icon.png");
        Image scaledIcon = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH); // glatte Skalierung
        setIconImage(scaledIcon);

        playerNames = new ArrayList<>(8);
        for (int i = 0; i < 8; i++) {
            playerNames.add("Spieler " + (i + 1));
        }

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(ImageArchive.getImage("background:"+mainGUI.getSelectedTheme()), 0, 0, null);
                if(exitButton != null){
                    exitButton.setOpaque(exitButton.isEnabled());
                    mainGUI.updateButtonColor(exitButton, false);
                }
                if(confirmButton != null){
                    mainGUI.updateButtonColor(confirmButton, false);
                }
                repaint();
            }
        };
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel playerLabel = new JLabel("Anzahl der Spieler (2-8):");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        playerLabel.setForeground(Color.WHITE);
        panel.add(playerLabel, gbc);

        Integer[] playerOptions = {2, 3, 4, 5, 6, 7, 8};
        JComboBox<Integer> playerDropdown = new JComboBox<>(playerOptions);
        playerDropdown.setSelectedItem(numPlayers);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        playerDropdown.setOpaque(false);
        panel.add(playerDropdown, gbc);

        playerDropdown.addActionListener(e -> {
            numPlayers = (int) playerDropdown.getSelectedItem();
            checkPlayerNames();
        });
        playerDropdown.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = -e.getWheelRotation();
                int newIndex = playerDropdown.getSelectedIndex() + notches;
                if (newIndex >= 0 && newIndex < playerDropdown.getItemCount()) {
                    playerDropdown.setSelectedIndex(newIndex);
                }
            }
        });

        startChipsLabel = new JLabel("Anzahl der Anfangschips (200-10000):");
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
            startChipsLabel.setText("Anzahl der Anfangschips (200-10000):");
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
                value -= notches * 20; // Increase step size to 100
                value = Math.max(20, Math.min(2000, value));
                if (value % 2 != 0) {
                    value = (value > bigBlindSlider.getValue() ? value + 1 : value - 1);
                }
                bigBlindSlider.setValue(value);
            }

        });

        confirmButton = new JButton("Namen eingeben");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        confirmButton.setBackground(new Color(78, 136, 174, 255));
        confirmButton.setForeground(Color.WHITE);
        styleButton(confirmButton);
        panel.add(confirmButton, gbc);

        exitButton = new JButton("Fortfahren");
        exitButton.setEnabled(false); // Disable the button by default
        exitButton.setBackground(new Color(78, 136, 174, 255));
        exitButton.setForeground(Color.WHITE);
        styleButton(exitButton);
        gbc.gridy = 7;
        panel.add(exitButton, gbc);

        confirmButton.addActionListener(e -> {
            //TODO: MIGRATE -> MainGUI.playSound("click");
            enterPlayerNames(numPlayers);
        });

        exitButton.addActionListener(e -> {
            //TODO: MIGRATE -> MainGUI.playSound("click");
            for(int i = 0; i < 8; i++){
                if(i >= numPlayers){
                    playerNames.set(i, "");
                }
            }
            new PokerGUI(numPlayers, playerNames, startChips, bigBlind, mainGUI).setVisible(true);
            this.dispose();
        });

        add(panel);
        setVisible(true);
    }

    // Methode, um den Button zu stylen
    private void styleButton(JButton button) {
        button.setBackground(new Color(78, 136, 174, 255));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(150, 40)); // Größe setzen
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        /*button.addActionListener(e -> {
            mainGUI.playSound("click");
        });*/

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

    private void enterPlayerNames(int numPlayers) {
        JFrame nameFrame = new JFrame("Spielernamen eingeben");
        nameFrame.setSize(400, 300);
        nameFrame.setLocationRelativeTo(null);

        String[] columnNames = {"Spieler", "Name"};
        Object[][] data = new Object[numPlayers][2]; // Create rows based on numPlayers

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

        JTable table = new JTable(model){
            @Override
            public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
                if (columnIndex != 0) {
                    super.changeSelection(rowIndex, columnIndex, toggle, extend);
                }
            }

            @Override
            protected void processKeyEvent(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB && getSelectedRow() == getRowCount() - 1 && getSelectedColumn() == 1) {
                    changeSelection(0, 1, false, false);
                    e.consume();
                } else {
                    super.processKeyEvent(e);
                }
            }
        };

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (column == 1 && value.equals("Spieler " + (row + 1))) {
                    c.setForeground(Color.LIGHT_GRAY);
                } else {
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        nameFrame.add(scrollPane, BorderLayout.CENTER);

        table.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                if (column == 1) {
                    String playerName = (String) table.getValueAt(row, column);
                    updatePlayerNames(row, playerName);
                    checkPlayerNames(); // Check player names after each update
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

    private void checkPlayerNames() {
        boolean allNamesEntered = true;
        for (int i = 0; i < numPlayers; i++) {
            if (playerNames.get(i).trim().equals("")) {
                allNamesEntered = false;
                break;
            }
        }
        exitButton.setEnabled(allNamesEntered);
    }
}