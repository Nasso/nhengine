package io.github.nasso.nhengine.utils;

import java.util.Random;

public class Perlin {
	private int seed;
	private float scale = 1, power = 1;
	
	private Random rand = new Random();
	
	public Perlin(int seed, float scale, float power) {
		this.seed = seed;
		this.scale = scale;
		this.power = power;
	}
	
	public int getSeed() {
		return this.seed;
	}
	
	public void setSeed(int seed) {
		this.seed = seed;
	}
	
	public float getScale() {
		return this.scale;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public float getPower() {
		return this.power;
	}
	
	public void setPower(float power) {
		this.power = power;
	}
	
	private float getRandomValueAt(int x, int y) {
		this.rand.setSeed(x * 42246 + y * 146642 + this.seed);
		return this.rand.nextFloat();
	}
	
	private float getPerlinSmoothValueAt(int x, int y) {
		float center = this.getRandomValueAt(x, y) / 4f;
		float corners = (this.getRandomValueAt(x + 1, y - 1) + this.getRandomValueAt(x + 1, y + 1) + this.getRandomValueAt(x - 1, y - 1) + this.getRandomValueAt(x - 1, y + 1)) / 16f;
		float sides = (this.getRandomValueAt(x + 1, y) + this.getRandomValueAt(x - 1, y) + this.getRandomValueAt(x, y + 1) + this.getRandomValueAt(x, y - 1)) / 8f;
		
		return center + corners + sides;
	}
	
	private float interpolate(float x, float y, float pos) {
		double cos = 1.0 - Math.cos(pos * Math.PI) * 0.5;
		
		return (float) (x * (1.0 - cos) + y * cos);
	}
	
	public float getValueAt(float x, float y) {
		x *= this.scale;
		y *= this.scale;
		
		int intx = (int) x;
		int inty = (int) y;
		
		float fracx = x - intx;
		float fracy = y - inty;
		
		float v1 = this.getPerlinSmoothValueAt(intx, inty);
		float v2 = this.getPerlinSmoothValueAt(intx + 1, inty);
		float v3 = this.getPerlinSmoothValueAt(intx, inty + 1);
		float v4 = this.getPerlinSmoothValueAt(intx + 1, inty + 1);
		
		float i1 = this.interpolate(v1, v2, fracx);
		float i2 = this.interpolate(v3, v4, fracx);
		
		if(this.power == 1.0f) return this.interpolate(i1, i2, fracy);
		
		float perl = this.interpolate(i1, i2, fracy) * 2.0f - 1.0f;
		float powperl = (float) Math.pow(Math.abs(perl), this.power);
		
		if(perl < 0.0f && powperl > 0.0f) powperl = -powperl;
		
		return powperl * 0.5f + 0.5f;
	}
}
