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

    // Konstruktor
    public MainGUI() {
        this.setTitle("Casino");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        // Icon setzen mit Skalierung
        ImageIcon icon = new ImageIcon("img/icon.png");
        Image scaledIcon = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH); // glatte Skalierung
        setIconImage(scaledIcon);

        // Musik initialisieren und Lautstärke laden
        initMusicPlayer();

        // Versuche, den Hintergrund als Bild zu setzen
        try {
            BufferedImage backgroundImage = ImageIO.read(getClass().getResource("/background.png"));
            panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // Bild als Hintergrund
                }
            };
        } catch (IOException e) {
            System.out.println("Hintergrundbild konnte nicht geladen werden, benutze stattdessen dunkelgrün.");
            panel = new JPanel(); // Standard-Panel, falls das Bild nicht geladen werden konnte
            panel.setBackground(new Color(0, 51, 0)); // Dunkelgrüner Hintergrund
        }

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
        Color normalColor = new Color(35, 35, 35); // Grau
        Color pressedColor = new Color(0, 100, 0); // Helleres Grün
        button.setFont(new Font("Arial", Font.BOLD, 16)); // Schriftart und Größe
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false); // Fokusrand deaktivieren
        button.setBackground(normalColor);
        button.setForeground(Color.yellow); // Gelber Text
        button.setPreferredSize(new Dimension(150, 40)); // Größe setzen
        button.addActionListener(this);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                button.setBackground(pressedColor);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                button.setBackground(normalColor);
            }
        });
    }

    // Methode zum Initialisieren und Starten der Hintergrundmusik
    private void initMusicPlayer() {
        try {
            // Lade die Musikdatei
            URL url = getClass().getResource("/background.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);

            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);

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

            // Ladebildschirm anzeigen und PokerGUI nach dem Laden öffnen
            LoadingScreen.showLoadingScreen(this, () -> {
                // PokerGUI öffnen, wenn der Ladevorgang abgeschlossen ist
                new PlayerSelectionIDEE(this);
                this.dispose(); // Schließe MainGUI, wenn der Ladevorgang abgeschlossen ist
            });
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
}
