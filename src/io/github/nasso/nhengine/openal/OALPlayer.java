package io.github.nasso.nhengine.openal;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.openal.AL10;

import io.github.nasso.nhengine.audio.AudioPlayer;
import io.github.nasso.nhengine.audio.Sound;
import io.github.nasso.nhengine.component.AudioSourceComponent;
import io.github.nasso.nhengine.level.Component;
import io.github.nasso.nhengine.level.Level;
import io.github.nasso.nhengine.level.Node;
import io.github.nasso.nhengine.level.Scene;

public class OALPlayer implements AudioPlayer {
	public static final int QUICK_SOURCES_COUNT = 8;
	
	private OALSource[] quickSources = new OALSource[QUICK_SOURCES_COUNT];
	private int[] quickSourcesLastUse = new int[QUICK_SOURCES_COUNT];
	private int useCounter = 0;
	
	private List<AudioSourceComponent> componentsPool = new ArrayList<AudioSourceComponent>();
	
	public OALPlayer() {
		for(int i = 0; i < QUICK_SOURCES_COUNT; i++) {
			this.quickSources[i] = new OALSource();
			this.quickSources[i].setMinGain(0);
			this.quickSources[i].setMaxGain(1);
			this.quickSources[i].setGain(1);
			this.quickSources[i].setPitch(1);
			this.quickSources[i].setLooping(false);
		}
	}
	
	public void dispose() {
		for(int i = 0; i < QUICK_SOURCES_COUNT; i++) {
			this.quickSources[i].dispose();
		}
	}
	
	private void projectNode(Node n) {
		if(!n.isEnabled()) return;
		
		List<Component> comps = n.getComponents();
		for(int i = 0; i < comps.size(); i++) {
			Component c = comps.get(i);
			
			if(c instanceof AudioSourceComponent) {
				this.componentsPool.add((AudioSourceComponent) c);
			}
		}
		
		List<Node> children = n.getChildren();
		for(int i = 0; i < children.size(); i++)
			this.projectNode(children.get(i));
	}
	
	public void audioStep(Level lvl) {
		List<Scene> sceneList = lvl.getOverlayScenes();
		if(sceneList.isEmpty()) return;
		
		for(int s = 0; s < sceneList.size(); s++) {
			Scene sce = sceneList.get(s);
			
			if(!sce.getRoot().isEnabled()) continue;
			
			this.projectNode(sce.getRoot());
			
			for(int i = 0; i < this.componentsPool.size(); i++) {
				AudioSourceComponent comp = this.componentsPool.get(i);
				OALSources.get().update(comp);
			}
			
			this.componentsPool.clear();
		}
		
		OALManager.fastCheckError("audio step");
	}
	
	private OALSource findQuickAvailableSource() {
		// returns the oldest
		int oldest = 0;
		for(int i = 0; i < QUICK_SOURCES_COUNT; i++) {
			if(this.quickSources[i].getSourceState() != AL10.AL_PLAYING) {
				// if it's available, use it even if it's not the oldest
				this.quickSourcesLastUse[oldest] = this.useCounter++;
				return this.quickSources[i];
			}
			
			if(this.quickSourcesLastUse[i] < this.quickSourcesLastUse[oldest]) oldest = i;
		}
		
		// here, they're all playing!
		// we decide to kill the oldest, rip
		this.quickSourcesLastUse[oldest] = this.useCounter++; // a new baby is born
		if(this.quickSources[oldest].getSourceState() == AL10.AL_PLAYING) this.quickSources[oldest].stop();
		
		return this.quickSources[oldest];
	}
	
	/**
	 * Quickly play a sound. Useful for very short ones.<br>
	 * There's no way to pause or stop it.<br>
	 * If no source is available, kills the oldest.
	 */
	public void playSound(Sound snd, float volume, float pitch) {
		OALSource src = this.findQuickAvailableSource();
		if(src == null) return;
		
		src.setGain(volume);
		src.setPitch(pitch);
		src.setBuffer(OALBuffers.get().get(snd));
		src.play();
	}
	
	public void playSound(Sound snd, float volume) {
		this.playSound(snd, volume, 1);
	}
	
	public void playSound(Sound snd) {
		this.playSound(snd, 1);
	}
}
