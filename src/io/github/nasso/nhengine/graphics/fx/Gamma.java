package io.github.nasso.nhengine.graphics.fx;

public class Gamma extends PostEffect {
	private float gamma = 2.2f;
	
	public Gamma(float gamma) {
		this.gamma = gamma;
	}
	
	public Gamma() {
		this(2.2f);
	}
	
	public float getGamma() {
		return this.gamma;
	}
	
	public void setGamma(float gamma) {
		this.gamma = gamma;
	}
}
