package io.github.nasso.nhengine.graphics;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

import io.github.nasso.nhengine.core.Disposable;
import io.github.nasso.nhengine.core.Nhengine;
import io.github.nasso.nhengine.event.Observable;

public class Texture2D extends Observable implements Disposable, Cloneable {
	public static class Builder {
		private int width = 512;
		private int height = 512;
		private int magFilter = Nhengine.LINEAR;
		private int minFilter = Nhengine.LINEAR_MIPMAP_LINEAR;
		private int wrapS = Nhengine.REPEAT;
		private int wrapT = Nhengine.REPEAT;
		private int internalFormat = Nhengine.SRGB_ALPHA;
		private int format = Nhengine.RGBA;
		private int type = Nhengine.UNSIGNED_BYTE;
		
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
		
		public Builder data(ByteBuffer value) {
			this.data = value;
			return this;
		}
		
		public Texture2D build() {
			return new Texture2D(this.width, this.height, this.magFilter, this.minFilter, this.wrapS, this.wrapT, this.internalFormat, this.format, this.type, this.data);
		}
	}
	
	private int width = 512;
	private int height = 512;
	private int magFilter = Nhengine.LINEAR;
	private int minFilter = Nhengine.LINEAR_MIPMAP_LINEAR;
	private int wrapS = Nhengine.REPEAT;
	private int wrapT = Nhengine.REPEAT;
	private int internalFormat = Nhengine.SRGB_ALPHA;
	private int format = Nhengine.RGBA;
	private int type = Nhengine.UNSIGNED_BYTE;
	
	private ByteBuffer data = null;
	
	private int version = 0;
	
	private String id = "";
	
	public void setID(String id) {
		this.id = id;
	}
	
	public String getID() {
		return this.id;
	}
	
	private String name = "";
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Texture2D(int width, int height, int magFilter, int minFilter, int wrapS, int wrapT, int internalFormat, int format, int type, ByteBuffer data) {
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
	}
	
	public void needUpdate() {
		this.version++;
	}
	
	public int getVersion() {
		return this.version;
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
	
	public Texture2D clone() {
		ByteBuffer dataCopy = BufferUtils.createByteBuffer(this.data.limit());
		dataCopy.put(this.data);
		this.data.flip();
		dataCopy.flip();
		
		return new Texture2D(this.width, this.height, this.magFilter, this.minFilter, this.wrapS, this.wrapT, this.internalFormat, this.format, this.type, dataCopy);
	}
	
	public void dispose() {
		this.triggerEvent("dispose");
		this.data.clear();
	}
}
