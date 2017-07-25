package io.github.nasso.nhengine.component;

import io.github.nasso.nhengine.audio.Sound;
import io.github.nasso.nhengine.level.Component;

/**
 * A component that can be used as an audio source. It can be modelised as a single point, emitting audio in all directions.
 * 
 * @author nasso
 */
public class AudioSourceComponent extends Component {
	/**
	 * An enumeration of the different states an audio source can be.
	 * 
	 * @author nasso
	 */
	public enum Status {
		PLAYING, PAUSING, PAUSED, STOPPING, STOPPED, RESTARTING
	}
	
	private Sound sound;
	private Status status = Status.STOPPED;
	
	private float currentTime = 0;
	private float pitch = 1;
	private float gain = 1;
	private float minGain = 0;
	private float maxGain = 1;
	private boolean looping = false;
	
	private int version = 0;
	
	/**
	 * Plays the sound.
	 * Equivalent to:<br>
	 * 
	 * <pre>
	 * src.setCurrentTime(time);
	 * src.setPitch(pitch);
	 * src.play();
	 * </pre>
	 * 
	 * @param time
	 *            The start time in seconds
	 * @param pitch
	 *            The pitch
	 */
	public void play(float time, float pitch) {
		this.setCurrentTime(time);
		this.setPitch(pitch);
		this.play();
	}
	
	/**
	 * Plays the current sound with a normal pitch.<br>
	 * Equivalent to:
	 * 
	 * <pre>
	 * src.setCurrentTime(time);
	 * src.play();
	 * </pre>
	 * 
	 * @param time
	 *            The start time in seconds
	 */
	public void play(float time) {
		this.setCurrentTime(time);
		this.play();
	}
	
	/**
	 * Plays the sound, starting from the current time, with the current pitch.
	 */
	public void play() {
		if(this.sound != null) this.status = (this.status == Status.PLAYING ? Status.RESTARTING : Status.PLAYING);
		this.version++;
	}
	
	/**
	 * Pauses
	 */
	public void pause() {
		if(this.sound != null) this.status = Status.PAUSING;
		this.version++;
	}
	
	/**
	 * Stops
	 */
	public void stop() {
		this.status = Status.STOPPING;
		this.version++;
	}
	
	/**
	 * Sets the pitch. Changes will take effect the next time the sound is played.
	 * 
	 * @param pitch
	 */
	public void setPitch(float pitch) {
		if(this.pitch == pitch) return;
		
		this.pitch = pitch;
		this.version++;
	}
	
	/**
	 * @return The current pitch
	 */
	public float getPitch() {
		return this.pitch;
	}
	
	/**
	 * Called by the audio backend, the client shouldn't call it.
	 */
	public void setStatus(Status stat) {
		this.status = stat;
	}
	
	/**
	 * @return The current status of the audio source.
	 */
	public Status getStatus() {
		return this.status;
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
		this.sound = sound;
		this.status = Status.STOPPING;
		this.version++;
	}
	
	/**
	 * The "version" is an int that get incremented at every change in the state of this audio source. It is used internally.
	 * 
	 * @return The current version of the audio source.
	 */
	public int getVersion() {
		return this.version;
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
		if(this.gain == gain) return;
		
		this.gain = gain;
		this.version++;
	}
	
	/**
	 * @return The minimum gain
	 */
	public float getMinGain() {
		return this.minGain;
	}
	
	/**
	 * @param minGain
	 *            The next minimum gain
	 */
	public void setMinGain(float minGain) {
		if(this.minGain == minGain) return;
		
		this.minGain = minGain;
		this.version++;
	}
	
	/**
	 * @return The maximum gain
	 */
	public float getMaxGain() {
		return this.maxGain;
	}
	
	/**
	 * @param maxGain
	 *            The new maximum gain
	 */
	public void setMaxGain(float maxGain) {
		if(this.maxGain == maxGain) return;
		
		this.maxGain = maxGain;
		this.version++;
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
		this.version++;
	}
	
	/**
	 * @return The current time in seconds
	 */
	public float getCurrentTime() {
		return this.currentTime;
	}
	
	/**
	 * @param currentTime
	 *            The target time in seconds
	 */
	public void setCurrentTime(float currentTime) {
		this.currentTime = currentTime;
	}
}
