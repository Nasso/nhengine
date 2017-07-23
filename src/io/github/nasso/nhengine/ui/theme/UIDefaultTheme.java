package io.github.nasso.nhengine.ui.theme;

import java.io.IOException;

import io.github.nasso.nhengine.data.TrueTypeFont;
import io.github.nasso.nhengine.graphics.FontFamily;

public abstract class UIDefaultTheme implements UITheme {
	private FontFamily font;
	
	public UIDefaultTheme() {
		this(true);
	}
	
	public UIDefaultTheme(boolean fontSmoothing) {
		try {
			int size = 18;
			
			this.font = new FontFamily();
			this.font.setCondensed(new TrueTypeFont("res/fonts/Ubuntu-C.ttf", size, fontSmoothing, true));
			this.font.setLight(new TrueTypeFont("res/fonts/Ubuntu-L.ttf", size, fontSmoothing, true));
			this.font.setLightItalic(new TrueTypeFont("res/fonts/Ubuntu-LI.ttf", size, fontSmoothing, true));
			this.font.setRegular(new TrueTypeFont("res/fonts/Ubuntu-R.ttf", size, fontSmoothing, true));
			this.font.setRegularItalic(new TrueTypeFont("res/fonts/Ubuntu-RI.ttf", size, fontSmoothing, true));
			this.font.setMedium(new TrueTypeFont("res/fonts/Ubuntu-M.ttf", size, fontSmoothing, true));
			this.font.setMediumItalic(new TrueTypeFont("res/fonts/Ubuntu-MI.ttf", size, fontSmoothing, true));
			this.font.setBold(new TrueTypeFont("res/fonts/Ubuntu-B.ttf", size, fontSmoothing, true));
			this.font.setBoldItalic(new TrueTypeFont("res/fonts/Ubuntu-BI.ttf", size, fontSmoothing, true));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public FontFamily getFontFamily() {
		return this.font;
	}
	
	public float getButtonBorderRadius() {
		return 4;
	}
	
	public float getButtonBorderSize() {
		return 2;
	}
	
	public float getProgressBarBorderRadius() {
		return 4;
	}
	
	public float getToggleButtonRoundness() {
		return 0.5f;
	}
	
	public float getSliderBarHeight() {
		return 4;
	}
	
	public float getSliderBorderRadius() {
		return 2;
	}
	
	public float getSliderDotBorderRadius() {
		return 2;
	}
	
	public float getSliderDotWidth() {
		return 8;
	}
	
	public float getSliderDotHeight() {
		return 18;
	}
	
	public float getTextFieldBorderRadius() {
		return 2;
	}
	
	public float getTextFieldBorderSize() {
		return 2;
	}
	
	public float getTabAngleWidth() {
		return 8;
	}
	
	public float getTabCloseButtonSize() {
		return 12;
	}
	
	public float getTabCloseButtonBorderRadius() {
		return 8;
	}
	
	public float getScrollBarSize() {
		return 16;
	}
	
	public float getScrollBarThumbBorderSize() {
		return 0;
	}
	
	public float getScrollBarThumbBorderRadius() {
		return 0;
	}
	
	public float getDialogTitleBarHeight() {
		return 32;
	}
	
	public float getDialogCloseButtonBorderRadius() {
		return 4;
	}
}
