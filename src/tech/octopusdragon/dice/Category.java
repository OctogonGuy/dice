package tech.octopusdragon.dice;

/**
 * A scoring category which a player can use to get points in a dice game.
 * @author Alex Gill
 *
 */
public enum Category {
	
	ONES(	"Ones",
			"Any combination",
			"The sum of dice with the 1 face"),
	
	TWOS(	"Twos",
			"Any combination",
			"The sum of dice with the 2 face"),
	
	THREES(	"Threes",
			"Any combination",
			"The sum of dice with the 3 face"),
	
	FOURS(	"Fours",
			"Any combination",
			"The sum of dice with the 4 face"),
	
	FIVES(	"Fives",
			"Any combination",
			"The sum of dice with the 5 face"),
	
	SIXES(	"Sixes",
			"Any combination",
			"The sum of dice with the 6 face"),
	
	FULL_HOUSE(	"Full House",
				"Three of one same face and two of another",
				"25"),
	
	FOUR_OF_A_KIND(	"Four-Of-A-Kind",
					"At least four dice showing the same face",
					"Sum of those four dice"),
	
	LITTLE_STRAIGHT("Little Straight",
					"1-2-3-4-5",
					"30"),
	
	BIG_STRAIGHT(	"Big Straight",
					"2-3-4-5-6",
					"30"),
	
	CHOICE(	"Choice",
			"Any combination",
			"Sum of all dice"),
	
	YACHT(	"Yacht",
			"All five dice showing the same face",
			"50"),

	
	CRAG(	"Crag",
			"Any combination containing a pair and totalling 13",
			"50"),
	
	THIRTEEN(	"Thirteen",
				"Any combination totalling 13",
				"26"),
	
	THREE_OF_A_KIND("Three-Of-A-Kind",
					"Three dice showing the same face",
					"25"),
	
	LOW_STRAIGHT(	"Low Straight",
					"1-2-3",
					"20"),
	
	HIGH_STRAIGHT(	"High Straight",
					"4-5-6",
					"20"),
	
	ODD_STRAIGHT(	"Odd Straight",
					"1-3-5",
					"20"),
	
	EVEN_STRAIGHT(	"Even Straight",
					"2-4-6",
					"20");
	
	
	
	// --- Attributes ---
	private final String name;			// The name of the category
	private final String description;	// A brief description
	private final String scoring;		// How the dice are scored
	
	
	
	/**
	 * Instantiates the category.
	 * @param name the name of the category
	 * @param description a brief description
	 * @param score how the dice are scored
	 */
	private Category(String name, String description, String score) {
		this.name = name;
		this.description = description;
		this.scoring = score;
	}
	
	
	
	/**
	 * @return the name of the category
	 */
	public String getName() {
		return name;
	}



	/**
	 * @return a brief description
	 */
	public String getDescription() {
		return description;
	}



	/**
	 * @return how the dice are scored
	 */
	public String getScoring() {
		return scoring;
	}
	
	
	
	/**
	 * Calculates and returns the number of points a player would get by playing
	 * on this category.
	 * @param dice The player's dice
	 * @return The number of points
	 */
	public int score(Die[] dice) {
		int score = 0; 
		
		switch (this) {
		
			case ONES:
				score = numDiceWithValue(dice, 1) * 1;
				break;
				
				
			case TWOS:
				score = numDiceWithValue(dice, 2) * 2;
				break;
				
				
			case THREES:
				score = numDiceWithValue(dice, 3) * 3;
				break;
				
				
			case FOURS:
				score = numDiceWithValue(dice, 4) * 4;
				break;
				
				
			case FIVES:
				score = numDiceWithValue(dice, 5) * 5;
				break;
				
				
			case SIXES:
				score = numDiceWithValue(dice, 6) * 6;
				break;
				
				
			case FULL_HOUSE:
				boolean fullHouse = false;
				for (int i = 1; i <= 6; i++) {
					for (int j = 1; j <= 6; j++) {
						if (i != j &&
							numDiceWithValue(dice, i) == 2 &&
							numDiceWithValue(dice, j) == 3) {
							fullHouse = true;
							break;
						}
					}
				}
				
				if (fullHouse)
					score = 25;
				break;
				
				
			case FOUR_OF_A_KIND:
				if (numDiceWithValue(dice, 1) >= 4)
					score = 1 * 4;
				
				else if (numDiceWithValue(dice, 2) >= 4)
					score = 2 * 4;
				
				else if (numDiceWithValue(dice, 3) >= 4)
					score = 3 * 4;
				
				else if (numDiceWithValue(dice, 4) >= 4)
					score = 4 * 4;
				
				else if (numDiceWithValue(dice, 5) >= 4)
					score = 5 * 4;
				
				else if (numDiceWithValue(dice, 6) >= 4)
					score = 6 * 4;
				break;
				
				
			case LITTLE_STRAIGHT:
				if (numDiceWithValue(dice, 1) >= 1 &&
					numDiceWithValue(dice, 2) >= 1 &&
					numDiceWithValue(dice, 3) >= 1 &&
					numDiceWithValue(dice, 4) >= 1 &&
					numDiceWithValue(dice, 5) >= 1)
						score = 30;
				break;
				
				
			case BIG_STRAIGHT:
				if (numDiceWithValue(dice, 2) >= 1 &&
					numDiceWithValue(dice, 3) >= 1 &&
					numDiceWithValue(dice, 4) >= 1 &&
					numDiceWithValue(dice, 5) >= 1 &&
					numDiceWithValue(dice, 6) >= 1)
						score = 30;
				break;
				
				
			case CHOICE:
				for (Die die: dice)
					score += die.getValue();
				break;
				
				
			case YACHT:
				if (numDiceWithValue(dice, 1) == 5 ||
					numDiceWithValue(dice, 2) == 5 ||
					numDiceWithValue(dice, 3) == 5 ||
					numDiceWithValue(dice, 4) == 5 ||
					numDiceWithValue(dice, 5) == 5 ||
					numDiceWithValue(dice, 6) == 5)
						score = 50;
				break;
				
				
			case CRAG:
				if ((numDiceWithValue(dice, 1) >= 2 ||
					 numDiceWithValue(dice, 2) >= 2 ||
					 numDiceWithValue(dice, 3) >= 2 ||
					 numDiceWithValue(dice, 4) >= 2 ||
					 numDiceWithValue(dice, 5) >= 2 ||
					 numDiceWithValue(dice, 6) >= 2) &&
					diceTotal(dice) == 13)
					score = 50;
				break;
				
				
			case THIRTEEN:
				if (diceTotal(dice) == 13)
					score = 26;
				break;
				
				
			case THREE_OF_A_KIND:
				if (numDiceWithValue(dice, 1) == 3 ||
					numDiceWithValue(dice, 2) == 3 ||
					numDiceWithValue(dice, 3) == 3 ||
					numDiceWithValue(dice, 4) == 3 ||
					numDiceWithValue(dice, 5) == 3 ||
					numDiceWithValue(dice, 6) == 3)
					score = 25;
				break;
				
				
			case LOW_STRAIGHT:
				if (numDiceWithValue(dice, 1) >= 1 &&
					numDiceWithValue(dice, 2) >= 1 &&
					numDiceWithValue(dice, 3) >= 1)
					score = 20;
				break;
				
				
			case HIGH_STRAIGHT:
				if (numDiceWithValue(dice, 4) >= 1 &&
					numDiceWithValue(dice, 5) >= 1 &&
					numDiceWithValue(dice, 6) >= 1)
					score = 20;
				break;
				
				
			case ODD_STRAIGHT:
				if (numDiceWithValue(dice, 1) >= 1 &&
					numDiceWithValue(dice, 3) >= 1 &&
					numDiceWithValue(dice, 5) >= 1)
					score = 20;
				break;
				
				
			case EVEN_STRAIGHT:
				if (numDiceWithValue(dice, 2) >= 1 &&
					numDiceWithValue(dice, 4) >= 1 &&
					numDiceWithValue(dice, 6) >= 1)
					score = 20;
				break;
		}
		
		
		return score;
	}
	
	
	
	/**
	 * Returns the number of dice with the given value right-side up.
	 * @param dice The dice
	 * @param value The value (1 - 6)
	 * @return The number of dice with the given value
	 */
	private int numDiceWithValue(Die[] dice, int value) {
		int numDice = 0;
		
		for (Die die: dice)
			if (die.getValue() == value)
				numDice++;
		
		return numDice;
	}
	
	
	
	/**
	 * Returns the total of the given dice values.
	 * @param dice The dice
	 * @return The total of the dice values
	 */
	private int diceTotal(Die[] dice) {
		int total = 0;
		
		for (Die die: dice)
			total += die.getValue();
		
		return total;
	}
}
