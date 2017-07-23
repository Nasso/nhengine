package io.github.nasso.nhengine.graphics.fx;

public abstract class PostEffect {
	private boolean enabled = true;
	
	public boolean isEnabled() {
		return this.enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
