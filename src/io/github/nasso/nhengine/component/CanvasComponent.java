package io.github.nasso.nhengine.component;

import io.github.nasso.nhengine.graphics.GraphicsContext2D;
import io.github.nasso.nhengine.utils.Box2D;

/**
 * A canvas component can be represented as a flat 2D drawable cartesian surface,
 * where <code>x</code> is going right, <code>y</code> is going down,
 * and the origin is in the top left corner.
 * <br>
 * Every canvas has its own {@link GraphicsContext2D} associated with it, which can be used to draw on it.
 * 
 * @author nasso
 */
public class CanvasComponent extends DrawableComponent {
	private GraphicsContext2D context = new GraphicsContext2D();
	
	private Box2D bounds = new Box2D();
	private int width, height;
	
	private boolean flipY = false;
	
	/**
	 * Constructs a new canvas component, with a resolution of <code>width</code> by <code>height</code>.<br>
	 * Note that even if the component <strong>resolution</strong> is changed, the component <strong>size</strong> remains 1x1 by default!
	 * So in most case, you'll want to call {@link #setScale(float, float)} to make the canvas bigger.
	 * 
	 * @param width
	 * @param height
	 */
	public CanvasComponent(int width, int height) {
		this.setResolution(width, height);
	}
	
	/**
	 * @return The {@link GraphicsContext2D} associated with this canvas.
	 */
	public GraphicsContext2D getContext2D() {
		return this.context;
	}
	
	public Box2D getBoundingBox() {
		this.bounds.redefineXYWH(0, 0, this.getScale().x, this.getScale().y);
		return this.bounds;
	}
	
	/**
	 * Sets the resolution of this canvas to be <code>width</code> by <code>height</code>.
	 * 
	 * @param width
	 *            The new <code>x</code> (horizontal) resolution.
	 * @param height
	 *            The new <code>y</code> (vertical) resolution.
	 */
	public void setResolution(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	/**
	 * @return The <code>x</code> (horizontal) resolution.
	 */
	public int getWidth() {
		return this.width;
	}
	
	/**
	 * @return The <code>y</code> (vertical) resolution.
	 */
	public int getHeight() {
		return this.height;
	}
	
	/**
	 * @return True if the <code>y</code> axis of this canvas is being inverted.
	 */
	public boolean isFlipY() {
		return this.flipY;
	}
	
	/**
	 * @param flipY
	 *            True to invert the <code>y</code> axis of this canvas, false otherwise.
	 */
	public void setFlipY(boolean flipY) {
		this.flipY = flipY;
	}
}
