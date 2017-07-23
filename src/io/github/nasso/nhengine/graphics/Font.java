package io.github.nasso.nhengine.graphics;

import io.github.nasso.nhengine.core.Disposable;
import io.github.nasso.nhengine.event.Observable;

public abstract class Font extends Observable implements Disposable, Cloneable {
	public abstract Texture2D getTexture();
	
	public abstract FontPackedGlyph getPackedGlyph(int codepoint);
	
	public abstract float getGlyphWidth(int codepoint);
	
	public abstract float getAscent();
	
	public abstract float getDescent();
	
	public abstract float getLineGap();
	
	public abstract float getHeight();
	
	public abstract float getSize();
	
	public abstract Font clone();
	
	public abstract void dispose();
	
	public static abstract class FontPackedGlyph {
		public abstract int codepoint();
		
		public abstract int x0();
		
		public abstract int y0();
		
		public abstract int x1();
		
		public abstract int y1();
		
		public abstract float xoff();
		
		public abstract float yoff();
		
		public abstract float xadvance();
		
		public int w() {
			return this.x1() - this.x0();
		}
		
		public int h() {
			return this.y1() - this.y0();
		}
	}
}
