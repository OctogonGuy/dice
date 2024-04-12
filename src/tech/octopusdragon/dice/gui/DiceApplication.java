package tech.octopusdragon.dice.gui;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import tech.octopusdragon.dice.*;
import tech.octopusdragon.dice.variants.*;

/**
 * Plays the game of Yacht on a Graphical User Interface.
 * @author Alex Gill
 *
 */
public class DiceApplication extends Application {
	
	// Constants
	public final static double INFO_SPACING = 6.0;		// Spacing between info panels
	public final static double SPACING = 22.0;		// Spacing between left and right sides
	public final static double DICE_PADDING = 10.0;	// Padding around dice
	public final static double PADDING = 20.0;	// Padding around the root node
	public final static double CATEGORY_INFO_PANEL_WIDTH = 120.0;
	public final static double CATEGORY_INFO_PANEL_HEIGHT = 100.0;
	public final static double DIE_LENGTH = 70.0;	// The width and height of the die images
	public final static double DELAY = 100.0; 	// Difference in time between each die roll
	
	// Die images
	public final static Map<Integer, Image> dieImages = new HashMap<Integer, Image>();
	public final static String ONE_IMAGE = "diceOne.png";
	public final static String TWO_IMAGE = "diceTwo.png";
	public final static String THREE_IMAGE = "diceThree.png";
	public final static String FOUR_IMAGE = "diceFour.png";
	public final static String FIVE_IMAGE = "diceFive.png";
	public final static String SIX_IMAGE = "diceSix.png";
	
	public final static String BACKGROUND_IMAGE = "background.jpg";
	
	// Sounds
	public final static String DICE_SOUND = "dice.wav";
	public final static String CHEER_SOUND = "cheer.wav";
	MediaPlayer diceSound;
	MediaPlayer cheerSound;
	
	// GUI components
	GridPane root;
	List<ImageView> dieImageViews;	// The die image views
	Label rollsLeftLabel;	// Shows the number of rolls the user has left
	Button rollButton;			// The roll button
	CategoryInfoPanel[] categoryInfoPanels;	// The category info panels
	Label playerLabel;	// Shows the current player
	Label totalScoreLabel;	// Shows total score of the current player
	
	// The game
	DiceGame game;
	
	// Other variables
	Class<?>[] variants;	// List of regional variants
	List<Integer> heldDice;	// Indexes of held dice
	boolean diceDisabled;	// Whether dice can be interacted with
	boolean animationRunning;	// Whether the animation is running
	
	
	
	@Override
	public void init() {
		
		// Initialize static roll animation image array
		try {
			Class.forName(RollAnimation.class.getName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// Create a map of die values and die images
		dieImages.put(1, new Image(getClass().getClassLoader().getResourceAsStream(ONE_IMAGE)));
		dieImages.put(2, new Image(getClass().getClassLoader().getResourceAsStream(TWO_IMAGE)));
		dieImages.put(3, new Image(getClass().getClassLoader().getResourceAsStream(THREE_IMAGE)));
		dieImages.put(4, new Image(getClass().getClassLoader().getResourceAsStream(FOUR_IMAGE)));
		dieImages.put(5, new Image(getClass().getClassLoader().getResourceAsStream(FIVE_IMAGE)));
		dieImages.put(6, new Image(getClass().getClassLoader().getResourceAsStream(SIX_IMAGE)));
		
		// Initialize the sounds
		diceSound = new MediaPlayer(new Media(getClass().getClassLoader().getResource(DICE_SOUND).toExternalForm()));
		diceSound.setOnEndOfMedia(() -> {
			diceSound.stop();
		});
		cheerSound = new MediaPlayer(new Media(getClass().getClassLoader().getResource(CHEER_SOUND).toExternalForm()));
		cheerSound.setOnEndOfMedia(() -> {
			cheerSound.stop();
		});
		
		// Instantiate the list of regional variants
		variants = new Class<?>[] {Yacht.class,
								   Crag.class};
	}

	
	
	@Override
	public void start(Stage primaryStage) {
		
		// Create the root
		root = new GridPane();
		root.setHgap(INFO_SPACING);
		root.setVgap(INFO_SPACING);
		root.setPadding(new Insets(PADDING));
		root.setBackground(new Background(
				new BackgroundImage(new Image(getClass().getClassLoader().getResourceAsStream(BACKGROUND_IMAGE)), null, null, null, null)));
		
		
		// If the user closes the primary stage, show a new game dialog
		primaryStage.setOnCloseRequest(e -> {
			e.consume();
			primaryStage.hide();
			newGameDialog();
			primaryStage.show();
		});
		
		
		// Set the scene
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Unspecified dice game");
		primaryStage.setResizable(false);
		
		// Show the user the new game dialog before constructing the stage.
		newGameDialog();
		
		// Show the stage
		primaryStage.show();
	}

	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	
	public void newGame(Class<?> variant, int numPlayers) {
		
		// Instantiate a new game
		try {
			game = (DiceGame) variant.getDeclaredConstructor(int.class).newInstance(numPlayers);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			System.out.println("Error instantiating checkers class...");
			e.printStackTrace();
			System.exit(1);
		}
		
		
		// Clear the grid pane
		root.getChildren().clear();
		
		
		// Create the rolls left label
		rollsLeftLabel = new Label("Rolls left: " + game.rollsLeft());
		rollsLeftLabel.setFont(new Font("Calibri Light", 20));
		rollsLeftLabel.setBackground(new Background(
			new BackgroundFill(Color.web("#FFFFFF", 0.75), new CornerRadii(10.0), null)));
		rollsLeftLabel.setPadding(new Insets(10.0));
		
		root.add(rollsLeftLabel, 0, 0);
		GridPane.setColumnSpan(rollsLeftLabel, game.numDice());
		GridPane.setHalignment(rollsLeftLabel, HPos.CENTER);
		
		
		// Create ImageViews for the die images
		dieImageViews = new ArrayList<ImageView>(game.numDice());
		for (int i = 0; i < game.numDice(); i++) {
			ImageView curDieImageView = new ImageView(dieImages.get(1));
			curDieImageView.setPreserveRatio(true);
			curDieImageView.setFitWidth(DIE_LENGTH);
			curDieImageView.setOnMouseEntered(new DieMouseEnterHandler());
			curDieImageView.setOnMouseExited(new DieMouseExitHandler());
			curDieImageView.setOnMouseClicked(new DieClickHandler());
			VBox curPane = new VBox(curDieImageView);	// For padding
			curPane.setAlignment(Pos.CENTER);
			curPane.setPadding(new Insets(DICE_PADDING));
			if (i == game.numDice() - 1)
				curPane.setPadding(new Insets(DICE_PADDING, SPACING, DICE_PADDING, DICE_PADDING));
			dieImageViews.add(curDieImageView);
			
			root.add(curPane, i, 1);
		}
		
		
		// Create the category info panes
		categoryInfoPanels = new CategoryInfoPanel[game.categories().length];
		for (int i = 0; i < game.categories().length; i++) {
			categoryInfoPanels[i] = new CategoryInfoPanel(game.categories()[i], CATEGORY_INFO_PANEL_WIDTH, CATEGORY_INFO_PANEL_HEIGHT);
			categoryInfoPanels[i].setOnMouseClicked(new CategoryClickHandler());
			
			int row = i % (game.categories().length / 2 + game.categories().length % 2);
			int column = i < game.categories().length / 2 + game.categories().length % 2 ? game.numDice(): game.numDice() + 1;
			root.add(categoryInfoPanels[i], column, row);
		}
		
		
		// Create the roll button
		rollButton = new Button("Roll");
		if (game.getClass() == Yacht.class) { 
			rollButton.setFont(new Font("Bauhaus 93", 70));
			rollButton.setTextFill(Color.LIGHTYELLOW);
			rollButton.backgroundProperty().bind(Bindings.when(rollButton.pressedProperty())
					.then(new Background(new BackgroundFill(Color.INDIANRED, new CornerRadii(20.0), null)))
					.otherwise(Bindings.when(rollButton.hoverProperty()).then(new Background(new BackgroundFill(Color.DARKRED, new CornerRadii(20.0), null)))
	                .otherwise(new Background(new BackgroundFill(Color.MAROON, new CornerRadii(20.0), null)))));
			rollButton.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10.0), new BorderWidths(5.0))));
		}
		else if (game.getClass() == Crag.class) {
			rollButton.setFont(new Font("Freestyle Script", 70));
			rollButton.setTextFill(Color.AZURE);
			rollButton.backgroundProperty().bind(Bindings.when(rollButton.pressedProperty())
					.then(new Background(new BackgroundFill(Color.SEAGREEN, null, null)))
					.otherwise(Bindings.when(rollButton.hoverProperty()).then(new Background(new BackgroundFill(Color.DARKSEAGREEN, null, null)))
	                .otherwise(new Background(new BackgroundFill(Color.MEDIUMSEAGREEN, null, null)))));
			rollButton.setBorder(new Border(new BorderStroke(Color.DARKGOLDENROD, BorderStrokeStyle.SOLID, null, new BorderWidths(3.0))));
		}
		rollButton.setOnAction(new RollButtonHandler());
		
		root.add(rollButton, 0, 2);
		GridPane.setRowSpan(rollButton, game.categories().length / 2 + game.categories().length % 2 - 4);
		GridPane.setColumnSpan(rollButton, game.numDice());
		GridPane.setValignment(rollButton, VPos.CENTER);
		GridPane.setHalignment(rollButton, HPos.CENTER);
		
		
		// Create the player label
		playerLabel = new Label();
		playerLabel.setFont(new Font("Consolas", 24));
		playerLabel.setBackground(new Background(
				new BackgroundFill(Color.web("#FFFFFF", 0.75), new CornerRadii(10.0), null)));
		playerLabel.setPadding(new Insets(10.0));
		
		root.add(playerLabel, 0, game.categories().length / 2 + game.categories().length % 2 - 2);
		GridPane.setColumnSpan(playerLabel, game.numDice());
		GridPane.setHalignment(playerLabel, HPos.CENTER);
		
		
		// Create the total score label
		totalScoreLabel = new Label();
		totalScoreLabel.setFont(new Font("Consolas", 24));
		totalScoreLabel.setBackground(new Background(
				new BackgroundFill(Color.web("#FFFFFF", 0.75), new CornerRadii(10.0), null)));
		totalScoreLabel.setPadding(new Insets(10.0));
		
		root.add(totalScoreLabel, 0, game.categories().length / 2 + game.categories().length % 2 - 1);
		GridPane.setColumnSpan(totalScoreLabel, game.numDice());
		GridPane.setHalignment(totalScoreLabel, HPos.CENTER);
		
		
		// Update the rolls left label
		rollsLeftLabel.setText("Rolls left: " + game.rollsLeft());
		
		// Update the category info panes
		for (int i = 0; i < game.categories().length; i++) {
			categoryInfoPanels[i].setPanelDisabled(false);
			Category category = categoryInfoPanels[i].getCategory();
			categoryInfoPanels[i].setScore(
					game.curPlayer().getPoints(category),
					game.curPlayer().hasUsedCategory(category));
			if (game.curPlayer().hasUsedCategory(category))
				categoryInfoPanels[i].disable();
			else
				categoryInfoPanels[i].deselect();
			categoryInfoPanels[i].setPanelDisabled(true);
		}
		
		// Update the roll button
		rollButton.setText("Roll");
		rollButton.setOnAction(new RollButtonHandler());
		
		// Update the player label
		playerLabel.setText(game.curPlayer().getName());
		
		// Update the total score label
		totalScoreLabel.setText("Total score: " + game.curPlayer().getTotalScore());
		
		// Update some other variables
		heldDice = new ArrayList<Integer>();
		diceDisabled = true;
		animationRunning = false;
		
		
		// Set stage title
		((Stage)root.getScene().getWindow()).setTitle(game.name());
	}
	
	
	
	/**
	 * Advances to the next player and updates the GUI to reflect the change.
	 */
	public void nextPlayer() {
		
		// Advance to the next player
		game.nextPlayer();
		
		// Clear held dice
		heldDice.clear();
		
		// Reset dice highlight/glow effects
		for (ImageView die: dieImageViews)
			setDieEffect(die, false, false);
		
		// Update the category info panel score labels
		for (int i = 0; i < categoryInfoPanels.length; i++) {
			categoryInfoPanels[i].setPanelDisabled(false);
			Category category = categoryInfoPanels[i].getCategory();
			categoryInfoPanels[i].setScore(
					game.curPlayer().getPoints(category),
					game.curPlayer().hasUsedCategory(category));
			if (game.curPlayer().hasUsedCategory(category))
				categoryInfoPanels[i].disable();
			else
				categoryInfoPanels[i].deselect();
			categoryInfoPanels[i].setPanelDisabled(true);
		}
		
		// Show the new number of rolls left
		rollsLeftLabel.setText("Rolls left: " + game.rollsLeft());
		
		// Change the button back to a roll button.
		rollButton.setText("Roll");
		rollButton.setOnAction(new RollButtonHandler());
		
		// Show the the new current player's name
		playerLabel.setText(game.curPlayer().getName());
		
		// Update the total score label
		totalScoreLabel.setText("Total score: " + game.curPlayer().getTotalScore());
	}
	
	
	
	/**
	 * Rolls the dice.
	 * @author Alex Gill
	 *
	 */
	public class RollButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			
			// Do nothing if animation is going.
			if (animationRunning)
				return;
			
			// Get indexes of dice to roll in the form of an array
			List<Integer> diceToRollList = new ArrayList<Integer>();
			for (int i = 0; i < game.numDice(); i++)
				if (heldDice.indexOf(i) == -1)
					diceToRollList.add(i);
			int[] diceToRoll = new int[diceToRollList.size()];
			for (int i = 0; i < diceToRoll.length; i++)
				diceToRoll[i] = diceToRollList.get(i).intValue();
			
			// Roll
			game.roll(diceToRoll);
			
			// Animate the dice images to simulate rolling
			RollAnimation[] animations = new RollAnimation[diceToRoll.length];
			for (int i = 0; i < diceToRoll.length; i++) {
				
				// Get the relevant information
				Die curDie = game.getDice()[diceToRoll[i]];
				ImageView dieImageView = dieImageViews.get(diceToRoll[i]);
				Image startImage = dieImageView.getImage();
				Image endImage = dieImages.get(curDie.getValue());
				
				// Create the animation
				animations[i] = new RollAnimation(
						dieImageView,
						startImage,
						endImage,
						i * DELAY);
			}
			animationRunning = true;
			
			// Play sound upon the first animation finishing
			animations[0].getAnimation().setOnFinished(e -> {// Play sound
				diceSound.play();
			});
			
			// Enable dice upon the last animation finishing
			animations[diceToRoll.length - 1].getAnimation().setOnFinished(e -> {
				if (diceToRoll.length == 1)
					diceSound.play();
				
				if (game.rollsLeft() != 0)
					diceDisabled = false;
				animationRunning = false;
			});
			
			// Start the animations
			for (int i = 0; i < diceToRoll.length; i++) {
				animations[i].play();
			}
			
			// Update the rolls left label text
			rollsLeftLabel.setText("Rolls left: " + game.rollsLeft());
			
			// Enable the panels that can be clicked
			for (int i = 0; i < categoryInfoPanels.length; i++) {
				Category category = categoryInfoPanels[i].getCategory();
				categoryInfoPanels[i].setPanelDisabled(game.curPlayer().hasUsedCategory(category));
			}
			
			// Disable the roll button and dice if there are no more rolls left.
			if (game.rollsLeft() == 0) {
				rollButton.setDisable(true);
				for (ImageView die: dieImageViews)
					setDieEffect(die, false, false);
			}
			
			// Disable the dice until the animation is done
			diceDisabled = true;
		}
	}
	
	
	
	/**
	 * Submits the user's combination to the selected scoring category.
	 * @author Alex Gill
	 *
	 */
	public class CategoryClickHandler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			CategoryInfoPanel panel = (CategoryInfoPanel)event.getSource();
			Category category = panel.getCategory();
			
			// Do nothing if animation is going.
			if (animationRunning)
				return;

			// If the number of rolls left is still the maximum, the user has
			// not rolled yet and is not allowed to submit.
			if (game.curPlayerHasGone() ||
				game.rollsLeft() == game.numRolls()||
				game.curPlayer().hasUsedCategory(category))
				return;
			
			// Submit the score.
			game.submit(category);
			
			// Display the score.
			panel.setScore(
					game.curPlayer().getPoints(category), 
					game.curPlayer().hasUsedCategory(category));
			
			// Update the total score label
			totalScoreLabel.setText("Total score: " + game.curPlayer().getTotalScore());
			
			// Deselect the panel
			panel.disable();
			
			// All panels are now disabled
			for (int i = 0; i < categoryInfoPanels.length; i++) {
				categoryInfoPanels[i].setPanelDisabled(true);
			}
			
			// Temporarily change the roll button to a "next" button
			rollButton.setText("Next");
			rollButton.setOnAction(e -> { nextPlayer(); });
			rollButton.setDisable(false);
			
			// If the game is over, display the results
			if (game.isOver())
				playAgainDialog();
		}
	}
	
	
	
	/**
	 * Highlights the die and changes the cursor to a hand.
	 * @author Alex Gill
	 *
	 */
	public class DieMouseEnterHandler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			
			// Dice must be enabled
			if (diceDisabled)
				return;
			
			// Get the image view and index of the die
			ImageView die = (ImageView)event.getSource();
			int dieIndex = dieImageViews.indexOf((ImageView)event.getSource());
			
			// Highlight the die
			setDieEffect(die, true, heldDice.indexOf(dieIndex) != -1);
			
			// Change the cursor to a hand.
			Scene scene = ((ImageView)event.getSource()).getScene();
			scene.setCursor(Cursor.HAND);
		}
	}
	
	
	
	/**
	 * Undoes the highlighting of the die and changes the cursor back to normal.
	 * @author Alex Gill
	 *
	 */
	public class DieMouseExitHandler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			
			// Dice must be enabled
			if (diceDisabled)
				return;
			
			// Get the image view and index of the die
			ImageView die = (ImageView)event.getSource();
			int dieIndex = dieImageViews.indexOf((ImageView)event.getSource());
			
			// Undo the highlighting of the die
			setDieEffect(die, false, heldDice.indexOf(dieIndex) != -1);
			
			// Change the cursor back to normal
			Scene scene = ((ImageView)event.getSource()).getScene();
			scene.setCursor(Cursor.DEFAULT);
		}
	}
	
	
	
	/**
	 * Allows the player to 'hold' a die so that it does not roll.
	 * @author Alex Gill
	 *
	 */
	public class DieClickHandler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			
			// Dice must be enabled
			if (diceDisabled)
				return;
			
			// Get the image view and index of the die
			ImageView die = (ImageView)event.getSource();
			int dieIndex = dieImageViews.indexOf((ImageView)event.getSource());
			
			// If supposed to hold...
			if (heldDice.indexOf(dieIndex) == -1) {
				
				// Create a glow around the die
				setDieEffect(die, true, true);
				
				// Add it to the held dice list
				heldDice.add(dieIndex);
			}
			
			// If not supposed to hold... 
			else {
				
				// Remove the glow around the die
				setDieEffect(die, true, false);
				
				// Add it to the held dice list
				heldDice.remove(Integer.valueOf(dieIndex));
			}
			
			// Sort the list
			heldDice.sort(null);
			
			// If all of the dice are held, disable the roll button
			if (heldDice.size() == game.numDice())
				rollButton.setDisable(true);
			
			// Otherwise, enable it
			else
				rollButton.setDisable(false);
		}
	}
	
	
	
	/**
	 * Sets the effect of the die image.
	 * @param die The die image view
	 * @param highlight Whether to highlight the die
	 * @param glow Whether to add glow to the die
	 */
	public void setDieEffect(ImageView die, boolean highlight, boolean glow) {
		
		// Create highlight
		Glow highlightEffect = null;
		if (highlight) {
			highlightEffect = new Glow(0.5);
		}
		
		// Create glow
		DropShadow glowEffect = null;
		if (glow) {
			glowEffect = new DropShadow();
			glowEffect.setColor(Color.YELLOW);
			glowEffect.setRadius(12.0);
			glowEffect.setSpread(0.8);
		}
		
		// Set the effect(s)
		if (highlight && glow) {
			glowEffect.setInput(highlightEffect);
			die.setEffect(glowEffect);
		}
		else if (highlight) {
			die.setEffect(highlightEffect);
		}
		else if (glow) {
			die.setEffect(glowEffect);
		}
		else {
			die.setEffect(null);
		}
	}
	
	
	
	/**
	 * Shows a dialog that asks whether the user would like to play again. If
	 * Yes, the user will be shown the number of players dialog. If No, the
	 * program will exit.
	 */
	public void playAgainDialog() {
		
		// Create the buttons
		ButtonType playButtonType = new ButtonType("Yes", ButtonData.OK_DONE);
		ButtonType exitButtonType = new ButtonType("No", ButtonData.CANCEL_CLOSE);
		
		// Create the text showing the winner
		GridPane results = new GridPane();
		Label resultLabel = new Label("Here are the results:");
		results.add(resultLabel, 0, 0);
		GridPane.setColumnSpan(resultLabel, game.numPlayers());
		Player winner = game.getPlayer(0);
		boolean tied = false;
		for (int i = 0; i < game.numPlayers(); i++) {
			results.add(new Label(game.getPlayer(i).getName()), i, 1);
			results.add(new Label(String.valueOf(game.getPlayer(i).getTotalScore())), i, 2);
			if (game.getPlayer(i).getTotalScore() > winner.getTotalScore()) {
				winner = game.getPlayer(i);
				tied = false;
			}
			else if (game.getPlayer(i).getTotalScore() == winner.getTotalScore())
				tied = true;
		}
		Label messageLabel = new Label();
		if (game.numPlayers() == 1)
			messageLabel.setText("Game over!");
		else if (!tied)
			messageLabel.setText(String.format("%s won with %d points. Congratulations!",
			winner.getName(),
			winner.getTotalScore()));
		else
			messageLabel.setText("There was a tie");
		results.add(messageLabel, 0, 3);
		GridPane.setColumnSpan(messageLabel, game.numPlayers());
		
		// Create the dialog
		Alert dialog = new Alert(AlertType.CONFIRMATION, null, playButtonType, exitButtonType);
		dialog.setTitle(game.name() + " - New Game");
		dialog.setHeaderText("Play again?");
		dialog.getDialogPane().setContent(results);
		
		// Play cheer sound
		cheerSound.play();
		
		// Standby and act depending on the user's choice
		dialog.showAndWait().ifPresent(response -> {
			
			// If Play, create a new game with the given number of players
			if (response == playButtonType) {
				newGameDialog();
			}
			
			// If Exit, close the game
			else {
				Platform.exit();
			}
		});
	}
	
	
	
	public void newGameDialog() {
		Class<?> variant = NewGameDialog.showAndWait(variants);
		numberOfPlayersDialog(variant);
	}
	
	
	
	/**
	 * Shows the user a dialog that asks for the number of players. The user is
	 * shown a spinner and can select the number of players. Once the user
	 * presses Next, the user will advance to the player names dialog. The user
	 * can also select Exit to exit the program.
	 */
	public void numberOfPlayersDialog(Class<?> variant) {
		
		// Create the buttons
		ButtonType nextButtonType = new ButtonType("Next", ButtonData.OK_DONE);
		ButtonType exitButtonType = new ButtonType("Exit", ButtonData.CANCEL_CLOSE);
		
		// Create the player count spinner
		Spinner<Integer> playerCountSpinner = new Spinner<Integer>(1, 8, 1);
		
		// Instantiate the variant to get name
		DiceGame variantInstance = null;
		try {
			variantInstance = (DiceGame) variant.getDeclaredConstructor(int.class).newInstance(1);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			System.out.println("Error instantiating dice game class...");
			e.printStackTrace();
			System.exit(1);
		}
		
		// Create the dialog
		Alert dialog = new Alert(AlertType.CONFIRMATION, null, nextButtonType, exitButtonType);
		dialog.setTitle(variantInstance.name() + " - New Game");
		dialog.setHeaderText("How many players?");
		dialog.getDialogPane().setContent(playerCountSpinner);
		
		// Standby and act depending on the user's choice
		dialog.showAndWait().ifPresent(response -> {
			
			// If Play, create a new game with the given number of players
			if (response == nextButtonType) {
				playerNamesDialog(variant, playerCountSpinner.getValue());
			}
			
			// If Exit, close the game
			else {
				Platform.exit();
			}
		});
	}
	
	
	
	/**
	 * Shows the user a dialog that asks for the players' names. The user can
	 * opt to leave any or all of these blank, and the player names will be set
	 * to the default. The user can select Play to start the game, Back to go
	 * back to the number of players dialog, or Exit to exit the program.
	 */
	public void playerNamesDialog(Class<?> variant, int numPlayers) {
		
		// Create the buttons
		ButtonType playButtonType = new ButtonType("Play", ButtonData.OK_DONE);
		ButtonType backButtonType = new ButtonType("Back", ButtonData.BACK_PREVIOUS);
		ButtonType exitButtonType = new ButtonType("Exit", ButtonData.CANCEL_CLOSE);
		
		// Create the player names text areas
		TextField[] names = new TextField[numPlayers];
		VBox namesBox = new VBox();
		for (int i = 0; i < numPlayers; i++) {
			names[i] = new TextField();
			namesBox.getChildren().add(names[i]);
		}
		
		// Instantiate the variant to get name
		DiceGame variantInstance = null;
		try {
			variantInstance = (DiceGame) variant.getDeclaredConstructor(int.class).newInstance(1);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			System.out.println("Error instantiating dice game class...");
			e.printStackTrace();
			System.exit(1);
		}
		
		// Create the dialog
		Alert dialog = new Alert(AlertType.CONFIRMATION, null, playButtonType, backButtonType, exitButtonType);
		dialog.setTitle(variantInstance.name() + " - New Game");
		dialog.setHeaderText("What are the players' names? (Optional)");
		dialog.getDialogPane().setContent(namesBox);
		
		// Standby and act depending on the user's choice
		dialog.showAndWait().ifPresent(response -> {
			
			// If Play, start the game and name players.
			if (response == playButtonType) {
				newGame(variant, numPlayers);
				for (int i = 0; i < numPlayers; i++)
					if (!names[i].getText().isEmpty())
						game.getPlayer(i).setName(names[i].getText());
			}
			
			// If Back, go to number of players dialog
			else if (response == backButtonType) {
				numberOfPlayersDialog(variant);
			}
			
			// If Exit, close the game
			else {
				Platform.exit();
			}
		});
	}

}
