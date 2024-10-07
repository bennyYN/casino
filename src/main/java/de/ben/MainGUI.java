package de.ben;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGUI extends JFrame implements ActionListener {

    //ATTRIBUTE
    Poker pokerGame;
    JButton startButton;
    JButton settingsButton;
    JButton exitButton;
    JPanel panel;

    //KONTRUKTOR
    public MainGUI(){

        this.setTitle("Casino");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        panel = new JPanel();
        this.add(panel);


        //BUTTONS
        startButton = new JButton("Start");
        startButton.setVisible(true);
        startButton.addActionListener(this);
        startButton.setSize(300, 40);
        this.panel.add(startButton);

        settingsButton = new JButton("Settings");
        settingsButton.setVisible(true);
        settingsButton.addActionListener(this);
        settingsButton.setSize(300, 40);
        this.panel.add(settingsButton);

        exitButton = new JButton("Exit");
        exitButton.setVisible(true);
        exitButton.addActionListener(this);
        exitButton.setSize(300, 40);
        this.panel.add(exitButton);

        this.setVisible(true);

    }

    public static void main(String[] args) {
        new MainGUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //BUTTON-EVENTS
        //START BUTTON
        if(e.getSource() == startButton){
            //openPokerGame
            System.out.println("Start");
        }
        //SETTINGS BUTTON
        if(e.getSource() == settingsButton){
            //openSettings
        }
        //EXIT BUTTON
        if(e.getSource() == exitButton){
            System.exit(0);
        }
    }

}
