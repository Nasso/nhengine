package io.github.nasso.nhengine.component;

import org.joml.Vector3f;
import org.joml.Vector4f;

import io.github.nasso.nhengine.graphics.Texture2D;
import io.github.nasso.nhengine.utils.Box2D;

public class SpriteComponent extends DrawableComponent {
	private Vector4f subRegion = new Vector4f(0f, 0f, 1f, 1f);
	private float width = 1, height = 1;
	private Texture2D sprite;
	private Vector3f colorTeint = new Vector3f(1.0f);
	
	private Box2D boundingBox = new Box2D();
	
	public SpriteComponent(Texture2D sprite, float x, float y, float width, float height) {
		this.setSprite(sprite);
		this.setRegion(x, y, width, height);
	}
	
	public SpriteComponent(Texture2D sprite) {
		this(sprite, 0.0f, 0.0f, 1.0f, 1.0f);
	}
	
	public Texture2D getSprite() {
		return this.sprite;
	}
	
	public void setSprite(Texture2D sprite) {
		this.sprite = sprite;
	}
	
	public void autoSize(float scale) {
		if(this.sprite == null) {
			System.err.println("Can't set automatic size because no sprite is bound to this sprite component.");
			return;
		}
		
		this.width = scale;
		this.height = (float) this.sprite.getHeight() / (float) this.sprite.getWidth() * scale;
		
		this.computeBoundingBox();
	}
	
	public void setSize(float w, float h) {
		this.width = w;
		this.height = h;
		
		this.computeBoundingBox();
	}
	
	public float getWidth() {
		return this.width;
	}
	
	public void setWidth(float width) {
		this.width = width;
		
		this.computeBoundingBox();
	}
	
	public float getHeight() {
		return this.height;
	}
	
	public void setHeight(float height) {
		this.height = height;
		
		this.computeBoundingBox();
	}
	
	public Vector3f getColorTeint() {
		return this.colorTeint;
	}
	
	public void setColorTeint(Vector3f colorTeint) {
		this.colorTeint.set(colorTeint);
	}
	
	/**
	 * De 0.0 à 1.0
	 * 
	 * @param r
	 * @param g
	 * @param b
	 */
	public void setColorTeint(float r, float g, float b) {
		this.colorTeint.set(r, g, b);
	}
	
	public void setRegion(float x, float y, float width, float height) {
		this.subRegion.set(x, y, width, height);
	}
	
	public Vector4f getRegion() {
		return this.subRegion;
	}
	
	private void computeBoundingBox() {
		this.boundingBox.redefine(0, 0, this.width, this.height);
	}
	
	public Box2D getBoundingBox() {
		return this.boundingBox;
	}
}
