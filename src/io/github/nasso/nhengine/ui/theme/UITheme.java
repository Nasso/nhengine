package io.github.nasso.nhengine.ui.theme;

import java.util.HashMap;
import java.util.Map;

import io.github.nasso.nhengine.graphics.Color;
import io.github.nasso.nhengine.graphics.FontFamily;

public abstract class UITheme {
	public static final UITheme DEFAULT_INSTANCE = new UIDefaultDarkTheme(true);
	
	public static final float USE_THEME_VALUE = Float.NaN;
	
	private Map<String, Float> numbers = new HashMap<String, Float>();
	private Map<String, FontFamily> fonts = new HashMap<String, FontFamily>();
	private Map<String, Color> colors = new HashMap<String, Color>();
	
	public void putNumber(String key, float value) {
		this.numbers.put(key, value);
	}
	
	public void putFontFamily(String key, FontFamily value) {
		this.fonts.put(key, value);
	}
	
	public void putColor(String key, Color value) {
		this.colors.put(key, value);
	}

	public float getFloat(String key, float override) {
		if(!this.numbers.containsKey(key)) {
			System.out.println("Couldn't find key " + key);
		}
		
		return Float.isNaN(override) ? this.numbers.getOrDefault(key, 0.0F) : override;
	}
	
	public FontFamily getFontFamily(String key, FontFamily override) {
		if(!this.fonts.containsKey(key)) {
			System.out.println("Couldn't find key " + key);
		}
		
		return override == null ? this.fonts.getOrDefault(key, null) : override;
	}
	
	public Color getColor(String key, Color override) {
		if(!this.colors.containsKey(key)) {
			System.out.println("Couldn't find key " + key);
		}
		
		return override == null ? this.colors.getOrDefault(key, Color.BLACK) : override;
	}
}
