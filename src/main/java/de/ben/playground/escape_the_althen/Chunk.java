package de.ben.playground.escape_the_althen;

import java.awt.Graphics;

public class Chunk {

	//ATTRIBUTE
	public double xPlayerSpawn, yPlayerSpawn;
	public double gameScale;
	public int xChunkCoord, yChunkCoord, layer;
	public double[] playerPosition;
	public boolean renderCB;
	public Tile tiles[][] = new Tile[16][16]; 
	public Object objects[][] = new Object[16][16];
	CollisionManager cm = new CollisionManager();
	Player player;
	World world;
	
	//KONSTRUKTOR
	public Chunk(int xChunkCoord, int yChunkCoord, double scale, double[] playerPos, double xSpawn, double ySpawn, int layer, Player player, World world) {
		
		//VARIABLEN
		xPlayerSpawn = xSpawn;
		yPlayerSpawn = ySpawn;
		this.layer = layer;
		this.world = world;
		gameScale = scale;
		this.playerPosition = playerPos;
		this.xChunkCoord = xChunkCoord;
		this.yChunkCoord = yChunkCoord;
		this.player = player;
		
		//TILE ARRAY MITHILFE VON EINER ZÄHLERGESTEUERTEN SCHEIFE FÜLLEN
		for(int i = 0; i <= 15; i++) {
			for(int j = 0; j <= 15; j++) {
				if(layer == 1) {
					tiles[i][j] = new Tile(i+(xChunkCoord*16), j+(yChunkCoord*16), scale, playerPos, "grass", layer);
				}else{
					tiles[i][j] = new Tile(i+(xChunkCoord*16), j+(yChunkCoord*16), scale, playerPos, "void", layer);
				}
				if(layer == 3){
					objects[i][j] = new Object(i, j, scale, playerPosition, "void", 3, player, world);
				}
			}
		}
	}
	
	//METHODE ZUM RENDERN EINES CHUNKS
	public void renderChunk(Graphics g, Player player) {
		//ZÄHLERSCHLEIFE FÜR ALLE IM CHUNK ENTHALTENDEN KACHELN
		for(int i = 0; i <= 15; i++) {
			for(int j = 0; j <= 15; j++) {
				//RENDER-METHODE DER EINZELNEN KACHELN AUFRUFEN
				tiles[i][j].render(g, renderCB);
				//WENN DIE KACHEL NAHE AM SPIELER UND SOLIDE IST, WIRD DESSEN KOLLISIONSBOX ZUM KOLLISIONSMANAGER HINZUGEFÜGT
				if((tiles[i][j].isNearPlayer(xPlayerSpawn, yPlayerSpawn)) && (tiles[i][j].isSolid)) {
					cm.addBox(tiles[i][j].getCollisionBox());
				}
			}
		}
	}

	//METHODE ZUM ÜBERPRÜFEN OB EIN CHUNK IN DER NÄHE DES SPIELERS IST
	//UND DEMNACH GERENDERT WERDEN SOLLTE (UM SPIEL-PERFORMANCE ZU STEIGERN)
	public boolean isNearPlayer(double xSpawn, double ySpawn) {
		if((((playerPosition[1]+ySpawn)/16/gameScale <= (yChunkCoord+2)*16) && ((playerPosition[1]+ySpawn)/16/gameScale >= (yChunkCoord-1)*16)) && (((playerPosition[0]+xSpawn)/16/gameScale <= (xChunkCoord+2)*16) && ((playerPosition[0]+xSpawn)/16/gameScale >= (xChunkCoord-1)*16))) {
			return true;
		}else {
			return false;
		}
		
	}
	
	//METHODE UM EINE KACHEL INNERHALB DES CHUNKS MIT DEN CHUNK-KOORDINATEN ZU KRIEGEN
	public Tile getTile(int xInnerChunk, int yInnerChunk) {
		if(tiles[xInnerChunk][yInnerChunk] == null) {
			return null;
		}else {
			if(xInnerChunk >= 0 && xInnerChunk <= 15 && yInnerChunk >= 0 && yInnerChunk <= 15) {
				return tiles[xInnerChunk][yInnerChunk];
			}else {
				return tiles[1][1];
			}
		}
	}
	
	//METHODE UM DEN TYP EINER KACHEL INNERHALB DES CHUNKS MIT DEN CHUNK-KOORDINATEN, DEM NEUEN TILE-TYP
	//UND DER WELT-SCHICHT ZU ÄNDERN UND BEI BEDARF EINE NEUE KACHEL ZU ERSTELLEN
	public void replaceTile(int x, int y, String type, int layer) {
		if(x >= 0 && x <= 15 && y >= 0 && y <= 15) {
			if(tiles[x][y] == null) {
				//NEUE KACHEL MACHEN WENN KEINE DA IST
				tiles[x][y] = new Tile(x+(xChunkCoord*16), y+(yChunkCoord*16), gameScale, playerPosition, type, layer);
			}else {
				//WENN DIE KACHEL DA EXISTIERT WIRD NUR IHR TYP GEÄNDERT
				tiles[x][y].changeType(type);
			}
		}
	}
	
	//METHODE UM DEN TYP EINES OBJEKTS INNERHALB DES CHUNKS MIT DEN CHUNK-KOORDINATEN UND
	//DEM NEUEN TILE-TYP ZU ÄNDERN UND BEI BEDARF EIN NEUES OBJEKT ZU ERSTELLEN
	public void replaceObject(int x, int y, String type) {
		if(x >= 0 && x <= 15 && y >= 0 && y <= 15) {
			if(objects[x][y] == null) {
				//NEUE KACHEL MACHEN WENN KEINE DA IST
				objects[x][y] = new Object(x+(xChunkCoord*16), y+(yChunkCoord*16), gameScale, playerPosition, type, 3, player, world);
			}else {
				//WENN DIE KACHEL DA EXISTIERT WIRD NUR IHR TYP GEÄNDERT
				objects[x][y].changeType(type);
			}
		}
	}
	
	//SELBE METHODE WIE DARÜBER MIT DEM UNTERSCHIED, DASS ZUSÄTZLICH EIN ITEM ANGEGEBEN WIRD
	//z.B: WENN EIN OBJEKT ZUR KISTE ODER ZUM TOPF GEMACHT WIRD, KANN DIESE DAMIT EIN ITEM ALS INHALT KRIEGEN
	public void replaceObject(int x, int y, String type, String item) {
		if(x >= 0 && x <= 15 && y >= 0 && y <= 15) {
			if(objects[x][y] == null) {
				//NEUE KACHEL MACHEN WENN KEINE DA IST
				objects[x][y] = new Object(x+(xChunkCoord*16), y+(yChunkCoord*16), gameScale, playerPosition, type, 3, player, world, item);
			}else {
				//WENN DIE KACHEL DA EXISTIERT WIRD NUR IHR TYP GEÄNDERT
				objects[x][y].changeType(type, item);
			}
		}
	}
	
	//DIESE METHODE FÜGT ALLE KACHELN UND OBJEKTE DIESES CHUNKS ZUR WARTELISTE DES RENDERMANAGERS HINZU
	public void queueTiles(RenderManager rm) {
		for(int i = 0; i <= 15; i++) {
			for(int j = 0; j <= 15; j++) {
				//if(i = )
				rm.dynamicTiles.add(tiles[i][j]);
				rm.dynamicTiles.add(objects[i][j]);
				
			}
		}
	}
}
