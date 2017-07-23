package io.github.nasso.nhengine.ui.layout;

import org.joml.Vector2f;

import io.github.nasso.nhengine.ui.UIComponent;
import io.github.nasso.nhengine.ui.UIContainer;

public class UIBoxLayout implements UILayout {
	public static final int VERTICAL = 0;
	public static final int HORIZONTAL = 1;
	
	public static final int LEFT = UIComponent.ANCHOR_LEFT;
	public static final int CENTER = UIComponent.ANCHOR_CENTER;
	public static final int RIGHT = UIComponent.ANCHOR_RIGHT;
	
	public static final int TOP = LEFT;
	public static final int MIDDLE = CENTER;
	public static final int BOTTOM = RIGHT;
	
	private int orientation = 0;
	private int align = CENTER;
	private float gap;
	
	private boolean expand = true;
	
	public UIBoxLayout(int orientation, float gap, int align, boolean expand) {
		this.orientation = orientation;
		this.gap = gap;
		this.align = align;
		this.expand = expand;
	}
	
	public UIBoxLayout(int orientation, float gap, int align) {
		this(orientation, gap, align, true);
	}
	
	public UIBoxLayout(int orientation, float gap) {
		this(orientation, gap, CENTER);
	}
	
	public UIBoxLayout(int orientation) {
		this(orientation, 4);
	}
	
	public UIBoxLayout() {
		this(VERTICAL);
	}
	
	public void apply(UIContainer parent) {
		float w = parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight();
		float h = parent.getHeight() - parent.getPaddingTop() - parent.getPaddingBottom();
		
		float x = parent.getPaddingLeft();
		float y = parent.getPaddingTop();
		
		float prefW = 0, prefH = 0;
		
		switch(this.orientation) {
			case VERTICAL:
				for(int i = 0; i < parent.getChildrenCount(); i++) {
					UIComponent child = parent.getChild(i);
					prefW = this.expand ? w : child.getPreferredSize().x;
					prefH = child.getPreferredSize().y;
					
					switch(this.align) {
						case LEFT:
							child.setBounds(parent.getPaddingLeft(), y, prefW, prefH);
							break;
						case CENTER:
							child.setBounds(parent.getPaddingLeft() + w / 2 - prefW / 2, y, prefW, prefH);
							break;
						case RIGHT:
							child.setBounds(parent.getPaddingLeft() + w - prefW, y, prefW, prefH);
							break;
					}
					
					y += prefH + this.gap;
				}
				break;
			case HORIZONTAL:
				for(int i = 0; i < parent.getChildrenCount(); i++) {
					UIComponent child = parent.getChild(i);
					prefW = child.getPreferredSize().x;
					prefH = this.expand ? h : child.getPreferredSize().y;
					
					switch(this.align) {
						case TOP:
							child.setBounds(x, parent.getPaddingTop(), prefW, prefH);
							break;
						case MIDDLE:
							child.setBounds(x, parent.getPaddingTop() + h / 2 - prefH / 2, prefW, prefH);
							break;
						case BOTTOM:
							child.setBounds(x, parent.getPaddingTop() + h - prefH, prefW, prefH);
							break;
					}
					
					x += prefW + this.gap;
				}
				break;
		}
	}
	
	public void getPreferredSize(UIContainer parent, Vector2f dest) {
		dest.zero();
		
		if(parent.getChildrenCount() != 0) {
			switch(this.orientation) {
				case VERTICAL:
					for(int i = 0; i < parent.getChildrenCount(); i++) {
						UIComponent child = parent.getChild(i);
						
						dest.x = Math.max(dest.x, child.getPreferredSize().x);
						dest.y += child.getPreferredSize().y;
					}
					dest.y += this.gap * parent.getChildrenCount() - this.gap;
					break;
				case HORIZONTAL:
					for(int i = 0; i < parent.getChildrenCount(); i++) {
						UIComponent child = parent.getChild(i);
						
						dest.x += child.getPreferredSize().x;
						dest.y = Math.max(dest.y, child.getPreferredSize().y);
						
						if(i + 1 < parent.getChildrenCount()) dest.x += this.gap;
					}
					break;
			}
		}
		
		dest.x += parent.getPaddingLeft();
		dest.x += parent.getPaddingRight();
		dest.y += parent.getPaddingTop();
		dest.y += parent.getPaddingBottom();
	}
	
	public void addComponent(int index, UIComponent comp, Object constraints) {
	}
	
	public void removeComponent(UIComponent comp) {
	}
	
	public void removeAllComponents() {
	}
	
	public int getOrientation() {
		return this.orientation;
	}
	
	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}
	
	public float getGap() {
		return this.gap;
	}
	
	public void setGap(float gap) {
		this.gap = gap;
	}
	
	public boolean isExpand() {
		return this.expand;
	}
	
	public void setExpand(boolean expand) {
		this.expand = expand;
	}
	
	public int getAlign() {
		return this.align;
	}
	
	public void setAlign(int align) {
		this.align = align;
	}
}
