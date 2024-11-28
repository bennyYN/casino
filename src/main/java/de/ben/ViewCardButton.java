package de.ben;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ViewCardButton extends JButton {

    static int soundVariationCounter = 1;
    static ArrayList<ViewCardButton> allButtons;
    int assignedPlayer;

    public ViewCardButton(PokerGUI gui, int assignedPlayer, ArrayList<ViewCardButton> allButtons, int x, int y){
        super("View Cards");

        this.assignedPlayer = assignedPlayer;
        this.allButtons = allButtons;
        Color normalColor = new Color(255, 255, 255, 98); // Grau
        setFont(new Font("Arial", Font.PLAIN, 12)); // Schriftart und Größe
        setOpaque(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setFocusPainted(false); // Fokusrand deaktivieren
        setFocusable(false); // Disable focus on the button
        setBackground(normalColor);
        setForeground(new Color(170, 170, 170)); // Gelber Text
        setBounds(x, y, 100, 12); // Größe setzen
        setContentAreaFilled(false);

        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if(soundVariationCounter == 1){
                    MainGUI.playSound("view1");
                    soundVariationCounter = 2;
                }else{
                    MainGUI.playSound("view2");
                    soundVariationCounter = 1;
                }

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
            //set text to underlined & bold for the player currently showing cards
            setFont(new Font("Arial", Font.BOLD, 12));
            setText("<HTML><U>View Cards</U></HTML>");
            setForeground(Color.WHITE);
        }else{
            setForeground(new Color(163, 163, 163));
            setFont(new Font("Arial", Font.PLAIN, 12));
            setText("View Cards");
        }
    }

    public static void setWinner(int winnerIndex){
        for(ViewCardButton button : allButtons){
            if(button.assignedPlayer == winnerIndex){
                button.highlight(true);
            }
        }
    }

    public static void setAllVisible(boolean state){
        for(ViewCardButton button : allButtons){
            button.setVisible(state);
        }
    }

}
