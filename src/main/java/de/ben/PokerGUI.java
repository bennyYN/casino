package de.ben;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class PokerGUI extends JFrame implements KeyListener {

  //Attribute & Objekte
    public JButton foldButton, checkButton, callButton, raiseButton, allInButton, toggleButton, menuButton, exitButton, continueButton; //Verschiedene Buttons
    public int raiseAmount, totalPlayers, actualPlayerCount, startChips, bigBlind, playerShowing = -1, currentPlayerIndex = 0; //Integer Variablen
    public JButton menuexitButton, menusettingsButton, menuMainMenuButton, menuContinueButton; //Pausemenü-Buttons
    public JLabel raiseLabel, bigBlindLabel, smallBlindLabel, betLabel; //Verschiedene Labels
    ArrayList<ViewCardButton> viewCardButtons = new ArrayList<ViewCardButton>(); //ArrayList für die Buttons zum Anzeigen der Karten
    ArrayList<JButton> menuButtons = new ArrayList<JButton>(); //ArrayList für die Pausemenü-Buttons
    public BufferedImage blurredImage = null; //Bild für die Hintergrundunschärfe
    public boolean isMenuOpen = false; //Boolean-Variable, ob das Menü geöffnet ist
    private String action = "idle"; //String-Variable für die Aktion des Spielers
    ArrayList<String> playerNames; //ArrayList für die Spielernamen
    public List<String> messages; //Liste für die Dialogbox
    public JTextField raiseField; //Textfeld für das Erhöhen
    public JTextPane dialogPane; //Dialogbox
    FadingLabel fadingLabel; //Informationslabel
    protected Poker game; //Poker-Logikklasse
    public JPanel panel; //JPanel
    Playerslot slots; //Spieler-Slots
    MainGUI mainGUI; //Mainmenu

  //Konstruktor
    public PokerGUI(int numPlayers, ArrayList<String> playerNames, int startChips, int bigBlind, int actualPlayerCount, MainGUI mainGUI) {

        //Übergebene Werte speichern
        this.mainGUI = mainGUI;
        totalPlayers = numPlayers;
        this.actualPlayerCount = actualPlayerCount;
        this.startChips = startChips;
        this.bigBlind = bigBlind;
        this.playerNames = playerNames;

        //Liste für die Pausemenü-Buttons
        menuButtons.add(menuexitButton);
        menuButtons.add(menusettingsButton);
        menuButtons.add(menuMainMenuButton);
        menuButtons.add(menuContinueButton);

        //JFrame-Einstellungen
        setTitle("Poker Game"); //Fenster Titel zuweisen
        setSize(1200, 850); //Größe des Fensters setzen
        setResizable(false); //Größe des Fensters festsetzen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Das Programm schließen, wenn das Fenster geschlossen wird
        setLocationRelativeTo(null); //Das Fenster in der Mitte des Bildschirms platzieren
        addKeyListener(this); //KeyListener hinzufügen
        setFocusable(true); //Das JFrame fokussierbar machen um KeyEvents zu empfangen

        //Titlebar-Icon mit Skalierung setzen
        ImageIcon icon = new ImageIcon("img/icon.png");
        Image scaledIcon = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH); // glatte Skalierung
        setIconImage(scaledIcon);

        //Spieler-Slots instanzieren
        slots = new Playerslot(startChips, playerNames, this);

        //JPanel erstellen
        panel = new JPanel() {
            @Override //Überschreiben der Methode paintComponent um auf das Panel zu zeichnen
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); //Methode der Superklasse aufrufen

                //Ein Bild des Typs BufferedImage vom JPanel erstellen und ein neues Grafikobjekt erzeugen
                BufferedImage screenCapture = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = screenCapture.createGraphics();
                super.paintComponent(g2d);

                //Zeichnen des Hintergrunds und des Pokertisches
                g2d.drawImage(new ImageIcon("img/background.jpg").getImage(), 0, 0, null);
                g2d.drawImage(new ImageIcon("img/table.png").getImage(), 45, 45, null);

                //Zeichnen verschiedener Spielelemente
                if (game != null) {

                    //Spielerkarten
                    if (!game.playerWon) {
                        if (game.currentPlayer != null) {
                            game.currentPlayer.renderCards(g2d);
                        }
                    } else {
                        try {
                            game.players.get(playerShowing).renderCards(g2d);
                        } catch (Exception e) {
                            e.toString();
                        }
                    }
                    //Dealerkarten
                    if (game.dealer != null) {
                        game.dealer.renderCards(g2d);
                    }

                    //Gewinnpot
                    if(!game.isGameOver && !isMenuOpen){
                        g2d.drawImage(new ImageIcon("img/pot.png").getImage(), 495, 70, null);
                        g2d.setFont(new Font("TimesRoman", Font.BOLD, 30));
                        g2d.setColor(Color.WHITE);
                        g2d.drawString(String.valueOf(game.GewinnPot.getAmount()), 585, 132);
                    }

                    //Abtönungsschicht, wenn das Spiel zuende ist, zeichnen
                    if (game.isGameOver) {
                        g2d.setColor(new Color(0, 0, 0, 120));
                        g2d.fillRect(0, 0, 1200, 850);
                    }

                    //Einzeichnen der Spieler-Slots
                    slots.renderAll(g2d);

                    //Karten Symbol im Spieler-Slot des Spielers, wessen Karten am Rundenende angezeigt werden, zeichnen
                    if (playerShowing >= 0 && playerShowing < 8 && !game.isGameOver && game.playerWon && !isMenuOpen) {
                        if (playerShowing <= 3) {
                            g2d.drawImage(new ImageIcon("img/cards_icon.png").getImage(), 153, 276 + (playerShowing * 100), 20, 20, null);
                        } else {
                            g2d.drawImage(new ImageIcon("img/cards_icon.png").getImage(), 1142, 276 + ((playerShowing - 4) * 100), 20, 20, null);
                        }
                    }
                }
                //Aktualisierungsmethoden aufrufen
                updateBetLabel();
                PokerGUI.this.update();

                //Das zusätzlich erstellte Grafikobjekt nach der Nutzung löschen
                g2d.dispose();

                //Wenn das Menü offen ist, wird das Spiel im Hintergrund verschwommen und verdunkelt
                if (isMenuOpen && !game.isGameOver) {
                    if(blurredImage == null){
                        blurredImage = ImageUtils.blurImage(screenCapture, 10);
                    }
                    g.drawImage(blurredImage, 0, 0, null);
                    //Abtönungsschicht zeichnen
                    g.setColor(new Color(0, 0, 0, 110));
                    g.fillRect(0, 0, 1200, 850);
                    //Infotext, dass das Spiel pausiert ist
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("Arial", Font.BOLD, 50));
                    g.drawString("Spiel Pausiert", 425, 120);

                //Wenn das Menü nicht offen ist, wird das Spiel normal gezeichnet
                } else {
                    g.drawImage(screenCapture, 0, 0, null);
                    blurredImage = null;
                    //Wenn das Spiel zuende ist, wird ein Text angezeigt
                    if(game.isGameOver){
                        g.setColor(Color.WHITE);
                        g.setFont(new Font("Arial", Font.BOLD, 50));
                        g.drawString("Spiel Ende!", 450, 120);
                        g.setColor(new Color(234, 234, 234, 200));
                        g.setFont(new Font("Arial", Font.PLAIN, 18));
                        g.drawString("Ein Spieler hat keine Chips mehr.", 455, 165);
                    }
                }
                repaint(); //Das Panel neu zeichnen
            }
        };

        //Layout des Panels setzen
        panel.setLayout(null);

        //Dialogbox oben links
        messages = new ArrayList<>(); //Liste für den Inhalt der Dialogbox
        dialogPane = new JTextPane();
        dialogPane.setEditable(false); //Dialogbox nicht editierbar machen
        dialogPane.setOpaque(false); //Dialogbox transparent machen
        dialogPane.setForeground(Color.WHITE); //Textfarbe auf weiß setzen
        dialogPane.setFont(new Font("Arial", Font.PLAIN, 16)); //Schriftart und -Größe setzen
        JScrollPane scrollPane = new JScrollPane(dialogPane);
        scrollPane.setBounds(10, 10, 350, 170); //Position und Größe des ScrollPanes setzen
        scrollPane.setOpaque(false); //ScrollPane transparent machen
        scrollPane.getViewport().setOpaque(false); //Viewport transparent machen
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); //Rahmen des ScrollPanes entfernen
        //Scrollbar unsichtbar machen aber trotzdem funktionsfähig lassen
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
        panel.add(scrollPane); //ScrollPane zum JPanel hinzufügen

    //Textlabels:
        //Big Blind
        bigBlindLabel = new JLabel("Big Blind: " + bigBlind);
        bigBlindLabel.setBounds(1000, 10, 150, 30);
        bigBlindLabel.setForeground(Color.WHITE);
        bigBlindLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(bigBlindLabel);

        //Small Blind
        smallBlindLabel = new JLabel("Small Blind: " + (bigBlind/2));
        smallBlindLabel.setBounds(1000, 50, 150, 30);
        smallBlindLabel.setForeground(Color.WHITE);
        smallBlindLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(smallBlindLabel);

        //Höchsteinsatz
        betLabel = new JLabel();
        betLabel.setBounds(1000, 90, 150, 30);
        betLabel.setForeground(Color.WHITE);
        betLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(betLabel);

        //FadingLabel (für verschiedene Informationen im Laufe des Spiels; Text verblasst nach einigen Sekunden)
        fadingLabel = new FadingLabel("");
        fadingLabel.setForeground(Color.WHITE);
        fadingLabel.setBounds(200, 20, 800, 30);
        fadingLabel.setHorizontalAlignment(FadingLabel.CENTER);
        fadingLabel.setFont(new Font("Arial", Font.BOLD, 17));
        panel.add(fadingLabel);

    //Buttons:
        //Hilfe-Button zum Anzeigen des Handranking-Bildes
        JButton helpButton = new JButton("?");
        helpButton.setBounds(1150, 10, 30, 30); //Position setzen
        helpButton.setBackground(new Color(170, 0, 0)); //Farbe setzen
        helpButton.setForeground(Color.WHITE); //Textfarbe festlegen
        helpButton.setFocusable(false); //Sicherstellen, dass der Button nicht fokussiert werden kann
        helpButton.setFont(new Font("Arial", Font.BOLD, 20)); //Schriftart und Schriftgröße festlegen
        helpButton.setBorder(BorderFactory.createLineBorder(Color.WHITE)); //Rahmen um den Button setzen
        helpButton.setFocusPainted(false); //Fokusrand deaktivieren
        panel.add(helpButton);

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
            menuButtons.set(i, createButton(tempButtonText));

            //Pausemenü-Button Positionen
            int buttonWidth = 225;
            int buttonHeight = 70;
            int yPosition = 700;
            int spacing = 30;
            int startX = ((getWidth() - (140 * 6 + spacing * 5)) / 2) - 6;
            menuButtons.get(i).setBounds(startX + (buttonWidth + spacing) * i, yPosition, buttonWidth, buttonHeight);
            menuButtons.get(i).setVisible(false);
            panel.add(menuButtons.get(i));
        }

        //Gameplay Buttons:
        foldButton = createButton("Fold");
        checkButton = createButton("Check");
        callButton = createButton("Call");
        raiseButton = createButton("Raise");
        allInButton = createButton("All In");
        toggleButton = createButton("Show/Hide Hand");
        continueButton = createButton("Continue");
        menuButton = createButton("Back to Menu");
        exitButton = createButton("Exit");
        continueButton.setVisible(false);
        menuButton.setVisible(false);
        exitButton.setVisible(false);

        //Buttons für jeden Spieler zum Karten anzeigen
        for(int i = 0; i <= 7; i++){
            if(i <= 3){
                viewCardButtons.add(new ViewCardButton(this, i, viewCardButtons,15, 280 + (i * 100)));
                if(slots.actualPlayer.get(i)){
                    panel.add(viewCardButtons.get(i));
                }
            }else{
                viewCardButtons.add(new ViewCardButton(this, i, viewCardButtons,1003, 280 + ((i-4) * 100)));
                if(slots.actualPlayer.get(i)){
                    panel.add(viewCardButtons.get(i));
                }
            }
        }

        //Positionen für die Gameplay-Buttons
        int buttonWidth = 140;
        int buttonHeight = 70;
        int yPosition = 700;
        int spacing = 30;
        int startX = ((getWidth() - (buttonWidth * 6 + spacing * 5)) / 2) - 6;
        raiseButton.setBounds(startX, yPosition, buttonWidth, buttonHeight);
        checkButton.setBounds(startX + (buttonWidth + spacing) * 1, yPosition, buttonWidth, buttonHeight);
        callButton.setBounds(startX + (buttonWidth + spacing) * 2, yPosition, buttonWidth, buttonHeight);
        foldButton.setBounds(startX + (buttonWidth + spacing) * 3, yPosition, buttonWidth, buttonHeight);
        allInButton.setBounds(startX + (buttonWidth + spacing) * 4, yPosition, buttonWidth, buttonHeight);
        toggleButton.setBounds(startX + (buttonWidth + spacing) * 5, yPosition, buttonWidth, buttonHeight);
        continueButton.setBounds(raiseButton.getX(), raiseButton.getY(), (callButton.getX()+callButton.getWidth()-raiseButton.getX())*2+30, raiseButton.getHeight());
        menuButton.setBounds(raiseButton.getX(), raiseButton.getY(), callButton.getX()+callButton.getWidth()-raiseButton.getX(), raiseButton.getHeight());
        exitButton.setBounds(foldButton.getX(), foldButton.getY(), (continueButton.getWidth()/2)-30, continueButton.getHeight());
        //Gameplay-Buttons zum JPanel hinzufügen
        panel.add(foldButton);
        panel.add(checkButton);
        panel.add(callButton);
        panel.add(raiseButton);
        panel.add(allInButton);
        panel.add(toggleButton);
        panel.add(continueButton);
        panel.add(menuButton);
        panel.add(exitButton);

        //Textfeld zum Erhöhen
        raiseField = new JTextField();
        raiseField.setBounds(raiseButton.getX(), raiseButton.getY() - 40, buttonWidth, 30); // Position above the raise button
        raiseField.setVisible(false);
        panel.add(raiseField);

        //Label für das Textfeld zum Erhöhen
        raiseLabel = new JLabel("Raise To:");
        raiseLabel.setBounds(raiseField.getX(), raiseField.getY() - 20, buttonWidth, 20); // Position direkt über dem Eingabefeld
        raiseLabel.setForeground(Color.WHITE);
        raiseLabel.setFont(new Font("Arial", Font.BOLD, 12));
        raiseLabel.setVisible(false);
        panel.add(raiseLabel);

    //ActionListener Methoden um den Buttons eine Aktion/Funktion zuzuweisen
        //Hilfe-Button
        helpButton.addActionListener(e -> new ImageWindow().setVisible(true)); //Öffnet Fenster mit Handranking-Bild

        //Fold Button
        foldButton.addActionListener(e -> {
            hideRaiseField(); //Textfeld zum Erhöhen verstecken
            action = "fold"; //Die Aktion des Spielers auf Fold setzen
            nextPlayer(); //Zum nächsten Spieler wechseln
        });

        //Check Button
        checkButton.addActionListener(e -> {
            hideRaiseField(); //Textfeld zum Erhöhen verstecken
            action = "check"; //Die Aktion des Spielers auf Check setzen
            nextPlayer(); //Zum nächsten Spieler wechseln
        });

        //Call Button
        callButton.addActionListener(e -> {
            hideRaiseField(); //Textfeld zum Erhöhen verstecken
            action = "call"; //Die Aktion des Spielers auf Call setzen
            nextPlayer(); //Zum nächsten Spieler wechseln
        });

        //Raise Button
        raiseButton.addActionListener(e -> {
            //Textfeld und Label für das Erhöhen sichtbar machen
            raiseField.setVisible(true);
            raiseLabel.setVisible(true);
            //Wenn der Inhalt des Textfeldes nicht leer ist, wird der Wert in raiseAmount gespeichert
            if (!raiseField.getText().isEmpty()) {
                raiseAmount = Integer.parseInt(raiseField.getText());
                raiseField.setText(""); // Clear the field after submission
                hideRaiseField(); //Textfeld zum Erhöhen wieder Verstecken
                action = "raise"; //Die Aktion des Spielers auf Erhöhen setzen
                nextPlayer(); //Zum nächsten Spieler wechseln
            }
        });

        //All-In Button
        allInButton.addActionListener(e -> {
            slots.players.get(currentPlayerIndex).setAllIn(true); //Die All-In Flag des Spielers auf True setzen
            hideRaiseField(); //Das Eingabefeld zum Erhöhen verstecken
            action = "allin"; //Die Aktion des Spielers auf All-In setzen
            nextPlayer(); //Zum nächsten Spieler wechseln
        });

        //Button zum Anziegen/Verdecken der Hand
        toggleButton.addActionListener(e -> {
            if(game != null){
                //Wenn ein Spieler eine Runde gewonnen hat, wird die Hand des ausgewählten Spielers gezeigt...
                if(game.playerWon){
                    game.players.get(playerShowing).handVisible = !game.players.get(playerShowing).handVisible;
                //Bei einer unentschiedenen Runde wird die Hand des aktuellen Spielers angezeigt
                }else{
                    game.currentPlayer.handVisible = !game.currentPlayer.handVisible;
                }
            }
            hideRaiseField(); //Das Eingabefeld zum Erhöhen verstecken
        });

        //Fortfahren Button um eine neue Runde zu starten
        continueButton.addActionListener(e -> {
            if(game != null){
                game.playerWon = false; //Boolean-Flag, für wenn ein Spieler gewonen hat zurücksetzen
                //Die Hand jedes Spielers beim Beginn einer neuen Runde verdecken
                for(Player player : game.players){
                    player.handVisible = false;
                }
            }
            fadingLabel.killText(); //Den eingefrorenen Gewinner-Informations-Text löschen
        });

        //Hauptmenü Button, für wenn das Spiel zuende ist
        menuButton.addActionListener(e -> SwingUtilities.invokeLater(() -> {

            mainGUI.setVisible(true); //Das Hauptmenü im Hintergrund sichtbar machen
            PokerGUI.this.dispose(); //Die Poker-Benutzeroberfläche schließen
        }));

        //Exit Button, für wenn das Spiel zuende ist
        exitButton.addActionListener(e -> System.exit(0));

        //Buttons für das Menü:
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

        //JPanel zum JFrame hinzufügen
        getContentPane().add(panel);

        //Poker Logikklasse instanzieren und in einem neuen Thread starten
        game = new Poker(startChips, bigBlind, numPlayers, Playerslot.actualPlayer, this);
        new Thread(game::startGame).start();
    }

    //Methode um ein Bild-Objekt von dem JPanel zu erstellen
    public BufferedImage capturePanelImage() {
        BufferedImage image = new BufferedImage(panel.getWidth(), panel.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        panel.paintAll(g2d);
        g2d.dispose();
        return image;
    }

    //Methode, welche kontinuierlich aufgerufen wird (bis das Spiel zuende ist)
    //-> Wird für die Aktualisierung der GUI und verschiedene Werte benötigt
    public void update(){

        //Aktualisierung der statischen Booleanvariable isGameDecided der Klasse Spieler
        if(game != null){
            Player.isGameDecided = game.playerWon;
        }

        //Spieler mit 0 Chips auf All-in setzen damit sie nicht mehr dran kommen
        for(Player player : game.players){
            if(player.getChips().getAmount() <= 0){
                player.setAllIn(true);
            }
        }

        //Überprüfungslogik, für wenn das Spiel vorbei ist
        if(game.isGameOver){
            //Spiel Buttons verschwinden lassen
            raiseButton.setVisible(false);
            callButton.setVisible(false);
            checkButton.setVisible(false);
            foldButton.setVisible(false);
            allInButton.setVisible(false);
            continueButton.setVisible(false);
            toggleButton.setVisible(false);
            ViewCardButton.setAllVisible(false);
            //Exit & Menu Button zeigen
            exitButton.setVisible(true);
            menuButton.setVisible(true);
        }else{
            //Wenn ein oder weniger Spieler übrig sind, wird das Spiel beendet
            int temp = game.anzahlSpieler;
            for(Player player : game.players) {
                if ((player.isFolded() || player.isAllIn()) && !player.dummy) {
                    temp--;
                }
                if (temp <= 1) {
                    game.ending = true;
                }
            }
        }

        //Wenn das Spiel noch nicht zuende ist und ein Spieler eine Runde gewonnen hat:
        if(game.playerWon && !game.isGameOver){
                //Spiel Buttons verschwinden lassen
                raiseButton.setVisible(false);
                callButton.setVisible(false);
                checkButton.setVisible(false);
                foldButton.setVisible(false);
                allInButton.setVisible(false);
                toggleButton.setVisible(false);
                //Weiter spielen Button zeigen
                continueButton.setVisible(true);
                //Buttons bei jedem Spieler zum Anzeigen derer Karten sichtbar machen
                for (ViewCardButton button : viewCardButtons) {
                    button.setVisible(true);
                }
        //Wenn das Spiel nicht zuende ist, aber ein Spieler eine Runde gewonnen hat:
        }else if(!game.isGameOver){
            //Spiel-Aktions-Buttons zeigen
            raiseButton.setVisible(true);
            callButton.setVisible(true);
            checkButton.setVisible(true);
            foldButton.setVisible(true);
            allInButton.setVisible(true);
            toggleButton.setVisible(true);
            continueButton.setVisible(false);
            //Buttons bei jedem Spieler zum Anzeigen derer Karten unsichtbar machen
            for(ViewCardButton button : viewCardButtons){
                button.setVisible(false);
            }
        }

        //Logik um die Raise-Buttons zu deaktivieren, wenn der Spieler nicht genug Chips hat
        //oder der eingetragene Betrag kleiner oder gleich der höchsten Wette und somit zu niedrig ist
        if(game != null){
            if(raiseField.isVisible() && raiseLabel.isVisible() && !game.playerWon){
                if(!raiseField.getText().isEmpty()){
                    try {
                        if (Integer.parseInt(raiseField.getText()) <= game.highestBet) {
                            raiseButton.setEnabled(false);
                        } else {
                            if (Integer.parseInt(raiseField.getText()) <= game.currentPlayer.getChips().getAmount()) {
                                raiseButton.setEnabled(true);
                            } else {
                                raiseButton.setEnabled(false);
                            }

                        }
                    }catch (Exception e){
                        e.toString();
                    }
                }else{
                    raiseButton.setEnabled(false);
                }
            }else{
                if(!game.playerWon)
                raiseButton.setEnabled(true);
            }
        }

        //Pause-Menü
        //Wenn das Spiel läuft und das Menü geöffnet ist
        if(isMenuOpen && !game.isGameOver){
            //Ungewünschte Aktionsbuttons unsichtbar machen
            raiseButton.setVisible(false);
            callButton.setVisible(false);
            checkButton.setVisible(false);
            foldButton.setVisible(false);
            allInButton.setVisible(false);
            continueButton.setVisible(false);
            toggleButton.setVisible(false);
            exitButton.setVisible(false);
            menuButton.setVisible(false);
            ViewCardButton.setAllVisible(false);
            //Infolabels etc. unsichtbar machen
            raiseField.setVisible(false);
            raiseLabel.setVisible(false);
            betLabel.setVisible(false);
            bigBlindLabel.setVisible(false);
            smallBlindLabel.setVisible(false);
            fadingLabel.setVisible(false);
            dialogPane.setVisible(false);
            //Alle Menü-Buttons sichtbar machen
            for(JButton button : menuButtons){
                button.setVisible(true);
            }
        /*Wenn das Spiel läuft und das Menü geschlossen ist
          müssen die für das offene Menü geschlossenen Buttons wieder sichtbar gemacht werden (wenn dies
          nicht bereits durch vorhandene Logik geschieht)*/
        }else {
            assert game != null;
            if(!game.isGameOver){
                //Alle Labels sichtbar machen
                betLabel.setVisible(true);
                bigBlindLabel.setVisible(true);
                smallBlindLabel.setVisible(true);
                fadingLabel.setVisible(true);
                dialogPane.setVisible(true);
                //Alle Menu Buttons unsichtbar machen
                for(JButton button : menuButtons){
                    button.setVisible(false);
                }
            }
        }
    }

    //Methode um einen custom Button zu erstellen
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(170, 0, 0)); // Wine red color
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setOpaque(true);
        button.setFocusable(false); // Disable focus on the button
        button.setBorderPainted(true);
        return button;
    }

    //Methode um eine Nachricht zur Dialogbox hinzuzufügen
    public void addMessageToDialogBox(String message) {
        messages.add(message);
        dialogPane.setText(String.join("\n", messages));
        dialogPane.setCaretPosition(dialogPane.getDocument().getLength());
    }

    //Methode um das Textfeld und das Label für das Erhöhen zu verstecken
    public void hideRaiseField() {
        raiseField.setVisible(false);
        raiseLabel.setVisible(false);
    }

    //Methode um den Betrag der höchsten Wette für das zuständige JLabel zu aktualisieren
    private void updateBetLabel(){
        if(game != null) {
            betLabel.setText("Highest Bet: " + String.valueOf(game.highestBet));
        }
    }

    //Methode um zum nächsten Spieler zu wechseln
    public void nextPlayer() {
        if(game.ending || game.isGameOver){
            int temp = 0;
            for(int i = 0; i < 8; i++){
                if(Playerslot.actualPlayer.get(i)){
                    temp = i;
                    break;
                }
            }
            currentPlayerIndex = temp;
        }else{
            do {
                currentPlayerIndex = (currentPlayerIndex + 1) % totalPlayers;
            }
            while (Playerslot.players.get(currentPlayerIndex).isFolded() || Playerslot.players.get(currentPlayerIndex).isAllIn());
        }
        if (Playerslot.players.get(currentPlayerIndex) != null) {
            Playerslot.players.get(currentPlayerIndex).handVisible = false;
        }
    }

    //Getter- & Settermethoden für den Actionstring
    public String getAction() {
       return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    //Zu implementierende Methoden vom KeyListener-Interface
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //Wenn die Escape-Taste gedrückt wird, wird das Menü geöffnet/geschlossen
        if(e.getKeyCode() == 27){
            isMenuOpen = !isMenuOpen;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}