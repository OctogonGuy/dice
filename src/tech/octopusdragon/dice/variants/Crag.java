package tech.octopusdragon.dice.variants;

import tech.octopusdragon.dice.Category;
import tech.octopusdragon.dice.DiceGame;

public class Crag extends DiceGame {

	public Crag(int numPlayers) {
		super(numPlayers);
	}
	
	@Override
	public String name() {
		return "Crag";
	}
	
	@Override
	public String description() {
		return "Crag is a dice game in which players score points by rolling "
				+ "three dice to make certain combinations. The dice may be "
				+ "rolled up to three times in one turn to try to make these "
				+ "combinations. After each round, the player chooses which "
				+ "scoring category is to be used for that round. Once a "
				+ "category has been used, it cannot be used again that game. "
				+ "The winner is the player who scores the most points after "
				+ "all categories have been used.";
	}

	@Override
	public int numDice() {
		return 3;
	}

	@Override
	public int numRolls() {
		return 3;
	}

	@Override
	public Category[] categories() {
		return new Category[] {
				Category.CRAG,
				Category.THIRTEEN,
				Category.THREE_OF_A_KIND,
				Category.LOW_STRAIGHT,
				Category.HIGH_STRAIGHT,
				Category.ODD_STRAIGHT,
				Category.EVEN_STRAIGHT,
				Category.SIXES,
				Category.FIVES,
				Category.FOURS,
				Category.THREES,
				Category.TWOS,
				Category.ONES
		};
	}

}
