package io.github.nasso.nhengine.component;

import io.github.nasso.nhengine.level.Component;
import io.github.nasso.nhengine.utils.Box2D;
import io.github.nasso.nhengine.utils.MathUtils;

public abstract class DrawableComponent extends Component {
	private boolean opaque = true;
	private float alpha = 1.0f;
	
	public abstract Box2D getBoundingBox();
	
	public float getAlpha() {
		return this.alpha;
	}
	
	public void setAlpha(float alpha) {
		this.alpha = MathUtils.clamp(alpha, 0.0f, 1.0f);
	}
	
	public boolean isOpaque() {
		return this.opaque;
	}
	
	public void setOpaque(boolean opaque) {
		this.opaque = opaque;
	}
}
