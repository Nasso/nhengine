package io.github.nasso.nhengine.nanovg;

public class NVGTextMetrics {
	private float ascender, descender, lineh;
	
	NVGTextMetrics(float ascender, float descender, float lineh) {
		this.ascender = ascender;
		this.descender = descender;
		this.lineh = lineh;
	}
	
	public float getAscender() {
		return this.ascender;
	}
	
	public float getDescender() {
		return this.descender;
	}
	
	public float getLineHeight() {
		return this.lineh;
	}
}
