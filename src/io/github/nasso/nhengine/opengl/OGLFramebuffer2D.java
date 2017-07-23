package io.github.nasso.nhengine.opengl;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;

import java.util.HashMap;
import java.util.Map;

public class OGLFramebuffer2D {
	private static int renderTargetCount = 0;
	
	private Map<Integer, OGLRenderBuffer2D> renderBuffers = new HashMap<>();
	private Map<Integer, OGLTexture2D> textures = new HashMap<>();
	
	private int id;
	
	public OGLFramebuffer2D() {
		this.id = glGenFramebuffers();
		OGLFramebuffer2D.renderTargetCount++;
	}
	
	public boolean isComplete() {
		return glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE;
	}
	
	public int getStatus() {
		return glCheckFramebufferStatus(GL_FRAMEBUFFER);
	}
	
	public void bindRenderBuffer(int attachment, OGLRenderBuffer2D renderBuffer) {
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, attachment, GL_RENDERBUFFER, renderBuffer.id);
		this.renderBuffers.put(attachment, renderBuffer);
	}
	
	public void bindTexture(int attachment, OGLTexture2D texture) {
		glFramebufferTexture(GL_FRAMEBUFFER, attachment, texture.id, 0);
		this.textures.put(attachment, texture);
	}
	
	public OGLRenderBuffer2D getRenderBuffer(int attachment) {
		return this.renderBuffers.get(attachment);
	}
	
	public OGLTexture2D getTexture(int attachment) {
		return this.textures.get(attachment);
	}
	
	public OGLFramebuffer2D bind() {
		glBindFramebuffer(GL_FRAMEBUFFER, this.id);
		
		return this;
	}
	
	public OGLFramebuffer2D unbind() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		
		return this;
	}
	
	public OGLFramebuffer2D bindRead() {
		glBindFramebuffer(GL_READ_FRAMEBUFFER, this.id);
		
		return this;
	}
	
	public OGLFramebuffer2D unbindRead() {
		glBindFramebuffer(GL_READ_FRAMEBUFFER, 0);
		
		return this;
	}
	
	public OGLFramebuffer2D bindWrite() {
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, this.id);
		
		return this;
	}
	
	public OGLFramebuffer2D unbindWrite() {
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
		
		return this;
	}
	
	public void dispose() {
		if(glIsFramebuffer(this.id)) {
			glDeleteFramebuffers(this.id);
			OGLFramebuffer2D.renderTargetCount--;
		}
	}
	
	public static void unbindAll() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glBindFramebuffer(GL_READ_FRAMEBUFFER, 0);
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
	}
	
	public static int getRenderTargetCount() {
		return OGLFramebuffer2D.renderTargetCount;
	}
}
