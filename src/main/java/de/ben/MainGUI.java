import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class MainGUI extends JFrame {
    private JButton b1, b2, b3;
    private JLabel backgroundLabel;

    public MainGUI() {
        setTitle("Poker Casino Game");
        setSize(768, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            URL backgroundURL = new URL("https://bennyyn.xyz/upload/img/background.png");
            ImageIcon backgroundIcon = new ImageIcon(backgroundURL);
            backgroundLabel = new JLabel(backgroundIcon);
            setContentPane(backgroundLabel);
            backgroundLabel.setLayout(new GridBagLayout());

            // Button-Initialisierung
            b1 = new JButton("Start");
            b2 = new JButton("Settings");
            b3 = new JButton("Quit");

            // Aktionen für Buttons
            b1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                }
            });

            b2.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Settings geöffnet!");
                }
            });

            b3.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);  // Beendet das Programm
                }
            });

            JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
            buttonPanel.setOpaque(false);
            buttonPanel.add(b1);
            buttonPanel.add(b2);
            buttonPanel.add(b3);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            backgroundLabel.add(buttonPanel, gbc);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Fehler beim Laden der Bilder.", "Bildladefehler", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openPokerGUI() {
    //    EventQueue.invokeLater(() -> {
    //        PokerGUI pokerGUI = new PokerGUI(); // Erstelle eine neue Instanz der PokerGUI
     //       pokerGUI.setVisible(true);// Mache die PokerGUI sichtbar
      //      this.setVisible(false); // Mache die MainGUI unsichtbar
        //});
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            MainGUI frame = new MainGUI();
            frame.setVisible(true);
        });
    }
}
