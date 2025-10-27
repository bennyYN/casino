package de.ben.sound;

/**
 * <h3>Channel</h3>
 * Enumeration of all independent sound channels.
 *
 * @author Maurice Steimer
 */
public enum SoundChannel {

    /**
     * Channel for in-game sounds.
     */
    INGAME(new Channel()),

    /**
     * Channel for user interface sounds.
     */
    USER_INTERFACE(new Channel()),

    /**
     * Channel, exclusively used for background music.
     */
    BACKGROUND_MUSIC(new Channel());

    /**
     * The sound channel associated with this enum.
     */
    private final Channel channel;

    /**
     * Constructor, initializes the sound channel upon this enum getting instanced.
     *
     * @param channel
     */
    SoundChannel(Channel channel) {
        this.channel = channel;
    }

    /**
     * Sets the volume of the selected sound channel.
     * @param volume
     */
    void setVolume(float volume){
        channel.volume = volume;
    }

    private static class Channel{

        float volume = 50.0f;

        public Channel(){

        }
    }
}