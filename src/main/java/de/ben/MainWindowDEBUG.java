package de.ben;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class MainWindowDEBUG extends JFrame {
	public MainWindowDEBUG() {
		setTitle("Casino - Hauptmenü");
		setSize(600, 300);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		try {
			URL iconUrl = new URL("https://bennyyn.xyz/upload/images/icon.png");
			BufferedImage iconImage = ImageIO.read(iconUrl);
			setIconImage(iconImage);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			URL imageUrl = new URL("https://bennyyn.xyz/upload/images/background.png");
			BufferedImage backgroundImage = ImageIO.read(imageUrl);
			setContentPane(new JLabel(new ImageIcon(backgroundImage)));
		} catch (IOException e) {
			e.printStackTrace();
			getContentPane().setBackground(Color.BLACK);
		}

		setLayout(new BorderLayout());

		JPanel contentPanel = new JPanel(new BorderLayout());
		contentPanel.setOpaque(false);

		JLabel bigTextLabel = new JLabel("Casino");
		bigTextLabel.setFont(new Font("Arial", Font.BOLD, 30));
		bigTextLabel.setForeground(Color.WHITE);
		bigTextLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(bigTextLabel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 0, 1));
		buttonPanel.setOpaque(false);

		JButton slotsButton = new JButton("SlotsDEBUG");
		slotsButton.setFont(new Font("Arial", Font.BOLD, 14));
		slotsButton.setForeground(Color.YELLOW);
		slotsButton.setBackground(Color.DARK_GRAY);
		slotsButton.setFocusable(false);
		slotsButton.setFocusPainted(false);
		slotsButton.setBorderPainted(false);

		slotsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new SlotsDEBUG();
				dispose();
			}
		});

		JButton blackjackButton = new JButton("Blackjack");
		blackjackButton.setFont(new Font("Arial", Font.BOLD, 14));
		blackjackButton.setForeground(Color.YELLOW);
		blackjackButton.setBackground(Color.DARK_GRAY);
		blackjackButton.setFocusable(false);
		blackjackButton.setFocusPainted(false);
		blackjackButton.setBorderPainted(false);

		JButton quitButton = new JButton("Quit");
		quitButton.setFont(new Font("Arial", Font.BOLD, 14));
		quitButton.setForeground(Color.YELLOW);
		quitButton.setBackground(Color.DARK_GRAY);
		quitButton.setFocusable(false);
		quitButton.setFocusPainted(false);
		quitButton.setBorderPainted(false);
		quitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		JPanel slotsButtonPanel = new JPanel(new FlowLayout());
		slotsButtonPanel.setOpaque(false);
		slotsButtonPanel.add(slotsButton);

		JPanel blackjackButtonPanel = new JPanel(new FlowLayout());
		blackjackButtonPanel.setOpaque(false);
		blackjackButtonPanel.add(blackjackButton);

		JPanel quitButtonPanel = new JPanel(new FlowLayout());
		quitButtonPanel.setOpaque(false);
		quitButtonPanel.add(quitButton);

		buttonPanel.add(slotsButtonPanel);
		buttonPanel.add(blackjackButtonPanel);
		buttonPanel.add(quitButtonPanel);

		contentPanel.add(buttonPanel, BorderLayout.SOUTH);

		JLabel bottomLeftTextLabel = new JLabel("by Alex & Ben");
		bottomLeftTextLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		bottomLeftTextLabel.setForeground(Color.WHITE);
		bottomLeftTextLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JPanel bottomLeftPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		bottomLeftPanel.setOpaque(false);
		bottomLeftPanel.add(bottomLeftTextLabel);
		contentPanel.add(bottomLeftPanel, BorderLayout.NORTH);

		add(contentPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

		setVisible(true);
	}

	public static void main(String[] args) {
		new MainWindowDEBUG();
	}
}
