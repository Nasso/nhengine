package io.github.nasso.nhengine.component;

import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import io.github.nasso.nhengine.graphics.Texture2D;
import io.github.nasso.nhengine.utils.Box2D;

/**
 * A sprite component is a simple component that does nothing but rendering a flat 2D sprite.<br>
 * But it can do more: you can specify a color to be blended with the actual sprite texture,
 * or crop the original image (to use only a part of it).
 * 
 * @author nasso
 */
public class SpriteComponent extends DrawableComponent {
	private Vector4f viewRect = new Vector4f(0, 0, 1, 1);
	private Vector3f colorTeint = new Vector3f(1.0f);
	
	private float width = 1, height = 1;
	private Texture2D sprite;
	
	private Box2D boundingBox = new Box2D();
	
	/**
	 * Constructs a sprite component using the specified texture, and the specified view rectangle.
	 * 
	 * @param sprite
	 *            The texture to use.
	 * @param x
	 *            The <code>x</code> coordinate of the top-left corner of the view rectangle, in normalized coordinates (in the range <code>[0..1]</code>).
	 * @param y
	 *            The <code>y</code> coordinate of the top-left corner of the view rectangle, in normalized coordinates (in the range <code>[0..1]</code>).
	 * @param width
	 *            The width of the view rectangle, in normalized coordinates (in the range <code>[0..1]</code>).
	 * @param height
	 *            The height of the view rectangle, in normalized coordinates (in the range <code>[0..1]</code>).
	 */
	public SpriteComponent(Texture2D sprite, float x, float y, float width, float height) {
		this.setSprite(sprite);
		this.setViewRectangle(x, y, width, height);
	}
	
	/**
	 * Constructs a sprite component using the specified texture. The view rectangle is set to be a full view of the texture.<br>
	 * Equivalent to:
	 * 
	 * <pre>
	 * new {@link #SpriteComponent(Texture2D, float, float, float, float) SpriteComponent}(sprite, 0.0f, 0.0f, 1.0f, 1.0f);
	 * </pre>
	 * 
	 * @param sprite
	 *            The texture to use.
	 */
	public SpriteComponent(Texture2D sprite) {
		this(sprite, 0.0f, 0.0f, 1.0f, 1.0f);
	}
	
	/**
	 * @return The sprite's full texture.
	 */
	public Texture2D getSprite() {
		return this.sprite;
	}
	
	/**
	 * @param sprite
	 *            The new sprite texture.
	 */
	public void setSprite(Texture2D sprite) {
		this.sprite = sprite;
	}
	
	/**
	 * Automatically set the size of this component based on the given <code>width</code>
	 * by keeping the aspect ratio of the current texture.<br>
	 * If this sprite component has no texture, this method does nothing.
	 * 
	 * @param width
	 *            The desired component width.
	 */
	public void autoHeight(float width) {
		if(this.sprite == null) return;
		
		this.setSize(width, (float) this.sprite.getHeight() / (float) this.sprite.getWidth() * width);
	}
	
	/**
	 * Automatically set the size of this sprite component based on the given <code>height</code>
	 * by keeping the aspect ratio of the current texture.<br>
	 * If this sprite component has no texture, this method does nothing.
	 * 
	 * @param height
	 *            The desired component height.
	 */
	public void autoWidth(float height) {
		if(this.sprite == null) return;
		
		this.setSize((float) this.sprite.getWidth() / (float) this.sprite.getHeight() * height, height);
	}
	
	/**
	 * Automatically set the size of this sprite component to be equal to the size of the current texture.<br>
	 * If this sprite component has no texture, this method does nothing.
	 */
	public void autoSize() {
		if(this.sprite == null) return;
		
		this.setSize(this.sprite.getWidth(), this.sprite.getHeight());
	}
	
	/**
	 * Sets the size of this sprite component.
	 * 
	 * @param w
	 *            The desired width.
	 * @param h
	 *            The desired height.
	 */
	public void setSize(float w, float h) {
		this.width = w;
		this.height = h;
		
		this.computeBoundingBox();
	}
	
	/**
	 * @return The current width of this sprite component.
	 */
	public float getWidth() {
		return this.width;
	}
	
	/**
	 * @param width
	 *            The desired width of this sprite component.
	 */
	public void setWidth(float width) {
		this.width = width;
		
		this.computeBoundingBox();
	}
	
	/**
	 * @return The current height of this sprite component.
	 */
	public float getHeight() {
		return this.height;
	}
	
	/**
	 * @param height
	 *            The desired height of this sprite component.
	 */
	public void setHeight(float height) {
		this.height = height;
		
		this.computeBoundingBox();
	}
	
	/**
	 * @return The current RGB color teint, as a normalized {@link Vector3fc},
	 *         where <code>x</code> is the red component,
	 *         <code>y</code> is the green component
	 *         and <code>z</code> is the blue component.<br>
	 *         The default value is white <code>(1.0f, 1.0f, 1.0f)</code>.
	 */
	public Vector3fc getColorTeint() {
		return this.colorTeint;
	}
	
	/**
	 * Sets the RGB color teint to be blended to the texture.
	 * 
	 * @param r
	 *            The normalized <code>red</code> component of the color (in the range <code>[0..1]</code>).
	 * @param g
	 *            The normalized <code>green</code> component of the color (in the range <code>[0..1]</code>).
	 * @param b
	 *            The normalized <code>blue</code> component of the color (in the range <code>[0..1]</code>).
	 */
	public void setColorTeint(float r, float g, float b) {
		this.colorTeint.set(r, g, b);
	}
	
	/**
	 * Sets the view rectangle (crop) specifying "where the sprite is" in the texture.
	 * 
	 * @param x
	 *            The <code>x</code> coordinate of the top-left corner of the view rectangle, in normalized coordinates (in the range <code>[0..1]</code>).
	 * @param y
	 *            The <code>y</code> coordinate of the top-left corner of the view rectangle, in normalized coordinates (in the range <code>[0..1]</code>).
	 * @param width
	 *            The width of the view rectangle, in normalized coordinates (in the range <code>[0..1]</code>).
	 * @param height
	 *            The height of the view rectangle, in normalized coordinates (in the range <code>[0..1]</code>).
	 */
	public void setViewRectangle(float x, float y, float width, float height) {
		this.viewRect.x = x;
		this.viewRect.y = y;
		this.viewRect.z = width;
		this.viewRect.w = height;
	}
	
	/**
	 * @return The current view rectangle, in normalized coordinates (in the range <code>[0..1]</code>), as a {@link Vector4fc} where:
	 *         <ul>
	 *         <li><code>x</code> is the <code>x</code> coordinate of the top-left corner</li>
	 *         <li><code>y</code> is the <code>y</code> coordinate of the top-left corner.</li>
	 *         <li><code>z</code> is the <code>width</code> of the rectangle.</li>
	 *         <li><code>w</code> is the <code>height</code> of the rectangle.</li>
	 *         </ul>
	 */
	public Vector4fc getViewRectangle() {
		return this.viewRect;
	}
	
	private void computeBoundingBox() {
		this.boundingBox.redefine(0, 0, this.width, this.height);
	}
	
	public Box2D getBoundingBox() {
		return this.boundingBox;
	}
}
