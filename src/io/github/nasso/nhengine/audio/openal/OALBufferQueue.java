package io.github.nasso.nhengine.audio.openal;

import static org.lwjgl.openal.AL10.*;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

public class OALBufferQueue {
	public static final int QUEUE_SIZE = 4;
	public static final int BUFFER_SIZE = 48000 / 2; // for 1 channel > will be 48000 for stereo
	
	private int[] bufferIDs = new int[QUEUE_SIZE];
	
	private int format;
	
	private int frequency;
	private int bits;
	private int channels;
	private int size;
	private int sampleSize;
	private ByteBuffer data;
	
	private ByteBuffer subBuf, lastSubBuf;
	private int bufferCount = 0;
	private int position = 0;
	
	public OALBufferQueue() {
		for(int i = 0; i < this.bufferIDs.length; i++) {
			this.bufferIDs[i] = alGenBuffers();
		}
	}
	
	public boolean reachedTheEnd() {
		System.out.println("Position: " + this.position + " / " + this.bufferCount);
		return this.position >= this.bufferCount - QUEUE_SIZE;
	}
	
	private void fillBuffer(int i) {
		int buf = this.bufferIDs[i];
		
		int globalIndex = i + this.position;
		
		if(i + this.position == this.bufferCount - 1 && this.lastSubBuf != null) {
			for(int j = 0; j < this.lastSubBuf.capacity(); j++) {
				this.lastSubBuf.put(this.data.get(globalIndex * this.size + j));
			}
			this.lastSubBuf.flip();
			
			alBufferData(buf, this.format, this.lastSubBuf, this.frequency);
		} else {
			for(int j = 0; j < this.size; j++) {
				this.subBuf.put(this.data.get(globalIndex * this.size + j));
			}
			this.subBuf.flip();
			
			alBufferData(buf, this.format, this.subBuf, this.frequency);
		}
	}
	
	public void flip() {
		this.position++;
		
		if(this.reachedTheEnd()) return;
		
		int first = this.bufferIDs[0];
		
		for(int i = 0; i < this.bufferIDs.length - 1; i++)
			this.bufferIDs[i] = this.bufferIDs[i + 1];
		
		this.bufferIDs[this.bufferIDs.length - 1] = first;
		this.fillBuffer(this.bufferIDs.length - 1);
	}
	
	public int getBufferID(int i) {
		return this.bufferIDs[i];
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
		
		// Setup the format
		this.frequency = freq;
		this.bits = 0;
		this.channels = 0;
		this.size = 0;
		
		switch(format) {
			case AL_FORMAT_MONO8:
				this.bits = 8;
				this.channels = 1;
				break;
			case AL_FORMAT_MONO16:
				this.bits = 16;
				this.channels = 1;
				break;
			case AL_FORMAT_STEREO8:
				this.bits = 8;
				this.channels = 2;
				break;
			case AL_FORMAT_STEREO16:
				this.bits = 16;
				this.channels = 2;
				break;
		}
		this.size = BUFFER_SIZE * this.channels * (this.bits / 8);
		this.sampleSize = (this.size / this.channels) / (this.bits / 8);
		this.format = format;
		if(this.bits == 16) this.sampleSize /= 2;
		
		if(this.subBuf == null || this.subBuf.capacity() != this.size) this.subBuf = BufferUtils.createByteBuffer(this.size);
		
		int lastBufSize = data.remaining() % this.size;
		if(lastBufSize > 0) {
			if(
				this.lastSubBuf == null ||
				this.lastSubBuf.capacity() != this.size) this.lastSubBuf = BufferUtils.createByteBuffer(lastBufSize);
		} else this.lastSubBuf = null;
		
		this.bufferCount = data.remaining() / this.size + (lastBufSize == 0 ? 0 : 1);
		
		// Fill the buffers
		for(int i = 0; i < this.bufferIDs.length; i++) {
			this.fillBuffer(i);
		}
	}
	
	public void dispose() {
		for(int i = 0; i < this.bufferIDs.length; i++) {
			alDeleteBuffers(this.bufferIDs[i]);
			this.bufferIDs[i] = 0;
		}
	}
}
