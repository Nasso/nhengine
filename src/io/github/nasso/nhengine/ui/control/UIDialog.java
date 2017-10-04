package io.github.nasso.nhengine.ui.control;

import org.joml.Vector4f;

import io.github.nasso.nhengine.core.Nhengine;
import io.github.nasso.nhengine.graphics.Color;
import io.github.nasso.nhengine.graphics.GraphicsContext2D;
import io.github.nasso.nhengine.ui.UIComponent;
import io.github.nasso.nhengine.ui.UIContainer;
import io.github.nasso.nhengine.ui.UIRootPane;
import io.github.nasso.nhengine.ui.layout.UIBorderLayout;
import io.github.nasso.nhengine.utils.FloatTransition;
import io.github.nasso.nhengine.utils.Nhutils;

public class UIDialog {
	public class CloseButton extends UIComponent {
		private Color color, hoverColor, pressedColor;
		private float borderRadius = USE_THEME_VALUE;
		
		private FloatTransition hoverTransition = new FloatTransition(0.1f, this::setTransitionValue, 0);
		private FloatTransition pressTransition = new FloatTransition(0.1f, this::setTransitionValue, 0);
		
		private Vector4f _rgba = new Vector4f();
		
		private CloseButton() {
			this.setPadding(4);
		}
		
		public void setTransitionValue(float value) {
			this.repaint();
		}
		
		public boolean mouseButtonPressed(float x, float y, int btn) {
			if(btn == Nhengine.MOUSE_BUTTON_LEFT) {
				this.pressTransition.setTargetValue(1);
			}
			return true;
		}
		
		public boolean mouseDragged(float x, float y, float relx, float rely, int btn) {
			return false;
		}
		
		public boolean mouseButtonReleased(float x, float y, int btn) {
			if(btn == Nhengine.MOUSE_BUTTON_LEFT) {
				this.pressTransition.setTargetValue(0);
				this.hoverTransition.setTargetValue(0);
			}
			return true;
		}
		
		public boolean mouseEntered(float newX, float newY, float relX, float relY) {
			this.hoverTransition.setTargetValue(1);
			return true;
		}
		
		public boolean mouseExited(float newX, float newY, float relX, float relY) {
			this.hoverTransition.setTargetValue(0);
			this.pressTransition.setTargetValue(0);
			return true;
		}
		
		public boolean mouseClicked(int button) {
			if(button == Nhengine.MOUSE_BUTTON_LEFT) UIDialog.this.close();
			return true;
		}

		public Color getColor() {
			return this.getTheme().getColor("dialog.closeButton.color", this.color);
		}

		public Color getHoverColor() {
			return this.getTheme().getColor("dialog.closeButton.hoverColor", this.hoverColor);
		}

		public Color getPressedColor() {
			return this.getTheme().getColor("dialog.closeButton.pressedColor", this.pressedColor);
		}
		
		private void updateColor() {
			Color normal = this.getColor();
			Color hover = this.getHoverColor();
			Color pressed = this.getPressedColor();
			
			Nhutils.colorBlend(normal, hover, this.hoverTransition.getValue(), this._rgba);
			Nhutils.colorBlend(this._rgba, pressed, this.pressTransition.getValue(), this._rgba);
		}
		
		protected void paintComponent(GraphicsContext2D gtx) {
			float top = this.getPaddingTop();
			float left = this.getPaddingLeft();
			float w = this.getWidth() - left - this.getPaddingRight();
			float h = this.getHeight() - top - this.getPaddingBottom();
			
			this.updateColor();
			
			gtx.setFill(this._rgba);
			gtx.fillRoundedRect(0, 0, this.getWidth(), this.getHeight(), this.getBorderRadius());
			
			gtx.setStroke(this.getTextColor());
			gtx.beginPath();
			gtx.moveTo(left, top);
			gtx.lineTo(top + w, left + h);
			gtx.moveTo(left + w, top);
			gtx.lineTo(left, top + h);
			gtx.stroke();
		}
		
		public void computePreferredSize() {
			this.preferredSize.set(18);
		}

		public void setColor(Color color) {
			this.color = color;
		}

		public void setHoverColor(Color hoverColor) {
			this.hoverColor = hoverColor;
		}
		public void setPressedColor(Color pressedColor) {
			this.pressedColor = pressedColor;
		}

		public float getBorderRadius() {
			return this.getTheme().getFloat("dialog.closeButton.borderRadius", this.borderRadius);
		}

		public void setBorderRadius(float borderRadius) {
			this.borderRadius = borderRadius;
		}
	}
	
	private class TitleBar extends UIContainer {
		UILabel titleLabel = new UILabel(null, UILabel.ANCHOR_LEFT);
		
		private TitleBar() {
			super(new UIBorderLayout(0));
			
			UIContainer draggablePart = new UIContainer(new UIBorderLayout()) {
				float pressX = 0, pressY = 0;
				
				public boolean mouseButtonPressed(float x, float y, int btn) {
					if(btn != Nhengine.MOUSE_BUTTON_LEFT) return true;
					
					this.pressX = x;
					this.pressY = y;
					
					return true;
				}
				
				public boolean mouseDragged(float x, float y, float relx, float rely, int btn) {
					if(btn != Nhengine.MOUSE_BUTTON_LEFT) return true;
					
					x += this.getAbsoluteX();
					y += this.getAbsoluteY();
					
					UIDialog.this.chrome.setPosition(x - this.pressX - UIDialog.this.chrome.getPaddingLeft(), y - this.pressY - UIDialog.this.chrome.getPaddingTop());
					return true;
				}
			};
			
			draggablePart.setOpaque(false);
			draggablePart.setPadding(6, 8);
			draggablePart.add(this.titleLabel, UIBorderLayout.CENTER);
			
			UIContainer controlPart = new UIContainer(new UIBorderLayout());
			controlPart.setOpaque(false);
			controlPart.setPadding(6, 8);
			controlPart.add(new CloseButton(), UIBorderLayout.CENTER);
			
			this.add(draggablePart, UIBorderLayout.CENTER);
			this.add(controlPart, UIBorderLayout.EAST);
		}
		
		public Color getBackground() {
			return this.getTheme().getColor("dialog.titleBar.background", super.background);
		}
	}
	
	class DialogueChrome extends UIContainer {
		TitleBar titleBar = new TitleBar();
		UIContainer content = new UIContainer();
		
		public DialogueChrome() {
			this.setPadding(2);
			
			this.setLayout(new UIBorderLayout());
			this.add(this.titleBar, UIBorderLayout.NORTH);
			this.add(this.content, UIBorderLayout.CENTER);
		}
		
		public Color getBackground() {
			return this.getTheme().getColor("dialog.titleBar.background", super.background);
		}
	}
	
	DialogueChrome chrome = new DialogueChrome();
	private UIRootPane openedOnRoot = null;
	
	private Color modalFocusColor;
	
	private float titleBarHeight = UIComponent.USE_THEME_VALUE;
	
	private boolean modal = true;
	
	public UIDialog(CharSequence title, boolean modal) {
		this.modal = modal;
		this.setTitle(title);
	}
	
	public UIDialog(CharSequence title) {
		this(title, true);
	}
	
	public UIDialog() {
		this("Dialog");
	}
	
	public float getX() {
		return this.chrome.getX();
	}
	
	public float getY() {
		return this.chrome.getY();
	}
	
	public float getWidth() {
		return this.chrome.getWidth();
	}
	
	public float getHeight() {
		return this.chrome.getHeight();
	}
	
	public void open(UIRootPane root) {
		if(this.openedOnRoot != null) this.close();
		if(root == null) return;
		
		if(this.isModal()) root.pushModalLayer(this);
		root.add(this.chrome);
		this.chrome.setPosition((int) root.getWidth() / 2f - this.chrome.getWidth() / 2f, (int) root.getHeight() / 2f - this.chrome.getHeight() / 2f);
		
		this.openedOnRoot = root;
	}
	
	public void close() {
		if(this.openedOnRoot == null) return;
		this.openedOnRoot.remove(this.chrome);
		if(this.isModal()) this.openedOnRoot.popModalLayer();
		this.openedOnRoot = null;
	}
	
	public CharSequence getTitle() {
		return this.chrome.titleBar.titleLabel.getText();
	}
	
	public void setTitle(CharSequence title) {
		this.chrome.titleBar.titleLabel.setText(title);
	}
	
	public boolean isModal() {
		return this.modal;
	}
	
	public UIContainer getContent() {
		return this.chrome.content;
	}
	
	public float getTitleBarHeight() {
		return this.chrome.getTheme().getFloat("dialog.titleBar.height", this.titleBarHeight);
	}
	
	public void setTitleBarHeight(float titleBarHeight) {
		this.titleBarHeight = titleBarHeight;
	}
	
	public boolean isShown() {
		return this.chrome.isEffectivelyVisible();
	}

	public Color getModalFocusColor() {
		return this.modalFocusColor;
	}

	public void setModalFocusColor(Color modalFocusColor) {
		this.modalFocusColor = modalFocusColor;
	}
}
