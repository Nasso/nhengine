package io.github.nasso.nhengine.graphics.opengl;

import io.github.nasso.nhengine.component.DrawableComponent;
import io.github.nasso.nhengine.level.Scene;

public abstract class OGLComponentRenderer<T extends DrawableComponent> {
	private int width, height;
	
	public OGLComponentRenderer(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public abstract void render(Scene sce, T comp);
	
	public abstract void dispose();
	
	public int getWidth() {
		return this.width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
}
