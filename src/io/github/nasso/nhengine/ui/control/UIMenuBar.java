package io.github.nasso.nhengine.ui.control;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector2fc;

import io.github.nasso.nhengine.graphics.Color;
import io.github.nasso.nhengine.graphics.GraphicsContext2D;
import io.github.nasso.nhengine.graphics.TextBaseline;
import io.github.nasso.nhengine.ui.UIComponent;
import io.github.nasso.nhengine.ui.UIContainer;
import io.github.nasso.nhengine.ui.layout.UIFlowLayout;
import io.github.nasso.nhengine.utils.Nhutils;

public class UIMenuBar extends UIContainer {
	private static final float MENUBAR_ITEM_HPADDING = 2;
	private static final float MENUBAR_ITEM_VPADDING = 8;
	private static final float MENUBAR_ITEM_GAP = 2;
	
	private List<UIMenuItem> menuItems = new ArrayList<UIMenuItem>();
	
	private Vector2f subPopupPositionOffset = new Vector2f(0, 0);
	
	public UIMenuBar() {
		this(null);
	}
	
	public UIMenuBar(UIMenuItem[] items) {
		this.setPadding(0, 8);
		this.setLayout(new UIFlowLayout(UIFlowLayout.LEFT, MENUBAR_ITEM_GAP, MENUBAR_ITEM_GAP));
		
		if(items != null) {
			for(int i = 0; i < items.length; i++) {
				this.addItem(items[i]);
			}
		}
	}
	
	public void addItem(UIMenuItem item) {
		if(item.parent != null) item.parent.removeItem(item);
		
		this.add(new UIMenuBarItem(item));
		this.menuItems.add(item);
		
		this.repaint();
	}
	
	public UIMenuItem removeItem(UIMenuItem item) {
		if(item == null || !this.menuItems.contains(item)) return null;
		
		this.remove(this.menuItems.indexOf(item));
		this.menuItems.remove(item);
		
		return item;
	}
	
	public UIMenuItem removeItem(int i) {
		return this.removeItem(this.menuItems.get(i));
	}
	
	public UIMenuItem getItem(int i) {
		return this.menuItems.get(i);
	}
	
	public int getItemCount() {
		return this.menuItems.size();
	}
	
	public void clearItems() {
		this.menuItems.clear();
		this.clear();
	}
	
	public Color getBackground() {
		return this.getTheme().getColor("menuBar.background", this.background);
	}
	
	public Vector2fc getSubPopupPositionOffset() {
		return this.subPopupPositionOffset;
	}
	
	public void setSubPopupPositionOffset(Vector2fc subPopupPositionOffset) {
		this.setSubPopupPositionOffset(subPopupPositionOffset.x(), subPopupPositionOffset.y());
	}
	
	public void setSubPopupPositionOffset(float x, float y) {
		this.subPopupPositionOffset.set(x, y);
	}
	
	private void openBarMenu(UIMenu menu, float x, float y) {
		if(this.getPopupMenu() == menu.popupMenu && this.getPopupMenu().isEffectivelyVisible()) return;
		
		this.closePopupMenu();
		this.setPopupMenu(menu.popupMenu);
		this.openPopupMenu(x + this.subPopupPositionOffset.x, y + this.subPopupPositionOffset.y);
	}
	
	private void clickedMenuItem(UIMenuItem item, float x, float y) {
		if(item instanceof UIMenu) {
			if(this.getPopupMenu() != null && this.getPopupMenu().isEffectivelyVisible()) {
				this.closePopupMenu();
				return;
			}
			
			item.doClick();
			this.openBarMenu((UIMenu) item, x, y);
		} else {
			item.doClick();
		}
	}
	
	private void enteredMenuItem(UIMenuItem item, float x, float y) {
		if(this.getPopupMenu() != null && this.getPopupMenu().isEffectivelyVisible() && item instanceof UIMenu) {
			this.openBarMenu((UIMenu) item, x, y);
		}
	}
	
	private class UIMenuBarItem extends UIComponent {
		private final UIMenuBar bar;
		private UIMenuItem item;
		
		public UIMenuBarItem(UIMenuItem item) {
			this.bar = UIMenuBar.this;
			this.item = item;
			
			this.setPadding(MENUBAR_ITEM_HPADDING, MENUBAR_ITEM_VPADDING);
		}
		
		public boolean mouseEntered(float x, float y, float rx, float ry) {
			this.bar.enteredMenuItem(this.item, this.getX(), this.getY() + this.getHeight());
			this.repaint();
			return true;
		}
		
		public boolean mouseExited(float x, float y, float rx, float ry) {
			this.repaint();
			return true;
		}
		
		public boolean mouseButtonPressed(float x, float y, int btn) {
			this.bar.clickedMenuItem(this.item, this.getX(), this.getY() + this.getHeight());
			return false;
		}
		
		public void computePreferredSize() {
			this.preferredSize.zero();
			if(this.bar.getFont() == null) return;
			
			this.preferredSize.x = Nhutils.measureTextWidth(this.bar.getFont(), this.item.getText()) + this.getPaddingLeft() + this.getPaddingRight();
			this.preferredSize.y = this.bar.getFont().getHeight() + this.getPaddingTop() + this.getPaddingBottom();
		}
		
		protected void paintComponent(GraphicsContext2D gtx) {
			if(this.item.getText() == null) return;
			
			gtx.setFill(this.bar.getTextColor());
			gtx.setFont(this.bar.getFont());
			gtx.setTextBaseline(TextBaseline.ASCENT_MIDDLE);
			
			float h = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
			
			if(this.isMouseOver()) {
				gtx.save();
				gtx.setGlobalAlpha(0.1f);
				gtx.fillRect(0, 0, this.getWidth(), this.getHeight());
				gtx.restore();
			}
			
			gtx.fillText(this.item.getText().toString(), this.getPaddingLeft(), this.getPaddingTop() + h / 2f);
		}
	}
}
