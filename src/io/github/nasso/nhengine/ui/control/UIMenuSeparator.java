package io.github.nasso.nhengine.ui.control;

public class UIMenuSeparator extends UIMenuItem {
	private float space = 4;
	
	public UIMenuSeparator() {
		this(4);
	}
	
	public UIMenuSeparator(float space) {
		this.space = space;
	}
	
	public float getSpace() {
		return this.space;
	}
	
	public void setSpace(float space) {
		this.space = space;
	}
}
