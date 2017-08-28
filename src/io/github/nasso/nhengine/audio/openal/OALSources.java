package io.github.nasso.nhengine.audio.openal;

import static org.lwjgl.openal.AL10.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import io.github.nasso.nhengine.component.AudioSourceComponent;
import io.github.nasso.nhengine.event.Observable;

public class OALSources {
	private Map<AudioSourceComponent, OALSource> sources = new HashMap<AudioSourceComponent, OALSource>();
	private Map<AudioSourceComponent, OALQueuedSource> queuedSources = new HashMap<AudioSourceComponent, OALQueuedSource>();
	
	private Consumer<Observable> srcDisposer, queuedSrcDisposer;
	
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
		
		this.queuedSrcDisposer = (src) -> {
			if(this.queuedSources.containsKey(src)) {
				this.queuedSources.get(src).dispose();
				this.queuedSources.remove(src);
			}
			
			src.removeEventListener("dispose", this.queuedSrcDisposer);
		};
	}
	
	private void disposeAll() {
		for(AudioSourceComponent s : this.queuedSources.keySet()) {
			OALQueuedSource src = this.queuedSources.get(s);
			
			if(src != null) src.dispose();
		}
		
		this.queuedSources.clear();
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
			oalSrc.setBuffer(OALBuffers.get().get(src.getSound()));
			
			switch(src.getStatus()) {
				case PAUSED:
					if(oalSrc.getSourceState() != AL_STOPPED) {
						oalSrc.stop();
						src.setStartTime(oalSrc.getCurrentTime());
					}
					break;
				case PLAYING:
					if(oalSrc.getSourceState() != AL_PLAYING) {
						oalSrc.setCurrentTime(src.getStartTime());
						oalSrc.play();
					}
					break;
				case STOPPED:
					if(oalSrc.getSourceState() != AL_STOPPED) {
						oalSrc.stop();
					}
					
					src.setStartTime(0);
					break;
			}
			
			oalSrc.setVersion(src.getVersion());
		}
		
		oalSrc.step();
		
		return oalSrc;
	}
	/*
	public OALQueuedSource updateQueued(AudioSourceComponent src) {
		if(src == null) return null;
		
		OALQueuedSource oalSrc = this.queuedSources.get(src);
		
		boolean enabled = src.isEnabled();
		if(oalSrc == null && enabled) {
			oalSrc = new OALQueuedSource();
			src.addEventListener("dispose", this.srcDisposer);
			this.queuedSources.put(src, oalSrc);
		} else if(oalSrc != null && !enabled) {
			oalSrc.stop();
		}
		
		if(oalSrc.getVersion() != src.getVersion()) {
			oalSrc.setPitch(src.getPitch());
			oalSrc.setGain(src.getGain());
			oalSrc.setMinGain(src.getMinGain());
			oalSrc.setMaxGain(src.getMaxGain());
			oalSrc.setLooping(src.isLooping());
			oalSrc.setCurrentTime(src.getCurrentTime());
			oalSrc.setBuffer(OALBuffers.get().getQueue(src.getSound()));

			switch(src.getStatus()) {
				case PAUSED:
					if(oalSrc.getSourceState() != AL_STOPPED) oalSrc.pause();
					break;
				case PLAYING:
					if(oalSrc.getSourceState() != AL_PLAYING) oalSrc.play();
					break;
				case STOPPED:
					if(oalSrc.getSourceState() != AL_STOPPED) oalSrc.stop();
					break;
				default:
					break;
			}
			
			oalSrc.setVersion(src.getVersion());
		}
		
		oalSrc.step();
		
		return oalSrc;
	}*/
}
