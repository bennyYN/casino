package de.ben.playground.escape_the_althen;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Item extends Placeable{

	//ATTRIBUTE
	public int x, y;
	double counter = 0;
	World world;
	Image texture;
	String type;
	CollisionBox box;
	CollisionManager cm = new CollisionManager();
	Player player;
	boolean pickedUp = false;
	static Inventory inv;
	RenderManager rm;
	static ArrayList<Item> Items = new ArrayList<Item>();
	
	//KONSTRUKTOR
	public Item(World world, int x, int y, String type) {

		if(!type.equals("void")) {
			Items.add(this);
		}
		this.x = x;
		this.y = y;
		this.world = world;
		this.type = type;
		player = this.world.player;
		rm = new RenderManager(world, player);
		denullInv();
		matchBoxToItem();
		updateTexture();
		RenderManager.dynamicTiles.add(this);
	}
	
	private void denullInv() {
		inv = world.p.inv;
	}
	
	//METHODE ZUM UPDATEN DES INDIVIDUELLEN ITEMS
	public void update(Inventory givenInv) {
		//UPDATE INV
		inv = givenInv;
		//PICK-UP CHECK
		matchBoxToItem();
		if(player.hitbox.intersects(box)) {
			pickUp();
		}
	}
	
	//METHODE ZUM UPDATEN ALLER ITEMS
	public static void updateAll(Inventory givenInv) {
		for(int i = 0; i < Items.size(); i++) {
			if(!Items.get(i).pickedUp) {
				Items.get(i).update(givenInv);
			}
			
		}
	}
	
	//METHODE WELCHE AUFGERUFEN WIRD WENN EIN SPIELER AUF EINEM ITEM STEHT UND
	//DAS ITEM DANN AUFSAMMELT
	//DAS ITEM AN SICH WIRD GELÖSCHT UND DER WERT DES ITEMS INS INVENTAR ÜBERTRAGEN
	private void pickUp() {
		if(!inv.isFull() && !pickedUp) {
			pickedUp = true;
			RenderManager.dynamicTiles.remove(this);
			Items.remove(this);
			inv.addItem(type);			
		}else {
		}
	}
	
	//METHODE UM DIE KOLLISIONSBOX DES ITEMS ZU AKTUALISIEREN
	private void matchBoxToItem() {
		switch(type) {
		default: box = new CollisionBox((int)(((x*16*player.scale))-((int)player.position[0])), (int)(((y*16*player.scale))-((int)player.position[1])), 16, 16, player.scale, 0, 0); break;
		case "immortality_potion", "health_potion", "speed_potion": box = new CollisionBox((int)((x*16*player.scale)-((int)player.position[0])), (int)((y*16*player.scale)-((int)player.position[1])), 10, 14, player.scale, 3, 1); break;
		case "key": box = new CollisionBox((int)((x*16*player.scale)-((int)player.position[0])), (int)((y*16*player.scale)-((int)player.position[1])), 8, 14, player.scale, 4, 1); break;
		}
	}

	//METHODE UM DIE TEXTUR (DAS BILD/ICON) DES ITEMS ZU AKTUALISIEREN
	private void updateTexture() {
		texture = new ImageIcon("src/main/resources/img/playground/escapethealthen/graphics/items/"+type+".png").getImage();
	}
	
	//METHODE, DAMIT SICH DAS ITEM AUF DEM BODEN LEICHT HOCH UND RUNTER BEWEGT (ALS INDIKATOR FÜRS ITEM)
	private double Movement() {
		counter = counter + 0.05;
		if(counter >= 1000) {
			counter = 0;
		}
		return 2*Math.sin(2.5*counter);
	}
	
	//METHODE ZUM RENDERN
	@Override
	public void render(Graphics g, boolean renderCollisionBox) {
		if(!pickedUp) {
			g.drawImage(texture, (int)(((x*16*player.scale))-((int)player.position[0])), (int)(((y*16*player.scale)+Movement())-((int)player.position[1])), (int)(16*player.scale), (int)(16*player.scale), null);
			if(renderCollisionBox && !type.equals("void")) {
				matchBoxToItem();
				g.setColor(Color.WHITE);
				g.drawRect((int)box.getX(), (int)box.getY(), (int)box.getWidth(), (int)box.getHeight());
				g.setColor(Color.BLACK);
			}
		}
		
	}

	@Override
	public boolean behindCreature(Creature creature) {
		if(((this.y-(1-0.5))*player.scale*16) >= creature.getY()) {
			
			
			return true;
			
		}else {
			
			return false;
		}
	}

	
}
