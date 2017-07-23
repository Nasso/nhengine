package io.github.nasso.nhengine.audio;

import io.github.nasso.nhengine.level.Level;

public abstract class AudioPlayer {
	public abstract void audioStep(Level lvl);
	
	public abstract void playSound(Sound snd, float volume, float pitch);
	
	public abstract void playSound(Sound snd, float volume);
	
	public abstract void playSound(Sound snd);
}
