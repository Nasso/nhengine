package io.github.nasso.nhengine.component;

import io.github.nasso.nhengine.graphics.Texture2D;

/**
 * A tiled sprite component is just a simple {@link SpriteComponent sprite component}, but providing an easy way to retrieve sprites from a tile-set texture.
 * 
 * @author nasso
 */
public class TiledSpriteComponent extends SpriteComponent {
	private int colCount, rowCount, x, y;
	
	/**
	 * Constructs a tiled sprite component using the given tile-set, with the given parameters.
	 * 
	 * @param sprite
	 *            The tile-set texture.
	 * @param colCount
	 *            The number of columns in the texture.
	 * @param rowCount
	 *            The number of rows in the texture.
	 * @param x
	 *            The column of this sprite in the tile-set (indices start at 0, first column is 0).
	 * @param y
	 *            The row of this sprite in the tile-set (indices start at 0, first row is 0).
	 */
	public TiledSpriteComponent(Texture2D sprite, int colCount, int rowCount, int x, int y) {
		super(sprite);
		this.redefine(colCount, rowCount, x, y);
	}
	
	/**
	 * Constructs a tiled sprite component using the given tile-set, with the given parameters.<br>
	 * The initial position for the sprite is column 1, row 1. Since the column and row indices are zero-based, this constructor is equivalent to:
	 * 
	 * <pre>
	 * new {@link TiledSpriteComponent#TiledSpriteComponent(Texture2D, int, int, int, int) TiledSpriteComponent}(sprite, colCount, rowCount, 0, 0);
	 * </pre>
	 * 
	 * @param sprite
	 *            The tile-set texture.
	 * @param colCount
	 *            The number of columns in the texture.
	 * @param rowCount
	 *            The number of rows in the texture.
	 */
	public TiledSpriteComponent(Texture2D sprite, int colCount, int rowCount) {
		this(sprite, colCount, rowCount, 0, 0);
	}
	
	/**
	 * @return The column count.
	 */
	public int getColumnCount() {
		return this.colCount;
	}
	
	/**
	 * @return The row count.
	 */
	public int getRowCount() {
		return this.rowCount;
	}
	
	/**
	 * @return The column of the sprite in the tile-set.
	 */
	public int getCurrentX() {
		return this.x;
	}
	
	/**
	 * @return The row of the sprite in the tile-set.
	 */
	public int getCurrentY() {
		return this.y;
	}
	
	/**
	 * @param colCount
	 *            The new column count of the tile-set.
	 */
	public void setColumnCount(int colCount) {
		this.redefine(colCount, this.rowCount, this.x, this.y);
	}
	
	/**
	 * 
	 * @param rowCount
	 *            The new row count of the tile-set.
	 */
	public void setRowCount(int rowCount) {
		this.redefine(this.colCount, rowCount, this.x, this.y);
	}
	
	/**
	 * @param x
	 *            The new sprite column.
	 */
	public void setCurrentX(int x) {
		this.redefine(this.colCount, this.rowCount, x, this.y);
	}
	
	/**
	 * 
	 * @param y
	 *            The new sprite row.
	 */
	public void setCurrentY(int y) {
		this.redefine(this.colCount, this.rowCount, this.x, y);
	}
	
	/**
	 * Redefines the sprite location in the tile-set.
	 * 
	 * @param x
	 *            The new sprite column.
	 * @param y
	 *            The new sprite row.
	 */
	public void setCurrentCell(int x, int y) {
		this.redefine(this.colCount, this.rowCount, x, y);
	}
	
	/**
	 * Redefines the tile-set size (number of columns and rows).
	 * 
	 * @param colCount
	 *            The new column count of the tile-set.
	 * @param rowCount
	 *            The new row count of the tile-set.
	 */
	public void setTileSetSize(int colCount, int rowCount) {
		this.redefine(colCount, rowCount, this.x, this.y);
	}
	
	/**
	 * Redefines this component, without changing the tile-set texture.
	 * 
	 * @param colCount
	 *            The new column count of the tile-set.
	 * @param rowCount
	 *            The new row count of the tile-set.
	 * @param x
	 *            The new sprite column.
	 * @param y
	 *            The new sprite row.
	 */
	public void redefine(int colCount, int rowCount, int x, int y) {
		this.colCount = colCount;
		this.rowCount = rowCount;
		this.x = x;
		this.y = y;
		
		this.updateViewRectangle();
	}
	
	private void updateViewRectangle() {
		this.setViewRectangle(1.0f / this.colCount * this.x, 1.0f / this.rowCount * this.y, 1.0f / this.colCount, 1.0f / this.rowCount);
	}
}
