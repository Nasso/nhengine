package io.github.nasso.nhengine.audio;

import io.github.nasso.nhengine.component.AudioSourceComponent;

/**
 * An interface representing an audio player. An <code>AudioPlayer</code> provides methods to play <code>Sound</code> objects globally (like a music).
 * 
 * @author nasso
 */
public interface AudioPlayer {
	/**
	 * Plays the given sound with the given parameters.
	 * 
	 * @param snd
	 *            The sound to play
	 * @param volume
	 *            The volume
	 * @param pitch
	 *            The pitch
	 */
	public void playSound(Sound snd, float volume, float pitch);
	
	/**
	 * Plays the given sound with the given parameters.
	 * 
	 * @param snd
	 *            The sound to play
	 * @param volume
	 *            The volume
	 */
	public void playSound(Sound snd, float volume);
	
	/**
	 * Plays the given sound with the given parameters.
	 * 
	 * @param snd
	 *            The sound to play
	 */
	public void playSound(Sound snd);
	
	public void sourceBuffer(AudioSourceComponent src, Sound snd);
	
	public void sourcePlay(AudioSourceComponent src, float time, float pitch, float gain, boolean loop);
	
	public void sourcePause(AudioSourceComponent src);
	
	public void sourceStop(AudioSourceComponent src);
}
