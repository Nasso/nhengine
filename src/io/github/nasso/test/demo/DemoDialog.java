package io.github.nasso.test.demo;

import io.github.nasso.nhengine.audio.Sound;
import io.github.nasso.nhengine.graphics.Font;
import io.github.nasso.nhengine.graphics.Texture2D;

public class DemoDialog {
	public static final int DEFAULT_DIALOG_SPEED = 0;
	public static final String MACRO_PAUSE = "\0";
	public static final String MACRO_PAUSE_2 = MACRO_PAUSE + MACRO_PAUSE;
	public static final String MACRO_PAUSE_4 = MACRO_PAUSE_2 + MACRO_PAUSE_2;
	public static final String MACRO_PAUSE_8 = MACRO_PAUSE_4 + MACRO_PAUSE_4;
	public static final String MACRO_PAUSE_16 = MACRO_PAUSE_8 + MACRO_PAUSE_8;
	
	public static final char MACRO_VALUE_DEFAULT = 'a';
	public static final char MACRO_VALUE_RED = 'b';
	public static final char MACRO_VALUE_BLUE = 'c';
	public static final char MACRO_VALUE_GREEN = 'd';
	public static final char MACRO_VALUE_PURPLE = 'e';
	public static final char MACRO_VALUE_YELLOW = 'f';
	
	public static final String MACRO_COLOR_DEFAULT = "\\" + MACRO_VALUE_DEFAULT;
	public static final String MACRO_COLOR_RED = "\\" + MACRO_VALUE_RED;
	public static final String MACRO_COLOR_BLUE = "\\" + MACRO_VALUE_BLUE;
	public static final String MACRO_COLOR_GREEN = "\\" + MACRO_VALUE_GREEN;
	public static final String MACRO_COLOR_PURPLE = "\\" + MACRO_VALUE_PURPLE;
	public static final String MACRO_COLOR_YELLOW = "\\" + MACRO_VALUE_YELLOW;
	
	public static final char MACRO_LINE_SEPARATOR = '&';
	public static final char MACRO_HEART_POINT = '#';
	
	// public static final TextComponent.TextEffect TEXT_EFFECT_SHAKING = new TextComponent.ShakingTextEffect(0.7f, 1f);
	// public static final TextComponent.TextEffect TEXT_EFFECT_TENSION = new TextComponent.ShakingTextEffect(1.2f, 0.02f);
	public static final int TEXT_EFFECT_NONE = 0;
	public static final int TEXT_EFFECT_SHAKING = 1;
	public static final int TEXT_EFFECT_TENSION = 2;
	
	public static class DialogPage {
		private String text;
		
		private Sound voice;
		private Texture2D face;
		
		private Font font;
		private int effect = TEXT_EFFECT_NONE;
		
		private int speed = DEFAULT_DIALOG_SPEED;
		private boolean skippable = true;
		
		private DialogPage next;
		
		public DialogPage(Texture2D face, String text, Sound voice, Font font, int speed, int effect, boolean skippable) {
			this.face = face;
			this.text = text;
			this.voice = voice;
			this.font = font;
			this.speed = speed;
			this.effect = effect;
			this.skippable = skippable;
		}
		
		public Texture2D getFace() {
			return this.face;
		}
		
		public void setFace(Texture2D face) {
			this.face = face;
		}
		
		public Font getFont() {
			return this.font;
		}
		
		public void setFont(Font font) {
			this.font = font;
		}
		
		public int getSpeed() {
			return this.speed;
		}
		
		public void setSpeed(int speed) {
			this.speed = speed;
		}
		
		public boolean isSkippable() {
			return this.skippable;
		}
		
		public void setSkippable(boolean skippable) {
			this.skippable = skippable;
		}
		
		public Sound getVoice() {
			return this.voice;
		}
		
		public void setVoice(Sound voice) {
			this.voice = voice;
		}
		
		public int getEffect() {
			return this.effect;
		}
		
		public void setEffect(int effect) {
			this.effect = effect;
		}
		
		public String getText() {
			return this.text;
		}
		
		public void setText(String text) {
			this.text = text;
		}
		
		public DialogPage getNext() {
			return this.next;
		}
		
		public void setNext(DialogPage next) {
			this.next = next;
		}
	}
	
	private DialogPage page = null;
	
	public DemoDialog() {
	}
	
	public DialogPage getPage() {
		return this.page;
	}
	
	public void setPage(DialogPage page) {
		this.page = page;
	}
}
