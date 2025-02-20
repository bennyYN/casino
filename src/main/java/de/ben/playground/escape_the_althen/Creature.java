package de.ben.playground.escape_the_althen;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.ImageIcon;

public abstract class Creature {

	//GENERELL ÜBERGREIFENDE ATTRIBUTE
		public static int creatureCount = 0;
		public static ArrayList<Creature> Creatures= new ArrayList<Creature>();
		public static ArrayList<Creature> sortedCreatures= new ArrayList<Creature>();
		public static byte textureVariation1 = 1, textureVariation2 = 1, textureVariation3 = 1, textureVariation4 = 1;
		Random random;
		LOS los;
	
	//GENERELLE ATTRIBUTE
		public boolean isAlive, isWalking, isHostile;
		public static boolean gamePaused;
		public double health;
		CollisionBox hitbox;
		CollisionBox iBox;
		public double[] position = new double[2];
		public double[] velocity = {0, 0};
		public String moveDirection = "down";
		public double walkingSpeed, scale, strength, height;
		double xSp, ySp;
		String name;
	
	//KONSTRUKTOR
		public Creature(double xSp, double ySp, double walkingSpeed, double gameScale, int maxHealth) {
			creatureCount++;
			Creatures.add(this);
			sortedCreatures.add(this);
		
			random = new Random();
			
			health = maxHealth;
			this.xSp = xSp;
			this.ySp = ySp;
			isAlive = true;
			isWalking = false;
			position[0] = xSp;
			position[1] = ySp;
			this.scale = gameScale;
			this.walkingSpeed = walkingSpeed;
			
			
		}

	//GENERELLE, IN DEN SUBKLASSEN ZU IMPLEMENTIERENDE METHODEN
		public abstract void renderCreature(Graphics g);
	
		public abstract void updateIndividual(GamePanel gp);
	
		public abstract double getX();
	
		public abstract double getY();
	
	//GENERELLE METHODEN
		//METHODE ZUM AUSLÖSEN DES TODES DIESER KREATUR
		public void triggerDeathEvent() {
			isAlive = false;
			creatureCount--;
			sortedCreatures.remove(this);
			Creatures.remove(this);
		}
	
		//METHODE ZUM ERLANGEN DER KOLLISIONSBOX DIESER KREATUR
		public CollisionBox getCollisionBox() {
			return hitbox;
		}
	
		//METHODE UM ALLE KREATUREN ZU UPDATEN
		//(RUFT 'updateIndividual()' FÜR JEDE KREATUR AUF)
		public void update(GamePanel gp) {
			// ZÄHLERSCHLEIFE UM ALLE KREATUREN EINZELN ZU UPDATEN
			for (int i = 0; i < sortedCreatures.size(); i++) {
				sortedCreatures.get(i).updateIndividual(gp);
			}
			// AKTUALISIERUNG DES "PAUSE-ATTRIBUTS"
			gamePaused = gp.paused;
			// SORTIEREN NACH Y POSITION (FÜRS RENDERING)
			sortedCreatures.sort(new Comparator<Creature>() {
                public int compare(Creature c1, Creature c2) {
                    double c1Pos = c1.getY();
                    double c2Pos = c2.getY();
                    return Double.compare(c1Pos, c2Pos);
                }
            });
		}

		//Methode um alles für einen neustart zu resetten
		public static void reset() {
			creatureCount = 0;
			Creatures.clear();
			sortedCreatures.clear();
		}
}
