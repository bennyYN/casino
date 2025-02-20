package de.ben.playground.escape_the_althen;

import java.awt.Graphics;

import javax.swing.ImageIcon;

public class Einweiser extends Creature{

	//ATTRIBUTE
	Player player;
	CollisionManager cm;
	
	//KONSTRUKTOR
	public Einweiser(double xSp, double ySp, double walkingSpeed, double gameScale, int MaxHealth, Player player) {
		super(xSp, ySp, walkingSpeed, gameScale, MaxHealth);
		name = "Einweiser";
		this.player = player;
		los = new LOS(this, player, 7);	
		cm = new CollisionManager();
		height = 32*scale;	
	}

	//METHODE UM DIE KREATUR ZU RENDERN
	@Override
	public void renderCreature(Graphics g) {
		//KREATUR NUR RENDERN WENN AM LEBEN
		if(isAlive) {
			//WENN DIE KREATUR AM LAUFEN IST, SOLL EINE LAUFANIMATION GESPIELT WERDEN
			if(isWalking) {
				if(moveDirection == "up" || moveDirection == "down") {
					g.drawImage(new ImageIcon("img/playground/escapethealthen/graphics/creatures/einweiser/walking/"+moveDirection+textureVariation2+".png").getImage(),  (int)((position[0]*16*scale)-((int)player.position[0])), (int)((position[1]*16*scale)-((int)player.position[1])), (int)(16*scale), (int)(32*scale), null);
				}else {
					g.drawImage(new ImageIcon("img/playground/escapethealthen/graphics/creatures/einweiser/walking/"+moveDirection+textureVariation1+".png").getImage(),  (int)((position[0]*16*scale)-((int)player.position[0])), (int)((position[1]*16*scale)-((int)player.position[1])), (int)(16*scale), (int)(32*scale), null);
				}
			}else {
			//STEHENDER EINWEISER
			g.drawImage(new ImageIcon("img/playground/escapethealthen/graphics/creatures/einweiser/idle/"+moveDirection+".png").getImage(),  (int)((position[0]*16*scale)-((int)player.position[0])), (int)((position[1]*16*scale)-((int)player.position[1])), (int)(16*scale), (int)(32*scale), null);
			}
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
					velocity[0] = ((los.lineVectorSegment[0]/55)*walkingSpeed);
					velocity[1] = ((los.lineVectorSegment[1]/55)*walkingSpeed);
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
}
