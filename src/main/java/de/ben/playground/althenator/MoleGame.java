package de.ben.playground.althenator;

import de.ben.MainGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MoleGame extends JFrame {
    private static final int DEFAULT_GAME_TIME = 30; // Default game time in seconds
    private static final int DEFAULT_MIN_MOLE_APPEAR_INTERVAL = 400; // Default minimum interval in milliseconds
    private static final int DEFAULT_MAX_MOLE_APPEAR_INTERVAL = 1000; // Default maximum interval in milliseconds
    private static final double DEFAULT_MOLE_SPAWN_PROBABILITY = 0.125; // Default mole spawn probability (1/8)
    private static final int DEFAULT_ROWS = 2;
    private static final int DEFAULT_COLS = 8;
    public String cursorState = "inactive";
    private Image activeCursor = new ImageIcon("img/playground/althenator/cursor/active.png").getImage();
    private Image inactiveCursor = new ImageIcon("img/playground/althenator/cursor/inactive.png").getImage();
    private Image electrifiedActiveCursor = new ImageIcon("img/playground/althenator/cursor/electrified_active.png").getImage();

    private int gameTime;
    private int minMoleAppearInterval;
    private int maxMoleAppearInterval;
    private double moleSpawnProbability;
    private int score = 0;
    private int timeLeft;
    private JLabel scoreLabel;
    private JLabel timerLabel;
    private JPanel gamePanel;
    private List<Mole> moles;
    private Timer gameTimer;
    private Timer moleTimer;
    private Random random;
    private int rows;
    private int cols;
    public static MainGUI mainGUI;

    public MoleGame(int rows, int cols, int gameTime, int minMoleAppearInterval, int maxMoleAppearInterval, double moleSpawnProbability) {
        this.rows = rows;
        this.cols = cols;
        this.gameTime = gameTime;
        this.minMoleAppearInterval = minMoleAppearInterval;
        this.maxMoleAppearInterval = maxMoleAppearInterval;
        this.moleSpawnProbability = moleSpawnProbability;
        this.timeLeft = gameTime;
        this.random = new Random(); // Initialize the random object

        setTitle("Althenator II - Der Schutzpatron der FI-Schalter & Schirmherr des Ölpreises");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        scoreLabel = new JLabel("Score: 0");
        timerLabel = new JLabel("Time: " + gameTime);
        JPanel topPanel = new JPanel();
        topPanel.add(scoreLabel);
        topPanel.add(timerLabel);
        add(topPanel, BorderLayout.NORTH);

        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundIcon = new ImageIcon("img/playground/althenator/sicherung.png");
                ImageIcon otherBackgroundIcon = new ImageIcon("img/playground/althenator/rausgeflogene_sicherung.png");
                Image backgroundImage = backgroundIcon.getImage();

                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        int index = i * cols + j;
                        Mole mole = moles.get(index);
                        g.drawImage(mole.usedImage, j * 96, i * 256, this);
                    }
                }
                renderCursor(g);
                repaint();
            }
        };
        gamePanel.setLayout(new GridLayout(rows, cols, 0, 0)); // Set no gaps between buttons
        add(gamePanel);

        moles = new ArrayList<>();
        for (int i = 0; i < rows * cols; i++) {
            Mole mole = new Mole(this);
            moles.add(mole);
            gamePanel.add(mole.getButton());
            if (random.nextDouble() < moleSpawnProbability) {
                mole.appear();
            }
        }

        pack(); // Adjust the size of the JFrame to fit the content

        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                timerLabel.setText("Time: " + timeLeft);
                if (timeLeft <= 0) {
                    gameTimer.stop();
                    moleTimer.stop();
                    checkGameOver();
                }
            }
        });

        moleTimer = new Timer(random.nextInt(maxMoleAppearInterval - minMoleAppearInterval) + minMoleAppearInterval, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = random.nextInt(moles.size());
                moles.get(index).appear();
                moleTimer.setDelay(random.nextInt(maxMoleAppearInterval - minMoleAppearInterval) + minMoleAppearInterval);
            }
        });

        gameTimer.start();
        moleTimer.start();
    }

   /* @Override
    public void paint(Graphics g) {
        super.paint(g);
        try{
            Thread.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        repaint();
    }*/

    private void checkGameOver() {
        if (moles.stream().noneMatch(Mole::isVisible)) {
            int response = JOptionPane.showConfirmDialog(this, "Gewonnen! Noch eine Runde?", "Spiel zuende", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                showSettingsDialog();
                this.dispose();
            } else {
                mainGUI.setVisible(true);
                this.dispose();
            }
        } else if (timeLeft <= 0) {
            int response = JOptionPane.showConfirmDialog(this, "Verloren! Nochmal versuchen?", "Spiel zuende", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {

                showSettingsDialog();
                this.dispose();
            } else {
                mainGUI.setVisible(true);
                this.dispose();
            }
        }
    }

    public void increaseScore() {
        score++;
        repaint();
        scoreLabel.setText("Score: " + score);
        if (moles.stream().noneMatch(Mole::isVisible)) {
            gameTimer.stop();
            moleTimer.stop();
            checkGameOver();
        }
    }

    private void resetGame() {
        score = 0;
        timeLeft = gameTime;
        scoreLabel.setText("Score: 0");
        timerLabel.setText("Zeit: " + gameTime);
        for (Mole mole : moles) {
            mole.hide();
        }
        gameTimer.start();
        moleTimer.start();
    }

    public void addMole(Mole mole) {
        moles.add(mole);
        gamePanel.add(mole.getButton());
        gamePanel.revalidate();
        gamePanel.repaint();
    }

    public static void create(MainGUI givenMainGUI) {
        mainGUI = givenMainGUI;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                showSettingsDialog();
            }
        });
    }

    private void renderCursor(Graphics g) {
        Point mousePosition = getMousePosition();
        if (mousePosition != null) {
            if(cursorState.equals("electrified_active")) {
                g.drawImage(electrifiedActiveCursor, mousePosition.x, mousePosition.y-125, this);
            }else if(cursorState.equals("active")) {
                g.drawImage(activeCursor, mousePosition.x, mousePosition.y-125, this);
            } else {
                g.drawImage(inactiveCursor, mousePosition.x, mousePosition.y-125, this);
            }
        }
    }

    public static void showSettingsDialog() {
        JTextField rowsField = new JTextField(String.valueOf(DEFAULT_ROWS));
        JTextField colsField = new JTextField(String.valueOf(DEFAULT_COLS));
        JTextField gameTimeField = new JTextField(String.valueOf(DEFAULT_GAME_TIME));
        JTextField minIntervalField = new JTextField(String.valueOf(DEFAULT_MIN_MOLE_APPEAR_INTERVAL));
        JTextField maxIntervalField = new JTextField(String.valueOf(DEFAULT_MAX_MOLE_APPEAR_INTERVAL));
        JTextField moleSpawnProbabilityField = new JTextField(String.valueOf(DEFAULT_MOLE_SPAWN_PROBABILITY));

        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("Zeilen:"));
        panel.add(rowsField);
        panel.add(new JLabel("Spalten:"));
        panel.add(colsField);
        panel.add(new JLabel("Spielzeit (s):"));
        panel.add(gameTimeField);
        panel.add(new JLabel("Min Ruhezeit (ms):"));
        panel.add(minIntervalField);
        panel.add(new JLabel("Max Ruhezeit (ms):"));
        panel.add(maxIntervalField);
        panel.add(new JLabel("Kettenreaktion Wahrscheinlichkeit:"));
        panel.add(moleSpawnProbabilityField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Game Settings", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            int rows = Integer.parseInt(rowsField.getText());
            int cols = Integer.parseInt(colsField.getText());
            int gameTime = Integer.parseInt(gameTimeField.getText());
            int minInterval = Integer.parseInt(minIntervalField.getText());
            int maxInterval = Integer.parseInt(maxIntervalField.getText());
            double moleSpawnProbability = Double.parseDouble(moleSpawnProbabilityField.getText());
            new MoleGame(rows, cols, gameTime, minInterval, maxInterval, moleSpawnProbability).setVisible(true);
        } else {
            mainGUI.setVisible(true);
        }
    }

    public void triggerCursorStateChange() {
        new Thread(() -> {
            if (random.nextInt(12) == 0) {
                cursorState = "electrified_active";
            } else {
                cursorState = "active";
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cursorState = "inactive";
        }).start();
    }
}