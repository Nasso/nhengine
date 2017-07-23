package io.github.nasso.nhengine.opengl;

import org.joml.Matrix3f;
import org.joml.Vector2f;

public class OGLClipping {
	float x, y, w, h;
	
	float[] settime_xform = new float[6];
	
	// Just like NVG!
	Matrix3f xform = new Matrix3f();
	Vector2f extent = new Vector2f();
	
	boolean isNull = true;
	
	public OGLClipping(OGLClipping clip) {
		this.set(clip);
	}
	
	public OGLClipping() {
		this.x = this.y = -Float.MAX_VALUE * 0.5f;
		this.w = this.h = Float.MAX_VALUE;
	}
	
	public void set(OGLClipping clip) {
		this.isNull = clip.isNull;
		if(this.isNull) return;
		
		this.xform.set(clip.xform);
		this.extent.set(clip.extent);
		
		this.x = clip.x;
		this.y = clip.y;
		this.w = clip.w;
		this.h = clip.h;
		
		for(int i = 0; i < this.settime_xform.length; i++)
			this.settime_xform[i] = clip.settime_xform[i];
	}
	
	public boolean isEmpty() {
		return this.w <= 0 || this.h <= 0;
	}
	
}
