package io.github.nasso.nhengine.component;

import java.util.ArrayList;
import java.util.List;

import io.github.nasso.nhengine.graphics.Texture2D;
import io.github.nasso.nhengine.utils.Box2D;

/**
 * A tile map component displays a map made with one of more tile-set(s) and one or more layer(s).
 * 
 * @author nasso
 */
public class TileMapComponent extends DrawableComponent {
	/**
	 * A tile-set consists of a texture, containing multiple "tiles", in a fixed-size grid.
	 * 
	 * @author nasso
	 */
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
		
		/**
		 * @param id
		 * @return True if the given global ID is in this tile-set.
		 */
		public boolean containsGlobalID(int id) {
			return id >= 0 && id < this.getTileCount();
		}
		
		/**
		 * Returns the global ID for the tile located at the specified location in the tile-set.
		 * The returned ID can then be used to reference this tile in the map.
		 * 
		 * @param x The column of the requested tile in the tile-set (first column is 0).
		 * @param y The row of the requested tile in the tile-set (first row is 0).
		 * @return The global tile ID of the requested tile.
		 */
		public int getTileID(int x, int y) {
			if(x < 0 || x >= this.columns || y < 0 || y > this.rows) return -1;
			
			return this.firstgid + y * this.columns + x;
		}
		
		/**
		 * Returns the column corresponding to the given global tile ID in this tile-set. If the given ID isn't in this tile-set, it returns -1.
		 * 
		 * @param id The global tile ID
		 * @return The column of the tile in the tile-set, or -1.
		 */
		public int getTileColumn(int id) {
			if(!this.containsGlobalID(id)) return -1;
			id -= this.firstgid;
			
			return id % this.columns;
		}

		/**
		 * Returns the row corresponding to the given global tile ID in this tile-set. If the given ID isn't in this tile-set, it returns -1.
		 * 
		 * @param id The global tile ID
		 * @return The row of the tile in the tile-set, or -1.
		 */
		public int getTileRow(int id) {
			if(!this.containsGlobalID(id)) return -1;
			id -= this.firstgid;
			
			return id / this.columns;
		}
		
		/**
		 * @return The current texture bound to this tile-set.
		 */
		public Texture2D getTexture() {
			return this.texture;
		}
		
		/**
		 * @return The number of columns in this tile-set.
		 */
		public int getColumns() {
			return this.columns;
		}
		
		/**
		 * @return The number of rows in this tile-set.
		 */
		public int getRows() {
			return this.rows;
		}
		
		/**
		 * Disposes this tile-set. <strong>The texture currently attached to it is also disposed!</strong><br>
		 * Actually the only thing it does is disposing the texture.
		 */
		public void dispose() {
			this.texture.dispose();
		}
		
		/**
		 * @return The global ID of the first tile present in this tile-set.
		 */
		public int getFirstGlobalID() {
			return this.firstgid;
		}
		
		/**
		 * @return The number of tiles in this tile-set. It is equal to <code>columns * rows</code>.
		 */
		public int getTileCount() {
			return this.columns * this.rows;
		}
	}
	
	/**
	 * A layer contains an array of global IDs to tiles. A map can be composed of multiple layers but they're all the same size.
	 * @author nasso
	 */
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
		
		/**
		 * @return This layer's opacity.
		 */
		public float getOpacity() {
			return this.opacity;
		}
		
		/**
		 * @param opacity The new layer opacity.
		 */
		public void setOpacity(float opacity) {
			this.opacity = opacity;
		}
		
		/**
		 * @return This layer's horizontal offset.
		 */
		public float getHorizontalOffset() {
			return this.horizontalOffset;
		}
		
		/**
		 * @param horizontalOffset The new horizontal offset of this layer.
		 */
		public void setHorizontalOffset(float horizontalOffset) {
			this.horizontalOffset = horizontalOffset;
		}

		
		/**
		 * @return This layer's vertical offset.
		 */
		public float getVerticalOffset() {
			return this.verticalOffset;
		}

		
		/**
		 * @param horizontalOffset The new vertical offset of this layer.
		 */
		public void setVerticalOffset(float verticalOffset) {
			this.verticalOffset = verticalOffset;
		}

		/**
		 * The data array is a 1 dimensional array containing tiles global IDs arranged by rows.<br>
		 * The array returned is a direct reference to the data of this layer: all modification to this array will be visible on the map immediately.
		 * 
		 * @return This layer's data.
		 */
		public int[] getData() {
			return this.data;
		}
		
		/**
		 * Sets the tile ID at the given position in this layer. If the given coordinates are out of range, nothing happens.
		 * 
		 * @param x The column
		 * @param y The row
		 * @param tileID The global tile ID
		 */
		public void setTileAt(int x, int y, int tileID) {
			if(x < 0 || x >= this.width || y < 0 || y >= this.height) return;
			
			this.data[y * this.width + x] = tileID;
		}
		
		/**
		 * Returns the global tile ID at the given position or -1 if the given coordinates are out of range.
		 * 
		 * @param x The column
		 * @param y The row
		 * @return The global tile ID at the given position or -1.
		 */
		public int getTileAt(int x, int y) {
			if(x < 0 || x >= this.width || y < 0 || y >= this.height) return -1;
			
			return this.data[y * this.width + x];
		}
		
		/**
		 * @return The visibility of this layer.
		 */
		public boolean isVisible() {
			return this.visible;
		}
		
		/**
		 * @param visible True to set visible, false for invisible.
		 */
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
	
	/**
	 * Constructs a tile map component with the given parameters.
	 * 
	 * @param width The row count.
	 * @param height The column count.
	 * @param tileWidth The width of each tile.
	 * @param tileHeight The height of each tile.
	 */
	public TileMapComponent(int width, int height, float tileWidth, float tileHeight) {
		this.width = width;
		this.height = height;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.setOpaque(false);
		
		this.computeBoundingBox();
	}
	
	/**
	 * Adds a new tile set to this tile map.
	 * 
	 * @param tex The tile-set texture.
	 * @param cols The number of columns in the tile-set.
	 * @param rows The number of rows in the tile-set.
	 * @return The created tile-set object.
	 */
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
	
	/**
	 * @param id The tile-set id.
	 * @return The tile-set of this tile-map corresponding to the given tile-set ID.
	 */
	public TileSet getTileSet(int id) {
		if(id < 0 || id >= this.getTileSetCount()) return null;
		
		return this.tilesets.get(id);
	}
	
	/**
	 * @return The total tile-set count.
	 */
	public int getTileSetCount() {
		return this.tilesets.size();
	}
	
	/**
	 * Adds a new layer to this map. The layer size will be the same as the map.
	 * @return The created layer.
	 */
	public Layer createLayer() {
		Layer layer = new Layer(this.width, this.height);
		this.layers.add(layer);
		
		return layer;
	}

	/**
	 * @param id The layer id.
	 * @return The layer of this tile-map corresponding to the given layer ID.
	 */
	public Layer getLayer(int id) {
		if(id < 0 || id >= this.getLayerCount()) return null;
		
		return this.layers.get(id);
	}
	
	/**
	 * @return The number of layers in this map.
	 */
	public int getLayerCount() {
		return this.layers.size();
	}
	
	/**
	 * Returns the tile-set containing the tile corresponding to the given tile global ID.<br>
	 * It returns null if the ID is out of range.
	 * 
	 * @param tileID The global tile ID.
	 * @return The tile-set.
	 */
	public TileSet getTileSetForGlobalTileID(int tileID) {
		if(tileID < 0) return null;
		
		for(int i = 0; i < this.getTileSetCount(); i++) {
			TileSet set = this.getTileSet(i);
			if(tileID < set.firstgid + set.getTileCount()) return set;
		}
		
		return null;
	}
	
	/**
	 * @return This map's column count.
	 */
	public int getMapWidth() {
		return this.width;
	}

	/**
	 * @return This map's row count.
	 */
	public int getMapHeight() {
		return this.height;
	}
	
	/**
	 * @return The width of each cell in this map.
	 */
	public float getCellWidth() {
		return this.tileWidth;
	}
	
	/**
	 * @return The height of each cell in this map.
	 */
	public float getCellHeight() {
		return this.tileHeight;
	}
	
	private void computeBoundingBox() {
		this.boundingBox.redefine(-this.width / 2.0f * this.tileWidth, -this.height / 2.0f * this.tileHeight, this.width / 2.0f * this.tileWidth, -this.height / 2.0f * this.tileHeight);
	}
	
	public Box2D getBoundingBox() {
		return this.boundingBox;
	}
	
	/**
	 * <em>Note: This feature is experimental and may be broken.</em>
	 * 
	 * @return True if this map is in isometric mode, false otherwise.
	 */
	public boolean isIsometric() {
		return this.isometric;
	}
	
	/**
	 * <em>Note: This feature is experimental and may be broken.</em>
	 * 
	 * @param isometric True to set this map to isometric mod.
	 */
	public void setIsometric(boolean isometric) {
		if(this.isometric == isometric) return;
		
		this.isometric = isometric;
		this.computeBoundingBox();
	}
}
