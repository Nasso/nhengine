package io.github.nasso.nhengine.core;

import static io.github.nasso.nhengine.core.Nhengine.*;
import static org.lwjgl.glfw.GLFW.*;

import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFWImage;

import io.github.nasso.nhengine.utils.Nhutils;

/**
 * Represents a custom mouse cursor.
 * 
 * @author nasso
 */
public class Cursor {
	private static Cursor arrow, ibeam, crosshair, hand, hresize, vrisize;
	
	long id;
	
	private boolean undisposable = false;
	
	private Cursor(int standardCursor, boolean undisposable) {
		if(!Nhutils.check4int(standardCursor, ARROW_CURSOR, IBEAM_CURSOR, CROSSHAIR_CURSOR, HAND_CURSOR, HRESIZE_CURSOR, VRESIZE_CURSOR)) return;
		
		this.id = glfwCreateStandardCursor(standardCursor);
		this.undisposable = undisposable;
	}
	
	/**
	 * Creates a custom cursor from the raw image data.
	 * The pixels are 32-bit, little-endian, non-premultiplied RGBA, i.e. eight bits per channel.
	 * They are arranged canonically as packed sequential rows, starting from the top-left corner.
	 * 
	 * @param pixels The pixel data
	 * @param w The width in pixel
	 * @param h The height in pixel
	 * @param xhot The desired x-coordinate, in pixels, of the cursor hotspot
	 * @param yhot The desired y-coordinate, in pixels, of the cursor hotspot
	 */
	public Cursor(ByteBuffer pixels, int w, int h, int xhot, int yhot) {
		GLFWImage img = GLFWImage.create();
		img.width(w);
		img.height(h);
		img.pixels(pixels);
		
		this.id = glfwCreateCursor(img, xhot, yhot);
		this.undisposable = false;
	}
	
	/**
	 * Disposes this cursor, if it's a custom one (standard cursors can't be disposed, and calling this method will do nothing).
	 */
	public void dispose() {
		if(this.undisposable) return;
		
		glfwDestroyCursor(this.id);
	}
	
	/**
	 * @return The "arrow" standard cursor.
	 */
	public static Cursor getArrowCursor() {
		return arrow == null ? (arrow = new Cursor(ARROW_CURSOR, true)) : arrow;
	}

	/**
	 * @return The "I-beam" standard cursor.
	 */
	public static Cursor getIBeamCursor() {
		return ibeam == null ? (ibeam = new Cursor(IBEAM_CURSOR, true)) : ibeam;
	}

	/**
	 * @return The "crosshair" standard cursor.
	 */
	public static Cursor getCrosshairCursor() {
		return crosshair == null ? (crosshair = new Cursor(CROSSHAIR_CURSOR, true)) : crosshair;
	}

	/**
	 * @return The "hand" standard cursor.
	 */
	public static Cursor getHandCursor() {
		return hand == null ? (hand = new Cursor(HAND_CURSOR, true)) : hand;
	}

	/**
	 * @return The "horizontal-resize" standard cursor.
	 */
	public static Cursor getHResizeCursor() {
		return hresize == null ? (hresize = new Cursor(HRESIZE_CURSOR, true)) : hresize;
	}

	/**
	 * @return The "vertical-resize" standard cursor.
	 */
	public static Cursor getVResizeCursor() {
		return vrisize == null ? (vrisize = new Cursor(VRESIZE_CURSOR, true)) : vrisize;
	}
}
