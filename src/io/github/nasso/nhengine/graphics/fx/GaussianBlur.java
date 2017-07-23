package io.github.nasso.nhengine.graphics.fx;

public class GaussianBlur extends PostEffect {
	public static enum BlurDirection {
		HORIZONTAL, VERTICAL, BOTH
	}
	
	private BlurDirection direction = BlurDirection.BOTH;
	private float size = 1.0f;
	private int downsample = 1;
	private int iterations = 1;
	
	public GaussianBlur(BlurDirection direction, float size, int downsample, int iterations) {
		this.direction = direction;
		this.size = size;
		this.downsample = downsample;
		this.iterations = iterations;
	}
	
	public GaussianBlur(BlurDirection direction, float size, int downsample) {
		this.direction = direction;
		this.size = size;
		this.downsample = downsample;
	}
	
	public GaussianBlur(BlurDirection direction, float size) {
		this.direction = direction;
		this.size = size;
	}
	
	public GaussianBlur(BlurDirection direction) {
		this.direction = direction;
	}
	
	public GaussianBlur() {
		this(BlurDirection.BOTH);
	}
	
	public BlurDirection getDirection() {
		return this.direction;
	}
	
	public void setDirection(BlurDirection direction) {
		this.direction = direction;
	}
	
	public int getDownsample() {
		return this.downsample;
	}
	
	public void setDownsample(int downsample) {
		this.downsample = downsample;
	}
	
	public int getIterations() {
		return this.iterations;
	}
	
	public void setIterations(int iterations) {
		this.iterations = Math.max(iterations, 1);
	}
	
	public float getSize() {
		return this.size;
	}
	
	public void setSize(float size) {
		this.size = size;
	}
}
