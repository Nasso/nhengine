package io.github.nasso.nhengine.level;

import java.util.ArrayList;
import java.util.List;

import io.github.nasso.nhengine.core.Disposable;
import io.github.nasso.nhengine.event.Observable;

public class Level extends Observable implements Disposable {
	private List<Scene> scenes = new ArrayList<Scene>();
	
	public Level() {
	}
	
	public void dispose() {
		this.triggerEvent("dispose");
	}
	
	public final void step(float delta) {
		this.update(delta);
		
		for(int i = 0; i < this.scenes.size(); i++) {
			this.scenes.get(i).step(delta);
		}
	}
	
	/**
	 * Called every frame
	 * 
	 * @param delta
	 *            in ms
	 */
	public void update(float delta) {
		
	}
	
	public void addOverlayScene(Scene sce) {
		this.scenes.add(sce);
	}
	
	public void addOverlayScene(int i, Scene sce) {
		this.scenes.add(i, sce);
	}
	
	public void removeOverlayScene(int i) {
		this.scenes.remove(i);
	}
	
	public void removeOverlayScene(Scene sce) {
		this.scenes.remove(sce);
	}
	
	public void clearOverlayScenes() {
		this.scenes.clear();
	}
	
	public Scene getOverlayScene(int i) {
		return this.scenes.get(i);
	}
	
	public List<Scene> getOverlayScenes() {
		return this.scenes;
	}
}
