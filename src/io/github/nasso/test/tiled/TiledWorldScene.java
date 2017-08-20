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
	
	private float cameraSpeed = 1.0f / 20.0f;
	private float cameraX = 0;
	private float cameraY = 0;
	
	private Node terrainNode;
	
	private Vector2f frameSize = new Vector2f();
	
	public TiledWorldScene() {
		super("World Scene");
		
		Node root = this.getRoot();
		
		try {
			this.terrainBase = TiledFormatLoader.loadJSON("res/demo/maps/orth/map.json", true);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		this.terrainBase.setDepth(0);
		
		this.terrainNode = new Node();
		this.terrainNode.setPosition(-this.terrainBase.getMapSizeX() * this.terrainBase.getCellWidth() * 0.5f, -this.terrainBase.getMapSizeY() * this.terrainBase.getCellHeight() * 0.5f);
		this.terrainNode.addComponents(this.terrainBase);
		
		root.addChild(this.terrainNode);
		this.getCamera().setFieldOfView(this.terrainBase.getCellWidth() * 8);
	}
	
	public void update(float delta) {
		GameWindow win = Game.instance().window();
		
		if(win.getFrameWidth() != this.frameSize.x || win.getFrameHeight() != this.frameSize.y) {
			this.frameSize.set(win.getFrameWidth(), win.getFrameHeight());
			
			this.getCamera().setAspectRatio(this.frameSize.x, this.frameSize.y);
		}
		
		if(win.isKeyDown(Nhengine.KEY_D)) {
			this.cameraX += this.cameraSpeed * delta;
		}
		
		if(win.isKeyDown(Nhengine.KEY_A)) {
			this.cameraX -= this.cameraSpeed * delta;
		}
		
		if(win.isKeyDown(Nhengine.KEY_W)) {
			this.cameraY -= this.cameraSpeed * delta;
		}
		
		if(win.isKeyDown(Nhengine.KEY_S)) {
			this.cameraY += this.cameraSpeed * delta;
		}
		
		this.getCamera().setPosition(Math.round(this.cameraX), Math.round(this.cameraY));
	}
	
	public void dispose() {
		super.dispose();
	}
}
