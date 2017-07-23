package io.github.nasso.nhengine.graphics;

public class FontFamily {
	public static enum FontStyle {
		LIGHT, LIGHT_ITALIC, REGULAR, REGULAR_ITALIC, MEDIUM, MEDIUM_ITALIC, BOLD, BOLD_ITALIC, CONDENSED
	}
	
	private Font light;
	private Font regular;
	private Font medium;
	private Font bold;
	private Font lightItalic;
	private Font regularItalic;
	private Font mediumItalic;
	private Font boldItalic;
	private Font condensed;
	
	public FontFamily(Font regular, Font regularItalic, Font bold, Font boldItalic, Font medium, Font mediumItalic, Font light, Font lightItalic, Font condensed) {
		this.regular = regular;
		this.regularItalic = regularItalic;
		this.medium = medium;
		this.mediumItalic = mediumItalic;
		this.light = light;
		this.lightItalic = lightItalic;
		this.condensed = condensed;
		this.bold = bold;
		this.boldItalic = boldItalic;
	}
	
	public FontFamily(Font regular, Font regularItalic, Font bold, Font boldItalic, Font medium, Font mediumItalic, Font light, Font lightItalic) {
		this(regular, regularItalic, bold, boldItalic, medium, mediumItalic, light, lightItalic, null);
	}
	
	public FontFamily(Font regular, Font regularItalic, Font bold, Font boldItalic, Font medium, Font mediumItalic) {
		this(regular, regularItalic, bold, boldItalic, medium, mediumItalic, null, null);
	}
	
	public FontFamily(Font regular, Font regularItalic, Font bold, Font boldItalic) {
		this(regular, regularItalic, bold, boldItalic, null, null);
	}
	
	public FontFamily(Font regular, Font regularItalic) {
		this(regular, regularItalic, null, null);
	}
	
	public FontFamily(Font regular) {
		this(regular, null);
	}
	
	public FontFamily() {
		this(null);
	}
	
	// Getters
	public Font regular() {
		return this.regular;
	}
	
	public Font regularItalic() {
		return this.regularItalic;
	}
	
	public Font light() {
		return this.light;
	}
	
	public Font lightItalic() {
		return this.lightItalic;
	}
	
	public Font bold() {
		return this.bold;
	}
	
	public Font boldItalic() {
		return this.boldItalic;
	}
	
	public Font medium() {
		return this.medium;
	}
	
	public Font mediumItalic() {
		return this.mediumItalic;
	}
	
	public Font condensed() {
		return this.condensed;
	}
	
	public Font get(FontStyle style) {
		if(style == null) return null;
		
		switch(style) {
			case LIGHT:
				return this.light();
			case LIGHT_ITALIC:
				return this.lightItalic();
			case REGULAR:
				return this.regular();
			case REGULAR_ITALIC:
				return this.regularItalic();
			case MEDIUM:
				return this.medium();
			case MEDIUM_ITALIC:
				return this.mediumItalic();
			case BOLD:
				return this.bold();
			case BOLD_ITALIC:
				return this.boldItalic();
			case CONDENSED:
				return this.condensed();
		}
		
		return null;
	}
	
	// Setters
	public void setRegular(Font regular) {
		this.regular = regular;
	}
	
	public void setRegularItalic(Font regularItalic) {
		this.regularItalic = regularItalic;
	}
	
	public void setMediumItalic(Font mediumMediumItalic) {
		this.mediumItalic = mediumMediumItalic;
	}
	
	public void setLight(Font light) {
		this.light = light;
	}
	
	public void setLightItalic(Font lightItalic) {
		this.lightItalic = lightItalic;
	}
	
	public void setCondensed(Font condensed) {
		this.condensed = condensed;
	}
	
	public void setBold(Font bold) {
		this.bold = bold;
	}
	
	public void setBoldItalic(Font boldItalic) {
		this.boldItalic = boldItalic;
	}
	
	public void setMedium(Font medium) {
		this.medium = medium;
	}
	
}
