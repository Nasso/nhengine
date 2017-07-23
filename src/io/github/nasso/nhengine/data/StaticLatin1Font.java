package io.github.nasso.nhengine.data;

import static org.lwjgl.stb.STBTruetype.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackRange;
import org.lwjgl.stb.STBTTPackedchar;

import io.github.nasso.nhengine.core.Disposable;
import io.github.nasso.nhengine.core.Nhengine;
import io.github.nasso.nhengine.graphics.Font;
import io.github.nasso.nhengine.graphics.Texture2D;
import io.github.nasso.nhengine.graphics.TextureData;
import io.github.nasso.nhengine.utils.Nhutils;

public class StaticLatin1Font extends Font implements Disposable, Cloneable {
	private static final int HDR_SIGN = 0x4E48454E; // "NHEN"
	private static final int HDR_TYPE_FONT = 0x666F6E74; // "font"
	private static final int CHUNK_TYPE_ATTR = 0x61747472; // "attr"
	private static final int CHUNK_TYPE_TEX_ = 0x74657820; // "tex "
	private static final int CHUNK_TYPE_FONT_BASL = 0x6261736C; // "basl"
	private static final int CHUNK_TYPE_FONT_LAT1 = 0x6C617431; // "lat1"
	private static final int FTR_END = 0x656E6420; // "end "
	
	public static final int UNICODE_BASIC_LATIN_FIRST = 0x0020;
	public static final int UNICODE_BASIC_LATIN_LAST = 0x007E;
	public static final int UNICODE_BASIC_LATIN_COUNT = UNICODE_BASIC_LATIN_LAST - UNICODE_BASIC_LATIN_FIRST;
	
	public static final int UNICODE_LATIN1_SUPPLEMENT_FIRST = 0x00A1;
	public static final int UNICODE_LATIN1_SUPPLEMENT_LAST = 0x00FF;
	public static final int UNICODE_LATIN1_SUPPLEMENT_COUNT = UNICODE_LATIN1_SUPPLEMENT_LAST - UNICODE_LATIN1_SUPPLEMENT_FIRST;
	
	private float size, ascent, descent, lineGap;
	private int res;
	
	private Texture2D tex = null;
	private StaticFontPackedGlyph[] basicLatin = null;
	private StaticFontPackedGlyph[] latin1 = null;
	
	private StaticLatin1Font(Texture2D tex, StaticFontPackedGlyph[] basicLatin, StaticFontPackedGlyph[] latin1, int res, float size, float ascent, float descent, float lineGap) {
		this.tex = tex;
		this.basicLatin = basicLatin;
		this.latin1 = latin1;
		this.res = res;
		this.size = size;
		this.ascent = ascent;
		this.descent = descent;
		this.lineGap = lineGap;
	}
	
	public Texture2D getTexture() {
		return this.tex;
	}
	
	public StaticFontPackedGlyph[] getBasicLatinTable() {
		return this.basicLatin;
	}
	
	public StaticFontPackedGlyph[] getLatin1Table() {
		return this.latin1;
	}
	
	public FontPackedGlyph getPackedGlyph(int c) {
		if(this.tex == null) return c == '?' ? null : this.getPackedGlyph('?');
		
		if(this.basicLatin != null && c >= UNICODE_BASIC_LATIN_FIRST && c <= UNICODE_BASIC_LATIN_LAST) return this.basicLatin[c - UNICODE_BASIC_LATIN_FIRST];
		if(this.latin1 != null && c >= UNICODE_LATIN1_SUPPLEMENT_FIRST && c <= UNICODE_LATIN1_SUPPLEMENT_LAST) return this.latin1[c - UNICODE_LATIN1_SUPPLEMENT_FIRST];
		
		return c == '?' ? null : this.getPackedGlyph('?');
	}
	
	public float getGlyphWidth(int codepoint) {
		return this.getPackedGlyph(codepoint).xadvance();
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
	
	public int getResolution() {
		return this.res;
	}
	
	public StaticLatin1Font clone() {
		return new StaticLatin1Font(this.tex.clone(), this.basicLatin, this.latin1, this.res, this.size, this.ascent, this.descent, this.lineGap);
	}
	
	public void dispose() {
		this.triggerEvent("dispose");
		
		this.tex.dispose();
		this.tex = null;
		this.basicLatin = null;
		this.latin1 = null;
	}
	
	private static class StaticFontPackedGlyph extends Font.FontPackedGlyph {
		private static final int CLASS_SIZE = 28;
		
		int codepoint;
		int x0;
		int y0;
		int x1;
		int y1;
		float xoff;
		float yoff;
		float xadvance;
		
		public StaticFontPackedGlyph(STBTTPackedchar pc, int codepoint) {
			this.codepoint = codepoint;
			this.x0 = pc.x0();
			this.y0 = pc.y0();
			this.x1 = pc.x1();
			this.y1 = pc.y1();
			this.xoff = pc.xoff();
			this.yoff = pc.yoff();
			this.xadvance = pc.xadvance();
		}
		
		public StaticFontPackedGlyph(int x0, int y0, int x1, int y1, float xoff, float yoff, float xadvance, int codepoint) {
			this.codepoint = codepoint;
			this.x0 = x0;
			this.y0 = y0;
			this.x1 = x1;
			this.y1 = y1;
			this.xoff = xoff;
			this.yoff = yoff;
			this.xadvance = xadvance;
		}
		
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
	
	private static int readInt(InputStream in) throws IOException {
		return ((in.read() & 0xFF) << 24) | ((in.read() & 0xFF) << 16) | ((in.read() & 0xFF) << 8) | (in.read() & 0xFF);
	}
	
	private static StaticFontPackedGlyph[] readFontPackedCharChunk(InputStream in, int chunkSize, int tableOffset) throws IOException {
		int charCount = chunkSize / StaticFontPackedGlyph.CLASS_SIZE;
		
		StaticFontPackedGlyph[] chars = new StaticFontPackedGlyph[charCount];
		
		int x0, y0, x1, y1;
		float xoff, yoff, xadvance;
		
		for(int i = 0; i < charCount; i++) {
			x0 = readInt(in);
			y0 = readInt(in);
			x1 = readInt(in);
			y1 = readInt(in);
			xoff = Float.intBitsToFloat(readInt(in));
			yoff = Float.intBitsToFloat(readInt(in));
			xadvance = Float.intBitsToFloat(readInt(in));
			
			chars[i] = new StaticFontPackedGlyph(x0, y0, x1, y1, xoff, yoff, xadvance, tableOffset + i);
		}
		
		return chars;
	}
	
	public static StaticLatin1Font loadStaticFont(String fileName, boolean inJar) throws IOException {
		BufferedInputStream in;
		
		if(inJar) {
			InputStream ressource = StaticLatin1Font.class.getClassLoader().getResourceAsStream(fileName);
			if(ressource == null) {
				System.err.println("Can't find ressource: " + fileName);
				
				return null;
			}
			
			in = new BufferedInputStream(ressource);
		} else in = new BufferedInputStream(new FileInputStream(fileName));
		if(readInt(in) != HDR_SIGN) throw new IOException("Not a Nhengine asset");
		if(readInt(in) != HDR_TYPE_FONT) throw new IOException("Not a Nhengine font asset");
		
		float size = 0, ascent = 0, descent = 0, lineGap = 0;
		int res = 0;
		
		Texture2D tex = null;
		StaticFontPackedGlyph[] basicLatin = null;
		StaticFontPackedGlyph[] latin1 = null;
		
		while(true) {
			// read chunk
			int chunkID = readInt(in);
			if(chunkID == FTR_END) break;
			int chunkSize = readInt(in);
			
			switch(chunkID) {
				case CHUNK_TYPE_ATTR:
					if(chunkSize != 20) throw new IOException("Expected a 20 bytes long attribute chunk, got " + chunkSize);
					size = Float.intBitsToFloat(readInt(in));
					ascent = Float.intBitsToFloat(readInt(in));
					descent = Float.intBitsToFloat(readInt(in));
					lineGap = Float.intBitsToFloat(readInt(in));
					res = readInt(in);
					break;
				case CHUNK_TYPE_TEX_:
					tex = TextureIO.loadTexture2D(in, chunkSize, 1, false, true, false);
					tex.setWrapS(Nhengine.CLAMP_TO_EDGE);
					tex.setWrapT(Nhengine.CLAMP_TO_EDGE);
					break;
				case CHUNK_TYPE_FONT_BASL:
					basicLatin = readFontPackedCharChunk(in, chunkSize, StaticLatin1Font.UNICODE_BASIC_LATIN_FIRST);
					break;
				case CHUNK_TYPE_FONT_LAT1:
					latin1 = readFontPackedCharChunk(in, chunkSize, StaticLatin1Font.UNICODE_LATIN1_SUPPLEMENT_FIRST);
					break;
			}
		}
		
		in.close();
		
		return new StaticLatin1Font(tex, basicLatin, latin1, res, size, ascent, descent, lineGap);
	}
	
	public static StaticLatin1Font loadStaticFont(String fileName) throws IOException {
		return loadStaticFont(fileName, false);
	}
	
	public static StaticLatin1Font loadStaticTTF(String ttfPath, int size, int res) throws IOException {
		return loadStaticTTF(ttfPath, size, res, false);
	}
	
	public static StaticLatin1Font loadStaticTTF(String ttfPath, int size, int res, boolean inJar) throws IOException {
		byte[] fontDataArray = Nhutils.readFileBytes(ttfPath, inJar);
		
		ByteBuffer fontData = BufferUtils.createByteBuffer(fontDataArray.length);
		fontData.put(fontDataArray);
		fontData.flip();
		fontDataArray = null;
		
		int[] intArray1 = new int[1];
		int[] intArray2 = new int[1];
		int[] intArray3 = new int[1];
		
		STBTTFontinfo fontInfo = STBTTFontinfo.create();
		stbtt_InitFont(fontInfo, fontData);
		stbtt_GetFontVMetrics(fontInfo, intArray1, intArray2, intArray3);
		float factor = stbtt_ScaleForPixelHeight(fontInfo, size);
		
		float ascent = intArray1[0] * factor;
		float descent = intArray2[0] * factor;
		float lineGap = intArray3[0] * factor;
		
		STBTTPackedchar.Buffer basic_latin_packedChars_buf = STBTTPackedchar.malloc(UNICODE_BASIC_LATIN_COUNT);
		STBTTPackedchar.Buffer latin1_supplement_chars_buf = STBTTPackedchar.malloc(UNICODE_LATIN1_SUPPLEMENT_COUNT);
		
		STBTTPackRange.Buffer ranges = STBTTPackRange.create(2);
		
		STBTTPackRange range1 = ranges.get(0);
		range1.font_size(size);
		range1.first_unicode_codepoint_in_range(UNICODE_LATIN1_SUPPLEMENT_FIRST);
		range1.num_chars(UNICODE_LATIN1_SUPPLEMENT_COUNT);
		range1.chardata_for_range(latin1_supplement_chars_buf);
		
		STBTTPackRange range2 = ranges.get(1);
		range2.font_size(size);
		range2.first_unicode_codepoint_in_range(UNICODE_BASIC_LATIN_FIRST);
		range2.num_chars(UNICODE_BASIC_LATIN_COUNT);
		range2.chardata_for_range(basic_latin_packedChars_buf);
		
		ByteBuffer bitmapData = BufferUtils.createByteBuffer(res * res);
		STBTTPackContext ctx = STBTTPackContext.create();
		stbtt_PackBegin(ctx, bitmapData, res, res, 0, 1);
		stbtt_PackFontRanges(ctx, fontData, 0, ranges);
		stbtt_PackEnd(ctx);
		
		StaticFontPackedGlyph[] basicLatin = new StaticFontPackedGlyph[UNICODE_BASIC_LATIN_COUNT];
		StaticFontPackedGlyph[] latin1 = new StaticFontPackedGlyph[UNICODE_LATIN1_SUPPLEMENT_COUNT];
		
		for(int i = 0; i < UNICODE_BASIC_LATIN_COUNT; i++) {
			basicLatin[i] = new StaticFontPackedGlyph(basic_latin_packedChars_buf.get(i), ((char) (UNICODE_BASIC_LATIN_FIRST + i)));
		}
		
		for(int i = 0; i < UNICODE_LATIN1_SUPPLEMENT_COUNT; i++) {
			latin1[i] = new StaticFontPackedGlyph(latin1_supplement_chars_buf.get(i), ((char) (UNICODE_LATIN1_SUPPLEMENT_FIRST + i)));
		}
		
		basic_latin_packedChars_buf.free();
		latin1_supplement_chars_buf.free();
		
		Texture2D bitmap = new Texture2D(res, res, Nhengine.NEAREST, Nhengine.LINEAR_MIPMAP_LINEAR, Nhengine.CLAMP_TO_EDGE, Nhengine.CLAMP_TO_EDGE, Nhengine.RED, Nhengine.RED, Nhengine.UNSIGNED_BYTE, bitmapData);
		
		return new StaticLatin1Font(bitmap, basicLatin, latin1, res, size, ascent, descent, lineGap);
	}
	
	private static void writeInt(OutputStream out, int a) throws IOException {
		out.write((a >> 24) & 0xFF);
		out.write((a >> 16) & 0xFF);
		out.write((a >> 8) & 0xFF);
		out.write(a & 0xFF);
	}
	
	private static void writeFontPackedCharsChunk(OutputStream out, FontPackedGlyph[] chars) throws IOException {
		writeInt(out, chars.length * StaticFontPackedGlyph.CLASS_SIZE); // Chunk size
		for(int i = 0; i < chars.length; i++) {
			writeInt(out, chars[i].x0());
			writeInt(out, chars[i].y0());
			writeInt(out, chars[i].x1());
			writeInt(out, chars[i].y1());
			writeInt(out, Float.floatToIntBits(chars[i].xoff()));
			writeInt(out, Float.floatToIntBits(chars[i].yoff()));
			writeInt(out, Float.floatToIntBits(chars[i].xadvance()));
		}
	}
	
	public static void writeFont(StaticLatin1Font f, String destFile) throws IOException {
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(destFile));
		
		// HEADER
		writeInt(out, HDR_SIGN);
		writeInt(out, HDR_TYPE_FONT);
		
		// CHUNK: attr (attributes)
		writeInt(out, CHUNK_TYPE_ATTR); // Chunk ID
		writeInt(out, 20); // Chunk size
		writeInt(out, Float.floatToIntBits(f.getSize())); // 4: float: size
		writeInt(out, Float.floatToIntBits(f.getAscent())); // 4: float: ascent
		writeInt(out, Float.floatToIntBits(f.getDescent())); // 4: float: descent
		writeInt(out, Float.floatToIntBits(f.getLineGap())); // 4: float: lineGap
		writeInt(out, f.getResolution()); // 4: int: res
		
		// CHUNK: text (texture)
		writeInt(out, CHUNK_TYPE_TEX_); // Chunk ID
		
		// write texture as PNG in a byte buffer
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		TextureData texData = new TextureData();
		texData.setBytesPerPixel(1);
		texData.setWidth(f.getTexture().getWidth());
		texData.setHeight(f.getTexture().getHeight());
		texData.setData(f.getTexture().getData());
		TextureIO.writeTexture(baos, "png", texData);
		
		writeInt(out, baos.size()); // Chunk size
		out.write(baos.toByteArray()); // Chunk data (png)
		baos = null; // "free"
		
		// CHUNK: basl
		writeInt(out, CHUNK_TYPE_FONT_BASL); // Chunk ID
		writeFontPackedCharsChunk(out, f.getBasicLatinTable());
		
		// CHUNK: lat1
		writeInt(out, CHUNK_TYPE_FONT_LAT1); // Chunk ID
		writeFontPackedCharsChunk(out, f.getLatin1Table());
		
		writeInt(out, FTR_END);
		
		out.close();
	}
	
}
