package io.github.nasso.nhengine.audio;

import java.io.IOException;
import java.nio.ByteBuffer;

import io.github.nasso.nhengine.data.AudioDataLoader;
import io.github.nasso.nhengine.event.Observable;

public class Sound extends Observable {
	private ByteBuffer data;
	private int channels;
	private int sampleRate;
	private int sampleCount;
	private int bitsPerSample;
	
	public Sound(ByteBuffer data, int channels, int sampleRate, int bitsPerSample, int sampleCount) {
		this.data = data;
		this.channels = channels;
		this.sampleRate = sampleRate;
		this.bitsPerSample = bitsPerSample;
		this.sampleCount = sampleCount;
	}
	
	/**
	 * @return Duration in seconds
	 */
	public float getDuration() {
		return (float) this.sampleCount / this.sampleRate;
	}
	
	public int getChannels() {
		return this.channels;
	}
	
	public int getSampleRate() {
		return this.sampleRate;
	}
	
	public int getSampleCount() {
		return this.sampleCount;
	}
	
	public int getBitsPerSample() {
		return this.bitsPerSample;
	}
	
	public void setBitsPerSample(int bitsPerSample) {
		this.bitsPerSample = bitsPerSample;
	}
	
	public ByteBuffer getData() {
		return this.data;
	}
	
	public void dispose() {
		this.triggerEvent("dispose");
	}
	
	public static Sound load(String fileName) throws IOException {
		return load(fileName, false);
	}
	
	public static Sound load(String fileName, boolean inJar) throws IOException {
		return AudioDataLoader.load(fileName, inJar);
	}
}
