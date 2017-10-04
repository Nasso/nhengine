package io.github.nasso.nhengine.ui.control;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import io.github.nasso.nhengine.core.Nhengine;
import io.github.nasso.nhengine.graphics.Color;
import io.github.nasso.nhengine.graphics.GraphicsContext2D;
import io.github.nasso.nhengine.ui.UIComponent;
import io.github.nasso.nhengine.ui.UIContainer;
import io.github.nasso.nhengine.ui.layout.UIBorderLayout;
import io.github.nasso.nhengine.ui.layout.UIBoxLayout;
import io.github.nasso.nhengine.ui.layout.UICardLayout;
import io.github.nasso.nhengine.utils.FloatTransition;
import io.github.nasso.nhengine.utils.MathUtils;
import io.github.nasso.nhengine.utils.Nhutils;

public class UITabbedPane extends UIContainer {
	private class TabView extends UIContainer {
		private class TabViewCloseButton extends UIComponent {
			private FloatTransition hoverTransition = new FloatTransition(0.2f, this::transitionStep, 0);
			
			public TabViewCloseButton() {
				
			}
			
			private void transitionStep(float x) {
				this.repaint();
			}
			
			public boolean mouseEntered(float x, float y, float rx, float ry) {
				this.hoverTransition.setTargetValue(1);
				return true;
			}
			
			public boolean mouseExited(float x, float y, float rx, float ry) {
				this.hoverTransition.setTargetValue(0);
				return true;
			}
			
			public boolean mouseClicked(int btn) {
				if(btn == Nhengine.MOUSE_BUTTON_LEFT) {
					TabView.this.closeTab();
				}
				
				return false;
			}
			
			protected void paintComponent(GraphicsContext2D gtx) {
				float w = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
				float h = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
				
				gtx.translate(this.getPaddingLeft(), this.getPaddingTop());
				
				if(this.hoverTransition.isFinished()) {
					gtx.setFill(this.isMouseOver() ? UITabbedPane.this.getTabCloseButtonHoverBackground() : UITabbedPane.this.getTabCloseButtonBackground());
				} else {
					gtx.setFill(Nhutils.colorBlend(UITabbedPane.this.getTabCloseButtonBackground(), UITabbedPane.this.getTabCloseButtonHoverBackground(), this.hoverTransition.getValue()));
				}
				
				gtx.fillRoundedRect(0, 0, w, h, UITabbedPane.this.getTabCloseButtonBorderRadius());
				
				gtx.setStroke(UITabbedPane.this.getTabCloseButtonForeground());
				gtx.beginPath();
				gtx.moveTo(w * 0.25f, h * 0.25f);
				gtx.lineTo(w * 0.75f, h * 0.75f);
				gtx.moveTo(w * 0.75f, h * 0.25f);
				gtx.lineTo(w * 0.25f, h * 0.75f);
				gtx.stroke();
			}
			
			public void computePreferredSize() {
				this.preferredSize.set(UITabbedPane.this.getTabCloseButtonSize());
				
				this.preferredSize.x += this.getPaddingLeft() + this.getPaddingRight();
				this.preferredSize.y += this.getPaddingTop() + this.getPaddingBottom();
			}
		}
		
		private Tab tab;
		
		private UILabel label;
		private TabViewCloseButton closeBtn;
		
		public TabView(Tab t) {
			this.tab = t;
			
			this.label = new UILabel(null, UILabel.ANCHOR_LEFT) {
				public CharSequence getText() {
					return TabView.this.tab.title;
				}
			};
			this.label.setPadding(0, 4, 0, 2);
			
			this.closeBtn = new TabViewCloseButton();
			this.closeBtn.setPadding(0, 0, 2, 0);
			
			this.setLayout(new UIBoxLayout(UIBoxLayout.HORIZONTAL, 4, UIBoxLayout.BOTTOM, false));
			this.setOpaque(false);
			this.add(this.label);
			
			if(this.tab.closeable) this.add(this.closeBtn);
			
			this.setPadding(2, 4);
		}
		
		private void closeTab() {
			this.tab.closeTab();
		}
		
		public float getPaddingLeft() {
			return super.getPaddingLeft() + UITabbedPane.this.getTabAngleWidth();
		}
		
		public float getPaddingRight() {
			return super.getPaddingRight() + UITabbedPane.this.getTabAngleWidth();
		}
		
		public boolean mouseClicked(int btn) {
			if(btn == Nhengine.MOUSE_BUTTON_LEFT) UITabbedPane.this.setTab(this.tab);
			return true;
		}
		
		private boolean isActive() {
			return this.tab == UITabbedPane.this.getCurrentTab();
		}
		
		protected void paintComponent(GraphicsContext2D gtx) {
			gtx.setFill(this.isActive() ? UITabbedPane.this.getActiveTabBackground() : UITabbedPane.this.getTabBackground());
			
			float angleW = UITabbedPane.this.getTabAngleWidth();
			
			if(angleW <= 0) {
				gtx.fillRect(0, 0, this.getWidth(), this.getHeight());
			} else {
				gtx.beginPath();
				gtx.moveTo(0, this.getHeight());
				gtx.lineTo(angleW, 0);
				gtx.lineTo(this.getWidth() - angleW, 0);
				gtx.lineTo(this.getWidth(), this.getHeight());
				gtx.fill();
				
				gtx.translate(angleW, 0);
			}
		}
	}
	
	public class Tab {
		private TabView view;
		private CharSequence title;
		private boolean closeable;
		private UIComponent comp;
		
		private Consumer<Tab> onClosing, onClosed;
		
		private Tab(UIComponent comp, CharSequence title, boolean closeable) {
			this.comp = comp;
			this.title = title;
			this.closeable = closeable;
			this.view = new TabView(this);
		}
		
		public boolean isCloseable() {
			return this.closeable;
		}
		
		public void setCloseable(boolean closeable) {
			this.closeable = closeable;
		}
		
		private void closeTab() {
			if(!this.closeable) return;
			
			if(this.onClosing != null) this.onClosing.accept(this);
			UITabbedPane.this.removeTab(this);
			if(this.onClosed != null) this.onClosed.accept(this);
		}
		
		public UIComponent getComponent() {
			return this.comp;
		}
		
		public void setComponent(UIComponent comp) {
			this.comp = comp;
		}
		
		public CharSequence getTitle() {
			return this.title;
		}
		
		public void setTitle(CharSequence title) {
			this.title = title;
		}
		
		public Consumer<Tab> getOnClosing() {
			return this.onClosing;
		}
		
		public void setOnClosing(Consumer<Tab> onClosing) {
			this.onClosing = onClosing;
		}
		
		public Consumer<Tab> getOnClosed() {
			return this.onClosed;
		}
		
		public void setOnClosed(Consumer<Tab> onClosed) {
			this.onClosed = onClosed;
		}
	}
	
	private List<Tab> tabs = new ArrayList<Tab>();
	
	private int currentTab = -1;
	
	private float tabAngleWidth = USE_THEME_VALUE;
	private float tabCloseBtnBorderRadius = USE_THEME_VALUE;
	private float tabCloseBtnSize = USE_THEME_VALUE;
	
	private UIContainer head;
	private UIContainer body;
	
	private UICardLayout cardLayout;
	
	private Color tabBackground = null;
	private Color activeTabBackground = null;
	private Color tabCloseButtonBackground = null;
	private Color tabCloseButtonForeground = null;
	private Color tabCloseButtonHoverBackground = null;
	
	private Consumer<UITabbedPane> onTabChange;
	
	public UITabbedPane() {
		this.head = new UIContainer();
		this.head.setPadding(0, 2);
		this.head.setOpaque(false);
		this.head.setLayout(new UIBoxLayout(UIBoxLayout.HORIZONTAL, 2));
		
		this.body = new UIContainer() {
			public Color getBackground() {
				return UITabbedPane.this.getActiveTabBackground();
			}
		};
		this.body.setPadding(1);
		this.body.setLayout(this.cardLayout = new UICardLayout());
		
		this.setLayout(new UIBorderLayout());
		this.add(this.head, UIBorderLayout.NORTH);
		this.add(this.body, UIBorderLayout.CENTER);
	}
	
	public Color getBackground() {
		return this.getTheme().getColor("tabPane.background", this.background);
	}
	
	private void fireTabChange() {
		if(this.onTabChange != null) this.onTabChange.accept(this);
	}
	
	public Tab insertTab(int i, CharSequence title, boolean closeable, UIComponent comp) {
		i = MathUtils.clamp(i, 0, this.tabs.size());
		
		Tab newTab = new Tab(comp, title, closeable);
		this.tabs.add(i, newTab);
		
		this.head.add(i, newTab.view);
		this.body.add(i, newTab.comp);
		
		this.repack();
		
		if(this.currentTab < 0) this.setTab(i);
		
		return newTab;
	}
	
	public Tab insertTab(String title, UIComponent comp) {
		return this.insertTab(this.tabs.size(), title, true, comp);
	}
	
	public void removeTab(int i) {
		if(i < 0 || i >= this.tabs.size()) return;
		
		this.tabs.remove(i);
		this.head.remove(i);
		this.body.remove(i);
		this.head.repack();
		
		if(this.currentTab == i) {
			this.currentTab = -1; // Trick to force change
			if(i < this.tabs.size()) this.setTab(i);
			else this.setTab(this.tabs.size() - 1);
		} else if(this.currentTab > i) {
			this.currentTab--;
		}
	}
	
	public void removeTab(Tab t) {
		if(!this.tabs.contains(t)) return;
		
		this.removeTab(this.tabs.indexOf(t));
	}
	
	public Tab getTab(int i) {
		if(i < 0 || i >= this.tabs.size()) return null;
		return this.tabs.get(i);
	}
	
	public void setTab(Tab t) {
		if(!this.tabs.contains(t)) return;
		this.setTab(this.tabs.indexOf(t));
	}
	
	public void setTab(int i) {
		if(this.currentTab == i) return;
		
		this.cardLayout.setCurrentCard(i);
		this.currentTab = i;
		this.fireTabChange();
	}
	
	public Tab getCurrentTab() {
		return this.getTab(this.getCurrentTabIndex());
	}
	
	public int getCurrentTabIndex() {
		return this.currentTab;
	}
	
	public Color getTabBackground() {
		return this.getTheme().getColor("tabPane.tab.background", this.tabBackground);
	}
	
	public void setTabBackground(Color tabBackground) {
		this.tabBackground = tabBackground;
	}
	
	public float getTabAngleWidth() {
		return this.getTheme().getFloat("tabPane.tab.angleWidth", this.tabAngleWidth);
	}
	
	public void setTabAngleWidth(float tabAngleWidth) {
		this.tabAngleWidth = tabAngleWidth;
	}
	
	public Color getActiveTabBackground() {
		return this.getTheme().getColor("tabPane.activeTab.background", this.activeTabBackground);
	}
	
	public void setActiveTabBackground(Color activeTabBackground) {
		this.activeTabBackground = activeTabBackground;
	}
	
	public float getTabCloseButtonSize() {
		return this.getTheme().getFloat("tabPane.tab.closeButton.size", this.tabCloseBtnSize);
	}
	
	public void setTabCloseButtonSize(float tabCloseBtnSize) {
		this.tabCloseBtnSize = tabCloseBtnSize;
	}
	
	public float getTabCloseButtonBorderRadius() {
		return this.getTheme().getFloat("tabPane.tab.closeButton.borderRadius", this.tabCloseBtnBorderRadius);
	}
	
	public void setTabCloseButtonBorderRadius(float tabCloseBtnBorderRadius) {
		this.tabCloseBtnBorderRadius = tabCloseBtnBorderRadius;
	}
	
	public Color getTabCloseButtonBackground() {
		return this.getTheme().getColor("tabPane.tab.closeButton.background", this.tabCloseButtonBackground);
	}
	
	public void setTabCloseButtonBackground(Color tabCloseButtonBackground) {
		this.tabCloseButtonBackground = tabCloseButtonBackground;
	}
	
	public Color getTabCloseButtonForeground() {
		return this.getTheme().getColor("tabPane.tab.closeButton.foreground", this.tabCloseButtonForeground);
	}
	
	public void setTabCloseButtonForeground(Color tabCloseButtonForeground) {
		this.tabCloseButtonForeground = tabCloseButtonForeground;
	}
	
	public Color getTabCloseButtonHoverBackground() {
		return this.getTheme().getColor("tabPane.tab.closeButton.hoverBackground", this.tabCloseButtonHoverBackground);
	}
	
	public void setTabCloseButtonHoverBackground(Color tabCloseButtonHoverBackground) {
		this.tabCloseButtonHoverBackground = tabCloseButtonHoverBackground;
	}
	
	public Consumer<UITabbedPane> getOnTabChange() {
		return this.onTabChange;
	}
	
	public void setOnTabChange(Consumer<UITabbedPane> onTabChange) {
		this.onTabChange = onTabChange;
	}
}
