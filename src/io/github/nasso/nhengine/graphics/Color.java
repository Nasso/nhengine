package io.github.nasso.nhengine.graphics;

import java.util.HashMap;
import java.util.Map;

import org.joml.Vector4f;
import org.joml.Vector4fc;

import io.github.nasso.nhengine.utils.MathUtils;

public class Color implements Paint {
	private static final Map<Integer, Color> COLORS = new HashMap<Integer, Color>();
	
	/**
	 * The color transparent with an alpha of 0%.
	 */
	public static final Color TRANSPARENT = Color.get(0f, 0f, 0f, 0f);
	/**
	 * The color black with an RGB value of #000000 <div style= "border:1px solid black;width:40px;height:20px;background-color:#000000;float:right;margin: 0 10px 0 0" ></div><br/>
	 * <br/>
	 */
	public static final Color BLACK = Color.get(0f);
	/**
	 * The color dark gray with an RGB value of #404040 <div style= "border:1px solid black;width:40px;height:20px;background-color:#404040;float:right;margin: 0 10px 0 0" ></div><br/>
	 * <br/>
	 */
	public static final Color DARK_GRAY = Color.get(0.25f);
	/**
	 * The color gray with an RGB value of #808080 <div style= "border:1px solid black;width:40px;height:20px;background-color:#808080;float:right;margin: 0 10px 0 0" ></div><br/>
	 * <br/>
	 */
	public static final Color GRAY = Color.get(0.5f);
	/**
	 * The color light gray with an RGB value of #BFBFBF <div style= "border:1px solid black;width:40px;height:20px;background-color:#BFBFBF;float:right;margin: 0 10px 0 0" ></div><br/>
	 * <br/>
	 */
	public static final Color LIGHT_GRAY = Color.get(0.75f);
	/**
	 * The color white with an RGB value of #FFFFFF <div style= "border:1px solid black;width:40px;height:20px;background-color:#FFFFFF;float:right;margin: 0 10px 0 0" ></div><br/>
	 * <br/>
	 */
	public static final Color WHITE = Color.get(1f);
	
	// ------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * The color red with an RGB value of #FF0000 <div style= "border:1px solid black;width:40px;height:20px;background-color:#FF0000;float:right;margin: 0 10px 0 0" ></div><br/>
	 * <br/>
	 */
	public static final Color RED = Color.get(1f, 0f, 0f);
	/**
	 * The color orange with an RGB value of #FF8000 <div style= "border:1px solid black;width:40px;height:20px;background-color:#FF8000;float:right;margin: 0 10px 0 0" ></div><br/>
	 * <br/>
	 */
	public static final Color ORANGE = Color.get(1f, 0.5f, 0f);
	/**
	 * The color yellow with an RGB value of #FFFF00 <div style= "border:1px solid black;width:40px;height:20px;background-color:#FFFF00;float:right;margin: 0 10px 0 0" ></div><br/>
	 * <br/>
	 */
	public static final Color YELLOW = Color.get(1f, 1f, 0f);
	/**
	 * The color yellow-green with an RGB value of #80FF00 <div style= "border:1px solid black;width:40px;height:20px;background-color:#80FF00;float:right;margin: 0 10px 0 0" ></div><br/>
	 * <br/>
	 */
	public static final Color YELLOW_GREEN = Color.get(0.5f, 1f, 0f);
	/**
	 * The color lime with an RGB value of #00FF00 <div style= "border:1px solid black;width:40px;height:20px;background-color:#00FF00;float:right;margin: 0 10px 0 0" ></div><br/>
	 * <br/>
	 */
	public static final Color LIME = Color.get(0f, 1f, 0f);
	/**
	 * The color blue-green with an RGB value of #00FF80 <div style= "border:1px solid black;width:40px;height:20px;background-color:#00FF80;float:right;margin: 0 10px 0 0" ></div><br/>
	 * <br/>
	 */
	public static final Color BLUE_GREEN = Color.get(0f, 1f, 0.5f);
	/**
	 * The color cyan with an RGB value of #00FFFF <div style= "border:1px solid black;width:40px;height:20px;background-color:#00FFFF;float:right;margin: 0 10px 0 0" ></div><br/>
	 * <br/>
	 */
	public static final Color CYAN = Color.get(0f, 1f, 1f);
	/**
	 * The color royal-blue with an RGB value of #0080FF <div style= "border:1px solid black;width:40px;height:20px;background-color:#0080FF;float:right;margin: 0 10px 0 0" ></div><br/>
	 * <br/>
	 */
	public static final Color ROYAL_BLUE = Color.get(0f, 0.5f, 1f);
	/**
	 * The color blue with an RGB value of #0000FF <div style= "border:1px solid black;width:40px;height:20px;background-color:#0000FF;float:right;margin: 0 10px 0 0" ></div><br/>
	 * <br/>
	 */
	public static final Color BLUE = Color.get(0f, 0f, 1f);
	/**
	 * The color ??? with an RGB value of #8000FF <div style= "border:1px solid black;width:40px;height:20px;background-color:#8000FF;float:right;margin: 0 10px 0 0" ></div><br/>
	 * <br/>
	 */
	public static final Color PURPLE = Color.get(0.5f, 0f, 1f);
	/**
	 * The color fuchsia with an RGB value of #FF00FF <div style= "border:1px solid black;width:40px;height:20px;background-color:#FF00FF;float:right;margin: 0 10px 0 0" ></div><br/>
	 * <br/>
	 */
	public static final Color FUCHSIA = Color.get(1f, 0f, 1f);
	/**
	 * The color candy-pink with an RGB value of #FF0080 <div style= "border:1px solid black;width:40px;height:20px;background-color:#FF0080;float:right;margin: 0 10px 0 0" ></div><br/>
	 * <br/>
	 */
	public static final Color CANDY_PINK = Color.get(1f, 0f, 0.5f);
	
	// ------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * The color maroon with an RGB value of #800000 <div style= "border:1px solid black;width:40px;height:20px;background-color:#800000;float:right;margin: 0 10px 0 0" ></div><br/>
	 * <br/>
	 */
	public static final Color MAROON = Color.get(0.5f, 0f, 0f);
	/**
	 * The color olive with an RGB value of #808000 <div style= "border:1px solid black;width:40px;height:20px;background-color:#808000;float:right;margin: 0 10px 0 0" ></div><br/>
	 * <br/>
	 */
	public static final Color OLIVE = Color.get(0.5f, 0.5f, 0f);
	/**
	 * The color green with an RGB value of #008000 <div style= "border:1px solid black;width:40px;height:20px;background-color:#008000;float:right;margin: 0 10px 0 0" ></div><br/>
	 * <br/>
	 */
	public static final Color GREEN = Color.get(0f, 0.5f, 0f);
	/**
	 * The color teal with an RGB value of #008080 <div style= "border:1px solid black;width:40px;height:20px;background-color:#008080;float:right;margin: 0 10px 0 0" ></div><br/>
	 * <br/>
	 */
	public static final Color TEAL = Color.get(0f, 0.5f, 0.5f);
	/**
	 * The color navy with an RGB value of #000080 <div style= "border:1px solid black;width:40px;height:20px;background-color:#000080;float:right;margin: 0 10px 0 0" ></div><br/>
	 * <br/>
	 */
	public static final Color NAVY = Color.get(0f, 0f, 0.5f);
	/**
	 * The color purple with an RGB value of #800080 <div style= "border:1px solid black;width:40px;height:20px;background-color:#800080;float:right;margin: 0 10px 0 0" ></div><br/>
	 * <br/>
	 */
	public static final Color DARK_PURPLE = Color.get(0.5f, 0f, 0.5f);
	
	public static Color get(float r, float g, float b, float a) {
		int ri = (int) (MathUtils.clamp(r, 0.0f, 1.0f) * 255);
		int gi = (int) (MathUtils.clamp(g, 0.0f, 1.0f) * 255);
		int bi = (int) (MathUtils.clamp(b, 0.0f, 1.0f) * 255);
		int ai = (int) (MathUtils.clamp(a, 0.0f, 1.0f) * 255);
		
		int hash = ri & 0xFF;
		hash <<= 8;
		hash |= gi & 0xFF;
		hash <<= 8;
		hash |= bi & 0xFF;
		hash <<= 8;
		hash |= ai & 0xFF;
		
		Color c = COLORS.get(hash);
		if(c != null) return c;
		
		c = new Color(r, g, b, a);
		COLORS.put(hash, c);
		
		return c;
	}
	
	public static Color get(float r, float g, float b) {
		return get(r, g, b, 1.0f);
	}
	
	public static Color get(float gray) {
		return get(gray, gray, gray);
	}
	
	public static Color get(Vector4fc rgba) {
		return get(rgba.x(), rgba.y(), rgba.z(), rgba.w());
	}
	
	// ----
	public Vector4fc rgba = null;
	
	private Color(float r, float g, float b, float a) {
		this.rgba = new Vector4f(r, g, b, a);
	}
	
	public float red() {
		return this.rgba.x();
	}
	
	public float green() {
		return this.rgba.y();
	}
	
	public float blue() {
		return this.rgba.z();
	}
	
	public float alpha() {
		return this.rgba.w();
	}
	
	public boolean equals(Object o) {
		if(this == o) return true;
		
		if(o instanceof Color) return this.rgba.equals(((Color) o).rgba);
		
		return false;
	}
	
	public Vector4fc getRGBA() {
		return this.rgba;
	}
	
	public String toString() {
		return "rgba(" + this.rgba.x() + ", " + this.rgba.y() + ", " + this.rgba.z() + ", " + this.rgba.w() + ")";
	}
}
