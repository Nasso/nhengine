package io.github.nasso.nhengine.ui.layout;

import static java.lang.Math.*;

import org.joml.Vector2f;

import io.github.nasso.nhengine.ui.UIComponent;
import io.github.nasso.nhengine.ui.UIContainer;

public class UIBorderLayout implements UILayout {
	public static final String NORTH = "NORTH";
	public static final String EAST = "EAST";
	public static final String CENTER = "CENTER";
	public static final String WEST = "WEST";
	public static final String SOUTH = "SOUTH";
	
	public static final String TOP = NORTH;
	public static final String RIGHT = EAST;
	public static final String MIDDLE = CENTER;
	public static final String LEFT = WEST;
	public static final String BOTTOM = SOUTH;
	
	private float hgap = 0, vgap = 0;
	private UIComponent north, east, center, west, south;
	
	public UIBorderLayout() {
		this(0);
	}
	
	public UIBorderLayout(float gap) {
		this(gap, gap);
	}
	
	public UIBorderLayout(float hgap, float vgap) {
		this.hgap = hgap;
		this.vgap = vgap;
	}
	
	public void apply(UIContainer parent) {
		float w = parent.getWidth();
		float h = parent.getHeight();
		
		float top = parent.getPaddingTop();
		float bottom = h - parent.getPaddingBottom();
		float left = parent.getPaddingLeft();
		float right = w - parent.getPaddingRight();
		
		if(this.north != null) {
			Vector2f prefSize = this.north.getPreferredSize();
			this.north.setBounds(left, top, right - left, prefSize.y);
			top += prefSize.y + this.vgap;
		}
		
		if(this.south != null) {
			Vector2f prefSize = this.south.getPreferredSize();
			this.south.setBounds(left, bottom - prefSize.y, right - left, prefSize.y);
			bottom -= prefSize.y + this.vgap;
		}
		
		if(this.east != null) {
			Vector2f prefSize = this.east.getPreferredSize();
			this.east.setBounds(right - prefSize.x, top, prefSize.x, bottom - top);
			right -= prefSize.x + this.hgap;
		}
		
		if(this.west != null) {
			Vector2f prefSize = this.west.getPreferredSize();
			this.west.setBounds(left, top, prefSize.x, bottom - top);
			left += prefSize.x + this.hgap;
		}
		
		if(this.center != null) {
			this.center.setBounds(left, top, right - left, bottom - top);
		}
		
		for(int i = 0; i < parent.getChildrenCount(); i++) {
			UIComponent child = parent.getChild(i);
			if(child != this.north && child != this.east && child != this.west && child != this.center && child != this.south) {
				child.setBounds(child.getX(), child.getY(), child.getPreferredSize().x, child.getPreferredSize().y);
			}
		}
	}
	
	public void addComponent(int index, UIComponent comp, Object constraints) {
		if(NORTH.equals(constraints)) {
			this.north = comp;
		} else if(EAST.equals(constraints)) {
			this.east = comp;
		} else if(CENTER.equals(constraints)) {
			this.center = comp;
		} else if(WEST.equals(constraints)) {
			this.west = comp;
		} else if(SOUTH.equals(constraints)) {
			this.south = comp;
		}
	}
	
	public void removeAllComponents() {
		this.north = this.east = this.center = this.west = this.south = null;
	}
	
	public float getHGap() {
		return this.hgap;
	}
	
	public void setHGap(float hgap) {
		this.hgap = hgap;
	}
	
	public float getVGap() {
		return this.vgap;
	}
	
	public void setVGap(float vgap) {
		this.vgap = vgap;
	}
	
	public void getPreferredSize(UIContainer parent, Vector2f dest) {
		float prefW = 0;
		float prefH = 0;
		
		if(this.west != null) {
			Vector2f prefSize = this.west.getPreferredSize();
			prefW += prefSize.x + this.hgap;
			prefH = max(prefH, prefSize.y);
		}
		
		if(this.east != null) {
			Vector2f prefSize = this.east.getPreferredSize();
			prefW += prefSize.x + this.hgap;
			prefH = max(prefH, prefSize.y);
		}
		
		if(this.center != null) {
			Vector2f prefSize = this.center.getPreferredSize();
			prefW += prefSize.x;
			prefH = max(prefH, prefSize.y);
		}
		
		if(this.north != null) {
			Vector2f prefSize = this.north.getPreferredSize();
			prefW = max(prefW, prefSize.x);
			prefH += prefSize.y + this.vgap;
		}
		
		if(this.south != null) {
			Vector2f prefSize = this.south.getPreferredSize();
			prefW = max(prefW, prefSize.x);
			prefH += prefSize.y + this.vgap;
		}
		
		dest.set(prefW + parent.getPaddingLeft() + parent.getPaddingRight(), prefH + parent.getPaddingTop() + parent.getPaddingBottom());
	}
	
	public void removeComponent(UIComponent comp) {
		if(this.north == comp) this.north = null;
		if(this.south == comp) this.south = null;
		if(this.west == comp) this.west = null;
		if(this.east == comp) this.east = null;
		if(this.center == comp) this.center = null;
	}
}
