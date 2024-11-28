package de.ben;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class MainGUI extends JFrame implements ActionListener {

    // Attribute
    JButton startButton;
    JButton settingsButton;
    JButton exitButton;
    JPanel panel;
    private Clip backgroundMusic;
    private FloatControl volumeControl;
    private final String VOLUME_FILE = "volume.txt"; // Datei zum Speichern der Lautstärke
    private final String THEME_FILE = "theme.txt"; // Datei zum Speichern des Themes
    private boolean showLoadingScreen = false;
    private String selectedTheme = "Original";

    // Konstruktor
    public MainGUI() {
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
                switch (selectedTheme) {
                    case "Original":
                        g.drawImage(new ImageIcon("img/background.jpg").getImage(), 0, 0, null);
                        break;
                    case "Dark":
                        g.drawImage(new ImageIcon("img/background-dark.jpg").getImage(), 0, 0, null);
                        break;
                        case "Darkblue":
                        g.drawImage(new ImageIcon("img/background-darkblue.jpg").getImage(), 0, 0, null);
                        break;
                    case "Light":
                        g.drawImage(new ImageIcon("img/background-light.jpg").getImage(), 0, 0, null);
                        break;
                    case "Scarlet":
                        g.drawImage(new ImageIcon("img/background-scarlet.jpg").getImage(), 0, 0, null);
                        break;
                    default:
                        break;
                }
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

    // Methode, um den Button zu stylen
    private void styleButton(JButton button) {
        button.setBackground(new Color(78, 136, 174, 255));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(150, 40)); // Größe setzen
        button.addActionListener(this);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
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

        } else if (sourceButton == settingsButton) {
            this.setVisible(false); // Verstecke MainGUI statt es zu schließen
            new SettingsGUI(this, true); // Öffne SettingsGUI und übergebe MainGUI für Lautstärkeanpassung
        } else if (sourceButton == exitButton) {
            System.exit(0); // Beende das Programm
        }
    }

    public static void main(String[] args) {
        new MainGUI(); // Starte die MainGUI
    }

    public void saveSelectedTheme(String selectedTheme) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(THEME_FILE))) {
            writer.write(String.valueOf(selectedTheme));
            updateSelectedTheme();
            System.out.println("Theme gespeichert: " + selectedTheme);
        } catch (IOException e) {
            e.printStackTrace();
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
}

