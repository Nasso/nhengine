package io.github.nasso.nhengine.audio.openal;

import static org.lwjgl.openal.AL10.*;

import java.nio.ByteBuffer;

public class OALBuffer {
	int id;
	
	private int frequency;
	private int bits;
	private int channels;
	private int size;
	private int sampleSize;
	private ByteBuffer data;
	
	public OALBuffer() {
		this.id = alGenBuffers();
		
		this.frequency = alGetBufferi(this.id, AL_FREQUENCY);
		this.bits = alGetBufferi(this.id, AL_BITS);
		this.channels = alGetBufferi(this.id, AL_CHANNELS);
		this.size = alGetBufferi(this.id, AL_SIZE);
		this.sampleSize = 0;
	}
	
	public boolean isValid() {
		return alIsBuffer(this.id);
	}
	
	public void dispose() {
		alDeleteBuffers(this.id);
		this.id = 0;
	}
	
	public int getFrequency() {
		return this.frequency;
	}
	
	public int getBits() {
		return this.bits;
	}
	
	public int getChannels() {
		return this.channels;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public int getSampleSize() {
		return this.sampleSize;
	}
	
	public ByteBuffer getData() {
		return this.data;
	}
	
	public void setData(ByteBuffer data, int format, int freq) {
		this.data = data;
		
		alBufferData(this.id, format, data, freq);
		this.frequency = alGetBufferi(this.id, AL_FREQUENCY);
		this.bits = alGetBufferi(this.id, AL_BITS);
		this.channels = alGetBufferi(this.id, AL_CHANNELS);
		this.size = alGetBufferi(this.id, AL_SIZE);
		this.sampleSize = this.size / this.channels;
		if(this.bits == 16) this.sampleSize /= 2;
	}
}
