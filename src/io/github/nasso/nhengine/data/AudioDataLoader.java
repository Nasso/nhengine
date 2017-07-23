package io.github.nasso.nhengine.data;

import java.io.IOException;

import io.github.nasso.nhengine.audio.Sound;

public class AudioDataLoader {
	public enum AudioFormat {
		VORBIS_OGG, WAVE
	}
	
	private static AudioFormatLoader oggVorbisLoader, waveLoader;
	
	public static final AudioFormat getAudioFormatByFileName(String fileName) {
		if(fileName.endsWith(".ogg")) return AudioFormat.VORBIS_OGG;
		if(fileName.endsWith(".wav")) return AudioFormat.WAVE;
		
		return null;
	}
	
	public static final AudioFormatLoader getSupportedFormatLoader(AudioFormat fmt) {
		switch(fmt) {
			case VORBIS_OGG:
				return oggVorbisLoader == null ? (oggVorbisLoader = new OggVorbisFormatLoader()) : oggVorbisLoader;
			case WAVE:
				return waveLoader == null ? (waveLoader = new WaveFormatLoader()) : waveLoader;
			default:
				System.err.println("Unsupported audio data format: " + fmt);
				return null;
		}
	}
	
	public static final AudioFormatLoader getSupportedFormatLoader(String fileName) {
		return getSupportedFormatLoader(getAudioFormatByFileName(fileName));
	}
	
	public static final Sound load(String fileName, boolean inJar) throws IOException {
		return getSupportedFormatLoader(fileName).load(fileName, inJar);
	}
	
	private AudioDataLoader() {
	}
}
