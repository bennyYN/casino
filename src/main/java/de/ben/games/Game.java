package de.ben.games;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public enum Game {

    BLACKJACK(
            "blackjack",
            "Blackjack",
            "Spiele Blackjack gegen einen Bot-Dealer.",
            GameStartAction.BLACKJACK),

    POKER_GROUP(
            "poker1",
            "Poker (Group Game)",
            "Gruppenversion von Poker, die an einem Rechner gespielt wird.",
            GameStartAction.POKER_GROUP),

    POKER_MULTIPLAYER(
            "poker2",
            "Poker (Multiplayer)",
            "Multiplayerversion von Poker, die online mit anderen gespielt wird.",
            GameStartAction.POKER_MULTIPLAYER),

    ALTHENPONG(
            "althenpong",
            "Althen-Pong",
            "Das klassische Pong mit BG-Star Andreas Althen.",
            GameStartAction.ALTHENPONG),

    FLAPPYSCHMANDT(
            "flappyschmandt",
            "Flappy-Schmandt",
            "Begleite und helfe Flappy-Schmandt auf ihrer Reise in die Ferne.",
            GameStartAction.FLAPPYSCHMANDT),

    ALTHENATOR(
            "althenator",
            "Althenator II",
            "Die Sicherungen fliegen nacheinander raus und nur der Althenator kann nun helfen.",
            GameStartAction.ALTHENATOR),

    ESCAPETHEALTHEN(
            "escapethealthen",
            "Escape the Althen!",
            "Keiner entkam jemals dem Althen. Kannst du es schaffen?",
            GameStartAction.ESCAPETHEALTHEN);

    private final String id, title, description;
    private final GameStartAction gameStartAction;

    Game(String id, String title, String description, GameStartAction gameStartAction) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.gameStartAction = gameStartAction;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public BufferedImage getCoverImage() throws IOException {
        return ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("img/menu/" + id + ".png")));
    }

    public void startGame(){
        gameStartAction.execute();
    }
}