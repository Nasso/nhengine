package io.github.nasso.nhengine.opengl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class OGLRenderBuffer2D {
	public static class Builder {
		private int internalFormat = GL11.GL_RGBA;
		private int width = 1024;
		private int height = 1024;
		
		public Builder internalFormat(int value) {
			this.internalFormat = value;
			return this;
		}
		
		public Builder width(int value) {
			this.width = value;
			return this;
		}
		
		public Builder height(int value) {
			this.height = value;
			return this;
		}
		
		public OGLRenderBuffer2D build() {
			return new OGLRenderBuffer2D(this.internalFormat, this.width, this.height);
		}
	}
	
	private static int renderBufferCount = 0;
	
	int id = 0;
	
	private int internalFormat = GL11.GL_RGBA;
	private int width = 1024;
	private int height = 1024;
	
	public OGLRenderBuffer2D(int internalFormat, int width, int height) {
		this.internalFormat = internalFormat;
		this.width = width;
		this.height = height;
		
		this.update();
	}
	
	public OGLRenderBuffer2D update() {
		if(!GL30.glIsRenderbuffer(this.id)) {
			this.id = GL30.glGenRenderbuffers();
			OGLRenderBuffer2D.renderBufferCount++;
		}
		
		this.bind();
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, this.internalFormat, this.width, this.height);
		
		return this;
	}
	
	public OGLRenderBuffer2D bind() {
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, this.id);
		
		return this;
	}
	
	public OGLRenderBuffer2D unbind() {
		OGLRenderBuffer2D.unbindAll();
		
		return this;
	}
	
	public void dispose() {
		if(GL30.glIsFramebuffer(this.id)) {
			GL30.glDeleteFramebuffers(this.id);
			OGLRenderBuffer2D.renderBufferCount--;
		}
	}
	
	public void finalize() {
		this.dispose();
	}
	
	public static void unbindAll() {
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
	}
	
	public static int getRenderBufferCount() {
		return OGLRenderBuffer2D.renderBufferCount;
	}
	
	public int getInternalFormat() {
		return this.internalFormat;
	}
	
	public void setInternalFormat(int internalFormat) {
		this.internalFormat = internalFormat;
	}
	
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
