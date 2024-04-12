package tech.octopusdragon.dice;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a player in a dice game.
 * @author Alex Gill
 *
 */
public class Player {

	// --- Variables
	private String name;	// The name of the player
	private Map<Category, Integer> categoryPoints;	// Points for each category
	private Map<Category, Boolean> usedCategories;	// Used categories
	
	
	
	/**
	 * Instantiates a new player.
	 * @param name The name of the player
	 * @param categories The scoring categories
	 */
	public Player(String name, Category[] categories) {
		this.name = name;
		
		categoryPoints = new HashMap<Category, Integer>();
		usedCategories = new HashMap<Category, Boolean>();
		for (Category category: categories) {
			categoryPoints.put(category, 0);
			usedCategories.put(category, false);
		}
	}
	
	
	
	/**
	 * Instantiates a new player with no name.
	 * @param categories The scoring categories
	 */
	public Player(Category[] categories) {
		this("Player", categories);
	}
	
	
	
	/**
	 * Returns the player's name.
	 * @return The player's name
	 */
	public String getName() {
		return name;
	}
	
	
	
	/**
	 * Sets the player's name.
	 * @param name The player's name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	/**
	 * Submits a score using the given category for the player.
	 * @param category The category
	 * @param points The number of points earned for the category
	 */
	public void submit(Category category, int points) {
		categoryPoints.put(category, points);
		usedCategories.put(category, true);
	}
	
	
	
	/**
	 * Returns whether the player has already used the given category.
	 * @param category The category
	 * @return true if the player has used the category; false otherwise
	 */
	public boolean hasUsedCategory(Category category) {
		return usedCategories.get(category);
	}
	
	
	
	/**
	 * Returns the player's number of points in a given category.
	 * @param category The category
	 * @return The number of points
	 */
	public int getPoints(Category category) {
		return categoryPoints.get(category);
	}
	
	
	
	/**
	 * Returns the player's total score.
	 * @return The player's total score
	 */
	public int getTotalScore() {
		int score = 0;
		for (Category category: usedCategories.keySet()) {
			score += categoryPoints.get(category);
		}
		return score;
	}
	
}
