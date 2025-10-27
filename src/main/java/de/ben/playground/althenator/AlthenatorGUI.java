package de.ben.playground.althenator;

import de.ben.MainGUI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;


//import static de.ben.MainGUI.playSound;

public class AlthenatorGUI extends JFrame implements KeyListener {

    private double difficultyIncrement = 0.7;
    private double timeDecrement = 0.8;
    Image logo = new ImageIcon(getClass().getClassLoader().getResource("img/playground/althenator/logo.png")).getImage();
    JPanel gamePanel;
    JPanel infoPanel;
    boolean paused = false, started = false, gameover = false;
    JButton backButton, menuButton, restartButton;
    Sicherung[][] sicherungen = new Sicherung[3][12];
    Althenator althenator;
    JLayeredPane layeredPane;
    TransparentPanel transparentPanel;
    private  int MINIMUM_INTERVAL = 1000, MAXIMUM_INTERVAL = 5000;
    private Timer timer;
    private Random random = new Random();
    private Timer gameTimer;
    private int timeRemaining = 60; // 1 minute in seconds
    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> gameTimerFuture;

    public AlthenatorGUI(MainGUI mainGUI) {
        setTitle("Althenator II - Der Schirmherr der fliegenden Sicherungen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create a JLayeredPane
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1920, 1080));
        add(layeredPane, BorderLayout.CENTER);

        // Load Althenator centered
        althenator = new Althenator(1920 / 2 - 150, 830 / 2 - 264, this);

        // Set window icon
        Image icon = new ImageIcon(getClass().getClassLoader().getResource("img/playground/althenpong/althen_icon.png")).getImage();
        setIconImage(icon);

        // Infopanel
        infoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(32, 48, 58));
                g.fillRect(0, 0, getWidth(), getHeight());

                // Menü
                if (paused || !started) {
                    g.setColor(new Color(28, 37, 80, 190));
                    g.fillRect(0, 0, 1920, 1080);
                }

                g.drawImage(logo, (1920 - 800) / 2, 25, 800, 100, null);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.PLAIN, 40));
                g.drawString("Score: " + althenator.getScore() + "   " + "Time: " + timeRemaining, (1920 - g.getFontMetrics().stringWidth("Score: " + althenator.getScore() + "   " + "Time: " + timeRemaining)) / 2, 150);
            }
        };
        infoPanel.setBounds(0, 0, 1920, 200);
        layeredPane.add(infoPanel, JLayeredPane.DEFAULT_LAYER);

        // Border panels
        JPanel leftBorderPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(32, 48, 58));
                g.fillRect(0, 0, getWidth(), getHeight());
                if (paused || !started) {
                    g.setColor(new Color(28, 37, 80, 190));
                    g.fillRect(0, 0, 1920, 1080);
                }
            }
        };
        leftBorderPanel.setBounds(0, 200, 50, 830);
        layeredPane.add(leftBorderPanel, JLayeredPane.DEFAULT_LAYER);

        JPanel rightBorderPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(32, 48, 58));
                g.fillRect(0, 0, getWidth(), getHeight());
                if (paused || !started) {
                    g.setColor(new Color(28, 37, 80, 190));
                    g.fillRect(0, 0, 1920, 1080);
                }
            }
        };
        rightBorderPanel.setBounds(1870, 200, 50, 830);
        layeredPane.add(rightBorderPanel, JLayeredPane.DEFAULT_LAYER);

        JPanel bottomBorderPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(32, 48, 58));
                g.fillRect(0, 0, getWidth(), getHeight());
                if (paused || !started) {
                    g.setColor(new Color(28, 37, 80, 190));
                    g.fillRect(0, 0, 1920, 1080);
                }
            }
        };
        bottomBorderPanel.setBounds(0, 1030, 1920, 50);
        layeredPane.add(bottomBorderPanel, JLayeredPane.DEFAULT_LAYER);

        // Gamepanel
        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                AlthenatorGUI.this.update();
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());

                repaint();
            }
        };
        gamePanel.setBounds(50, 200, 1820, 830);
        gamePanel.setLayout(null);
        layeredPane.add(gamePanel, JLayeredPane.DEFAULT_LAYER);

        // Initialize Sicherungen
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 12; j++) {
                sicherungen[i][j] = new Sicherung(j, i, althenator);
                gamePanel.add(sicherungen[i][j]);
            }
        }

        backButton = new JButton("Fortfahren");
        menuButton = new JButton("Zum Menü");
        restartButton = new JButton("Neustart");
        styleButton(backButton);
        styleButton(menuButton);
        styleButton(restartButton);
        int xoffset = 18;
        backButton.setBounds(((1820 - 450) / 3) * 1 + 290 - xoffset, (int) (830 / 1.5) , 150, 50);
        menuButton.setBounds(((1820 - 450) / 3) * 2 - xoffset, (int) (830 / 1.5) , 150, 50);
        restartButton.setBounds(((1820 - 450) / 3) * 3 - 290 - xoffset, (int) (830 / 1.5) , 150, 50);
        backButton.setFocusable(false);
        menuButton.setFocusable(false);
        restartButton.setFocusable(false);
        backButton.setVisible(false);
        menuButton.setVisible(false);
        restartButton.setVisible(false);

        // Actionlistener
        backButton.addActionListener(e -> {
            paused = false;
            repaint();
        });
        menuButton.addActionListener(e -> {
            mainGUI.setVisible(true);
            this.dispose();
        });
        restartButton.addActionListener(e -> {
            new AlthenatorGUI(mainGUI);
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            this.dispose();
        });

        gamePanel.setFocusable(true);
        gamePanel.addKeyListener(this);

        // Add TransparentPanel
        transparentPanel = new TransparentPanel();
        transparentPanel.setBounds(0, 0, 1920, 1080);
        layeredPane.add(transparentPanel, JLayeredPane.DRAG_LAYER);

        transparentPanel.add(backButton);
        transparentPanel.add(menuButton);
        transparentPanel.add(restartButton);

        scheduler = Executors.newScheduledThreadPool(1);

        setVisible(true);
    }

    // Methode, um den Button zu stylen
    private void styleButton(JButton button) {
        button.setBackground(new Color(78, 136, 174, 255));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(150, 40)); // Größe setzen
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(e -> {
            //TODO: MIGRATE -> playSound("click");
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

    private void startGameTimer() {
        gameTimer = new Timer();
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!paused && !gameover) {
                    if (timeRemaining > 0) {
                        timeRemaining--;
                    } else {
                        gameover = true;
                        gameTimer.cancel();
                        repaint();
                    }
                    infoPanel.repaint();
                }
            }
        }, 0, 1000); // Schedule task to run every second
    }

    private void update() {
        if (started && !paused) {
            backButton.setVisible(false);
            menuButton.setVisible(false);
            restartButton.setVisible(false);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (paused || gameover) {
            backButton.setVisible(true);
            menuButton.setVisible(true);
            restartButton.setVisible(true);
            if (gameTimer != null) {
                gameTimer.cancel();
            }
        }
        // Make the game harder over time
        MAXIMUM_INTERVAL -= timeDecrement;
        if (MAXIMUM_INTERVAL < 900) {
            MAXIMUM_INTERVAL = 900;
        }
        MINIMUM_INTERVAL -= timeDecrement / 2;
        if (MINIMUM_INTERVAL < 500) {
            MINIMUM_INTERVAL = 500;
        }
        timeDecrement += difficultyIncrement;
    }

    private void startButtonToggleTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toggleRandomButton();
                // Schedule the next toggle
                while (paused || gameover) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                startButtonToggleTimer();
            }
        }, getRandomInterval());
    }

    private int getRandomInterval() {
        return MINIMUM_INTERVAL + random.nextInt(MAXIMUM_INTERVAL - MINIMUM_INTERVAL + 1);
    }

    private void toggleRandomButton() {
        int row = random.nextInt(sicherungen.length);
        int col = random.nextInt(sicherungen[row].length);
        Sicherung button = sicherungen[row][col];
        button.setRausgeflogen(true);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_ESCAPE && started && !gameover) {
            paused = !paused;
            repaint();
        } else if (key == KeyEvent.VK_SPACE && !started) {
            started = true;
            startGameTimer();
            startButtonToggleTimer();
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    // Inner class for TransparentPanel
    private class TransparentPanel extends JPanel {
        public TransparentPanel() {
            setOpaque(false); // Make the panel transparent
            setLayout(null);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Render Althenator
            try {
                if (althenator.isAlive() && !paused) {
                    althenator.setPosition((int)(this.getMousePosition().getX() - Althenator.WIDTH / 2), (int)(this.getMousePosition().getY() - Althenator.HEIGHT / 2)-105);
                }
            } catch (Exception e) {
                System.out.println("---Cursor außerhalb des Spielpanels---");
            }
            althenator.render(g);

            // Start
            if (!started) {
                g.setColor(new Color(32, 48, 58, 132));
                g.fillRect(0, 0, 1920, 1080);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.PLAIN, 50));
                g.drawString("Drücke [LEERTASTE] zum Starten!", 1820 / 2 - 400, 830 / 3);
            }

            // Gameover Screen
            if (gameover) {
                g.setColor(new Color(46, 0, 0, 189));
                g.fillRect(0, 0, 1920, 1080);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.PLAIN, 100));
                g.drawString("Game Over", 1920 / 2 - 250, 830 / 2 + 25);
            }else if(paused){
                g.setColor(new Color(28, 35, 41, 187));
                g.fillRect(0, 0, 1920, 1080);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.PLAIN, 100));
                g.drawString("Game paused", (1920 - g.getFontMetrics().stringWidth("Game paused")) / 2, 830 / 2 + 25);

            }

            repaint();
        }
    }
}