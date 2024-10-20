package de.ben;

import javax.swing.*;
import java.awt.*;

public class LoadingScreen {

    public static void showLoadingScreen(JFrame parentFrame, Runnable onLoadingComplete) {
        // Create the loading dialog
        JDialog loadingDialog = new JDialog(parentFrame, "Loading...", true);
        loadingDialog.setSize(300, 150);
        loadingDialog.setLocationRelativeTo(parentFrame);
        loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        // Add a label, progress bar, and percentage label
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 51, 0)); // Color (dunkelgrün)

        JLabel loadingLabel = new JLabel("Loading, please wait...", JLabel.CENTER);
        loadingLabel.setForeground(Color.YELLOW); // Color text
        JProgressBar progressBar = new JProgressBar(0, 100); // 0-100%
        JLabel percentLabel = new JLabel("0%", JLabel.CENTER); // Label percentage
        percentLabel.setForeground(Color.YELLOW); // text percentage yellow

        // Panel 
        panel.add(loadingLabel, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.CENTER);
        panel.add(percentLabel, BorderLayout.SOUTH); // percentage under the progressbar
        loadingDialog.add(panel);

        // Create a SwingWorker to simulate a background task with progress updates
        SwingWorker<Void, Integer> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Simulate a task by updating progress
                for (int i = 0; i <= 100; i += 10) {
                    Thread.sleep(200); // Waiting time simulates the background task
                    publish(i); // Report current progress
                }
                return null;
            }

            @Override
            protected void process(java.util.List<Integer> chunks) {
                // Update the progress bar and the percentage label with the latest value
                int progress = chunks.get(chunks.size() - 1);
                progressBar.setValue(progress);
                percentLabel.setText(progress + "%"); // Percentage update
            }

            @Override
            protected void done() {
                loadingDialog.dispose(); // close when finished
                onLoadingComplete.run(); // Open PokerGUI
            }
        };

        worker.execute(); // Start worker
        loadingDialog.setVisible(true); 
    }
}
