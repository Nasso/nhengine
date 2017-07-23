package io.github.nasso.nhengine.ui.control;

import io.github.nasso.nhengine.graphics.FontFamily.FontStyle;
import io.github.nasso.nhengine.graphics.GraphicsContext2D;
import io.github.nasso.nhengine.graphics.TextAlignment;
import io.github.nasso.nhengine.graphics.TextBaseline;
import io.github.nasso.nhengine.utils.Nhutils;

public class UILabel extends UITextComponent {
	private int textAnchor = ANCHOR_CENTER;
	
	public UILabel(CharSequence text, int anchor, FontStyle style) {
		super(text);
		this.textAnchor = anchor;
		this.setFontStyle(style);
	}
	
	public UILabel(CharSequence text, FontStyle style) {
		this(text, ANCHOR_CENTER, style);
	}
	
	public UILabel(CharSequence text, int anchor) {
		this(text, anchor, null);
	}
	
	public UILabel(CharSequence text) {
		this(text, ANCHOR_CENTER);
	}
	
	public UILabel() {
		this(null);
	}
	
	protected void paintComponent(GraphicsContext2D gtx) {
		gtx.setFont(this.getFont());
		gtx.setFill(this.getTextColor());
		
		String cutText = Nhutils.cutStringWidth(this.getFont(), this.getText(), this.getWidth(), this.textAnchor == ANCHOR_RIGHT || this.textAnchor == ANCHOR_BOTTOM_RIGHT || this.textAnchor == ANCHOR_TOP_RIGHT).toString();
		
		switch(this.textAnchor) {
			case ANCHOR_TOP_LEFT:
				gtx.setTextBaseline(TextBaseline.TOP);
				gtx.setTextAlign(TextAlignment.LEFT);
				gtx.fillText(cutText, this.getPaddingLeft(), this.getPaddingTop() - 1);
				break;
			
			case ANCHOR_TOP:
				gtx.setTextBaseline(TextBaseline.TOP);
				gtx.setTextAlign(TextAlignment.CENTER);
				gtx.fillText(cutText, this.getWidth() / 2f, this.getPaddingTop() - 1);
				break;
			
			case ANCHOR_TOP_RIGHT:
				gtx.setTextBaseline(TextBaseline.TOP);
				gtx.setTextAlign(TextAlignment.RIGHT);
				gtx.fillText(cutText, this.getWidth() - this.getPaddingRight(), this.getPaddingTop() - 1);
				break;
			
			case ANCHOR_LEFT:
				gtx.setTextBaseline(TextBaseline.ASCENT_MIDDLE);
				gtx.setTextAlign(TextAlignment.LEFT);
				gtx.fillText(cutText, this.getPaddingLeft(), this.getHeight() / 2f - 1);
				break;
			
			case ANCHOR_CENTER:
				gtx.setTextBaseline(TextBaseline.ASCENT_MIDDLE);
				gtx.setTextAlign(TextAlignment.CENTER);
				gtx.fillText(cutText, this.getWidth() / 2f, this.getHeight() / 2f - 1);
				break;
			
			case ANCHOR_RIGHT:
				gtx.setTextBaseline(TextBaseline.ASCENT_MIDDLE);
				gtx.setTextAlign(TextAlignment.RIGHT);
				gtx.fillText(cutText, this.getWidth() - this.getPaddingRight(), this.getHeight() / 2f - 1);
				break;
			
			case ANCHOR_BOTTOM_LEFT:
				gtx.setTextBaseline(TextBaseline.BOTTOM);
				gtx.setTextAlign(TextAlignment.LEFT);
				gtx.fillText(cutText, this.getPaddingLeft(), this.getHeight() - this.getPaddingBottom() - 1);
				break;
			
			case ANCHOR_BOTTOM:
				gtx.setTextBaseline(TextBaseline.BOTTOM);
				gtx.setTextAlign(TextAlignment.CENTER);
				gtx.fillText(cutText, this.getWidth() / 2f, this.getHeight() - this.getPaddingBottom() - 1);
				break;
			
			case ANCHOR_BOTTOM_RIGHT:
				gtx.setTextBaseline(TextBaseline.BOTTOM);
				gtx.setTextAlign(TextAlignment.RIGHT);
				gtx.fillText(cutText, this.getWidth() - this.getPaddingRight(), this.getHeight() - this.getPaddingBottom() - 1);
				break;
		}
	}
	
	public void computePreferredSize() {
		this.preferredSize.zero();
		
		if(this.getFont() != null && this.getText() != null) {
			Nhutils.measureText(this.getFont(), this.getText(), this.preferredSize);
		}
		
		this.preferredSize.x += this.getPaddingLeft() + this.getPaddingRight();
		this.preferredSize.y += this.getPaddingTop() + this.getPaddingBottom();
	}
	
	public int getTextAnchor() {
		return this.textAnchor;
	}
	
	public void setTextAnchor(int textAnchor) {
		this.textAnchor = textAnchor;
	}
}
