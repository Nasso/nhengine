package io.github.nasso.nhengine.graphics.fx;

import org.joml.Vector3f;

public class Vignette extends PostEffect {
	private Vector3f color = new Vector3f();
	private float opacity = 1.0f;
	private float distance = 1.4f;
	private float smoothness = 0.7f;
	
	public Vignette(float r, float g, float b, float opacity, float distance, float smoothness) {
		this.setColor(r, g, b);
		
		this.setOpacity(opacity);
		this.setDistance(distance);
		this.setSmoothness(smoothness);
	}
	
	public Vignette(int rgb, float opacity, float distance, float smoothness) {
		this.setColor(rgb);
		
		this.setOpacity(opacity);
		this.setDistance(distance);
		this.setSmoothness(smoothness);
	}
	
	public Vignette(float opacity, float distance, float smoothness) {
		this.setOpacity(opacity);
		this.setDistance(distance);
		this.setSmoothness(smoothness);
	}
	
	public Vignette(float distance, float smoothness) {
		this.setDistance(distance);
		this.setSmoothness(smoothness);
	}
	
	public Vignette(float smoothness) {
		this.setSmoothness(smoothness);
	}
	
	public Vignette() {
		super();
	}
	
	public Vector3f getColor() {
		return this.color;
	}
	
	public void setColor(Vector3f color) {
		this.color.set(color);
	}
	
	public void setColor(float r, float g, float b) {
		this.color.set(r, g, b);
	}
	
	public void setColor(int color) {
		this.color.set(((color >> 16) & 0xFF) / 255.0f, ((color >> 8) & 0xFF) / 255.0f, ((color >> 0) & 0xFF) / 255.0f);
	}
	
	public float getOpacity() {
		return this.opacity;
	}
	
	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}
	
	public float getDistance() {
		return this.distance;
	}
	
	public void setDistance(float distance) {
		this.distance = distance;
	}
	
	public float getSmoothness() {
		return this.smoothness;
	}
	
	public void setSmoothness(float smoothness) {
		this.smoothness = smoothness;
	}
}
