package de.ben.playground.escape_the_althen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.text.DecimalFormat;

import javax.swing.ImageIcon;



//HEAD-UP-DISPLAY
public class HUD {
	
	//ATTRIBUTE
	private int healthbarAnimation = 0, burningAnimation = 0, delay = 0, delay2 = 0;
	
	//"IMPORT" VON ALLEN BENÖTIGTEN BILDERN IN STATISCHE FINAL ARRAYS, s.d. DIESE SCHNELL ABRUFBAR SIND
	//UND DER GARBAGE COLLECTOR NICHT EINSCHREITEN MUSS BEI STÄNDIGER NEU DEKLARIERUNG
	//-> MINIMALSTE ANZAHL AN BILDERN GLEICHZEITIG
	private static final Image[] HEALTHBAR_IMAGES = {
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/health1.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/health2.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/health3.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/health4.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/health5.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/health6.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/health7.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/health8.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/health9.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/health10.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/health11.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/health12.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/health13.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/health14.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/health15.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/health16.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/health17.png").getImage(),
	};
	
	private static final Image[] IMMORTAL_HEALTHBAR_IMAGES = {
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/immortal/healthbar1.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/immortal/healthbar2.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/immortal/healthbar3.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/immortal/healthbar4.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/immortal/healthbar5.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/immortal/healthbar6.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/immortal/healthbar7.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/immortal/healthbar8.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/immortal/healthbar9.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/immortal/healthbar10.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/immortal/healthbar11.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/immortal/healthbar12.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/immortal/healthbar13.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/immortal/healthbar14.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/immortal/healthbar15.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/immortal/healthbar16.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/immortal/healthbar17.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/immortal/healthbar18.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/immortal/healthbar19.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/immortal/healthbar20.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/immortal/healthbar21.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/immortal/healthbar22.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/immortal/healthbar23.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/immortal/healthbar24.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/immortal/healthbar25.png").getImage(),
	};
	
	private static final Image[] BURNING_HEALTHBAR_IMAGES = {
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/burning/flames1.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/burning/flames2.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/burning/flames3.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/healthbar/burning/flames4.png").getImage()
			
	};
	
	private static final Image[] XPBAR_IMAGES = {
			new ImageIcon("img/playground/escapethealthen/graphics/gui/xpbar/xp_0.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/xpbar/xp_1.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/xpbar/xp_2.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/xpbar/xp_3.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/xpbar/xp_4.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/xpbar/xp_5.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/xpbar/xp_6.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/xpbar/xp_7.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/xpbar/xp_8.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/xpbar/xp_9.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/xpbar/xp_10.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/xpbar/xp_11.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/xpbar/xp_12.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/xpbar/xp_13.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/xpbar/xp_14.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/xpbar/xp_15.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/xpbar/xp_16.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/xpbar/xp_17.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/xpbar/xp_18.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/xpbar/xp_19.png").getImage(),
			new ImageIcon("img/playground/escapethealthen/graphics/gui/xpbar/xp_20.png").getImage(),
	};
	
	Player spieler;
	World welt;
	GamePanel p;
	DecimalFormat df = new DecimalFormat("#");
	protected int wMiddle;
	protected int wHeight;
	
	//KONSTRUKTOR
	public HUD(GamePanel p,  int windowMiddle, int windowHeight) {
		spieler = p.player;
		welt = p.world;
		this.p = p;
		wMiddle = windowMiddle;
		wHeight = windowHeight;
		
	}
	
	//RENDER METHODE
	public void renderHUD(Graphics g, boolean debugmode) {
		
		//DEBUG-MODE
		if(debugmode) {
			welt.renderTileCollisionBoxes = true;
			g.setColor(new Color(30, 30, 50, 100));
			g.fillRect(0, 0, 300, wHeight);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 18));
			g.setColor(Color.WHITE);
			//COORDINATES
				//World Coordinates
				g.drawString("Player Position: "+df.format((((spieler.position[0]+spieler.xScreenPos)+0.5*spieler.scale)/(16*spieler.scale)))+", "+df.format(((spieler.position[1]+spieler.yScreenPos)+0.5*spieler.scale)/(16*spieler.scale)+1), 10, 75);
			//TILE-INFORMATION
				try {
				g.drawString("Standing on: "+welt.getTile(Integer.parseInt(df.format((((spieler.position[0]+spieler.xScreenPos)+0.5*spieler.scale)/(16*spieler.scale)))), Integer.parseInt(df.format(((spieler.position[1]+spieler.yScreenPos)+0.5*spieler.scale)/(16*spieler.scale)+1)), 1).type, 10, 100);
				}catch(Exception e) {
					g.drawString("Standing on: void", 10, 100);
				}
			//SELECTED ITEM INFORMATION
				g.drawString("Selected Item: "+p.inv.getSelectedItemType(), 10, 130);
			//PLAYER-COLLISIONBOX
				spieler.hitbox.render(g, Color.CYAN);
			//EINWEISER COLLISIONS BOX (TEMP)
				for(int i = 1; i < p.althen.Creatures.size(); i++) {
					p.althen.Creatures.get(i).getCollisionBox().render(g, Color.BLUE);
					p.althen.Creatures.get(i).los.render(g);
				}
			//CHUNK-BORDERS
				g.setColor(Color.MAGENTA);
				for(int i = 0; i <= welt.worldSize; i++) {
					for(int j = 0; j <= welt.worldSize; j++) {
						g.drawRect((int)((i*16*16*spieler.scale)-spieler.position[0]), (int)((j*16*16*spieler.scale)-spieler.position[1]), (int)(16*16*spieler.scale), (int)(16*16*spieler.scale));
					}
				}
			g.setColor(Color.BLACK);
		}else {
			welt.renderTileCollisionBoxes = false;
		}
		
		//XP-BAR
		g.drawImage(XPBAR_IMAGES[spieler.xp], wMiddle-360, wHeight-225, 720, 60, null);
		g.setFont(new Font("TimesRoman", Font.PLAIN, 22)); 
		g.setColor(Color.CYAN);
		if(spieler.lvl <= 9) {
			g.drawString("Level "+spieler.lvl, wMiddle-309, wHeight-188);
		}else if(spieler.lvl <= 99) {
			g.drawString("Level "+spieler.lvl, wMiddle-316, wHeight-188);
		}else {
			g.drawString("Level "+spieler.lvl, wMiddle-322, wHeight-188);
		}
		g.setColor(Color.BLACK);
		
		//HEALTH-BAR
		if(spieler.burning) {
			//IMMORTALITY ANIMATION
			if(delay2 >= 5) {
				if(burningAnimation < 3) {
					burningAnimation++;
				}else {
					burningAnimation = 0;
				}
				delay2 = 0;
			}else {
				delay2++;
			}
			
			g.drawImage(BURNING_HEALTHBAR_IMAGES[burningAnimation], wMiddle-350, wHeight-315, 700, 80, null);
		}
		if(spieler.immortal) {
			//IMMORTALITY ANIMATION
			if(delay >= 3) {
				if(healthbarAnimation < 24) {
					healthbarAnimation++;
				}else {
					healthbarAnimation = 0;
				}
				delay = 0;
			}else {
				delay++;
			}
			
			g.drawImage(IMMORTAL_HEALTHBAR_IMAGES[healthbarAnimation], wMiddle-350, wHeight-305, 700, 80, null);
		}else {
			g.drawImage(HEALTHBAR_IMAGES[(int)spieler.health], wMiddle-350, wHeight-305, 700, 80, null);
		}
	
	}

}
