package de.ben.ui.menu.carousel;

import de.ben.games.Game;

import java.awt.*;
import java.io.IOException;

public class GamePlate {

    private final Game game;

    public GamePlate(Game game) throws IOException {
        this.game = game;
    }

    public void draw(Graphics g, PlateBounds bounds) {
        g.drawImage(getCoverImage().getScaledInstance(bounds.width, bounds.height, Image.SCALE_SMOOTH),
                bounds.x,
                bounds.y,
                bounds.width,
                bounds.height,
                null);
    }

    public Image getCoverImage() {
        try {
            return game.getCoverImage();
        } catch (IOException e) {
            throw new RuntimeException("GamePlate.getCoverImage():\n" + e);
        }
    }

    public Game getGame() {
        return game;
    }

    //TODO: vll statt record eif rectangle inheritance
    public record PlateBounds(int x, int y, int width, int height) {
    }
}