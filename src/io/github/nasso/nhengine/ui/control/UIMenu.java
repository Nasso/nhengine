package io.github.nasso.nhengine.ui.control;

import io.github.nasso.nhengine.graphics.Texture2D;

public class UIMenu extends UIMenuItem {
	UIPopupMenu popupMenu = new UIPopupMenu();
	
	public UIMenu() {
		this(null);
	}
	
	public UIMenu(CharSequence text) {
		this(null, text);
	}
	
	public UIMenu(Texture2D icon, CharSequence text) {
		this(icon, text, null);
	}
	
	public UIMenu(CharSequence text, UIMenuItem[] items) {
		this(null, text, items);
	}
	
	public UIMenu(Texture2D icon, CharSequence text, UIMenuItem[] items) {
		super(icon, text);
		
		if(items != null) {
			for(int i = 0; i < items.length; i++) {
				this.addItem(items[i]);
			}
		}
	}
	
	public void addItem(UIMenuItem item) {
		this.popupMenu.addItem(item);
	}
	
	public UIMenuItem removeItem(UIMenuItem item) {
		return this.popupMenu.removeItem(item);
	}
	
	public UIMenuItem removeItem(int i) {
		return this.popupMenu.removeItem(i);
	}
	
	public UIMenuItem getItem(int i) {
		return this.popupMenu.getItem(i);
	}
	
	public int getItemCount() {
		return this.popupMenu.getItemCount();
	}
	
	public void clearItems() {
		this.popupMenu.clearItems();
	}
}
