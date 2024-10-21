package de.ben;

import javax.swing.*;
import java.awt.*;

public class LoadingScreen {

    // Hauptmethode, um die Klasse direkt ausführbar zu machen
    public static void main(String[] args) {
        // Erstelle ein neues JFrame als Hauptfenster
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);  // Größe des Hauptfensters

        // Führe den Ladebildschirm aus
        showLoadingScreen(frame, () -> {
            System.out.println("Laden abgeschlossen. Weiter geht's!");
            // Hier kann man den nächsten Schritt nach dem Laden ausführen,
            // z.B. ein neues Fenster öffnen oder die Hauptanwendung starten.
        });
    }

    public static void showLoadingScreen(JFrame parentFrame, Runnable onLoadingComplete) {
        // Erstelle den Lade-Dialog
        JDialog loadingDialog = new JDialog(parentFrame, "Laden...", true);
        loadingDialog.setSize(400, 200);  // Vergrößere das Fenster
        loadingDialog.setLocationRelativeTo(parentFrame);
        loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        loadingDialog.setUndecorated(true);  // Entfernt den Fensterrahmen für ein sauberes Aussehen

        // Panel für den Inhalt des Ladebildschirms mit schwarzem Hintergrund
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Schwarzer Hintergrund
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setOpaque(false); // Panel transparent lassen, damit der schwarze Hintergrund sichtbar bleibt

        // Lade-Text ("Laden, bitte warten...")
        JLabel loadingLabel = new JLabel("Laden, bitte warten...", JLabel.CENTER);
        loadingLabel.setForeground(Color.WHITE); // Weißer Text
        loadingLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Schriftgröße und Stil
        loadingLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // Fügt oben 20 Pixel Abstand hinzu

        // Prozent-Label (über der Fortschrittsanzeige)
        JLabel percentLabel = new JLabel("0%", JLabel.CENTER);
        percentLabel.setForeground(Color.WHITE);  // Weißer Text für die Prozentanzeige
        percentLabel.setFont(new Font("Arial", Font.BOLD, 16));  // Schriftgröße und Stil
        percentLabel.setBorder(BorderFactory.createEmptyBorder(80, 0, 0, 0));  // Fügt X Pixel Abstand nach oben hinzu

        // Fortschrittsanzeige (graue Ladeleiste)
        JProgressBar progressBar = new JProgressBar(0, 100);  // Fortschrittsanzeige von 0 bis 100
        progressBar.setPreferredSize(new Dimension(250, 30)); // Setze die Breite auf 250 Pixel, damit Platz auf beiden Seiten bleibt
        progressBar.setForeground(Color.LIGHT_GRAY);  // Graue Farbe für die Ladeleiste
        progressBar.setBackground(Color.DARK_GRAY);  // Dunkleres Grau für den Hintergrund der Ladeleiste
        progressBar.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));  // Füge einen Rand hinzu, um Platz rechts und links zu lassen
        progressBar.setStringPainted(false);  // Deaktiviere die eingebaute Textanzeige in der Fortschrittsleiste

        // Erstelle ein zentriertes Panel für die Fortschrittsleiste und das Prozent-Label
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setOpaque(false); // Transparent lassen
        progressPanel.add(percentLabel, BorderLayout.NORTH);
        progressPanel.add(progressBar, BorderLayout.CENTER);

        // Füge die Komponenten dem Hauptpanel hinzu
        panel.add(loadingLabel, BorderLayout.NORTH);  // Lade-Text oben im Fenster mit Abstand
        panel.add(progressPanel, BorderLayout.CENTER); // Fortschrittsleiste und Prozent-Label mittig

        loadingDialog.add(panel);

        // Erstelle einen SwingWorker, um eine Aufgabe mit Fortschrittsaktualisierungen zu simulieren
        SwingWorker<Void, Integer> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Simuliere eine Aufgabe, indem der Fortschritt aktualisiert wird
                for (int i = 0; i <= 100; i += 5) {
                    Thread.sleep(100);  // Simuliere Wartezeit
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