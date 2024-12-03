package de.ben;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.border.Border;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainGUI extends JFrame implements ActionListener {

    // Attribute
    JButton startButton;
    JButton multiplayerButton;
    JButton settingsButton;
    JButton exitButton;
    JPanel panel;
    private Clip backgroundMusic;
    private FloatControl volumeControl;
    private final String VOLUME_FILE = "volume.txt"; // Datei zum Speichern der Lautstärke
    private final String THEME_FILE = "theme.txt"; // Datei zum Speichern des Themes
    private final String GAME_SOUNDS_FILE = "gamesounds.txt"; // Datei zum Speichern der Soundeinstellungen
    private boolean showLoadingScreen = false;
    private String selectedTheme;
    private static float gameSoundsVolume = 50; // Default game sounds volume
    Color originalTheme = new Color(78, 136, 174, 255), transparentOriginalTheme = new Color(142, 215, 255, 81);
    Color darkTheme = new Color(43, 49, 64, 255), transparentDarkTheme = new Color(34, 34, 34, 81);
    Color darkblueTheme = new Color(62, 103, 147, 255), transparentDarkblueTheme = new Color(78, 136, 174, 255);
    Color scarletTheme = new Color(172, 41, 66, 255), transparentScarletTheme = new Color(197, 0, 0, 136);

    // Konstruktor
    public MainGUI() {

        //Laden des Bilderarchivs
        new ImageArchive();

        // Laden des gespeicherten Themes und der Lautstärken
        loadSelectedTheme();
        loadVolume();
        loadGameSoundsVolume();

        this.setTitle("Casino");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        //Titlebar-Icon mit Skalierung setzen
        ImageIcon icon = new ImageIcon("img/icon.png");
        Image scaledIcon = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH); // glatte Skalierung
        setIconImage(scaledIcon);

        // Musik initialisieren und Lautstärke laden
        initMusicPlayer();

        // Versuche, den Hintergrund als Bild zu setzen
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(ImageArchive.getImage("background:"+selectedTheme), 0, 0, null);
                updateButtonColor(startButton, false);
                updateButtonColor(settingsButton, false);
                updateButtonColor(exitButton, false);
            }
        };

        panel.setLayout(new GridBagLayout()); // Setze Layout nach dem Laden des Bildes oder dem Default
        this.add(panel);

        // GridBagConstraints für die Zentrierung der Buttons
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 0, 10, 0); // Abstand zwischen den Buttons
        gbc.anchor = GridBagConstraints.CENTER;

        // Start Button
        startButton = new JButton("Start");
        styleButton(startButton);
        panel.add(startButton, gbc);

        multiplayerButton = new JButton("Multiplayer");
        styleButton(multiplayerButton);
        panel.add(multiplayerButton, gbc);

        // Settings Button
        settingsButton = new JButton("Settings");
        styleButton(settingsButton);
        panel.add(settingsButton, gbc);

        // Exit Button
        exitButton = new JButton("Exit");
        styleButton(exitButton);
        panel.add(exitButton, gbc);

        this.setVisible(true);
    }

    public void updateButtonColor(JButton button, boolean isTransparent){
        if(isTransparent){
            switch (selectedTheme){
                case "Original":
                    button.setBackground(transparentOriginalTheme);
                    break;
                case "Dark":
                    button.setBackground(transparentDarkTheme);
                    break;
                case "Darkblue":
                    button.setBackground(transparentDarkblueTheme);
                    break;
                case "Scarlet":
                    button.setBackground(transparentScarletTheme);
                    break;
            }
        }else{
            switch (selectedTheme){
                case "Original":
                    button.setBackground(originalTheme);
                    break;
                case "Dark":
                    button.setBackground(darkTheme);
                    break;
                case "Darkblue":
                    button.setBackground(darkblueTheme);
                    break;
                case "Scarlet":
                    button.setBackground(scarletTheme);
                    break;
            }
        }

    }

    // Methode, um den Button zu stylen
    private void styleButton(JButton button) {
        button.setBackground(new Color(78, 136, 174, 255));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(150, 40)); // Größe setzen
        button.addActionListener(this);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(e -> {
            playSound("click");
        });

        // Create a thin line border
        Border thinBorder = BorderFactory.createLineBorder(new Color(255, 255, 255, 81), 2); // 1 pixel thick
        button.setBorder(thinBorder);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBorderPainted(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBorderPainted(false);
            }
        });
    }

    // Methode zum Initialisieren und Starten der Hintergrundmusik
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

            backgroundMusic = AudioSystem.getClip();
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

    // Methode zur Anpassung der Lautstärke
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

    // Methode zum Speichern der Lautstärke in einer Textdatei
    private void saveVolume(float volume) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(VOLUME_FILE))) {
            writer.write(String.valueOf(volume));
            System.out.println("Lautstärke gespeichert: " + volume);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Methode zum Laden der Lautstärke aus einer Textdatei
    private void loadVolume() {
        try (BufferedReader reader = new BufferedReader(new FileReader(VOLUME_FILE))) {
            String volumeStr = reader.readLine();
            if (volumeStr != null) {
                float volume = Float.parseFloat(volumeStr);
                setVolume(volume); // Setze die geladene Lautstärke direkt vor dem Start der Musik
            } else {
                setVolume(50); // Standardlautstärke verwenden, wenn nichts gefunden wird
            }
        } catch (FileNotFoundException e) {
            System.out.println("Lautstärkedatei nicht gefunden. Verwende Standardlautstärke.");
            setVolume(50); // Standardlautstärke verwenden, wenn Datei nicht vorhanden ist
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            setVolume(50); // Falls ein Fehler auftritt, setze eine Standardlautstärke
        }
    }

    // Methode zum Speichern der Game Sounds Lautstärke in einer Textdatei
    private void saveGameSoundsVolume(float volume) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(GAME_SOUNDS_FILE))) {
            writer.write(String.valueOf(volume));
            System.out.println("Game Sounds Lautstärke gespeichert: " + volume);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Methode zum Laden der Game Sounds Lautstärke aus einer Textdatei
    private void loadGameSoundsVolume() {
        try (BufferedReader reader = new BufferedReader(new FileReader(GAME_SOUNDS_FILE))) {
            String volumeStr = reader.readLine();
            if (volumeStr != null) {
                gameSoundsVolume = Float.parseFloat(volumeStr);
            } else {
                gameSoundsVolume = 50; // Standardlautstärke verwenden, wenn nichts gefunden wird
            }
        } catch (FileNotFoundException e) {
            System.out.println("Game Sounds Datei nicht gefunden. Verwende Standardlautstärke.");
            gameSoundsVolume = 50; // Standardlautstärke verwenden, wenn Datei nicht vorhanden ist
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            gameSoundsVolume = 50; // Falls ein Fehler auftritt, setze eine Standardlautstärke
        }
    }

    // Methode zum Abspielen eines Sounds mit der gespeicherten Lautstärke

    public static void playSound(String sound){
        triggerSound("sounds/"+sound+".wav");
    }

    private static void triggerSound(String filePath) {
        try {
            // Lade die Sounddatei
            File soundFile = new File(filePath);
            if (!soundFile.exists()) {
                throw new FileNotFoundException("Sound file not found: " + filePath);
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);

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

            Clip clip = AudioSystem.getClip();
            clip.open(decodedAudioStream);

            // Lautstärkeregelung initialisieren
            FloatControl soundVolumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float min = soundVolumeControl.getMinimum();
            float max = soundVolumeControl.getMaximum();
            float gain = min + (max - min) * (gameSoundsVolume / 100.0f); // Umwandlung des Volumens in den Bereich [min, max]
            soundVolumeControl.setValue(gain);

            clip.start(); // Sound abspielen
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton sourceButton = (JButton) e.getSource();
        if (sourceButton == startButton) {
            // Verstecke das MainGUI-Fenster und zeige den Ladescreen
            this.setVisible(false); // Verstecke das Fenster, anstatt es zu schließen

            if(showLoadingScreen){
                // Ladebildschirm anzeigen und PokerGUI nach dem Laden öffnen
                LoadingScreen.showLoadingScreen(this, () -> {
                    // PokerGUI öffnen, wenn der Ladevorgang abgeschlossen ist
                    new PlayerSelection(this);
                    this.dispose(); // Schließe MainGUI, wenn der Ladevorgang abgeschlossen ist
                });
            }else{
                // PokerGUI direkt öffnen
                new PlayerSelection(this);
                this.dispose(); // Schließe MainGUI
            }

        }else if(sourceButton == multiplayerButton){
            this.setVisible(false);
            new MultiplayerGUI(this, true);
        }else if (sourceButton == settingsButton) {
            this.setVisible(false); // Verstecke MainGUI statt es zu schließen
            new SettingsGUI(this, true); // Öffne SettingsGUI und übergebe MainGUI für Lautstärkeanpassung
        } else if (sourceButton == exitButton) {
            System.exit(0); // Beende das Programm
        }
    }

    public static void main(String[] args) {
        new MainGUI(); // Starte die MainGUI
    }

    public void saveSelectedTheme(String theme) {
        this.selectedTheme = theme;
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("theme.txt"))) {
            writer.write(theme);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSelectedTheme() {
        try {
            selectedTheme = new String(Files.readAllBytes(Paths.get("theme.txt")));
        } catch (IOException e) {
            selectedTheme = "Original"; // Standard-Theme
        }
    }

    public void updateSelectedTheme() {
        try (BufferedReader reader = new BufferedReader(new FileReader(THEME_FILE))) {
            String theme = reader.readLine();
            if (theme != null) {
                this.selectedTheme = theme;
                System.out.println("Theme loaded: " + theme);
            } else {
                this.selectedTheme = "Original"; // Default theme if file is empty
            }
        } catch (FileNotFoundException e) {
            System.out.println("Theme file not found. Using default theme.");
            this.selectedTheme = "Original"; // Default theme if file is not found
        } catch (IOException e) {
            e.printStackTrace();
            this.selectedTheme = "Original"; // Default theme in case of an error
        }
    }

    public String getSelectedTheme() {
        return selectedTheme;
    }

    public float getGameSoundsVolume() {
        return this.gameSoundsVolume;
    }

    public void setGameSoundsVolume(float volume) {
        this.gameSoundsVolume = volume;
        saveGameSoundsVolume(volume); // Save game sounds volume
    }


}