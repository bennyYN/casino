package de.ben.playground.escape_the_althen;

import de.ben.MainGUI;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Inventory {

	static ArrayList<String> items = new ArrayList<String>();
	Player player;
	HUD h;
	int xInv, selected;
	static Image unselectedBox = new ImageIcon("src/main/resources/img/playground/escapethealthen/graphics/gui/inventory/unselected_box.png").getImage();
	static Image selectedBox = new ImageIcon("src/main/resources/img/playground/escapethealthen/graphics/gui/inventory/selected_box.png").getImage();
	public Inventory(Player player, HUD hud) {
		this.player = player;
		h = hud;
		xInv =  h.wMiddle-355;
		for(int i = 0; i <= 7; i++) {
			items.add("void");
		}
	}

    public static void reset() {
		items.clear();
    }

    public void render(Graphics g) {
		//SLOTS
			for(int i = 0; i <= 7; i++) {
				g.drawImage(unselectedBox, xInv+(90*i), h.wHeight-160, 80, 80, null);
			}
			//SELECTED SLOT
			g.drawImage(selectedBox, xInv+(90*selected), h.wHeight-160, 80, 80, null);
			
		//ITEM ICONS
			for(int i = 0; i <= 7; i++) {
				try{
					g.drawImage(new ImageIcon("src/main/resources/img/playground/escapethealthen/graphics/items/"+items.get(i)+".png").getImage(), xInv+(90*i)+10, h.wHeight-150, 60, 60, null);
				}catch(Exception e){}
			}
	}
	
	public void moveSelection(int direction) {
		selected = selected - direction;
		if(selected < 0) {
			selected = 7;
		}else if(selected > 7) {
			selected = 0;
		}
	}
	
	public boolean isFull() {
		int temp = 0;
		boolean tempReturn = true;
		for(int i = 0; i <= 7; i++) {
			if(items.get(i).equals("void")) {
				tempReturn = false;
			}else {
				temp++;
			}
		}
		if(temp >= 8) {
			return true;
		}else {
			return tempReturn;
		}
		
	}  
	
	public String getSelectedItemType() {
		
		try {
			return items.get(selected);
		} catch (Exception e) {
			return "---";
		}
	}
	
	public void triggerItemInteraction() {
		switch(getSelectedItemType()) {
		case "immortality_potion": player.immortal = true; items.set(selected, "void"); break;
		case "health_potion": player.heal(); items.set(selected, "void"); break;
		case "speed_potion": player.speedCounter = 1250; items.set(selected, "void"); break;
		}
	}
	
	public void addItem(String pickedUpItemType) {
		int emptySlotIndex = 8;
		for(int i = 7; i >= 0; i--) {
			if(items.get(i).equals("void")) {
				emptySlotIndex = i;
			}
		}
		items.set(emptySlotIndex, pickedUpItemType);
		MainGUI.playSound("view2");
	}
	
	public void throwItem(GamePanel p) {
		if(!items.get(selected).equals("void")) {
			Item thrownItem = new Item(p.world, (int)((player.position[0]/16/player.scale)+12), (int)((player.position[1]/16/player.scale)+9), items.get(selected));
			MainGUI.playSound("view1");
			items.set(selected, "void");
		}
	}
	
	public void removeSelectedItem() {
		if(!items.get(selected).equals("void")) {
			items.set(selected, "void");
		}
	}
}
