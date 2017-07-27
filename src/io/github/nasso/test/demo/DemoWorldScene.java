package io.github.nasso.test.demo;

import java.io.IOException;

import org.joml.Vector2f;

import io.github.nasso.nhengine.component.TileMapComponent;
import io.github.nasso.nhengine.component.TiledSpriteComponent;
import io.github.nasso.nhengine.core.Game;
import io.github.nasso.nhengine.core.GameWindow;
import io.github.nasso.nhengine.data.TextureIO;
import io.github.nasso.nhengine.data.TiledFormatLoader;
import io.github.nasso.nhengine.graphics.Texture2D;
import io.github.nasso.nhengine.level.Node;
import io.github.nasso.nhengine.level.Scene;
import io.github.nasso.nhengine.utils.CachedPerlin;

public class DemoWorldScene extends Scene {
	private TileMapComponent terrainBase;
	private TileMapComponent terrainDecorationLayer;
	
	private int terrainSize = 64;
	
	private Node terrainNode;
	private Node player;
	private float playerHeight = 1f; // W / H
	
	private Texture2D basicTiles;
	private Texture2D characterTiles;
	
	private TiledSpriteComponent characterTilesComp;
	
	private TiledSpriteComponent grass;
	private TiledSpriteComponent heavyGrass;
	private TiledSpriteComponent water;
	
	private TiledSpriteComponent tree;
	
	// Water borders:
	private TiledSpriteComponent wgTop;
	private TiledSpriteComponent wgBot;
	private TiledSpriteComponent wgLeft;
	private TiledSpriteComponent wgRight;
	// ..
	private TiledSpriteComponent wgTopLeft;
	private TiledSpriteComponent wgTopRight;
	private TiledSpriteComponent wgBotLeft;
	private TiledSpriteComponent wgBotRight;
	// ..
	private TiledSpriteComponent wgTopLeftBot;
	private TiledSpriteComponent wgTopRightBot;
	private TiledSpriteComponent wgLeftBotRight;
	private TiledSpriteComponent wgLeftTopRight;
	// ..
	private TiledSpriteComponent wgLeftRight;
	private TiledSpriteComponent wgTopBot;
	// ..
	private TiledSpriteComponent wgAll;
	// ---------------------------------------------
	
	private Vector2f frameSize = new Vector2f();
	
	public DemoWorldScene() {
		super("World Scene");
		
		try {
			this.basicTiles = TextureIO.loadTexture2D("res/demo/textures/basictiles.png", 4, false, false, false, true);
			
			this.characterTiles = TextureIO.loadTexture2D("res/demo/textures/cyber_kid.png", 4, false, false, false, true);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		this.tree = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 6, 4);
		this.grass = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 0, 8);
		this.heavyGrass = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 1, 8);
		this.water = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 5, 1);
		// ..
		this.wgTop = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 1, 3);
		this.wgBot = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 1, 5);
		this.wgLeft = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 0, 4);
		this.wgRight = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 2, 4);
		// ..
		this.wgTopLeft = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 0, 3);
		this.wgTopRight = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 2, 3);
		this.wgBotLeft = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 0, 5);
		this.wgBotRight = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 2, 5);
		// ..
		this.wgTopLeftBot = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 0, 12);
		this.wgTopRightBot = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 1, 12);
		this.wgLeftBotRight = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 0, 13);
		this.wgLeftTopRight = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 1, 13);
		// ..
		this.wgLeftRight = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 2, 13);
		this.wgTopBot = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 2, 12);
		// ..
		this.wgAll = new TiledSpriteComponent(DemoWorldScene.this.basicTiles, 8, 14, 2, 11);
		
		this.characterTilesComp = new TiledSpriteComponent(this.characterTiles, 4, 4, 0, 2);
		this.characterTilesComp.setOpaque(false);
		
		this.playerHeight = (float) this.characterTiles.getHeight() / this.characterTiles.getWidth();
		
		Node root = this.getRoot();
		
		CachedPerlin p = new CachedPerlin(0, 0.2f, 0.4f, this.terrainSize, this.terrainSize);
		
		float waterLevel = 0.3f;
		float grassLevel = 0.7f;
		float treeLevel = 0.75f;
		
		this.terrainBase = new TileMapComponent(this.terrainSize, this.terrainSize, 1, 1);
		
		this.terrainDecorationLayer = new TileMapComponent(this.terrainSize, this.terrainSize, 1, 1);
		
		for(int x = 0; x < this.terrainSize; x++) {
			for(int y = 0; y < this.terrainSize; y++) {
				float c = p.getValueAt(x, y);
				
				if(c < waterLevel) this.terrainBase.setSpriteComponent(x, y, DemoWorldScene.this.water);
				else if(c < grassLevel) this.terrainBase.setSpriteComponent(x, y, DemoWorldScene.this.grass);
				else if(c < treeLevel) this.terrainBase.setSpriteComponent(x, y, DemoWorldScene.this.heavyGrass);
				else this.terrainBase.setSpriteComponent(x, y, DemoWorldScene.this.grass);
				
				if(c < waterLevel) { // water
					boolean ct = p.getValueAt(x, y - 1) > waterLevel;
					boolean cb = p.getValueAt(x, y + 1) > waterLevel;
					boolean cl = p.getValueAt(x - 1, y) > waterLevel;
					boolean cr = p.getValueAt(x + 1, y) > waterLevel;
					
					if(ct && cb && cl && cr) this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.wgAll);
					else if(ct && cl && cb && !cr) this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.wgTopLeftBot);
					else if(ct && !cl && cb && cr) this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.wgTopRightBot);
					else if(!ct && cl && cb && cr) this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.wgLeftBotRight);
					else if(ct && cl && !cb && cr) this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.wgLeftTopRight);
					else if(!ct && cl && !cb && cr) this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.wgLeftRight);
					else if(ct && !cl && cb && !cr) this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.wgTopBot);
					else if(ct && cl && !cb && !cr) this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.wgTopLeft);
					else if(ct && !cl && !cb && cr) this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.wgTopRight);
					else if(!ct && cl && cb && !cr) this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.wgBotLeft);
					else if(!ct && !cl && cb && cr) this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.wgBotRight);
					else if(ct && !cl && !cb && !cr) this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.wgTop);
					else if(!ct && cl && !cb && !cr) this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.wgLeft);
					else if(!ct && !cl && cb && !cr) this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.wgBot);
					else if(!ct && !cl && !cb && cr) this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.wgRight);
				} else if(c > treeLevel) {
					this.terrainDecorationLayer.setSpriteComponent(x, y, DemoWorldScene.this.tree);
				}
			}
		}
		
		this.terrainBase.setDepth(0);
		this.terrainDecorationLayer.setDepth(1);
		
		this.player = new Node();
		this.player.setDepth(2);
		this.player.setPosition(2, 2);
		this.player.addComponent(this.characterTilesComp);
		this.characterTilesComp.setScale(1, this.playerHeight);
		
		this.terrainNode = new Node();
		this.terrainNode.setPosition(-this.terrainSize / 2f, -this.terrainSize / 2f);
		this.terrainNode.addComponents(this.terrainBase, this.terrainDecorationLayer);
		
		root.addChild(this.terrainNode);
		root.addChild(this.player);
		this.getCamera().setScale(6);
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
