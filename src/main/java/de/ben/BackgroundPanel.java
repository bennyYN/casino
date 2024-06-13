package de.ben;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * A custom JPanel that paints a background image.
 */
public class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    /**
     * Constructs a BackgroundPanel with an image loaded from a specified URL.
     * @param imageUrl The URL of the background image.
     */
    public BackgroundPanel(String imageUrl) {
        try {
            // Create an ImageIcon to load the image
            backgroundImage = new ImageIcon(new URL(imageUrl)).getImage();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception or provide a fallback image
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image, scaling it to fit the entire panel
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
