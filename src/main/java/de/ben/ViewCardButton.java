package de.ben;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ViewCardButton extends JButton {

    static ArrayList<ViewCardButton> allButtons;

    public ViewCardButton(PokerGUI gui, int assignedPlayer, ArrayList<ViewCardButton> allButtons, int x, int y){
        super("View Cards");

        this.allButtons = allButtons;
        Color normalColor = new Color(255, 255, 255, 98); // Grau
        setFont(new Font("Arial", Font.PLAIN, 12)); // Schriftart und Größe
        setOpaque(false);
        setBorderPainted(false);
        setFocusPainted(false); // Fokusrand deaktivieren
        setBackground(normalColor);
        setForeground(Color.WHITE); // Gelber Text
        setBounds(x, y, 100, 12); // Größe setzen

        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gui.playerShowing = assignedPlayer;
                highlight(true);
            }
        });
        setVisible(true);
    }

    public void highlight(boolean state){
        if(state){
            for(ViewCardButton button : allButtons){
                button.highlight(false);
            }
            setFont(new Font("Arial", Font.BOLD, 12));
            setText("<HTML><U>View Cards</U></HTML>");
        }else{
            setFont(new Font("Arial", Font.PLAIN, 12));
            setText("View Cards");
        }
    }

}
