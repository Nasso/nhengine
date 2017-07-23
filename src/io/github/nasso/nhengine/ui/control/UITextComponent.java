package io.github.nasso.nhengine.ui.control;

import io.github.nasso.nhengine.ui.UIComponent;

public abstract class UITextComponent extends UIComponent {
	private CharSequence text = null;
	
	public UITextComponent() {
		this(null);
	}
	
	public UITextComponent(CharSequence text) {
		this.text = text;
	}
	
	public CharSequence getText() {
		return this.text;
	}
	
	public void setText(CharSequence text) {
		this.text = text;
		if(this.parent != null) this.parent.repack();
		this.repaint();
	}
}
