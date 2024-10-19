package de.ben;

import javax.swing.*;
import java.awt.*;

// LoadingScreen Klasse mit Ladebalken, separater Prozentanzeige und angepasstem Hintergrund
public class LoadingScreen {

    // Statische Methode zum Anzeigen des Ladebildschirms mit Fortschrittsanzeige und Prozenttext darunter
    public static void showLoadingScreen(JFrame parentFrame, Runnable onLoadingComplete) {
        // Create the loading dialog
        JDialog loadingDialog = new JDialog(parentFrame, "Loading...", true);
        loadingDialog.setSize(300, 150);
        loadingDialog.setLocationRelativeTo(parentFrame);
        loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        // Add a label, progress bar, and percentage label
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 51, 0)); // Setze die Hintergrundfarbe passend zur MainGUI (dunkelgrün)

        JLabel loadingLabel = new JLabel("Loading, please wait...", JLabel.CENTER);
        loadingLabel.setForeground(Color.YELLOW); // Setze den Text in gelber Farbe, um den Kontrast zu verbessern

        JProgressBar progressBar = new JProgressBar(0, 100); // Fortschritt von 0 bis 100
        JLabel percentLabel = new JLabel("0%", JLabel.CENTER); // Label für die Prozentanzeige unter der Bar
        percentLabel.setForeground(Color.YELLOW); // Setze den Text der Prozentanzeige auf gelb

        // Panel für das Layout
        panel.add(loadingLabel, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.CENTER);
        panel.add(percentLabel, BorderLayout.SOUTH); // Prozentanzeige unterhalb des Ladebalkens
        loadingDialog.add(panel);

        // Create a SwingWorker to simulate a background task with progress updates
        SwingWorker<Void, Integer> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Simulate a task by updating progress
                for (int i = 0; i <= 100; i += 10) {
                    Thread.sleep(200); // Wartezeit simuliert die Hintergrundaufgabe
                    publish(i); // Aktuellen Fortschritt melden
                }
                return null;
            }

            @Override
            protected void process(java.util.List<Integer> chunks) {
                // Update the progress bar and the percentage label with the latest value
                int progress = chunks.get(chunks.size() - 1);
                progressBar.setValue(progress);
                percentLabel.setText(progress + "%"); // Aktualisiert die Prozentanzeige
            }

            @Override
            protected void done() {
                loadingDialog.dispose(); // Schließt das Ladefenster, wenn der Vorgang abgeschlossen ist
                onLoadingComplete.run(); // Führt die angegebene Aktion nach Abschluss der Aufgabe aus (z.B. Öffnen der PokerGUI)
            }
        };

        worker.execute(); // Startet die Aufgabe
        loadingDialog.setVisible(true); // Zeigt das Dialogfenster an
    }
}