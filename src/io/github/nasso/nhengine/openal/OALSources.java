package io.github.nasso.nhengine.openal;

import static org.lwjgl.openal.AL10.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import io.github.nasso.nhengine.component.AudioSourceComponent;
import io.github.nasso.nhengine.event.Observable;

public class OALSources {
	private Map<AudioSourceComponent, OALSource> sources = new HashMap<AudioSourceComponent, OALSource>();
	
	private Consumer<Observable> srcDisposer;
	
	private static OALSources INSTANCE;
	
	public static OALSources get() {
		return INSTANCE != null ? INSTANCE : (INSTANCE = new OALSources());
	}
	
	public static void dispose() {
		if(INSTANCE != null) INSTANCE.disposeAll();
	}
	
	private OALSources() {
		this.srcDisposer = (src) -> {
			if(this.sources.containsKey(src)) {
				this.sources.get(src).dispose();
				this.sources.remove(src);
			}
			
			src.removeEventListener("dispose", this.srcDisposer);
		};
	}
	
	private void disposeAll() {
		for(AudioSourceComponent s : this.sources.keySet()) {
			OALSource src = this.sources.get(s);
			
			if(src != null) src.dispose();
		}
		
		this.sources.clear();
	}
	
	public OALSource update(AudioSourceComponent src) {
		if(src == null) return null;
		
		OALSource oalSrc = this.sources.get(src);
		
		boolean enabled = src.isEnabled();
		if(oalSrc == null && enabled) {
			oalSrc = new OALSource();
			src.addEventListener("dispose", this.srcDisposer);
			this.sources.put(src, oalSrc);
		} else if(oalSrc != null && !enabled) {
			oalSrc.stop();
		}
		
		if(oalSrc.getVersion() != src.getVersion()) {
			oalSrc.setPitch(src.getPitch());
			oalSrc.setGain(src.getGain());
			oalSrc.setMinGain(src.getMinGain());
			oalSrc.setMaxGain(src.getMaxGain());
			oalSrc.setLooping(src.isLooping());
			oalSrc.setBuffer(OALBuffers.get().get(src.getSoundBuffer()));
			
			switch(src.getStatus()) {
				case PAUSING:
					oalSrc.pause();
					src.setStatus(AudioSourceComponent.Status.PAUSED);
					break;
				case PLAYING:
					if(oalSrc.getSourceState() != AL_PLAYING) oalSrc.play();
					break;
				case RESTARTING:
					oalSrc.play(); // (it restarts again, see the spec)
					src.setStatus(AudioSourceComponent.Status.PLAYING);
					break;
				case STOPPING:
					oalSrc.stop();
					src.setStatus(AudioSourceComponent.Status.STOPPED);
					break;
				default:
					break;
			}
			
			oalSrc.setVersion(src.getVersion());
		}
		
		return oalSrc;
	}
}
