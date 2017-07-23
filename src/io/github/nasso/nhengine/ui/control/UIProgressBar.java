package io.github.nasso.nhengine.ui.control;

import io.github.nasso.nhengine.graphics.Color;
import io.github.nasso.nhengine.graphics.GraphicsContext2D;
import io.github.nasso.nhengine.ui.UIComponent;
import io.github.nasso.nhengine.utils.MathUtils;

public class UIProgressBar extends UIComponent {
	private Color foreground = null;
	
	private float value = 0;
	private float borderRadius = USE_THEME_VALUE;
	
	public UIProgressBar(float value) {
		this.setPadding(2);
		this.setOpaque(false);
		this.setValue(value);
	}
	
	public UIProgressBar() {
		this(0);
	}
	
	protected void paintComponent(GraphicsContext2D gtx) {
		float w = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
		float h = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
		
		gtx.setFill(this.getBackground());
		gtx.fillRoundedRect(0, 0, this.getWidth(), this.getHeight(), this.getBorderRadius());
		
		gtx.setFill(this.getForeground());
		
		float foreRadius = this.getBorderRadius() - this.getPaddingTop();
		float foreW = w * this.value;
		
		if(foreW >= foreRadius * 2) {
			gtx.fillRoundedRect(this.getPaddingLeft(), this.getPaddingTop(), foreW, h, foreRadius);
		} else if(foreW != 0) {
			float angle = (float) Math.acos(foreW / (foreRadius * 2) * 2 - 1);
			
			gtx.beginPath();
			gtx.arc(this.getPaddingLeft() + foreRadius, this.getPaddingTop() + foreRadius, foreRadius, -angle, MathUtils.PI, false);
			gtx.lineTo(this.getPaddingLeft(), this.getPaddingTop() + foreRadius);
			gtx.arc(this.getPaddingLeft() + foreRadius, this.getPaddingTop() + h - foreRadius, foreRadius, MathUtils.PI, angle, false);
			gtx.fill();
		}
	}
	
	public void computePreferredSize() {
		this.preferredSize.set(100, 32);
	}
	
	public float getBorderRadius() {
		return Float.isNaN(this.borderRadius) && this.getTheme() != null ? this.getTheme().getProgressBarBorderRadius() : this.borderRadius;
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
		return this.foreground == null && this.getTheme() != null ? this.getTheme().getProgressBarForegroundColor() : this.foreground;
	}
	
	public Color getBackground() {
		return this.background == null && this.getTheme() != null ? this.getTheme().getProgressBarBackgroundColor() : this.background;
	}
	
	public void setForeground(Color foreground) {
		this.foreground = foreground;
	}
	
	public void setProgressBarBackground(Color background) {
		this.background = background;
	}
}
