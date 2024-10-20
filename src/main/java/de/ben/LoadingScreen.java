package de.ben;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;

public class LoadingScreen {

    // Deklariere backgroundImage als Instanzvariable
    private static BufferedImage backgroundImage;

    public static void showLoadingScreen(JFrame parentFrame, Runnable onLoadingComplete) {
        // Erstelle den Lade-Dialog
        JDialog loadingDialog = new JDialog(parentFrame, "Laden...", true);
        loadingDialog.setSize(300, 150);  // Kleinere Fenstergröße für den Ladebildschirm
        loadingDialog.setLocationRelativeTo(parentFrame);
        loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        loadingDialog.setUndecorated(true);  // Entfernt den Fensterrahmen für ein sauberes Aussehen

        // Versuche, das Hintergrundbild zu laden
        try (InputStream inputStream = LoadingScreen.class.getClassLoader().getResourceAsStream("background.png")) {
            if (inputStream == null) {
                throw new IOException("Hintergrundbild konnte nicht gefunden werden.");
            }
            backgroundImage = ImageIO.read(inputStream);
        } catch (IOException e) {
            System.out.println("Hintergrundbild für den Ladebildschirm konnte nicht geladen werden.");
        }

        // Panel für den Inhalt des Ladebildschirms mit dem gleichen Hintergrundbild
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    // Zeichne das Hintergrundbild auf die gesamte Fläche des Lade-Dialogs
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    // Setze einen Fallback-Hintergrund, wenn das Bild nicht geladen werden kann
                    g.setColor(new Color(0, 51, 0));  // Dunkelgrüner Fallback-Hintergrund
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        panel.setOpaque(false); // Panel transparent lassen, damit das Hintergrundbild sichtbar bleibt

        // Lade-Text
        JLabel loadingLabel = new JLabel("Laden, bitte warten...", JLabel.CENTER);
        loadingLabel.setForeground(Color.YELLOW); // Gelber Text, wie im MainGUI
        loadingLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Schriftgröße und Stil anpassen

        // Fortschrittsanzeige
        JProgressBar progressBar = new JProgressBar(0, 100);  // Fortschrittsanzeige von 0 bis 100
        progressBar.setPreferredSize(new Dimension(250, 20)); // Größe der Fortschrittsanzeige
        progressBar.setForeground(new Color(255, 215, 0));  // Goldene Farbe, passend zum Stil des MainGUI
        progressBar.setStringPainted(true);  // Fortschrittswert anzeigen

        // Prozent-Label
        JLabel percentLabel = new JLabel("0%", JLabel.CENTER);
        percentLabel.setForeground(Color.YELLOW);  // Gelber Text, wie im MainGUI
        percentLabel.setFont(new Font("Arial", Font.BOLD, 14));  // Schriftgröße und Stil

        // Füge die Komponenten dem Panel hinzu
        panel.add(loadingLabel, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.CENTER);
        panel.add(percentLabel, BorderLayout.SOUTH);

        loadingDialog.add(panel);

        // Erstelle einen SwingWorker, um eine Aufgabe mit Fortschrittsaktualisierungen zu simulieren
        SwingWorker<Void, Integer> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Simuliere eine Aufgabe, indem der Fortschritt aktualisiert wird
                for (int i = 0; i <= 100; i += 10) {
                    Thread.sleep(200);  // Simuliere Wartezeit
                    publish(i);  // Berichte den Fortschritt
                }
                return null;
            }

            @Override
            protected void process(java.util.List<Integer> chunks) {
                // Aktualisiere die Fortschrittsanzeige und das Prozent-Label
                int progress = chunks.get(chunks.size() - 1);
                progressBar.setValue(progress);
                percentLabel.setText(progress + "%");
            }

            @Override
            protected void done() {
                loadingDialog.dispose();  // Schließe den Dialog, wenn die Aufgabe abgeschlossen ist
                onLoadingComplete.run();  // Führe die nächste Aktion aus (z.B. PokerGUI öffnen)
            }
        };

        worker.execute();  // Starte den SwingWorker
        loadingDialog.setVisible(true);  // Zeige den Lade-Dialog an
    }
}
