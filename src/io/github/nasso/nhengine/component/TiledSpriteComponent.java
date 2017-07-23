package io.github.nasso.nhengine.component;

import io.github.nasso.nhengine.graphics.Texture2D;

public class TiledSpriteComponent extends SpriteComponent {
	private int colCount, rowCount, x, y;
	
	public TiledSpriteComponent(Texture2D sprite, int colCount, int rowCount, int x, int y) {
		super(sprite);
		this.redefine(colCount, rowCount, x, y);
	}
	
	public TiledSpriteComponent(Texture2D sprite, int colCount, int rowCount) {
		this(sprite, colCount, rowCount, 0, 0);
	}
	
	public int getColumnCount() {
		return this.colCount;
	}
	
	public int getRowCount() {
		return this.rowCount;
	}
	
	public int getActiveX() {
		return this.x;
	}
	
	public int getActiveY() {
		return this.y;
	}
	
	public void setColumnCount(int colCount) {
		this.redefine(colCount, this.rowCount, this.x, this.y);
	}
	
	public void setRowCount(int rowCount) {
		this.redefine(this.colCount, rowCount, this.x, this.y);
	}
	
	public void setActiveX(int x) {
		this.redefine(this.colCount, this.rowCount, x, this.y);
	}
	
	public void setActiveY(int y) {
		this.redefine(this.colCount, this.rowCount, this.x, y);
	}
	
	public void setActiveCell(int x, int y) {
		this.redefine(this.colCount, this.rowCount, x, y);
	}
	
	public void setTableSize(int colCount, int rowCount) {
		this.redefine(colCount, rowCount, this.x, this.y);
	}
	
	public void redefine(int colCount, int rowCount, int x, int y) {
		this.colCount = colCount;
		this.rowCount = rowCount;
		this.x = x;
		this.y = y;
		
		this.updateRegion();
	}
	
	public void updateRegion() {
		this.setRegion(1.0f / this.colCount * this.x, 1.0f / this.rowCount * this.y, 1.0f / this.colCount, 1.0f / this.rowCount);
	}
}
