package io.github.nasso.nhengine.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import io.github.nasso.nhengine.core.Disposable;
import io.github.nasso.nhengine.graphics.Drawable2D;
import io.github.nasso.nhengine.graphics.GraphicsContext2D;
import io.github.nasso.nhengine.utils.Nhutils;
import io.github.nasso.nhengine.utils.parsing.Token;
import io.github.nasso.nhengine.utils.parsing.Tokenizer;

public class ScalableVectorGraphics implements Drawable2D, Disposable {
	private GraphicsContext2D gtx = new GraphicsContext2D();
	
	private StyleProperties sprops = new StyleProperties();
	
	private Vector2f curs = new Vector2f();
	
	private static enum TokenType {
		// @format:off
		TRANSFORM_FUNC_MATRIX,
		TRANSFORM_FUNC_TRANSLATE,
		TRANSFORM_FUNC_SCALE,
		TRANSFORM_FUNC_ROTATE,
		TRANSFORM_FUNC_SKEWX,
		TRANSFORM_FUNC_SKEWY,
		
		PATH_ABS_MOVETO,
		PATH_REL_MOVETO,
		PATH_ABS_LINETO,
		PATH_REL_LINETO,
		PATH_ABS_HORTO,
		PATH_REL_HORTO,
		PATH_ABS_VERTO,
		PATH_REL_VERTO,
		PATH_ABS_QUADTO,
		PATH_REL_QUADTO,
		PATH_ABS_CURVETO,
		PATH_REL_CURVETO,
		PATH_ABS_ARCTO,
		PATH_REL_ARCTO,
		PATH_CLOSEPATH,
		
		OPEN_PAREN,
		CLOSE_PAREN,
		NUMBER,
		COMMA
		// @format:on
	}
	
	private static Tokenizer<TokenType> tokenizer = new Tokenizer<TokenType>();
	
	static {
		tokenizer.ignore("(\\s)+");
		
		tokenizer.addToken("\\,", TokenType.COMMA);
		
		tokenizer.addToken("matrix", TokenType.TRANSFORM_FUNC_MATRIX);
		tokenizer.addToken("translate", TokenType.TRANSFORM_FUNC_TRANSLATE);
		tokenizer.addToken("scale", TokenType.TRANSFORM_FUNC_SCALE);
		tokenizer.addToken("rotate", TokenType.TRANSFORM_FUNC_ROTATE);
		tokenizer.addToken("skewX", TokenType.TRANSFORM_FUNC_SKEWX);
		tokenizer.addToken("skewY", TokenType.TRANSFORM_FUNC_SKEWY);
		
		tokenizer.addToken("M", TokenType.PATH_ABS_MOVETO);
		tokenizer.addToken("m", TokenType.PATH_REL_MOVETO);
		tokenizer.addToken("L", TokenType.PATH_ABS_MOVETO);
		tokenizer.addToken("l", TokenType.PATH_REL_MOVETO);
		tokenizer.addToken("H", TokenType.PATH_ABS_HORTO);
		tokenizer.addToken("h", TokenType.PATH_REL_HORTO);
		tokenizer.addToken("V", TokenType.PATH_ABS_VERTO);
		tokenizer.addToken("v", TokenType.PATH_REL_VERTO);
		tokenizer.addToken("Q", TokenType.PATH_ABS_QUADTO);
		tokenizer.addToken("q", TokenType.PATH_REL_QUADTO);
		tokenizer.addToken("C", TokenType.PATH_ABS_CURVETO);
		tokenizer.addToken("c", TokenType.PATH_REL_CURVETO);
		tokenizer.addToken("A", TokenType.PATH_ABS_ARCTO);
		tokenizer.addToken("a", TokenType.PATH_REL_ARCTO);
		tokenizer.addToken("(Z|z)", TokenType.PATH_REL_MOVETO);
		
		tokenizer.addToken("\\(", TokenType.OPEN_PAREN);
		tokenizer.addToken("\\)", TokenType.CLOSE_PAREN);
		// tokenizer.addToken("(\\-)?((\\d*\\.\\d+)|(\\d+))(?=(\\W|$))", TokenType.NUMBER);
		tokenizer.addToken("(([\\+\\-]?[0-9]*\\.[0-9]+([Ee]([\\+\\-]?[0-9]+))?)|(([\\+\\-]?[0-9]+)([Ee]([\\+\\-]?[0-9]+))?))", TokenType.NUMBER);
	}
	
	public ScalableVectorGraphics(String path) throws IOException {
		this(path, false);
	}
	
	public ScalableVectorGraphics(String path, boolean inJar) throws IOException {
		InputStream in = Nhutils.getFileInputStream(path, inJar);
		this.load(in);
		in.close();
	}
	
	public ScalableVectorGraphics(InputStream in) throws IOException {
		this.load(in);
	}
	
	private void load(InputStream in) throws IOException {
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
			this.renderDocument(doc);
		} catch(SAXException e) {
			e.printStackTrace();
		} catch(ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	private void renderDocument(Document doc) {
		Node svg = doc.getElementsByTagName("svg").item(0);
		
		this.renderNode(svg);
	}
	
	private void renderNode(Node n) {
		if(n instanceof Element) {
			Element e = (Element) n;
			
			this.gtx.save();
			
			if(e.hasAttribute("transform")) this.applyTransform(e.getAttribute("transform"));
			if(e.hasAttribute("style")) {
				this.sprops.reset();
				this.sprops.set(e.getAttribute("style"));
			}
			
			String name = e.getTagName();
			
			if("svg".equals(name) || "g".equals(name)) {
				NodeList elems = e.getChildNodes();
				for(int i = 0; i < elems.getLength(); i++) {
					Node c = elems.item(i);
					
					this.renderNode(c);
				}
			} else if("path".equals(name)) {
				if(this.sprops.fill != null || this.sprops.stroke != null) {
					if(e.hasAttribute("d")) this.applyPath(e.getAttribute("d"));
					
					if(this.sprops.fill != null) {
						this.gtx.setFill(this.sprops.fill, this.sprops.fillOpacity);
						this.gtx.fill();
					}
					
					if(this.sprops.stroke != null) {
						this.gtx.setStrokeSize(this.sprops.strokeWidth);
						this.gtx.setStroke(this.sprops.stroke, this.sprops.strokeOpacity);
						this.gtx.stroke();
					}
				}
			}
			
			this.gtx.restore();
		}
	}
	
	private float nextFloat(Iterator<Token<TokenType>> tks) {
		return tks.hasNext() ? Float.valueOf(tks.next().getValue()) : Float.NaN;
	}
	
	private void applyPath(String pathList) {
		List<Token<TokenType>> tokenList = tokenizer.tokenize(pathList);
		Iterator<Token<TokenType>> tks = tokenList.iterator();
		
		TokenType lastFunc = null;
		
		boolean firstPoint = true;
		
		float sx = 0, sy = 0;
		
		float x = 0, y = 0;
		float c1x = 0, c1y = 0, c2x = 0, c2y = 0;
		
		this.gtx.beginPath();
		
		Token<TokenType> t;
		while(tks.hasNext()) {
			t = tks.next();
			
			switch(t.getType()) {
				case PATH_ABS_MOVETO:
				case PATH_REL_MOVETO:
				case PATH_ABS_LINETO:
				case PATH_REL_LINETO:
				case PATH_ABS_HORTO:
				case PATH_REL_HORTO:
				case PATH_ABS_VERTO:
				case PATH_REL_VERTO:
				case PATH_ABS_ARCTO:
				case PATH_REL_ARCTO:
				case PATH_ABS_QUADTO:
				case PATH_REL_QUADTO:
				case PATH_ABS_CURVETO:
				case PATH_REL_CURVETO:
					lastFunc = t.getType();
					break;
				
				case PATH_CLOSEPATH:
					this.gtx.closePath();
					this.curs.set(sx, sy);
					lastFunc = null;
					break;
				
				case NUMBER:
					switch(lastFunc) {
						// Moveto
						case PATH_ABS_MOVETO:
							x = Float.valueOf(t.getValue());
							tks.next(); // comma
							y = this.nextFloat(tks);
							
							this.curs.set(x, y);
							this.gtx.moveTo(this.curs.x, this.curs.y);
							
							sx = this.curs.x;
							sy = this.curs.y;
							
							lastFunc = TokenType.PATH_ABS_LINETO;
							break;
						case PATH_REL_MOVETO:
							x = Float.valueOf(t.getValue());
							tks.next(); // comma
							y = this.nextFloat(tks);
							
							// absolute if first path point 
							if(firstPoint) this.curs.set(x, y);
							else this.curs.add(x, y);
							this.gtx.moveTo(this.curs.x, this.curs.y);
							
							sx = this.curs.x;
							sy = this.curs.y;
							
							lastFunc = TokenType.PATH_REL_LINETO;
							break;
						
						// Lineto
						case PATH_ABS_LINETO:
							x = Float.valueOf(t.getValue());
							tks.next(); // comma
							y = this.nextFloat(tks);
							
							this.curs.set(x, y);
							this.gtx.lineTo(this.curs.x, this.curs.y);
							break;
						case PATH_REL_LINETO:
							x = Float.valueOf(t.getValue());
							tks.next(); // comma
							y = this.nextFloat(tks);
							
							this.curs.add(x, y);
							this.gtx.lineTo(this.curs.x, this.curs.y);
							break;
						
						// Horizontal Lineto
						case PATH_ABS_HORTO:
							this.curs.x = Float.valueOf(t.getValue());
							this.gtx.lineTo(this.curs.x, this.curs.y);
							break;
						case PATH_REL_HORTO:
							this.curs.x += Float.valueOf(t.getValue());
							this.gtx.lineTo(this.curs.x, this.curs.y);
							break;
						
						// Vertical Lineto
						case PATH_ABS_VERTO:
							this.curs.y = Float.valueOf(t.getValue());
							this.gtx.lineTo(this.curs.x, this.curs.y);
							break;
						case PATH_REL_VERTO:
							this.curs.y += Float.valueOf(t.getValue());
							this.gtx.lineTo(this.curs.x, this.curs.y);
							break;
						
						// Arcto
						case PATH_ABS_ARCTO:
							break;
						case PATH_REL_ARCTO:
							break;
						
						// Quadratic Curveto
						case PATH_ABS_QUADTO:
							c1x = Float.valueOf(t.getValue());
							tks.next();
							c1y = this.nextFloat(tks);
							
							x = this.nextFloat(tks);
							tks.next();
							y = this.nextFloat(tks);
							
							this.curs.set(x, y);
							this.gtx.quadTo(c1x, c1y, x, y);
							break;
						case PATH_REL_QUADTO:
							c1x = this.curs.x + Float.valueOf(t.getValue());
							tks.next();
							c1y = this.curs.y + this.nextFloat(tks);
							
							x = this.curs.x + this.nextFloat(tks);
							tks.next();
							y = this.curs.y + this.nextFloat(tks);
							
							this.curs.set(x, y);
							this.gtx.quadTo(c1x, c1y, x, y);
							break;
						
						// Curveto
						case PATH_ABS_CURVETO:
							c1x = Float.valueOf(t.getValue());
							tks.next();
							c1y = this.nextFloat(tks);
							
							c2x = this.nextFloat(tks);
							tks.next();
							c2y = this.nextFloat(tks);
							
							x = this.nextFloat(tks);
							tks.next();
							y = this.nextFloat(tks);
							
							this.curs.set(x, y);
							this.gtx.bezierTo(c1x, c1y, c2x, c2y, x, y);
							break;
						case PATH_REL_CURVETO:
							c1x = this.curs.x + Float.valueOf(t.getValue());
							tks.next();
							c1y = this.curs.y + this.nextFloat(tks);
							
							c2x = this.curs.x + this.nextFloat(tks);
							tks.next();
							c2y = this.curs.y + this.nextFloat(tks);
							
							x = this.curs.x + this.nextFloat(tks);
							tks.next();
							y = this.curs.y + this.nextFloat(tks);
							
							this.curs.set(x, y);
							this.gtx.bezierTo(c1x, c1y, c2x, c2y, x, y);
							break;
						
						default:
							break;
					}
					break;
				default:
					break;
			}
			
			firstPoint = false;
		}
	}
	
	private int nextArgs(Iterator<Token<TokenType>> tks, float[] commands) {
		if(commands.length == 0 || tks.next().getType() != TokenType.OPEN_PAREN) return 0;
		
		int i = 0;
		
		while(tks.hasNext()) {
			Token<TokenType> tk = tks.next();
			
			switch(tk.getType()) {
				case CLOSE_PAREN:
					return i + 1;
				case COMMA:
					i++;
					if(i >= commands.length) return i;
					break;
				case NUMBER:
					if(i < commands.length) commands[i] = Float.valueOf(tk.getValue());
					break;
				default:
					return i + 1;
			}
		}
		
		return i + 1;
	}
	
	private void applyTransform(String xformList) {
		Iterator<Token<TokenType>> tokens = tokenizer.tokenize(xformList).iterator();
		
		float[] commands = new float[6];
		int argc = 0;
		
		while(tokens.hasNext()) {
			Token<TokenType> tk = tokens.next();
			
			switch(tk.getType()) {
				case TRANSFORM_FUNC_MATRIX:
					argc = this.nextArgs(tokens, commands);
					this.gtx.transform(commands[0], commands[1], commands[2], commands[3], commands[4], commands[5]);
					break;
				case TRANSFORM_FUNC_TRANSLATE:
					argc = this.nextArgs(tokens, commands);
					this.gtx.translate(commands[0], argc > 1 ? commands[1] : 0);
					break;
				case TRANSFORM_FUNC_SCALE:
					argc = this.nextArgs(tokens, commands);
					this.gtx.scale(commands[0], argc > 1 ? commands[1] : commands[0]);
					break;
				case TRANSFORM_FUNC_ROTATE:
					argc = this.nextArgs(tokens, commands);
					if(argc == 1) this.gtx.rotate(commands[0]);
					else {
						this.gtx.translate(commands[1], commands[2]);
						this.gtx.rotate(commands[0]);
						this.gtx.translate(-commands[1], -commands[2]);
					}
					break;
				case TRANSFORM_FUNC_SKEWX:
					argc = this.nextArgs(tokens, commands);
					this.gtx.skewX(commands[0]);
					break;
				case TRANSFORM_FUNC_SKEWY:
					argc = this.nextArgs(tokens, commands);
					this.gtx.skewY(commands[0]);
					break;
				default:
					break;
			}
		}
	}
	
	public void draw(GraphicsContext2D gtx) {
		if(this.gtx == null) return;
		
		gtx.apply(this.gtx);
	}
	
	public void dispose() {
		this.gtx.discardAll();
		
		this.gtx = null;
		this.sprops = null;
		this.curs = null;
	}
	
	private static class StyleProperties {
		Vector3f fill;
		float fillOpacity = 1;
		
		Vector3f stroke;
		float strokeOpacity = 1;
		
		float strokeWidth = 1;
		
		public void reset() {
			this.fill = null;
			this.fillOpacity = 1;
			this.stroke = null;
			this.strokeOpacity = 1;
		}
		
		public void set(String str) {
			String[] props = str.split(";");
			
			for(int i = 0; i < props.length; i++) {
				String[] prop = props[i].split(":");
				
				String k = prop[0];
				String v = prop[1];
				
				if("fill".equals(k)) {
					if("none".equals(v)) this.fill = null;
					else this.fill = Nhutils.parseColor(v, this.fill);
				} else if("fill-opacity".equals(k)) {
					this.fillOpacity = Float.valueOf(v);
				} else if("stroke".equals(k)) {
					if("none".equals(v)) this.stroke = null;
					else this.stroke = Nhutils.parseColor(v, this.stroke);
				} else if("stroke-opacity".equals(k)) {
					this.strokeOpacity = Float.valueOf(v);
				} else if("stroke-width".equals(k)) {
					this.strokeOpacity = Float.valueOf(v);
				}
			}
		}
	}
}
