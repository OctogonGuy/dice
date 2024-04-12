package tech.octopusdragon.dice;
import java.util.Random;

/**
 * Represents a standard six-sided die.
 * @author Alex Gill
 *
 */
public class Die {
	
	private final int SIDES = 6;	// The number of sides on the die
	
	private int value;	// The value of the die's face-up side
	
	
	
	/**
	 * Instantiates a new die.
	 */
	public Die() {
		value = 1;
	}
	
	
	
	/**
	 * Returns the value of the die's face-up side.
	 * @return The value of the die's face-up side.
	 */
	public int getValue() {
		return value;
	}
	
	
	
	/**
	 * Rolls the die and randomly selects a side to face up.
	 */
	public void roll() {
		
		// Create a Random object for generating the die's value.
		Random rand = new Random();
		
		// Generate a new value between 1 and 6
		value = rand.nextInt(SIDES) + 1;
	}
}
