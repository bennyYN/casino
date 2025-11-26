package de.ben.games;

import de.ben.MainGUI;
import de.ben.blackjack.GameSettings;
import de.ben.playground.althenator.AlthenatorGUI;
import de.ben.playground.althenpong.PongGUI;
import de.ben.playground.escape_the_althen.MenuFrame;
import de.ben.poker.PlayerSelection;

//TODO: MainGUI nicht als Parameter mitgeben
public enum GameStartAction {

    BLACKJACK(() -> {
        new GameSettings(MainGUI.getInstance());
        MainGUI.getInstance().setVisible(false);
    }),

    POKER_GROUP(() -> {
        new PlayerSelection(MainGUI.getInstance());
        MainGUI.getInstance().dispose();
    }),

    POKER_MULTIPLAYER(() -> {
        MainGUI.getInstance().startMultiplayerPoker();
    }),

    ALTHENPONG(() -> {
        new PongGUI(MainGUI.getInstance());
        MainGUI.getInstance().setVisible(false);
    }),

    FLAPPYSCHMANDT(() -> {
        new de.ben.playground.flappyschmandt.FlappySchmandtGUI(MainGUI.getInstance());
        MainGUI.getInstance().setVisible(false);
    }),

    ALTHENATOR(() -> {
        new AlthenatorGUI(MainGUI.getInstance());
        MainGUI.getInstance().setVisible(false);
    }),

    ESCAPETHEALTHEN(() -> {
        new MenuFrame(MainGUI.getInstance());
        MainGUI.getInstance().setVisible(false);
    });

    private final Action startAction;

    GameStartAction(Action startAction) {
        this.startAction = startAction;
    }

    public void execute() {
        startAction.execute();
    }
}
