package de.ben.playground.escape_the_althen;

import de.ben.MainGUI;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Object extends Placeable implements MouseMotionListener {

		//ATTRIBUTE & OBJEKTE
			public String model = "default", lootItemType = "void";
			public int xTilePosition, yTilePosition, potCounter = 0, fireCounter = 0, doorCounter = 0;
			private Image texture;
			private double[] playerPos;
			private double scale;
			public boolean isSolid, nearPlayer, potOP;
			public String type;
			public boolean overwritten;
			public boolean topSolidBlock = false;
			private CollisionBox cBox; //<-- CollisionBox
			public CollisionBox iBox, eBox; //<-- Interaction Box
			Rectangle listenerBox;
			
			private boolean thin = false;
			public int layer;
			CollisionManager cm = new CollisionManager();
			Player player;
			World world;
			public boolean dynamicRendering = true;
			private Rectangle highlightBox;
			private int xHighlightBoxOffset, yHighlightBoxOffset;
			private boolean mouseOver = false;
	
	//KONSTRUKTOR
		public Object(int xTilePos, int yTilePos, double scale, double[] playerPos, String type, int layer, Player player, World world) {
			//"SOLID" EIGENSCHAFT ANHAND DES ÜBERGEBENEN TILE-TYPS ÜBERPRÜFEN
				updateSolidState(type);
			//ATTRIBUTE FESTLEGEN
				xTilePosition = xTilePos;
				yTilePosition = yTilePos;
				this.scale = scale;
				this.playerPos = playerPos;
				this.type = type;
			//TEXTUR ANHAND DES TILE-TYPS ALS BILD DATEI SPEICHERN
            try {
                this.texture = new ImageIcon(ImageIO.read(HUD.class.getClassLoader().getResourceAsStream("img/playground/escapethealthen/graphics/objects/"+type+".png"))).getImage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            highlightBox = new Rectangle((int)((xTilePosition * 16 * scale) - ((int)playerPos[0])), (int)((yTilePosition * 16 * scale) - ((int)playerPos[1])), (int)(16 * scale), (int)(16 * scale));
				listenerBox = highlightBox;
				if(layer == 3 && type != "void") {
					thin = true;
				}
				this.layer = layer;
				this.player = player;
				this.world = world;
				iBox = new CollisionBox((int)(((xTilePosition*16)-1)*scale), (int)(((yTilePosition*16)-1)*scale), 18, 18, scale, 0, 0);
	}
	
		public Object(int xTilePos, int yTilePos, double scale, double[] playerPos, String type, int layer, Player player, World world, String item) {
			//"SOLID" EIGENSCHAFT ANHAND DES ÜBERGEBENEN TILE-TYPS ÜBERPRÜFEN
				lootItemType = item;
				updateSolidState(type);
			//ATTRIBUTE FESTLEGEN
				xTilePosition = xTilePos;
				yTilePosition = yTilePos;
				this.scale = scale;
				this.playerPos = playerPos;
				this.type = type;
				highlightBox = new Rectangle((int)((xTilePosition * 16 * scale) - ((int)playerPos[0])), (int)((yTilePosition * 16 * scale) - ((int)playerPos[1])), (int)(16 * scale), (int)(16 * scale));
			listenerBox = highlightBox;
			//TEXTUR ANHAND DES TILE-TYPS ALS BILD DATEI SPEICHERN
            try {
                this.texture = new ImageIcon(ImageIO.read(HUD.class.getClassLoader().getResourceAsStream("img/playground/escapethealthen/graphics/objects/"+type+".png"))).getImage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if(layer == 3 && type != "void") {
					thin = true;
				}
				this.layer = layer;
				this.player = player;
				this.world = world;
				iBox = new CollisionBox((int)(((xTilePosition*16)-1)*scale), (int)(((yTilePosition*16)-1)*scale), 18, 18, scale, 0, 0);
	}
		
	//METHODE ZUM NACHTRÄGLICHEN ÄNDERN DES TILE-TYPS
		public void changeType(String type) {
            try {
                this.texture = new ImageIcon(ImageIO.read(HUD.class.getClassLoader().getResourceAsStream("img/playground/escapethealthen/graphics/objects/"+type+".png"))).getImage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.type = type;
			if(layer == 3 && type != "void") {
				thin = true;
			}
			updateSolidState(type);
		}
		public void changeType(String type, String lootItem) {
            try {
                this.texture = new ImageIcon(ImageIO.read(HUD.class.getClassLoader().getResourceAsStream("img/playground/escapethealthen/graphics/objects/"+type+".png"))).getImage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            this.type = type;
			if(layer == 3 && type != "void") {
				thin = true;
			}
			lootItemType = lootItem;
			updateSolidState(type);
		}

	//METHODE UM DIE TILE ZU RENDERN
		public void render(Graphics g, boolean renderCollisionBox) {
			//TILE RENDERN
			if(model.equals("door")){
				g.drawImage(texture, (int)((xTilePosition*16*scale)-((int)playerPos[0])), (int)((yTilePosition*16*scale)-((int)playerPos[1])), (int)(32*scale), (int)(48*scale), null);
			}else if(type.equals("tree1")){
				model = "chest";
				g.drawImage(texture, (int)((((xTilePosition-2)*16*scale))-((int)playerPos[0])), (int)(((yTilePosition-6)*16*scale)-((int)playerPos[1])), (int)(96*scale), (int)(112*scale), null);
			}else if(type.equals("tree2")) {
				model = "pot";
				g.drawImage(texture, (int) (((xTilePosition-2) * 16 * scale +(5*scale)) - ((int) playerPos[0])), (int) (((yTilePosition-5) * 16 * scale) - ((int) playerPos[1])), (int) (70 * scale), (int) (96 * scale), null);
			}else{
				g.drawImage(texture, (int)((xTilePosition*16*scale)-((int)playerPos[0])), (int)((yTilePosition*16*scale)-((int)playerPos[1])), (int)(16*scale), (int)(16*scale), null);
			}
			//KOLLISIONSBOX AKTUALISIEREN
			if(thin) {
				cBox = new CollisionBox((int)((xTilePosition*16*scale)-((int)playerPos[0])), (int)((yTilePosition*16*scale)-((int)playerPos[1])), 16, 1, scale, 0, 15);
				
			}else {
				cBox = new CollisionBox((int)((xTilePosition*16*scale)-((int)playerPos[0])), (int)((yTilePosition*16*scale)-((int)playerPos[1])), 16, 16, scale, 0, 0);
			}

			switch(model) {
				case "pot":
					xHighlightBoxOffset = 3;
					yHighlightBoxOffset = 1;
					highlightBox = new Rectangle((int)(((xTilePosition*16*scale)+xHighlightBoxOffset*scale)-player.position[0]), (int)(((yTilePosition*16*scale)+yHighlightBoxOffset*scale)-player.position[1]), (int)(10*scale), (int)(15*scale));
					break;
				case "chest":
					cBox = new CollisionBox((int)((xTilePosition*16*scale)-((int)playerPos[0])), (int)((yTilePosition*16*scale)-((int)playerPos[1])), 14, 2, scale, 1, 12);
					xHighlightBoxOffset = 0;
					yHighlightBoxOffset = 3;
					highlightBox = new Rectangle((int)(((xTilePosition*16*scale)+xHighlightBoxOffset*scale)-player.position[0]), (int)(((yTilePosition*16*scale)+yHighlightBoxOffset*scale)-player.position[1]), (int)(16*scale), (int)(13*scale));
					break;
				case "door":
					cBox = new CollisionBox((int)((xTilePosition*16*scale)-((int)playerPos[0])), (int)((yTilePosition*16*scale)-((int)playerPos[1])), 32, 48, scale, 0, 0);
					xHighlightBoxOffset = 17;
					yHighlightBoxOffset = 34;
					highlightBox = new Rectangle((int)(((xTilePosition*16*scale)+xHighlightBoxOffset*scale)-player.position[0]), (int)(((yTilePosition*16*scale)+yHighlightBoxOffset*scale)-player.position[1]), (int)(5*scale), (int)(6*scale));
					break;
				default:
					xHighlightBoxOffset = 0;
					yHighlightBoxOffset = 0;
					highlightBox = new Rectangle((int)(((xTilePosition*16*scale)+xHighlightBoxOffset*scale)-player.position[0]), (int)(((yTilePosition*16*scale)+yHighlightBoxOffset*scale)-player.position[1]), (int)(16*scale), (int)(16*scale));
					break;
			}	

			if(isSolid) {
					cm.addBox(cBox);
			}
			if(renderCollisionBox) {
				if(isSolid) {
					g.setColor(new Color(255, 0, 0, 60));
					g.fillRect((int)cBox.getX(), (int)cBox.getY(), (int)cBox.getWidth(), (int)cBox.getHeight());
					g.setColor(Color.BLACK);
					cBox.render(g, Color.RED);
				}else{
					cBox.render(g, Color.YELLOW);
				}
				if(type != "void") {
					iBox.render(g, Color.WHITE);
				}
				if(model.equals("door")) {
					eBox.render(g, Color.GREEN);
				}
			}
			
			//OBJECT UPDATE
			
			if(type.equals("fire1") || type.equals("fire2") || type.equals("fire3") || type.equals("fire4") || type.equals("fire5") || type.equals("fire6") || type.equals("fire7") || type.equals("fire8"))  {
				iBox = new CollisionBox((int)((((xTilePosition*16))*scale)-((int)playerPos[0])), (int)((((yTilePosition*16))*scale)-((int)playerPos[1])), 16, 16, scale, 0, 0);
			}else if(model.equals("door")){
				eBox = new CollisionBox((int)((xTilePosition*16*scale)-((int)playerPos[0])), (int)(((yTilePosition)*16*scale)-((int)playerPos[1])), 26, 8, scale, 3, 41);
				iBox = new CollisionBox((int)((xTilePosition*16*scale)-((int)playerPos[0])), (int)(((yTilePosition)*16*scale)-((int)playerPos[1])), 38, 8, scale, -3, 44);
				if(type.equals("door22") && playerInEventBox()) {
					world.p.levelCompleted = true;
					MainGUI.playSound("level_completed");
				}

			}else {
				iBox = new CollisionBox((int)((((xTilePosition*16)-1)*scale)-((int)playerPos[0])), (int)((((yTilePosition*16)-1)*scale)-((int)playerPos[1])), 18, 18, scale, 0, 0);

			}
			//TODO -> KLASSE "AnimationManager" FÜR EINFACHEREN CODE FÜR ANIMATIONEN UND GENERELL LESBAREREN CODE!! (WENN ZEIT)
			switch(type) {
			case "pot1": 
				if(isPlayerInRange() && player.hitEvent && mouseOver && world.p.inv.getSelectedItemType().equals("hammer")) {
					changeType("pot2");
					player.hitEvent = false;
					MainGUI.playSound("pot_breaking");
				}
				break;
			case "pot2": 
				if(potCounter == 5) {
					changeType("pot3");
					potCounter = 0;
				}else {
					potCounter++;
				}
				break;
			case "pot3": 
				if(potCounter == 4) {
					changeType("pot4");
					potCounter = 0;
				}else {
					potCounter++;
				}
				break;
			case "pot4": 
				if(potCounter == 3) {
					changeType("pot5");
					potCounter = 0;
					if(!lootItemType.equals("void")) {
						Item droppedItem = new Item(world, xTilePosition, yTilePosition, lootItemType);
					}
				}else {
					potCounter++;
				}
				break;
			case "pot5": 
				if(potCounter == 3) {
					changeType("pot6");
					potCounter = 0;
				}else {
					potCounter++;
				}
				break;
			case "pot6": 
				if(potCounter == 3) {
					changeType("pot7");
					potCounter = 0;
				}else {
					potCounter++;
				}
				break;
				case "bronze_chest1":
				if(isPlayerInRange() && player.interactionEvent && world.p.inv.getSelectedItemType().equals("bronze_key")  && mouseOver) {
					player.interactionEvent = false;
					MainGUI.playSound("toggle2");
					changeType("bronze_chest2");
					world.p.inv.removeSelectedItem();
					Item droppedItem = new Item(world, xTilePosition, yTilePosition, lootItemType);
				}
				break;
				case "iron_chest1":
					if(isPlayerInRange() && player.interactionEvent && world.p.inv.getSelectedItemType().equals("iron_key")  && mouseOver) {
						player.interactionEvent = false;
						MainGUI.playSound("toggle2");
						changeType("iron_chest2");
						world.p.inv.removeSelectedItem();
						Item droppedItem = new Item(world, xTilePosition, yTilePosition, lootItemType);
					}
					break;
				case "golden_chest1":
					if(isPlayerInRange() && player.interactionEvent && world.p.inv.getSelectedItemType().equals("golden_key")  && mouseOver) {
						player.interactionEvent = false;
						MainGUI.playSound("toggle2");
						changeType("golden_chest2");
						world.p.inv.removeSelectedItem();
						Item droppedItem = new Item(world, xTilePosition, yTilePosition, lootItemType);
					}
					break;
				case "diamond_chest1":
					if(isPlayerInRange() && player.interactionEvent && world.p.inv.getSelectedItemType().equals("diamond_key")  && mouseOver) {
						player.interactionEvent = false;
						MainGUI.playSound("toggle2");
						changeType("diamond_chest2");
						world.p.inv.removeSelectedItem();
						Item droppedItem = new Item(world, xTilePosition, yTilePosition, lootItemType);
					}
					break;
			case "fire1": 
				if(isPlayerInRange()) {
					player.burning = true;
				}
				if(fireCounter == 20) {
					changeType("fire2");
					fireCounter = 0;
				}else {
					fireCounter++;
				}
				break;
			case "fire2": 
				if(isPlayerInRange()) {
					player.burning = true;
				}
				if(fireCounter == 20) {
					changeType("fire3");
					fireCounter = 0;
				}else {
					fireCounter++;
				}
				break;
			case "fire3": 
				if(isPlayerInRange()) {
					player.burning = true;
				}
				if(fireCounter == 20) {
					changeType("fire4");
					fireCounter = 0;
				}else {
					fireCounter++;
				}
				break;
			case "fire4": 
				if(isPlayerInRange()) {
					player.burning = true;
				}
				if(fireCounter == 20) {
					changeType("fire5");
					fireCounter = 0;
				}else {
					fireCounter++;
				}
				break;
			case "fire5": 
				if(isPlayerInRange()) {
					player.burning = true;
				}
				if(fireCounter == 20) {
					changeType("fire6");
					fireCounter = 0;
				}else {
					fireCounter++;
				}
				break;
			case "fire6": 
				if(isPlayerInRange()) {
					player.burning = true;
				}
				if(fireCounter == 20) {
					changeType("fire7");
					fireCounter = 0;
				}else {
					fireCounter++;
				}
				break;
			case "fire7": 
				if(isPlayerInRange()) {
					player.burning = true;
				}
				if(fireCounter == 20) {
					changeType("fire8");
					fireCounter = 0;
				}else {
					fireCounter++;
				}
				break;
			case "fire8": 
				if(isPlayerInRange()) {
					player.burning = true;
				}
				if(fireCounter == 20) {
					changeType("fire1");
					fireCounter = 0;
				}else {
					fireCounter++;
				}
				break;
			case "door1": 
				if(isPlayerInRange() && player.interactionEvent && world.p.inv.getSelectedItemType().equals("golden_key")  && mouseOver) {
					//player.interactionEvent = false;
					changeType("door2");
					MainGUI.playSound("door_opening");
					world.p.inv.removeSelectedItem();
				}
				break;
			case "door2": 
				if(doorCounter == 20) {
					changeType("door3");
					doorCounter = 0;
				}else {
					doorCounter++;
				}
				break;
			case "door3": 
				if(doorCounter == 10) {
					changeType("door4");
					doorCounter = 0;
				}else {
					doorCounter++;
				}
				break;
			case "door4": 
				if(doorCounter == 10) {
					changeType("door5");
					doorCounter = 0;
				}else {
					doorCounter++;
				}
				break;
			case "door5": 
				if(doorCounter == 10) {
					changeType("door6");
					doorCounter = 0;
				}else {
					doorCounter++;
				}
				break;
			case "door6": 
				if(doorCounter == 10) {
					changeType("door7");
					doorCounter = 0;
				}else {
					doorCounter++;
				}
				break;
			case "door7": 
				if(doorCounter == 10) {
					changeType("door8");
					doorCounter = 0;
				}else {
					doorCounter++;
				}
				break;
			case "door8": 
				if(doorCounter == 10) {
					changeType("door9");
					doorCounter = 0;
				}else {
					doorCounter++;
				}
				break;
			case "door9": 
				if(doorCounter == 10) {
					changeType("door10");
					doorCounter = 0;
				}else {
					doorCounter++;
				}
				break;
			case "door10": 
				if(doorCounter == 10) {
					changeType("door11");
					doorCounter = 0;
				}else {
					doorCounter++;
				}
				break;
			case "door11": 
				if(doorCounter == 10) {
					changeType("door12");
					doorCounter = 0;
				}else {
					doorCounter++;
				}
				break;
			case "door12": 
				if(doorCounter == 10) {
					changeType("door13");
					doorCounter = 0;
				}else {
					doorCounter++;
				}
				break;
			case "door13": 
				if(doorCounter == 10) {
					changeType("door14");
					doorCounter = 0;
				}else {
					doorCounter++;
				}
				break;
			case "door14": 
				if(doorCounter == 10) {
					changeType("door15");
					doorCounter = 0;
				}else {
					doorCounter++;
				}
				break;
			case "door15": 
				if(doorCounter == 10) {
					changeType("door16");
					doorCounter = 0;
				}else {
					doorCounter++;
				}
				break;
			case "door16": 
				if(doorCounter == 10) {
					changeType("door17");
					doorCounter = 0;
				}else {
					doorCounter++;
				}
				break;
			case "door17": 
				if(doorCounter == 10) {
					changeType("door18");
					doorCounter = 0;
				}else {
					doorCounter++;
				}
				break;
			case "door18": 
				if(doorCounter == 10) {
					changeType("door19");
					doorCounter = 0;
				}else {
					doorCounter++;
				}
				break;
			case "door19": 
				if(doorCounter == 10) {
					changeType("door20");
					doorCounter = 0;
				}else {
					doorCounter++;
				}
				break;
			case "door20": 
				if(doorCounter == 10) {
					changeType("door21");
					doorCounter = 0;
				}else {
					doorCounter++;
				}
				break;
			case "door21": 
				if(doorCounter == 10) {
					changeType("door22");
					doorCounter = 0;
				}else {
					doorCounter++;
				}
				break;
			}
			if (mouseOver) {
				g.setColor(Color.YELLOW);
				g.drawRect((int)((highlightBox.x)), (int)((highlightBox.y)), highlightBox.width, highlightBox.height);
			}
		}
	
	//"SOLID" EIGENSCHAFT ANHAND DES ÜBERGEBENEN TILE-TYPS ÜBERPRÜFEN
		private void updateSolidState(String type) {
			switch(type) {
				default: isSolid = false; break;
				//ÜBERPRÜFUNG ALLER TILE-TYPEN MIT KOLLISIONSÜBERPRÜFUNG
					//TODO -> WIRD BEI ÜBERSCHÜSSIGER ZEIT MITHILFE VON EINEM OBJECT-TYPE-ATLAS OPTIMIERT!
				
				case "pot1",
					"pot2",
					"pot3",
					"pot4",
					 "tree1",
					 "tree2",
					"bronze_chest1",
					"bronze_chest2",
					 "iron_chest1",
					 "iron_chest2",
					 "golden_chest1",
					 "golden_chest2",
					 "diamond_chest1",
					 "diamond_chest2",
					"door1",
					"door2",
					"door3",
					"door4",
					"door5",
					"door6",
					"door7",
					"door8",
					"door9",
					"door10",
					"door11",
					"door12",
					"door13",
					"door14",
					"door15",
					"door16",
					"door17",
					"door18",
					"door19",
					"door20",
					"door21",
					"door22"
					: isSolid = true; break;
			}
			//COLLISIONBOX MODEL:
			switch(type) {
			case "pot1",
				"pot2",
				"pot3",
				"pot4",
				"pot5",
				"pot6",
				"pot7"
				: model = "pot"; break;
			case "bronze_chest1", "bronze_chest2", "iron_chest1", "iron_chest2", "golden_chest1", "golden_chest2", "diamond_chest1", "diamond_chest2": model = "chest"; break;
			case "door1",
			"door2",
			"door3",
			"door4",
			"door5",
			"door6",
			"door7",
			"door8",
			"door9",
			"door10",
			"door11",
			"door12",
			"door13",
			"door14",
			"door15",
			"door16",
			"door17",
			"door18",
			"door19",
			"door20",
			"door21",
			"door22": model = "door"; break;
			}
			//RENDERING EXCEPTIONS:
			switch(type) {
			case "pot5",
				"pot6",
				"pot7"
				: dynamicRendering = false; break;
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
			if(dynamicRendering) {
				if(((this.yTilePosition-(1-0.5))*scale*16) >= creature.getY()) {
				
				
				return true;
				
			}else {
				
				return false;
			}
			}else {
				return false;
			}
			
		}
		
		public boolean isPlayerInRange() {
			if(player.hitbox.intersects(iBox)) {
				return true;
			}else {
				return false;
			}
		}
		
		public boolean playerInEventBox() {
			if(player.hitbox.intersects(eBox)) {
				return true;
			}else {
				return false;
			}
		}

	@Override
	public void mouseMoved(MouseEvent e) {
		// Check if the mouse is over the object
		if (highlightBox != null) {
			listenerBox.setLocation(highlightBox.x, highlightBox.y + (int)(6*scale));
			mouseOver = listenerBox.contains(e.getPoint());
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {}
}
