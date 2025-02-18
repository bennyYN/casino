package de.ben.playground.escape_the_althen;

import java.awt.Rectangle;
import java.util.ArrayList;

//KLASSE, WELCHE HÄUFIG AUFTRETENDE KOLLISIONS-BERECHNUNGEN ERLEDIGT
public class CollisionManager {

	//KLASSENATTRIBUTE
	public static ArrayList<CollisionBox> boxes = new ArrayList<CollisionBox>();
	public static ArrayList<CollisionBox> itemBoxes = new ArrayList<CollisionBox>();
	
	//KONSTRUKTOR
	public CollisionManager() {
	}
	
	//METHODE UM EINE KOLLISIONSBOX ZUR ÜBERPRÜFUNG HINZUZUFÜGEN
	public void addBox(CollisionBox box) {
		boxes.add(box);
	}
	
	//METHODE UM DIE LISTE NACH GEBRAUCH DER BOXEN ZU LÖSCHEN (-> WICHTIG FÜR SPIEL OPTIMIERUNG)
	public void clearBox() {
		boxes.clear();
	}

	//METHODE ZUR ÜBERPRÜFUNG OB DER SPIELER MIT EINER DER GELADENEN KOLLIONSBOXEN KOLLIDIERT
	public boolean isColliding(Player player) {
		for(int i = 0; i < boxes.size(); i++) {
			if(boxes.get(i).intersects(player.getCollisionBox())) {
				return true;
			}
		}
		return false;
	}
	
	//METHODE WELCHE BERECHNET, OB DER SPIELER BEI DER ÜBERGEBENEN GESCHWINDIGKEIT KOLLIDIEREN WÜRDE
	public boolean wouldCollide(Player player, double xVelocity, double yVelocity) {
		CollisionBox temp = player.hitbox;
		temp.move(xVelocity*1.5, yVelocity*1.5);
		for(int i = 0; i < boxes.size(); i++) {
			if(boxes.get(i).intersects(temp)) {
				return true;
			}
		}
		return false;
	}
	
	//METHODE FÜR KOLLISIONSÜBERPRÜFUNG VON ITEMS UND SPIELER (ZUM AUFSAMMELN VON GEGENSTÄNDEN)
	public boolean isCollecting(Player player) {
		if(itemBoxes.size() != 0) {
			for(int i = 0; i < itemBoxes.size(); i++) {
				if(itemBoxes.get(i).intersects(player.getCollisionBox())) {
					return true;
				}
			}
		}
		return false;
	}
	
	//METHODE ZUR BERECHNUNG OB EINE GEGEBENE KREATUR MIT EINER ÜBERGEBENEN GESCHWINDIGKEIT KOLLIDIEREN WÜRDE
	public boolean wouldCollide(Creature creature, double xVelocity, double yVelocity) {
		CollisionBox temp = creature.hitbox;
		temp.move(xVelocity, yVelocity);
		for(int i = 0; i < boxes.size(); i++) {
			if(boxes.get(i).intersects(temp)) {
				return true;
			}
		}
		return false;
	}
	
	//METHODE WELCHE TESTET, OB EINE ÜBERGEBENE COLLISIONSBOX ODER ANDERE SUBKLASSE DES TYPEN RECTANGLE KOLLIDIERT
	public boolean collideWithAny(Rectangle collisionbox) {
		for(int i = 0; i < boxes.size(); i++) {
			if(boxes.get(i).intersects(collisionbox)) {
				return true;
			}
		}
		return false;
	}
}
