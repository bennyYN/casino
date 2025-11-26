package de.ben.sound;

import de.ben.MainGUI;

import javax.sound.sampled.*;
import java.io.*;
import java.net.URL;

/**
 *
 * @author Maurice Steimer
 */
public class SoundManager {

    private FloatControl volumeControl;
    private static float gameSoundsVolume = 50; // Default game sounds volume

    public SoundManager(){
        loadVolume();
        loadGameSoundsVolume();
        initMusicPlayer();
    }

    public static void playSound(Sound sound) {
        triggerSound(sound.getFilePath());
    }

    private static void triggerSound(String soundFilePath) {
        try (InputStream soundStream = MainGUI.class.getClassLoader().getResourceAsStream(soundFilePath)) {
            if (soundStream == null) {
                throw new IllegalArgumentException("Sound file not found: " + soundFilePath);
            }
            File tempFile = File.createTempFile("tempSound", ".wav");
            tempFile.deleteOnExit();
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                soundStream.transferTo(out);
            }
            AudioInputStream originalAudioStream = AudioSystem.getAudioInputStream(tempFile);

            // Convert the audio format to PCM_SIGNED if necessary
            AudioFormat originalFormat = originalAudioStream.getFormat();
            AudioFormat targetFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    originalFormat.getSampleRate(),
                    16, // 16-bit sample size
                    originalFormat.getChannels(),
                    originalFormat.getChannels() * 2, // Frame size
                    originalFormat.getSampleRate(),
                    false // Big-endian
            );

            AudioInputStream convertedAudioStream = AudioSystem.getAudioInputStream(targetFormat, originalAudioStream);

            Clip clip = AudioSystem.getClip();
            clip.open(convertedAudioStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initMusicPlayer() {
        try {
            // Lade die Musikdatei
            URL url = getClass().getResource("/background.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);

            // Überprüfe das Audioformat und konvertiere es bei Bedarf
            AudioFormat baseFormat = audioStream.getFormat();
            AudioFormat decodedFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false
            );
            AudioInputStream decodedAudioStream = AudioSystem.getAudioInputStream(decodedFormat, audioStream);

            Clip backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(decodedAudioStream);

            // Lautstärkeregelung initialisieren
            volumeControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);

            // Setze die Lautstärke auf den gespeicherten Wert (oder Standardwert)
            loadVolume(); // Lade und setze die Lautstärke vor dem Starten der Musik

            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY); // Musik in einer Endlosschleife abspielen
            backgroundMusic.start(); // Musik abspielen
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void setVolume(float volume) {
        if (volumeControl != null) {
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            float gain = min + (max - min) * (volume / 100.0f); // Umwandlung des Volumens in den Bereich [min, max]
            volumeControl.setValue(gain);
            saveVolume(volume); // Speichere die Lautstärke
        }
    }

    // Methode zum Abrufen der aktuellen Lautstärke
    public float getCurrentVolume() {
        if (volumeControl != null) {
            float min = volumeControl.getMinimum();
            float max = volumeControl.getMaximum();
            float currentGain = volumeControl.getValue();
            return (currentGain - min) / (max - min) * 100; // Umrechnung auf den Bereich 0-100
        }
        return 50; // Standardwert, falls etwas schiefgeht
    }

    private void saveVolume(float volume) {
        String externalPath = System.getProperty("user.home") + File.separator + "volume.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(externalPath))) {
            writer.write(String.valueOf(volume));
            System.out.println("Volume saved: " + volume);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveGameSoundsVolume(float volume) {
        try {
            URL resourceUrl = getClass().getClassLoader().getResource("config/gamesounds.txt");
            if (resourceUrl == null) {
                throw new FileNotFoundException("Resource file not found: config/gamesounds.txt");
            }
            File file = new File(resourceUrl.toURI());
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(String.valueOf(volume));
                System.out.println("Game Sounds volume saved: " + volume);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadVolume() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config/volume.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String volumeStr = reader.readLine();
            if (volumeStr != null) {
                float volume = Float.parseFloat(volumeStr);
                setVolume(volume); // Set the loaded volume
            } else {
                setVolume(50); // Default volume
            }
        } catch (Exception e) {
            System.out.println("Volume file not found or invalid. Using default volume.");
            setVolume(50); // Default volume in case of error
        }
    }

    // Methode zum Laden der Game-Sounds Lautstärke aus einer Textdatei
    private void loadGameSoundsVolume() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config/gamesounds.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String volumeStr = reader.readLine();
            if (volumeStr != null) {
                gameSoundsVolume = Float.parseFloat(volumeStr);
            } else {
                gameSoundsVolume = 50; // Default game sounds volume
            }
        } catch (Exception e) {
            System.out.println("Game sounds file not found or invalid. Using default volume.");
            gameSoundsVolume = 50; // Default volume in case of error
        }
    }

}