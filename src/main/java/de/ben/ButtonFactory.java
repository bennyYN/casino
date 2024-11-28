package de.ben;

import javax.swing.*;
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
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

                // Setze die Farbe für den Text zurück
                g2.setColor(getForeground());
                // Zeichne den Text des Buttons
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textHeight = fm.getAscent();
                g2.drawString(getText(), (getWidth() - textWidth) / 2, (getHeight() + textHeight) / 2 - fm.getDescent());

                g2.dispose();
            }
        };

        // Add MouseListener to change border color on hover
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

        if(playingSounds){
            // Add an ActionListener
            button.addActionListener(e -> {
                MainGUI.playSound("click");
            });
        }

        // Erstelle einen zusammengesetzten Rand für den Button
        Border raisedbevel = BorderFactory.createRaisedBevelBorder();
        Border compound = BorderFactory.createCompoundBorder(raisedbevel, raisedbevel);
        button.setBorder(compound);

        // Setze die Schriftart und -größe des Buttons
        button.setFont(new Font("Arial", Font.BOLD, textSize));
        button.setForeground(Color.WHITE);
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFocusable(false);

        return button;
    }
}