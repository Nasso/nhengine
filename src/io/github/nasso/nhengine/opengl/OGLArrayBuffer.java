package io.github.nasso.nhengine.opengl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import java.nio.Buffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

public class OGLArrayBuffer {
	private static OGLArrayBuffer boundVBO = null;
	private static int bufferCount = 0;
	
	private int id = 0;
	private int type = GL_FLOAT;
	
	private int version = -1;
	
	public OGLArrayBuffer() {
		this.id = glGenBuffers();
		OGLArrayBuffer.bufferCount++;
	}
	
	public OGLArrayBuffer(long size) {
		this(size, GL_STATIC_DRAW);
	}
	
	public OGLArrayBuffer(long size, int usage) {
		this();
		this.allocate(size, usage);
	}
	
	public void allocate(long size) {
		this.allocate(size, GL_STATIC_DRAW);
	}
	
	public void allocate(long size, int usage) {
		this.bind();
		glBufferData(GL_ARRAY_BUFFER, size, usage);
	}
	
	public OGLArrayBuffer loadData(FloatBuffer data) {
		return this.loadData(data, GL_STATIC_DRAW);
	}
	
	public OGLArrayBuffer loadData(IntBuffer data) {
		return this.loadData(data, GL_STATIC_DRAW);
	}
	
	public OGLArrayBuffer loadData(ShortBuffer data) {
		return this.loadData(data, GL_STATIC_DRAW);
	}
	
	public OGLArrayBuffer loadData(DoubleBuffer data) {
		return this.loadData(data, GL_STATIC_DRAW);
	}
	
	public OGLArrayBuffer loadData(Buffer data) {
		return this.loadData(data, GL_STATIC_DRAW);
	}
	
	public OGLArrayBuffer loadData(FloatBuffer data, int usage) {
		this.type = GL_FLOAT;
		
		this.bind();
		glBufferData(GL_ARRAY_BUFFER, data, usage);
		
		return this;
	}
	
	public OGLArrayBuffer loadData(IntBuffer data, int usage) {
		this.type = GL_INT;
		
		this.bind();
		glBufferData(GL_ARRAY_BUFFER, data, usage);
		
		return this;
	}
	
	public OGLArrayBuffer loadData(ShortBuffer data, int usage) {
		this.type = GL_SHORT;
		
		this.bind();
		glBufferData(GL_ARRAY_BUFFER, data, usage);
		
		return this;
	}
	
	public OGLArrayBuffer loadData(DoubleBuffer data, int usage) {
		this.type = GL_DOUBLE;
		
		this.bind();
		glBufferData(GL_ARRAY_BUFFER, data, usage);
		
		return this;
	}
	
	public OGLArrayBuffer loadData(Buffer data, int usage) {
		if(data instanceof FloatBuffer) this.loadData((FloatBuffer) data, usage);
		else if(data instanceof IntBuffer) this.loadData((IntBuffer) data, usage);
		else if(data instanceof ShortBuffer) this.loadData((ShortBuffer) data, usage);
		else if(data instanceof DoubleBuffer) this.loadData((DoubleBuffer) data, usage);
		
		return this;
	}
	
	public OGLArrayBuffer loadData(float[] data) {
		return this.loadData(data, GL_STATIC_DRAW);
	}
	
	public OGLArrayBuffer loadData(int[] data) {
		return this.loadData(data, GL_STATIC_DRAW);
	}
	
	public OGLArrayBuffer loadData(short[] data) {
		return this.loadData(data, GL_STATIC_DRAW);
	}
	
	public OGLArrayBuffer loadData(double[] data) {
		return this.loadData(data, GL_STATIC_DRAW);
	}
	
	public OGLArrayBuffer loadData(float[] data, int usage) {
		this.type = GL_FLOAT;
		
		this.bind();
		glBufferData(GL_ARRAY_BUFFER, data, usage);
		
		return this;
	}
	
	public OGLArrayBuffer loadData(int[] data, int usage) {
		this.type = GL_INT;
		
		this.bind();
		glBufferData(GL_ARRAY_BUFFER, data, usage);
		
		return this;
	}
	
	public OGLArrayBuffer loadData(short[] data, int usage) {
		this.type = GL_SHORT;
		
		this.bind();
		glBufferData(GL_ARRAY_BUFFER, data, usage);
		
		return this;
	}
	
	public OGLArrayBuffer loadData(double[] data, int usage) {
		this.type = GL_DOUBLE;
		
		this.bind();
		glBufferData(GL_ARRAY_BUFFER, data, usage);
		
		return this;
	}
	
	// Sub versions
	public OGLArrayBuffer loadSubData(FloatBuffer data) {
		return this.loadSubData(data, 0);
	}
	
	public OGLArrayBuffer loadSubData(IntBuffer data) {
		return this.loadSubData(data, 0);
	}
	
	public OGLArrayBuffer loadSubData(ShortBuffer data) {
		return this.loadSubData(data, 0);
	}
	
	public OGLArrayBuffer loadSubData(DoubleBuffer data) {
		return this.loadSubData(data, 0);
	}
	
	public OGLArrayBuffer loadSubData(Buffer data) {
		return this.loadSubData(data, 0);
	}
	
	public OGLArrayBuffer loadSubData(FloatBuffer data, int offset) {
		this.type = GL_FLOAT;
		
		this.bind();
		glBufferSubData(GL_ARRAY_BUFFER, offset, data);
		
		return this;
	}
	
	public OGLArrayBuffer loadSubData(IntBuffer data, int offset) {
		this.type = GL_INT;
		
		this.bind();
		glBufferSubData(GL_ARRAY_BUFFER, offset, data);
		
		return this;
	}
	
	public OGLArrayBuffer loadSubData(ShortBuffer data, int offset) {
		this.type = GL_SHORT;
		
		this.bind();
		glBufferSubData(GL_ARRAY_BUFFER, offset, data);
		
		return this;
	}
	
	public OGLArrayBuffer loadSubData(DoubleBuffer data, int offset) {
		this.type = GL_DOUBLE;
		
		this.bind();
		glBufferSubData(GL_ARRAY_BUFFER, offset, data);
		
		return this;
	}
	
	public OGLArrayBuffer loadSubData(Buffer data, int offset) {
		if(data instanceof FloatBuffer) this.loadSubData((FloatBuffer) data, offset);
		else if(data instanceof IntBuffer) this.loadSubData((IntBuffer) data, offset);
		else if(data instanceof ShortBuffer) this.loadSubData((ShortBuffer) data, offset);
		else if(data instanceof DoubleBuffer) this.loadSubData((DoubleBuffer) data, offset);
		
		return this;
	}
	
	public OGLArrayBuffer loadSubData(float[] data) {
		return this.loadSubData(data, 0);
	}
	
	public OGLArrayBuffer loadSubData(int[] data) {
		return this.loadSubData(data, 0);
	}
	
	public OGLArrayBuffer loadSubData(short[] data) {
		return this.loadSubData(data, 0);
	}
	
	public OGLArrayBuffer loadSubData(double[] data) {
		return this.loadSubData(data, 0);
	}
	
	public OGLArrayBuffer loadSubData(float[] data, int offset) {
		this.type = GL_FLOAT;
		
		this.bind();
		glBufferSubData(GL_ARRAY_BUFFER, offset, data);
		
		return this;
	}
	
	public OGLArrayBuffer loadSubData(int[] data, int offset) {
		this.type = GL_INT;
		
		this.bind();
		glBufferSubData(GL_ARRAY_BUFFER, offset, data);
		
		return this;
	}
	
	public OGLArrayBuffer loadSubData(short[] data, int offset) {
		this.type = GL_SHORT;
		
		this.bind();
		glBufferSubData(GL_ARRAY_BUFFER, offset, data);
		
		return this;
	}
	
	public OGLArrayBuffer loadSubData(double[] data, int offset) {
		this.type = GL_DOUBLE;
		
		this.bind();
		glBufferSubData(GL_ARRAY_BUFFER, offset, data);
		
		return this;
	}
	
	public int getType() {
		return this.type;
	}
	
	public OGLArrayBuffer bind() {
		if(OGLArrayBuffer.boundVBO != this) {
			glBindBuffer(GL_ARRAY_BUFFER, this.id);
			OGLArrayBuffer.boundVBO = this;
		}
		return this;
	}
	
	public OGLArrayBuffer unbind() {
		OGLArrayBuffer.unbindAll();
		return this;
	}
	
	public void dispose() {
		if(glIsBuffer(this.id)) {
			glDeleteBuffers(this.id);
			OGLArrayBuffer.bufferCount--;
			this.id = 0;
		}
	}
	
	public static void unbindAll() {
		if(OGLArrayBuffer.boundVBO != null) {
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			OGLArrayBuffer.boundVBO = null;
		}
	}
	
	public static int getArrayBufferCount() {
		return OGLArrayBuffer.bufferCount;
	}
	
	public int getVersion() {
		return this.version;
	}
	
	public void setVersion(int version) {
		this.version = version;
	}
}
