package de.ben.sound;

/**
 * <h3>Sound</h3>
 * Enumeration class used for registering every (one-shot) sound in the application.
 *
 * @author Maurice Steimer
 */
public enum Sound {

    // User Interface Sounds
    /**
     * Sound played when an invalid action is performed by the user.
     */
    INVALID_ACTION("sounds/invalid_action.wav", SoundChannel.USER_INTERFACE),

    /**
     * Sound played when a valid action is performed by the user.
     */
    VALID_ACTION("sounds/valid_action.wav", SoundChannel.USER_INTERFACE),

    /**
     * Sound played when the menu is closed.
     */
    CLOSE_MENU("sounds/close_menu.wav", SoundChannel.USER_INTERFACE),

    /**
     * Sound played when the menu is opened.
     */
    OPEN_MENU("sounds/open_menu.wav", SoundChannel.USER_INTERFACE),

    /**
     * Sound played when a button is clicked.
     */
    BUTTON_CLICK("sounds/click_button.wav", SoundChannel.USER_INTERFACE),

    /**
     * Sound played when the game is won.
     */
    WIN_GAME("sounds/win_game.wav", SoundChannel.USER_INTERFACE),

    // Ingame Sounds
    /**
     * Sound played when a level is completed.
     */
    LEVEL_COMPLETED("sounds/level_completed.wav", SoundChannel.INGAME),

    /**
     * Temporary sound just for fun.
     */
    @Deprecated
    ZIMMER_HORN("sounds/zimmer_horn.wav", SoundChannel.INGAME),

    /**
     * Sound played when revealing a card.
     */
    VIEW_CARD_1("sounds/view_card_1.wav", SoundChannel.INGAME),

    /**
     * Alternative version of the sound played when revealing a card.
     */
    VIEW_CARD_2("sounds/view_card_2.wav", SoundChannel.INGAME),

    /**
     * Sound played when closing a door.
     */
    CLOSE_DOOR("sounds/close_door.wav", SoundChannel.INGAME),

    /**
     * Sound played when opening a door.
     */
    OPEN_DOOR("sounds/open_door.wav", SoundChannel.INGAME),

    /**
     * Sound played when flipping a card.
     */
    FLIP_CARD("sounds/flip_card.wav", SoundChannel.INGAME),

    /**
     * Sound played when breaking a pot.
     */
    BREAK_POT("sounds/break_pot.wav", SoundChannel.INGAME),

    /**
     * Sound played when toggling a switch.
     */
    TOGGLE_1("sounds/toggle_1.wav", SoundChannel.INGAME),

    /**
     * Alternate sound played when toggling a switch.
     */
    TOGGLE_2("sounds/toggle_2.wav", SoundChannel.INGAME);

    /**
     * The file path of the .wav file.
     */
    private final String filePath;

    /**
     * The channel the sound is categorized under.
     */
    private final SoundChannel channel;

    /**
     * Constructor, initializes the file path and channel of the sound upon this enum getting instanced.
     *
     * @param filePath The file path of the .wav file.
     * @param channel  The channel the sound is categorized under.
     */
    Sound(String filePath, SoundChannel channel) {
        this.filePath = filePath;
        this.channel = channel;
    }

    public String getFilePath() {
        return filePath;
    }
}