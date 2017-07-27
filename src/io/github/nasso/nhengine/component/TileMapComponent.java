package io.github.nasso.nhengine.component;

import io.github.nasso.nhengine.level.Component;
import io.github.nasso.nhengine.utils.Box2D;

public class TileMapComponent extends Component {
	private int sizeX, sizeY;
	private float cellWidth, cellHeight;
	private SpriteComponent[] data;
	
	private boolean isometric = false;
	
	private Box2D boundingBox = new Box2D();
	
	public TileMapComponent(int sx, int sy, float cellWidth, float cellHeight) {
		this.data = new SpriteComponent[sx * sy];
		this.sizeX = sx;
		this.sizeY = sy;
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
		this.computeBoundingBox();
	}
	
	public int getSizeX() {
		return this.sizeX;
	}
	
	public int getSizeY() {
		return this.sizeY;
	}
	
	public float getCellWidth() {
		return this.cellWidth;
	}
	
	public float getCellHeight() {
		return this.cellHeight;
	}
	
	public void setSpriteComponent(int x, int y, SpriteComponent comp) {
		if(x < 0 || x >= this.sizeX || y < 0 || y >= this.sizeY) return;
		this.data[y * this.sizeX + x] = comp;
	}
	
	public SpriteComponent getSpriteComponent(int x, int y) {
		if(x < 0 || x >= this.sizeX || y < 0 || y >= this.sizeY) return null;
		return this.data[y * this.sizeX + x];
	}
	
	public SpriteComponent[] getData() {
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
}
