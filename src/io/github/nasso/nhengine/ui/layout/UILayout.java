package io.github.nasso.nhengine.ui.layout;

import org.joml.Vector2f;

import io.github.nasso.nhengine.ui.UIComponent;
import io.github.nasso.nhengine.ui.UIContainer;

public interface UILayout {
	public void apply(UIContainer parent);
	
	public void getPreferredSize(UIContainer parent, Vector2f dest);
	
	public void addComponent(int index, UIComponent comp, Object constraints);
	
	public void removeComponent(UIComponent comp);
	
	public void removeAllComponents();
}
