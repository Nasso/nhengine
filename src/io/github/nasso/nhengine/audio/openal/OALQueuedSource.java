package io.github.nasso.nhengine.audio.openal;

import static java.lang.Math.*;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL11.*;

import org.joml.Vector3f;

public class OALQueuedSource {
	private int id;
	private float pitch;
	private float gain;
	private float minGain;
	private float maxGain;
	private Vector3f position = new Vector3f();
	private Vector3f velocity = new Vector3f();
	private Vector3f direction = new Vector3f();
	private boolean looping;
	private OALBufferQueue queue;
	
	private boolean playing = false;
	
	private int version = -1;
	
	public OALQueuedSource() {
		this.id = alGenSources();
		
		this.pitch = alGetSourcef(this.id, AL_PITCH);
		this.gain = alGetSourcef(this.id, AL_GAIN);
		this.minGain = alGetSourcef(this.id, AL_MIN_GAIN);
		this.maxGain = alGetSourcef(this.id, AL_MAX_GAIN);
		
		float[] v3 = new float[3];
		alGetSourcefv(this.id, AL_POSITION, v3);
		this.position.set(v3[0], v3[1], v3[2]);
		alGetSourcefv(this.id, AL_VELOCITY, v3);
		this.velocity.set(v3[0], v3[1], v3[2]);
		alGetSourcefv(this.id, AL_DIRECTION, v3);
		this.direction.set(v3[0], v3[1], v3[2]);
		
		this.looping = alGetSourcei(this.id, AL_LOOPING) == AL_TRUE;
		this.queue = null;
	}
	
	public void play() {
		alSourcePlay(this.id);
		this.playing = true;
	}
	
	public void pause() {
		if(this.playing) {
			alSourcePause(this.id);
			this.playing = false;
		}
	}
	
	public void stop() {
		if(this.playing) {
			alSourceStop(this.id);
			this.playing = false;
		}
	}
	
	public void rewind() {
		alSourceRewind(this.id);
	}
	
	public void setPitch(float pitch) {
		if(this.pitch == pitch) return;
		
		this.pitch = pitch;
		alSourcef(this.id, AL_PITCH, pitch);
	}
	
	public void setGain(float gain) {
		if(this.gain == gain) return;
		
		this.gain = gain;
		alSourcef(this.id, AL_GAIN, gain);
	}
	
	public void setMinGain(float minGain) {
		if(this.minGain == minGain) return;
		
		this.minGain = minGain;
		alSourcef(this.id, AL_MIN_GAIN, minGain);
	}
	
	public void setMaxGain(float maxGain) {
		if(this.maxGain == maxGain) return;
		
		this.maxGain = maxGain;
		alSourcef(this.id, AL_MAX_GAIN, maxGain);
	}
	
	public void setPosition(float x, float y, float z) {
		if(this.position.x == x && this.position.y == y && this.position.z == z) return;
		
		this.position.set(x, y, z);
		alSource3f(this.id, AL_POSITION, x, y, z);
	}
	
	public void setVelocity(float x, float y, float z) {
		if(this.velocity.x == x && this.velocity.y == y && this.velocity.z == z) return;
		
		this.velocity.set(x, y, z);
		alSource3f(this.id, AL_VELOCITY, x, y, z);
	}
	
	public void setDirection(float x, float y, float z) {
		if(this.direction.x == x && this.direction.y == y && this.direction.z == z) return;
		
		this.direction.set(x, y, z);
		alSource3f(this.id, AL_DIRECTION, x, y, z);
	}
	
	public void setLooping(boolean looping) {
		if(this.looping == looping) return;
		
		this.looping = looping;
		alSourcei(this.id, AL_LOOPING, looping ? AL_TRUE : AL_FALSE);
	}
	
	public void step() {
		int processed = alGetSourcei(this.id, AL_BUFFERS_PROCESSED);
		
		while(processed > 0) {
			int buf = alSourceUnqueueBuffers(this.id);
			this.queue.flip();
			
			if(!this.queue.reachedTheEnd()) alSourceQueueBuffers(this.id, buf);
			
			processed--;
		}
	}
	
	public void setBuffer(OALBufferQueue buffer) {
		if(this.queue == buffer) return;
		
		this.queue = buffer;
		
		if(buffer != null) {
			for(int i = 0; i < OALBufferQueue.QUEUE_SIZE; i++) {
				alSourceQueueBuffers(this.id, buffer.getBufferID(i));
			}
		} else alSourcei(this.id, AL_BUFFER, 0);
		
		this.setCurrentTime(0);
	}
	
	public void setCurrentTime(float currentTime) {
		if(this.queue != null) alSourcei(this.id, AL_SAMPLE_OFFSET, (int) min(this.queue.getFrequency() * currentTime, this.queue.getSampleSize()));
	}
	
	public float getCurrentTime() {
		return (float) alGetSourcei(this.id, AL_SAMPLE_OFFSET) / this.queue.getFrequency();
	}
	
	public int getID() {
		return this.id;
	}
	
	public float getPitch() {
		return this.pitch;
	}
	
	public float getGain() {
		return this.gain;
	}
	
	public float getMinGain() {
		return this.minGain;
	}
	
	public float getMaxGain() {
		return this.maxGain;
	}
	
	public Vector3f getPosition() {
		return this.position;
	}
	
	public Vector3f getVelocity() {
		return this.velocity;
	}
	
	public Vector3f getDirection() {
		return this.direction;
	}
	
	public boolean isLooping() {
		return this.looping;
	}
	
	public OALBufferQueue getQueue() {
		return this.queue;
	}
	
	public int getSourceState() {
		return alGetSourcei(this.id, AL_SOURCE_STATE);
	}
	
	public boolean isValid() {
		return alIsSource(this.id);
	}
	
	public void dispose() {
		alDeleteSources(this.id);
		this.id = 0;
	}
	
	public int getVersion() {
		return this.version;
	}
	
	public void setVersion(int version) {
		this.version = version;
	}
}
