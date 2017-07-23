package io.github.nasso.nhengine.ui.control;

import org.joml.Vector2f;

import io.github.nasso.nhengine.ui.UIComponent;
import io.github.nasso.nhengine.ui.UIContainer;
import io.github.nasso.nhengine.ui.layout.UILayout;

public class UIScrollPane extends UIContainer {
	private class ScrollPaneLayout implements UILayout {
		public void apply(UIContainer parent) {
			UIViewport vp = UIScrollPane.this.viewport;
			UIScrollBar hb = UIScrollPane.this.horizontalBar;
			UIScrollBar vb = UIScrollPane.this.verticalBar;
			
			BarPolicy hbp = UIScrollPane.this.horizontalBarPolicy;
			BarPolicy vbp = UIScrollPane.this.verticalBarPolicy;
			
			float hbs = hb.getPreferredSize().y;
			float vbs = vb.getPreferredSize().x;
			
			float top = parent.getPaddingTop();
			float left = parent.getPaddingLeft();
			float right = parent.getPaddingRight();
			float bottom = parent.getPaddingBottom();
			float w = parent.getWidth();
			float h = parent.getHeight();
			
			boolean horizontalBarVisible = hbp != BarPolicy.DISABLED && (hbp == BarPolicy.ALWAYS_VISIBLE || (vp.getContent() != null && w < vp.getContent().getWidth()));
			boolean verticalBarVisible = vbp != BarPolicy.DISABLED && (vbp == BarPolicy.ALWAYS_VISIBLE || (vp.getContent() != null && h < vp.getContent().getHeight()));
			
			// Double check
			if(horizontalBarVisible && !verticalBarVisible) {
				verticalBarVisible = vbp != BarPolicy.DISABLED && (vp.getContent() != null && (h - hbs) < vp.getContent().getHeight());
			} else if(verticalBarVisible && !horizontalBarVisible) {
				horizontalBarVisible = hbp != BarPolicy.DISABLED && (vp.getContent() != null && (w - vbs) < vp.getContent().getWidth());
			}
			
			hb.setVisible(horizontalBarVisible);
			vb.setVisible(verticalBarVisible);
			
			if(horizontalBarVisible) {
				hb.setBounds(left, h - bottom - hbs, w - left - right - (verticalBarVisible ? vbs : 0), hbs);
				
				bottom += hbs + bottom;
			}
			
			if(verticalBarVisible) {
				vb.setBounds(w - right - vbs, top, vbs, h - top - bottom);
				
				right += vbs + right;
			}
			
			vp.setBounds(top, left, w - left - right, h - top - bottom);
		}
		
		public void getPreferredSize(UIContainer parent, Vector2f dest) {
			dest.set(UIScrollPane.this.viewport.getPreferredSize());
			dest.add(parent.getPaddingLeft() + parent.getPaddingRight(), parent.getPaddingTop() + parent.getPaddingBottom());
			
			if(UIScrollPane.this.verticalBarPolicy == BarPolicy.ALWAYS_VISIBLE) {
				dest.x += UIScrollPane.this.verticalBar.getPreferredSize().x;
			}
			
			if(UIScrollPane.this.horizontalBarPolicy == BarPolicy.ALWAYS_VISIBLE) {
				dest.y += UIScrollPane.this.horizontalBar.getPreferredSize().y;
			}
		}
		
		public void addComponent(int index, UIComponent comp, Object constraints) {
		}
		
		public void removeComponent(UIComponent comp) {
		}
		
		public void removeAllComponents() {
		}
	}
	
	public static enum BarPolicy {
		ALWAYS_VISIBLE, SHOW_WHEN_NEEDED, DISABLED
	}
	
	private float mouseScrollXStep = 50;
	private float mouseScrollYStep = 50;
	
	private BarPolicy horizontalBarPolicy = BarPolicy.SHOW_WHEN_NEEDED;
	private BarPolicy verticalBarPolicy = BarPolicy.SHOW_WHEN_NEEDED;
	
	private UIScrollBar horizontalBar = new UIScrollBar(false);
	private UIScrollBar verticalBar = new UIScrollBar(true);
	
	private UIViewport viewport;
	
	public UIScrollPane() {
		this(null);
	}
	
	public UIScrollPane(UIComponent content) {
		this(content, BarPolicy.SHOW_WHEN_NEEDED);
	}
	
	public UIScrollPane(UIComponent content, BarPolicy barPolicy) {
		this(content, barPolicy, barPolicy);
	}
	
	public UIScrollPane(UIComponent content, BarPolicy horizontalPol, BarPolicy verticalPol) {
		this.horizontalBarPolicy = horizontalPol;
		this.verticalBarPolicy = verticalPol;
		
		this.viewport = new UIViewport(content);
		
		this.setPadding(1);
		
		this.setLayout(new ScrollPaneLayout());
		this.addAll(this.viewport, this.horizontalBar, this.verticalBar);
		
		this.horizontalBar.setOnValueChanged((bar) -> {
			float cw = this.viewport.getContent().getWidth();
			float vw = this.viewport.getWidth();
			
			this.viewport.setScrollX(bar.getCurrentValue() * Math.max(0, cw - vw));
		});
		
		this.verticalBar.setOnValueChanged((bar) -> {
			float ch = this.viewport.getContent().getHeight();
			float vh = this.viewport.getHeight();
			
			this.viewport.setScrollY(bar.getCurrentValue() * Math.max(0, ch - vh));
		});
	}
	
	public void setLayout(UILayout layout) {
		if(layout instanceof ScrollPaneLayout) super.setLayout(layout);
	}
	
	public boolean mouseWheelMoved(float x, float y, float scrollX, float scrollY) {
		float cw = this.viewport.getContent().getWidth();
		float vw = this.viewport.getWidth();
		
		float ch = this.viewport.getContent().getHeight();
		float vh = this.viewport.getHeight();
		
		this.horizontalBar.scroll(-scrollX * this.mouseScrollXStep / Math.max(1, cw - vw));
		this.verticalBar.scroll(-scrollY * this.mouseScrollYStep / Math.max(1, ch - vh));
		
		return false;
	}
	
	public BarPolicy getHorizontalBarPolicy() {
		return this.horizontalBarPolicy;
	}
	
	public void setHorizontalBarPolicy(BarPolicy horizontalBarPolicy) {
		this.horizontalBarPolicy = horizontalBarPolicy;
	}
	
	public BarPolicy getVerticalBarPolicy() {
		return this.verticalBarPolicy;
	}
	
	public void setVerticalBarPolicy(BarPolicy verticalBarPolicy) {
		this.verticalBarPolicy = verticalBarPolicy;
	}
	
	public UIViewport getViewport() {
		return this.viewport;
	}
	
	public float getMouseScrollXStep() {
		return this.mouseScrollXStep;
	}
	
	public void setMouseScrollXStep(float mouseScrollXStep) {
		this.mouseScrollXStep = mouseScrollXStep;
	}
	
	public float getMouseScrollYStep() {
		return this.mouseScrollYStep;
	}
	
	public void setMouseScrollYStep(float mouseScrollYStep) {
		this.mouseScrollYStep = mouseScrollYStep;
	}
}
