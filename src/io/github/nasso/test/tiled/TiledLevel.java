package io.github.nasso.test.tiled;

import io.github.nasso.nhengine.level.Level;

public class TiledLevel extends Level {
	private TiledWorldScene worldScene = new TiledWorldScene();
	
	public TiledLevel() {
		this.addOverlayScene(this.worldScene);
	}
	
	public void update(float delta) {
	}
	
	public TiledWorldScene getWorldScene() {
		return this.worldScene;
	}
	
	public void dispose() {
		super.dispose();
		
		this.worldScene.dispose();
	}
}
