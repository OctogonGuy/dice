package tech.octopusdragon.dice.variants;

import tech.octopusdragon.dice.Category;
import tech.octopusdragon.dice.DiceGame;

public class Yacht extends DiceGame {

	public Yacht(int numPlayers) {
		super(numPlayers);
	}
	
	@Override
	public String name() {
		return "Yacht";
	}
	
	@Override
	public String description() {
		return "Yacht is a dice game in which players score points by rolling "
				+ "five dice to make certain combinations. The dice may be "
				+ "rolled up to three times in one turn to try to make these "
				+ "combinations. After each round, the player chooses which "
				+ "scoring category is to be used for that round. Once a "
				+ "category has been used, it cannot be used again that game. "
				+ "The winner is the player who scores the most points after "
				+ "all categories have been used.";
	}

	@Override
	public int numDice() {
		return 5;
	}

	@Override
	public int numRolls() {
		return 3;
	}

	@Override
	public Category[] categories() {
		return new Category[] {
				Category.ONES,
				Category.TWOS,
				Category.THREES,
				Category.FOURS,
				Category.FIVES,
				Category.SIXES,
				Category.FULL_HOUSE,
				Category.FOUR_OF_A_KIND,
				Category.LITTLE_STRAIGHT,
				Category.BIG_STRAIGHT,
				Category.CHOICE,
				Category.YACHT
		};
	}

}
