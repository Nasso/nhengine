package io.github.nasso.nhengine.ui;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import io.github.nasso.nhengine.core.Nhengine;
import io.github.nasso.nhengine.graphics.Color;
import io.github.nasso.nhengine.ui.layout.UILayout;

public class UIContainer extends UIComponent {
	private List<UIComponent> children = new ArrayList<UIComponent>();
	
	private UILayout layout;
	
	public UIContainer() {
		this(null);
	}
	
	public UIContainer(UILayout layout) {
		this.setOpaque(true);
		this.setFocusable(false);
		this.setLayout(layout);
	}
	
	public boolean mouseButtonPressed(float x, float y, int btn) {
		if(btn == Nhengine.MOUSE_BUTTON_RIGHT) this.openPopupMenu(x, y);
		return true;
	}
	
	boolean fireMouseWheelMoved(float x, float y, float scrollX, float scrollY) {
		if(!super.fireMouseWheelMoved(x, y, scrollX, scrollY)) return false;
		
		for(int i = this.children.size() - 1; i >= 0; i--) {
			UIComponent child = this.children.get(i);
			if(child == null || !child.isVisible()) continue;
			
			if(child.bounds.contains(x, y)) {
				child.fireMouseWheelMoved(x - child.getX(), y - child.getY(), scrollX, scrollY);
				break;
			}
		}
		
		return true;
	}
	
	boolean fireMousePressedEvent(float x, float y, int btn) {
		if(!super.fireMousePressedEvent(x, y, btn)) return false;
		
		for(int i = this.children.size() - 1; i >= 0; i--) {
			UIComponent child = this.children.get(i);
			if(child == null || !child.isVisible()) continue;
			
			if(child.bounds.contains(x, y)) {
				child.fireMousePressedEvent(x - child.getX(), y - child.getY(), btn);
				break;
			}
		}
		
		return true;
	}
	
	boolean fireMouseReleasedEvent(float x, float y, int btn) {
		if(!super.fireMouseReleasedEvent(x, y, btn)) return false;
		
		for(int i = this.children.size() - 1; i >= 0; i--) {
			UIComponent child = this.children.get(i);
			if(child == null || !child.isVisible()) continue;
			
			if(child.buttonPresses[btn]) {
				child.fireMouseReleasedEvent(x - child.getX(), y - child.getY(), btn);
				break;
			}
		}
		
		return true;
	}
	
	boolean fireMouseMovedEvent(float newX, float newY, float relX, float relY) {
		if(!super.fireMouseMovedEvent(newX, newY, relX, relY)) return false;
		
		boolean elementFound = false;
		for(int i = this.children.size() - 1; i >= 0; i--) {
			UIComponent child = this.children.get(i);
			if(child == null || !child.isVisible()) continue;
			
			if(!elementFound && child.bounds.contains(newX, newY)) {
				if(!child.isMouseOver()) {
					child.fireMouseEnteredEvent(newX - child.getX(), newY - child.getY(), relX, relY);
				} else {
					child.fireMouseMovedEvent(newX - child.getX(), newY - child.getY(), relX, relY);
				}
				elementFound = true;
			} else if(child.isMouseOver()) {
				child.fireMouseExitedEvent(newX - child.getX(), newY - child.getY(), relX, relY);
			}
		}
		
		return true;
	}
	
	boolean fireMouseDraggedEvent(float newX, float newY, float relX, float relY, int btn) {
		if(!super.fireMouseDraggedEvent(newX, newY, relX, relY, btn)) return false;
		
		for(int i = this.children.size() - 1; i >= 0; i--) {
			UIComponent child = this.children.get(i);
			if(child == null || !child.isVisible()) continue;
			
			for(int j = 0; j < child.buttonPresses.length; j++) {
				if(child.buttonPresses[j]) child.fireMouseDraggedEvent(newX - child.getX(), newY - child.getY(), relX, relY, j);
			}
		}
		
		return true;
	}
	
	boolean fireMouseEnteredEvent(float x, float y, float relX, float relY) {
		if(!super.fireMouseEnteredEvent(x, y, relX, relY)) return false;
		
		for(int i = this.children.size() - 1; i >= 0; i--) {
			UIComponent child = this.children.get(i);
			if(child == null || !child.isVisible()) continue;
			
			if(child.bounds.contains(x, y)) return child.fireMouseEnteredEvent(x - child.getX(), y - child.getY(), relX, relY);
		}
		
		return true;
	}
	
	boolean fireMouseExitedEvent(float x, float y, float relX, float relY) {
		if(!super.fireMouseExitedEvent(x, y, relX, relY)) return false;
		
		for(int i = 0; i < this.children.size(); i++) {
			UIComponent child = this.children.get(i);
			if(child == null || !child.isVisible()) continue;
			
			child.fireMouseExitedEvent(x, y, relX, relY);
		}
		
		return true;
	}
	
	public void setBounds(float x, float y, float w, float h) {
		if(!super.setBounds_norepaint(x, y, w, h)) return;
		this.repack();
		this.repaint();
	}
	
	private void computeRequiredSize(Vector2f dest) {
		float minX = 0, minY = 0, maxX = 0, maxY = 0;
		
		for(int i = 0; i < this.children.size(); i++) {
			UIComponent child = this.children.get(i);
			if(child == null) continue;
			
			minX = Math.min(child.getX(), minX);
			maxX = Math.max(child.getX() + child.preferredSize.x, maxX);
			
			minY = Math.min(child.getY(), minY);
			maxY = Math.max(child.getY() + child.preferredSize.y, maxY);
		}
		
		float w = maxX - minX, h = maxY - minY;
		
		dest.set(w + this.getPaddingLeft() + this.getPaddingRight(), h + this.getPaddingTop() + this.getPaddingBottom());
	}
	
	public void computePreferredSize() {
		for(int i = 0; i < this.children.size(); i++) {
			UIComponent child = this.children.get(i);
			
			if(child != null) child.computePreferredSize();
		}
		
		if(this.isPreferredSizeSet()) return;
		
		if(this.layout != null) {
			this.layout.getPreferredSize(this, this.preferredSize);
		} else {
			this.computeRequiredSize(this.preferredSize);
		}
	}
	
	public void requestRepack() {
		if(this.parent != null) this.parent.requestRepack();
		this.repack();
	}
	
	public void repack() {
		this.computePreferredSize();
		
		if(this.layout != null) {
			this.layout.apply(this);
		} else {
			for(int i = 0; i < this.children.size(); i++) {
				UIComponent child = this.children.get(i);
				
				if(this.isPreferredSizeSet()) child.computePreferredSize();
				child.setSize(child.getPreferredSize());
			}
		}
	}
	
	public void clear() {
		this.children.clear();
	}
	
	public void addAll(UIComponent first, UIComponent... comps) {
		this.addAll(this.children.size(), first, comps);
	}
	
	public void addAll(int index, UIComponent first, UIComponent... comps) {
		this.addSilently(index, first, null);
		
		for(int i = 0; i < comps.length; i++) {
			this.addSilently(index + i + 1, comps[i], null);
		}
		
		this.repack();
		this.repaint();
	}
	
	public void add(UIComponent c) {
		this.add(this.children.size(), c);
	}
	
	public void add(int index, UIComponent c) {
		this.add(index, c, null);
	}
	
	public void add(UIComponent c, Object constraints) {
		this.add(this.children.size(), c, constraints);
	}
	
	public void add(int index, UIComponent c, Object constraints) {
		this.addSilently(index, c, constraints);
		
		this.requestRepack();
	}
	
	private void addSilently(int i, UIComponent c, Object constraints) {
		if(c != null) {
			if(c == this || c.isChildOf(this)) return;
			if(c.parent != null) c.parent.remove(c);
			c.parent = this;
		}
		
		this.children.add(i, c);
		if(this.layout != null) this.layout.addComponent(i, c, constraints);
	}
	
	public void remove(int i) {
		if(i < 0 || i >= this.children.size()) return;
		
		if(this.children.get(i) != null) {
			if(this.layout != null) this.layout.removeComponent(this.children.get(i));
			this.children.get(i).parent = null;
		}
		
		this.children.remove(i);
		
		this.repaint();
	}
	
	public void remove(UIComponent comp) {
		this.remove(this.children.indexOf(comp));
	}
	
	public void remove(UIComponent... comps) {
		for(int i = 0; i < comps.length; i++) {
			this.remove(comps[i]);
		}
	}
	
	public int getChildrenCount() {
		return this.children.size();
	}
	
	public UIComponent getChild(int n) {
		return this.children.get(n);
	}
	
	public UILayout getLayout() {
		return this.layout;
	}
	
	public void setLayout(UILayout layout) {
		if(this.layout != null) this.layout.removeAllComponents();
		
		this.layout = layout;
	}
	
	protected void generateDescription(StringBuilder builder, int tabs) {
		builder.append("+ " + this.getClass().getSimpleName() + ": " + (this.layout == null ? "no layout" : this.layout.getClass().getSimpleName()) + "\n");
		
		String prefix = new String(new char[tabs + 1]).replace("\0", ".\t"); // prefix = rep("|\t", tabs + 1)
		for(int i = 0, l = this.children.size(); i < l; i++) {
			builder.append(prefix + "|\n" + prefix);
			
			UIComponent child = this.children.get(i);
			
			if(child instanceof UIContainer) {
				((UIContainer) child).generateDescription(builder, tabs + 1);
			} else {
				builder.append("+ " + child.toString() + "\n");
			}
		}
	}
	
	public String toString() {
		StringBuilder b = new StringBuilder();
		this.generateDescription(b, 0);
		
		return b.toString();
	}
	
	public Color getBackground() {
		return this.background == null && this.getTheme() != null ? this.getTheme().getPaneBackground() : this.background;
	}
}
