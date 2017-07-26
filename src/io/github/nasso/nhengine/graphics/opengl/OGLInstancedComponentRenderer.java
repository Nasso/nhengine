package io.github.nasso.nhengine.graphics.opengl;

import io.github.nasso.nhengine.component.DrawableComponent;
import io.github.nasso.nhengine.level.Scene;

public abstract class OGLInstancedComponentRenderer<T extends DrawableComponent> extends OGLComponentRenderer<T> {
	public OGLInstancedComponentRenderer(int width, int height) {
		super(width, height);
	}
	
	public abstract void render(Scene lvl, T[] comps, float[] gridXYPositionOffsets, int count);
}
