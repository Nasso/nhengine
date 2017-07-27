package io.github.nasso.test.tiled;

import java.io.IOException;

import org.joml.Vector2f;

import io.github.nasso.nhengine.component.TileMapComponent;
import io.github.nasso.nhengine.core.Game;
import io.github.nasso.nhengine.core.GameWindow;
import io.github.nasso.nhengine.core.Nhengine;
import io.github.nasso.nhengine.data.TiledFormatLoader;
import io.github.nasso.nhengine.level.Node;
import io.github.nasso.nhengine.level.Scene;

public class TiledWorldScene extends Scene {
	private TileMapComponent terrainBase;
	
	private int terrainSize = 64;
	
	private float cameraSpeed = 4.0f / 100.0f;
	
	private Node terrainNode;
	
	private Vector2f frameSize = new Vector2f();
	
	public TiledWorldScene() {
		super("World Scene");
		
		Node root = this.getRoot();
		
		try {
			this.terrainBase = TiledFormatLoader.loadJSON("res/demo/maps/map.json", true);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		this.terrainBase.setDepth(0);
		
		this.terrainNode = new Node();
		this.terrainNode.setPosition(-this.terrainSize / 2f, -this.terrainSize / 2f);
		this.terrainNode.addComponents(this.terrainBase);
		
		root.addChild(this.terrainNode);
		this.getCamera().setScale(128);
		
	}
	
	public void update(float delta) {
		GameWindow win = Game.instance().window();
		
		if(win.getFrameWidth() != this.frameSize.x || win.getFrameHeight() != this.frameSize.y) {
			this.frameSize.set(win.getFrameWidth(), win.getFrameHeight());
			
			this.getCamera().setAspectRatio(this.frameSize.x, this.frameSize.y);
		}
		
		if(win.isKeyDown(Nhengine.KEY_D)) {
			this.getCamera().translateX(this.cameraSpeed * delta);
		}
		
		if(win.isKeyDown(Nhengine.KEY_A)) {
			this.getCamera().translateX(-this.cameraSpeed * delta);
		}
		
		if(win.isKeyDown(Nhengine.KEY_W)) {
			this.getCamera().translateY(-this.cameraSpeed * delta);
		}
		
		if(win.isKeyDown(Nhengine.KEY_S)) {
			this.getCamera().translateY(this.cameraSpeed * delta);
		}
	}
	
	public void dispose() {
		super.dispose();
	}
}
