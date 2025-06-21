package de.ben.blackjack;

import de.ben.ImageArchive;
import de.ben.MainGUI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class BlackJackGUI extends JFrame implements KeyListener {

    public boolean seperatedBevels = false;
    JSlider betSlider;
    JTextField betField;
    JButton confirmButton, restartButton;
    JPanel panel;
    MainGUI mainGUI;
    int startChips, bet = 10;
    boolean playedFlipSound = false;
    public JButton menuexitButton, menusettingsButton, menuMainMenuButton, menuContinueButton; //Pausemenü-Buttons
    public JButton standButton, hitButton, doubleDownButton, splitButton, menuButton, exitButton, continueButton; //Verschiedene Buttons
    public boolean isMenuOpen = false; //Boolean-Variable, ob das Menü geöffnet ist
    ArrayList<JButton> menuButtons = new ArrayList<JButton>(); //ArrayList für die Pausemenü-Buttons
    private String action = "idle"; //String-Variable für die Aktion des Spielers
    public String state = "betting";
    BlackJack bj;
    JLabel stateLabel, playerCardsValueLabel, splitPlayerCardsValueLabel1, splitPlayerCardsValueLabel2, dealerCardsValueLabel, splitStateLabel1, SplitStateLabel2; // Label to show the current state of the game
    boolean selectedAction = false;
    int flipframe = 0;
    int seperationFrame = 0;

    public BlackJackGUI(int startChips, MainGUI mainGUI) {

        new AnimationManager().start();
        this.mainGUI = mainGUI;
        this.startChips = startChips;
        bj = new BlackJack(mainGUI, startChips, this);

        //Liste für die Pausemenü-Buttons
        menuButtons.add(menuexitButton);
        menuButtons.add(menusettingsButton);
        menuButtons.add(menuMainMenuButton);
        menuButtons.add(menuContinueButton);

        //JFrame-Einstellungen
        setTitle("Blackjack"); //Fenster Titel zuweisen
        setSize(1200, 850); //Größe des Fensters setzen
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Das Programm schließen, wenn das Fenster geschlossen wird
        setLocationRelativeTo(null); //Das Fenster in der Mitte des Bildschirms platzieren
        addKeyListener(this); //KeyListener hinzufügen
        setFocusable(true); //Das JFrame fokussierbar machen um KeyEvents zu empfangen
        //Titlebar-Icon
        setIconImage(ImageArchive.getImage("icon"));

        //JPanel erstellen
        panel = new JPanel() {
            @Override //Überschreiben der Methode paintComponent um auf das Panel zu zeichnen
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); //Methode der Superklasse aufrufen

                //Convert the graphics to Graphics2D to allow more advanced rendering
                Graphics2D g2d = (Graphics2D) g;

                bj.setGraphics(g);

                // Set rendering hints for high-quality image rendering
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                //Hintergrundbild zeichnen
                g2d.drawImage(ImageArchive.getImage("background:" + mainGUI.getSelectedTheme()), 0, 0, null);

                //Bevels zeichnen
                renderBevels(g2d);

                //Karten zeichnen
                if(!state.equals("betting")){
                    renderPlayerCards(g2d);
                    renderDealerCards(g2d);
                }

                //Aktuellen Spielstatus oben links zeichnen
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 18));
                //if(state.equals("betting")||state.equals("active")){
                if(!bj.hasSplit){
                    //Normaler Infobevel
                    g.drawImage(new ImageIcon("src/main/resources/img/infobevel.png").getImage(), 10, 10, null);
                    //Chips
                    if(state.equals("active")){
                        g2d.drawString("Chips: " + (bj.chips-(bj.bet)), 23, 40);
                    }else{
                        g2d.drawString("Chips: " + bj.chips, 23, 40);
                        g2d.setColor(new Color(202, 202, 202, 100));
                    }
                    //Einsatz
                    g2d.drawString("Einsatz: " + (bj.bet), 23, 80);
                }else{
                    //Erweiterter Infobevel
                    g.drawImage(new ImageIcon("src/main/resources/img/extended_infobevel.png").getImage(), 10, 10, null);
                    //Chips
                    if(state.equals("active")){
                        g2d.drawString("Chips: " + (bj.chips-((bj.bet1)+(bj.bet2))), 23, 40);
                    }else{
                        g2d.drawString("Chips: " + bj.chips, 23, 40);
                        g2d.setColor(new Color(202, 202, 202, 100));
                    }
                    //Einsatz
                    g2d.drawString("Einsatz:", 23, 90);
                    g2d.drawString(" Hand 1:  " + (bj.bet1), 23, 115);
                    g2d.drawString(" Hand 2:  " + (bj.bet2), 23, 140);
                    g2d.drawString(" ----------", 23, 158);
                    g2d.drawString(" Gesamt:  " + ((bj.bet1)+(bj.bet2)), 23, 180);
                }
                //}
                g2d.setColor(Color.WHITE);



                BlackJackGUI.this.update();

                if(isMenuOpen){
                    //Abtönungsschicht zeichnen
                    g.setColor(new Color(0, 0, 0, 110));
                    g.fillRect(0, 0, 1200, 850);
                    //Infotext, dass das Spiel pausiert ist
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("Arial", Font.BOLD, 50));
                    g.drawString("Spiel Pausiert", 425, 120);
                }

                repaint(); //Das Panel neu zeichnen
            }
        };

        panel.setLayout(null);

        //Add stuff
        add(panel);
        setVisible(true);

        //Buttons für das Menü:
        //Erstellung der Buttons für das Pausemenü
        for(int i = 0; i < 4; i++){
            String tempButtonText = "[text not set]";
            switch(i){
                case 0:
                    tempButtonText = "Back to Game";
                    break;
                case 1:
                    tempButtonText = "Settings";
                    break;
                case 2:
                    tempButtonText = "Mainmenu";
                    break;
                case 3:
                    tempButtonText = "Leave Game";
                    break;
            }
            menuButtons.set(i, ButtonFactory.getButton(tempButtonText, new Color(151, 217, 255, 89), 20, true));

            //Pausemenü-Button Positionen
            int buttonWidth = 225;
            int buttonHeight = 70;
            int yPosition = 700;
            int spacing = 30;
            int startX = ((1200 - (140 * 6 + spacing * 5)) / 2) - 6;
            menuButtons.get(i).setBounds(startX + (buttonWidth + spacing) * i, yPosition, buttonWidth, buttonHeight);
            menuButtons.get(i).setVisible(false);
            panel.add(menuButtons.get(i));
        }

        continueButton = ButtonFactory.getButton("Continue", new Color(142, 215, 255, 81), 20, true);
        confirmButton = ButtonFactory.getButton("Confirm", new Color(142, 215, 255, 81), 20, true);
        restartButton = ButtonFactory.getButton("Restart", new Color(142, 215, 255, 81), 20, true);
        hitButton = ButtonFactory.getButton("Hit", new Color(142, 215, 255, 81), 20, true);
        standButton = ButtonFactory.getButton("Stand", new Color(142, 215, 255, 81), 20, true);
        doubleDownButton = ButtonFactory.getButton("Double Down", new Color(142, 215, 255, 81), 20, true);
        splitButton = ButtonFactory.getButton("Split", new Color(142, 215, 255, 81), 20, true);
        menuButton = ButtonFactory.getButton("Menu", new Color(151, 217, 255, 89), 20, true);
        exitButton = ButtonFactory.getButton("Menu", new Color(151, 217, 255, 89), 20, true);

        panel.add(continueButton);
        panel.add(confirmButton);
        panel.add(hitButton);
        panel.add(standButton);
        panel.add(doubleDownButton);
        panel.add(splitButton);
        panel.add(menuButton);
        panel.add(exitButton);
        panel.add(restartButton);

        continueButton.setVisible(false);
        confirmButton.setVisible(false);
        hitButton.setVisible(false);
        standButton.setVisible(false);
        doubleDownButton.setVisible(false);
        splitButton.setVisible(false);
        menuButton.setVisible(false);
        exitButton.setVisible(false);

        // Initialisiere die Labels und füge sie zum Panel hinzu
        splitPlayerCardsValueLabel1 = new JLabel("");
        splitPlayerCardsValueLabel2 = new JLabel("");

        splitPlayerCardsValueLabel1.setForeground(Color.WHITE);
        splitPlayerCardsValueLabel1.setFont(new Font("Arial", Font.BOLD, 24));
        splitPlayerCardsValueLabel1.setBounds((1200 - 600) / 2 - 275, 450, 600, 50); // Setze die Position und Größe des Labels
        panel.add(splitPlayerCardsValueLabel1);

        splitPlayerCardsValueLabel2.setForeground(Color.WHITE);
        splitPlayerCardsValueLabel2.setFont(new Font("Arial", Font.BOLD, 24));
        splitPlayerCardsValueLabel2.setBounds((1200 - 600) / 2 + 260, 450, 600, 50); // Setze die Position und Größe des Labels
        panel.add(splitPlayerCardsValueLabel2);

        //Hauptmenü Button, für wenn das Spiel zuende ist
        menuButton.addActionListener(e -> SwingUtilities.invokeLater(() -> {
            mainGUI.setVisible(true); //Das Hauptmenü im Hintergrund sichtbar machen
            dispose(); //Die Poker-Benutzeroberfläche schließen
        }));
        //Fortfahren Button um das Menü zu schließen und die aktuelle Runde fortzusetzen
        menuButtons.get(0).addActionListener(e -> isMenuOpen = false);

        //Settings Button um ein Fenster für Spieleinstellungen zu öffnen
        menuButtons.get(1).addActionListener(e -> {
            SettingsGUI settings = new SettingsGUI(mainGUI, false); //Einstellungsfenster öffnen
        });

        //Hauptmenü Button um das aktuelle Spiel zu schließen und zum Hauptmenü zurückzukehren
        menuButtons.get(2).addActionListener(e -> {
            dispose(); // Schließe PokerGUI
            mainGUI.setVisible(true); // Zeige MainGUI wieder an
        });

        //Exit Button im Hauptmenü um das Programm zu schließen
        menuButtons.get(3).addActionListener(e -> {
            System.exit(0); // Beende das Programm
        });

        continueButton.addActionListener(e -> {
            updateSlider();
            dealerCardsValueLabel.setForeground(Color.WHITE);
            dealerCardsValueLabel.setText("");
            playerCardsValueLabel.setText("");
            playerCardsValueLabel.setForeground(Color.WHITE);
            splitPlayerCardsValueLabel1.setText("");
            splitPlayerCardsValueLabel2.setText("");
            splitPlayerCardsValueLabel1.setForeground(Color.WHITE);
            splitPlayerCardsValueLabel2.setForeground(Color.WHITE);
            state = "betting";
            continueButton.setVisible(false);
            exitButton.setVisible(false);
            confirmButton.setVisible(true);
            betSlider.setVisible(true);
            betField.setVisible(true);
            action = "c";
            updateSlider();
        });

        confirmButton.addActionListener(e -> {
            dealerCardsValueLabel.setForeground(Color.WHITE);
            dealerCardsValueLabel.setText("");
            playerCardsValueLabel.setText("");
            playerCardsValueLabel.setForeground(Color.WHITE);
            splitPlayerCardsValueLabel1.setText("");
            splitPlayerCardsValueLabel2.setText("");
            splitPlayerCardsValueLabel1.setForeground(Color.WHITE);
            splitPlayerCardsValueLabel2.setForeground(Color.WHITE);
            playedFlipSound = false;

            state = "active";
            selectedAction = false;
            betField.setVisible(false);
            betSlider.setVisible(false);
            confirmButton.setVisible(false);
            bj.bet = bet;
            bj.betConfirmed = true;
        });

        restartButton.addActionListener(e -> {
            new GameSettings(mainGUI);
            this.dispose(); // Verstecke das Fenster, anstatt es zu schließen
        });

        hitButton.addActionListener(e -> {
            action = "h";
            selectedAction = true;
        });

        standButton.addActionListener(e -> {
            action = "s";
            selectedAction = true;
        });

        doubleDownButton.addActionListener(e -> {
            action = "d";
            selectedAction = true;
        });

        splitButton.addActionListener(e -> {
            action = "p";
            selectedAction = true;
        });

        exitButton.addActionListener(e -> {
            dispose();
            mainGUI.setVisible(true);
        });


        JLabel betLabel = new JLabel("Einsatz:");
        betLabel.setForeground(Color.WHITE);
        betLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(betLabel);

        betSlider = new JSlider(10, startChips, 10);
        betSlider.setMajorTickSpacing(2000);
        betSlider.setPaintTicks(false);
        betSlider.setPaintLabels(false);
        betSlider.setVisible(false);
        betSlider.setOpaque(false);
        betSlider.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(betSlider);

        betField = new JTextField(String.valueOf(10), 5);
        betField.setOpaque(false);
        betField.setForeground(Color.WHITE);
        betField.setCaretColor(new Color(174, 174, 174));
        betField.setVisible(false);
        betField.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(betField);

        // Set bounds for continueButton and exitButton at the bottom
        int buttonWidth = 300;
        int buttonHeight = 100;
        int spacing = 50;
        int bottomY = 850 - buttonHeight - 50; // 50 pixels from the bottom
        int centerX = (1200 - (buttonWidth * 2 + spacing)) / 2;

        continueButton.setBounds(centerX, bottomY, buttonWidth, buttonHeight);
        restartButton.setBounds(centerX, bottomY, buttonWidth, buttonHeight);
        exitButton.setBounds(centerX + buttonWidth + spacing, bottomY, buttonWidth, buttonHeight);

        // Set bounds for betSlider, betField, and confirmButton above the buttons
        int sliderWidth = buttonWidth * 2 + spacing;
        int sliderY = bottomY - buttonHeight - spacing;
        betSlider.setBounds(centerX, sliderY, sliderWidth - 150, buttonHeight);
        betField.setBounds(centerX + sliderWidth - 150, sliderY, 150, buttonHeight);
        confirmButton.setBounds(centerX, sliderY - buttonHeight - spacing, sliderWidth, buttonHeight);

        // Set bounds for game action buttons with spacing
        int actionButtonWidth = buttonWidth / 2 + 20; // Make buttons wider
        int actionButtonHeight = buttonHeight-10;
        int actionButtonY = 710;
        int actionButtonSpacing = 25; // Add spacing between action buttons

        hitButton.setBounds(centerX, actionButtonY, actionButtonWidth - actionButtonSpacing, actionButtonHeight);
        standButton.setBounds(centerX + actionButtonWidth, actionButtonY, actionButtonWidth - actionButtonSpacing, actionButtonHeight);
        doubleDownButton.setBounds(centerX + actionButtonWidth * 2, actionButtonY, actionButtonWidth - actionButtonSpacing, actionButtonHeight);
        splitButton.setBounds(centerX + actionButtonWidth * 3, actionButtonY, actionButtonWidth - actionButtonSpacing, actionButtonHeight);

        // Style buttons
        styleButton(continueButton);
        styleButton(exitButton);
        styleButton(confirmButton);
        styleButton(hitButton);
        styleButton(standButton);
        styleButton(doubleDownButton);
        styleButton(splitButton);
        styleButton(restartButton);

        // Add components to panel
        panel.add(continueButton);
        panel.add(exitButton);
        panel.add(confirmButton);
        panel.add(restartButton);
        panel.add(hitButton);
        panel.add(standButton);
        panel.add(doubleDownButton);
        panel.add(splitButton);
        panel.add(betSlider);
        panel.add(betField);
        betSlider.setUI(new CustomSliderUI(betSlider)); // Apply custom UI

        // Initially hide components
        continueButton.setVisible(false);
        restartButton.setVisible(false);
        exitButton.setVisible(false);
        confirmButton.setVisible(false);
        hitButton.setVisible(false);
        standButton.setVisible(false);
        doubleDownButton.setVisible(false);
        splitButton.setVisible(false);
        betSlider.setVisible(false);
        betField.setVisible(false);

        betSlider.addChangeListener(e -> {
            updateSlider();
        });

        betField.addActionListener(e -> {
            int value = Integer.parseInt(betField.getText());
            value = Math.max(200, Math.min(bj.chips, value));
            betSlider.setValue(value);
        });

        betSlider.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                int value = betSlider.getValue();
                betSlider.setValue(value - notches * 100); // Increase step size to 100
            }
        });

        // Add state label at the top
        stateLabel = new JLabel("State: " + state, SwingConstants.CENTER);
        stateLabel.setForeground(Color.WHITE);
        stateLabel.setFont(new Font("Arial", Font.BOLD, 36));
        stateLabel.setBounds((1200 - 600) / 2, 10, 600, 50); // Increase width to 600
        panel.add(stateLabel);

        // Split State Label 1
        splitStateLabel1 = new JLabel("Split1");
        splitStateLabel1.setForeground(Color.WHITE);
        splitStateLabel1.setFont(new Font("Arial", Font.BOLD, 24));
        splitStateLabel1.setBounds(75, 260, 500, 265); // Set position and size of the label
        splitStateLabel1.setVisible(false);
        panel.add(splitStateLabel1);

        // Split State Label 2
        SplitStateLabel2 = new JLabel("Split2");
        SplitStateLabel2.setForeground(Color.WHITE);
        SplitStateLabel2.setFont(new Font("Arial", Font.BOLD, 24));
        SplitStateLabel2.setBounds(625, 260, 500, 265); // Set position and size of the label
        SplitStateLabel2.setVisible(false);
        panel.add(SplitStateLabel2);

        // Add player cards total value label
        playerCardsValueLabel = new JLabel("");
        playerCardsValueLabel.setForeground(Color.WHITE);
        playerCardsValueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        playerCardsValueLabel.setBounds((1200 - 600) / 2, 450, 600, 50); // Increase width to 600
        panel.add(playerCardsValueLabel);

        // Add dealer cards total value label
        dealerCardsValueLabel = new JLabel("");
        dealerCardsValueLabel.setForeground(Color.WHITE);
        dealerCardsValueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        dealerCardsValueLabel.setBounds((1200 - 600) / 2, 320, 600, 50); // Increase width to 600
        panel.add(dealerCardsValueLabel);

        //Spiel in neuem Thread starten
        new Thread(bj::startGame).start();
    }

    private void renderBevels(Graphics2D g2d){
        //Bevels zeichnen
        if(!state.equals("betting")) {
            //Dealerbevel
            g2d.setColor(new Color(0, 0, 0, 69));
            g2d.fillRoundRect(300, 80, 600, 280, 20, 20);
            if(!bj.hasSplit){
                if(!bj.isSplitting){
                    //Nicht gesplitteter Spielerbevel
                    g2d.fillRoundRect(300, 420, 600, 265, 20, 20);
                }else{
                    if(seperationFrame < 100){
                        //Animation: Splittenden Bevels
                        //1 x(300->75) & w(600->500)
                        g2d.fillRoundRect(interpolate(300, 75), 420, interpolate(300, 500), 265, 20, 20);
                        //2
                        g2d.fillRoundRect(interpolate(600, 625), 420, interpolate(300, 500), 265, 20, 20);

                        try{
                            Thread.sleep(1);
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }
                        seperationFrame = seperationFrame + 4;
                    }else{
                        //Gesplittete Spielerbevel zeichnen
                        //1
                        g2d.fillRoundRect(75, 420, 500, 265, 20, 20);
                        //2
                        g2d.fillRoundRect(625, 420, 500, 265, 20, 20);
                        seperatedBevels = true;
                    }
                }
            }else{
                //Gesplittete Spielerbevel zeichnen
                //1
                g2d.fillRoundRect(75, 420, 500, 265, 20, 20);
                //2
                g2d.fillRoundRect(625, 420, 500, 265, 20, 20);
            }

            //Bevel Umrandung bei Spieler/Dealer bei Turn zeichnen
            g2d.setColor(new Color(255, 255, 255, (int)(AnimationManager.getAlpha())));
            g2d.setStroke(new BasicStroke(5));
            if(bj.playerTurn){
                if(!bj.hasSplit){
                    if(!bj.isSplitting){
                        g2d.drawRoundRect(300, 420, 600, 265, 20, 20);
                    }else{
                        g2d.drawRoundRect(interpolate(300, 75), 420, interpolate(600, 500), 265, 20, 20);
                    }
                }else{
                    //Gerade ausgewählter Bevel der gesplitteten Spielerhand zeigen
                    if(bj.selectedLeftSplitHand){
                        g2d.drawRoundRect(75, 420, 500, 265, 20, 20);
                        seperationFrame = 0;
                    }else{
                        g2d.drawRoundRect(625, 420, 500, 265, 20, 20);
                        seperationFrame = 0;
                    }
                }
            }else{
                g2d.drawRoundRect(300, 80, 600, 280, 20, 20);
            }
        }
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(78, 136, 174, 255));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(300, 100)); // Größe setzen
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        Border thinBorder = BorderFactory.createLineBorder(new Color(255, 255, 255, 81), 2); // 1 pixel thick
        button.setBorder(thinBorder);

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
    }

    private void updateSlider(){
        betSlider.setMaximum(bj.chips);
        if(state.equals("betting")) {
            bet = betSlider.getValue();
            betField.setText(String.valueOf(bet));
            bj.bet = bet;
            bj.bet = Math.min(bj.bet, bj.chips);
        }
    }

    private void update() {


        splitStateLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        SplitStateLabel2.setHorizontalAlignment(SwingConstants.CENTER);

        if (!bj.playerTurn) {
            hitButton.setEnabled(false);
            standButton.setEnabled(false);
            doubleDownButton.setEnabled(false);
            splitButton.setEnabled(false);
        } else {
            if(!bj.hasSplit){
                int enteredPlayerCards = 0;
                for(Card card : bj.playerHand.cards){
                    if(card.entered){
                        enteredPlayerCards++;
                    }
                }
                if(enteredPlayerCards == bj.playerHand.cards.size() && !bj.dealerHand.cards.get(1).flipping){
                    hitButton.setEnabled(true);
                    standButton.setEnabled(true);
                    doubleDownButton.setEnabled(bj.chips >= 2*bet && bj.canDoubleDown); // Double Down requires at least double the bet
                }else{
                    hitButton.setEnabled(false);
                    standButton.setEnabled(false);
                    doubleDownButton.setEnabled(false);
                }
            }else{
                int enteredPlayerCards = 0;
                for(Card card : bj.splitHand1.cards){
                    if(card.entered){
                        enteredPlayerCards++;
                    }
                }
                for(Card card : bj.splitHand2.cards){
                    if(card.entered){
                        enteredPlayerCards++;
                    }
                }
                if(enteredPlayerCards == (bj.splitHand1.cards.size()+bj.splitHand2.cards.size()) && !bj.dealerHand.cards.get(1).flipping){
                    hitButton.setEnabled(true);
                    standButton.setEnabled(true);
                    doubleDownButton.setEnabled(bj.chips >= bj.bet * 2); // Double Down requires at least double the bet
                }else{
                    hitButton.setEnabled(false);
                    standButton.setEnabled(false);
                    doubleDownButton.setEnabled(false);
                }
            }
            splitButton.setEnabled(!bj.hasSplit && bj.canSplit && bj.chips >= bj.bet*2 && (bj.playerHand.cards.size()==2) && !bj.isSplitting); // Split requires at least double the bet
        }
        if (bj != null) {
            betSlider.setMaximum(bj.chips);
            if (bj.dealerHand != null) {
                if (bj.dealerCardIsHidden) {
                    dealerCardsValueLabel.setText(String.valueOf(bj.dealerHand.cards.get(0).getValue()) + " + ?");
                }
                dealerCardsValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
            }
        }

        if (isMenuOpen) {
            for (int i = 0; i < 4; i++) {
                menuButtons.get(i).setVisible(true);
            }
            betSlider.setVisible(false);
            betField.setVisible(false);
            confirmButton.setVisible(false);
            exitButton.setVisible(false);
            continueButton.setVisible(false);
            stateLabel.setVisible(false);
            hitButton.setVisible(false);
            standButton.setVisible(false);
            doubleDownButton.setVisible(false);
            splitButton.setVisible(false);
            //Labels abtönen im Hintergrund vom Menu
            dealerCardsValueLabel.setForeground(dealerCardsValueLabel.getForeground().darker());
            playerCardsValueLabel.setForeground(playerCardsValueLabel.getForeground().darker());
            splitPlayerCardsValueLabel1.setForeground(splitPlayerCardsValueLabel1.getForeground().darker());
            splitPlayerCardsValueLabel2.setForeground(splitPlayerCardsValueLabel2.getForeground().darker());
            splitStateLabel1.setForeground(splitPlayerCardsValueLabel1.getForeground().darker());
            SplitStateLabel2.setForeground(splitPlayerCardsValueLabel1.getForeground().darker());
        } else {
            for (int i = 0; i < 4; i++) {
                menuButtons.get(i).setVisible(false);
            }
            stateLabel.setVisible(true);
            switch (state) {
                case "inactive":
                    if(bj.hasSplit){
                        playerCardsValueLabel.setVisible(false);
                        splitPlayerCardsValueLabel1.setVisible(true);
                        splitPlayerCardsValueLabel2.setVisible(true);
                        splitStateLabel1.setVisible(true);
                        SplitStateLabel2.setVisible(true);
                        stateLabel.setVisible(false);
                    }else{
                        playerCardsValueLabel.setVisible(true);
                        splitPlayerCardsValueLabel1.setVisible(false);
                        splitPlayerCardsValueLabel2.setVisible(false);
                        splitStateLabel1.setVisible(false);
                        SplitStateLabel2.setVisible(false);
                    }
                    continueButton.setVisible(true);
                    exitButton.setVisible(true);
                    panel.add(continueButton);
                    hitButton.setVisible(false);
                    standButton.setVisible(false);
                    doubleDownButton.setVisible(false);
                    splitButton.setVisible(false);
                    break;
                case "betting":
                    if(bj != null){
                        bj.hasSplit = false;
                    }
                    seperatedBevels = false;
                    confirmButton.setVisible(true);
                    betSlider.setVisible(true);
                    betField.setVisible(true);
                    playerCardsValueLabel.setVisible(false);
                    dealerCardsValueLabel.setVisible(false);
                    exitButton.setVisible(false);
                    hitButton.setVisible(false);
                    standButton.setVisible(false);
                    doubleDownButton.setVisible(false);
                    splitButton.setVisible(false);
                    splitStateLabel1.setVisible(false);
                    SplitStateLabel2.setVisible(false);
                    bj.leftHandWinner = "void";
                    bj.rightHandWinner = "void";
                    break;
                case "active":
                    if(bj.hasSplit){
                        playerCardsValueLabel.setVisible(false);
                        splitPlayerCardsValueLabel1.setVisible(true);
                        splitPlayerCardsValueLabel2.setVisible(true);
                        splitStateLabel1.setVisible(true);
                        SplitStateLabel2.setVisible(true);
                        stateLabel.setVisible(false);
                    }else{
                        playerCardsValueLabel.setVisible(true);
                        splitPlayerCardsValueLabel1.setVisible(false);
                        splitPlayerCardsValueLabel2.setVisible(false);
                        splitStateLabel1.setVisible(false);
                        SplitStateLabel2.setVisible(false);
                    }
                    dealerCardsValueLabel.setVisible(true);
                    hitButton.setVisible(true);
                    standButton.setVisible(true);
                    doubleDownButton.setVisible(true);
                    splitButton.setVisible(true);
                    break;
                case "gameover":
                    //Actionbuttons unsichtbar machen
                    hitButton.setVisible(false);
                    standButton.setVisible(false);
                    doubleDownButton.setVisible(false);
                    splitButton.setVisible(false);

                    restartButton.setVisible(true);
                    exitButton.setVisible(true);
                    break;
            }
        }
        if(!isMenuOpen && bj.hasSplit && !state.equals("betting")) {
            splitStateLabel1.setText(getStateMessage(state, bj.leftHandWinner, true));
            SplitStateLabel2.setText(getStateMessage(state, bj.rightHandWinner, true));
            stateLabel.setVisible(false);
            if(!state.equals("active")){
                splitStateLabel1.setVisible(true);
                SplitStateLabel2.setVisible(true);
            }else{
                splitStateLabel1.setVisible(false);
                SplitStateLabel2.setVisible(false);
            }
        }
        stateLabel.setText(getStateMessage(state, bj.winner, false));


        if(bj.hasSplit){
            splitButton.setEnabled(false);
        }

    }

    private String getStateMessage(String state, String winner, boolean split) {
        if(split){
            if(winner.equals("player")){
                return "Du hast gewonnen!";
            }else if(winner.equals("dealer")){
                return "Verloren!";
            }else if(winner.equals("bust")){
                return "Überkauft & Verloren!";
            }else if(winner.equals("blackjack")){
                return "Blackjack!";
            }else{
                return "Unentschieden!";
            }
        }else {
            switch (state) {
                case "inactive":
                    if (winner.equals("player")) {
                        return "Du hast gewonnen!";
                    } else if (winner.equals("dealer")) {
                        return "Verloren!";
                    } else if (winner.equals("bust")) {
                        return "Überkauft & Verloren!";
                    } else if (winner.equals("blackjack")) {
                        return "Blackjack!";
                    } else {
                        return "Unentschieden!";
                    }
                case "betting":
                    return "Einsatz bestimmen";
                case "active":
                    return "Blackjack";
                case "gameover":
                    return "Dein Erbe ist verzockt!";
                default:
                    return "ERROR: Unknown state";
            }
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        //Wenn die Escape-Taste gedrückt wird, wird das Menü geöffnet/geschlossen
        switch(e.getKeyCode()){
            case 27:
                if(isMenuOpen){
                    MainGUI.playSound("close_menu");
                }else{
                   MainGUI.playSound("open_menu");
                }
                isMenuOpen = !isMenuOpen;
                break;
            //Hit
            case 72:

                if(!AnimationManager.onCooldown() && hitButton.isEnabled() && !isMenuOpen && hitButton.isVisible()){
                    hitButton.doClick();
                    AnimationManager.setCooldown(200);
                }
                break;
            //Stand
            case 83:
                if(!AnimationManager.onCooldown() && standButton.isEnabled() && !isMenuOpen && standButton.isVisible()){
                    standButton.doClick();
                    AnimationManager.setCooldown(200);
                }
                break;
            //Double Down
            case 68:
                if(!AnimationManager.onCooldown() && doubleDownButton.isEnabled() && !isMenuOpen && doubleDownButton.isVisible()){
                    doubleDownButton.doClick();
                    AnimationManager.setCooldown(200);
                }
                break;
            //Split
            case 80:
                if(!AnimationManager.onCooldown() && splitButton.isEnabled() && !isMenuOpen && splitButton.isVisible()){
                    splitButton.doClick();
                    AnimationManager.setCooldown(200);
                }
                break;
            default:
                System.out.println("Unknown Keycode: " + e.getKeyCode() + "(" + e.getKeyChar() + ")");
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void renderPlayerCards(Graphics2D g) {
        if (bj.playerHand != null) {
            if (!bj.hasSplit && !bj.isSplitting) {
                for (int i = 0; i < bj.playerHand.cards.size(); i++) {
                    if (bj.playerHand.cards.size() - 1 == i && i > 1) {
                        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, bj.playerHand.cards.get(i).alpha);
                        Composite previousComposite = g.getComposite();
                        g.setComposite(ac);
                        bj.playerHand.cards.get(i).triggerEnteranceAnimation(((1200 - (bj.playerHand.cards.size() * 40)) / 2) - 40 + i * 40, 520 - i * 15, 600, -60);
                        g.drawImage(ImageArchive.getImage("card:" + bj.playerHand.cards.get(i).toString()), bj.playerHand.cards.get(i).xPosition, bj.playerHand.cards.get(i).yPosition, null);
                        g.setComposite(previousComposite);
                    } else if(!bj.isSplitting){
                        g.drawImage(ImageArchive.getImage("card:" + bj.playerHand.cards.get(i).toString()), ((1200 - (bj.playerHand.cards.size() * 40)) / 2) - 40 + i * 40, 520 - i * 15, null);
                    }
                    playerCardsValueLabel.setBounds(playerCardsValueLabel.getX(), 470 - i * 15, playerCardsValueLabel.getWidth(), playerCardsValueLabel.getHeight());
                }

                playerCardsValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
                if(bj.playerHand.cards.size() <= 2){
                    if (bj.playerHand.getValue() > 21) {
                        playerCardsValueLabel.setForeground(Color.RED);
                    } else {
                        playerCardsValueLabel.setForeground(Color.WHITE);
                    }
                    playerCardsValueLabel.setText(String.valueOf(bj.playerHand.getValue()));
                }else{
                    if (bj.playerHand.getVisibleValue() > 21) {
                        playerCardsValueLabel.setForeground(Color.RED);
                    } else {
                        playerCardsValueLabel.setForeground(Color.WHITE);
                    }
                    playerCardsValueLabel.setText(String.valueOf(bj.playerHand.getVisibleValue()));
                }
            } else if (!bj.isSplitting){
                // Split Hand (-1-)
                for (int i = 0; i < bj.splitHand1.cards.size(); i++) {
                    if (bj.splitHand1.cards.size() - 1 == i && i > 0) {
                        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, bj.splitHand1.cards.get(i).alpha);
                        Composite previousComposite = g.getComposite();
                        g.setComposite(ac);
                        bj.splitHand1.cards.get(i).triggerEnteranceAnimation(((1200 - (bj.splitHand1.cards.size() * 40)) / 2) - 300 + i * 40, 520 - i * 15, 700, 0);
                        g.drawImage(ImageArchive.getImage("card:" + bj.splitHand1.cards.get(i).toString()), bj.splitHand1.cards.get(i).xPosition, bj.splitHand1.cards.get(i).yPosition, null);
                        g.setComposite(previousComposite);
                    } else {
                        g.drawImage(ImageArchive.getImage("card:" + bj.splitHand1.cards.get(i).toString()), ((1200 - (bj.splitHand1.cards.size() * 40)) / 2) - 300 + i * 40, 520 - i * 15, null);
                    }
                    splitPlayerCardsValueLabel1.setBounds(splitPlayerCardsValueLabel1.getX(), 470 - i * 15, splitPlayerCardsValueLabel1.getWidth(), splitPlayerCardsValueLabel1.getHeight());
                }
                if (bj.splitHand1.getVisibleValue() > 21) {
                    splitPlayerCardsValueLabel1.setForeground(Color.RED);
                } else {
                    splitPlayerCardsValueLabel1.setForeground(Color.WHITE);
                }
                splitPlayerCardsValueLabel1.setHorizontalAlignment(SwingConstants.CENTER);
                splitPlayerCardsValueLabel1.setText(String.valueOf(bj.splitHand1.getVisibleValue()));
                splitPlayerCardsValueLabel1.setVisible(true);

                // Split Hand (-2-)
                for (int i = 0; i < bj.splitHand2.cards.size(); i++) {
                    if (bj.splitHand2.cards.size() - 1 == i && i > 0) {
                        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, bj.splitHand2.cards.get(i).alpha);
                        Composite previousComposite = g.getComposite();
                        g.setComposite(ac);
                        bj.splitHand2.cards.get(i).triggerEnteranceAnimation(((1200 - (bj.splitHand2.cards.size() * 40)) / 2) + 235 + i * 40, 520 - i * 15, 500, 0);
                        g.drawImage(ImageArchive.getImage("card:" + bj.splitHand2.cards.get(i).toString()), bj.splitHand2.cards.get(i).xPosition, bj.splitHand2.cards.get(i).yPosition, null);
                        g.setComposite(previousComposite);
                    } else {
                        g.drawImage(ImageArchive.getImage("card:" + bj.splitHand2.cards.get(i).toString()), ((1200 - (bj.splitHand2.cards.size() * 40)) / 2) + 235 + i * 40, 520 - i * 15, null);
                    }
                    splitPlayerCardsValueLabel2.setBounds(splitPlayerCardsValueLabel2.getX(), 470 - i * 15, splitPlayerCardsValueLabel2.getWidth(), splitPlayerCardsValueLabel2.getHeight());
                }
                if (bj.splitHand2.getVisibleValue() > 21) {
                    splitPlayerCardsValueLabel2.setForeground(Color.RED);
                } else {
                    splitPlayerCardsValueLabel2.setForeground(Color.WHITE);
                }
                splitPlayerCardsValueLabel2.setHorizontalAlignment(SwingConstants.CENTER);
                splitPlayerCardsValueLabel2.setText(String.valueOf(bj.splitHand2.getVisibleValue()));
                splitPlayerCardsValueLabel2.setVisible(true);
            }else{
                //Animation: Splittende Karten
                //1 ( <- )
                g.drawImage(ImageArchive.getImage("card:" + bj.playerHand.cards.get(0).toString()), interpolate(((1200 - (2 * 40)) / 2) - 40, ((1200 - (2 * 40)) / 2) - 300), 520, null);
                //2 ( -> )
                g.drawImage(ImageArchive.getImage("card:" + bj.playerHand.cards.get(1).toString()), interpolate(((1200 - (bj.playerHand.cards.size() * 40)) / 2) - 40 + 40, ((1200 - (bj.splitHand2.cards.size() * 40)) / 2) + 235), interpolate(505, 520), null);
            }
        }
    }

    private int interpolate(int x, int y){
        double xDistance = y - x;
        return x + (int)((xDistance/100) * Math.min(100, seperationFrame));
    }

    public void renderDealerCards(Graphics2D g){
        if(bj.dealerHand != null){
            for (int i = 0; i < bj.dealerHand.cards.size(); i++) {
                if (i == 1 && bj.dealerCardIsHidden) {
                    if(!bj.dealerHand.cards.get(1).flipping){
                        g.drawImage(ImageArchive.getImage("card:back:"+mainGUI.getSelectedTheme()), ((1200 - (bj.dealerHand.cards.size() * 40)) / 2) - 40 + i * 40, 175 - i * 15, null);
                    }else{
                        //Flipping Animation
                        if(flipframe <=52){
                            g.drawImage(ImageArchive.getImage("card:back:"+mainGUI.getSelectedTheme()), (((1200 - (bj.dealerHand.cards.size() * 40)) / 2) - 40 + i * 40)+flipframe, 175 - i * 15, 104-(2*flipframe), 145, null);
                            if(flipframe == 6){
                                if(!playedFlipSound){
                                    MainGUI.playSound("flip");
                                    playedFlipSound = true;
                                }
                            }
                            flipframe = flipframe + 6;
                        }else if(flipframe <= 104){
                            g.drawImage(ImageArchive.getImage("card:" + bj.dealerHand.cards.get(i).toString()), (((1200 - (bj.dealerHand.cards.size() * 40)) / 2) - 40 + i * 40)+52-(flipframe-52), 175 - i * 15, 2*(flipframe-52), 145, null);
                            flipframe = flipframe + 6;
                        }else{
                            g.drawImage(ImageArchive.getImage("card:" + bj.dealerHand.cards.get(i).toString()), ((1200 - (bj.dealerHand.cards.size() * 40)) / 2) - 40 + i * 40, 175 - i * 15, null);
                            flipframe = 0;
                            bj.dealerHand.cards.get(1).flipping = false;
                            bj.dealerHand.cards.get(1).flipped = true;
                        }

                    }
                }else if (bj.dealerHand.cards.size()-1 == i && i > 1){
                    AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, bj.dealerHand.cards.get(i).alpha);
                    Composite previousComposite = g.getComposite();
                    g.setComposite(ac);
                    bj.dealerHand.cards.get(i).triggerEnteranceAnimation(((1200 - (bj.dealerHand.cards.size() * 40)) / 2) - 40 + i * 40, 175 - i * 15, 800, -120);
                    g.drawImage(ImageArchive.getImage("card:" + bj.dealerHand.cards.get(i).toString()), bj.dealerHand.cards.get(i).xPosition, bj.dealerHand.cards.get(i).yPosition, null);
                    g.setComposite(previousComposite);
                }else {
                    g.drawImage(ImageArchive.getImage("card:" + bj.dealerHand.cards.get(i).toString()), ((1200 - (bj.dealerHand.cards.size() * 40)) / 2) - 40 + i * 40, 175 - i * 15, null);
                }
            }
            if(!bj.dealerCardIsHidden) {
                if (bj.dealerHand.getValue() > 21) {
                    dealerCardsValueLabel.setForeground(Color.RED);
                } else {
                    dealerCardsValueLabel.setForeground(Color.WHITE);
                }
                dealerCardsValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
                if(bj.dealerHand.cards.size()<=2){
                    dealerCardsValueLabel.setText(String.valueOf(bj.dealerHand.getValue()));
                }else{
                    dealerCardsValueLabel.setText(String.valueOf(bj.dealerHand.getVisibleValue()));
                }
            }
        }
    }
}