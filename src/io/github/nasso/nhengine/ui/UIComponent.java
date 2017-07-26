package io.github.nasso.nhengine.ui;

import org.joml.Vector2f;
import org.joml.Vector2fc;
import org.joml.Vector4fc;

import io.github.nasso.nhengine.core.Cursor;
import io.github.nasso.nhengine.core.Nhengine;
import io.github.nasso.nhengine.graphics.Color;
import io.github.nasso.nhengine.graphics.Font;
import io.github.nasso.nhengine.graphics.FontFamily;
import io.github.nasso.nhengine.graphics.FontFamily.FontStyle;
import io.github.nasso.nhengine.graphics.GraphicsContext2D;
import io.github.nasso.nhengine.ui.control.UIPopupMenu;
import io.github.nasso.nhengine.ui.theme.UITheme;
import io.github.nasso.nhengine.utils.Box2D;
import io.github.nasso.nhengine.utils.MathUtils;

public abstract class UIComponent {
	public static final int ANCHOR_TOP_LEFT = 0;
	public static final int ANCHOR_TOP = 1;
	public static final int ANCHOR_TOP_RIGHT = 2;
	
	public static final int ANCHOR_LEFT = 3;
	public static final int ANCHOR_CENTER = 4;
	public static final int ANCHOR_RIGHT = 5;
	
	public static final int ANCHOR_BOTTOM_LEFT = 6;
	public static final int ANCHOR_BOTTOM = 7;
	public static final int ANCHOR_BOTTOM_RIGHT = 8;
	
	public static final float USE_THEME_VALUE = Float.NaN;
	
	protected UIContainer parent;
	
	private Cursor cursor = null;
	
	private UIPopupMenu popupMenu = null;
	
	protected Box2D bounds = new Box2D();
	protected Vector2f preferredSize = new Vector2f();
	
	private float[] padding = { 0.0f, 0.0f, 0.0f, 0.0f };
	
	protected Color background = null;
	private Color textColor = null;
	
	private FontFamily fontFamily = null;
	private FontStyle fontStyle = null;
	
	private float opacity = 1;
	
	private boolean focusable = true;
	
	boolean[] buttonPresses = new boolean[Nhengine.MOUSE_BUTTON_LAST + 1];
	
	private boolean opaque = false;
	
	private boolean mouseOver = false;
	private boolean preferredSizeSet = false;
	
	private boolean visible = true;
	
	public abstract void computePreferredSize();
	
	public boolean isChildOf(UIContainer container) {
		if(this == container) return false;
		if(this.parent == null) return container == null;
		if(this.parent == container) return true;
		return this.parent.isChildOf(container);
	}
	
	public boolean isPreferredSizeSet() {
		return this.preferredSizeSet;
	}
	
	public UIRootPane getRootPane() {
		return this.parent == null ? null : this.parent.getRootPane();
	}
	
	public float getAbsoluteX() {
		return this.parent == null ? this.getX() : this.getX() + this.parent.getAbsoluteX();
	}
	
	public float getAbsoluteY() {
		return this.parent == null ? this.getY() : this.getY() + this.parent.getAbsoluteY();
	}
	
	public void openPopupMenu(float x, float y) {
		if(this.popupMenu == null) return;
		
		UIRootPane root = this.getRootPane();
		if(root == null) return;
		
		root.openPopup(this.popupMenu, this.getAbsoluteX() + x, this.getAbsoluteY() + y);
	}
	
	public void closePopupMenu() {
		if(this.getPopupMenu() == null) return;
		
		UIRootPane root = this.getRootPane();
		if(root == null) return;
		
		root.closePopup(this.popupMenu);
	}
	
	/**
	 * Same as {@code mouseClicked(MOUSE_BUTTON_LEFT) }
	 */
	public void doClick() {
		this.mouseClicked(Nhengine.MOUSE_BUTTON_LEFT);
	}
	
	public boolean hasPressed(int btn) {
		return this.buttonPresses[btn];
	}
	
	public void focus() {
		UIRootPane root = this.getRootPane();
		
		if(root != null) root.requestFocus(this);
	}
	
	public boolean hasFocus() {
		UIRootPane root = this.getRootPane();
		
		return root != null && root.hasFocus(this);
	}
	
	public void focusGain() {
		
	}
	
	public void focusLost() {
		
	}
	
	boolean fireMouseWheelMoved(float x, float y, float scrollX, float scrollY) {
		return this.mouseWheelMoved(x, y, scrollX, scrollY);
	}
	
	boolean fireMousePressedEvent(float x, float y, int btn) {
		this.buttonPresses[btn] = true;
		this.focus();
		
		return this.mouseButtonPressed(x, y, btn);
	}
	
	boolean fireMouseReleasedEvent(float x, float y, int btn) {
		boolean spread = true;
		if(this.buttonPresses[btn] && MathUtils.boxContains(x, y, 0, 0, this.getWidth(), this.getHeight())) {
			spread &= this.mouseClicked(btn);
		}
		
		this.buttonPresses[btn] = false;
		return this.mouseButtonReleased(x, y, btn) && spread;
	}
	
	boolean fireMouseMovedEvent(float newX, float newY, float relX, float relY) {
		return this.mouseMoved(newX, newY, relX, relY);
	}
	
	boolean fireMouseDraggedEvent(float newX, float newY, float relX, float relY, int btn) {
		return this.mouseDragged(newX, newY, relX, relY, btn);
	}
	
	boolean fireMouseEnteredEvent(float newX, float newY, float relX, float relY) {
		UIRootPane root = this.getRootPane();
		if(root != null) root.mouseEnteredComponent(this);
		
		this.mouseOver = true;
		return this.mouseEntered(newX, newY, relX, relY);
	}
	
	boolean fireMouseExitedEvent(float x, float y, float relX, float relY) {
		UIRootPane root = this.getRootPane();
		if(root != null) root.mouseExitedComponent(this);
		
		this.mouseOver = false;
		return this.mouseExited(x, y, relX, relY);
	}
	
	public void setPreferredSize(float w, float h) {
		this.preferredSize.set(w, h);
		this.preferredSizeSet = w >= 0 && h >= 0;
	}
	
	public void setPreferredSize(Vector2fc prefSize) {
		if(prefSize == null) this.preferredSize.zero();
		else this.preferredSize.set(prefSize);
		this.preferredSizeSet = prefSize != null;
	}
	
	public final Vector2f getPreferredSize() {
		return this.preferredSize;
	}
	
	public UITheme getTheme() {
		if(this.parent == null) return null;
		
		return this.parent.getTheme();
	}
	
	public void repaint() {
		if(this.parent == null) return;
		
		this.parent.repaint();
	}
	
	boolean setBounds_norepaint(float x, float y, float w, float h) {
		if(this.bounds.getMinX() == x && this.bounds.getMinY() == y && this.bounds.getWidth() == w && this.bounds.getHeight() == h) return false;
		
		float oldW = this.bounds.getWidth(), oldH = this.bounds.getHeight();
		
		this.bounds.redefineXYWH(x, y, w, h);
		this.resized(oldW, oldH, w, h);
		return true;
	}
	
	public void setBounds(float x, float y, float w, float h) {
		if(this.setBounds_norepaint(x, y, w, h)) this.repaint();
	}
	
	public void setPosition(float x, float y) {
		this.setBounds(x, y, this.bounds.getWidth(), this.bounds.getHeight());
	}
	
	public void setSize(float w, float h) {
		this.setBounds(this.bounds.getMinX(), this.bounds.getMinY(), w, h);
	}
	
	public void setSize(Vector2fc size) {
		this.setSize(size.x(), size.y());
	}
	
	public float getX() {
		return this.bounds.getMinX();
	}
	
	public float getY() {
		return this.bounds.getMinY();
	}
	
	public float getWidth() {
		return this.bounds.getWidth();
	}
	
	public float getHeight() {
		return this.bounds.getHeight();
	}
	
	protected void paintComponent(GraphicsContext2D gtx) {
		
	}
	
	public UIContainer getParent() {
		return this.parent;
	}
	
	public Color getBackground() {
		return this.background == null && this.parent != null ? this.parent.getBackground() : this.background;
	}
	
	public void setBackground(Color background) {
		this.background = background;
	}
	
	public void resized(float oldWidth, float oldHeight, float newWidth, float newHeight) {
		
	}
	
	public void textInput(int codepoint) {
		
	}
	
	public void keyPressed(int key) {
	}
	
	public void keyTyped(int key) {
	}
	
	public void keyReleased(int key) {
	}
	
	public boolean mouseButtonPressed(float x, float y, int btn) {
		return true;
	}
	
	public boolean mouseButtonReleased(float x, float y, int btn) {
		return true;
	}
	
	public boolean mouseMoved(float newX, float newY, float relX, float relY) {
		return true;
	}
	
	public boolean mouseWheelMoved(float x, float y, float scrollX, float scrollY) {
		return true;
		
	}
	
	public boolean mouseEntered(float x, float y, float relX, float relY) {
		return true;
	}
	
	public boolean mouseExited(float x, float y, float relX, float relY) {
		return true;
	}
	
	public boolean mouseDragged(float x, float y, float relX, float relY, int button) {
		return true;
	}
	
	public boolean mouseClicked(int btn) {
		return true;
	}
	
	public boolean isMouseOver() {
		return this.mouseOver;
	}
	
	public boolean isEffectivelyVisible() {
		UIRootPane root = this.getRootPane(); // returns the root pane
		return root != null && root.isVisible(); // no need to check if the root own this, because we only get the root if we're a child of it
	}
	
	public boolean isVisible() {
		return this.visible;
	}
	
	public void setVisible(boolean visible) {
		if(this.visible == visible) return;
		
		this.visible = visible;
		this.repaint();
	}
	
	public boolean isOpaque() {
		return this.opaque;
	}
	
	public void setOpaque(boolean opaque) {
		this.opaque = opaque;
	}
	
	public float getPaddingTop() {
		return this.padding[0];
	}
	
	public float getPaddingRight() {
		return this.padding[1];
	}
	
	public float getPaddingBottom() {
		return this.padding[2];
	}
	
	public float getPaddingLeft() {
		return this.padding[3];
	}
	
	public void setPadding(float top, float right, float bottom, float left) {
		this.padding[0] = top;
		this.padding[1] = right;
		this.padding[2] = bottom;
		this.padding[3] = left;
	}
	
	public void setPadding(float topBottom, float leftRight) {
		this.setPadding(topBottom, leftRight, topBottom, leftRight);
	}
	
	public void setPadding(float topRightBottomLeft) {
		this.setPadding(topRightBottomLeft, topRightBottomLeft);
	}
	
	public void setPadding(Vector4fc padding) {
		this.setPadding(padding.x(), padding.y(), padding.z(), padding.w());
	}
	
	public boolean isFocusable() {
		return this.focusable;
	}
	
	public void setFocusable(boolean focusable) {
		this.focusable = focusable;
	}
	
	public UIPopupMenu getPopupMenu() {
		return this.popupMenu;
	}
	
	public void setPopupMenu(UIPopupMenu popupMenu) {
		this.popupMenu = popupMenu;
	}
	
	public float getOpacity() {
		return this.opacity;
	}
	
	public void setOpacity(float opacity) {
		this.opacity = MathUtils.clamp(opacity, 0.0f, 1.0f);
		this.repaint();
	}
	
	public Cursor getCursor() {
		return this.cursor;
	}
	
	public void setCursor(Cursor cursor) {
		this.cursor = cursor;
	}
	
	public FontFamily getFontFamily() {
		return this.fontFamily != null ? this.fontFamily : (this.parent != null ? this.parent.getFontFamily() : (this.getTheme() != null ? this.getTheme().getFontFamily() : null));
	}
	
	public void setFontFamily(FontFamily fontFamily) {
		if(fontFamily == this.fontFamily) return;
		
		this.fontFamily = fontFamily;
		if(this.parent != null) this.parent.repack();
	}
	
	public FontStyle getFontStyle() {
		return this.fontStyle != null ? this.fontStyle : (this.parent != null ? this.parent.getFontStyle() : FontStyle.REGULAR);
	}
	
	public void setFontStyle(FontStyle fontStyle) {
		if(fontStyle == this.fontStyle) return;
		
		this.fontStyle = fontStyle;
		if(this.parent != null) this.parent.repack();
	}
	
	public final Font getFont() {
		FontFamily fam = this.getFontFamily();
		return fam == null ? null : fam.get(this.getFontStyle());
	}
	
	public Color getTextColor() {
		return this.textColor == null && this.getTheme() != null ? this.getTheme().getTextColor() : this.textColor;
	}
	
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}
}
