package io.github.nasso.nhengine.component;

import io.github.nasso.nhengine.audio.Sound;
import io.github.nasso.nhengine.level.Component;

public class AudioSourceComponent extends Component {
	public enum Status {
		PLAYING, PAUSING, PAUSED, STOPPING, STOPPED, RESTARTING
	}
	
	private Sound soundBuffer;
	private Status status = Status.STOPPED;
	
	private float pitch = 1;
	private float gain = 1;
	private float minGain = 0;
	private float maxGain = 1;
	private boolean looping = false;
	
	private int version = 0;
	
	public void play(float time, float pitch) {
		if(this.soundBuffer != null) this.status = (this.status == Status.PLAYING ? Status.RESTARTING : Status.PLAYING);
		this.version++;
	}
	
	public void play(float time) {
		this.play(time, 1);
	}
	
	public void play() {
		this.play(0);
	}
	
	public void pause() {
		if(this.soundBuffer != null) this.status = Status.PAUSING;
		this.version++;
	}
	
	public void stop() {
		this.status = Status.STOPPING;
		this.version++;
	}
	
	public void setPitch(float pitch) {
		if(this.pitch == pitch) return;
		
		this.pitch = pitch;
		this.version++;
	}
	
	public float getPitch() {
		return this.pitch;
	}
	
	/**
	 * Called by the audio backend, DO NOT CALL BY YOURSELF
	 */
	public void setStatus(Status stat) {
		this.status = stat;
	}
	
	public Status getStatus() {
		return this.status;
	}
	
	public Sound getSoundBuffer() {
		return this.soundBuffer;
	}
	
	public void setSoundBuffer(Sound soundBuffer) {
		this.soundBuffer = soundBuffer;
		this.status = Status.STOPPING;
		this.version++;
	}
	
	public int getVersion() {
		return this.version;
	}
	
	public void setVersion(int version) {
		this.version = version;
	}
	
	public float getGain() {
		return this.gain;
	}
	
	public void setGain(float gain) {
		if(this.gain == gain) return;
		
		this.gain = gain;
		this.version++;
	}
	
	public float getMinGain() {
		return this.minGain;
	}
	
	public void setMinGain(float minGain) {
		if(this.minGain == minGain) return;
		
		this.minGain = minGain;
		this.version++;
	}
	
	public float getMaxGain() {
		return this.maxGain;
	}
	
	public void setMaxGain(float maxGain) {
		if(this.maxGain == maxGain) return;
		
		this.maxGain = maxGain;
		this.version++;
	}
	
	public boolean isLooping() {
		return this.looping;
	}
	
	public void setLooping(boolean looping) {
		if(this.looping == looping) return;
		
		this.looping = looping;
		this.version++;
	}
}
