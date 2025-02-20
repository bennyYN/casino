package de.ben.playground.escape_the_althen;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Player extends Creature{

	//SPIELER SPEZIFISCHE ATTRIBUTE
		public byte xp, lvl;
		public double sprintFactor, xScreenPos, yScreenPos;
		public boolean isSprinting = false;
		public boolean burning = false;
		public boolean immortal = false;
		public boolean hitEvent = false, interactionEvent = false;
		public int burning_counter = 0, immortality_counter = 0, speedCounter = 0;
		public String consumedItem = "void";
	
	//KONSTRUKTOR
		public Player(double xSp, double ySp, double walkingSpeed, double gameScale, double sprintFactor, double xScreenPos, double yScreenPos) {
			super(xSp, ySp, walkingSpeed, gameScale, 16);
			name = "Spieler";
			hitbox = new CollisionBox(xScreenPos, yScreenPos,  8, 4, scale, 4, 28);
			xp = 0;
			lvl = 0;
			height = 32*scale;
			this.xScreenPos = xScreenPos;
			this.yScreenPos = yScreenPos;
			this.sprintFactor = sprintFactor;
		}
	
	//ZU IMPLEMENTIERENDEN METHODEN
		public void renderCreature(Graphics g) {
			if(isAlive) {

			//FLAMES (IF BURNING)
				if(burning) {
					g.drawImage(new ImageIcon("img/playground/escapethealthen/graphics/effects/burning/tall_flames"+textureVariation3+".png").getImage(), (int)xScreenPos, (int)yScreenPos, (int)(16*scale), (int)(32*scale), null);
				}
			//WALKING PLAYER
				if(isWalking) {
					if(moveDirection == "up" || moveDirection == "down") {
						g.drawImage(new ImageIcon("img/playground/escapethealthen/graphics/creatures/player/walking/"+moveDirection+textureVariation4+".png").getImage(), (int)xScreenPos, (int)yScreenPos, (int)(16*scale), (int)(32*scale), null);
					}else {
						g.drawImage(new ImageIcon("img/playground/escapethealthen/graphics/creatures/player/walking/"+moveDirection+textureVariation1+".png").getImage(), (int)xScreenPos, (int)yScreenPos, (int)(16*scale), (int)(32*scale), null);
					}
			//IDLE PLAYER	
				}else {
					g.drawImage(new ImageIcon("img/playground/escapethealthen/graphics/creatures/player/idle/"+moveDirection+".png").getImage(), (int)xScreenPos, (int)yScreenPos, (int)(16*scale), (int)(32*scale), null);
				}
			}else {
				g.drawImage(new ImageIcon("img/playground/escapethealthen/graphics/creatures/player/death/dead_player.png").getImage(), (int)(xScreenPos-(8*scale)), (int)yScreenPos, (int)(32*scale), (int)(32*scale), null);
			}
		}

	//UPDATE METHODE
		public void updateIndividual(GamePanel gp) {
		
			//WENN ITEM KONSUMIERT / GENUTZT
			if(!consumedItem.equals("void")) {
				switch(consumedItem) {
				case "healing_potion": 
					if(health <= 8) {
						health = health + 2;
					}else {
						health = 10;
					}
					break;
				case "water_bottle": burning = false; break;
				}
				consumedItem = "void";
			}
			
			//SPEED-EFFECT
			if(speedCounter > 0) {
				speedCounter--;
				super.walkingSpeed = 0.5;
			}else {
				super.walkingSpeed = 0.3;
			}
			
			//IMMORTALITY
			if(immortal) {
				if(immortality_counter >= 1250) {
					immortal = false;
					immortality_counter = 0;
				}else {
					immortality_counter++;
				}
			}
			
			
			//BURNING
					
			if(burning && health >= 0.005) {
				if(!immortal) {
					health = health - 0.0075;
				}
				if(burning_counter >= 600) {
					burning_counter = 0;
					burning = false;
				}else {
					burning_counter++;
				}
			}else if(burning && !immortal) {
				health = 0;
			}
			
			//UPDATE COLLISIONBOX
				hitbox = new CollisionBox(xScreenPos, yScreenPos,  8, 4, scale, 4, 28);
			//PLAYER-DIRECTION
				if(velocity[1]<0) {
					moveDirection = "up";
				}
				if(velocity[1]>0) {
					moveDirection = "down";
				}
				if(velocity[0]<0) {
					moveDirection = "left";
				}
				if(velocity[0]>0) {
					moveDirection = "right";
				}
			//EXPERIENCE-SYSTEM
				if(xp >= 21) {
					xp = 0;
					if(lvl < 127) {
						lvl++;
					}else {
						lvl = 0;
					}
				}
			//HEALTH-SYSTEM
				if(immortal) {
					health = 16;
				}else if(health <= 0) {
					triggerDeathEvent();
				}
		}
	
	//GETTER FÜR X-KOORDINATE
		public double getX() {
			return (0.5*scale*16+(xScreenPos+position[0]));
		}
		
	//GETTER FÜR Y-KOORDINATE
		public double getY() {
			return (0.5*scale*16+(yScreenPos+position[1]));
		}
		
		public void heal() {
			if(health <= 13) {
				health = health + 3;
			}else {
				health = 16;
			}
		}
}