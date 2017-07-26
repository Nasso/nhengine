package io.github.nasso.nhengine.graphics.opengl;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL33.*;

import java.util.HashMap;
import java.util.Map;

public class OGLVertexArray {
	private Map<Integer, Boolean> locEnableState = new HashMap<>();
	private static OGLVertexArray boundVAO = null;
	private static int vaoCount = 0;
	
	private int id = 0;
	
	public OGLVertexArray() {
		this.id = glGenVertexArrays();
		OGLVertexArray.vaoCount++;
	}
	
	public OGLVertexArray bind() {
		if(OGLVertexArray.boundVAO != this) {
			glBindVertexArray(this.id);
			OGLVertexArray.boundVAO = this;
		}
		
		return this;
	}
	
	public OGLVertexArray unbind() {
		OGLVertexArray.unbindAll();
		return this;
	}
	
	public void setAttribLocationEnabled(int loc, boolean enabled) {
		if(enabled) glEnableVertexAttribArray(loc);
		else glDisableVertexAttribArray(loc);
		this.locEnableState.put(loc, enabled);
	}
	
	public boolean isAttribLocationEnabled(int loc) {
		return this.locEnableState.getOrDefault(loc, false);
	}
	
	/**
	 * DON'T FORGET TO ENABLE IT, THIS METHOD DOESN'T DO IT
	 * 
	 * @param loc
	 * @param size
	 * @param vbo
	 */
	public void loadVBOToAttrib(int loc, int size, OGLArrayBuffer vbo, int stride, int offset, int divisor) {
		vbo.bind();
		glVertexAttribPointer(loc, size, vbo.getType(), false, stride, offset);
		glVertexAttribDivisor(loc, divisor);
	}
	
	public void loadVBOToAttrib(int loc, int size, OGLArrayBuffer vbo, int stride, int offset) {
		vbo.bind();
		glVertexAttribPointer(loc, size, vbo.getType(), false, stride, offset);
	}
	
	public void loadVBOToAttrib(int loc, int size, OGLArrayBuffer vbo, int offset) {
		this.loadVBOToAttrib(loc, size, vbo, 0, offset);
	}
	
	public void loadVBOToAttrib(int loc, int size, OGLArrayBuffer vbo) {
		this.loadVBOToAttrib(loc, size, vbo, 0);
	}
	
	public void loadIndices(OGLElementArrayBuffer indices) {
		indices.bind();
	}
	
	public static void unbindAll() {
		if(OGLVertexArray.boundVAO != null) {
			glBindVertexArray(0);
			OGLVertexArray.boundVAO = null;
		}
	}
	
	public static int getVAOCount() {
		return OGLVertexArray.vaoCount;
	}
	
	public void dispose() {
		if(glIsVertexArray(this.id)) {
			glDeleteVertexArrays(this.id);
			OGLVertexArray.vaoCount--;
			this.id = 0;
		}
	}
	
}
