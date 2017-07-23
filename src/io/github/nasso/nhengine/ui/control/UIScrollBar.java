package io.github.nasso.nhengine.ui.control;

import java.util.function.Consumer;

import io.github.nasso.nhengine.core.Nhengine;
import io.github.nasso.nhengine.graphics.Color;
import io.github.nasso.nhengine.graphics.GraphicsContext2D;
import io.github.nasso.nhengine.ui.UIComponent;
import io.github.nasso.nhengine.ui.UIContainer;
import io.github.nasso.nhengine.ui.layout.UIBorderLayout;
import io.github.nasso.nhengine.utils.FloatTransition;
import io.github.nasso.nhengine.utils.MathUtils;

public class UIScrollBar extends UIContainer {
	public class ArrowButton extends UIComponent {
		private static final float ARROW_WS = 0.2f;
		private static final float ARROW_HS = 0.1f;
		
		private boolean vertical = false;
		private boolean topLeft = false;
		
		private Color foreground = null;
		
		public ArrowButton(boolean vertical, boolean topLeft) {
			this.vertical = vertical;
			this.topLeft = topLeft;
		}
		
		protected void paintComponent(GraphicsContext2D gtx) {
			float w = this.getWidth();
			float h = this.getHeight();
			
			gtx.setFill(this.getBackground());
			gtx.fillRect(0, 0, w, h);
			
			gtx.translate(w * 0.5f, h * 0.5f);
			
			gtx.setStroke(this.getForeground());
			gtx.beginPath();
			if(this.vertical) {
				if(this.topLeft) {
					gtx.moveTo(-w * ARROW_WS, h * ARROW_HS);
					gtx.lineTo(0, -h * ARROW_HS);
					gtx.lineTo(w * ARROW_WS, h * ARROW_HS);
				} else {
					gtx.moveTo(-w * ARROW_WS, -h * ARROW_HS);
					gtx.lineTo(0, h * ARROW_HS);
					gtx.lineTo(w * ARROW_WS, -h * ARROW_HS);
				}
			} else {
				if(this.topLeft) {
					gtx.moveTo(w * ARROW_HS, -h * ARROW_WS);
					gtx.lineTo(-w * ARROW_HS, 0);
					gtx.lineTo(w * ARROW_HS, h * ARROW_WS);
				} else {
					gtx.moveTo(-w * ARROW_HS, -h * ARROW_WS);
					gtx.lineTo(w * ARROW_HS, 0);
					gtx.lineTo(-w * ARROW_HS, h * ARROW_WS);
				}
			}
			gtx.stroke();
		}
		
		public boolean mouseClicked(int btn) {
			UIScrollBar.this.scroll(this.topLeft ? -UIScrollBar.this.arrowStep : UIScrollBar.this.arrowStep);
			return true;
		}
		
		public Color getBackground() {
			return this.background == null && this.getTheme() != null ? this.getTheme().getScrollBarArrowButtonBackground() : super.getBackground();
		}
		
		public void setForeground(Color c) {
			this.foreground = c;
		}
		
		public Color getForeground() {
			return this.foreground == null && this.getTheme() != null ? this.getTheme().getTextColor() : this.foreground;
		}
		
		public void computePreferredSize() {
			this.preferredSize.set(UIScrollBar.this.getBarSize());
		}
		
		public boolean isVertical() {
			return this.vertical;
		}
		
		public boolean isTopLeft() {
			return this.topLeft;
		}
		
		public void setTopLeft(boolean topLeft) {
			this.topLeft = topLeft;
		}
	}
	
	public class Thumb extends UIComponent {
		private Color color, borderColor;
		
		private float coverage = 0.5f;
		
		private float borderSize = USE_THEME_VALUE;
		private float borderRadius = USE_THEME_VALUE;
		
		public Thumb(boolean vertical) {
		}
		
		protected void paintComponent(GraphicsContext2D gtx) {
			float w = this.getWidth();
			float h = this.getHeight();
			float bs = this.getBorderSize();
			
			gtx.setFill(this.getColor());
			gtx.setStroke(this.getBorderColor());
			gtx.setStrokeSize(bs);
			
			gtx.beginPath();
			gtx.roundedRect(bs * 0.5f, bs * 0.5f, w - bs, h - bs, this.getBorderRadius());
			gtx.fill();
			gtx.stroke();
		}
		
		public boolean mouseDragged(float x, float y, float relX, float relY, int btn) {
			if(btn != Nhengine.MOUSE_BUTTON_LEFT) return true;
			
			float rel = UIScrollBar.this.vertical ? relY : relX;
			float size = (UIScrollBar.this.vertical ? (this.parent.getHeight() - this.getHeight()) : (this.parent.getWidth() - this.getWidth()));
			UIScrollBar.this.scrollImmediatly(rel / size);
			
			return true;
		}
		
		public void computePreferredSize() {
			if(UIScrollBar.this.vertical) {
				this.preferredSize.x = UIScrollBar.this.getBarSize();
				this.preferredSize.y = this.coverage * this.parent.getHeight();
			} else {
				this.preferredSize.x = this.coverage * this.parent.getWidth();
				this.preferredSize.y = UIScrollBar.this.getBarSize();
			}
		}
		
		public float getCoverage() {
			return this.coverage;
		}
		
		public void setCoverage(float coverage) {
			if(this.coverage == coverage) return;
			
			this.coverage = coverage;
		}
		
		public void setCurrentValue(float currentValue) {
			if(UIScrollBar.this.vertical) {
				this.setPosition(0, currentValue * (this.parent.getHeight() - this.getHeight()));
			} else {
				this.setPosition(currentValue * (this.parent.getWidth() - this.getWidth()), 0);
			}
		}
		
		public Color getColor() {
			return this.color == null && this.getTheme() != null ? this.getTheme().getScrollBarThumbColor() : this.color;
		}
		
		public void setColor(Color color) {
			this.color = color;
		}
		
		public Color getBorderColor() {
			return this.borderColor == null && this.getTheme() != null ? this.getTheme().getScrollBarThumbBorderColor() : this.borderColor;
		}
		
		public void setBorderColor(Color borderColor) {
			this.borderColor = borderColor;
		}
		
		public float getBorderSize() {
			return Float.isNaN(this.borderSize) && this.getTheme() != null ? this.getTheme().getScrollBarThumbBorderSize() : this.borderSize;
		}
		
		public void setBorderSize(float borderSize) {
			this.borderSize = borderSize;
		}
		
		public float getBorderRadius() {
			return Float.isNaN(this.borderRadius) && this.getTheme() != null ? this.getTheme().getScrollBarThumbBorderRadius() : this.borderRadius;
		}
		
		public void setBorderRadius(float borderRadius) {
			this.borderRadius = borderRadius;
		}
	}
	
	private class BarCenter extends UIContainer {
		private Thumb thumb;
		
		public BarCenter(boolean vertical) {
			this.thumb = new Thumb(vertical);
			this.add(this.thumb);
		}
		
		public Color getBackground() {
			return this.background == null && this.getTheme() != null ? this.getTheme().getScrollBarBackground() : super.getBackground();
		}
		
		public Thumb getThumb() {
			return this.thumb;
		}
	}
	
	private boolean vertical = false;
	
	private float barSize = USE_THEME_VALUE;
	
	private float currentValue = 0;
	private float arrowStep = 0.05f;
	
	private Consumer<UIScrollBar> onValueChanged;
	
	private FloatTransition valueTransition = new FloatTransition(0.2f, this::updateScrollTransition, 0);
	
	private ArrowButton topLeftArrow;
	private BarCenter center;
	private ArrowButton bottomRightArrow;
	
	public UIScrollBar(boolean vertical) {
		
		this.vertical = vertical;
		this.topLeftArrow = new ArrowButton(this.vertical, true);
		this.bottomRightArrow = new ArrowButton(this.vertical, false);
		this.center = new BarCenter(this.vertical);
		
		this.setLayout(new UIBorderLayout(this.vertical ? 0 : 2, this.vertical ? 2 : 0));
		
		this.add(this.topLeftArrow, this.vertical ? UIBorderLayout.NORTH : UIBorderLayout.WEST);
		this.add(this.bottomRightArrow, this.vertical ? UIBorderLayout.SOUTH : UIBorderLayout.EAST);
		this.add(this.center, UIBorderLayout.CENTER);
	}
	
	public UIScrollBar() {
		this(false);
	}
	
	private void updateScrollTransition(float val) {
		this.currentValue = MathUtils.clamp(val, 0, 1);
		
		this.center.getThumb().setCurrentValue(this.currentValue);
		
		if(this.onValueChanged != null) this.onValueChanged.accept(this);
	}
	
	public ArrowButton getTopLeftArrowButton() {
		return this.topLeftArrow;
	}
	
	public ArrowButton getBottomRightArrowButton() {
		return this.bottomRightArrow;
	}
	
	public Thumb getThumb() {
		return this.center.getThumb();
	}
	
	public void setBarSize(float size) {
		this.barSize = size;
	}
	
	public float getBarSize() {
		return Float.isNaN(this.barSize) && this.getTheme() != null ? this.getTheme().getScrollBarSize() : this.barSize;
	}
	
	public boolean isVertical() {
		return this.vertical;
	}
	
	public float getCurrentValue() {
		return this.currentValue;
	}
	
	public void scroll(float value) {
		this.setCurrentValue(this.valueTransition.getTargetValue() + value);
	}
	
	public void scrollImmediatly(float value) {
		this.setCurrentValueImmediatly(this.valueTransition.getTargetValue() + value);
	}
	
	public void setCurrentValueImmediatly(float val) {
		val = MathUtils.clamp(val, 0, 1);
		
		this.valueTransition.setTargetValue(val);
		this.valueTransition.setCurrentTime(this.valueTransition.getDuration());
	}
	
	public void setCurrentValue(float val) {
		val = MathUtils.clamp(val, 0, 1);
		if(this.valueTransition.getTargetValue() == val) return;
		
		this.valueTransition.setTargetValue(val);
	}
	
	public float getThumbCoverage() {
		return this.center.getThumb().getCoverage();
	}
	
	public void setThumbCoverage(float thumbCoverage) {
		this.center.getThumb().setCoverage(thumbCoverage);
	}
	
	public float getArrowStep() {
		return this.arrowStep;
	}
	
	public void setArrowStep(float arrowStep) {
		this.arrowStep = arrowStep;
	}
	
	public Consumer<UIScrollBar> getOnValueChanged() {
		return this.onValueChanged;
	}
	
	public void setOnValueChanged(Consumer<UIScrollBar> onValueChanged) {
		this.onValueChanged = onValueChanged;
	}
}
