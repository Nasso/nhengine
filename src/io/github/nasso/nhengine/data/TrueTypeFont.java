package io.github.nasso.nhengine.data;

import static org.lwjgl.stb.STBRectPack.*;
import static org.lwjgl.stb.STBTruetype.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBRPContext;
import org.lwjgl.stb.STBRPNode;
import org.lwjgl.stb.STBRPRect;
import org.lwjgl.stb.STBTTFontinfo;

import io.github.nasso.nhengine.core.Nhengine;
import io.github.nasso.nhengine.graphics.Font;
import io.github.nasso.nhengine.graphics.Texture2D;
import io.github.nasso.nhengine.utils.Nhutils;

public class TrueTypeFont extends Font {
	private boolean smooth = false;
	
	private static final int START_GLYPH_COUNT = 256;
	private static final int GLYPH_GROW_STEP = 16;
	private static final short START_TEXTURE_SCALE = 8; // 256*256
	private static final short MAX_TEXTURE_SCALE = 12; // 4096*4096
	
	private float scale, size, ascent, descent, lineGap;
	private short textureScale = START_TEXTURE_SCALE;
	
	private ByteBuffer fontData;
	
	private Texture2D tex = null;
	private ByteBuffer texData;
	
	private STBTTFontinfo fontInfo;
	private STBRPContext rpctx;
	private STBRPNode.Buffer nodes;
	
	private int glyphCount = 0;
	private STBRPRect.Buffer glyphRects;
	
	private TTFontPackedGlyph[] packedGlyphs = new TTFontPackedGlyph[START_GLYPH_COUNT];
	
	private IntBuffer _ibuf0, _ibuf1, _ibuf2, _ibuf3;
	
	public TrueTypeFont(String path, float size) throws IOException {
		this(path, size, false);
	}
	
	public TrueTypeFont(String path, float size, boolean smooth) throws IOException {
		this(path, size, smooth, false);
	}
	
	public TrueTypeFont(String path, float size, boolean smooth, boolean inJar) throws IOException {
		this.smooth = smooth;
		
		byte[] fontDataArray = Nhutils.readFileBytes(path, inJar);
		
		this.fontData = BufferUtils.createByteBuffer(fontDataArray.length);
		this.fontData.put(fontDataArray);
		this.fontData.flip();
		fontDataArray = null;
		
		int[] intArray1 = new int[1];
		int[] intArray2 = new int[1];
		int[] intArray3 = new int[1];
		
		this.fontInfo = STBTTFontinfo.create();
		stbtt_InitFont(this.fontInfo, this.fontData);
		stbtt_GetFontVMetrics(this.fontInfo, intArray1, intArray2, intArray3);
		this.scale = stbtt_ScaleForPixelHeight(this.fontInfo, size);
		
		this.size = size;
		this.ascent = intArray1[0] * this.scale;
		this.descent = intArray2[0] * this.scale;
		this.lineGap = intArray3[0] * this.scale;
		
		int textureSize = this.computeTextureSize();
		this.texData = BufferUtils.createByteBuffer(textureSize * textureSize);
		this.tex = new Texture2D(textureSize, textureSize, this.smooth ? Nhengine.LINEAR : Nhengine.NEAREST, Nhengine.LINEAR_MIPMAP_LINEAR, Nhengine.CLAMP_TO_EDGE, Nhengine.CLAMP_TO_EDGE, Nhengine.RED, Nhengine.RED, Nhengine.UNSIGNED_BYTE, this.texData);
		
		this.rpctx = STBRPContext.create();
		this.nodes = STBRPNode.create(textureSize);
		stbrp_init_target(this.rpctx, textureSize, textureSize, this.nodes);
		
		this.glyphRects = STBRPRect.create(START_GLYPH_COUNT);
		
		this._ibuf0 = BufferUtils.createIntBuffer(1);
		this._ibuf1 = BufferUtils.createIntBuffer(1);
		this._ibuf2 = BufferUtils.createIntBuffer(1);
		this._ibuf3 = BufferUtils.createIntBuffer(1);
	}
	
	private int computeTextureSize() {
		return this.powerOf2(this.textureScale);
	}
	
	private int powerOf2(short n) {
		int p = 1;
		for(short i = 0; i < n; i++)
			p *= 2;
		return p;
	}
	
	private void redrawTexture() {
		int textureSize = this.tex.getWidth();
		
		int bmpi = 0;
		int texi = 0;
		
		int gw = 0;
		int gh = 0;
		int offx = this.smooth ? 1 : 0;
		int offy = this.smooth ? 1 : 0;
		
		TTFontPackedGlyph glyph;
		BufferUtils.zeroBuffer(this.texData);
		for(int i = 0; i < this.glyphCount; i++) {
			glyph = this.packedGlyphs[i];
			if(!glyph.packed) continue;
			
			if(this.smooth) {
				gw = glyph.w() - 2;
				gh = glyph.h() - 2;
			} else {
				gw = glyph.w();
				gh = glyph.h();
			}
			
			for(int x = 0; x < gw; x++) {
				for(int y = 0; y < gh; y++) {
					bmpi = y * gw + x;
					texi = (y + glyph.y0() + offy) * textureSize + (x + glyph.x0() + offx);
					
					this.texData.put(texi, glyph.bitmap.get(bmpi));
				}
			}
		}
		
		this.tex.needUpdate();
	}
	
	private void repack() {
		boolean success = stbrp_pack_rects(this.rpctx, this.glyphRects) == 1;
		
		// Grow if needed
		if(!success && this.growTexture()) stbrp_pack_rects(this.rpctx, this.glyphRects);
		
		TTFontPackedGlyph glyph;
		STBRPRect rect;
		for(int i = 0; i < this.glyphCount; i++) {
			glyph = this.packedGlyphs[i];
			rect = this.glyphRects.get(i);
			
			glyph.x0 = rect.x();
			glyph.y0 = rect.y();
			glyph.x1 = glyph.x0 + rect.w();
			glyph.y1 = glyph.y0 + rect.h();
			
			glyph.packed = rect.was_packed() != 0;
			
			if(!glyph.packed) {
				System.out.println("Warning: codepoint " + glyph.codepoint + " couldn't be packed.");
				continue;
			}
		}
		
		this.redrawTexture();
	}
	
	// Add the char to the map
	private TTFontPackedGlyph indexGlyph(int codepoint) {
		int glyphFontIndex = stbtt_FindGlyphIndex(this.fontInfo, codepoint);
		
		for(int i = 0; i < this.glyphCount; i++) {
			if(this.packedGlyphs[i].glyphIndex == glyphFontIndex) return this.packedGlyphs[i];
		}
		
		// Increment the glyph counter
		this.glyphCount++;
		
		// Grow the glyph buffers if we reached the limit
		if(this.glyphCount == this.packedGlyphs.length) {
			int newSize = this.packedGlyphs.length + GLYPH_GROW_STEP;
			
			this.packedGlyphs = Arrays.copyOf(this.packedGlyphs, newSize);
			
			this.glyphRects = STBRPRect.create(newSize);
		}
		
		int glyphBufferIndex = this.glyphCount - 1;
		
		TTFontPackedGlyph glyph = new TTFontPackedGlyph();
		glyph.codepoint = codepoint;
		glyph.glyphIndex = glyphFontIndex;
		
		// Get glyph metrics
		stbtt_GetGlyphBitmapBox(this.fontInfo, glyph.glyphIndex, this.scale, this.scale, this._ibuf0, this._ibuf1, this._ibuf2, this._ibuf3);
		int ix0 = this._ibuf0.get(0);
		int iy0 = this._ibuf1.get(0);
		int ix1 = this._ibuf2.get(0);
		int iy1 = this._ibuf3.get(0);
		
		stbtt_GetGlyphHMetrics(this.fontInfo, glyph.glyphIndex, this._ibuf0, this._ibuf1);
		int advanceWidth = this._ibuf0.get(0);
		
		// Setup packed glyph
		glyph.xadvance = advanceWidth * this.scale;
		glyph.xoff = ix0;
		glyph.yoff = iy0;
		
		// With LINEAR mode activated, we're adding a 1 pixel gap everywhere...
		if(this.smooth) {
			glyph.xoff--;
			glyph.yoff--;
		}
		
		short w = (short) (ix1 - ix0);
		short h = (short) (iy1 - iy0);
		
		// Setup rect
		STBRPRect rect = this.glyphRects.get(glyphBufferIndex);
		if(this.smooth) {
			rect.w((short) (w + 2));
			rect.h((short) (h + 2));
		} else {
			rect.w(w);
			rect.h(h);
		}
		
		// Rasterize glyph
		glyph.bitmap = BufferUtils.createByteBuffer(w * h);
		stbtt_MakeGlyphBitmap(this.fontInfo, glyph.bitmap, w, h, w, this.scale, this.scale, glyph.glyphIndex);
		
		this.packedGlyphs[glyphBufferIndex] = glyph;
		
		// Repack the glyphs
		this.repack();
		
		// Return the FontPackedGlyph
		return glyph;
	}
	
	/**
	 * Multiply the texture atlas by 2
	 * 
	 * @return true if the atlas size changed
	 */
	private boolean growTexture() {
		if(this.textureScale == MAX_TEXTURE_SCALE) return false;
		
		this.textureScale++;
		int newSize = this.computeTextureSize();
		
		this.texData = BufferUtils.createByteBuffer(newSize * newSize);
		
		this.tex.setData(this.texData);
		this.tex.setWidth(newSize);
		this.tex.setHeight(newSize);
		this.tex.needUpdate();
		
		this.nodes = STBRPNode.create(newSize);
		stbrp_init_target(this.rpctx, newSize, newSize, this.nodes);
		
		return true;
	}
	
	public FontPackedGlyph getPackedGlyph(int codepoint) {
		TTFontPackedGlyph c;
		for(int i = 0; i < this.glyphCount; i++) {
			c = this.packedGlyphs[i];
			if(c.codepoint == codepoint) return this.packedGlyphs[i];
		}
		
		return this.indexGlyph(codepoint);
	}
	
	public float getGlyphWidth(int codepoint) {
		TTFontPackedGlyph c;
		for(int i = 0; i < this.glyphCount; i++) {
			c = this.packedGlyphs[i];
			if(c.codepoint == codepoint) return this.packedGlyphs[i].xadvance;
		}
		
		stbtt_GetCodepointHMetrics(this.fontInfo, codepoint, this._ibuf0, this._ibuf1);
		return this._ibuf0.get(0) * this.scale;
	}
	
	public Texture2D getTexture() {
		return this.tex;
	}
	
	public float getAscent() {
		return this.ascent;
	}
	
	public float getDescent() {
		return this.descent;
	}
	
	public float getLineGap() {
		return this.lineGap;
	}
	
	public float getHeight() {
		return this.ascent - this.descent;
	}
	
	public float getSize() {
		return this.size;
	}
	
	public boolean isSmooth() {
		return this.smooth;
	}
	
	public void setSmooth(boolean smooth) {
		this.smooth = smooth;
	}
	
	public Font clone() {
		return null;
	}
	
	public void dispose() {
		this.triggerEvent("dispose");
		
		this.rpctx = null;
		this.nodes = null;
		this.fontInfo = null;
		
		this._ibuf0 = this._ibuf1 = this._ibuf2 = this._ibuf3 = null;
		
		this.tex.dispose();
		this.tex = null;
		this.fontInfo = null;
	}
	
	private static class TTFontPackedGlyph extends Font.FontPackedGlyph {
		ByteBuffer bitmap;
		
		boolean packed = false;
		
		int glyphIndex = -1;
		
		int codepoint;
		int x0;
		int y0;
		int x1;
		int y1;
		float xoff;
		float yoff;
		float xadvance;
		
		public int codepoint() {
			return this.codepoint;
		}
		
		public int x0() {
			return this.x0;
		}
		
		public int y0() {
			return this.y0;
		}
		
		public int x1() {
			return this.x1;
		}
		
		public int y1() {
			return this.y1;
		}
		
		public float xoff() {
			return this.xoff;
		}
		
		public float yoff() {
			return this.yoff;
		}
		
		public float xadvance() {
			return this.xadvance;
		}
	}
}
