package io.github.nasso.nhengine.opengl;

import java.nio.IntBuffer;

import org.lwjgl.opengl.GL15;

public class OGLElementArrayBuffer {
	private static int bufferCount = 0;
	
	private int id = 0;
	
	private int version = -1;
	
	public OGLElementArrayBuffer() {
		this.id = GL15.glGenBuffers();
		OGLElementArrayBuffer.bufferCount++;
	}
	
	public OGLElementArrayBuffer loadData(IntBuffer data) {
		return this.loadData(data, GL15.GL_STATIC_DRAW);
	}
	
	public OGLElementArrayBuffer loadData(IntBuffer data, int usage) {
		this.bind();
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, data, usage);
		
		return this;
	}
	
	public OGLElementArrayBuffer loadData(int[] data) {
		return this.loadData(data, GL15.GL_STATIC_DRAW);
	}
	
	public OGLElementArrayBuffer loadData(int[] data, int usage) {
		this.bind();
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, data, usage);
		
		return this;
	}
	
	public OGLElementArrayBuffer bind() {
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.id);
		return this;
	}
	
	public OGLElementArrayBuffer unbind() {
		OGLElementArrayBuffer.unbindAll();
		return this;
	}
	
	public void dispose() {
		if(GL15.glIsBuffer(this.id)) {
			GL15.glDeleteBuffers(this.id);
			OGLElementArrayBuffer.bufferCount--;
			this.id = 0;
		}
	}
	
	public static void unbindAll() {
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	public static int getElementArrayBufferCount() {
		return OGLElementArrayBuffer.bufferCount;
	}
	
	public int getVersion() {
		return this.version;
	}
	
	public void setVersion(int version) {
		this.version = version;
	}
	
}
