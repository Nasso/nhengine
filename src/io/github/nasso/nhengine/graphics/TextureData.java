package io.github.nasso.nhengine.graphics;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

public class TextureData {
	private int bpp = 3;
	private int width = 0;
	private int height = 0;
	private ByteBuffer data;
	
	public TextureData() {
		
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
	
	public ByteBuffer getData() {
		return this.data;
	}
	
	public void setData(ByteBuffer data) {
		this.data = data;
	}
	
	/**
	 * Rotate by rot * 90� (clockwise).<br />
	 * <br />
	 * Ex: if rot == 1 then the texture will be rotated one time on the right<br />
	 * <em>Note: This currently only works for square texture (width == height)</em>
	 * 
	 * @param rot
	 *            The rotation to apply (rot * 90�)
	 */
	public void rotate(int rot) {
		if(this.width != this.height) {
			System.err.println("Currently no support to rotate non-square textures");
			return;
		}
		
		rot = rot % 4;
		
		if(rot < 0) rot += 4;
		
		if(rot == 0) return;
		
		int size = this.width;
		byte[] bdata = new byte[size * size * this.bpp];
		
		if(rot == 1) for(int x = 0; x < size; x++)
			for(int y = 0; y < size; y++) {
				int i = y * size + x;
				int ni = (size - x - 1) * size + y;
				
				i *= this.bpp;
				ni *= this.bpp;
				
				bdata[ni] = this.data.get(i);
				bdata[ni + 1] = this.data.get(i + 1);
				bdata[ni + 2] = this.data.get(i + 2);
				
				if(this.bpp == 4) bdata[ni + 3] = this.data.get(i + 3);
			}
		else if(rot == 2) for(int x = 0; x < size; x++)
			for(int y = 0; y < size; y++) {
				int i = y * size + x;
				int ni = (size - y - 1) * size + (size - x - 1);
				
				i *= this.bpp;
				ni *= this.bpp;
				
				bdata[ni] = this.data.get(i);
				bdata[ni + 1] = this.data.get(i + 1);
				bdata[ni + 2] = this.data.get(i + 2);
				
				if(this.bpp == 4) bdata[ni + 3] = this.data.get(i + 3);
			}
		else if(rot == 3) for(int x = 0; x < size; x++)
			for(int y = 0; y < size; y++) {
				int i = y * size + x;
				int ni = x * size + (size - y - 1);
				
				i *= this.bpp;
				ni *= this.bpp;
				
				bdata[ni] = this.data.get(i);
				bdata[ni + 1] = this.data.get(i + 1);
				bdata[ni + 2] = this.data.get(i + 2);
				
				if(this.bpp == 4) bdata[ni + 3] = this.data.get(i + 3);
			}
		else // wtf
			return;
		
		this.data.clear();
		this.data.put(bdata);
		this.data.flip();
	}
	
	public TextureData subData(int sx, int sy, int width, int height) {
		width = Math.max(1, width);
		height = Math.max(1, height);
		
		width = Math.min(this.width, width);
		height = Math.min(this.height, height);
		
		ByteBuffer subData = BufferUtils.createByteBuffer(width * height * this.bpp);
		
		int ex = sx + width;
		int ey = sy + height;
		for(int x = sx; x < ex; x++)
			for(int y = sy; y < ey; y++) {
				int offset = (y * this.width + x) * this.bpp;
				
				subData.put(this.data.get(offset));
				subData.put(this.data.get(offset + 1));
				subData.put(this.data.get(offset + 2));
				subData.put(this.data.get(offset + 3));
			}
		
		subData.flip();
		
		TextureData sub = new TextureData();
		sub.setWidth(width);
		sub.setHeight(height);
		sub.setData(subData);
		sub.setBytesPerPixel(this.bpp);
		
		return sub;
	}
	
	public TextureData clone() {
		return this.subData(0, 0, this.width, this.height);
	}
	
	public int getBytesPerPixel() {
		return this.bpp;
	}
	
	public void setBytesPerPixel(int bpp) {
		this.bpp = bpp;
	}
}
