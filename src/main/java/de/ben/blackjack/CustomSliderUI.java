package de.ben.blackjack;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;

public class CustomSliderUI extends BasicSliderUI {

    public CustomSliderUI(JSlider slider) {
        super(slider);
    }

    @Override
    protected Dimension getThumbSize() {
        return new Dimension(30, 30); // Increase the size of the thumb
    }

    @Override
    public void paintThumb(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLUE);
        g2d.fillOval(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);
    }

    @Override
    public void paintTrack(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.GRAY);
        g2d.fillRoundRect(trackRect.x, trackRect.y + trackRect.height / 2 - 5, trackRect.width, 10, 10, 10);
    }

    @Override
    public void paintFocus(Graphics g) {
        // Do nothing to remove the focus border
    }
}