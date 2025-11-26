package de.ben.ui.menu.carousel;

import de.ben.games.Game;

import java.awt.*;

public class PlateCarousel {

    PlateSet games = new PlateSet();
    RotationAnimation animation = new RotationAnimation();
    private int selectedGameIndex = 0;
    private int z = 0;

    public PlateCarousel() {
        //codeAusPaintMethode();
    }

    //TODO: besser benennen [render method outsourced from MainGUI.paintComponent()]
    public void codeAusPaintMethode(Graphics g) {

        //Rotation Animation
        if (animation.isAnimating()) {
            if (animation.getCurrentFrame() < 100) {
                if (animation.getDirection() == 1) {

                    //RIGHT ROTATION ANIMATION

                    Graphics2D g2d = (Graphics2D) g.create();

                    //middle game to right
                    g.setColor(new Color(255, 255, 255, (interpolate(178, 0))));
                    g.fillRect(interpolate(273, 549), interpolate(158, 184), interpolate(254, (int) (252 * 0.8)), interpolate(304, (int) (302 * 0.8)));
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, interpolate(1, 0.5f)));
                    g2d.drawImage(games.get(getGameToRight()).getCoverImage().getScaledInstance(interpolate(250, (int) (250 * 0.8)), interpolate(300, (int) (300 * 0.8)), Image.SCALE_SMOOTH), interpolate(275, 550), interpolate(160, 185), null);

                    //right to selection fade out to right
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, interpolate(0.5f, -0.5f)));
                    g2d.drawImage(games.get(getGame2ToRight()).getCoverImage().getScaledInstance((int) (250 * 0.8), (int) (300 * 0.8), Image.SCALE_SMOOTH), interpolate(550, 775), 185, null);

                    //left to selection morph to selection
                    g.setColor(new Color(255, 255, 255, interpolate(0, 178)));
                    g.fillRect(interpolate(49, 273), interpolate(184, 158), interpolate((int) (252 * 0.8), 254), interpolate((int) (302 * 0.8), 304)); //TODO MORPH ANIMATION
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, interpolate(0.5f, 1)));
                    g2d.drawImage(games.get(selectedGameIndex).getCoverImage().getScaledInstance(interpolate((int) (250 * 0.8), 250), interpolate((int) (300 * 0.8), 300), Image.SCALE_SMOOTH), interpolate(50, 275), interpolate(185, 160), null);

                    //fade in left selection from the left
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, interpolate(-0.5f, 0.5f)));
                    g2d.drawImage(games.get(getGameToLeft()).getCoverImage().getScaledInstance((int) (250 * 0.8), (int) (300 * 0.8), Image.SCALE_SMOOTH), interpolate(-175, 50), 185, null);

                    g2d.dispose();

                    animation.doStep();
                } else {

                    //LEFT ROTATION ANIMATION

                    Graphics2D g2d = (Graphics2D) g.create();

                    //middle game to left
                    g.setColor(new Color(255, 255, 255, (interpolate(178, 0))));
                    g.fillRect(interpolate(273, 49), interpolate(158, 184), interpolate(254, (int) (252 * 0.8)), interpolate(304, (int) (302 * 0.8)));
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, interpolate(1, 0.5f)));
                    g2d.drawImage(games.get(getGameToLeft()).getCoverImage().getScaledInstance(interpolate(250, (int) (250 * 0.8)), interpolate(300, (int) (300 * 0.8)), Image.SCALE_SMOOTH), interpolate(275, 50), interpolate(160, 185), null);

                    //left to selection fade out to left
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, interpolate(0.5f, -0.5f)));
                    g2d.drawImage(games.get(getGame2ToLeft()).getCoverImage().getScaledInstance((int) (250 * 0.8), (int) (300 * 0.8), Image.SCALE_SMOOTH), interpolate(50, -175), 185, null);

                    //right to selection morph to selection
                    g.setColor(new Color(255, 255, 255, interpolate(0, 178)));
                    g.fillRect(interpolate(549, 273), interpolate(184, 158), interpolate((int) (252 * 0.8), 254), interpolate((int) (302 * 0.8), 304)); //TODO MORPH ANIMATION
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, interpolate(0.5f, 1)));
                    g2d.drawImage(games.get(selectedGameIndex).getCoverImage().getScaledInstance(interpolate((int) (250 * 0.8), 250), interpolate((int) (300 * 0.8), 300), Image.SCALE_SMOOTH), interpolate(550, 275), interpolate(185, 160), null);

                    //fade in right selection from the right
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, interpolate(-0.5f, 0.5f)));
                    g2d.drawImage(games.get(getGameToRight()).getCoverImage().getScaledInstance((int) (250 * 0.8), (int) (300 * 0.8), Image.SCALE_SMOOTH), interpolate(775, 550), 185, null);

                    g2d.dispose();

                    animation.doStep();
                }
            } else {
                animation.setCurrentFrame(0);
                animation.setAnimatingState(false);
            }
        }
        if (!animation.isAnimating()) {

            animation.setCurrentFrame(0);
            //Display logo of selected game including white shadow to highlight
            g.setColor(new Color(255, 255, 255, 178));
            g.fillRect(273, 158, 254, 304);
            g.drawImage(games.get(selectedGameIndex).getCoverImage(), 275, 160, null);

            //Display logo of game with 50% transparency to the left and right of selected game
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g2d.drawImage(games.get(getGameToLeft()).getCoverImage().getScaledInstance((int) (250 * 0.8), (int) (300 * 0.8), Image.SCALE_SMOOTH), 50, 185, null);
            g2d.drawImage(games.get(getGameToRight()).getCoverImage().getScaledInstance((int) (250 * 0.8), (int) (300 * 0.8), Image.SCALE_SMOOTH), 550, 185, null);

            g2d.dispose();
        }

        //Progressbar using circles to represent a game
        g.setColor(new Color(255, 255, 255, 100));
        z = 0;
        for (int i = 0; i < games.size(); i++) {
            if (i == selectedGameIndex) {
                g.setColor(new Color(255, 255, 255, 255));
            } else {
                g.setColor(new Color(255, 255, 255, 100));
            }
            g.fillOval(360 + z, 492, 8, 8);
            z += 12;
        }
    }


    private int interpolate(int preValue, int postValue) {
        return preValue + (postValue - preValue) * animation.getCurrentFrame() / 100;
    }

    private float interpolate(float preValue, float postValue) {
        return Math.max(0, preValue + (postValue - preValue) * animation.getCurrentFrame() / 100);
    }

    private int getGameToLeft() {
        if (selectedGameIndex == 0) {
            return games.size() - 1;
        } else {
            return selectedGameIndex - 1;
        }
    }

    private int getGameToRight() {
        if (selectedGameIndex == games.size() - 1) {
            return 0;
        } else {
            return selectedGameIndex + 1;
        }
    }

    private int getGame2ToLeft() {
        if (selectedGameIndex == 0) {
            return games.size() - 2;
        } else if (selectedGameIndex == 1) {
            return games.size() - 1;
        } else {
            return selectedGameIndex - 2;
        }
    }

    private int getGame2ToRight() {
        if (selectedGameIndex == games.size() - 1) {
            return 1;
        } else if (selectedGameIndex == games.size() - 2) {
            return 0;
        } else {
            return selectedGameIndex + 2;
        }
    }

    //TODO: wird vll durch Collections.rotate() ersetzt
    public void rotateGame(int direction) {
        animation.setCurrentFrame(50);
        animation.setAnimatingState(false);
        if (direction == -1) {
            animation.setDirection(-1);
            if (selectedGameIndex == games.size() - 1) {
                selectedGameIndex = 0;
            } else {
                selectedGameIndex++;
            }
        } else if (direction == 1) {
            animation.setDirection(1);
            if (selectedGameIndex == 0) {
                selectedGameIndex = games.size() - 1;
            } else {
                selectedGameIndex--;
            }
        }
        animation.setCurrentFrame(0);
        animation.setAnimatingState(true);
    }

    public Game getSelectedGame() {
        return games.get(selectedGameIndex).getGame();
    }
}