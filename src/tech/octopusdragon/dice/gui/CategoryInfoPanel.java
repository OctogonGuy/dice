package tech.octopusdragon.dice.gui;

import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import tech.octopusdragon.dice.*;

/**
 * The GUI component showing the name of the scoring category, the description
 * of the number of points that can be awarded for that category, and the number
 * of points the player has scored in that category, if any.
 * @author Alex Gill
 *
 */
public class CategoryInfoPanel extends GridPane {
	
	// Constants
	private final double PADDING = 5.0;
	private final double SCORE_LABEL_WIDTH = 50.0;
	private final Background DEFAULT_BACKGROUND = new Background(
			new BackgroundFill(Color.WHITE, new CornerRadii(5.0), null));
	private final Background SELECTED_BACKGROUND = new Background(
			new BackgroundFill(Color.YELLOW, new CornerRadii(5.0), null));
	private final Background DISABLED_BACKGROUND = new Background(
			new BackgroundFill(Color.LIGHTGRAY, new CornerRadii(5.0), null));
	
	// GUI components
	private Label nameLabel;
	private Label descriptionLabel;
	private Label scoreLabel;
	
	private Category category;	// The category
	private boolean disabled;	// Whether the info panel is disabled
	
	/**
	 * Initializes and constructs the category info panel.
	 * @param cat The scoring category
	 * @param width The width of the panel
	 * @param height The height of the panel
	 */
	public CategoryInfoPanel(Category category, double width, double height) {
		super();
		
		// Initialize some information
		this.category = category;
		
		// Create the name label
		nameLabel = new Label(category.getName());
		nameLabel.setFont(new Font("Candara Bold", 13));
		
		// Create the description label
		descriptionLabel = new Label(category.getDescription() +
				" - " +
				category.getScoring());
		descriptionLabel.setFont(new Font("Georgia", 11));
		descriptionLabel.setTextFill(Color.GRAY);
		descriptionLabel.setWrapText(true);
		descriptionLabel.setTextAlignment(TextAlignment.CENTER);
		
		// Create the score label
		scoreLabel = new Label();
		scoreLabel.setBackground(new Background(new BackgroundFill(
				Color.WHITE,
				new CornerRadii(2.0),
				null)));
		scoreLabel.setBorder(new Border(new BorderStroke(
				Color.BLACK,
				BorderStrokeStyle.SOLID,
				new CornerRadii(2.0),
				new BorderWidths(1.0))));
		scoreLabel.setFont(new Font("Calibri", 13));
		scoreLabel.setAlignment(Pos.CENTER);
		scoreLabel.setMinWidth(SCORE_LABEL_WIDTH);
		scoreLabel.setPrefWidth(SCORE_LABEL_WIDTH);
		scoreLabel.setMaxWidth(SCORE_LABEL_WIDTH);
		
		// Put the components in the grid
		this.add(nameLabel, 0, 0);
		GridPane.setHalignment(nameLabel, HPos.CENTER);
		GridPane.setVgrow(nameLabel, Priority.ALWAYS);
		this.add(descriptionLabel, 0, 1);
		GridPane.setHalignment(descriptionLabel, HPos.CENTER);
		GridPane.setVgrow(descriptionLabel, Priority.ALWAYS);
		this.add(scoreLabel, 0, 2);
		GridPane.setHalignment(scoreLabel, HPos.CENTER);
		GridPane.setVgrow(scoreLabel, Priority.ALWAYS);
		
		// Configure the grid
		this.setBackground(DEFAULT_BACKGROUND);
		this.setOnMouseEntered(new MouseEnterHandler());
		this.setOnMouseExited(new MouseExitHandler());
		this.setAlignment(Pos.CENTER);
		this.setPadding(new Insets(PADDING));
		this.setMinSize(width, height);
		this.setPrefSize(width, height);
		this.setMaxSize(width, height);
	}
	
	
	
	/**
	 * Returns the scoring category.
	 * @return The scoring category
	 */
	public Category getCategory() {
		return category;
	}
	
	
	
	/**
	 * Sets the score to show in the score label
	 * @param score The score
	 * @param show Whether or not to show the score
	 */
	public void setScore(int score, boolean show) {
		
		// If set to show, show the score.
		if (show) {
			scoreLabel.setText(Integer.toString(score));
		}
		
		// Otherwise, show empty.
		else {
			scoreLabel.setText("");
		}
	}
	
	
	/**
	 * Sets the disabled value.
	 * @param d Whether the category info panel is to be disabled
	 */
	public void setPanelDisabled(boolean d) {
		disabled = d;
	}
	
	
	
	/**
	 * Selects the pane.
	 * @author Alex Gill
	 *
	 */
	private class MouseEnterHandler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			select();
		}
	}
	
	
	
	/**
	 * Deselects the pane.
	 * @author Alex Gill
	 *
	 */
	private class MouseExitHandler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			deselect();
		}
	}
	
	
	
	/**
	 * If not disabled, highlights the panel and changes the cursor to a hand
	 * @author Alex Gill
	 */
	public void select() {
		// Do nothing if disabled
		if (disabled)
			return;
		
		// Highlight the panel
		this.setBackground(SELECTED_BACKGROUND);
		
		// Change the cursor to a hand.
		Scene scene = this.getScene();
		scene.setCursor(Cursor.HAND);
	}
	
	
	
	/**
	 * If not disabled, reverts the panel and changes the cursor back to normal
	 * @author Alex Gill
	 *
	 */
	public void deselect() {
			
		// Do nothing if disabled
		if (disabled)
			return;
		
		// Revert the background color of the panel
		this.setBackground(DEFAULT_BACKGROUND);
		
		// Change the cursor back to normal.
		Scene scene = this.getScene();
		scene.setCursor(Cursor.DEFAULT);
	}
	
	
	
	/**
	 * If not changes the look of the panel to show that it is set and cannot be
	 * selected.
	 * @author Alex Gill
	 *
	 */
	public void disable() {
		
		// Change the background to the disabled background
		this.setBackground(DISABLED_BACKGROUND);
		
		// Change the cursor back to normal.
		Scene scene = this.getScene();
		scene.setCursor(Cursor.DEFAULT);
	}
}
