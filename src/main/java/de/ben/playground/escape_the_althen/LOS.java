package de.ben.playground.escape_the_althen;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

//KLASSE FÜR EINE "LOS" (LINE OF SIGHT) *VON* EINER KREATUR *ZUM* SPIELER
public class LOS{

	//ATTRIBUTE & OBJEKTE
		public double lineVector[] = {0, 0};
		public double lineVectorSegment[] = {0, 0};
		public double maxLength;
		private ArrayList<Point> bulletPoints = new ArrayList<Point>();
		private Player player;
		private Creature creature;
		public CollisionManager cm;
		Rectangle temp;
	
	//KONSTRUKTOR
		public LOS(Creature creature, Player player, double maxLength) {
			//LINIENVEKTOREN AUSRECHNEN
			this.player = player;
			this.creature = creature;
			lineVector[0] = (player.getX())-(creature.getX());
			lineVector[1] = (player.getY())-(creature.getY());
			cm = new CollisionManager();
			this.maxLength = maxLength*player.scale*16;
		}

	//METHODE ZUM RENDERN DER LINIE
		public void render(Graphics g) {
			for(int i = 0; i < bulletPoints.size()-1; i++) {
				if(!outOfSight()) {
					if(isObstructed()) {
						g.setColor(Color.RED);
					}else {
						g.setColor(Color.BLACK);
					}
					g.drawLine((int)bulletPoints.get(i).getX(), (int)bulletPoints.get(i).getY(), (int)bulletPoints.get(i+1).getX(), (int)bulletPoints.get(i+1).getY());
					g.setColor(Color.BLACK);
				}	
			}
		}
	
	//METHODE ZUM AKTUALISIEREN DER LINIE
		public void update() {
			//AKTUALISIEREN DES VEKTORS
				lineVector[0] = player.getX()-creature.getX();
				lineVector[1] = player.getY()-(creature.getY());
				lineVectorSegment[0] = ((lineVector[0]/100));
				lineVectorSegment[1] = ((lineVector[1]/100));
			//AKTUALISIEREN DER ARRAYLIST
				bulletPoints.clear();
				for(int i = 0; i <= 100; i++) {
					bulletPoints.add(new Point((int)(creature.getX()-player.position[0]+(i*lineVectorSegment[0])), (int)(creature.getY()-(player.position[1]-(player.height-(player.scale*10)))+(i*lineVectorSegment[1]))));
				}
		}
	
	//GUCKEN OB EINE SOLIDE KOLLISIONSBOX IM WEG IST
		public boolean isObstructed() {
			temp = new Rectangle(0,0,0,0);
			
			for(int i = 0; i < bulletPoints.size()-1; i++) {
				temp.setBounds((int)(bulletPoints.get(i).getX()-16), (int)(bulletPoints.get(i).getY()-8), 24, 24);
				if(cm.collideWithAny(temp)) {
					return true;
				}
			}
			return false;
		}
	
	//RECHNET LÄNGE DES VEKTORS
		public double getVectorLength() {
			return Math.sqrt((Math.pow(lineVector[0], 2)+Math.pow(lineVector[1], 2)));
		}
	
	//TESTET OB DER SPIELER IN DER MAXIMALEN REICHWEITE LIEGT
		public boolean outOfSight() {
			if(getVectorLength() < maxLength) {
				return false;
			}else {
				return true;
			}
		}
	
	//ÜBERPRÜFT OB DER SPIELER DIREKT GESEHEN WIRD
		public boolean playerInSight() {
			if(!(outOfSight() || isObstructed())) {
				return true;
			}else {
				return false;
			}
		}
}
