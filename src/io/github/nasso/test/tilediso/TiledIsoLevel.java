package io.github.nasso.test.tilediso;

import io.github.nasso.nhengine.level.Level;

public class TiledIsoLevel extends Level {
	private TiledIsoWorldScene worldScene = new TiledIsoWorldScene();
	
	public TiledIsoLevel() {
		this.addOverlayScene(this.worldScene);
	}
	
	public void update(float delta) {
	}
	
	public TiledIsoWorldScene getWorldScene() {
		return this.worldScene;
	}
	
	public void dispose() {
		super.dispose();
		
		this.worldScene.dispose();
	}
}
