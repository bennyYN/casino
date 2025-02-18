package de.ben.playground.escape_the_althen;

import java.awt.*;

public class CollisionBox extends Rectangle{

	//ATTRIBUTE
		public double xOffset, yOffset, scale;
		public double[] position = new double[2];
	
	//KONSTRUKTOR
		public CollisionBox(double x, double y, int widthInTPX, int heightInTPX, double gameScale, double xOffsetInTPX, double yOffsetInTPX) {
			super((int)(x+(xOffsetInTPX*gameScale)), (int)(y+(yOffsetInTPX*gameScale)), (int)(widthInTPX*gameScale), (int)(heightInTPX*gameScale));
			this.xOffset = xOffsetInTPX*gameScale;
			this.yOffset = yOffsetInTPX*gameScale;
			scale = gameScale;
			position[0] = super.x;
			position[1] = super.y;
		}
	
	//METHODE ZUM RENDERN DER KOLLISIONSBOX
		public void render(Graphics g, Color color) {
			g.setColor(color);
			g.drawRect(x, y, width, height);
			g.setColor(Color.BLACK);
		}
	
	//METHODE ZUM VERSCHIEBEN DER BOX
		public void move(double x, double y) {
			this.x = this.x + (int)x;
			this.y = this.y + (int)y;
		}

}
