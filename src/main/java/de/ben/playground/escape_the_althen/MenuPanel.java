package de.ben.playground.escape_the_althen;

import de.ben.MainGUI;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuPanel extends JPanel implements ActionListener{

	//ATTRIBUTE
	JButton b = new JButton();
	JButton b2 = new JButton();
	MenuFrame mf;
	public boolean alreadyOpened = false;
	MainGUI mainGUI;
	
	//KONSTRUKTOR
	public MenuPanel(MenuFrame mf, MainGUI mainGUI) {
		repaint();
		this.mainGUI = mainGUI;
		b.addActionListener(this);
		b2.addActionListener(this);
		b.setText("Spielen");
		b2.setText("Hauptmenü");
		b.setBounds(785, 500, 350, 70);
		this.setLayout(null);
		b2.setBounds(785, 600, 350, 70);
		this.add(b);
		this.add(b2);
		this.mf = mf;
	}
	
	//METHODE ZUM "BEMAHLEN" DES JPANELS
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(new ImageIcon("img/playground/escapethealthen/graphics/gui/menu_background.png").getImage(), 0, 0, null);
		repaint();
	}

	//ACTIONLISTENER METHODE ZUM TESTEN DER JBUTTONS
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == b && !alreadyOpened) {
			GameFrame gf = new GameFrame(1, mf);
			mf.setVisible(false);
		}
		if(e.getSource() == b2) {
			mainGUI.setVisible(true);
			mf.dispose();
		}
	}
}