package de.ben.playground.flappyschmandt;

import com.sun.tools.javac.Main;
import de.ben.MainGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class FlappySchmandtGUI extends JFrame {

    JPanel gamePanel;
    Image background = new ImageIcon("img/playground/flappyschmandt/background.png").getImage();
    Rectangle floor = new Rectangle(0, 990, 1920, 100);
    public double xOffset = 0;
    Schmandt player;
    List<Hindernis> obstacles;
    Timer timer;
    int obstacleSpeed = 5;
    Random random = new Random();
    boolean isPaused = false;
    private JButton resumeButton;
    private JButton restartButton;
    private JButton exitButton;

    public FlappySchmandtGUI(MainGUI mainGUI) {
        this.setTitle("Flappy Schmandt");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1920, 1080);
        this.setResizable(false);
        this.setVisible(true);
        this.setFocusable(false);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLayout(new BorderLayout());

        player = new Schmandt(700, 300, 75, 75);
        obstacles = new CopyOnWriteArrayList<>();
        generateObstacles();

        gamePanel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                // Background
                for (int i = 0; i < getWidth() / background.getWidth(null) + 2; i++) {
                    g.drawImage(background, (int) xOffset + background.getWidth(null) * i, -20, null);
                }

                player.render(g);
                for (Hindernis obstacle : obstacles) {
                    obstacle.render(g);
                }

                if (isPaused) {
                    resumeButton.setVisible(true);
                    restartButton.setVisible(true);
                    exitButton.setVisible(true);
                } else {
                    resumeButton.setVisible(false);
                    restartButton.setVisible(false);
                    exitButton.setVisible(false);
                }

                // Abtönungsschicht um den Score anzuzeigen
                g.setColor(new Color(0, 0, 0, 100));
                g.fillRect(0, 0, 200, 50);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString("Score: " + Schmandt.score, 10, 30); // Display the score from Schmandt class

                //Abtönungsschicht für Tod
                if(!player.isAlive) {
                    g.setColor(new Color(142, 28, 28, 111));
                    g.fillRect(0, 0, 1920, 1080);
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("Arial", Font.BOLD, 50));
                    g.drawString("GAME OVER!", 1920/2 - 200, 1080/2);
                }

                //Abtönungsschicht für Pausenmenü
                if(isPaused) {
                    g.setColor(new Color(0, 0, 0, 100));
                    g.fillRect(0, 0, 1920, 1080);
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("Arial", Font.BOLD, 50));
                    g.drawString("PAUSED", 1920/2 - 100, 1080/2);
                }
            }
        };
        gamePanel.setPreferredSize(new Dimension(1920, 1080));
        this.add(gamePanel, BorderLayout.CENTER);

        // Initialize buttons
        resumeButton = new JButton("Resume");
        restartButton = new JButton("Restart");
        exitButton = new JButton("Exit");

        //do not make the buttons focusable
        resumeButton.setFocusable(false);
        restartButton.setFocusable(false);
        exitButton.setFocusable(false);

        //Style Buttons
        mainGUI.styleButton(resumeButton);
        mainGUI.styleButton(restartButton);
        mainGUI.styleButton(exitButton);

        // Add buttons to the game panel
        gamePanel.setLayout(null);
        resumeButton.setBounds(860, 400, 200, 50);
        restartButton.setBounds(860, 460, 200, 50);
        exitButton.setBounds(860, 520, 200, 50);

        gamePanel.add(resumeButton);
        gamePanel.add(restartButton);
        gamePanel.add(exitButton);

        // Initially hide buttons
        resumeButton.setVisible(false);
        restartButton.setVisible(false);
        exitButton.setVisible(false);

        // Actionlisteners for Buttons
        resumeButton.addActionListener(e -> {
            isPaused = false;
            gamePanel.requestFocusInWindow();
        });
        restartButton.addActionListener(e -> {
            new FlappySchmandtGUI(mainGUI);
            this.dispose();
        });
        exitButton.addActionListener(e -> {
            mainGUI.setVisible(true);
            this.dispose();
        });

        gamePanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if ((e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) && !isPaused) {
                    player.jump();
                }
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                    isPaused = !isPaused;
                }
            }
        });
        gamePanel.setFocusable(true);
        gamePanel.requestFocusInWindow();
        gamePanel.setFocusable(true);

        timer = new Timer(20, e -> update());
        timer.start();
    }

    private boolean isOverlapping(int x, int width) {
        for (Hindernis obstacle : obstacles) {
            if (x < obstacle.x + obstacle.width && x + width > obstacle.x) {
                return true;
            }
        }
        return false;
    }

    private void generateType1(int lastX) {
        int height1 = 200 + random.nextInt(300); // Random height between 200 and 500
        int height2 = 400 + random.nextInt(300); // Random height between 200 and 500
        int yGap = 250 + random.nextInt(120); // Random y-gap between 200 and 280
        if (random.nextDouble() < 0.975) { // 95% chance to connect to the floor
            height2 = 1080 - (height1 + yGap);
        }
        obstacles.add(new Hindernis(lastX, 0, 100, height1));
        obstacles.add(new Hindernis(lastX, height1 + yGap, 100, height2));
    }

    private void generateType2(int lastX) {
        int height1 = 200 + random.nextInt(300); // Random height between 200 and 500
        int height2 = 200 + random.nextInt(300); // Random height between 200 and 500
        int yGap = 200 + random.nextInt(80); // Random y-gap between 200 and 280
        if (random.nextDouble() < 0.95) { // 95% chance to connect to the floor
            height2 = 1080 - (height1 + yGap);
        }
        obstacles.add(new Hindernis(lastX, 0, 130, height1));
        obstacles.add(new Hindernis(lastX + 370, height1 + yGap, 100, height2));
    }

    private void generateType3(int lastX) {
        int height1 = 200 + random.nextInt(300); // Random height between 200 and 500
        int height2 = 200 + random.nextInt(300); // Random height between 200 and 500
        int yGap = 220 + random.nextInt(80); // Random y-gap between 200 and 280
        if (random.nextDouble() < 0.95) { // 95% chance to connect to the floor
            height2 = 1080 - (height1 + yGap);
        }
        obstacles.add(new Hindernis(lastX, 0, 100, height1));
        obstacles.add(new Hindernis(lastX + 370, height1 + yGap, 100, height2));
    }

    private void generateType4(int lastX) {
        int height1 = 200 + random.nextInt(300); // Random height between 200 and 500
        int yGap = 270 + random.nextInt(80); // Random y-gap between 200 and 280
        if (random.nextDouble() < 0.95) { // 95% chance to connect to the floor
            obstacles.add(new Hindernis(lastX, 0, 130, height1));
            obstacles.add(new Hindernis(lastX, height1 + yGap, 100, 1080 - (height1 + yGap)));
        } else {
            obstacles.add(new Hindernis(lastX, 0, 100, height1));
            obstacles.add(new Hindernis(lastX, height1 + yGap, 100, 200 + random.nextInt(300)));
        }
    }

    private void generateType5(int lastX) {
        int height1 = 200 + random.nextInt(300); // Random height between 200 and 500
        int yGap = 300 + random.nextInt(80); // Random y-gap between 200 and 280
        if (random.nextDouble() < 0.95) { // 95% chance to connect to the floor
            obstacles.add(new Hindernis(lastX, 0, 130, height1));
            obstacles.add(new Hindernis(lastX, 1080 - height1 - yGap, 100, height1));
        } else {
            obstacles.add(new Hindernis(lastX, 0, 130, height1));
            obstacles.add(new Hindernis(lastX, 1080 - height1 - yGap, 100, 200 + random.nextInt(300)));
        }
    }

    private void update() {
        if (isPaused) {
            resumeButton.setVisible(true);
            restartButton.setVisible(true);
            exitButton.setVisible(true);
        } else {
            resumeButton.setVisible(false);
            restartButton.setVisible(false);
            exitButton.setVisible(false);
        }
        if (!isPaused) {
            if (!player.isAlive) {
                repaint();
                return;
            }

            long startTime = System.currentTimeMillis();

            player.update();
            xOffset -= 2;
            if (xOffset <= -background.getWidth(null)) {
                xOffset = 0;
            }
            if (player.intersects(floor) || player.y <= 0) {
                player.isAlive = false;
                timer.stop();
            }

            List<Hindernis> toRemove = new ArrayList<>();
            for (Hindernis obstacle : obstacles) {
                obstacle.update(obstacleSpeed);
                if (obstacle.x + obstacle.width < 0) {
                    toRemove.add(obstacle);
                }
                if (player.intersects(obstacle.getBox())) {
                    player.isAlive = false;
                    timer.stop();
                }
                // Increase score when player passes an obstacle
                if (!obstacle.isScored && player.x > obstacle.x + obstacle.width) {
                    Schmandt.score++;
                    obstacle.isScored = true;
                }
            }
            obstacles.removeAll(toRemove);

            // Ensure new obstacles are generated with the correct gap
            if (obstacles.isEmpty() || obstacles.get(obstacles.size() - 1).x + obstacles.get(obstacles.size() - 1).width + 450 < 1920) {
                generateObstacles();
            }

            gamePanel.repaint();

            long endTime = System.currentTimeMillis();
            long sleepTime = 5 - (endTime - startTime); // 20 ms for 50 FPS
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            gamePanel.repaint();
        }
    }

    private void generateObstacles() {
        int lastX = obstacles.isEmpty() ? 1920 : obstacles.get(obstacles.size() - 1).x + obstacles.get(obstacles.size() - 1).width;
        int minGap = 450; // Minimum gap between obstacles
        int maxGap = 950; // Maximum gap between obstacles

        while (lastX < 1920 * 3) { // Generate obstacles for 3 screen widths
            int generationType = random.nextInt(5); // Choose a generation type randomly
            int newX = lastX + minGap + random.nextInt(maxGap - minGap); // Ensure gap is within the defined range
            int width = 100; // Assuming obstacle width is 100

            // Ensure the new obstacle does not overlap with existing obstacles
            while (isOverlapping(newX, width)) {
                newX += 50; // Adjust the position to avoid overlap
            }

            switch (generationType) {
                case 0:
                    generateType1(newX);
                    break;
                case 1:
                    generateType2(newX);
                    break;
                case 2:
                    generateType3(newX);
                    break;
                case 3:
                    generateType4(newX);
                    break;
                case 4:
                    generateType5(newX);
                    break;
            }
            lastX = newX + width;
        }
    }
}