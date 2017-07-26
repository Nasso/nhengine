package io.github.nasso.nhengine.graphics.opengl.nanovg;

import org.lwjgl.nanovg.NVGColor;

import io.github.nasso.nhengine.graphics.Color;

public class NVGColors {
	public static final NVGColors INSTANCE = new NVGColors();
	
	private NVGColor aColor;
	
	public NVGColor get(float r, float g, float b, float a) {
		if(this.aColor == null) this.aColor = NVGContext.rgbaf(r, g, b, a);
		else {
			this.aColor.r(r);
			this.aColor.g(g);
			this.aColor.b(b);
			this.aColor.a(a);
		}
		
		return this.aColor;
	}
	
	public NVGColor get(Color c) {
		return this.get(c.red(), c.green(), c.blue(), c.alpha());
	}
}
