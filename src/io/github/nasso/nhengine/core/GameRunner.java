package io.github.nasso.nhengine.core;

public interface GameRunner {
	/**
	 * Appelé lors de l'initialisation.
	 */
	public void init();
	
	/**
	 * Appelé à chaque tour de boucle.
	 * 
	 * @param delta
	 *            temps depuis la dernière update, en millisecondes
	 */
	public void update(float delta);
	
	/**
	 * Appelé lors de la fin du jeu.
	 */
	public void dispose();
}
