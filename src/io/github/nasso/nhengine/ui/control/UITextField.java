package io.github.nasso.nhengine.ui.control;

import io.github.nasso.nhengine.core.Cursor;
import io.github.nasso.nhengine.graphics.Color;
import io.github.nasso.nhengine.graphics.GraphicsContext2D;
import io.github.nasso.nhengine.graphics.TextBaseline;
import io.github.nasso.nhengine.utils.FloatTransition;
import io.github.nasso.nhengine.utils.MathUtils;
import io.github.nasso.nhengine.utils.Nhutils;
import io.github.nasso.nhengine.utils.TextInputEngine;
import penner.easing.Linear;

public class UITextField extends UITextComponent {
	private Color borderColor = null;
	private Color selectionColor = null;
	
	private float borderSize = USE_THEME_VALUE;
	private float borderRadius = USE_THEME_VALUE;
	
	private CharSequence placeholder = "Type something...";
	private StringBuffer text = new StringBuffer();
	
	private TextInputEngine inputEngine;
	
	private FloatTransition scrollTransition = new FloatTransition(0.1f, this::setScrollX, 0, Linear::easeNone);
	private FloatTransition caretTransition = new FloatTransition(0.1f, this::setCaret, 0, Linear::easeNone);
	private FloatTransition selectTransition = new FloatTransition(0.1f, this::setSelect, 0, Linear::easeNone);
	private float scrollX = 0;
	private float caretOffsetX = 0;
	private float selectOffsetX = 0;
	
	private int preferredVisibleColumns = 0;
	
	public UITextField() {
		this(15);
	}
	
	public UITextField(int columns) {
		this.setPreferredVisibleColumns(columns);
		this.setText(this.text);
		this.setPadding(8);
		this.setOpaque(false);
		
		this.setCursor(Cursor.getIBeamCursor());
		
		this.inputEngine = new TextInputEngine(this.text, false);
	}
	
	private void setScrollX(float scrollX) {
		this.scrollX = scrollX;
		this.repaint();
	}
	
	private void setCaret(float caret) {
		this.caretOffsetX = caret;
		this.repaint();
	}
	
	private void setSelect(float select) {
		this.selectOffsetX = select;
		this.repaint();
	}
	
	protected void paintComponent(GraphicsContext2D gtx) {
		float w = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
		float h = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
		
		gtx.setFill(this.getBackground());
		gtx.setStroke(this.getBorderColor());
		gtx.setStrokeSize(this.getBorderSize());
		gtx.fillRoundedRect(0, 0, this.getWidth(), this.getHeight(), this.getBorderRadius());
		gtx.strokeRoundedRect(0, 0, this.getWidth(), this.getHeight(), this.getBorderRadius());
		
		CharSequence textToDraw = this.getText().length() <= 0 ? this.hasFocus() ? null : this.placeholder : this.getText();
		
		gtx.clip(this.getPaddingLeft() - 1, this.getPaddingTop() - 1, w + 2, h + 2);
		gtx.translate(-this.scrollX, 0);
		
		if(textToDraw != null) {
			gtx.setFill(this.getTextColor());
			gtx.setFont(this.getFont());
			if(textToDraw == this.placeholder) gtx.setGlobalAlpha(0.5f);
			gtx.setTextBaseline(TextBaseline.MIDDLE);
			
			gtx.save();
			gtx.fillText(textToDraw.toString(), this.getPaddingLeft(), this.getPaddingTop() + h / 2f + 1);
			gtx.restore();
		}
		
		if(this.inputEngine.hasSelection()) {
			
			float startX = this.caretOffsetX < this.selectOffsetX ? this.caretOffsetX : this.selectOffsetX;
			float width = Math.abs(this.caretOffsetX - this.selectOffsetX);
			
			gtx.setFill(this.getSelectionColor());
			gtx.setStroke(this.getSelectionColor());
			gtx.setStrokeSize(2);
			gtx.fillRoundedRect(this.getPaddingLeft() + startX, this.getPaddingTop(), width, h, 2);
			gtx.strokeRoundedRect(this.getPaddingLeft() + startX, this.getPaddingTop(), width, h, 2);
		} else if(this.hasFocus()) {
			gtx.setStroke(this.getTextColor());
			gtx.setStrokeSize(1);
			gtx.strokeLine(this.getPaddingLeft() + this.caretOffsetX, this.getPaddingTop(), this.getPaddingLeft() + this.caretOffsetX, this.getPaddingTop() + h);
		}
	}
	
	private void updateCaret() {
		this.caretTransition.setTargetValue(this.inputEngine.getCaretPosition());
		this.selectTransition.setTargetValue(this.inputEngine.getSelectionPoint());
		
		float caretOffsetX = this.inputEngine.getCaretPosition() <= 0 ? 0 : Nhutils.measureTextWidth(this.getFont(), this.getText(), 0, this.inputEngine.getCaretPosition());
		float selectOffsetX = this.inputEngine.getSelectionPoint() <= 0 ? 0 : Nhutils.measureTextWidth(this.getFont(), this.getText(), 0, this.inputEngine.getSelectionPoint());
		
		this.caretTransition.setTargetValue(caretOffsetX);
		this.selectTransition.setTargetValue(selectOffsetX);
		
		// We use the selection point, because it's equal to the caret position when there's no selection
		float w = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
		
		if(!MathUtils.rangeContains(selectOffsetX, this.scrollX, this.scrollX + w)) {
			if(selectOffsetX < this.scrollX) {
				this.scrollTransition.setTargetValue(selectOffsetX);
			} else { // selectOffsetX > scrollX + this.getWidth()
				this.scrollTransition.setTargetValue(selectOffsetX - w);
			}
		}
	}
	
	public boolean mouseButtonPressed(float x, float y, int btn) {
		this.inputEngine.setCaretPosition(Nhutils.charIndexAt(this.getFont(), this.getText(), x - this.getPaddingLeft() + this.scrollX));
		this.updateCaret();
		this.repaint();
		
		return true;
	}
	
	public boolean mouseDragged(float x, float y, float relX, float relY, int button) {
		this.inputEngine.setSelectionPoint(Nhutils.charIndexAt(this.getFont(), this.getText(), x - this.getPaddingLeft() + this.scrollX));
		this.updateCaret();
		this.repaint();
		
		return true;
	}
	
	public void textInput(int codepoint) {
		this.inputEngine.textInput(codepoint);
		this.updateCaret();
		this.repaint();
	}
	
	public void keyTyped(int key) {
		this.inputEngine.keyTyped(key);
		this.updateCaret();
		this.repaint();
	}
	
	public void keyReleased(int key) {
		this.inputEngine.keyReleased(key);
		this.updateCaret();
		this.repaint();
	}
	
	public void focusGain() {
		this.repaint();
	}
	
	public void focusLost() {
		this.repaint();
	}
	
	public void computePreferredSize() {
		this.preferredSize.zero();
		
		if(this.getFont() != null) {
			this.preferredSize.x = this.getFont().getGlyphWidth('m') * this.getPreferredVisibleColumns();
			this.preferredSize.y = this.getFont().getHeight();
		}
		
		this.preferredSize.x += this.getPaddingLeft() + this.getPaddingRight();
		this.preferredSize.y += this.getPaddingTop() + this.getPaddingBottom();
	}
	
	public Color getBorderColor() {
		return this.borderColor == null && this.getTheme() != null ? this.getTheme().getTextFieldBorderColor() : this.borderColor;
	}
	
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}
	
	public Color getBackground() {
		return this.background == null && this.getTheme() != null ? this.getTheme().getTextFieldBackgroundColor() : this.background;
	}
	
	public float getBorderRadius() {
		return Float.isNaN(this.borderRadius) && this.getTheme() != null ? this.getTheme().getTextFieldBorderRadius() : this.borderRadius;
	}
	
	public void setBorderRadius(float rad) {
		this.borderRadius = rad;
		this.repaint();
	}
	
	public float getBorderSize() {
		return Float.isNaN(this.borderSize) && this.getTheme() != null ? this.getTheme().getTextFieldBorderSize() : this.borderSize;
	}
	
	public void setBorderSize(float borderSize) {
		this.borderSize = borderSize;
	}
	
	public CharSequence getPlaceholder() {
		return this.placeholder;
	}
	
	public void setPlaceholder(CharSequence placeholder) {
		this.placeholder = placeholder;
	}
	
	public Color getSelectionColor() {
		return this.selectionColor == null && this.getTheme() != null ? this.getTheme().getTextFieldSelectionColor() : this.selectionColor;
	}
	
	public void setSelectionColor(Color selectionColor) {
		this.selectionColor = selectionColor;
	}
	
	public int getPreferredVisibleColumns() {
		return this.preferredVisibleColumns;
	}
	
	public void setPreferredVisibleColumns(int preferredVisibleColumns) {
		this.preferredVisibleColumns = preferredVisibleColumns;
	}
}
