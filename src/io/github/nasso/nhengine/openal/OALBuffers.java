package io.github.nasso.nhengine.openal;

import static org.lwjgl.openal.AL10.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import io.github.nasso.nhengine.audio.Sound;
import io.github.nasso.nhengine.event.Observable;

public class OALBuffers {
	private Map<Sound, OALBuffer> buffers = new HashMap<Sound, OALBuffer>();
	
	private Consumer<Observable> bufDisposer;
	
	private static OALBuffers INSTANCE;
	
	public static OALBuffers get() {
		return INSTANCE != null ? INSTANCE : (INSTANCE = new OALBuffers());
	}
	
	public static void dispose() {
		if(INSTANCE != null) INSTANCE.disposeAll();
	}
	
	private OALBuffers() {
		this.bufDisposer = (buf) -> {
			if(this.buffers.containsKey(buf)) {
				this.buffers.get(buf).dispose();
				this.buffers.remove(buf);
			}
			
			buf.removeEventListener("dispose", this.bufDisposer);
		};
	}
	
	private void disposeAll() {
		for(Sound b : this.buffers.keySet()) {
			OALBuffer buf = this.buffers.get(b);
			
			if(buf != null) buf.dispose();
		}
		
		this.buffers.clear();
	}
	
	private int getALFormat(Sound buf) {
		switch(buf.getChannels()) {
			case 1:
				switch(buf.getBitsPerSample()) {
					case 8:
						return AL_FORMAT_MONO8;
					case 16:
						return AL_FORMAT_MONO16;
				}
				break;
			case 2:
				switch(buf.getBitsPerSample()) {
					case 8:
						return AL_FORMAT_STEREO8;
					case 16:
						return AL_FORMAT_STEREO16;
				}
				break;
		}
		
		return 0;
	}
	
	public OALBuffer get(Sound buf) {
		if(buf == null) return null;
		
		OALBuffer oalBuf = this.buffers.get(buf);
		
		if(oalBuf == null) {
			int format = this.getALFormat(buf);
			if(format == 0) return null;
			
			oalBuf = new OALBuffer();
			oalBuf.setData(buf.getData(), format, buf.getSampleRate());
			
			buf.addEventListener("dispose", this.bufDisposer);
			this.buffers.put(buf, oalBuf);
		}
		
		return oalBuf;
	}
}
