package io.github.nasso.nhengine.component;

import io.github.nasso.nhengine.graphics.GraphicsContext2D;
import io.github.nasso.nhengine.utils.Box2D;

public class CanvasComponent extends DrawableComponent {
	private GraphicsContext2D context = new GraphicsContext2D();
	
	private Box2D bounds = new Box2D();
	private int width, height;
	
	private boolean flipY = false;
	
	public CanvasComponent(int width, int height) {
		this.setSize(width, height);
	}
	
	public GraphicsContext2D getContext2D() {
		return this.context;
	}
	
	public Box2D getBoundingBox() {
		return this.bounds;
	}
	
	private void updateBounds() {
		this.bounds.redefineXYWH(0, 0, this.width, this.height);
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		
		this.updateBounds();
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public boolean isFlipY() {
		return this.flipY;
	}
	
	public void setFlipY(boolean flipY) {
		this.flipY = flipY;
	}
}
