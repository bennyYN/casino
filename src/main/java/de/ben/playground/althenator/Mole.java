package de.ben.playground.althenator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Mole {
    private JButton button;
    private Random random;
    private MoleGame game;
    private static final Image MOLE_IMAGE = new ImageIcon("img/playground/althenator/rausgeflogene_sicherung.png").getImage();
    private static final Image NO_MOLE_IMAGE = new ImageIcon("img/playground/althenator/sicherung.png").getImage();
    public Image usedImage = NO_MOLE_IMAGE;

    public Mole(MoleGame game) {
        this.game = game;
        button = new JButton();
        button.setPreferredSize(new Dimension(96, 256)); // Set button size to 96x256
        button.setContentAreaFilled(false); // Make the button non-opaque
        button.setBorderPainted(false); // Remove the border
        button.setFocusPainted(false); // Remove the focus paint
        button.setOpaque(false); // Ensure the button is fully transparent
        button.setVisible(false); // Initially set the button to be invisible
        random = new Random();
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!button.isEnabled()) {
                    game.cursorState = "electrified_active";
                } else {
                    button.setVisible(false);
                    usedImage = NO_MOLE_IMAGE;
                    game.increaseScore();
                    hide();
                    game.triggerCursorStateChange();
                }
            }
        });
    }

    public JButton getButton() {
        return button;
    }

    public void appear() {
        button.setVisible(true);
        button.setEnabled(true);
        usedImage = MOLE_IMAGE;
        game.repaint();
    }

    public void hide() {
        button.setVisible(false);
        button.setEnabled(false);
        button.setIcon(null); // Remove the icon when hidden
        game.repaint();
    }

    public boolean isVisible() {
        return button.isVisible();
    }
}