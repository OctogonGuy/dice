package tech.octopusdragon.dice.gui;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tech.octopusdragon.dice.*;

/**
 * Shows a selection to allow the user to select a variant of the dice game
 * to play. It includes buttons with the name of the variant. Upon clicking
 * these, it starts a new game of that variant. Next to these buttons is a
 * button with a question mark. This button will bring up a new dialog that
 * displays a description of the variant.
 * @author Alex Gill
 *
 */
public class NewGameDialog {
	
	// --- Constants ---
	private static final double H_PADDING = 100.0;
	private static final double V_PADDING = 20.0;
	private static final double H_SPACING = 4.0;
	private static final double V_SPACING = 12.0;
	
	// --- Variables ---
	private static Class<?>[] variants;	// Array of checkers variants
	private static Class<?> selectedVariant;	// The variant the user selected
	
	/**
	 * Constructs the selection.
	 * @param variants
	 */
	public static Class<?> showAndWait(Class<?>[] variants) {
		
		// Instantiate instance variables if not already instantiated
		if (NewGameDialog.variants == null)
			NewGameDialog.variants = variants;
		
		// Create the stage
		Stage stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setOnCloseRequest(e -> {
			Platform.exit();
			System.exit(0);
		});
		
		// Build the UI
		buildUI(stage);
		
		// Show and wait for user selection
		stage.showAndWait();
		
		// Return the seleciton
		return selectedVariant;
	}
	
	/**
	 * Builds the UI for the dialog.
	 */
	private static void buildUI(Stage stage) {
		
		// The content of the dialog is a grid pane with a play button and info
		// button on each row
		GridPane variantGridPane = new GridPane();
		variantGridPane.setAlignment(Pos.CENTER);
		variantGridPane.setPadding(new Insets(V_PADDING, H_PADDING, V_PADDING, H_PADDING));
		variantGridPane.setHgap(H_SPACING);
		variantGridPane.setVgap(V_SPACING);
		for (int i = 0; i < variants.length; i++) {
			Class<?> variant = variants[i];
			
			// Instantiate the variant
			DiceGame variantInstance = null;
			try {
				variantInstance = (DiceGame) variants[i].getDeclaredConstructor(int.class).newInstance(1);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				System.out.println("Error instantiating dice game class...");
				e.printStackTrace();
				System.exit(1);
			}
			
			// Create the play button
			Button playButton = new Button(variantInstance.name());
			playButton.setOnAction(e -> {
				selectedVariant = variant;
				stage.close();
			});
			AnchorPane playButtonAnchorPane = new AnchorPane(playButton);
			AnchorPane.setLeftAnchor(playButton, 0.0);
			AnchorPane.setRightAnchor(playButton, 0.0);
			variantGridPane.add(playButtonAnchorPane, 0, i);
			
			// Create the info button
			Button infoButton = new Button("?");
			infoButton.setOnAction(e -> {
				VariantInfoDialog.show(variant);
			});
			AnchorPane infoButtonAnchorPane = new AnchorPane(infoButton);
			AnchorPane.setLeftAnchor(infoButton, 0.0);
			AnchorPane.setRightAnchor(infoButton, 0.0);
			variantGridPane.add(infoButtonAnchorPane, 1, i);
		}
		
		// Set the scene
		Scene scene = new Scene(variantGridPane);
		stage.setScene(scene);
		stage.setTitle("Variant Selection");
		stage.getIcons().add(RollAnimation.images[new Random().nextInt(RollAnimation.images.length)]);
	}
}
