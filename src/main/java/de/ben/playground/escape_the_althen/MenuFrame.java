package de.ben.playground.escape_the_althen;

import de.ben.MainGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

public class MenuFrame extends JFrame{

	public MainGUI mainGUI;
	//ATTRIBUTE
	MenuPanel mp;
	ImageIcon frameIcon = new ImageIcon("src/main/resources/img/playground/althenpong/althenos.png");
	
	//KONSTRUKTOR
	public MenuFrame(MainGUI mainGUI) {
		mp = new MenuPanel(this, mainGUI);
		this.add(mp);
		this.mainGUI = mainGUI;
		this.setIconImage(frameIcon.getImage());
		this.setSize(1920, 1080);
		this.setTitle("Escape The Althen!");
		this.setSize(1920, 1080);
		this.setVisible(true);
	}
}
