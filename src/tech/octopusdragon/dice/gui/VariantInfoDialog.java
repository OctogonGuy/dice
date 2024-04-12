package tech.octopusdragon.dice.gui;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tech.octopusdragon.dice.DiceGame;

public class VariantInfoDialog {
	
	// --- Constants ---
	private final static double WIDTH = 500.0;	// The width of the dialog
	private final static double HEIGHT = 300.0;	// The height of the dialog
	private static final double PADDING = 20.0;
	
	// --- Variables ---
	private static DiceGame variantInstance;
	
	public static void show(Class<?> variant) {
		
		// Instantiate variant
		try {
			variantInstance = (DiceGame) variant.getDeclaredConstructor(int.class).newInstance(1);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			System.out.println("Error instantiating dice game class...");
			e.printStackTrace();
			System.exit(1);
		}
		
		// Create the stage
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		
		// Build the UI
		buildUI(stage);
		
		// Show and wait for user selection
		stage.show();
	}
	
	private static void buildUI(Stage stage) {
		
		// Create the label to show the description
		Label descriptionLabel = new Label(variantInstance.description());
		descriptionLabel.setWrapText(true);
		descriptionLabel.setPadding(new Insets(PADDING));
		
		// Set the scene
		Scene scene = new Scene(descriptionLabel, WIDTH, HEIGHT);
		stage.setScene(scene);
		stage.setTitle(variantInstance.name() + " Info");
		stage.getIcons().add(RollAnimation.images[new Random().nextInt(RollAnimation.images.length)]);
	}
}
