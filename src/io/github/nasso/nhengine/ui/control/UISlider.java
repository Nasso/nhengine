package io.github.nasso.nhengine.ui.control;

import io.github.nasso.nhengine.core.Nhengine;
import io.github.nasso.nhengine.graphics.Color;
import io.github.nasso.nhengine.graphics.GraphicsContext2D;
import io.github.nasso.nhengine.ui.UIComponent;
import io.github.nasso.nhengine.utils.MathUtils;

public class UISlider extends UIComponent {
	private Color foreground = null;
	
	private float value = 0;
	
	private float scrollStep = 0.05f;
	
	private float barHeight = USE_THEME_VALUE;
	private float borderRadius = USE_THEME_VALUE;
	private float dotRadius = USE_THEME_VALUE;
	private float dotWidth = USE_THEME_VALUE;
	private float dotHeight = USE_THEME_VALUE;
	
	public UISlider(float val) {
		this.setPadding(8, 0);
		this.setOpaque(false);
		this.setValue(val);
	}
	
	public UISlider() {
		this(0.5f);
	}
	
	protected void paintComponent(GraphicsContext2D gtx) {
		float w = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
		float h = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
		
		gtx.setFill(this.getBackground());
		gtx.fillRoundedRect(0, this.getPaddingTop() + h / 2f - this.getBarHeight() / 2f, this.getWidth(), this.getBarHeight(), this.getBorderRadius());
		
		float dotFreeArea = w - this.getDotWidth();
		
		float dotX = this.getDotWidth() / 2f + this.getPaddingLeft() + dotFreeArea * this.value - this.getDotWidth() / 2f;
		float dotY = this.getPaddingTop() + h / 2f - this.getDotHeight() / 2f;
		
		gtx.setFill(this.getForeground());
		gtx.setStroke(this.getBackground());
		gtx.fillRoundedRect(dotX, dotY, this.getDotWidth(), this.getDotHeight(), this.getDotRadius());
		gtx.strokeRoundedRect(dotX, dotY, this.getDotWidth(), this.getDotHeight(), this.getDotRadius());
	}
	
	public boolean mouseWheelMoved(float x, float y, float scrollX, float scrollY) {
		this.setValue(this.getValue() + scrollY * this.getScrollStep());
		return true;
	}
	
	private void userSet(float x) {
		float w = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
		float dotFreeArea = w - this.getDotWidth();
		
		this.setValue((x - this.getDotWidth() / 2f) / dotFreeArea);
	}
	
	public boolean mouseButtonPressed(float x, float y, int btn) {
		if(btn == Nhengine.MOUSE_BUTTON_LEFT) this.userSet(x);
		return true;
	}
	
	public boolean mouseDragged(float x, float y, float rx, float ry, int btn) {
		if(btn == Nhengine.MOUSE_BUTTON_LEFT) this.userSet(x);
		return true;
	}
	
	public void computePreferredSize() {
		this.preferredSize.set(100, 32);
	}
	
	public float getBorderRadius() {
		return this.getTheme().getFloat("slider.borderRadius", this.borderRadius);
	}
	
	public void setBorderRadius(float rad) {
		this.borderRadius = rad;
		this.repaint();
	}
	
	public float getValue() {
		return this.value;
	}
	
	public void setValue(float value) {
		this.value = MathUtils.clamp(value, 0.0f, 1.0f);
		this.repaint();
	}
	
	public Color getForeground() {
		return this.getTheme().getColor("slider.foreground", this.foreground);
	}
	
	public Color getBackground() {
		return this.getTheme().getColor("slider.background", this.background);
	}
	
	public void setForeground(Color foreground) {
		this.foreground = foreground;
	}
	
	public float getDotRadius() {
		return this.getTheme().getFloat("slider.dot.borderRadius", this.dotRadius);
	}
	
	public void setDotRadius(float dotRadius) {
		this.dotRadius = dotRadius;
	}
	
	public float getDotWidth() {
		return this.getTheme().getFloat("slider.dot.width", this.dotWidth);
	}
	
	public void setDotWidth(float dotWidth) {
		this.dotWidth = dotWidth;
	}
	
	public float getDotHeight() {
		return this.getTheme().getFloat("slider.dot.height", this.dotHeight);
	}
	
	public void setDotHeight(float dotHeight) {
		this.dotHeight = dotHeight;
	}
	
	public float getBarHeight() {
		return this.getTheme().getFloat("slider.barHeight", this.barHeight);
	}
	
	public void setBarHeight(float barHeight) {
		this.barHeight = barHeight;
	}
	
	public float getScrollStep() {
		return this.scrollStep;
	}
	
	public void setScrollStep(float scrollStep) {
		this.scrollStep = scrollStep;
	}
}
