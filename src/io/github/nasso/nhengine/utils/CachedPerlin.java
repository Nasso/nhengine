package io.github.nasso.nhengine.utils;

import java.util.Arrays;

public class CachedPerlin extends Perlin {
	private int width;
	private int height;
	
	private float[] cache;
	
	public CachedPerlin(int seed, float scale, float power, int width, int height) {
		super(seed, scale, power);
		
		this.width = width;
		this.height = height;
		this.cache = new float[width * height];
		Arrays.fill(this.cache, -1);
	}
	
	public float getValueAt(float x, float y) {
		int ix = (int) x;
		int iy = (int) y;
		
		if(ix < 0 || iy < 0 || ix >= this.width || iy >= this.height) return 0;
		
		float cachedValue = this.cache[ix * this.width + iy];
		
		if(cachedValue != -1) return cachedValue;
		
		return this.cache[ix * this.width + iy] = super.getValueAt(ix, iy);
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
}
