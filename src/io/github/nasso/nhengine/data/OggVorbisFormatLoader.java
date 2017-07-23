package io.github.nasso.nhengine.data;

import static org.lwjgl.stb.STBVorbis.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBVorbisInfo;

import io.github.nasso.nhengine.audio.Sound;
import io.github.nasso.nhengine.utils.Nhutils;

public class OggVorbisFormatLoader implements AudioFormatLoader {
	public Sound load(String fileName, boolean inJar) throws IOException {
		byte[] dataArray = Nhutils.readFileBytes(fileName, inJar);
		ByteBuffer dataBuffer = BufferUtils.createByteBuffer(dataArray.length);
		dataBuffer.put(dataArray);
		dataBuffer.flip();
		dataArray = null;
		
		IntBuffer error = BufferUtils.createIntBuffer(1);
		long decoder = stb_vorbis_open_memory(dataBuffer, error, null);
		if(decoder == 0) {
			System.err.println("Couldn't load Ogg Vorbis file '" + fileName + "': " + error.get(0));
			return null;
		}
		
		ByteBuffer data;
		int channels;
		int sampleCount;
		int sampleRate;
		try(STBVorbisInfo info = STBVorbisInfo.malloc()) {
			stb_vorbis_get_info(decoder, info);
			channels = info.channels();
			sampleCount = stb_vorbis_stream_length_in_samples(decoder);
			sampleRate = info.sample_rate();
			
			data = BufferUtils.createByteBuffer(sampleCount * 2); // multiply by 2 bc short = 2 bytes
			data.limit(stb_vorbis_get_samples_short_interleaved(decoder, channels, data.asShortBuffer()) * channels);
			
			stb_vorbis_close(decoder);
		}
		
		return new Sound(data, channels, sampleRate, 16, sampleCount);
	}
}
