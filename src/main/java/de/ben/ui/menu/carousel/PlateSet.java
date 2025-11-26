package de.ben.ui.menu.carousel;

import de.ben.games.Game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

//TODO: HashSet statt ArrayList nehmen
public class PlateSet extends ArrayList<GamePlate> {

    public PlateSet() {
        super(Arrays.stream(Game.values()).map(game -> {
            try {
                return new GamePlate(game);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList());
    }

}
