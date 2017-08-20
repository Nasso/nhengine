package io.github.nasso.test.demo;

import java.io.IOException;

import org.joml.Vector2f;

import io.github.nasso.nhengine.component.TileMapComponent;
import io.github.nasso.nhengine.component.TileMapComponent.TileSet;
import io.github.nasso.nhengine.component.TiledSpriteComponent;
import io.github.nasso.nhengine.core.Game;
import io.github.nasso.nhengine.core.GameWindow;
import io.github.nasso.nhengine.data.TextureIO;
import io.github.nasso.nhengine.graphics.Texture2D;
import io.github.nasso.nhengine.level.Node;
import io.github.nasso.nhengine.level.Scene;
import io.github.nasso.nhengine.utils.CachedPerlin;

public class DemoWorldScene extends Scene {
	private TileMapComponent terrainBase;
	
	private int terrainSize = 64;
	
	private Node terrainNode;
	private Node player;
	private float playerHeight = 1f; // W / H
	
	private TileSet basicTiles;
	private Texture2D characterTiles;
	
	private TiledSpriteComponent characterTilesComp;
	
	private int grass;
	private int heavyGrass;
	private int water;
	
	private int tree;
	
	// Water borders:
	private int wgTop;
	private int wgBot;
	private int wgLeft;
	private int wgRight;
	// ..
	private int wgTopLeft;
	private int wgTopRight;
	private int wgBotLeft;
	private int wgBotRight;
	// ..
	private int wgTopLeftBot;
	private int wgTopRightBot;
	private int wgLeftBotRight;
	private int wgLeftTopRight;
	// ..
	private int wgLeftRight;
	private int wgTopBot;
	// ..
	private int wgAll;
	// ---------------------------------------------
	
	private Vector2f frameSize = new Vector2f();
	
	public DemoWorldScene() {
		super("World Scene");
		
		this.terrainBase = new TileMapComponent(this.terrainSize, this.terrainSize, 1, 1);
		TileMapComponent.Layer baseLayer = this.terrainBase.createLayer();
		TileMapComponent.Layer decorationLayer = this.terrainBase.createLayer();
		
		try {
			this.basicTiles = this.terrainBase.createTileSet(TextureIO.loadTexture2D("res/demo/textures/basictiles.png", 4, false, false, false, true), 8, 14);
			
			this.characterTiles = TextureIO.loadTexture2D("res/demo/textures/cyber_kid.png", 4, false, false, false, true);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		this.tree = this.basicTiles.getTileID(6, 4);
		this.grass = this.basicTiles.getTileID(0, 8);
		this.heavyGrass = this.basicTiles.getTileID(1, 8);
		this.water = this.basicTiles.getTileID(5, 1);
		// ..
		this.wgTop = this.basicTiles.getTileID(1, 3);
		this.wgBot = this.basicTiles.getTileID(1, 5);
		this.wgLeft = this.basicTiles.getTileID(0, 4);
		this.wgRight = this.basicTiles.getTileID(2, 4);
		// ..
		this.wgTopLeft = this.basicTiles.getTileID(0, 3);
		this.wgTopRight = this.basicTiles.getTileID(2, 3);
		this.wgBotLeft = this.basicTiles.getTileID(0, 5);
		this.wgBotRight = this.basicTiles.getTileID(2, 5);
		// ..
		this.wgTopLeftBot = this.basicTiles.getTileID(0, 12);
		this.wgTopRightBot = this.basicTiles.getTileID(1, 12);
		this.wgLeftBotRight = this.basicTiles.getTileID(0, 13);
		this.wgLeftTopRight = this.basicTiles.getTileID(1, 13);
		// ..
		this.wgLeftRight = this.basicTiles.getTileID(2, 13);
		this.wgTopBot = this.basicTiles.getTileID(2, 12);
		// ..
		this.wgAll = this.basicTiles.getTileID(2, 11);
		
		this.characterTilesComp = new TiledSpriteComponent(this.characterTiles, 4, 4, 0, 2);
		this.characterTilesComp.setOpaque(false);
		
		this.playerHeight = (float) this.characterTiles.getHeight() / this.characterTiles.getWidth();
		
		Node root = this.getRoot();
		
		CachedPerlin p = new CachedPerlin(0, 0.2f, 0.4f, this.terrainSize, this.terrainSize);
		
		float waterLevel = 0.3f;
		float grassLevel = 0.7f;
		float treeLevel = 0.75f;
		
		for(int x = 0; x < this.terrainSize; x++) {
			for(int y = 0; y < this.terrainSize; y++) {
				float c = p.getValueAt(x, y);
				
				if(c < waterLevel) baseLayer.setTileAt(x, y, this.water);
				else if(c < grassLevel) baseLayer.setTileAt(x, y, this.grass);
				else if(c < treeLevel) baseLayer.setTileAt(x, y, this.heavyGrass);
				else baseLayer.setTileAt(x, y, this.grass);
				
				if(c < waterLevel) { // water
					boolean ct = p.getValueAt(x, y - 1) > waterLevel;
					boolean cb = p.getValueAt(x, y + 1) > waterLevel;
					boolean cl = p.getValueAt(x - 1, y) > waterLevel;
					boolean cr = p.getValueAt(x + 1, y) > waterLevel;
					
					if(ct && cb && cl && cr) decorationLayer.setTileAt(x, y, this.wgAll);
					else if(ct && cl && cb && !cr) decorationLayer.setTileAt(x, y, this.wgTopLeftBot);
					else if(ct && !cl && cb && cr) decorationLayer.setTileAt(x, y, this.wgTopRightBot);
					else if(!ct && cl && cb && cr) decorationLayer.setTileAt(x, y, this.wgLeftBotRight);
					else if(ct && cl && !cb && cr) decorationLayer.setTileAt(x, y, this.wgLeftTopRight);
					else if(!ct && cl && !cb && cr) decorationLayer.setTileAt(x, y, this.wgLeftRight);
					else if(ct && !cl && cb && !cr) decorationLayer.setTileAt(x, y, this.wgTopBot);
					else if(ct && cl && !cb && !cr) decorationLayer.setTileAt(x, y, this.wgTopLeft);
					else if(ct && !cl && !cb && cr) decorationLayer.setTileAt(x, y, this.wgTopRight);
					else if(!ct && cl && cb && !cr) decorationLayer.setTileAt(x, y, this.wgBotLeft);
					else if(!ct && !cl && cb && cr) decorationLayer.setTileAt(x, y, this.wgBotRight);
					else if(ct && !cl && !cb && !cr) decorationLayer.setTileAt(x, y, this.wgTop);
					else if(!ct && cl && !cb && !cr) decorationLayer.setTileAt(x, y, this.wgLeft);
					else if(!ct && !cl && cb && !cr) decorationLayer.setTileAt(x, y, this.wgBot);
					else if(!ct && !cl && !cb && cr) decorationLayer.setTileAt(x, y, this.wgRight);
				} else if(c > treeLevel) {
					decorationLayer.setTileAt(x, y, this.tree);
				}
			}
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
		this.getCamera().setFieldOfView(6);
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
		
		this.basicTiles.dispose();
		this.characterTiles.dispose();
	}
}
