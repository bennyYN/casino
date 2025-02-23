package de.ben.playground.escape_the_althen;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class GameFrame extends JFrame implements KeyListener, ActionListener, MouseWheelListener, MouseListener{

	//ATTRIBUTE
	GamePanel p;
	MenuPanel m;
	MenuFrame mf;
	Item item;
	Timer timer;
	public int selectedLevel;
	CollisionManager cm = new CollisionManager();
	public double delayer1 = 0, delayer2 = 0, delayer3 = 0, delayer4 = 0;
	ImageIcon frameIcon = new ImageIcon("img/playground/althenpong/althenos.png");
	
	//KONSTRUKTOR
	public GameFrame(int selectedLevel, MenuFrame mf) {
		this.mf = mf;
		this.setLayout(null);
		this.selectedLevel = selectedLevel;
		this.setTitle("Escape The Althen!");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		p = new GamePanel(this, 1);
		this.add(p);
		this.setIconImage(frameIcon.getImage());
		this.setSize(1920, 1080);
		this.setVisible(true);
		this.addKeyListener(this);
		this.addMouseWheelListener(this);
		this.addMouseListener(this);
		//TIMER-THREAD DER IDEALERWEISE ALLE 5MS EIN ACTIONEVENT AUSLÖST
		timer = new Timer(5, (ActionListener) this);
		timer.start();
	}

	//UNGENUTZTE ABER ZU IMPLEMENTIERENDE METHODE VOM KEYLISTENER INTERFACE
	@Override
	public void keyTyped(KeyEvent e) {}
	
	//METHODE VOM KEYLISTENER INTERFACE, WELCHE BEIM DRUCK EINER TASTE AUSGELÖST WIRD
	@Override
	public void keyPressed(KeyEvent e) {
		//TOGGLE DEN PAUSE ZUSTAND BEIM DRUCK VON ESC
		if(e.getKeyCode() == 27) {
			p.paused = !p.paused;
		}
		if(p.player.isAlive && !p.paused) {
		//STRG (SPRINT)
			if(e.getKeyCode() == 17) {
				p.player.isSprinting = true;
			}
		//HOCH LAUFEN
			if(e.getKeyCode() == 87 || e.getKeyCode() == 38) {
				p.player.velocity[1] = -p.player.walkingSpeed*p.gameScale;
				p.player.isWalking = true;
			}
		//LINKS LAUFEN
			if(e.getKeyCode() == 65 || e.getKeyCode() == 37) {
				p.player.velocity[0] = -p.player.walkingSpeed*p.gameScale;
				p.player.isWalking = true;
			}
		//RUNTER LAUFEN
			if(e.getKeyCode() == 83 || e.getKeyCode() == 40) {
				p.player.velocity[1] = p.player.walkingSpeed*p.gameScale;
				p.player.isWalking = true;
			}
		//RECHTS LAUFEN
			if(e.getKeyCode() == 68 || e.getKeyCode() == 39) {
				p.player.velocity[0] = p.player.walkingSpeed*p.gameScale;
				p.player.isWalking = true;
			}
		//AUSGEWÄHLTES ITEM AUS DEM INVENTAR UNTER EINEN SCHMEIßEN
			if(e.getKeyCode() == 81) {
				p.inv.throwItem(p);
			}
		//TOGGLE DEBUG MODUS
			if(e.getKeyCode() == 114) {
				p.debugmode = !p.debugmode;
			}
		//TOGGLE EDITOR MODE
		//TODO -> NICHT FERTIG IMPLEMENTIERT, DAHER AUSKOMMENTIERT
			if(e.getKeyCode() == 112) {
				p.debugmode = false;
			}
		}
		
		//IM DEBUG MODUS WERTE VON SPIELER GESUNDHEIT, EFFEKTE & LEVEL SIMULIEREN
			if(p.debugmode && !p.paused) {
				if(e.getKeyCode()==521) {
					p.player.health++;
				}
				if(e.getKeyCode()==45) {
					p.player.health--;
				}
				if(e.getKeyCode()==109) {
					p.player.xp--;
				}
				if(e.getKeyCode()==107) {
					p.player.xp++;
				}
				if(e.getKeyCode() == 66) {
					p.player.burning = !p.player.burning;
				}
				if(e.getKeyCode() == 73) {
					p.player.immortal = !p.player.immortal;
				}
			}
	}

	//METHODE DES KEYLISTENER INTERFACES WELCHE BEIM LOSLASSEN EINER TASTE AUSGEFÜRT WIRD
	@Override
	public void keyReleased(KeyEvent e) {
		//UP
			if(e.getKeyCode() == 87 || e.getKeyCode() == 38) {
				p.player.velocity[1] = 0;
			}
		//LEFT
			if(e.getKeyCode() == 65 || e.getKeyCode() == 37) {
				p.player.velocity[0] = 0;
			}
		//DOWN
			if(e.getKeyCode() == 83 || e.getKeyCode() == 40) {
				p.player.velocity[1] = 0;
			}
		//RIGHT
			if(e.getKeyCode() == 68 || e.getKeyCode() == 39) {
				p.player.velocity[0] = 0;
			}
		//STRG (SPRINT)
			if(e.getKeyCode() == 17) {
				p.player.isSprinting = false;
			}
	}

	//METHODE DES ACTIONLISTENERS, WELCHE ZUSÄTZLICH VOM TIMER AUSGELÖST WIRD
	@Override
	public void actionPerformed(ActionEvent e) {

		//WENN DAS SPIEL NICHT PAUSIERT IST:
		if(!p.paused) {
		
			//UPDATE PLAYER WALKING STATE
			if((p.player.velocity[0] == 0) && (p.player.velocity[1] == 0)) {
					p.player.isWalking = false;
			}
			
			//AKTUALISIERUNG DES ANIMATIONSWERT DES SPIELERS & DES ALTHENS FÜR LAUFANIMATION
			//LINKS/RECHTS:
			if(delayer1 >= 8 || (p.player.isSprinting && delayer1 >= 3 && p.player.isWalking)) {
				if(p.player.textureVariation1 == 8) {
					p.player.textureVariation1 = 1;
				}else {
					p.player.textureVariation1++;
				}
				delayer1 = 0;
			}else {
				delayer1++;
			}
			//HOCH/RUNTER:
			if(delayer2 >= 8 || (p.player.isSprinting && delayer2 >= 3 && p.player.isWalking)) {
				if(p.player.textureVariation2 == 10) {
					p.player.textureVariation2 = 1;
				}else {
					p.player.textureVariation2++;
				}
				delayer2 = 0;
			}else {
				delayer2++;
			}
		//SPIELER BRENN ANIMATION
			if(delayer3 >= 8 || (p.player.isSprinting && delayer3 >= 3 && p.player.isWalking)) {
				if(p.player.textureVariation3 == 6) {
					p.player.textureVariation3 = 1;
				}else {
					p.player.textureVariation3++;
				}
				delayer3 = 0;
			}else {
				delayer3++;
			}

			// New texture variation
			if (delayer4 >= 8 || (p.player.isSprinting && delayer4 >= 3 && p.player.isWalking)) {
				if (p.player.textureVariation4 == 8) {
					p.player.textureVariation4 = 1;
				} else {
					p.player.textureVariation4++;
				}
				delayer4 = 0;
			} else {
				delayer4++;
			}
	//WENN DER SPIELER AM LEBEN IST:
		if(p.player.isAlive) {
			try {
				//ÄNDERE DIE POSITION DES SPIELERS BASIEREND AUF DEM FESTGELEGTEN GESCHWINDIGKEITSWERT
					//WENN DER SPIELER SPRINTET:
					if(p.player.isSprinting) {
						if(!(cm.wouldCollide(p.player, p.player.velocity[0]*p.player.sprintFactor, 0))) {
							p.player.position[0] = p.player.position[0] + (p.player.velocity[0]*p.player.sprintFactor);
						}
						if(!(cm.wouldCollide(p.player, 0, p.player.velocity[1]*p.player.sprintFactor))) {
							p.player.position[1] = p.player.position[1] + (p.player.velocity[1]*p.player.sprintFactor);
						}
					//WENN DER SPIELER NICHT SPRINTET:
					}else {
						if(!(cm.wouldCollide(p.player, p.player.velocity[0], 0))) {
							p.player.position[0] = p.player.position[0] + p.player.velocity[0];
						}
						if(!(cm.wouldCollide(p.player, 0, p.player.velocity[1]))) {
							p.player.position[1] = p.player.position[1] + p.player.velocity[1];
						}
					}
				}catch(Exception excp) {
					System.out.println(excp.toString());
				}
			} else {
				//WENN DER SPIELER NICHT AM LEBEN IST WIRD DER TIMER GESTOPPT
				repaint();
				timer.stop();
			}
		
				//JEDE KREATUR UND DEN VEKTOR DER KREATUR "EINWEISER" AKTUALISIEREN
				p.althen.los.update();
				p.player.update(p);
				//AKTUALISIERUNG JEDES ITEMS
				if(p.inv != null) {
					Item.updateAll(p.inv);
				}
				

				
		}
		p.repaint();
	}

	//ZU IMPLEMENTIERENDE METHODE VOM MOUSEWHEELLISTENER INTERFACE
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		//WENN DAS MAUSRAD BEWEGT WURDE, WIRD AUF BASIS DER ÜBERGEBENEN BEWEGUNG
		//DIE AUSWAHL EINES SLOTS IM INVENTAR BERECHNET
		p.inv.moveSelection(e.getWheelRotation());	
	}

	//ZU IMPLEMENTIERENDE METHODE VOM MOUSELISTENER INTERFACE
	@Override
	public void mouseClicked(MouseEvent e) {
		//BEIM LOSLASSEN VOM LINKSKLICK SOLL BEIM SPIELER EIN "ANGRIFFS-EVENT" SPÄTESTENS BEENDET WERDEN
		if(e.getButton() == 1) {
			p.player.hitEvent = false;
		}
		//BEIM LOSLASSEN VOM RECHTSKLICK SOLL BEIM SPIELER EIN "INTERAKTIONS-EVENT" SPÄTESTENS BEENDET WERDEN
		if(e.getButton() == 3) {
			p.player.interactionEvent = false;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		//BEIM RECHTSKLICK SOLL BEIM SPIELER EIN "INTERAKTIONS-EVENT" AUSGELÖST WERDEN
				if(e.getButton() == 1) {
					p.player.hitEvent = true;
				}
		//BEIM LINKSKLICK SOLL BEIM SPIELER EIN "ANGRIFFS-EVENT" AUSGELÖST WERDEN
		if(e.getButton() == 3) {
			p.inv.triggerItemInteraction();
			p.player.interactionEvent = true;
		}
	}

	//WEITERE UNGENUTZTE ABER ZU IMPLEMENTIERENDE METHODEN DES MOUSELISTENERS
	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}
