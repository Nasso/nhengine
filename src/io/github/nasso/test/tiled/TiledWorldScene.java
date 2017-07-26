package io.github.nasso.test.tiled;

import java.io.IOException;

import org.joml.Vector2f;

import io.github.nasso.nhengine.component.SpriteComponent;
import io.github.nasso.nhengine.component.TileMapComponent;
import io.github.nasso.nhengine.component.TiledSpriteComponent;
import io.github.nasso.nhengine.core.Game;
import io.github.nasso.nhengine.core.GameWindow;
import io.github.nasso.nhengine.data.TextureIO;
import io.github.nasso.nhengine.data.TiledFormatLoader;
import io.github.nasso.nhengine.graphics.Texture2D;
import io.github.nasso.nhengine.level.Node;
import io.github.nasso.nhengine.level.Scene;

public class TiledWorldScene extends Scene {
	private TileMapComponent terrainBase;
	private TileMapComponent terrainDecorationLayer;
	
	private int terrainSize = 64;
	
	private Node terrainNode;
	private Node player;
	private float playerHeight = 5f; // W / H
	
	private Texture2D basicTiles;
	private Texture2D characterTiles;
	
	private TiledSpriteComponent characterTilesComp;

	// ---------------------------------------------
	
	private Vector2f frameSize = new Vector2f();
	
	public TiledWorldScene() {
		super("World Scene");
		
		try {
			this.characterTiles = TextureIO.loadTexture2D("res/demo/textures/cyber_kid.png", 4, false, false, false, true);
		} catch(IOException e) {
			e.printStackTrace();
		}

		this.characterTilesComp = new TiledSpriteComponent(this.characterTiles, 4, 4, 0, 2);
		this.characterTilesComp.setOpaque(false);
		
		this.playerHeight = (float) this.characterTiles.getHeight() / this.characterTiles.getWidth();
		
		Node root = this.getRoot();

		try {
			this.terrainBase = TiledFormatLoader.parseTiledJSON("C:/Users/Kadau/Desktop/testjson.json");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		this.terrainBase.setDepth(0);
		this.player = new Node();
		this.player.setDepth(2);
		this.player.setPosition(2, 2);
		this.player.addComponent(this.characterTilesComp);
		this.characterTilesComp.setScale(1, this.playerHeight);
		
		this.terrainNode = new Node();
		this.terrainNode.setPosition(-this.terrainSize / 2f, -this.terrainSize / 2f);
		this.terrainNode.addComponents(this.terrainBase);
		
		root.addChild(this.terrainNode);
		root.addChild(this.player);
		this.getCamera().setScale(80);
		
	}
	
	public void update(float delta) {
		GameWindow win = Game.instance().window();
		
		if(win.getFrameWidth() != this.frameSize.x || win.getFrameHeight() != this.frameSize.y) {
			this.frameSize.set(win.getFrameWidth(), win.getFrameHeight());
			
			this.getCamera().setAspectRatio(this.frameSize.x, this.frameSize.y);
		}
	}
	
	public Node getPlayer() {
		return this.player;
	}
	
	public TiledSpriteComponent getPlayerTiledSprite() {
		return this.characterTilesComp;
	}
	
	public void dispose() {
		super.dispose();

		this.characterTiles.dispose();
	}
}
