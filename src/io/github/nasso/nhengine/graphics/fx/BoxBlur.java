package io.github.nasso.nhengine.graphics.fx;

public class BoxBlur extends PostEffect {
	private int size = 4;
	private int downsample = 1;
	private int iterations = 1;
	
	public BoxBlur(int size, int downsample, int iterations) {
		this.size = size;
		this.downsample = downsample;
		this.iterations = iterations;
	}
	
	public BoxBlur(int size, int downsample) {
		this.size = size;
		this.downsample = downsample;
	}
	
	public BoxBlur(int size) {
		this.size = size;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public int getIterations() {
		return this.iterations;
	}
	
	public void setIterations(int iterations) {
		this.iterations = iterations;
	}
	
	public int getDownsample() {
		return this.downsample;
	}
	
	public void setDownsample(int downsample) {
		this.downsample = downsample;
	}
}
