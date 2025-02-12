package de.ben.playground.althenpong;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class CollisionFX {

    public static int colliding = 0;
    private static BufferedImage[] leftFrames = new BufferedImage[8];
    private static BufferedImage[] rightFrames = new BufferedImage[8];
    private static int speed = 75; // Speed in milliseconds
    public static Image currentFrame;

    static {
        try {
            for (int i = 0; i < 8; i++) {
                leftFrames[i] = ImageIO.read(new File("img/playground/althenpong/fx/leftcollision" + (i + 1) + ".png"));
                rightFrames[i] = ImageIO.read(new File("img/playground/althenpong/fx/rightcollision" + (i + 1) + ".png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setSpeed(int newSpeed) {
        speed = newSpeed;
    }

    public static void startAnimation() {
        boolean isLeft = colliding == 1;
        new Thread(() -> {
            BufferedImage[] frames = isLeft ? leftFrames : rightFrames;
            for (BufferedImage frame : frames) {
                    currentFrame = frame;
                try {
                    Thread.sleep(speed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            SwingUtilities.invokeLater(() -> {
                currentFrame = new ImageIcon("").getImage();
                colliding = 0;
            });
        }).start();
    }
}