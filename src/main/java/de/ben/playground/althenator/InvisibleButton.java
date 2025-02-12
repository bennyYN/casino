package de.ben.playground.althenator;

import javax.swing.*;
import java.awt.*;

public class InvisibleButton extends JButton {
    public InvisibleButton(Icon icon) {
        super(icon);
        setVisible(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if(isEnabled() || isVisible()) {

        }else{
            super.paintComponent(g);
        }
    }
}