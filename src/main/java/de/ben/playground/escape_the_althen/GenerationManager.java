package de.ben.playground.escape_the_althen;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GenerationManager {

	//ATTRIBUTE
	BufferedReader reader;
	World world;

	//KONSTRUKTOR
	public GenerationManager(int level, World world) {
		this.world = world;
		try {
			reader = new BufferedReader(new FileReader("img/playground/escapethealthen/levels/level_"+level+".lvl"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Level Datei nicht gefunden!");
		}
	}
	
	/*METHODE UM DIE ZUM ÜBERGEBENEN LEVEL ZUGEHÖRIGE .LVL-DATEI ZU LESEN UND DADURCH
	GEGEBENE BEFEHLE ZUR WELTGENERIERUNG UMSETZT
	------
	SYNTAX:
	-JEDE ZEILE DARF HÖCHSTENS EINE ANWEISUNG HABEN
	-ZEILEN DÜRFEN AUF KEINER ART EINGERÜCKT WERDEN
	-ZWISCHEN JEDEM TEIL EINER ANWEISUNG MUSS EIN LEERZEICHEN SEIN
	-JEDES .LVL-DOKUMENT MUSS ZULETZT MIT EINEM "exit" BEENDET WERDEN, UM AUS DER SCHLEIFE ZU BRECHEN
	BEISPIEL:
		# WAS EIN TOLLER KOMMENTAR
		# EINE EINZELNE KACHEL SETZEN: setT (X) (Y) (TYPE) (SCHICHT)
		setT 0 0 grass 1
		# MEHRERE KACHELN VON EINEM PUNKT ZU EINEM ZWEITEN PUNKT PLATZIEREN: (X1) (Y1) (X2) (Y2) (TYPE) (SCHICHT)
		secT 1 1 3 3 stone_tiles 1
		# EIN OBJEKT SETZEN: setO (X) (Y) (TYPE)
		setO 4 4 pot1
		# MIT ITEM BEFÜLLTES OBJEKT SETZEN: setOi (X) (Y) (TYPE) (ITEM-TYPE)
		setOi 2 2 pot1 key
		# GENERATIONS PROGRAMM BEENDEN:
		exit
	*/
	public void generate() {
		try {
			String tempLine;
			boolean exited = false;
			while(((tempLine = reader.readLine()) != null) || !exited) {
				 tempLine.trim();
				 String[] instructions= tempLine.split(" ");
				
				 switch(instructions[0]) {
				 case "exit": exited = true; break;
				 case "#", "": break;
				 case "setT": 
					 setTile(Integer.valueOf(instructions[1]), Integer.valueOf(instructions[2]), instructions[3], Integer.valueOf(instructions[4]));
					 break;
				 case "secT":
					 setSectionOfTiles(Integer.valueOf(instructions[1]), Integer.valueOf(instructions[2]), Integer.valueOf(instructions[3]), Integer.valueOf(instructions[4]), instructions[5], Integer.valueOf(instructions[6]));
					 break;
				 case "setO":
					 setObject(Integer.valueOf(instructions[1]), Integer.valueOf(instructions[2]), instructions[3]);
				 	 break;
				 case "setOi":
					 setObjectWithItem(Integer.valueOf(instructions[1]), Integer.valueOf(instructions[2]), instructions[3], instructions[4]);
				 	 break;
				 case "setI":
					 new Item(world, Integer.valueOf(instructions[1]), Integer.valueOf(instructions[2]), instructions[3]);
					 break;
				 default: System.out.println("LEVEL: Unknown Generation-Instruction -> " + instructions[0]);
				 	 break;
				 }
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Level Datei nicht gefunden!");
		}
	}
	
	//METHODEN, WELCHE DURCH DIE ANWEISUNGEN DER .LVL-DATEI AUSGEFÜHRT WERDEN:
	
	private void setSectionOfTiles(int a1, int a2, int b1, int b2, String wasDennÜberhaupt, int layer) {
		try {
		for(int i = a1; i <= b1; i++) {
			for(int j = a2; j <= b2; j++) {
				world.setTile(i, j, layer, wasDennÜberhaupt);
			}
		}
		}catch(Exception e) {}
	}
	private void setTile(int x, int y, String wasDennÜberhaupt, int layer) {
		world.setTile(x, y, layer, wasDennÜberhaupt);
	}
	private void setObject(int x, int y, String wasDennÜberhaupt) {
		world.setObject(x, y, wasDennÜberhaupt);
		System.out.println(wasDennÜberhaupt);
	}
	private void setObjectWithItem(int x, int y, String wasDennÜberhaupt, String item) {
		world.setObjectWithItem(x, y, wasDennÜberhaupt, item);
		System.out.println("qid");
	}
}
