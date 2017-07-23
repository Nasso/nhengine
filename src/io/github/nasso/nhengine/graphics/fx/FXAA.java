package io.github.nasso.nhengine.graphics.fx;

public class FXAA extends PostEffect {
	private float reduceMin = 1.0f / 128.0f;
	private float reduceMul = 1.0f / 8.0f;
	private float spanMax = 8.0f;
	
	public FXAA(float reduceMin, float reduceMul, float spanMax) {
		this.reduceMin = reduceMin;
		this.reduceMul = reduceMul;
		this.spanMax = spanMax;
	}
	
	public FXAA(float reduceMin, float reduceMul) {
		this.reduceMin = reduceMin;
		this.reduceMul = reduceMul;
	}
	
	public FXAA(float reduceMin) {
		this.reduceMin = reduceMin;
	}
	
	public FXAA() {
		super();
	}
	
	public float getSpanMax() {
		return this.spanMax;
	}
	
	public void setSpanMax(float spanMax) {
		this.spanMax = spanMax;
	}
	
	public float getReduceMul() {
		return this.reduceMul;
	}
	
	public void setReduceMul(float reduceMul) {
		this.reduceMul = reduceMul;
	}
	
	public float getReduceMin() {
		return this.reduceMin;
	}
	
	public void setReduceMin(float reduceMin) {
		this.reduceMin = reduceMin;
	}
}
