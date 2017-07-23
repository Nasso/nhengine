package io.github.nasso.nhengine.ui.control;

import java.util.function.Consumer;

import io.github.nasso.nhengine.graphics.Texture2D;

public class UIMenuItem {
	private CharSequence text;
	protected UIMenu parent = null;
	private Texture2D icon = null;
	
	private Consumer<UIMenuItem> onAction = null;
	
	public UIMenuItem() {
		this(null);
	}
	
	public UIMenuItem(CharSequence text) {
		this(null, text);
	}
	
	public UIMenuItem(CharSequence text, Consumer<UIMenuItem> onAction) {
		this(null, text, onAction);
	}
	
	public UIMenuItem(Texture2D icon, CharSequence text) {
		this(icon, text, null);
	}
	
	public UIMenuItem(Texture2D icon, CharSequence text, Consumer<UIMenuItem> onAction) {
		this.text = text;
		this.icon = icon;
		this.onAction = onAction;
	}
	
	public void doClick() {
		if(this.onAction != null) this.onAction.accept(this);
	}
	
	public UIMenu getRootMenu() {
		return this.parent == null ? null : this.parent.getRootMenu();
	}
	
	public CharSequence getText() {
		return this.text;
	}
	
	public void setText(CharSequence text) {
		this.text = text;
	}
	
	public Texture2D getIcon() {
		return this.icon;
	}
	
	public void setIcon(Texture2D icon) {
		this.icon = icon;
	}
	
	public Consumer<UIMenuItem> getOnAction() {
		return this.onAction;
	}
	
	public void setOnAction(Consumer<UIMenuItem> onAction) {
		this.onAction = onAction;
	}
}
