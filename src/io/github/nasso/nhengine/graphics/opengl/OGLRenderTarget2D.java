package io.github.nasso.nhengine.graphics.opengl;

import org.lwjgl.opengl.GL30;

public class OGLRenderTarget2D {
	private OGLFramebuffer2D fbo = null;
	private OGLTexture2D colorTexture = null;
	
	public OGLRenderTarget2D(OGLTexture2D colorTexture) {
		this.fbo = new OGLFramebuffer2D();
		
		this.setColorTexture(colorTexture);
	}
	
	public OGLFramebuffer2D getFBO() {
		return this.fbo;
	}
	
	public void setFBO(OGLFramebuffer2D fbo) {
		this.fbo = fbo;
	}
	
	public OGLTexture2D getColorTexture() {
		return this.colorTexture;
	}
	
	public void setColorTexture(OGLTexture2D colorTexture) {
		if(this.colorTexture != colorTexture) {
			this.colorTexture = colorTexture;
			
			this.fbo.bind();
			this.fbo.bindTexture(GL30.GL_COLOR_ATTACHMENT0, colorTexture);
			this.fbo.unbind();
		}
	}
	
	public void dispose(boolean deleteTexture) {
		this.fbo.dispose();
		
		if(deleteTexture) this.colorTexture.dispose();
	}
}
