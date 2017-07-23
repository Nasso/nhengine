package io.github.nasso.nhengine.opengl;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;

public class OGLTexture2D {
	private static int textureCount = 0;
	
	public static class Builder {
		private int width = 512;
		private int height = 512;
		private int magFilter = GL11.GL_LINEAR;
		private int minFilter = GL11.GL_LINEAR_MIPMAP_LINEAR;
		private int wrapS = GL11.GL_REPEAT;
		private int wrapT = GL11.GL_REPEAT;
		private int internalFormat = GL21.GL_SRGB_ALPHA;
		private int format = GL11.GL_RGBA;
		private int type = GL11.GL_UNSIGNED_BYTE;
		
		private ByteBuffer data = null;
		
		public Builder width(int value) {
			this.width = value;
			return this;
		}
		
		public Builder height(int value) {
			this.height = value;
			return this;
		}
		
		public Builder magFilter(int value) {
			this.magFilter = value;
			return this;
		}
		
		public Builder minFilter(int value) {
			this.minFilter = value;
			return this;
		}
		
		public Builder wrapS(int value) {
			this.wrapS = value;
			return this;
		}
		
		public Builder wrapT(int value) {
			this.wrapT = value;
			return this;
		}
		
		public Builder internalFormat(int value) {
			this.internalFormat = value;
			return this;
		}
		
		public Builder format(int value) {
			this.format = value;
			return this;
		}
		
		public Builder type(int value) {
			this.type = value;
			return this;
		}
		
		public Builder anisotropy(int value) {
			return this;
		}
		
		public Builder data(ByteBuffer value) {
			this.data = value;
			return this;
		}
		
		public OGLTexture2D build() {
			return new OGLTexture2D(this.width, this.height, this.magFilter, this.minFilter, this.wrapS, this.wrapT, this.internalFormat, this.format, this.type, this.data);
		}
	}
	
	int id = 0;
	
	private int width = 512;
	private int height = 512;
	private int magFilter = GL11.GL_LINEAR;
	private int minFilter = GL11.GL_LINEAR_MIPMAP_LINEAR;
	private int wrapS = GL11.GL_REPEAT;
	private int wrapT = GL11.GL_REPEAT;
	private int internalFormat = GL21.GL_SRGB_ALPHA;
	private int format = GL11.GL_RGBA;
	private int type = GL11.GL_UNSIGNED_BYTE;
	
	private int version = -1;
	
	private ByteBuffer data = null;
	
	public OGLTexture2D(int width, int height, int magFilter, int minFilter, int wrapS, int wrapT, int internalFormat, int format, int type, ByteBuffer data) {
		this.width = width;
		this.height = height;
		this.magFilter = magFilter;
		this.minFilter = minFilter;
		this.wrapS = wrapS;
		this.wrapT = wrapT;
		this.internalFormat = internalFormat;
		this.format = format;
		this.type = type;
		this.data = data;
		
		this.update();
	}
	
	public OGLTexture2D update() {
		if(!GL11.glIsTexture(this.id)) {
			this.id = GL11.glGenTextures();
			OGLTexture2D.textureCount++;
		}
		
		this.bind();
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, this.internalFormat, this.width, this.height, 0, this.format, this.type, this.data);
		
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, this.magFilter);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, this.minFilter);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, this.wrapS);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, this.wrapT);
		
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		this.unbind();
		
		return this;
	}
	
	public int getID() {
		return this.id;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getMagFilter() {
		return this.magFilter;
	}
	
	public int getMinFilter() {
		return this.minFilter;
	}
	
	public int getWrapS() {
		return this.wrapS;
	}
	
	public int getWrapT() {
		return this.wrapT;
	}
	
	public int getInternalFormat() {
		return this.internalFormat;
	}
	
	public int getFormat() {
		return this.format;
	}
	
	public int getType() {
		return this.type;
	}
	
	public ByteBuffer getData() {
		return this.data;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setMagFilter(int magFilter) {
		this.magFilter = magFilter;
	}
	
	public void setMinFilter(int minFilter) {
		this.minFilter = minFilter;
	}
	
	public void setWrapS(int wrapS) {
		this.wrapS = wrapS;
	}
	
	public void setWrapT(int wrapT) {
		this.wrapT = wrapT;
	}
	
	public void setInternalFormat(int internalFormat) {
		this.internalFormat = internalFormat;
	}
	
	public void setFormat(int format) {
		this.format = format;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public void setData(ByteBuffer data) {
		this.data = data;
	}
	
	public void bind() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id);
	}
	
	public void unbind() {
		OGLTexture2D.unbindAll();
	}
	
	public void dispose() {
		if(GL11.glIsTexture(this.id)) {
			GL11.glDeleteTextures(this.id);
			OGLTexture2D.textureCount--;
			this.id = 0;
		}
	}
	
	public boolean isNull() {
		return !GL11.glIsTexture(this.id);
	}
	
	public static void unbindAll() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	public static int getTextureCount() {
		return OGLTexture2D.textureCount;
	}
	
	public int getVersion() {
		return this.version;
	}
	
	public void setVersion(int version) {
		this.version = version;
	}
}
