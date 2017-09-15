package io.github.nasso.nhengine.core;

import io.github.nasso.nhengine.level.Level;

/**
 * A game runner is what gets notified for the 3 major steps of a game:
 * <ul>
 * 	<li>Initialization</li>
 * 	<li>Frame update</li>
 * 	<li>Disposition</li>
 * </ul>
 * 
 * @author nasso
 */
public interface GameRunner {
	/**
	 * Called first, when the game starts.
	 */
	public void init();
	
	/**
	 * Called each frame, either before or after the call to the level {@link Level#update(float) update(float)} method
	 * (depends on the {@link Game#doesUpdateLevelBefore() doesUpdateLevelBefore()} property of the game).
	 * 
	 * @param delta The delta-time (time between each frame), in milliseconds.
	 */
	public void update(float delta);
	
	/**
	 * Called when the game's quitting.
	 */
	public void dispose();
}
