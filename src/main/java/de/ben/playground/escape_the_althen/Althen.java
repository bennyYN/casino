package de.ben.playground.escape_the_althen;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;

public class Althen extends Creature implements MouseMotionListener {

	//ATTRIBUTE
	Player player;
	CollisionManager cm;
	World world;
	private boolean mouseOver = false;
	private Polygon outlinePolygon;
	String droppedItem;
	private boolean droppedItemUponDeath = false;

	// KONSTRUKTOR
	public Althen(double xSp, double ySp, double walkingSpeed, double gameScale, int MaxHealth, Player player, World world, String droppedItem) {
		super(xSp, ySp, walkingSpeed, gameScale, MaxHealth);
		name = "Althen";
		this.player = player;
		los = new LOS(this, player, 7);
		cm = new CollisionManager();
		height = 32 * scale;
		this.world = world;
		this.droppedItem = droppedItem;
	}

	//METHODE UM DIE KREATUR ZU RENDERN
	@Override
	public void renderCreature(Graphics g) {
		this.g = g;
		Graphics2D g2d = (Graphics2D) g;
		int x = (int) ((position[0] * 16 * scale) - ((int) player.position[0]));
		int y = (int) ((position[1] * 16 * scale) - ((int) player.position[1]));
		int width = (int) (16 * scale);
		int height = (int) (32 * scale);

		if (isAlive) {
			if (isWalking) {
				if (moveDirection.equals("up") || moveDirection.equals("down")) {
					g.drawImage(new ImageIcon("src/main/resources/img/playground/escapethealthen/graphics/creatures/althen/walking/" + moveDirection + textureVariation2 + ".png").getImage(), x, y, width, height, null);
				} else {
					g.drawImage(new ImageIcon("src/main/resources/img/playground/escapethealthen/graphics/creatures/althen/walking/" + moveDirection + textureVariation1 + ".png").getImage(), x, y, width, height, null);
				}
			} else {
				g.drawImage(new ImageIcon("src/main/resources/img/playground/escapethealthen/graphics/creatures/althen/idle/" + moveDirection + ".png").getImage(), x, y, width, height, null);
			}

			// Define the polygon points for the visible part of the image
			int[] xPoints = {x, x + width, x + width, x};
			int[] yPoints = {y, y, y + height, y + height};
			outlinePolygon = new Polygon(xPoints, yPoints, xPoints.length);

			if (mouseOver) {
				if(los.getVectorLength()>120){
					g2d.setColor(Color.WHITE);
				}else if(los.getVectorLength()>65){
					g2d.setColor(Color.ORANGE);
				}else{
					g2d.setColor(Color.RED);
				}
				g2d.draw(outlinePolygon);
			}

			g.setColor(Color.BLACK);
			g.drawRect(x, y - 10, width, 5);
			g.setColor(Color.RED);
			g.fillRect(x + 1, y - 9, (int) (width * (health / this.maxHealth)) - 1, 4);
		} else {
			g.drawImage(new ImageIcon("src/main/resources/img/playground/escapethealthen/graphics/creatures/althen/dead.png").getImage(), x, y + (int) (14 * scale), (int) (30 * scale), (int) (18 * scale), null);
		}
	}

	//METHODE UM IN DIE RICHTUNG DES SPIELERS ZU LAUFEN
	public void moveTowardsPlayer() {
		//WIRD NUR AUSGEFÜHRT WENN DAS SPIEL NICHT PAUSIERT IST UND DIE KREATUR LEBT
		if(!gamePaused && isAlive) {
			try {		
				//WENN DER VEKTOR VON DER KREATUR ZUM SPIELER NICHT BLOCKIERT IST:
				if(los.playerInSight()) {
					//SETZEN VON GESCHWINDIGKEIT FÜR X & Y
					if(los.getVectorLength()>0.5){
						isWalking = true;
						velocity[0] = (((los.lineVectorSegment[0]/los.getVectorLength())*4.5)*walkingSpeed);
						velocity[1] = (((los.lineVectorSegment[1]/los.getVectorLength())*4.5)*walkingSpeed);
					}else{
						isWalking = false;
						velocity[0] = ((los.lineVectorSegment[0]/70)*walkingSpeed);
						velocity[1] = ((los.lineVectorSegment[1]/70)*walkingSpeed);
					}
				}else {
					//GESCHWINDIGKEIT AUF 0 SETZEN, WENN EIN OBJEKT DEN VEKTOR UNTERBRICHT
					velocity[0] = 0;
					velocity[1] = 0;
				}
				//TESTEN OB DIE KREATUR BEI DIESEM SCHRITT KOLLIDIEREN WÜRDE
				if(!(cm.wouldCollide(this, velocity[0], 0))) {
					//WENN NEIN WIRD DIE POSITION UM DIE GESETZTE GESCHWINDIGKEIT ERHÖHT
					position[0] = position[0] + velocity[0];
				}
				if(!(cm.wouldCollide(this, 0, velocity[1]))) {
					position[1] = position[1] + velocity[1];
				}
			}catch(Exception ex) {
				ex.toString();
			}
		}
	}	
	
	//METHODE UM DIESE EINZELNE KREATUR ZU AKTUALISIEREN
	@Override
	public void updateIndividual(GamePanel gp) {
		if(isAlive){
			//DEN SPIELER SCHADEN MACHEN
			if(los.getVectorLength()<=65){
				player.health -= 0.025*(1+los.getVectorLength()/(10*scale));
			}
			//SCHADEN DURCH DEN SPIELER KRIEGEN
			if(los.getVectorLength()<=120 && player.hitEvent && world.p.inv.getSelectedItemType().equals("sword") && mouseOver){
				player.hitEvent = false;
				health -= 1.5;
			}
		}

		//AKTUALISIERUNG DER BLICKRICHTUNG ANHAND DER WERTE DER VEKTOREN
		if(Math.abs(los.lineVector[1])>Math.abs(los.lineVector[0]) && velocity[1]<0) {
			moveDirection = "up";
			isWalking = true;
		}
		if(Math.abs(los.lineVector[0])>Math.abs(los.lineVector[1]) && velocity[0]<0) {
			moveDirection = "left";
			isWalking = true;
		}
		if(Math.abs(los.lineVector[0])>Math.abs(los.lineVector[1]) && velocity[0]>0) {
			moveDirection = "right";
			isWalking = true;
		}
		if(Math.abs(los.lineVector[1])>Math.abs(los.lineVector[0]) && velocity[1]>0) {
			moveDirection = "down";
			isWalking = true;
		}
		//clear walking flag if no movement was done at all
		if(velocity[0] == 0 && velocity[1] == 0) {
			isWalking = false;
		}
		
		//AKTUALISIERUNG DER HITBOX (KOLLISIONSBOX) UND EINEN SCHRITT RICHTUNG SPIELER
		hitbox = new CollisionBox((int)((position[0]*16*scale)-((int)player.position[0])), (int)((position[1]*16*scale)-((int)player.position[1])), 8, 4, scale, 4, 28);
		moveTowardsPlayer();
		
		//AKTUALISIERUNG DES VEKTORS ZWISCHEN DIESER KREATUR UND DEM SPIELER
		//(LOS = LINE OF SIGHT -> DEUTSCH: SICHTLINIE)
		los.update();
		
		//ÜBERPRÜFUNG DER GESUNDHEIT DER KREATUR:
		//WENN 0 ODER WENIGER HERZEN DA SIND, SOLL DER TOD DER KREATUR AUSGELÖST WERDEN
		if(health <= 0) {
			triggerDeathEvent();
		}
	}
	
	//METHODEN UM DIE EIGENTLICHE POSITION DER KREATUR ZU BEKOMMEN
	public double getX() {
		return (0.5*scale*16+(position[0]*16*scale));
	}
	public double getY() {
		return (0.5*scale*16+(position[1]*16*scale));
	}

	@Override
	public void triggerDeathEvent() {
		super.triggerDeathEvent();
		if(!droppedItem.equals("void") && !droppedItemUponDeath) {
			droppedItemUponDeath = true;
			new Item(world, (int)position[0], (int)position[1], droppedItem);
		}
		//new Thread to render the death texture
		new Thread() {
			public void run() {
				renderCreature(g);
			}
		}.start();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (outlinePolygon != null) {
			mouseOver = outlinePolygon.contains(e.getPoint());
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {}

}
