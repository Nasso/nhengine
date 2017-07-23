package io.github.nasso.nhengine.ui.control;

import io.github.nasso.nhengine.ui.UIComponent;
import io.github.nasso.nhengine.ui.UIContainer;
import io.github.nasso.nhengine.utils.MathUtils;

public class UIViewport extends UIContainer {
	private UIComponent content;
	
	private float scrollX, scrollY;
	
	public UIViewport() {
		this(null);
	}
	
	public UIViewport(UIComponent content) {
		this.setContent(content);
	}
	
	public UIComponent getContent() {
		return this.content;
	}
	
	public void setContent(UIComponent content) {
		if(this.content == content) return;
		if(this.content != null) this.remove(this.content);
		
		this.content = content;
		
		if(this.content != null) {
			this.add(this.content);
			if(content instanceof UIContainer) {
				((UIContainer) content).repack();
			}
		}
	}
	
	public float getHorizontalCoverage() {
		return this.content == null ? 1.0f : MathUtils.clamp(this.getWidth() / this.content.getWidth(), 0, 1);
	}
	
	public float getVerticalCoverage() {
		return this.content == null ? 1.0f : MathUtils.clamp(this.getHeight() / this.content.getHeight(), 0, 1);
	}
	
	public void setScroll(float x, float y) {
		this.scrollX = x;
		this.scrollY = y;
		
		if(this.content != null) {
			this.content.setPosition(-x, -y);
		}
	}
	
	public float getScrollX() {
		return this.scrollX;
	}
	
	public void setScrollX(float scrollX) {
		this.setScroll(scrollX, this.scrollY);
	}
	
	public float getScrollY() {
		return this.scrollY;
	}
	
	public void setScrollY(float scrollY) {
		this.setScroll(this.scrollX, scrollY);
	}
}
