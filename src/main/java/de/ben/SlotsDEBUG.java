package de.ben;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SlotsDEBUG extends JFrame {
    private JLabel[][] slotLabels = new JLabel[3][5];
    private JPanel slotsPanel;

    public SlotsDEBUG() {
        setTitle("Casino - SlotsDEBUG");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    URL backgroundImageUrl = new URL("https://bennyyn.xyz/upload/images/background.png");
                    BufferedImage backgroundImage = ImageIO.read(backgroundImageUrl);
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } catch (IOException e) {
                    e.printStackTrace();
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        contentPane.setLayout(new BorderLayout());

        slotsPanel = new JPanel(new GridLayout(3, 3));
        slotsPanel.setOpaque(false);
        slotLabels = new JLabel[3][3];

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                try {
                    URL imageUrl = new URL("https://bennyyn.xyz/upload/images/slot_image_" + row + "_" + col + ".png");
                    BufferedImage image = ImageIO.read(imageUrl);
                    ImageIcon icon = new ImageIcon(image);
                    slotLabels[row][col] = new JLabel(icon);
                    slotsPanel.add(slotLabels[row][col]);
                } catch (IOException e) {
                    e.printStackTrace();
                    slotLabels[row][col] = new JLabel("Slot " + row + "-" + col);
                    slotsPanel.add(slotLabels[row][col]);
                }
            }
        }

        contentPane.add(slotsPanel, BorderLayout.CENTER);


        JButton spinButton = new JButton("Spin");
        spinButton.setOpaque(true);
        spinButton.setBackground(Color.DARK_GRAY);
        spinButton.setForeground(Color.YELLOW);
        spinButton.setFont(new Font("Arial", Font.BOLD, 14));
        spinButton.setFocusable(false);
        spinButton.setFocusPainted(false);
        spinButton.setBorderPainted(false);
        spinButton.setBorderPainted(false);


        spinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shuffleSlots();
            }
        });

        spinButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                spinButton.setBackground(Color.GREEN);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                spinButton.setBackground(Color.DARK_GRAY);
            }
        });

        JButton backButton = new JButton("Back");
        backButton.setOpaque(true);
        backButton.setBackground(Color.DARK_GRAY);
        backButton.setForeground(Color.YELLOW);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setFocusable(false);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new MainWindowDEBUG();
            }
        });
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setBackground(Color.GREEN);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setBackground(Color.DARK_GRAY);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(spinButton);
        buttonPanel.add(backButton);
        buttonPanel.setOpaque(false);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);


        setContentPane(contentPane);

        setVisible(true);
    }

    private void shuffleSlots() {
        List<ImageIcon> images = new ArrayList<>();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                ImageIcon icon = (ImageIcon) slotLabels[row][col].getIcon();
                images.add(icon);
            }
        }
        Collections.shuffle(images);
        int index = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                slotLabels[row][col].setIcon(images.get(index++));
            }
        }
        if (checkWin()) {
            JOptionPane.showMessageDialog(this, "You won!");
        }
    }

    private boolean checkWin() {
        for (int row = 0; row < 3; row++) {
            if (checkRowWin(row)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkRowWin(int row) {
        String firstIconDescription = slotLabels[row][0].getIcon().toString();
        for (int col = 1; col < 5; col++) {
            if (!slotLabels[row][col].getIcon().toString().equals(firstIconDescription)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SlotsDEBUG::new);
    }
}
