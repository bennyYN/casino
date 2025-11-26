package de.ben.poker;

import de.ben.MainGUI;
import de.ben.sound.Sound;
import de.ben.sound.SoundManager;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ButtonFactory {

    public static JButton getButton(String buttonText, Color color, int textSize, boolean playingSounds) {
        JButton button = new JButton(buttonText) {
            @Override
            protected void paintComponent(Graphics g) {
                // Aktiviert Anti-Aliasing für glattere Kanten und Text
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Zeichne einen transparenten Hintergrund
                g2.setComposite(AlphaComposite.Clear);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setComposite(AlphaComposite.SrcOver);

                // Ändere die Hintergrundfarbe, wenn der Button gedrückt wird
                if (getModel().isArmed()) {
                    g2.setColor(Color.LIGHT_GRAY);
                } else {
                    g2.setColor(color);
                }
                // Zeichne einen abgerundeten Hintergrund
                if(isEnabled()){
                    g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                }else{
                    // Set the stroke width
                    float strokeWidth = 3.0f;
                    g2.setStroke(new BasicStroke(strokeWidth));
                    g2.setColor(new Color(147, 147, 147, 131));
                    g2.drawRoundRect((int)(0 + strokeWidth/2), (int)(0 + strokeWidth/2), getWidth() - 1 - (int) strokeWidth, getHeight() - 1  - (int) strokeWidth, 20, 20);
                }

                // Setze die Farbe für den Text zurück
                if(isEnabled()){
                    g2.setColor(getForeground());
                }else{
                    g2.setColor(new Color(117, 117, 117, 152));
                }
                // Zeichne den Text des Buttons
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textHeight = fm.getAscent();
                g2.drawString(getText(), (getWidth() - textWidth) / 2, (getHeight() + textHeight) / 2 - fm.getDescent());

                g2.dispose();
            }

            @Override
            public void setEnabled(boolean enabled) {
                super.setEnabled(enabled);
                repaint();
            }
        };

        // Add MouseListener to change border color on hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if(button.isEnabled()){
                    button.setBorder(new RoundedBorder(20, 3, new Color(255, 255, 255, 87))); // 1 pixel wider
                    button.setBorderPainted(true);
                }else{
                    button.setBorder(new RoundedBorder(20, 1, new Color(161, 161, 161, 0))); // 1 pixel wider
                    button.setBorderPainted(false);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBorderPainted(false);
            }
        });

        if(playingSounds){
            // Add an ActionListener
            button.addActionListener(e -> {
                SoundManager.playSound(Sound.BUTTON_CLICK);
            });
        }

        // Erstelle einen zusammengesetzten Rand für den Button
        Border roundedBorder = new RoundedBorder(20, 3, new Color(255, 255, 255, 94)); // 1 pixel wider
        button.setBorder(roundedBorder);

        // Setze die Schriftart und -größe des Buttons
        button.setFont(new Font("Arial", Font.BOLD, textSize));
        button.setForeground(Color.WHITE);
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFocusable(false);

        return button;
    }

    static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final int thickness;
        private final Color color;

        public RoundedBorder(int radius, int thickness, Color color) {
            this.radius = radius;
            this.thickness = thickness;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(thickness, thickness, thickness, thickness);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = insets.top = insets.bottom = thickness;
            return insets;
        }
    }
}