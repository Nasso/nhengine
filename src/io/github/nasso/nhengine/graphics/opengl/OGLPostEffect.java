package io.github.nasso.nhengine.graphics.opengl;

import io.github.nasso.nhengine.graphics.fx.PostEffect;

public abstract class OGLPostEffect {
	private int width, height;
	
	/**
	 * Apply the effects assuming that a full screen quad VAO is bound so it can be fully drawn with <code>glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);</code>
	 * 
	 * @param sourceColor
	 * @param sourceDepth
	 * @param dest
	 */
	public abstract void apply(OGLTexture2D sourceColor, OGLTexture2D sourceDepth, OGLFramebuffer2D dest);
	
	public abstract void dispose();
	
	public abstract void update(PostEffect e);
	
	/**
	 * Updates the size of the effect
	 * 
	 * @param width
	 *            The source width
	 * @param height
	 *            The source height
	 */
	public void updateSize(int width, int height) {
		
	}
	
	public int getWidth() {
		return this.width;
	}
	
	protected void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	protected void setHeight(int height) {
		this.height = height;
	}
}
