package de.ben.playground.escape_the_althen;

import java.awt.Graphics;
import java.util.ArrayList;

public class RenderManager {

	//UNFERTIGE KLASSE ZUM RENDERN MIT BEKANNTEN BUGS
	//(GESAMTE ZEIT BUGFIXING IN DIESER KLASSE: ~3h)
	
	World world;
	Creature creature;
	static ArrayList<Placeable> dynamicTiles = new ArrayList<Placeable>(); // <-- LISTE MIT KACHELN DIE AUFS RENDERING ÜBERPRÜFT WERDEN
	static ArrayList<ArrayList<Placeable>> validTiles = new ArrayList<ArrayList<Placeable>>(); // <-- LISTE MIT KACHELN DIE ÜBERPRÜFT WURDEN
	static ArrayList<Placeable> delayedRender = new ArrayList<Placeable>(); // <-- LISTE MIT KACHELN DIE NACH SPIELER VERZÖGERT GERENDERT WERDFEN
	
	public RenderManager(World world, Creature creature) {
		this.world = world;
		this.creature = creature;
	}

	private void updateNearTiles() {
		validTiles.clear();
		/*if((Creature.sortedCreatures.size() > 0) && (dynamicTiles.size() > 0)) {
			for(int currentCreature = 0; currentCreature < Creature.sortedCreatures.size(); currentCreature++) {
				validTiles.add(currentCreature, new ArrayList<Tile>());
				for(int tile = 0; tile < dynamicTiles.size(); tile++) {
					if(dynamicTiles.get(tile).isPotentialOrientationPoint(Creature.sortedCreatures.get(currentCreature))) {
						validTiles.get(currentCreature).add(dynamicTiles.get(tile));
					}
				}
			}
		}*/
	}
	
	public void render(Graphics g, boolean renderCollisionBox) {
		//CLEAR TEMP COLLISIONBOX STORAGE
			world.cm.clearBox();
			
		//BACKGROUND, MAIN & FOLIAGE LAYER
			world.renderChunks(g, 0, 2);
		//CREATURES & DYNAMIC LAYER
			dynamicTiles.clear();
			delayedRender.clear();
			world.renderChunks(g, 3, 3);
			//try {
				//ZÄHLERSCHLEIFE FÜR KREATUREN
			if((Creature.Creatures.size() > 0) && (dynamicTiles.size() > 0)) {
				updateNearTiles();
				for(int currentCreature = 0; currentCreature < 1/*Creature.sortedCreatures.size()*/; currentCreature++) {
					//ARRAYLIST FÜR JEDE KREATUR
					delayedRender.clear();
					//ZÄHLERSCHLEIFE FÜR KACHELN IN "DYNAMICTILES" LISTE
					for(int currentTile = 0; currentTile < dynamicTiles.size(); currentTile++) {
						//WENN TILE HINTER DER KREATUR: RENDER TILE
						if(!dynamicTiles.get(currentTile).behindCreature(Creature.Creatures.get(currentCreature))){
							dynamicTiles.get(currentTile).render(g, renderCollisionBox);	
						}else {
							delayedRender.add(dynamicTiles.get(currentTile));
						}
					}
					
					for(int cc = 0; cc < Creature.sortedCreatures.size(); cc++) {
					Creature.sortedCreatures.get(cc).renderCreature(g);
					}
					//try {
					for(int i = 0; i < delayedRender.size(); i++) {
						delayedRender.get(i).render(g, renderCollisionBox);;
					}
					
					//}catch(Exception e) {}
				}
			}
			//}catch(Exception e) {
			//}
			boolean renderplayeragain = false;
			for(int i = 0; i < Creature.sortedCreatures.size(); i++) {
				
				Creature.sortedCreatures.get(i).renderCreature(g);
				
				if(Creature.sortedCreatures.get(i).name.equals("Spieler")) {
					renderplayeragain = true;
				}
			}
			if(renderplayeragain) {
				for(int i = 0; i < delayedRender.size(); i++) {
					delayedRender.get(i).render(g, renderCollisionBox);;
				}
			}
			
			
		//FOLIAGE & AMBIANCE LAYER
			world.renderChunks(g, 4, 4);
		
			
	}
}
