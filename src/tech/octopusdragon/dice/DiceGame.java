package tech.octopusdragon.dice;

public abstract class DiceGame {
	
	/**
	 * Returns the name of the variant as a string.
	 * @return The name of the variant as a string
	 */
	public abstract String name();
	
	/**
	 * Returns a description of the variant as a string.
	 * @return A description of the variant as a string
	 */
	public abstract String description();
	
	/**
	 * Returns the number of dice.
	 * @return The number of dice
	 */
	public abstract int numDice();
	
	/**
	 * Returns the maximum number of rolls a player can make per turn.
	 * @return The number of rolls
	 */
	public abstract int numRolls();
	
	/**
	 * Returns the scoring categories.
	 * @return The scoring categories
	 */
	public abstract Category[] categories();
	
	private Die[] dice;			// The dice
	private Player[] players;	// The players
	private int curPlayerIndex;	// The current player
	private boolean curPlayerHasGone;	// Whether the current player has gone
	private int rollsLeft;		// The number of rolls left for the player
	
	
	
	/**
	 * Instantiates a new game of Yacht.
	 * @param numPlayers The number of players
	 */
	public DiceGame(int numPlayers) {
		dice = new Die[numDice()];
		for (int i = 0; i < numDice(); i++)
			dice[i] = new Die();
		
		players = new Player[numPlayers];
		for (int i = 0; i < numPlayers; i++)
			players[i] = new Player("Player " + (i + 1), categories());
		
		// Start the first player's turn
		curPlayerIndex = -1;
		nextPlayer();
	}
	
	
	
	/**
	 * Returns the dice.
	 * @return The dice
	 */
	public Die[] getDice() {
		return dice;
	}
	
	
	
	/**
	 * Returns the number of players.
	 * @return The number of players
	 */
	public int numPlayers() {
		return players.length;
	}
	
	
	
	/**
	 * Returns a particular player.
	 * @param i The index of the player
	 * @return The player.
	 */
	public Player getPlayer(int i) {
		return players[i];
	}
	
	
	
	/**
	 * Returns the current player.
	 * @return The current player
	 */
	public Player curPlayer() {
		return players[curPlayerIndex];
	}
	
	
	
	/**
	 * Advances to the next player.
	 */
	public void nextPlayer() {
		
		// Increment the player index
		curPlayerIndex++;
		if (curPlayerIndex >= players.length)
			curPlayerIndex = 0;
		
		// The current player has not gone
		curPlayerHasGone = false;
		
		// The current player has a number of rolls
		rollsLeft = numRolls();
	}
	
	
	
	/**
	 * Rolls all of the dice.
	 */
	public void roll() {
		int[] dieIndexes = new int[numDice()];
		for (int i = 0; i < numDice(); i++)
			dieIndexes[i] = i;
		roll(dieIndexes);
	}
	
	
	
	/**
	 * Rolls the specified dice.
	 * @param dieIndexes The indexes of the dice to roll.
	 */
	public void roll(int... dieIndexes) {
		
		// Do nothing if the player has no rolls left
		if (rollsLeft == 0)
			return;
		
		// Roll
		for (int i = 0; i < dieIndexes.length; i++)
			dice[dieIndexes[i]].roll();
		rollsLeft--;
	}
	
	
	
	/**
	 * Returns whether the current player has already submitted.
	 * @return Whether the current player has already submitted.
	 */
	public boolean curPlayerHasGone() {
		return curPlayerHasGone;
	}
	
	
	
	/**
	 * Returns whether the game is over.
	 * @return Whether the game is over.
	 */
	public boolean isOver() {
		for (int i = 0; i < players.length; i++)
			for (Category category: categories())
				if (!players[i].hasUsedCategory(category))
					return false;
		return true;
	}
	
	
	
	/**
	 * Returns the number of rolls the current player has left for the round.
	 * @return The number of rolls the current player has left
	 */
	public int rollsLeft() {
		return rollsLeft;
	}
	
	
	
	/**
	 * Selects a category for the current player to submit a score.
	 * @param category The scoring category
	 */
	public void submit(Category category) {
		
		// Award the player with the number of points earned.
		curPlayer().submit(category, category.score(dice));
		
		// The current player has gone
		curPlayerHasGone = true;
	}
}
