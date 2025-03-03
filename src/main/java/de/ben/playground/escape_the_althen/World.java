package de.ben.playground.escape_the_althen;

import java.awt.Graphics;
import java.util.ArrayList;

public class World {

	public double playerPosition[];
	public double xPlayerSpawn, yPlayerSpawn;
	public Chunk chunks[][][];
	public int worldSize = 64; //MAXIMUM = 256
	public boolean renderTileCollisionBoxes = false;
	CollisionManager cm = new CollisionManager();
	RenderManager rm;
	Player player;
	int selectedLevel;
	GamePanel p;
	ArrayList<Object> placedObjects = new ArrayList<Object>();


	public World(double scale, Player player, GamePanel p) {
		//VARIABLEN
		selectedLevel = p.currentLevel;
		chunks = new Chunk[worldSize][worldSize][5];
		
		this.p = p;
		playerPosition = player.position;
		xPlayerSpawn = player.xScreenPos;
		yPlayerSpawn = player.yScreenPos;
		this.player = player;
		//TILE ARRAY FÜLLEN
		for(int lay = 0; lay <= 4; lay++) {
			for(int i = 0; i <= worldSize-1; i++) {
				for(int j = 0; j <= worldSize-1; j++) {
					chunks[i][j][lay] = new Chunk(i, j, scale, playerPosition, xPlayerSpawn, yPlayerSpawn, lay, player, this);
				}
			}
		}
		
		rm = new RenderManager(this, player);	
	}
	
	public void renderChunks(Graphics g, int firstLayer, int lastLayer) {
		/* WORLD LAYERS:
		 * 0 -> Background Layer
		 * 1 -> Main Layer (tiles the player is standing on)
		 * 2 -> Foliage Layer (for plants, ...)
		 * 3 -> Dynamic Layer (Special render properties for thin objects)
		 * 4 -> Object Layer (Layer where Objects are placed on)              
		 *            */
		for(int layer = firstLayer; layer <= lastLayer; layer++) {
			if(layer == 3) {
				for(int i = 0; i <= worldSize-1; i++) {
					for(int j = 0; j <= worldSize-1; j++) {
						try {
							if(chunks[i][j][layer].isNearPlayer(xPlayerSpawn, yPlayerSpawn)) {
								chunks[i][j][layer].renderCB = renderTileCollisionBoxes;
								chunks[i][j][layer].queueTiles(rm); // <-- WARTESCHLANGE ZUR RENDER ÜBERPRÜFUNG
								
							}
						}catch(Exception e) {}
					}
				}
			}else {
				for(int i = 0; i <= worldSize-1; i++) {
					for(int j = 0; j <= worldSize-1; j++) {
						try {
							if(chunks[i][j][layer].isNearPlayer(xPlayerSpawn, yPlayerSpawn)) {
								chunks[i][j][layer].renderCB = renderTileCollisionBoxes;
								chunks[i][j][layer].renderChunk(g, player);
								if(layer == 2) {
								}
								
							}
						}catch(Exception e) {}
					}
				}
			}
		}
		for(int i = 0; i < Item.Items.size(); i++) {
			rm.dynamicTiles.add(Item.Items.get(i));
		}
	}
	
	public Tile getTile(int x, int y, int layer) {
		int xTemp = (int)x/16;
		int yTemp = (int)y/16;
		int bxTemp = (int)(x%16);
		int byTemp = (int)(y%16);
		if(chunks[xTemp][yTemp][layer].getTile(bxTemp, byTemp) == null) {
			return null;
		}else {
			return chunks[xTemp][yTemp][layer].getTile(bxTemp, byTemp);
		}
	}
	
	public void setTile(int x, int y, int layer, String type) {
		int xTemp = (int)x/16;
		int yTemp = (int)y/16;
		int bxTemp = (int)(x%16);
		int byTemp = (int)(y%16);
		chunks[xTemp][yTemp][layer].replaceTile(bxTemp, byTemp, type, layer);
	}
	
	public void setObject(int x, int y, String type) {
		int xTemp = (int)x/16;
		int yTemp = (int)y/16;
		int bxTemp = (int)(x%16);
		int byTemp = (int)(y%16);
		chunks[xTemp][yTemp][3].replaceObject(bxTemp, byTemp, type);
	}
	
	public void setObjectWithItem(int x, int y, String type, String item) {
		int xTemp = (int)x/16;
		int yTemp = (int)y/16;
		int bxTemp = (int)(x%16);
		int byTemp = (int)(y%16);
		chunks[xTemp][yTemp][3].replaceObject(bxTemp, byTemp, type, item);
	}
}
