package de.ben;

import de.ben.poker.MultiplayerGUI;
import de.ben.poker.SettingsGUI;
import de.ben.sound.Sound;
import de.ben.sound.SoundManager;
import de.ben.ui.menu.carousel.PlateCarousel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class MainGUI extends JFrame implements ActionListener, MouseWheelListener, KeyListener {

    // Attribute
    JButton startButton, settingsButton, infoButton;
    JPanel panel;
    private final boolean startingGame = false;
    private String selectedTheme;
    private static float gameSoundsVolume = 50; // Default game sounds volume
    private String MultiplayerName;
    Color originalTheme = new Color(78, 136, 174, 255), transparentOriginalTheme = new Color(142, 215, 255, 81);
    Color darkTheme = new Color(43, 49, 64, 255), transparentDarkTheme = new Color(34, 34, 34, 81);
    Color darkblueTheme = new Color(62, 103, 147, 255), transparentDarkblueTheme = new Color(78, 136, 174, 255);
    Color scarletTheme = new Color(172, 41, 66, 255), transparentScarletTheme = new Color(197, 0, 0, 136);
    public int playerIndex = -1;
    private int selectedGameIndex = 0;
    private boolean isAnimating = false;
    private int animationFrame = 0, direction = 0, startAnimationFrame = 1;
    private final int ANIMATION_SPEED = 10, ANIMATION_DELAY = 1;
    private boolean showGameInfo = true;

    private static final MainGUI INSTANCE = new MainGUI();
    private final PlateCarousel carousel = new PlateCarousel();

    // Konstruktor
    private MainGUI() {
        //Laden des Bilderarchivs
        new ImageArchive();

        // Erstellen des JLayeredPane
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(815, 600));

        // Laden des gespeicherten Themes und der Lautstärken
        loadSelectedTheme();

        this.setTitle("Game Library");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(815, 600);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.addKeyListener(this);

        //Titlebar-Icon mit Skalierung setzen
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("img/icon.png"));
        Image scaledIcon = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH); // glatte Skalierung
        setIconImage(scaledIcon);

        // Musik initialisieren und Lautstärke laden
        //initMusicPlayer();

        // Versuche, den Hintergrund als Bild zu setzen
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(ImageArchive.getImage("background:" + selectedTheme), 0, 0, null);

                //Button Farbe regelmäßig updaten um ausgewähltem Theme zu matchen
                updateButtonColor(startButton, false);
                updateButtonColor(settingsButton, false);

                //Rendering hints for smoother and more precise rendering
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                //Abtönungsschicht
                g.setColor(new Color(0, 0, 0, 100));
                g.fillRect(0, 475, 800, 125);

                //Game Infos
                if (showGameInfo) {
                    //TODO: neue position nur berechnen, wenn sich das spiel ändert
                    //OBERER Bevel für tooltips
                    g.setColor(new Color(0, 0, 0, 100));
                    g.fillRoundRect(100, 25, 600, 100, 10, 10);
                    g.setColor(new Color(255, 255, 255, 255));
                    //draw text which is centered on the bevel with taking the font size into account
                    g.setFont(new Font("Arial", Font.BOLD, 25));
                    FontMetrics fm = g.getFontMetrics();
                    int x = (500 - fm.stringWidth(carousel.getSelectedGame().getTitle())) / 2 + 150;
                    int y = (((100 - fm.getHeight()) / 2) + fm.getAscent() + 25) - 25;
                    g.drawString(carousel.getSelectedGame().getTitle(), x, y);
                    g.setFont(new Font("Arial", Font.PLAIN, 16));
                    fm = g.getFontMetrics();
                    x = (500 - fm.stringWidth(carousel.getSelectedGame().getDescription())) / 2 + 150;
                    y = (((100 - fm.getHeight()) / 2) + fm.getAscent() + 25) + 17;
                    g.drawString(carousel.getSelectedGame().getDescription(), x, y);
                }

                carousel.codeAusPaintMethode(g);

            }
        };

        // Inside the MainGUI constructor, after initializing the panel
        Timer timer = new Timer(ANIMATION_DELAY, o -> panel.repaint());
        timer.start();

        panel.setLayout(null); // Setze Layout nach dem Laden des Bildes oder dem Default

        // Add MouseWheelListener to the panel
        panel.addMouseWheelListener(this);

        // Start Button
        startButton = new JButton("Start");
        startButton.setFocusable(false);
        startButton.setBounds(325, 510, 150, 40);
        styleButton(startButton);
        panel.add(startButton);

        // Settings Button
        settingsButton = new JButton();
        settingsButton.setFocusable(false);
        settingsButton.setBounds(10, 505, 50, 50);

// Load and scale the images
        ImageIcon defaultIcon = new ImageIcon(getClass().getClassLoader().getResource("img/menu/settings1.png"));
        ImageIcon hoverIcon = new ImageIcon(getClass().getClassLoader().getResource("img/menu/settings2.png"));
        ImageIcon clickIcon = new ImageIcon(getClass().getClassLoader().getResource("img/menu/settings3.png"));

// Set the default icon
        settingsButton.setIcon(defaultIcon);
        settingsButton.setContentAreaFilled(false);
        settingsButton.setBorderPainted(false);
        settingsButton.setFocusPainted(false);

// Add action listener for click event
        settingsButton.addActionListener(e -> {
            //playSound("click");
            new SettingsGUI(this, true); // Open SettingsGUI and pass MainGUI for volume adjustment
            this.setVisible(false); // Hide MainGUI instead of closing it
        });

// Add mouse listener for hover and click events
        settingsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                settingsButton.setIcon(hoverIcon);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                settingsButton.setIcon(defaultIcon);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                settingsButton.setIcon(clickIcon);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                settingsButton.setIcon(hoverIcon);
            }
        });

        panel.add(settingsButton);

        // Inside the MainGUI constructor, after initializing the panel
        JButton leftInvisibleButton = new JButton();
        leftInvisibleButton.setFocusable(false);
        leftInvisibleButton.setBounds(50, 185, (int) (250 * 0.8), (int) (300 * 0.8)); // Position and size the button
        leftInvisibleButton.setContentAreaFilled(false);
        leftInvisibleButton.setBorderPainted(false);
        leftInvisibleButton.setFocusPainted(false);
        leftInvisibleButton.addActionListener(e -> {
            carousel.rotateGame(1);
        });

        JButton rightInvisibleButton = new JButton();
        rightInvisibleButton.setFocusable(false);
        rightInvisibleButton.setBounds(550, 185, (int) (250 * 0.8), (int) (300 * 0.8)); // Position and size the button
        rightInvisibleButton.setContentAreaFilled(false);
        rightInvisibleButton.setBorderPainted(false);
        rightInvisibleButton.setFocusPainted(false);
        rightInvisibleButton.addActionListener(e -> {
            carousel.rotateGame(-1);
        });

        panel.add(leftInvisibleButton);
        panel.add(rightInvisibleButton);
        add(layeredPane);

        // Load and scale the images for the info button
        ImageIcon infoDefaultIcon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("img/menu/info1.png")).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        ImageIcon infoHoverIcon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("img/menu/hover_info1.png")).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        ImageIcon infoDefaultIconFalse = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("img/menu/info2.png")).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        ImageIcon infoHoverIconFalse = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("img/menu/hover_info2.png")).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        ImageIcon infoClickIcon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("img/menu/info3.png")).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));

// Create the info button
        infoButton = new JButton();
        infoButton.setFocusable(false);
        infoButton.setBounds(75, 510, 40, 40); // Position it next to the settings button
        infoButton.setIcon(showGameInfo ? infoDefaultIcon : infoDefaultIconFalse);
        infoButton.setContentAreaFilled(false);
        infoButton.setBorderPainted(false);
        infoButton.setFocusPainted(false);

// Add action listener for click event
        infoButton.addActionListener(e -> {
            showGameInfo = !showGameInfo;
            infoButton.setIcon(showGameInfo ? infoDefaultIcon : infoDefaultIconFalse);
            //playSound("click");
        });

// Add mouse listener for hover and click events
        infoButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!showGameInfo) {
                    infoButton.setIcon(infoHoverIconFalse);
                } else {
                    infoButton.setIcon(infoHoverIcon);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!showGameInfo) {
                    infoButton.setIcon(infoDefaultIconFalse);
                } else {
                    infoButton.setIcon(infoDefaultIcon);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                infoButton.setIcon(infoClickIcon);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!showGameInfo) {
                    infoButton.setIcon(infoHoverIconFalse);
                } else {
                    infoButton.setIcon(infoHoverIcon);
                }
            }
        });

// Add the info button to the panel
        panel.add(infoButton);

// Right Arrow Button
        JButton rightArrowButton = new JButton();
        rightArrowButton.setFocusable(false);
        rightArrowButton.setBounds(0, 0, 30, 50); // Double the button size

// Load and scale the images for the right arrow button
        ImageIcon rightDefaultIcon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("img/menu/right1.png")).getImage().getScaledInstance(30, 50, Image.SCALE_SMOOTH));
        ImageIcon rightHoverIcon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("img/menu/right2.png")).getImage().getScaledInstance(30, 50, Image.SCALE_SMOOTH));
        ImageIcon rightClickIcon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("img/menu/right3.png")).getImage().getScaledInstance(30, 50, Image.SCALE_SMOOTH));

// Set the default icon
        rightArrowButton.setIcon(rightDefaultIcon);
        rightArrowButton.setContentAreaFilled(false);
        rightArrowButton.setBorderPainted(false);
        rightArrowButton.setFocusPainted(false);

// Add action listener for click event
        rightArrowButton.addActionListener(e -> {
            SoundManager.playSound(Sound.BUTTON_CLICK);
            carousel.rotateGame(-1);
            panel.repaint();
        });

// Add mouse listener for hover and click events
        rightArrowButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                rightArrowButton.setIcon(rightHoverIcon);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                rightArrowButton.setIcon(rightDefaultIcon);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                rightArrowButton.setIcon(rightClickIcon);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                rightArrowButton.setIcon(rightHoverIcon);
            }
        });

        panel.add(rightArrowButton);

// Left Arrow Button
        JButton leftArrowButton = new JButton();
        leftArrowButton.setFocusable(false);
        leftArrowButton.setBounds(5, 0, 30, 50); // Double the button size

// Load and scale the images for the left arrow button
        ImageIcon leftDefaultIcon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("img/menu/left1.png")).getImage().getScaledInstance(30, 50, Image.SCALE_SMOOTH));
        ImageIcon leftHoverIcon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("img/menu/left2.png")).getImage().getScaledInstance(30, 50, Image.SCALE_SMOOTH));
        ImageIcon leftClickIcon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("img/menu/left3.png")).getImage().getScaledInstance(30, 50, Image.SCALE_SMOOTH));

// Set the default icon
        leftArrowButton.setIcon(leftDefaultIcon);
        leftArrowButton.setContentAreaFilled(false);
        leftArrowButton.setBorderPainted(false);
        leftArrowButton.setFocusPainted(false);

// Add action listener for click event
        leftArrowButton.addActionListener(e -> {
            SoundManager.playSound(Sound.BUTTON_CLICK);
            carousel.rotateGame(1);
            panel.repaint();
        });

// Add mouse listener for hover and click events
        leftArrowButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                leftArrowButton.setIcon(leftHoverIcon);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                leftArrowButton.setIcon(leftDefaultIcon);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                leftArrowButton.setIcon(leftClickIcon);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                leftArrowButton.setIcon(leftHoverIcon);
            }
        });

        panel.add(leftArrowButton);

// Adjust button positions when the panel is resized
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int centerY = (panel.getHeight() - 50) / 2; // Adjust for the new button height
                leftArrowButton.setLocation(5, centerY); // Adjust for the new button width
                rightArrowButton.setLocation(panel.getWidth() - 35, centerY); // Adjust for the new button width
                infoButton.setLocation(settingsButton.getX() + 60, settingsButton.getY() + 5);
            }
        });

        leftArrowButton.setVisible(true);
        rightArrowButton.setVisible(true);

        // Hinzufügen des bestehenden Panels
        panel.setBounds(0, 0, 815, 600);
        layeredPane.add(panel, JLayeredPane.DEFAULT_LAYER);

        this.setVisible(true);
    }

    public void updateButtonColor(JButton button, boolean isTransparent) {
        if (isTransparent) {
            switch (selectedTheme) {
                case "Original":
                    button.setBackground(transparentOriginalTheme);
                    break;
                case "Dark":
                    button.setBackground(transparentDarkTheme);
                    break;
                case "Darkblue":
                    button.setBackground(transparentDarkblueTheme);
                    break;
                case "Scarlet":
                    button.setBackground(transparentScarletTheme);
                    break;
            }
        } else {
            switch (selectedTheme) {
                case "Original":
                    button.setBackground(originalTheme);
                    break;
                case "Dark":
                    button.setBackground(darkTheme);
                    break;
                case "Darkblue":
                    button.setBackground(darkblueTheme);
                    break;
                case "Scarlet":
                    button.setBackground(scarletTheme);
                    break;
            }
        }
    }

    // Methode, um den Button zu stylen
    public void styleButton(JButton button) {
        button.setBackground(new Color(78, 136, 174, 255));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(150, 40)); // Größe setzen
        button.addActionListener(this);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(e -> {
            SoundManager.playSound(Sound.BUTTON_CLICK);
        });

        // Create a thin line border
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

    public void saveSelectedTheme(String theme) {
        this.selectedTheme = theme;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getClass().getClassLoader().getResource("config/theme.txt").getFile()))) {
            writer.write(theme);
            System.out.println("Theme saved: " + theme);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMultiplayerName() {
        return MultiplayerName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton sourceButton = (JButton) e.getSource();
        if (sourceButton == startButton) {
            startAnimationFrame = 2;
            carousel.getSelectedGame().startGame();
        }
    }

    public void startMultiplayerPoker() {
        while (true) {
            MultiplayerName = JOptionPane.showInputDialog(this, "Bitte geben Sie Ihren Namen ein:", "Name eingeben (1-12 Zeichen)", JOptionPane.PLAIN_MESSAGE);
            if (MultiplayerName == null) {
                break;
            } else if (!MultiplayerName.isEmpty() && !MultiplayerName.contains(",") && MultiplayerName.length() <= 12) {
                new MultiplayerGUI(this);
                this.setVisible(false);
                break;
            }
            JOptionPane.showMessageDialog(this, "1-12 Zeichen & Keine \",\"", "Ungültiger Name", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        MainGUI.getInstance();
    }

    private void loadSelectedTheme() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config/theme.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String theme = reader.readLine();
            if (theme != null) {
                selectedTheme = theme;
            } else {
                selectedTheme = "Original"; // Default theme
            }
        } catch (Exception e) {
            System.out.println("Theme file not found or invalid. Using default theme.");
            selectedTheme = "Original"; // Default theme in case of error
        }
    }

    public void updateSelectedTheme() {
        // Datei zum Speichern des Themes
        String THEME_FILE = "theme.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(THEME_FILE))) {
            String theme = reader.readLine();
            if (theme != null) {
                this.selectedTheme = theme;
                System.out.println("Theme loaded: " + theme);
            } else {
                this.selectedTheme = "Original"; // Default theme if file is empty
            }
        } catch (FileNotFoundException e) {
            System.out.println("Theme file not found. Using default theme.");
            this.selectedTheme = "Original"; // Default theme if file is not found
        } catch (IOException e) {
            e.printStackTrace();
            this.selectedTheme = "Original"; // Default theme in case of an error
        }
    }

    public String getSelectedTheme() {
        return selectedTheme;
    }

    public float getGameSoundsVolume() {
        return gameSoundsVolume;
    }

    public void setGameSoundsVolume(float volume) {
        gameSoundsVolume = volume;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        if (notches < 0) {
            carousel.rotateGame(-1);
        } else {
            carousel.rotateGame(1);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case 37:
                carousel.rotateGame(1);
                break;
            case 39:
                carousel.rotateGame(-1);
                break;
            case 73:
                infoButton.doClick();
                break;
            case 10, 32:
                startButton.doClick();
                break;
            case 83:
                settingsButton.doClick();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    public static MainGUI getInstance() {
        return INSTANCE;
    }
}