package io.github.nasso.nhengine.component;

import java.util.ArrayList;
import java.util.List;

import io.github.nasso.nhengine.graphics.Texture2D;
import io.github.nasso.nhengine.utils.Box2D;

public class TileMapComponent extends DrawableComponent {
	public static class TileSet {
		private Texture2D texture;
		private int columns, rows;
		
		private int firstgid;
		
		private TileSet(Texture2D texture, int cols, int rows, int firstgid) {
			this.texture = texture;
			this.columns = cols;
			this.rows = rows;
			
			this.firstgid = firstgid;
		}
		
		public boolean containsGlobalID(int id) {
			return id >= 0 && id < this.getTileCount();
		}
		
		public int getTileID(int x, int y) {
			if(x < 0 || x >= this.columns || y < 0 || y > this.rows) return -1;
			
			return this.firstgid + y * this.columns + x;
		}
		
		public int getTileColumn(int id) {
			if(!this.containsGlobalID(id)) return -1;
			id -= this.firstgid;
			
			return id % this.columns;
		}
		
		public int getTileRow(int id) {
			if(!this.containsGlobalID(id)) return -1;
			id -= this.firstgid;
			
			return id / this.columns;
		}
		
		public Texture2D getTexture() {
			return this.texture;
		}
		
		public int getColumns() {
			return this.columns;
		}
		
		public int getRows() {
			return this.rows;
		}
		
		public void dispose() {
			this.texture.dispose();
		}
		
		public int getFirstGlobalID() {
			return this.firstgid;
		}
		
		public int getTileCount() {
			return this.columns * this.rows;
		}
	}
	
	public static class Layer {
		private boolean visible;
		
		private int width, height;
		
		private float opacity;
		private float horizontalOffset;
		private float verticalOffset;
		
		private int[] data;
		
		private Layer(int width, int height) {
			this.data = new int[width * height];
			
			for(int i = 0; i < this.data.length; i++)
				this.data[i] = -1;
			
			this.visible = true;
			
			this.width = width;
			this.height = height;
			
			this.opacity = 1.0f;
			this.horizontalOffset = 0.0f;
			this.verticalOffset = 0.0f;
		}
		
		public float getOpacity() {
			return this.opacity;
		}
		
		public void setOpacity(float opacity) {
			this.opacity = opacity;
		}
		
		public float getHorizontalOffset() {
			return this.horizontalOffset;
		}
		
		public void setHorizontalOffset(float horizontalOffset) {
			this.horizontalOffset = horizontalOffset;
		}
		
		public float getVerticalOffset() {
			return this.verticalOffset;
		}
		
		public void setVerticalOffset(float verticalOffset) {
			this.verticalOffset = verticalOffset;
		}
		
		public int[] getData() {
			return this.data;
		}
		
		public void setTileAt(int x, int y, int tileID) {
			if(x < 0 || x >= this.width || y < 0 || y >= this.height) return;
			
			this.data[y * this.width + x] = tileID;
		}
		
		public int getTileAt(int x, int y) {
			if(x < 0 || x >= this.width || y < 0 || y >= this.height) return -1;
			
			return this.data[y * this.width + x];
		}
		
		public int[] getTiles() {
			return this.data;
		}
		
		public boolean isVisible() {
			return this.visible;
		}
		
		public void setVisible(boolean visible) {
			this.visible = visible;
		}
	}
	
	private List<TileSet> tilesets = new ArrayList<TileSet>();
	private List<Layer> layers = new ArrayList<Layer>();
	
	private int width, height;
	private float tileWidth, tileHeight;
	
	private boolean isometric = false;
	
	private Box2D boundingBox = new Box2D();
	
	public TileMapComponent(int width, int height, float tileWidth, float tileHeight) {
		this.width = width;
		this.height = height;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.setOpaque(false);
		
		this.computeBoundingBox();
	}
	
	public TileSet createTileSet(Texture2D tex, int cols, int rows) {
		int firstgid = 0;
		
		if(!this.tilesets.isEmpty()) {
			TileSet previous = this.tilesets.get(this.tilesets.size() - 1);
			firstgid = previous.firstgid + previous.getTileCount();
		}
		
		TileSet set = new TileSet(tex, cols, rows, firstgid);
		this.tilesets.add(set);
		
		return set;
	}
	
	public TileSet getTileSet(int id) {
		if(id < 0 || id >= this.getTileSetCount()) return null;
		
		return this.tilesets.get(id);
	}
	
	public int getTileSetCount() {
		return this.tilesets.size();
	}
	
	public Layer createLayer() {
		Layer layer = new Layer(this.width, this.height);
		this.layers.add(layer);
		
		return layer;
	}
	
	public Layer getLayer(int id) {
		if(id < 0 || id >= this.getLayerCount()) return null;
		
		return this.layers.get(id);
	}
	
	public int getLayerCount() {
		return this.layers.size();
	}
	
	public TileSet getTileSetForGlobalTileID(int tileID) {
		if(tileID < 0) return null;
		
		for(int i = 0; i < this.getTileSetCount(); i++) {
			TileSet set = this.getTileSet(i);
			if(tileID < set.firstgid + set.getTileCount()) return set;
		}
		
		return null;
	}
	
	public int getMapSizeX() {
		return this.width;
	}
	
	public int getMapSizeY() {
		return this.height;
	}
	
	public float getCellWidth() {
		return this.tileWidth;
	}
	
	public float getCellHeight() {
		return this.tileHeight;
	}
	
	private void computeBoundingBox() {
		this.boundingBox.redefine(-this.width / 2.0f * this.tileWidth, -this.height / 2.0f * this.tileHeight, this.width / 2.0f * this.tileWidth, -this.height / 2.0f * this.tileHeight);
	}
	
	public Box2D getBoundingBox() {
		return this.boundingBox;
	}
	
	public boolean isIsometric() {
		return this.isometric;
	}
	
	public void setIsometric(boolean isometric) {
		if(this.isometric == isometric) return;
		
		this.isometric = isometric;
		this.computeBoundingBox();
	}
}
