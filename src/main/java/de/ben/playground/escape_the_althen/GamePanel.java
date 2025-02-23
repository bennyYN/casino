package de.ben.playground.escape_the_althen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseMotionListener;
import java.lang.Object;

public class GamePanel extends JPanel implements ActionListener{
	
	//ATTRIBUTE & OBJEKTE
		public boolean debugmode = false, paused = false, levelCompleted = false;
		public double gameScale = 5;
		Inventory inv;
		Graphics g;
		World world;
		Player player;
		Althen althen;
		Althen althen2;
		HUD hud;
		RenderManager rm;
		JButton b;
		GameFrame gf;
		GenerationManager gm;
		int currentLevel;

	//KONTRUKTOR
		public GamePanel(GameFrame gf, int level) {

			//reset creatures
			Creature.reset();

			this.setSize(1920, 1080);
			this.setOpaque(true);
			player = new Player((0)*16*gameScale, (0)*16*gameScale, 0.3, gameScale, 1.65, (this.getWidth()/2)-((16*gameScale)/2), (this.getHeight()/2)-((16*gameScale)/2));
			world = new World(gameScale, player, this);
			gm = new GenerationManager(level, world);

			this.gf = gf;
			gf.addMouseMotionListener(althen = new Althen(2.3, 0, 0.4, gameScale, 10, player, world));
			gf.addMouseMotionListener(althen2 = new Althen(9, 12, 0.4, gameScale, 10, player, world));
			hud = new HUD(this, (int)(this.getWidth()/2), (int)this.getHeight());
			this.setVisible(true);
			rm = new RenderManager(world, player);
			
			b = new JButton("Spiel schließen");
			b.setBounds(785, 650, 350, 70);
			this.setLayout(null);
			b.addActionListener(this);
			b.setVisible(false);
			this.add(b);

			inv = new Inventory(player, hud);
			currentLevel = level;
			//Load Level Generation File
			gm.generate();
		}

	//TEMPORÄRE METHODE ZUM ERSETZEN VON TILES
		private void replaceTiles(int a1, int a2, int b1, int b2, String wasDennÜberhaupt, int layer) {
			try {
			for(int i = a1; i <= b1; i++) {
				for(int j = a2; j <= b2; j++) {
					world.setTile(i, j, layer, wasDennÜberhaupt);
				}
			}
			}catch(Exception e) {}
		}
		private void replaceTile(int x, int y, String wasDennÜberhaupt, int layer) {
			world.setTile(x, y, layer, wasDennÜberhaupt);
		}
		private void setObject(int x, int y, String wasDennÜberhaupt) {
			world.setObject(x, y, wasDennÜberhaupt);
		}
	
	public void paintComponent(Graphics s) {
		
		this.g = s;
		super.paintComponent(s);
		rm.render(s, debugmode);
		hud.renderHUD(s, debugmode);
		inv.render(g);
		if(!player.isAlive && !levelCompleted) {
			g.drawImage(new ImageIcon("img/playground/escapethealthen/graphics/creatures/player/death/dead_player.png").getImage(), (int)(player.xScreenPos-(8*player.scale)), (int)player.yScreenPos, (int)(32*player.scale), (int)(32*player.scale), null);
			renderDeathscreen(g);
		}
		if(levelCompleted) {
			renderLevelCompletedScreen(g);
			gf.timer.stop();
		}
		
		
		//PAUSE MENU
		if(paused) {
			s.setColor(new Color(5, 5, 10, 100));
			s.fillRect(0, 0, 1920, 1080);
			s.setColor(Color.WHITE);
			s.setFont(new Font("TimesNewRoman", Font.PLAIN, 75));
			s.drawString("Spiel Pausiert", (int)(getWidth()/2)-250, 200);
			s.setColor(Color.BLACK);
			b.setVisible(true);
			this.setVisible(true);
		}else if(player.isAlive && !levelCompleted) {
			b.setVisible(false);
		}
	}
	
	private void renderLevelCompletedScreen(Graphics s) {
		debugmode = false;
		s.setColor(new Color(5, 5, 10, 100));
		s.fillRect(0, 0, 1920, 1080);
		s.setColor(Color.WHITE);
		s.setFont(new Font("TimesNewRoman", Font.PLAIN, 75));
		s.drawString("Level "+currentLevel+" geschafft!", (int)(getWidth()/2)-300, 200);
		s.setColor(Color.BLACK);
		b.setVisible(true);
		this.setVisible(true);
	}
	
	private void renderDeathscreen(Graphics s) {
		debugmode = false;
		s.setColor(new Color(190, 20, 20, 100));
		s.fillRect(0, 0, 1920, 1080);
		s.setColor(Color.WHITE);
		s.setFont(new Font("TimesNewRoman", Font.PLAIN, 75));
		s.drawString("You died!", (int)(getWidth()/2)-175, 425);
		s.setColor(Color.BLACK);
		b.setVisible(true);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == b) {
			gf.mf.mainGUI.setVisible(true);
			gf.mf.dispose();
			gf.dispose();

		}
	}
}
