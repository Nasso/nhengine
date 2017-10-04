package io.github.nasso.nhengine.ui.control;

import org.joml.Vector2f;

import io.github.nasso.nhengine.core.Cursor;
import io.github.nasso.nhengine.core.Nhengine;
import io.github.nasso.nhengine.graphics.Color;
import io.github.nasso.nhengine.graphics.GraphicsContext2D;
import io.github.nasso.nhengine.ui.UIComponent;
import io.github.nasso.nhengine.ui.UIContainer;
import io.github.nasso.nhengine.ui.layout.UILayout;
import io.github.nasso.nhengine.utils.MathUtils;

public class UISplitPane extends UIContainer {
	private class SplitLayout implements UILayout {
		public void apply(UIContainer parent) {
			float left = UISplitPane.this.getPaddingLeft();
			float top = UISplitPane.this.getPaddingTop();
			float right = UISplitPane.this.getPaddingRight();
			float bottom = UISplitPane.this.getPaddingBottom();
			float pw = UISplitPane.this.getWidth();
			float ph = UISplitPane.this.getHeight();
			float sw = UISplitPane.this.getSplitterWidth();
			
			float sp = UISplitPane.this.splitPoint;
			float w = pw - left - right;
			float h = ph - top - bottom;
			
			boolean vertical = UISplitPane.this.vertical;
			Splitter splitter = UISplitPane.this.splitter;
			UIComponent a = UISplitPane.this.a;
			UIComponent b = UISplitPane.this.b;
			
			if(vertical) {
				splitter.setBounds(0, top + sp * h - sw * 0.5f, pw, sw);
				
				if(a != null) {
					a.setBounds(left, top, w, sp * h - sw * 0.5f - bottom);
				}
				
				if(b != null) {
					b.setBounds(left, top + sp * h + sw * 0.5f + bottom, w, h * (1.0f - sp) - sw * 0.5f - bottom);
				}
			} else {
				splitter.setBounds(left + sp * w - sw * 0.5f, 0, sw, ph);
				
				if(a != null) {
					a.setBounds(left, top, sp * w - sw * 0.5f - right, h);
				}
				
				if(b != null) {
					b.setBounds(left + sp * w + sw * 0.5f + right, top, w * (1.0f - sp) - sw * 0.5f - right, h);
				}
			}
		}

		public void getPreferredSize(UIContainer parent, Vector2f dest) {
		}

		public void addComponent(int index, UIComponent comp, Object constraints) {
		}

		public void removeComponent(UIComponent comp) {
		}

		public void removeAllComponents() {
		}
	}
	
	private class Splitter extends UIComponent {
		public Splitter() {
		}
		
		protected void paintComponent(GraphicsContext2D gtx) {
			gtx.setFill(this.getBackground());
			gtx.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		
		public Color getBackground() {
			return this.getTheme() == null ? super.background : this.getTheme().getColor("splitter.background", super.background);
		}
		
		public void computePreferredSize() {
			this.preferredSize.set(UISplitPane.this.getSplitterWidth());
		}
		
		public boolean mouseDragged(float x, float y, float relX, float relY, int button) {
			if(button != Nhengine.MOUSE_BUTTON_LEFT) return true;
			
			float a = UISplitPane.this.vertical ? (this.getY() + y) / UISplitPane.this.getHeight() : (this.getX() + x) / UISplitPane.this.getWidth();
			
			UISplitPane.this.setSplitPoint(a);
			return true;
		}
	}
	
	private float splitterWidth = USE_THEME_VALUE;
	private float splitPoint = 0.5f;
	private boolean vertical = false;
	private Splitter splitter;
	private UIComponent a, b;
	
	public UISplitPane() {
		this.add(this.splitter = new Splitter());
		this.setVertical(this.vertical);
		
		this.setLayout(new SplitLayout());
	}

	public void setA(UIComponent a) {
		if(this.a == a) return;
		
		this.remove(this.a);
		this.a = a;
		this.add(this.a);
	}

	public void setB(UIComponent b) {
		if(this.b == b) return;
		
		this.remove(this.b);
		this.b = b;
		this.add(this.b);
	}

	public UIComponent getA() {
		return this.a;
	}

	public UIComponent getB() {
		return this.b;
	}

	public boolean isVertical() {
		return this.vertical;
	}

	public void setVertical(boolean vertical) {
		this.vertical = vertical;
		this.splitter.setCursor(vertical ? Cursor.getVResizeCursor() : Cursor.getHResizeCursor());
	}

	public float getSplitPoint() {
		return this.splitPoint;
	}

	public void setSplitPoint(float splitPoint) {
		this.splitPoint = MathUtils.clamp(splitPoint, 0, 1);
		this.repack();
	}

	public float getSplitterWidth() {
		return this.getTheme().getFloat("splitter.width", this.splitterWidth);
	}

	public void setSplitterWidth(float splitterWidth) {
		this.splitterWidth = splitterWidth;
		this.repaint();
	}
}
