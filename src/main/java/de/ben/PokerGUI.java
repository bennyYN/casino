package de.ben;

import de.ben.Poker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class PokerGUI extends JFrame {
    private Poker pokerGame;
    private JButton foldButton;
    private JButton checkButton;
    private JButton callButton;
    private JButton raiseButton;
    private JButton allInButton;
    private JTextField raiseAmountField;

    public PokerGUI() {
        pokerGame = new Poker(5000, 50);
        initComponents();
    }

    private void initComponents() {
        setTitle("Poker Game");
        setSize(1500, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(new BackgroundPanel("https://bennyyn.xyz/upload/img/backgrounding.png"));

        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        controlPanel.setOpaque(false);
        controlPanel.setLayout(new FlowLayout());

        foldButton = createPokerButton("Fold");
        checkButton = createPokerButton("Check");
        callButton = createPokerButton("Call");
        raiseButton = createPokerButton("Raise");
        allInButton = createPokerButton("All In");

        raiseAmountField = new JTextField(5);
        raiseAmountField.setFont(new Font("Arial", Font.PLAIN, 14));
        raiseAmountField.setForeground(Color.white);
        raiseAmountField.setOpaque(false);
        raiseAmountField.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        controlPanel.add(foldButton);
        controlPanel.add(checkButton);
        controlPanel.add(callButton);
        controlPanel.add(new JLabel("Raise Amount:"));
        controlPanel.add(raiseAmountField);
        controlPanel.add(raiseButton);
        controlPanel.add(allInButton);

        add(controlPanel, BorderLayout.SOUTH);

        foldButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleFold();
            }
        });

        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCheck();
            }
        });

        callButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCall();
            }
        });

        raiseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRaise();
            }
        });

        allInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleAllIn();
            }
        });
    }

    private void handleFold() {
        // Implement the fold action
        System.out.println("Player folded.");
    }

    private void handleCheck() {
        // Implement the check action
        System.out.println("Player checked.");
    }

    private void handleCall() {
        // Implement the call action
        System.out.println("Player called.");
    }

    private void handleRaise() {
        // Implement the raise action
        try {
            int raiseAmount = Integer.parseInt(raiseAmountField.getText());
            System.out.println("Player raised by " + raiseAmount + ".");
        } catch (NumberFormatException e) {
            System.out.println("Invalid raise amount.");
        }
    }

    private void handleAllIn() {
        // Implement the all-in action
        System.out.println("Player went all in.");
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PokerGUI().setVisible(true);
            }
        });
    }
}
