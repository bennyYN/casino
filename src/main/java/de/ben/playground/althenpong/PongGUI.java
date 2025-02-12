package de.ben.playground.althenpong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class PongGUI extends JFrame implements KeyListener {

    CollisionFX cfx = new CollisionFX();
    boolean passedLastCollisionCheck = true, didInvert = false;
    Image logo = new ImageIcon("img/playground/althenpong/logo.png").getImage();
    Player player1 = new Player(40);
    Player player2 = new Player(1730);
    JPanel gamePanel;
    Ball ball = new Ball();
    Goal leftGoal = new Goal(1);
    Goal rightGoal = new Goal(1800);
    JPanel infoPanel;

    public PongGUI() {
        setTitle("Althen-Pong");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Set window icon
        Image icon = new ImageIcon("img/playground/althenpong/althen_icon.png").getImage();
        setIconImage(icon);

        // Infopanel
        infoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(32, 48, 58));
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.PLAIN, 70));
                g.drawString(String.valueOf(player1.score), 50, 120);
                if(player2.score >= 10){
                    g.drawString(String.valueOf(player2.score), 1780, 120);
                }else{
                    g.drawString(String.valueOf(player2.score), 1820, 120);
                }
                g.drawImage(logo, (1920-800)/2, 50, 800, 100, null);

            }
        };
        infoPanel.setPreferredSize(new Dimension(1920, 200));
        add(infoPanel, BorderLayout.NORTH);

        // Border panels
        JPanel leftBorderPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(32, 48, 58));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        leftBorderPanel.setPreferredSize(new Dimension(50, 830));
        add(leftBorderPanel, BorderLayout.WEST);

        JPanel rightBorderPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(32, 48, 58));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        rightBorderPanel.setPreferredSize(new Dimension(50, 830));
        add(rightBorderPanel, BorderLayout.EAST);

        JPanel bottomBorderPanel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(32, 48, 58));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bottomBorderPanel.setPreferredSize(new Dimension(1920, 50));
        add(bottomBorderPanel, BorderLayout.SOUTH);

        // Gamepanel
        gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                PongGUI.this.update();
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());
                ball.render(g);
                player1.render(g);
                player2.render(g);




                //CollisionFX
                if(cfx.colliding == 1){
                    int fxX = (int)(ball.getSavedX() + ball.width / 2);
                    int fxY = (int)(ball.getSavedY() + ball.height / 2);
                    g.drawImage(cfx.currentFrame, fxX-30, fxY-50, 200, 200, null);
                }else if(cfx.colliding == 2){
                    int fxX = (int)(ball.getSavedX() - ball.width / 2);
                    int fxY = (int)(ball.getSavedY() - ball.height / 2);
                    g.drawImage(cfx.currentFrame, fxX-110, fxY-30, 200, 200, null);

                }
                repaint();
            }
        };
        add(gamePanel, BorderLayout.CENTER);

        gamePanel.setFocusable(true);
        gamePanel.addKeyListener(this);

        setVisible(true);
    }

    private void update() {
        try{
            Thread.sleep(10);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        // Player collision
        if (ball.intersects(player1) || ball.intersects(player2)) {
            ball.xVelocity *= -1;
            if(!passedLastCollisionCheck && !didInvert){
                ball.yVelocity *= -1;
                didInvert = true;
            }
            passedLastCollisionCheck = false;
            ball.saveCurrentPosition();
            if(ball.intersects(player1)){
                cfx.colliding = 1;
            }else{
                cfx.colliding = 2;
            }
            cfx.startAnimation();

        }else{
            passedLastCollisionCheck = true;
            didInvert = false;
        }

        // Goal collision
        if (ball.intersects(leftGoal)) {
            ball.xPos = 1920/2;
            ball.yPos = 1080/2;
            ball.xVelocity *= -1;
            ball.yVelocity *= -1;
            player2.score++;
            infoPanel.repaint();
        } else if (ball.intersects(rightGoal)) {
            ball.xPos = 1920/2;
            ball.yPos = 1080/2;
            ball.xVelocity *= -1;
            ball.yVelocity *= -1;
            player1.score++;
            infoPanel.repaint();
        }


        // Wall collision
        if (ball.yPos <= 0 || ball.yPos + ball.height >= gamePanel.getHeight()) {
            ball.yVelocity *= -1;
        }
        if (ball.xPos <= 0 || ball.xPos + ball.width >= gamePanel.getWidth()) {
            ball.xVelocity *= -1;
        }
        ball.move();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP) {
            player2.yVelocity = -8;
        } else if (key == KeyEvent.VK_DOWN) {
            player2.yVelocity = 8;
        } else if (key == KeyEvent.VK_W) {
            player1.yVelocity = -8;
        } else if (key == KeyEvent.VK_S) {
            player1.yVelocity = 8;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
            player2.yVelocity = 0;
        } else if (key == KeyEvent.VK_W || key == KeyEvent.VK_S) {
            player1.yVelocity = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        new PongGUI();
    }
}