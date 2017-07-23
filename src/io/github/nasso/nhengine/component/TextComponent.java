package io.github.nasso.nhengine.component;

import org.joml.Vector2f;

import io.github.nasso.nhengine.graphics.Font;
import io.github.nasso.nhengine.graphics.LineCap;
import io.github.nasso.nhengine.graphics.LineJoin;
import io.github.nasso.nhengine.graphics.Paint;
import io.github.nasso.nhengine.graphics.TextAlignment;
import io.github.nasso.nhengine.graphics.TextBaseline;
import io.github.nasso.nhengine.utils.Box2D;
import io.github.nasso.nhengine.utils.Nhutils;

public class TextComponent extends DrawableComponent {
	private Vector2f _vec2 = new Vector2f();
	
	private CharSequence text = null;
	private Font font = null;
	
	private TextBaseline baseline = TextBaseline.BASELINE;
	private TextAlignment alignment = TextAlignment.LEFT;
	
	private Box2D boundingBox = null;
	
	private Paint strokePaint;
	private Paint fillPaint;
	
	private LineCap lineCap = LineCap.BUTT;
	private LineJoin lineJoin = LineJoin.MITER;
	
	private boolean strokeBeforeFill = false;
	private float strokeSize = 1;
	
	public TextComponent(CharSequence text, Font font) {
		this.text = text;
		this.font = font;
		
		this.setOpaque(false);
	}
	
	public boolean isStrokeBeforeFill() {
		return this.strokeBeforeFill;
	}
	
	public void setStrokeBeforeFill(boolean strokeBeforeFill) {
		this.strokeBeforeFill = strokeBeforeFill;
	}
	
	public Paint getStroke() {
		return this.strokePaint;
	}
	
	public void setStroke(Paint strokePaint) {
		this.strokePaint = strokePaint;
	}
	
	public Paint getFill() {
		return this.fillPaint;
	}
	
	public void setFill(Paint fillPaint) {
		this.fillPaint = fillPaint;
	}
	
	public float getStrokeSize() {
		return this.strokeSize;
	}
	
	public void setStrokeSize(float strokeSize) {
		this.strokeSize = strokeSize;
	}
	
	public LineCap getLineCap() {
		return this.lineCap;
	}
	
	public void setLineCap(LineCap lineCap) {
		this.lineCap = lineCap;
	}
	
	public LineJoin getLineJoin() {
		return this.lineJoin;
	}
	
	public void setLineJoin(LineJoin lineJoin) {
		this.lineJoin = lineJoin;
	}
	
	public CharSequence getText() {
		return this.text;
	}
	
	public void setText(CharSequence text) {
		this.text = text;
		if(this.boundingBox != null) this.computeBoundingBox();
	}
	
	private Box2D computeBoundingBox() {
		if(this.font == null) {
			this.boundingBox.redefine(0, 0, 0, 0);
			return this.boundingBox;
		}
		
		Nhutils.measureText(this.font, this.text, this._vec2);
		
		float x = 0;
		float y = 0;
		
		switch(this.alignment) {
			case CENTER:
				x = -this._vec2.x * 0.5f;
				break;
			case LEFT:
				x = 0;
				break;
			case RIGHT:
				x = this._vec2.x;
				break;
		}
		
		switch(this.baseline) {
			case ASCENT_MIDDLE:
				y = this.font.getAscent() - this._vec2.y * 0.5f;
				break;
			case BASELINE:
				y = -this.font.getAscent();
				break;
			case BOTTOM:
				y = -this._vec2.y;
				break;
			case MIDDLE:
				y = -this._vec2.y * 0.5f;
				break;
			case TOP:
				y = 0;
				break;
		}
		
		if(this.boundingBox == null) this.boundingBox = new Box2D();
		this.boundingBox.redefineXYWH(x, y, this._vec2.x, this._vec2.y);
		return this.boundingBox;
	}
	
	public Box2D getBoundingBox() {
		return this.boundingBox == null ? this.computeBoundingBox() : this.boundingBox;
	}
	
	public Font getFont() {
		return this.font;
	}
	
	public void setFont(Font font) {
		this.font = font;
	}
	
	public TextBaseline getBaseline() {
		return this.baseline;
	}
	
	public void setBaseline(TextBaseline baseline) {
		this.baseline = baseline;
	}
	
	public TextAlignment getAlignment() {
		return this.alignment;
	}
	
	public void setAlignment(TextAlignment alignment) {
		this.alignment = alignment;
	}
}
