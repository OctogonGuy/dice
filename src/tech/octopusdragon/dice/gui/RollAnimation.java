package tech.octopusdragon.dice.gui;

import java.util.Random;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * Creates an animation that simulates the rolling of a die.
 * @author Alex Gill
 *
 */
public class RollAnimation {
	
	private final double DURATION = 1000.0;	// The duration of the animation
	private final int REVOLUTIONS = 0;		// The number of revolutions
	private final int IMAGE_CHANGES = 12;	// The number of image changes
	

	private ImageView imageView;
	private Image startImage;
	private Image endImage;
	
	
	private RotateTransition spinAnimation;
	private Timeline changeImageAnimation;
	private ParallelTransition combinedAnimation;

	
	// Load images (static)
	private static final String IMAGE_DIRECTORY = "animation";
	public static final Image[] images;
	static {
		images = new Image[32];
		for (int i = 0; i < images.length; i++) {
			images[i] = new Image(RollAnimation.class.getClassLoader().getResourceAsStream(String.format("%s/%02d.png", IMAGE_DIRECTORY, (i + 1))));
		}
	}
	
	
	
	/**
	 * Instantiates the RollAnimation object
	 */
	public RollAnimation(ImageView view, Image start, Image end) {
		this(view, start, end, 0.0);
	}
	
	
	
	/**
	 * Instantiates the RollAnimation object with a delay
	 */
	public RollAnimation(ImageView view, Image start, Image end, double delay) {
		imageView = view;
		startImage = start;
		endImage = end;
		
		// Create the spin animation
		spinAnimation = new RotateTransition();
		spinAnimation.setNode(imageView);
		spinAnimation.setDelay(new Duration(delay));
		spinAnimation.setDuration(new Duration(DURATION));
		spinAnimation.setFromAngle(0.0);
		spinAnimation.setToAngle(360.0 * REVOLUTIONS);
		
		// Create the change image animation
		changeImageAnimation = new Timeline();
		changeImageAnimation.setDelay(new Duration(delay));
		
		// Combine the animations so they play in parallel
		combinedAnimation = new ParallelTransition(
				spinAnimation,
				changeImageAnimation);
	}
	
	
	
	/**
	 * Returns the combined animation.
	 * @return The combined animation
	 */
	public Animation getAnimation() {
		return combinedAnimation;
	}
	
	
	
	/**
	 * Plays the animation
	 */
	public void play() {
		
		// Randomly generate key frames to be used
		generateKeyFrames();
		
		// Play the animation
		combinedAnimation.play();
	}
	
	
	
	/**
	 * Randomly generates key frames for the change image animation
	 */
	private void generateKeyFrames() {
		
		// Create a Random object for generating random images
		Random rand = new Random();
		
		// Clear existing key frames
		changeImageAnimation.getKeyFrames().clear();
		
		// Add key frames to simulate rolling.
		// Start with startImage
		changeImageAnimation.getKeyFrames().add(new KeyFrame(
				new Duration(0),
				new KeyValue(
						imageView.imageProperty(),
						startImage)));
		
		// Generate random images in the middle
		for (int i = 1; i < IMAGE_CHANGES - 2; i++) {
			changeImageAnimation.getKeyFrames().add(new KeyFrame(
					new Duration(DURATION / IMAGE_CHANGES * i),
					new KeyValue(
							imageView.imageProperty(),
							images[rand.nextInt(images.length)])));
		}
			
		// End with endImage
		changeImageAnimation.getKeyFrames().add(new KeyFrame(
				new Duration(DURATION - DURATION / IMAGE_CHANGES),
				new KeyValue(
						imageView.imageProperty(),
						endImage)));
	}
}
