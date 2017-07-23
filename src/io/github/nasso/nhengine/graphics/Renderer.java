package io.github.nasso.nhengine.graphics;

import io.github.nasso.nhengine.core.Disposable;
import io.github.nasso.nhengine.level.Level;

public abstract class Renderer implements Disposable {
	private int width, height;
	
	public Renderer(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public abstract void render(Level lvl);
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void updateSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
}
