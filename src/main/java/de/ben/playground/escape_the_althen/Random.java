package de.ben.playground.escape_the_althen;

public class Random {
//KLASSE UM EINEN RANDOM DOUBLE WERT ZU KRIEGEN
//WELCHER NICHT STÄNDIG MIT EINEM NEUEN WERT ÜBERSCHRIEBEN WIRD
	double randomDouble;
	
	public Random() {
		randomDouble = Math.random();
	}
	
	public double getDouble() {
		return randomDouble;
	}

	public void shuffle() {
		randomDouble = Math.random();
	}
}
