package io.github.nasso.nhengine.component;

import org.joml.Vector2f;

import io.github.nasso.nhengine.graphics.Font;
import io.github.nasso.nhengine.graphics.Paint;
import io.github.nasso.nhengine.graphics.TextAlignment;
import io.github.nasso.nhengine.graphics.TextBaseline;
import io.github.nasso.nhengine.utils.Box2D;
import io.github.nasso.nhengine.utils.Nhutils;

/**
 * A text component is a component that displays text. You can specify a font to use, a baseline, and the alignment.
 * You can also set a fill and stroke {@link Paint}.
 * 
 * @author nasso
 */
public class TextComponent extends DrawableComponent {
	private Vector2f _vec2 = new Vector2f();
	
	private CharSequence text = null;
	private Font font = null;
	
	private TextBaseline baseline = TextBaseline.BASELINE;
	private TextAlignment alignment = TextAlignment.LEFT;
	
	private Box2D boundingBox = null;
	
	private Paint strokePaint;
	private Paint fillPaint;
	
	private boolean strokeBeforeFill = false;
	private float strokeSize = 1;
	
	/**
	 * Constructs a text component with the given text and font.
	 * 
	 * @param text
	 *            The text.
	 * @param font
	 *            The font.
	 */
	public TextComponent(CharSequence text, Font font) {
		this.text = text;
		this.font = font;
		
		this.setOpaque(false);
	}
	
	/**
	 * When true, the text will be stroked before getting filled, so the outline will be half covered with the fill color (if any).<br>
	 * Default to false.
	 * 
	 * @return True if the outline should be drawn before the fill.
	 */
	public boolean isStrokeBeforeFill() {
		return this.strokeBeforeFill;
	}
	
	/**
	 * When true, the text will be stroked before getting filled, so the outline will be half covered with the fill color (if any).<br>
	 * Default to false.
	 * 
	 * @return True if the outline should be drawn before the fill.
	 */
	public void setStrokeBeforeFill(boolean strokeBeforeFill) {
		this.strokeBeforeFill = strokeBeforeFill;
	}
	
	/**
	 * @return The current stroke paint.
	 */
	public Paint getStroke() {
		return this.strokePaint;
	}
	
	/**
	 * @param strokePaint
	 *            The new stroke paint.
	 */
	public void setStroke(Paint strokePaint) {
		this.strokePaint = strokePaint;
	}
	
	/**
	 * @return The current fill paint.
	 */
	public Paint getFill() {
		return this.fillPaint;
	}
	
	/**
	 * @param fillPaint
	 *            The new fill paint.
	 */
	public void setFill(Paint fillPaint) {
		this.fillPaint = fillPaint;
	}
	
	/**
	 * @return The current stroke size.
	 */
	public float getStrokeSize() {
		return this.strokeSize;
	}
	
	/**
	 * @param strokeSize
	 *            The new stroke size.
	 */
	public void setStrokeSize(float strokeSize) {
		this.strokeSize = strokeSize;
	}
	
	/**
	 * @return The current text.
	 */
	public CharSequence getText() {
		return this.text;
	}
	
	/**
	 * The text is set by reference (no copy), so you can use a {@link StringBuffer} and modify it, the displayed text will change as well.
	 * 
	 * @param text
	 *            The new text.
	 */
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
	
	/**
	 * @return The current font.
	 */
	public Font getFont() {
		return this.font;
	}
	
	/**
	 * @param font
	 *            The new font.
	 */
	public void setFont(Font font) {
		this.font = font;
	}
	
	/**
	 * @return The current text baseline (vertical alignment).
	 */
	public TextBaseline getBaseline() {
		return this.baseline;
	}
	
	/**
	 * @param baseline
	 *            The new text baseline (vertical alignment).
	 */
	public void setBaseline(TextBaseline baseline) {
		this.baseline = baseline;
	}
	
	/**
	 * @return The current horizontal text alignment.
	 */
	public TextAlignment getAlignment() {
		return this.alignment;
	}
	
	/**
	 * @param alignment
	 *            The new horizontal text alignment.
	 */
	public void setAlignment(TextAlignment alignment) {
		this.alignment = alignment;
	}
}
