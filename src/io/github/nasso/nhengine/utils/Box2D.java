package io.github.nasso.nhengine.utils;

public class Box2D {
	private float minX, minY, maxX, maxY;
	
	public Box2D() {
		this.redefine(0, 0, 0, 0);
	}
	
	public Box2D(float minX, float minY, float maxX, float maxY) {
		this.redefine(minX, minY, maxX, maxY);
	}
	
	public Box2D(Box2D b) {
		this.minX = b.minX;
		this.maxX = b.maxX;
		this.minY = b.minY;
		this.maxY = b.maxY;
	}
	
	public String toString() {
		return "Box2D[minX=" + this.minX + "; minY=" + this.minY + "; maxX=" + this.maxX + "; maxY=" + this.maxY + "]";
	}
	
	public boolean intersects(float x, float y, float w, float h) {
		if(this.isEmpty() || w <= 0 || h <= 0) return false;
		return (x + w > this.minX && y + h > this.minY && x < this.maxX && y < this.maxY);
	}
	
	public boolean intersects(Box2D o) {
		return this.intersects(o.minX, o.minY, o.getWidth(), o.getHeight());
	}
	
	public boolean contains(float x, float y) {
		return MathUtils.boxContains(x, y, this.minX, this.minY, this.getWidth(), this.getHeight());
	}
	
	public boolean isEmpty() {
		return (this.getWidth() <= 0.0f) || (this.getHeight() <= 0.0f);
	}
	
	public void setMin(float x, float y) {
		this.minX = x;
		this.minY = y;
	}
	
	public void setMax(float x, float y) {
		this.maxX = x;
		this.maxY = y;
	}
	
	public void redefine(float minX, float minY, float maxX, float maxY) {
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
	}
	
	public void redefineXYWH(float x, float y, float w, float h) {
		this.setMin(x, y);
		this.setMax(x + w, y + h);
	}
	
	public float getWidth() {
		return this.maxX - this.minX;
	}
	
	public float getHeight() {
		return this.maxY - this.minY;
	}
	
	public float getMinX() {
		return this.minX;
	}
	
	public void setMinX(float minX) {
		this.minX = minX;
	}
	
	public float getMinY() {
		return this.minY;
	}
	
	public void setMinY(float minY) {
		this.minY = minY;
	}
	
	public float getMaxX() {
		return this.maxX;
	}
	
	public void setMaxX(float maxX) {
		this.maxX = maxX;
	}
	
	public float getMaxY() {
		return this.maxY;
	}
	
	public void setMaxY(float maxY) {
		this.maxY = maxY;
	}
}
