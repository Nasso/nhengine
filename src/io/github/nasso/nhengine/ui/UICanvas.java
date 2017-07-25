package io.github.nasso.nhengine.ui;

import io.github.nasso.nhengine.component.CanvasComponent;
import io.github.nasso.nhengine.component.InputComponent;
import io.github.nasso.nhengine.event.InputComponentEventHandler;
import io.github.nasso.nhengine.graphics.GraphicsContext2D;
import io.github.nasso.nhengine.level.Node;
import io.github.nasso.nhengine.ui.theme.UIDefaultDarkTheme;
import io.github.nasso.nhengine.ui.theme.UITheme;

public class UICanvas extends Node implements InputComponentEventHandler {
	private CanvasComponent cvs;
	private InputComponent input;
	private GraphicsContext2D gtx;
	
	private UIRootPane root = null;
	private UITheme theme = null;
	
	private UIComponent focusComponent = null;
	
	private boolean autoRepaintRequested = false;
	private boolean autoRepaint = true;
	private boolean _showComponentBounds = false;
	
	public UICanvas(UITheme theme, int w, int h) {
		this.cvs = new CanvasComponent(w, h);
		this.cvs.setScale(w, h);
		this.gtx = this.cvs.getContext2D();
		
		this.theme = theme;
		
		this.root = new UIRootPane(this);
		this.root.setSize(w, h);
		
		this.input = new InputComponent(w, h, this);
		
		this.addComponents(this.cvs, this.input);
	}
	
	public UICanvas(int w, int h) {
		this(new UIDefaultDarkTheme(), w, h);
	}
	
	public UICanvas(float w, float h) {
		this((int) w, (int) h);
	}
	
	public UICanvas(UITheme theme, float w, float h) {
		this(theme, (int) w, (int) h);
	}
	
	public void update() {
		if(this.autoRepaintRequested && this.autoRepaint) this.repaint();
		this.autoRepaintRequested = false;
	}
	
	public void repaint() {
		this.gtx.discardAll();
		this.gtx.clear();
		this.repaintContainer(this.root);
	}
	
	private void repaintComponent(UIComponent comp) {
		if(this._showComponentBounds) this.gtx.save();
		
		this.gtx.save();
		this.gtx.setGlobalAlpha(comp.getOpacity());
		this.gtx.translate(comp.getX(), comp.getY());
		this.gtx.clip(0, 0, comp.getWidth(), comp.getHeight());
		if(comp.isOpaque()) {
			this.gtx.setFill(comp.getBackground());
			this.gtx.fillRect(-0.5f, -0.5f, comp.getWidth() + 1, comp.getHeight() + 1);
			this.gtx.setFill(0);
		}
		comp.paintComponent(this.gtx);
		
		this.gtx.restore();
		
		if(this._showComponentBounds) {
			if(this.focusComponent == comp) this.gtx.setStroke(0, 0, 1, 1);
			else this.gtx.setStroke(1, 0, 0, 1);
			this.gtx.setStrokeSize(2);
			this.gtx.strokeRect(comp.getX(), comp.getY(), comp.getWidth(), comp.getHeight());
			this.gtx.restore();
		}
	}
	
	private void repaintContainer(UIContainer container) {
		this.gtx.save();
		this.gtx.setGlobalAlpha(container.getOpacity());
		this.gtx.translate(container.getX(), container.getY());
		this.gtx.clip(0, 0, container.getWidth(), container.getHeight());
		
		if(container.isOpaque()) {
			this.gtx.setFill(container.getBackground());
			this.gtx.fillRect(0, 0, container.getWidth(), container.getHeight());
			this.gtx.setFill(0);
		}
		
		this.gtx.save();
		container.paintComponent(this.gtx);
		this.gtx.restore();
		
		if(this._showComponentBounds) {
			this.gtx.save();
			if(this.focusComponent == container) this.gtx.setStroke(0, 0, 1, 1);
			else this.gtx.setStroke(0, 1, 0, 1);
			this.gtx.setStrokeSize(2);
			this.gtx.strokeRect(0, 0, container.getWidth(), container.getHeight());
			this.gtx.restore();
		}
		
		int count = container.getChildrenCount();
		UIComponent comp = null;
		for(int i = 0; i < count; i++) {
			comp = container.getChild(i);
			if(comp == null || !comp.isVisible()) continue;
			
			if(comp instanceof UIContainer) {
				this.repaintContainer((UIContainer) comp);
			} else {
				this.repaintComponent(comp);
			}
		}
		
		this.gtx.restore();
	}
	
	public void requestAutoRepaint() {
		this.autoRepaintRequested |= this.autoRepaint;
	}
	
	public void setSize(int w, int h) {
		this.input.setInputAreaWidth(w);
		this.input.setInputAreaHeight(h);
		
		this.cvs.setResolution(w, h);
		this.cvs.setScale(w, h);
		
		this.root.setSize(w, h);
	}
	
	public boolean isAutoRepaint() {
		return this.autoRepaint;
	}
	
	public void setAutoRepaint(boolean autoRepaint) {
		this.autoRepaint = autoRepaint;
	}
	
	public UIRootPane getRootPane() {
		return this.root;
	}
	
	public UITheme getTheme() {
		return this.theme;
	}
	
	public void setTheme(UITheme theme) {
		if(this.theme == theme) return;
		
		this.theme = theme;
		this.root.repack();
		this.repaint();
	}
	
	public void textInput(int codepoint) {
		if(this.focusComponent != null) this.focusComponent.textInput(codepoint);
	}
	
	public void keyPressed(int key, int scancode) {
		if(this.focusComponent != null) this.focusComponent.keyPressed(key, scancode);
	}
	
	public void keyTyped(int key, int scancode) {
		if(this.focusComponent != null) this.focusComponent.keyTyped(key, scancode);
	}
	
	public void keyReleased(int key, int scancode) {
		if(this.focusComponent != null) this.focusComponent.keyReleased(key, scancode);
	}
	
	public void mouseButtonPressed(float x, float y, int btn) {
		this.root.fireMousePressedEvent(x, y, btn);
	}
	
	public void mouseButtonReleased(float x, float y, int btn) {
		this.root.fireMouseReleasedEvent(x, y, btn);
	}
	
	public void mouseMoved(float newX, float newY, float relX, float relY) {
		for(int j = 0; j < this.root.buttonPresses.length; j++) {
			if(this.root.buttonPresses[j]) this.root.fireMouseDraggedEvent(newX - this.root.getX(), newY - this.root.getY(), relX, relY, j);
		}
		
		this.root.fireMouseMovedEvent(newX, newY, relX, relY);
	}
	
	public void mouseWheelMoved(float x, float y, float scrollX, float scrollY) {
		this.root.fireMouseWheelMoved(x, y, scrollX, scrollY);
	}
	
	public void mouseEntered(float x, float y, float relX, float relY) {
		this.root.fireMouseEnteredEvent(x, y, relX, relY);
	}
	
	public void mouseExited(float x, float y, float relX, float relY) {
		this.root.fireMouseExitedEvent(x, y, relX, relY);
	}
	
	public void mouseDragged(float x, float y, float relX, float relY, int button) {
	}
	
	public boolean is_showComponentBounds() {
		return this._showComponentBounds;
	}
	
	public void set_showComponentBounds(boolean _showComponentBounds) {
		this._showComponentBounds = _showComponentBounds;
		this.repaint();
	}
	
	public UIComponent getFocusComponent() {
		return this.focusComponent;
	}
	
	public void setFocusComponent(UIComponent focusComponent) {
		UIComponent newFocus = focusComponent.isFocusable() ? focusComponent : this.focusComponent;
		if(newFocus == this.focusComponent) return;
		
		UIComponent oldFocus = this.focusComponent;
		
		this.focusComponent = newFocus;
		this.focusComponent.focusGain();
		if(oldFocus != null) oldFocus.focusLost();
		
		if(this._showComponentBounds) this.repaint();
	}
	
	public boolean isAutoRepaintRequested() {
		return this.autoRepaintRequested;
	}
	
}
