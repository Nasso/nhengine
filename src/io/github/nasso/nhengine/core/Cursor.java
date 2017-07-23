package io.github.nasso.nhengine.core;

import static io.github.nasso.nhengine.core.Nhengine.*;
import static org.lwjgl.glfw.GLFW.*;

import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFWImage;

import io.github.nasso.nhengine.utils.Nhutils;

public class Cursor {
	private static Cursor arrow, ibeam, crosshair, hand, hresize, vrisize;
	
	long id;
	
	private boolean undisposable = false;
	
	private Cursor(int standardCursor, boolean undisposable) {
		if(!Nhutils.check4int(standardCursor, ARROW_CURSOR, IBEAM_CURSOR, CROSSHAIR_CURSOR, HAND_CURSOR, HRESIZE_CURSOR, VRESIZE_CURSOR)) return;
		
		this.id = glfwCreateStandardCursor(standardCursor);
		this.undisposable = undisposable;
	}
	
	public Cursor(int standardCursor) {
		this(standardCursor, false);
	}
	
	public Cursor(ByteBuffer pixels, int w, int h, int xhot, int yhot) {
		GLFWImage img = GLFWImage.create();
		img.width(w);
		img.height(h);
		img.pixels(pixels);
		
		this.id = glfwCreateCursor(img, xhot, yhot);
		this.undisposable = false;
	}
	
	public void dispose() {
		if(this.undisposable) return;
		
		glfwDestroyCursor(this.id);
	}
	
	public static Cursor getArrowCursor() {
		return arrow == null ? (arrow = new Cursor(ARROW_CURSOR, true)) : arrow;
	}
	
	public static Cursor getIBeamCursor() {
		return ibeam == null ? (ibeam = new Cursor(IBEAM_CURSOR, true)) : ibeam;
	}
	
	public static Cursor getCrosshairCursor() {
		return crosshair == null ? (crosshair = new Cursor(CROSSHAIR_CURSOR, true)) : crosshair;
	}
	
	public static Cursor getHandCursor() {
		return hand == null ? (hand = new Cursor(HAND_CURSOR, true)) : hand;
	}
	
	public static Cursor getHResizeCursor() {
		return hresize == null ? (hresize = new Cursor(HRESIZE_CURSOR, true)) : hresize;
	}
	
	public static Cursor getVResizeCursor() {
		return vrisize == null ? (vrisize = new Cursor(VRESIZE_CURSOR, true)) : vrisize;
	}
}
