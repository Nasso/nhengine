package io.github.nasso.nhengine.ui.control;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector2fc;

import io.github.nasso.nhengine.core.Nhengine;
import io.github.nasso.nhengine.graphics.Color;
import io.github.nasso.nhengine.graphics.GraphicsContext2D;
import io.github.nasso.nhengine.graphics.TextBaseline;
import io.github.nasso.nhengine.ui.UIComponent;
import io.github.nasso.nhengine.ui.UIContainer;
import io.github.nasso.nhengine.ui.UIRootPane;
import io.github.nasso.nhengine.ui.layout.UIBoxLayout;
import io.github.nasso.nhengine.utils.FloatTransition;
import io.github.nasso.nhengine.utils.Nhutils;
import penner.easing.Back;
import penner.easing.Cubic;

public class UIPopupMenu extends UIContainer {
	private static final float ITEM_APPEAR_DELAY = 0.02f;
	private static final float PADDING = 8;
	private static final float ICON_MARGIN = 24;
	private static final float ICON_SEPARATOR_MARGIN = 8;
	private static final float ITEM_PADDING = 8;
	private static final float RIGHT_MARGIN = 64;
	
	private List<UIMenuItem> items = new ArrayList<UIMenuItem>();
	
	private Vector2f subPopupPositionOffset = new Vector2f(-2, 0);
	
	private FloatTransition popupTransition = new FloatTransition(0.2f, this::popupTransitionStep, 0, Cubic::easeOut);
	
	private float popX = 0, popY = 0;
	
	public UIPopupMenu() {
		this(null);
	}
	
	public UIPopupMenu(UIMenuItem[] items) {
		this.setPadding(PADDING, 1);
		this.setLayout(new UIBoxLayout(UIBoxLayout.VERTICAL, 0));
		
		if(items != null) {
			for(int i = 0; i < items.length; i++) {
				this.addItem(items[i]);
			}
		}
	}
	
	public boolean mouseButtonPressed(float x, float y, int btn) {
		// Do nothing to prevent sub popup to open ><
		return true;
	}
	
	private void popupTransitionStep(float x) {
		this.setOpacity(1 - x);
		this.setPosition(this.popX - x * 16, this.popY);
	}
	
	public void addItem(UIMenuItem item) {
		if(item.parent != null) item.parent.removeItem(item);
		
		this.add(new UIPopupMenuItem(item));
		this.items.add(item);
		
		this.repaint();
	}
	
	public UIMenuItem removeItem(UIMenuItem item) {
		return this.removeItem(this.items.indexOf(item));
	}
	
	public UIMenuItem removeItem(int i) {
		if(i < 0 || i >= this.items.size()) return null;
		
		this.remove(i);
		return this.items.remove(i);
	}
	
	public UIMenuItem getItem(int i) {
		return this.items.get(i);
	}
	
	public int getItemCount() {
		return this.items.size();
	}
	
	public void clearItems() {
		this.items.clear();
		this.clear();
	}
	
	public void onPopUp(float x, float y) {
		this.popX = x;
		this.popY = y;
		
		this.popupTransition.restart(1, 0);
		
		for(int i = 0; i < this.getChildrenCount(); i++) {
			UIComponent compChild = this.getChild(i);
			
			if(compChild instanceof UIPopupMenuItem) {
				UIPopupMenuItem menuItem = (UIPopupMenuItem) compChild;
				menuItem.startAppear(i * ITEM_APPEAR_DELAY);
			}
		}
	}
	
	protected void paintComponent(GraphicsContext2D gtx) {
		gtx.setStroke(this.getTextColor());
		
		gtx.setGlobalAlpha(0.2f);
		gtx.strokeRoundedRect(0, 0, this.getWidth(), this.getHeight(), 2);
	}
	
	public Color getBackground() {
		return this.background == null && this.getTheme() != null ? this.getTheme().getPopupBackgroundColor() : this.background;
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
	
	private void openSubPopup(UIMenu menu, float x, float y) {
		this.closePopupMenu();
		this.setPopupMenu(menu.popupMenu);
		this.openPopupMenu(x + this.subPopupPositionOffset.x(), y + this.subPopupPositionOffset.y());
	}
	
	private void clickedMenuItem(UIMenuItem item, float x, float y) {
		UIRootPane root = this.getRootPane();
		if(root == null) return;
		
		item.doClick();
		
		if(item instanceof UIMenu) {
			UIMenu menu = (UIMenu) item;
			if(this.getPopupMenu() != null && this.getPopupMenu().isEffectivelyVisible() && menu.popupMenu == this.getPopupMenu()) return;
			
			this.openSubPopup(menu, x, y);
		} else {
			root.closePopup();
		}
	}
	
	private void enteredMenuItem(UIMenuItem item, float x, float y) {
		this.closePopupMenu();
	}
	
	private class UIPopupMenuItem extends UIComponent {
		private final UIPopupMenu pop;
		private UIMenuItem item;
		
		private FloatTransition appearTransition = new FloatTransition(0.4f, this::transitionStep, 1, Back::easeOut);
		private FloatTransition hoverTransition = new FloatTransition(0.1f, this::transitionStep, 0);
		
		public UIPopupMenuItem(UIMenuItem item) {
			this.pop = UIPopupMenu.this;
			this.item = item;
			
			this.setPadding(ITEM_PADDING / 2f, PADDING - 1);
		}
		
		public void startAppear(float x) {
			this.appearTransition.restart(1, 0, x);
		}
		
		private void transitionStep(float x) {
			this.repaint();
			if(this.parent != null) this.parent.repaint();
		}
		
		public boolean mouseEntered(float x, float y, float rx, float ry) {
			this.pop.enteredMenuItem(this.item, this.getX() + this.getWidth(), this.getY() - PADDING);
			
			this.hoverTransition.setTargetValue(1);
			return true;
		}
		
		public boolean mouseExited(float x, float y, float rx, float ry) {
			this.hoverTransition.setTargetValue(0);
			return true;
		}
		
		public boolean mouseButtonReleased(float x, float y, int btn) {
			if(btn == Nhengine.MOUSE_BUTTON_LEFT || btn == Nhengine.MOUSE_BUTTON_RIGHT) this.pop.clickedMenuItem(this.item, this.getX() + this.getWidth(), this.getY() - PADDING);
			this.hoverTransition.setTargetValue(0);
			return true;
		}
		
		public void computePreferredSize() {
			this.preferredSize.zero();
			if(this.item == null || this.pop == null) return;
			
			if(this.item instanceof UIMenuSeparator) {
				this.preferredSize.x = this.getPaddingLeft() + this.getPaddingRight() + ICON_MARGIN + ICON_SEPARATOR_MARGIN + RIGHT_MARGIN;
				this.preferredSize.y = ((UIMenuSeparator) this.item).getSpace() + this.getPaddingTop() + this.getPaddingBottom();
				return;
			}
			
			if(this.pop.getFont() == null || this.item.getText() == null) return;
			
			Nhutils.measureText(this.pop.getFont(), this.item.getText(), this.preferredSize);
			
			this.preferredSize.x += this.getPaddingLeft() + this.getPaddingRight() + ICON_MARGIN + ICON_SEPARATOR_MARGIN + RIGHT_MARGIN;
			this.preferredSize.y += this.getPaddingTop() + this.getPaddingBottom();
		}
		
		protected void paintComponent(GraphicsContext2D gtx) {
			if(this.item == null || this.pop == null || this.pop.getFont() == null) return;
			
			float w = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
			float h = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
			
			if(this.item instanceof UIMenuSeparator) {
				gtx.setStroke(this.pop.getTextColor());
				gtx.setGlobalAlpha(0.2f);
				gtx.strokeLine(this.getPaddingLeft(), this.getPaddingTop() + h / 2f, this.getPaddingLeft() + (w - w * this.appearTransition.getValue()), this.getPaddingTop() + h / 2f);
				return;
			}
			
			gtx.setFill(this.pop.getTextColor());
			gtx.setStroke(this.pop.getTextColor());
			gtx.setFont(this.pop.getFont());
			gtx.setTextBaseline(TextBaseline.ASCENT_MIDDLE);
			
			gtx.save();
			gtx.setGlobalAlpha(0.2f);
			gtx.strokeLine(this.getPaddingLeft() + ICON_MARGIN, 0, this.getPaddingLeft() + ICON_MARGIN, this.getHeight());
			
			if(this.isMouseOver()) {
				gtx.setGlobalAlpha(this.hoverTransition.getValue() * 0.4f);
				gtx.fillRect(0, 0, this.getWidth(), this.getHeight());
			}
			
			gtx.restore();
			
			if(this.item.getIcon() != null) {
				float s = ICON_MARGIN - ICON_SEPARATOR_MARGIN;
				
				gtx.drawImage(this.item.getIcon(), this.getPaddingLeft(), this.getPaddingTop() + h / 2f - s / 2f, s, s);
			}
			
			if(this.item instanceof UIMenu) {
				float arrowSize = h / 4f;
				
				// Draw the arrow
				gtx.beginPath();
				gtx.moveTo(this.getWidth() - this.getPaddingRight() - arrowSize, this.getPaddingTop() + h / 2f - arrowSize);
				gtx.lineTo(this.getWidth() - this.getPaddingRight(), this.getPaddingTop() + h / 2f);
				gtx.lineTo(this.getWidth() - this.getPaddingRight() - arrowSize, this.getPaddingTop() + h / 2f + arrowSize);
				gtx.stroke();
			}
			
			gtx.setGlobalAlpha(1 - this.appearTransition.getValue());
			gtx.fillText(this.item.getText().toString(), this.getPaddingLeft() + ICON_SEPARATOR_MARGIN + ICON_MARGIN + this.hoverTransition.getValue() * 4 - this.appearTransition.getValue() * 8, this.getPaddingTop() + h / 2f);
		}
	}
}
