package io.github.nasso.nhengine.ui.layout;

import org.joml.Vector2f;

import io.github.nasso.nhengine.ui.UIComponent;
import io.github.nasso.nhengine.ui.UIContainer;

public class UIFlowLayout implements UILayout {
	public static final int LEFT = UIComponent.ANCHOR_LEFT;
	public static final int CENTER = UIComponent.ANCHOR_CENTER;
	public static final int RIGHT = UIComponent.ANCHOR_RIGHT;
	
	private int align = CENTER;
	private float hgap, vgap;
	
	public UIFlowLayout(int align, float hgap, float vgap) {
		this.setAlign(align);
		this.setHGap(hgap);
		this.setVGap(vgap);
	}
	
	public UIFlowLayout(int align) {
		this(align, 4, 4);
	}
	
	public UIFlowLayout() {
		this(CENTER);
	}
	
	public void apply(UIContainer parent) {
		float w = parent.getWidth() - parent.getPaddingLeft() - parent.getPaddingRight();
		
		float highestComp = 0;
		
		float x = 0;
		float y = 0;
		
		float prefW = 0, prefH = 0;
		
		int compsOnThisLine = 0;
		float lineWidth = 0;
		
		switch(this.align) {
			case LEFT:
				for(int i = 0; i < parent.getChildrenCount(); i++) {
					UIComponent child = parent.getChild(i);
					prefW = child.getPreferredSize().x;
					prefH = child.getPreferredSize().y;
					
					if(compsOnThisLine > 0 && x + prefW + this.hgap >= w) {
						x = 0;
						y += highestComp + this.vgap;
						highestComp = 0;
						compsOnThisLine = 0;
					}
					
					child.setBounds(x + parent.getPaddingLeft(), y + parent.getPaddingTop(), prefW, prefH);
					highestComp = Math.max(highestComp, prefH);
					
					x += prefW + this.hgap;
					compsOnThisLine++;
				}
				break;
			case CENTER:
				for(int j = 0; j < parent.getChildrenCount(); j++) {
					float usedW = parent.getChild(j).getPreferredSize().x + this.hgap;
					
					if(lineWidth > 0 && lineWidth + usedW >= w) {
						break;
					}
					
					lineWidth += usedW;
				}
				lineWidth -= this.hgap;
				
				x = w / 2f - lineWidth / 2f;
				
				for(int i = 0; i < parent.getChildrenCount(); i++) {
					UIComponent child = parent.getChild(i);
					prefW = child.getPreferredSize().x;
					prefH = child.getPreferredSize().y;
					
					if(compsOnThisLine > 0 && x > 0 && x + prefW + this.hgap >= w) {
						y += highestComp + this.vgap;
						highestComp = 0;
						
						lineWidth = 0;
						
						for(int j = i; j < parent.getChildrenCount(); j++) {
							float usedW = parent.getChild(j).getPreferredSize().x + this.hgap;
							
							if(lineWidth > 0 && lineWidth + usedW >= w) {
								break;
							}
							
							lineWidth += usedW;
						}
						lineWidth -= this.hgap;
						
						x = w / 2f - lineWidth / 2f;
						compsOnThisLine = 0;
					}
					
					child.setBounds(x + parent.getPaddingLeft(), y + parent.getPaddingTop(), prefW, prefH);
					highestComp = Math.max(highestComp, prefH);
					
					x += prefW + this.hgap;
					compsOnThisLine++;
				}
				break;
			case RIGHT:
				for(int i = parent.getChildrenCount() - 1; i >= 0; i--) {
					UIComponent child = parent.getChild(i);
					prefW = child.getPreferredSize().x;
					prefH = child.getPreferredSize().y;
					
					if(compsOnThisLine > 0 && x + prefW + this.hgap >= w) {
						x = 0;
						y += highestComp + this.vgap;
						highestComp = 0;
						compsOnThisLine = 0;
					}
					
					child.setBounds(parent.getPaddingLeft() + w - x - prefW, y + parent.getPaddingTop(), prefW, prefH);
					highestComp = Math.max(highestComp, prefH);
					
					x += prefW + this.hgap;
					compsOnThisLine++;
				}
				break;
		}
	}
	
	public void getPreferredSize(UIContainer parent, Vector2f dest) {
		dest.zero();
		
		for(int i = 0; i < parent.getChildrenCount(); i++) {
			UIComponent child = parent.getChild(i);
			
			dest.x += child.getPreferredSize().x + this.hgap;
			dest.y = Math.max(child.getPreferredSize().y + this.vgap, dest.y);
		}
	}
	
	public void addComponent(int index, UIComponent comp, Object constraints) {
	}
	
	public void removeComponent(UIComponent comp) {
	}
	
	public void removeAllComponents() {
	}
	
	public int getAlign() {
		return this.align;
	}
	
	public void setAlign(int align) {
		this.align = align;
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
}
