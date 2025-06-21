package de.ben.playground.escape_the_althen;

import javax.swing.*;
import java.awt.*;
import java.lang.Object;
import java.util.ArrayList;

public class RenderManager {

	//UNFERTIGE KLASSE ZUM RENDERN MIT BEKANNTEN BUGS
	//(GESAMTE ZEIT BUGFIXING IN DIESER KLASSE: ~3h)
	
	World world;
	Creature creature;
	static ArrayList<Placeable> dynamicTiles = new ArrayList<Placeable>(); // <-- LISTE MIT KACHELN DIE AUFS RENDERING ÜBERPRÜFT WERDEN
	static ArrayList<ArrayList<Placeable>> validTiles = new ArrayList<ArrayList<Placeable>>(); // <-- LISTE MIT KACHELN DIE ÜBERPRÜFT WURDEN
	static ArrayList<Placeable> delayedRender = new ArrayList<Placeable>(); // <-- LISTE MIT KACHELN DIE NACH SPIELER VERZÖGERT GERENDERT WERDFEN
	private int selectedLevel;
	Image baked_shading;
	
	public RenderManager(World world, Creature creature) {
		this.world = world;
		this.selectedLevel = world.selectedLevel;
		this.creature = creature;
	}

	private void updateNearTiles() {
		validTiles.clear();
	}
	
	public void render(Graphics g, boolean renderCollisionBox) {
		//CLEAR TEMP COLLISIONBOX STORAGE
			world.cm.clearBox();
			
		//BACKGROUND, MAIN & FOLIAGE LAYER
			world.renderChunks(g, 0, 2);
		//BAKED SHADING
			baked_shading = new ImageIcon("src/main/resources/img/playground/escapethealthen/graphics/shading/baked_shading_"+selectedLevel+".png").getImage();
			g.drawImage(baked_shading, (int)(0-world.player.position[0]), (int)(0-world.player.position[1]), (int)(baked_shading.getWidth(null)*world.p.gameScale),(int)(baked_shading.getHeight(null)*world.p.gameScale), null);
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
