package io.github.nasso.nhengine.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import io.github.nasso.nhengine.graphics.Color;
import io.github.nasso.nhengine.graphics.Font;
import io.github.nasso.nhengine.graphics.Font.FontPackedGlyph;

public class Nhutils {
	private static Vector4f _vec4 = new Vector4f();
	
	private Nhutils() {
	}
	
	public static boolean isNewLine(int codepoint) {
		return isNewLineIgnoreR(codepoint) || codepoint == '\r';
	}
	
	public static boolean isNewLineIgnoreR(int codepoint) {
		return codepoint == '\n';
	}
	
	public static int getCodepoint(CharSequence seq, int i) {
		char a = seq.charAt(i);
		
		return Character.isHighSurrogate(a) && i + 1 < seq.length() ? Character.toCodePoint(a, seq.charAt(i + 1)) : a;
	}
	
	public static int charIndexAt(Font fnt, CharSequence txt, float offsetX) {
		if(fnt == null || txt == null) return -1;
		
		float width = 0;
		
		int i = 0;
		for(; i < txt.length(); i++) {
			int c = getCodepoint(txt, i);
			
			if(Character.isLowSurrogate(txt.charAt(i)) || c == '\0' || isNewLine(c)) continue;
			
			FontPackedGlyph bc = fnt.getPackedGlyph(c);
			if(bc == null) continue;
			
			width += bc.xadvance() * 0.5f;
			
			if(width >= offsetX) return i;
			
			width += bc.xadvance() * 0.5f;
		}
		
		return i;
	}
	
	public static int indexOf(CharSequence txt, int codepoint, int start) {
		if(txt == null) return -1;
		
		for(int i = start; i < txt.length(); i++) {
			if(getCodepoint(txt, i) == codepoint) return i;
		}
		
		return -1;
	}
	
	public static void measureText(Font fnt, CharSequence txt, Vector2f dest) {
		if(fnt == null || txt == null) {
			dest.zero();
			return;
		}
		
		float width = 0;
		
		int i = 0;
		int j = 0;
		while(true) {
			j = indexOf(txt, '\n', i);
			
			if(j == -1) {
				width = Math.max(width, measureTextWidth(fnt, txt, i, txt.length()));
				break;
			} else {
				width = Math.max(width, measureTextWidth(fnt, txt, i, j));
				i = j + 1;
			}
		}
		
		dest.x = width;
		dest.y = measureTextHeight(fnt, txt);
	}
	
	public static float measureTextUntilLineFeed(Font fnt, CharSequence txt, int start) {
		if(fnt == null || txt == null) return 0;
		
		int nextLineFeedIndex = indexOf(txt, '\n', start);
		if(nextLineFeedIndex == -1) nextLineFeedIndex = txt.length();
		
		return measureTextWidth(fnt, txt, start, nextLineFeedIndex);
	}
	
	public static float measureTextWidth(Font fnt, CharSequence txt, int start, int end) {
		if(fnt == null || txt == null || end - start <= 0) return 0;
		float width = 0;
		
		end = MathUtils.clamp(end, 0, txt.length());
		
		for(int i = start; i < end; i++) {
			int c = getCodepoint(txt, i);
			
			if(Character.isLowSurrogate(txt.charAt(i)) || c == '\0' || isNewLine(c)) continue;
			
			FontPackedGlyph bc = fnt.getPackedGlyph(c);
			if(bc == null) continue;
			
			width += bc.xadvance();
		}
		
		return width;
	}
	
	public static float measureTextWidth(Font font, CharSequence text) {
		return measureTextWidth(font, text, 0, text.length());
	}
	
	public static float measureTextHeight(Font fnt, CharSequence txt) {
		if(fnt == null || txt == null || txt.length() == 0) return 0;
		
		float height = fnt.getHeight();
		
		float lineHeight = fnt.getHeight() + fnt.getLineGap();
		
		for(int i = 0; i < txt.length(); i++) {
			int c = getCodepoint(txt, i);
			
			if(isNewLineIgnoreR(c)) height += lineHeight;
		}
		
		return height;
	}
	
	public static CharSequence cutStringWidth(Font fnt, CharSequence txt, float maxWidth, boolean cutStart) {
		if(fnt == null || txt == null) return null;
		if(measureTextWidth(fnt, txt) <= maxWidth + 1) return txt; // 1 pixel margin
		
		float width = 0;
		String threeDotsStr = "…";
		FontPackedGlyph threeDots = fnt.getPackedGlyph(threeDotsStr.codePointAt(0));
		
		if(threeDots == null) {
			threeDots = fnt.getPackedGlyph('.');
			threeDotsStr = ".";
		}
		
		float threeDotsWidth = threeDots.xadvance();
		
		if(cutStart) {
			for(int i = txt.length() - 1; i >= 0; i--) {
				int c = getCodepoint(txt, i);
				
				if(Character.isLowSurrogate(txt.charAt(i)) || c == '\0' || isNewLine(c)) continue;
				
				FontPackedGlyph bc = fnt.getPackedGlyph(c);
				if(bc == null) continue;
				
				width += bc.xadvance();
				
				if(width + threeDotsWidth > maxWidth) return threeDotsStr + txt.subSequence(i, txt.length());
			}
		} else {
			for(int i = 0; i < txt.length(); i++) {
				int c = getCodepoint(txt, i);
				
				if(Character.isLowSurrogate(txt.charAt(i)) || c == '\0' || isNewLine(c)) continue;
				
				FontPackedGlyph bc = fnt.getPackedGlyph(c);
				if(bc == null) continue;
				
				width += bc.xadvance();
				
				if(width + threeDotsWidth > maxWidth) return txt.subSequence(0, i) + threeDotsStr;
			}
		}
		
		return txt;
	}
	
	public static CharSequence makeSingleLine(CharSequence txt) {
		StringBuilder bld = new StringBuilder();
		
		char c;
		for(int i = 0; i < txt.length(); i++) {
			c = txt.charAt(i);
			if(isNewLine(c)) continue;
			
			bld.append(c);
		}
		
		return bld;
	}
	
	public static Vector4f colorBlend(float r1, float g1, float b1, float a1, float r2, float g2, float b2, float a2, float f, Vector4f dest) {
		dest.x = MathUtils.lerp(r1, r2, f);
		dest.y = MathUtils.lerp(g1, g2, f);
		dest.z = MathUtils.lerp(b1, b2, f);
		dest.w = MathUtils.lerp(a1, a2, f);
		
		return _vec4;
	}
	
	public static Vector4f colorBlend(float r1, float g1, float b1, float a1, float r2, float g2, float b2, float a2, float f) {
		return colorBlend(r1, g1, b1, a1, r2, g2, b2, a2, f, _vec4);
	}
	
	public static Vector4f colorBlend(Color a, Color b, float f, Vector4f dest) {
		return colorBlend(a.red(), a.green(), a.blue(), a.alpha(), b.red(), b.green(), b.blue(), b.alpha(), f, dest);
	}
	
	public static Vector4f colorBlend(Color a, Color b, float f) {
		return colorBlend(a, b, f, _vec4);
	}
	
	public static Vector4f colorBlend(Vector4fc a, Vector4fc b, float f, Vector4f dest) {
		return colorBlend(a.x(), a.y(), a.z(), a.w(), b.x(), b.y(), b.z(), b.w(), f, dest);
	}
	
	public static Vector4f colorBlend(Vector4fc a, Vector4fc b, float f) {
		return colorBlend(a, b, f, _vec4);
	}
	
	public static Vector4f colorBlend(Color a, Vector4fc b, float f, Vector4f dest) {
		return colorBlend(a.red(), a.green(), a.blue(), a.alpha(), b.x(), b.y(), b.z(), b.w(), f, dest);
	}
	
	public static Vector4f colorBlend(Color a, Vector4fc b, float f) {
		return colorBlend(a, b, f, _vec4);
	}
	
	public static Vector4f colorBlend(Vector4fc a, Color b, float f, Vector4f dest) {
		return colorBlend(a.x(), a.y(), a.z(), a.w(), b.red(), b.green(), b.blue(), b.alpha(), f, dest);
	}
	
	public static Vector4f colorBlend(Vector4fc a, Color b, float f) {
		return colorBlend(a, b, f, _vec4);
	}
	
	public static Vector4f parseColor(String str, Vector4f vec4) {
		if(vec4 == null) vec4 = new Vector4f();
		
		if(str.matches("^#[0-9A-Fa-f]{6}$")) {
			int rgb = Integer.parseInt(str.substring(1), 16);
			
			vec4.x = ((rgb >> 16) & 0xFF) / 255.0f;
			vec4.y = ((rgb >> 8) & 0xFF) / 255.0f;
			vec4.z = (rgb & 0xFF) / 255.0f;
		} else if(str.matches("^#[0-9A-Fa-f]{3}$")) {
			int rgb = Integer.parseInt(str.substring(1), 16);
			
			vec4.x = ((rgb >> 8) & 0xF) / 15.0f;
			vec4.y = ((rgb >> 4) & 0xF) / 15.0f;
			vec4.z = (rgb & 0xF) / 15.0f;
		} else if(str.matches("^#[0-9A-Fa-f]{4}$")) {
			int rgba = Integer.parseInt(str.substring(1), 16);
			
			vec4.x = ((rgba >> 12) & 0xF) / 15.0f;
			vec4.y = ((rgba >> 8) & 0xF) / 15.0f;
			vec4.z = ((rgba >> 4) & 0xF) / 15.0f;
			vec4.w = (rgba & 0xF) / 15.0f;
		} else if(str.matches("^#[0-9A-Fa-f]{8}$")) {
			int rgba = Integer.parseInt(str.substring(1), 16);
			
			vec4.x = ((rgba >> 24) & 0xFF) / 255.0f;
			vec4.y = ((rgba >> 16) & 0xFF) / 255.0f;
			vec4.z = ((rgba >> 8) & 0xFF) / 255.0f;
			vec4.w = (rgba & 0xFF) / 255.0f;
		}
		
		return vec4;
	}
	
	public static Vector3f parseColor(String str, Vector3f vec3) {
		if(vec3 == null) vec3 = new Vector3f();
		
		if(str.matches("^#[0-9A-Fa-f]{6}$")) {
			int rgb = Integer.parseInt(str.substring(1), 16);
			
			vec3.x = ((rgb >> 16) & 0xFF) / 255.0f;
			vec3.y = ((rgb >> 8) & 0xFF) / 255.0f;
			vec3.z = (rgb & 0xFF) / 255.0f;
		} else if(str.matches("^#[0-9A-Fa-f]{3}$")) {
			int rgb = Integer.parseInt(str.substring(1), 16);
			
			vec3.x = ((rgb >> 8) & 0xF) / 15.0f;
			vec3.y = ((rgb >> 4) & 0xF) / 15.0f;
			vec3.z = (rgb & 0xF) / 15.0f;
		}
		
		return vec3;
	}
	
	public static InputStream getFileInputStream(String filePath, boolean inJar) throws IOException {
		if(inJar) return Nhutils.class.getClassLoader().getResourceAsStream(filePath);
		else return new BufferedInputStream(new FileInputStream(filePath));
	}
	
	public static String readString(InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		
		StringBuilder lines = new StringBuilder();
		String line;
		
		while((line = reader.readLine()) != null)
			lines.append(line).append('\n');
		
		reader.close();
		
		return lines.toString();
	}
	
	public static byte[] readFileBytes(CharSequence filePath, boolean inJar) throws IOException {
		if(inJar) {
			InputStream ressource = Nhutils.class.getClassLoader().getResourceAsStream(filePath.toString());
			if(ressource == null) {
				System.err.println("Can't find ressource: " + filePath);
				
				return null;
			}
			
			BufferedInputStream in = new BufferedInputStream(ressource);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[2048];
			int numByteRead = 0;
			while((numByteRead = in.read(buffer)) != -1)
				baos.write(buffer, 0, numByteRead);
			
			in.close();
			
			return baos.toByteArray();
		} else {
			Path path = Paths.get(filePath.toString());
			
			if(path == null || !Files.exists(path) || Files.isDirectory(path)) throw new IOException("Couldn't find file " + path.toAbsolutePath());
			
			return Files.readAllBytes(path);
		}
	}
	
	public static String readFile(CharSequence filePath, boolean inJar) throws IOException {
		InputStream in = getFileInputStream(filePath.toString(), inJar);
		if(in == null) {
			System.err.println("Can't find ressource: " + filePath);
			return null;
		}
		
		String str = readString(in);
		in.close();
		
		return str;
	}
	
	public static String readFile(File file, boolean inJar) throws IOException {
		return Nhutils.readFile(file.getAbsolutePath(), inJar);
	}
	
	public static void getURLInputStream(CharSequence urlStr, Consumer<InputStream> callback) throws IOException {
		if(callback == null) return;
		
		try {
			URL url = new URI(urlStr.toString()).toURL();
			
			Thread connectionThread = new Thread(() -> {
				try {
					InputStream in = url.openStream();
					TimeManager.setTimeout(() -> {
						callback.accept(in);
					}, 0);
				} catch(IOException e) {
					e.printStackTrace();
				}
			});
			connectionThread.start();
		} catch(URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public static void downloadAsString(CharSequence urlStr, Consumer<String> callback) throws IOException {
		if(callback == null) return;
		
		try {
			URL url = new URI(urlStr.toString()).toURL();
			
			Thread connectionThread = new Thread(() -> {
				try {
					InputStream in = url.openStream();
					String str = readString(in);
					in.close();
					TimeManager.setTimeout(() -> {
						callback.accept(str);
					}, 0);
				} catch(IOException e) {
					e.printStackTrace();
				}
			});
			connectionThread.start();
		} catch(URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	public static String join(byte[] o, String c) {
		StringBuilder builder = new StringBuilder();
		
		for(int i = 0; i < o.length - 1; i++) {
			builder.append(o[i]);
			builder.append(c);
		}
		
		builder.append(o[o.length - 1]);
		
		return builder.toString();
	}
	
	public static String join(short[] o, String c) {
		StringBuilder builder = new StringBuilder();
		
		for(int i = 0; i < o.length - 1; i++) {
			builder.append(o[i]);
			builder.append(c);
		}
		
		builder.append(o[o.length - 1]);
		
		return builder.toString();
	}
	
	public static String join(char[] o, String c) {
		StringBuilder builder = new StringBuilder();
		
		for(int i = 0; i < o.length - 1; i++) {
			builder.append(o[i]);
			builder.append(c);
		}
		
		builder.append(o[o.length - 1]);
		
		return builder.toString();
	}
	
	public static String join(int[] o, String c) {
		StringBuilder builder = new StringBuilder();
		
		for(int i = 0; i < o.length - 1; i++) {
			builder.append(o[i]);
			builder.append(c);
		}
		
		builder.append(o[o.length - 1]);
		
		return builder.toString();
	}
	
	public static String join(float[] o, String c) {
		StringBuilder builder = new StringBuilder();
		
		for(int i = 0; i < o.length - 1; i++) {
			builder.append(o[i]);
			builder.append(c);
		}
		
		builder.append(o[o.length - 1]);
		
		return builder.toString();
	}
	
	public static String join(long[] o, String c) {
		StringBuilder builder = new StringBuilder();
		
		for(int i = 0; i < o.length - 1; i++) {
			builder.append(o[i]);
			builder.append(c);
		}
		
		builder.append(o[o.length - 1]);
		
		return builder.toString();
	}
	
	public static String join(double[] o, String c) {
		StringBuilder builder = new StringBuilder();
		
		for(int i = 0; i < o.length - 1; i++) {
			builder.append(o[i]);
			builder.append(c);
		}
		
		builder.append(o[o.length - 1]);
		
		return builder.toString();
	}
	
	public static String join(Object[] o, String c) {
		StringBuilder builder = new StringBuilder();
		
		for(int i = 0; i < o.length - 1; i++) {
			builder.append(o[i]);
			builder.append(c);
		}
		
		builder.append(o[o.length - 1]);
		
		return builder.toString();
	}
	
	public static boolean check4int(int a, int... xs) {
		for(int i = 0, l = xs.length; i < l; i++)
			if(xs[i] == a) return true;
		
		return false;
	}
}
