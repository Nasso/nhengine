package io.github.nasso.nhengine.ui.control;

import io.github.nasso.nhengine.graphics.Color;
import io.github.nasso.nhengine.graphics.GraphicsContext2D;
import io.github.nasso.nhengine.ui.UIComponent;

public class UISeparator extends UIComponent {
	private Color color = null;
	
	public UISeparator(Color col) {
		this.color = col;
	}
	
	public UISeparator(float paddingTop, float paddingRight, float paddingBottom, float paddingLeft) {
		this.setPadding(paddingTop, paddingRight, paddingBottom, paddingLeft);
	}
	
	public UISeparator(float paddingTopBottom, float paddingLeftRight) {
		this(paddingTopBottom, paddingLeftRight, paddingTopBottom, paddingLeftRight);
	}
	
	public UISeparator(float padding) {
		this(padding, padding);
	}
	
	public UISeparator() {
		this(8);
	}
	
	protected void paintComponent(GraphicsContext2D gtx) {
		gtx.setFill(this.getColor());
		gtx.fillRect(this.getPaddingLeft(), this.getPaddingTop(), this.getWidth() - this.getPaddingLeft() - this.getPaddingRight(), this.getHeight() - this.getPaddingTop() - this.getPaddingBottom());
	}
	
	public void computePreferredSize() {
		this.preferredSize.set(1);
		
		this.preferredSize.x += this.getPaddingLeft();
		this.preferredSize.x += this.getPaddingRight();
		this.preferredSize.y += this.getPaddingTop();
		this.preferredSize.y += this.getPaddingBottom();
	}
	
	public Color getColor() {
		return this.color == null && this.getTheme() != null ? this.getTheme().getSeparatorColor() : this.color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
}
