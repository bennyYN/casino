package de.ben.playground.escape_the_althen;

import java.awt.Graphics;

public abstract class Placeable {

		/*  "OBEROBJEKT" FÜR TILE, OBJECT & ITEM
		 *  = EINHEITLICHER TYP FÜR RENDER MANAGER METHODEN
		 *  TODO: (alternative -> verwendung von generics in RenderManager)
		 */
	
	//DEFAULT-KONSTRUKTOR
	public Placeable() {}

	//DIE IN DEN SUBKLASSEN ZU IMPLEMENTIERENDEN METHODEN
	public abstract void render(Graphics g, boolean renderCollisionBox);
	public abstract boolean behindCreature(Creature creature);
}
