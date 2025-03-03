package de.ben.playground.escape_the_althen;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Tile extends Placeable{

		//ATTRIBUTE & OBJEKTE
			public int xTilePosition, yTilePosition;
			private Image texture;
			private double[] playerPos;
			private double scale;
			public boolean isSolid, nearPlayer, potOP;
			public String type;
			public boolean overwritten;
			public boolean topSolidBlock = false;
			private CollisionBox cBox;
			private boolean thin = false;
			public int layer;
			CollisionManager cm = new CollisionManager();
	
	//KONSTRUKTOR
		public Tile(int xTilePos, int yTilePos, double scale, double[] playerPos, String type, int layer) {
			//"SOLID" EIGENSCHAFT ANHAND DES ÜBERGEBENEN TILE-TYPS ÜBERPRÜFEN
				updateSolidState(type);
			//ATTRIBUTE FESTLEGEN
				xTilePosition = xTilePos;
				yTilePosition = yTilePos;
				this.scale = scale;
				this.playerPos = playerPos;
				this.type = type;
			//TEXTUR ANHAND DES TILE-TYPS ALS BILD DATEI SPEICHERN
				//change texture only with a 20% chance to make the grass look more natural
				changeType(type);
				
				
		
	}

	//METHODE ZUM NACHTRÄGLICHEN ÄNDERN DES TILE-TYPS
		public void changeType(String type) {
			if(type.equals("grass")){
				//change texture only with a 20% chance to make the grass look more natural
				if(Math.random() > 0.92) {
					this.texture = new ImageIcon("img/playground/escapethealthen/graphics/tiles/flowering_grass.png").getImage();
				}else{
					this.texture = new ImageIcon("img/playground/escapethealthen/graphics/tiles/"+type+".png").getImage();
				}
			}else if(type.equals("path")){
				//change texture only with a 20% chance to make the grass look more natural
				if(Math.random() > 0.5) {
					this.texture = new ImageIcon("img/playground/escapethealthen/graphics/tiles/path_variation.png").getImage();
				}else{
					this.texture = new ImageIcon("img/playground/escapethealthen/graphics/tiles/"+type+".png").getImage();
				}
			}else{
				this.texture = new ImageIcon("img/playground/escapethealthen/graphics/tiles/"+type+".png").getImage();
			}
			this.type = type;
			if(layer == 3 && type != "void") {
				thin = true;
			}
			updateSolidState(type);
		}

	//METHODE UM DIE TILE ZU RENDERN
		public void render(Graphics g, boolean renderCollisionBox) {
			//TILE RENDERN
			if(type.equals("grass")) {
				g.drawImage(texture, (int)((xTilePosition*16*scale)-((int)playerPos[0])), (int)(((yTilePosition-1)*16*scale)-((int)playerPos[1])), (int)(16*scale), (int)(32*scale), null);
			}else {
				g.drawImage(texture, (int)((xTilePosition*16*scale)-((int)playerPos[0])), (int)((yTilePosition*16*scale)-((int)playerPos[1])), (int)(16*scale), (int)(16*scale), null);
			}
			
			//KOLLISIONSBOX AKTUALISIEREN
			if(thin) {
				cBox = new CollisionBox((int)((xTilePosition*16*scale)-((int)playerPos[0])), (int)((yTilePosition*16*scale)-((int)playerPos[1])), 16, 1, scale, 0, 15);
				
			}else {
				cBox = new CollisionBox((int)((xTilePosition*16*scale)-((int)playerPos[0])), (int)((yTilePosition*16*scale)-((int)playerPos[1])), 16, 16, scale, 0, 0);
			}
			if(isSolid) {
					cm.addBox(cBox);
			}
			if(renderCollisionBox) {
				if(isSolid) {
					g.setColor(new Color(255, 0, 0, 60));
					if(thin) {
						g.fillRect((int)((xTilePosition*16*scale)-((int)playerPos[0])), (int)(((yTilePosition*16*scale)+(15*scale))-((int)playerPos[1])), (int)(16*scale), (int)(1*scale));
					}else {
						g.fillRect((int)((xTilePosition*16*scale)-((int)playerPos[0])), (int)((yTilePosition*16*scale)-((int)playerPos[1])), (int)(16*scale), (int)(16*scale));
					}
					g.setColor(Color.BLACK);
					cBox.render(g, Color.RED);
				}else 
				
				{
					cBox.render(g, Color.YELLOW);
				}
			}
		}
	
	//"SOLID" EIGENSCHAFT ANHAND DES ÜBERGEBENEN TILE-TYPS ÜBERPRÜFEN
		private void updateSolidState(String type) {
			switch(type) {
				default: isSolid = false; break;
				//ÜBERPRÜFUNG ALLER TILE-TYPEN MIT KOLLISIONSÜBERPRÜFUNG
					//TODO -> WIRD BEI ÜBERSCHÜSSIGER ZEIT MITHILFE VON EINEM TILE-TYPE-ATLAS OPTIMIERT!
				
				case "barrier","border",
					"mossy_stonebricks",
					"mossy_chiseled_stonebricks",
					"fence",
					"water1", "water2", "water3", "water4", "water5", "water6", "water7", "water8", "water9"
					: isSolid = true; break;
			}
			//THIN EXCEPTIONS
			if(type == "void") {
				thin = false;
			}
		}
	
	//GETTER-METHODE FÜR DIE KOLLISIONSBOX DER TILE
		public CollisionBox getCollisionBox() {
				return cBox;
		}
		
	
	//ÜBERPRÜFUNG OB DIE TILE NAHE AM SPIELER GELEGEN IST
		public boolean isNearPlayer(double xSpawn, double ySpawn) {
			if((((playerPos[1]+ySpawn)/16/scale >= (yTilePosition-8)) && ((playerPos[1]+ySpawn)/16/scale <= (yTilePosition+7))) && (((playerPos[0]+xSpawn)/16/scale <= (xTilePosition+8)) && ((playerPos[0]+xSpawn)/16/scale >= (xTilePosition-7)))) {
				nearPlayer = true;
				return true;
			}else {
				nearPlayer = false;
				return false;
			}
		}
		
		public boolean behindCreature(Creature creature) {
			if(((this.yTilePosition-(1-0.5))*scale*16) >= creature.getY()) {
				
				
				return true;
				
			}else {
				
				return false;
			}
		}
}
