package io.github.nasso.nhengine.ui;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import io.github.nasso.nhengine.core.Game;
import io.github.nasso.nhengine.core.Nhengine;
import io.github.nasso.nhengine.graphics.GraphicsContext2D;
import io.github.nasso.nhengine.ui.control.UIDialog;
import io.github.nasso.nhengine.ui.control.UIMenuBar;
import io.github.nasso.nhengine.ui.control.UIPopupMenu;
import io.github.nasso.nhengine.ui.layout.UIBorderLayout;
import io.github.nasso.nhengine.ui.layout.UILayout;
import io.github.nasso.nhengine.ui.theme.UITheme;
import io.github.nasso.nhengine.utils.FloatTransition;
import io.github.nasso.nhengine.utils.MathUtils;

public class UIRootPane extends UIContainer {
	private class RootPaneLayout implements UILayout {
		public void apply(UIContainer parent) {
			UIMenuBar menu = UIRootPane.this.menuBar;
			
			float top = parent.getPaddingTop();
			float left = parent.getPaddingLeft();
			float right = parent.getPaddingRight();
			float bottom = parent.getPaddingBottom();
			
			float w = parent.getWidth();
			float h = parent.getHeight();
			
			if(menu != null) {
				Vector2f menuPrefSize = menu.getPreferredSize();
				menu.setBounds(left, top, w - left - right, menuPrefSize.y);
				top += menuPrefSize.y;
			}
			
			UIContainer content = UIRootPane.this.contentPane;
			
			if(content != null) {
				content.setBounds(left, top, w - left - right, h - bottom - top);
			}
			
			for(int i = 0; i < parent.getChildrenCount(); i++) {
				UIComponent child = parent.getChild(i);
				if(child == null) continue;
				
				if(child != content && child != menu) {
					if(child instanceof ModalLayer) child.setBounds(0, 0, w, h);
					else child.setBounds(child.getX(), child.getY(), child.getPreferredSize().x, child.getPreferredSize().y);
				}
			}
		}
		
		public void getPreferredSize(UIContainer parent, Vector2f dest) {
		}
		
		public void addComponent(int index, UIComponent comp, Object constraints) {
		}
		
		public void removeComponent(UIComponent comp) {
		}
		
		public void removeAllComponents() {
		}
	}
	
	private class ModalLayer extends UIComponent {
		private UIDialog dial;
		
		private FloatTransition blinkTransition = new FloatTransition(0.5f, this::setTransitionValue, 0, (t, b, c, d) -> {
			return (float) Math.abs(Math.sin((b + t / d * c) * Math.PI * 2));
		});
		
		public ModalLayer(UIDialog dial) {
			this.dial = dial;
			this.blinkTransition.setTargetValue(1);
		}
		
		private void setTransitionValue(float val) {
			this.repaint();
		}
		
		public void computePreferredSize() {
			// Who cares about setting the pref size, we're setting the size in the layout anyway!
		}
		
		protected void paintComponent(GraphicsContext2D gtx) {
			if(!this.blinkTransition.isFinished()) {
				gtx.setGlobalAlpha(this.blinkTransition.getValue());
				gtx.setStroke(this.getTheme().getDialogModalFocusColor());
				gtx.setStrokeSize(2);
				gtx.strokeRect(this.dial.getX(), this.dial.getY(), this.dial.getWidth(), this.dial.getHeight());
			}
		}
		
		public boolean mouseClicked(int btn) {
			this.blinkTransition.setCurrentTime(0);
			
			return true;
		}
	}
	
	private UICanvas cvs = null;
	
	private UIContainer contentPane;
	private UIMenuBar menuBar;
	
	UIPopupMenu activePopup = null;
	
	private List<ModalLayer> modalLayers = new ArrayList<ModalLayer>();
	
	public UIRootPane(UICanvas cvs) {
		this.setLayout(new RootPaneLayout());
		this.setOpaque(false);
		
		this.contentPane = new UIContainer();
		this.add(this.contentPane, UIBorderLayout.CENTER);
		
		this.cvs = cvs;
	}
	
	public void pushModalLayer(UIDialog dial) {
		ModalLayer l = new ModalLayer(dial);
		this.modalLayers.add(l);
		this.add(l);
	}
	
	public void popModalLayer() {
		ModalLayer l = this.modalLayers.remove(this.modalLayers.size() - 1);
		
		this.remove(l);
	}
	
	public UITheme getTheme() {
		return this.cvs.getTheme();
	}
	
	public void requestFocus(UIComponent comp) {
		this.cvs.setFocusComponent(comp);
	}
	
	public boolean hasFocus(UIComponent comp) {
		return this.cvs.getFocusComponent() == comp;
	}
	
	public UIRootPane getRootPane() {
		return this;
	}
	
	private boolean isSubPopup(UIPopupMenu parent, UIPopupMenu a) {
		if(parent == null) return false;
		if(a == null) return parent.getPopupMenu() == null;
		
		return parent.getPopupMenu() != null && (parent.getPopupMenu() == a || this.isSubPopup(parent.getPopupMenu(), a));
	}
	
	private void removePopup(UIPopupMenu popup) {
		if(popup == null) return;
		
		this.remove(popup);
		this.removePopup(popup.getPopupMenu());
	}
	
	void mouseEnteredComponent(UIComponent comp) {
		Game.instance().window().setCursor(comp.getCursor());
	}
	
	void mouseExitedComponent(UIComponent comp) {
		Game.instance().window().setCursor(null);
	}
	
	public void openPopup(UIPopupMenu popup, float x, float y) {
		if(popup == null) return;
		if(popup == this.activePopup) {
			this.activePopup.setPosition(x, y);
			return;
		}
		
		if(!this.isSubPopup(this.activePopup, popup)) {
			if(this.activePopup != null) {
				this.removePopup(this.activePopup);
			}
			
			this.activePopup = popup;
		}
		
		this.add(popup);
		
		float pw = popup.getPreferredSize().x;
		float ph = popup.getPreferredSize().y;
		
		x = MathUtils.clamp(x, 0, this.getWidth() - pw - 1);
		y = MathUtils.clamp(y, 0, this.getHeight() - ph - 1);
		
		popup.onPopUp(x, y);
	}
	
	public void closePopup(UIPopupMenu popup) {
		if(popup == null) return;
		if(!popup.isChildOf(this)) return;
		
		this.closePopup(popup.getPopupMenu());
		
		this.remove(popup);
		if(popup == this.activePopup) this.activePopup = null;
		
		this.repaint();
	}
	
	public void closePopup() {
		this.closePopup(this.activePopup);
	}
	
	public void focus() {
		this.cvs.setFocusComponent(this);
	}
	
	public void repaint() {
		if(this.cvs != null) this.cvs.requestAutoRepaint();
	}
	
	private boolean isHoverPopups(UIPopupMenu parentPopup, float x, float y) {
		return parentPopup.bounds.contains(x, y) || (parentPopup.getPopupMenu() != null && this.isHoverPopups(parentPopup.getPopupMenu(), x, y));
	}
	
	public boolean mouseButtonPressed(float x, float y, int btn) {
		if(this.activePopup != null && !this.isHoverPopups(this.activePopup, x, y) && btn == Nhengine.MOUSE_BUTTON_LEFT) {
			this.closePopup();
		}
		
		return true;
	}
	
	public UIContainer getContentPane() {
		return this.contentPane;
	}
	
	public UIMenuBar getMenuBar() {
		return this.menuBar;
	}
	
	public void setMenuBar(UIMenuBar menuBar) {
		if(menuBar == null || menuBar == this.menuBar) return;
		if(this.menuBar != null) {
			this.remove(this.menuBar);
		}
		
		this.menuBar = menuBar;
		this.add(this.menuBar, UIBorderLayout.NORTH);
	}
}
