package io.github.nasso.nhengine.ui.theme;

import io.github.nasso.nhengine.graphics.Color;

public class UIDefaultDarkTheme extends UIDefaultTheme {
	public static final Color PANE_BG = Color.get(0.28f);
	public static final Color SEPARATOR_COLOR = Color.get(0.4f);
	public static final Color TEXT_COLOR = Color.get(0.9f);
	public static final Color BUTTON_COLOR = Color.get(0.2f);
	public static final Color BUTTON_BORDER_COLOR = Color.get(0.15f);
	public static final Color BUTTON_HOVER_COLOR = Color.get(0.25f);
	public static final Color BUTTON_PRESSED_COLOR = Color.get(0.3f);
	public static final Color TOGGLE_BUTTON_COLOR = Color.get(1f);
	public static final Color TOGGLE_BUTTON_SEL_COLOR = Color.ROYAL_BLUE;
	public static final Color TOGGLE_BUTTON_UNSEL_COLOR = Color.get(0.15f);
	public static final Color POPUP_BG = Color.get(0.15f);
	public static final Color MENUBAR_BG = Color.get(0.2f);
	public static final Color PROGRESSBAR_BG = Color.get(0.2f);
	public static final Color PROGRESSBAR_FG = Color.get(0.3f);
	public static final Color SLIDER_BG = Color.get(0.2f);
	public static final Color SLIDER_FG = Color.get(0.9f);
	public static final Color TEXTFIELD_SELECTION = Color.get(0f, 0.5f, 1f, 0.5f);
	public static final Color TEXTFIELD_BG = Color.get(0.2f);
	public static final Color TEXTFIELD_BORDER_COLOR = Color.get(0.15f);
	public static final Color TABBEDPANE_BG = Color.get(0.15f);
	public static final Color TAB_BG = Color.get(0.2f);
	public static final Color ACTIVETAB_BG = Color.get(0.28f);
	public static final Color TAB_CLOSE_BG = Color.get(0.4f);
	public static final Color TAB_CLOSE_FG = Color.get(1f);
	public static final Color TAB_CLOSE_HOVER_BG = Color.get(1f, 0.3f, 0.3f);
	public static final Color SCROLL_BAR_ARROW_BTN_BG = Color.get(0.2f);
	public static final Color SCROLL_BAR_BG = Color.get(0.25f);
	public static final Color SCROLL_BAR_THUMB = Color.get(0.2f);
	public static final Color SCROLL_BAR_THUMB_BORDER = Color.get(0.25f);
	public static final Color DIALOG_TITLEBAR_BG = Color.get(0.2f);
	public static final Color DIALOG_CLOSE_BUTTON_COLOR = Color.get(0.15f);
	public static final Color DIALOG_CLOSE_BUTTON_HOVER_COLOR = Color.get(1f, 0.3f, 0.3f);
	public static final Color DIALOG_CLOSE_BUTTON_PRESSED_COLOR = Color.get(0.7f, 0.2f, 0.2f);
	public static final Color DIALOG_MODAL_FOCUS_COLOR = Color.ROYAL_BLUE;
	
	public UIDefaultDarkTheme(boolean smoothFont) {
		super(smoothFont);
	}
	
	public UIDefaultDarkTheme() {
		super();
	}
	
	public Color getPaneBackground() {
		return PANE_BG;
	}
	
	public Color getSeparatorColor() {
		return SEPARATOR_COLOR;
	}
	
	public Color getTextColor() {
		return TEXT_COLOR;
	}
	
	public Color getButtonColor() {
		return BUTTON_COLOR;
	}
	
	public Color getButtonBorderColor() {
		return BUTTON_BORDER_COLOR;
	}
	
	public Color getButtonHoverColor() {
		return BUTTON_HOVER_COLOR;
	}
	
	public Color getButtonPressedColor() {
		return BUTTON_PRESSED_COLOR;
	}
	
	public Color getToggleButtonColor() {
		return TOGGLE_BUTTON_COLOR;
	}
	
	public Color getToggleButtonSelectedColor() {
		return TOGGLE_BUTTON_SEL_COLOR;
	}
	
	public Color getToggleButtonUnselectedColor() {
		return TOGGLE_BUTTON_UNSEL_COLOR;
	}
	
	public Color getPopupBackgroundColor() {
		return POPUP_BG;
	}
	
	public Color getMenuBarBackgroundColor() {
		return MENUBAR_BG;
	}
	
	public Color getProgressBarBackgroundColor() {
		return PROGRESSBAR_BG;
	}
	
	public Color getProgressBarForegroundColor() {
		return PROGRESSBAR_FG;
	}
	
	public Color getSliderBackgroundColor() {
		return SLIDER_BG;
	}
	
	public Color getSliderForegroundColor() {
		return SLIDER_FG;
	}
	
	public Color getTextFieldBackgroundColor() {
		return TEXTFIELD_BG;
	}
	
	public Color getTextFieldBorderColor() {
		return TEXTFIELD_BORDER_COLOR;
	}
	
	public Color getTabbedPaneBackgroundColor() {
		return TABBEDPANE_BG;
	}
	
	public Color getTabBackgroundColor() {
		return TAB_BG;
	}
	
	public Color getActiveTabBackgroundColor() {
		return ACTIVETAB_BG;
	}
	
	public Color getTabCloseButtonBackground() {
		return TAB_CLOSE_BG;
	}
	
	public Color getTabCloseButtonForeground() {
		return TAB_CLOSE_FG;
	}
	
	public Color getTabCloseButtonHoverBackground() {
		return TAB_CLOSE_HOVER_BG;
	}
	
	public Color getTextFieldSelectionColor() {
		return TEXTFIELD_SELECTION;
	}
	
	public Color getScrollBarArrowButtonBackground() {
		return SCROLL_BAR_ARROW_BTN_BG;
	}
	
	public Color getScrollBarBackground() {
		return SCROLL_BAR_BG;
	}
	
	public Color getScrollBarThumbColor() {
		return SCROLL_BAR_THUMB;
	}
	
	public Color getScrollBarThumbBorderColor() {
		return SCROLL_BAR_THUMB_BORDER;
	}
	
	public Color getDialogTitleBarColor() {
		return DIALOG_TITLEBAR_BG;
	}
	
	public Color getDialogCloseButtonColor() {
		return DIALOG_CLOSE_BUTTON_COLOR;
	}
	
	public Color getDialogCloseButtonHoverColor() {
		return DIALOG_CLOSE_BUTTON_HOVER_COLOR;
	}
	
	public Color getDialogCloseButtonPressedColor() {
		return DIALOG_CLOSE_BUTTON_PRESSED_COLOR;
	}
	
	public Color getDialogModalFocusColor() {
		return DIALOG_MODAL_FOCUS_COLOR;
	}
}
