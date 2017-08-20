package io.github.nasso.nhengine.component;

import io.github.nasso.nhengine.graphics.Texture2D;
import io.github.nasso.nhengine.utils.Box2D;

public class TileMapComponent extends DrawableComponent {
	public static class TileSet {
		private Texture2D texture;
		private int columns, rows;
		
		public TileSet(Texture2D texture, int cols, int rows) {
			this.texture = texture;
			this.columns = cols;
			this.rows = rows;
		}
		
		public int getTileIDAt(int x, int y) {
			if(x < 0 || x >= this.columns || y < 0 || y > this.rows) return -1;
			
			return y * this.columns + x;
		}
		
		public int getTileColumn(int id) {
			return id < 0 || id >= this.columns * this.rows ? -1 : id % this.columns;
		}
		
		public int getTileRow(int id) {
			return id < 0 || id >= this.columns * this.rows ? -1 : id / this.columns;
		}
		
		public Texture2D getTexture() {
			return this.texture;
		}
		
		public void setTexture(Texture2D texture) {
			this.texture = texture;
		}
		
		public int getColumns() {
			return this.columns;
		}
		
		public void setColumns(int columns) {
			this.columns = columns;
		}
		
		public int getRows() {
			return this.rows;
		}
		
		public void setRows(int rows) {
			this.rows = rows;
		}
		
		public void dispose() {
			this.texture.dispose();
		}
	}
	
	private TileSet tileset;
	
	private int sizeX, sizeY;
	private float cellWidth, cellHeight;
	
	private int[] data;
	
	private boolean isometric = false;
	
	private Box2D boundingBox = new Box2D();
	
	public TileMapComponent(TileSet tileset, int sx, int sy, float cellWidth, float cellHeight) {
		this.tileset = tileset;
		
		this.data = new int[sx * sy];
		for(int i = 0; i < this.data.length; i++)
			this.data[i] = -1;
		
		this.sizeX = sx;
		this.sizeY = sy;
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
		this.setOpaque(false);
		
		this.computeBoundingBox();
	}
	
	public int getMapSizeX() {
		return this.sizeX;
	}
	
	public int getMapSizeY() {
		return this.sizeY;
	}
	
	public float getCellWidth() {
		return this.cellWidth;
	}
	
	public float getCellHeight() {
		return this.cellHeight;
	}
	
	public void setTileAt(int x, int y, int tileCol, int tileRow) {
		this.setTileAt(x, y, this.tileset.getTileIDAt(tileCol, tileRow));
	}
	
	public void setTileAt(int x, int y, int tileID) {
		if(x < 0 || x >= this.sizeX || y < 0 || y >= this.sizeY) return;
		
		this.data[y * this.sizeX + x] = tileID;
	}
	
	public int getTileAt(int x, int y) {
		if(x < 0 || x >= this.sizeX || y < 0 || y >= this.sizeY) return -1;
		
		return this.data[y * this.sizeX + x];
	}
	
	public int[] getTiles() {
		return this.data;
	}
	
	private void computeBoundingBox() {
		this.boundingBox.redefine(-this.sizeX / 2.0f * this.cellWidth, -this.sizeY / 2.0f * this.cellHeight, this.sizeX / 2.0f * this.cellWidth, -this.sizeY / 2.0f * this.cellHeight);
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
	
	public TileSet getTileSet() {
		return this.tileset;
	}
	
	public void setTileSet(TileSet tileset) {
		this.tileset = tileset;
	}
}
