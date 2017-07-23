package io.github.nasso.nhengine.data;

import java.io.IOException;

import io.github.nasso.nhengine.audio.Sound;

public interface AudioFormatLoader {
	public Sound load(String fileName, boolean inJar) throws IOException;
}
