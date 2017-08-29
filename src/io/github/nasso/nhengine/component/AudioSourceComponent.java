package io.github.nasso.nhengine.component;

import io.github.nasso.nhengine.audio.Sound;
import io.github.nasso.nhengine.core.Game;
import io.github.nasso.nhengine.level.Component;

/**
 * A component that can be used as an audio source. It can be modelised as a single point, emitting audio in all directions.
 * 
 * @author nasso
 */
public class AudioSourceComponent extends Component {
	private boolean looping = false;
	private float startTime = 0, pitch = 1, gain = 1;
	
	private Sound sound = null;
	
	/**
	 * Plays the current sound. The given parameters will be set using their respective setters before actually playing.<br>
	 * Does nothing if no Sound is bound to this source.
	 * 
	 * @param time The start time in seconds
	 * @param pitch The pitch
	 * @param gain The gain
	 * @param looping looping
	 */
	public void play(float time, float pitch, float gain, boolean looping) {
		this.setPitch(pitch);
		this.setGain(gain);
		this.setLooping(looping);
		
		Game.instance().getAudioPlayer().sourcePlay(this, time, this.getPitch(), this.getGain(), this.isLooping());
	}
	
	/**
	 * Plays the current sound.<br>
	 * Equivalent to:
	 * 
	 * <pre>
	 * src.play(time, pitch, gain, src.isLooping());
	 * </pre>
	 * 
	 * @param time The start time in seconds
	 * @param pitch The pitch
	 * @param gain The gain
	 */
	public void play(float time, float pitch, float gain) {
		this.play(time, pitch, gain, this.isLooping());
	}
	
	/**
	 * Plays the current sound.<br>
	 * Equivalent to:
	 * 
	 * <pre>
	 * src.play(time, pitch, src.getGain());
	 * </pre>
	 * 
	 * @param time The start time in seconds
	 * @param pitch The pitch
	 */
	public void play(float time, float pitch) {
		this.play(time, pitch, this.getGain());
	}
	
	/**
	 * Plays the current sound.<br>
	 * Equivalent to:
	 * 
	 * <pre>
	 * src.play(time, src.getPitch());
	 * </pre>
	 * 
	 * @param time
	 *            The start time in seconds
	 */
	public void play(float time) {
		this.play(time, this.getPitch());
	}
	
	/**
	 * Plays the sound, starting from the beginning.
	 * Equivalent to:
	 * 
	 * <pre>
	 * src.play(0);
	 * </pre>
	 */
	public void play() {
		this.play(0);
	}
	
	/**
	 * Pauses
	 */
	public void pause() {
		Game.instance().getAudioPlayer().sourcePause(this);
	}
	
	/**
	 * Stops
	 */
	public void stop() {
		Game.instance().getAudioPlayer().sourceStop(this);
	}
	
	/**
	 * Sets the pitch. Changes will take effect the next time the sound is played.
	 * 
	 * @param pitch
	 */
	public void setPitch(float pitch) {
		if(this.pitch == pitch) return;
		
		this.pitch = pitch;
	}
	
	/**
	 * @return The current pitch
	 */
	public float getPitch() {
		return this.pitch;
	}
	
	/**
	 * @return Return the current sound.
	 */
	public Sound getSound() {
		return this.sound;
	}
	
	/**
	 * Sets the current sound to be played using this audio source.
	 * 
	 * @param sound
	 */
	public void setSound(Sound sound) {
		if(this.sound == sound) return;
		this.sound = sound;
		
		Game.instance().getAudioPlayer().sourceBuffer(this, sound);
	}
	
	/**
	 * @return The gain
	 */
	public float getGain() {
		return this.gain;
	}
	
	/**
	 * @param gain
	 *            The new gain
	 */
	public void setGain(float gain) {
		this.gain = gain;
	}
	
	/**
	 * @return True if the audio shall loop
	 */
	public boolean isLooping() {
		return this.looping;
	}
	
	/**
	 * @param looping
	 *            When true, the audio will loop when it'll reach the end
	 */
	public void setLooping(boolean looping) {
		if(this.looping == looping) return;
		
		this.looping = looping;
	}

	public float getStartTime() {
		return this.startTime;
	}

	public void setStartTime(float startTime) {
		this.startTime = startTime;
	}
}
