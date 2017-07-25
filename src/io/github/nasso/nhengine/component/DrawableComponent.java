package io.github.nasso.nhengine.component;

import io.github.nasso.nhengine.level.Component;
import io.github.nasso.nhengine.utils.Box2D;
import io.github.nasso.nhengine.utils.MathUtils;

/**
 * A drawable component is a {@link Component} that is somehow visible on screen.<br>
 * Common drawable components are:
 * 
 * <ul>
 * <li>{@link CanvasComponent}: A flat drawable 2D cartesian surface.</li>
 * <li>{@link SpriteComponent}: Displays a sprite.</li>
 * <li>{@link TextComponent}: Displays text with a particular font.</li>
 * <li>{@link TileMapComponent}: Efficiently displays multiple {@link SpriteComponent}s in a 2D grid (like a terrain).</li>
 * </ul>
 * 
 * @author nasso
 */
public abstract class DrawableComponent extends Component {
	private boolean opaque = true;
	private float opacity = 1.0f;
	
	/**
	 * The bounding box is a 2D AABB (Axis Aligned Bounding Box).
	 * 
	 * @return The bounding box of this component.
	 */
	public abstract Box2D getBoundingBox();
	
	/**
	 * @return The opacity of this component.
	 */
	public float getOpacity() {
		return this.opacity;
	}
	
	/**
	 * @param alpha
	 *            The new opacity of this component.
	 */
	public void setOpacity(float alpha) {
		this.opacity = MathUtils.clamp(alpha, 0.0f, 1.0f);
	}
	
	/**
	 * @return True if this component is opaque.
	 */
	public boolean isOpaque() {
		return this.opaque;
	}
	
	/**
	 * An opaque component acts as if its opacity is 1, even if it's acutally not.<br>
	 * By default, a component is opaque.
	 * 
	 * @param opaque
	 *            True to set this component as opaque, false otherwise.
	 */
	public void setOpaque(boolean opaque) {
		this.opaque = opaque;
	}
}
