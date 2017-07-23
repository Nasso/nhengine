package io.github.nasso.nhengine.opengl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL31.*;

import java.io.IOException;

import org.joml.Matrix3fc;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector4fc;

import io.github.nasso.nhengine.component.TextComponent;
import io.github.nasso.nhengine.graphics.Color;
import io.github.nasso.nhengine.graphics.Font;
import io.github.nasso.nhengine.graphics.Font.FontPackedGlyph;
import io.github.nasso.nhengine.graphics.TextAlignment;
import io.github.nasso.nhengine.graphics.TextBaseline;
import io.github.nasso.nhengine.level.Camera;
import io.github.nasso.nhengine.level.Scene;
import io.github.nasso.nhengine.utils.Nhutils;

public class OGLTextRenderer extends OGLComponentRenderer<TextComponent> {
	private static final int MAX_GLYPH_INSTANCES = 256;
	
	private int[] _i2 = new int[2];
	
	private Matrix4f _mat4 = new Matrix4f();
	
	private OGLTextProgram gbufferTextProgram;
	
	private OGLTextures textures = OGLTextures.get();
	
	private OGLVertexArray glyphRectVAO;
	
	private float[] glyph_instPositionStartXYEndXY = new float[MAX_GLYPH_INSTANCES * 4];
	private float[] glyph_instTextureStartSTEndST = new float[MAX_GLYPH_INSTANCES * 4];
	private float[] glyph_letterDiffuseColorAlpha = new float[MAX_GLYPH_INSTANCES * 4];
	private float glyph_rendererPosX, glyph_rendererPosY;
	
	private OGLArrayBuffer glyph_instPositionStartXYEndXYVBO; // vec4
	private OGLArrayBuffer glyph_instTextureStartSTEndSTVBO; // vec4
	private OGLArrayBuffer glyph_letterDiffuseColorAlphaVBO; // vec4
	
	public OGLTextRenderer(OGLArrayBuffer rectPosVBO, int width, int height) throws IOException {
		super(width, height);
		
		this.gbufferTextProgram = new OGLTextProgram();
		
		this.glyph_instPositionStartXYEndXYVBO = new OGLArrayBuffer(MAX_GLYPH_INSTANCES * 16, GL_STREAM_DRAW);
		this.glyph_instTextureStartSTEndSTVBO = new OGLArrayBuffer(MAX_GLYPH_INSTANCES * 16, GL_STREAM_DRAW);
		this.glyph_letterDiffuseColorAlphaVBO = new OGLArrayBuffer(MAX_GLYPH_INSTANCES * 16, GL_STREAM_DRAW);
		
		// Glyphs (text)
		this.glyphRectVAO = new OGLVertexArray();
		this.glyphRectVAO.bind();
		
		this.glyphRectVAO.setAttribLocationEnabled(0, true);
		this.glyphRectVAO.loadVBOToAttrib(0, 2, rectPosVBO);
		
		this.glyphRectVAO.setAttribLocationEnabled(1, true);
		this.glyphRectVAO.loadVBOToAttrib(1, 4, this.glyph_instPositionStartXYEndXYVBO, 0, 0, 1);
		
		this.glyphRectVAO.setAttribLocationEnabled(2, true);
		this.glyphRectVAO.loadVBOToAttrib(2, 4, this.glyph_instTextureStartSTEndSTVBO, 0, 0, 1);
		
		this.glyphRectVAO.setAttribLocationEnabled(3, true);
		this.glyphRectVAO.loadVBOToAttrib(3, 4, this.glyph_letterDiffuseColorAlphaVBO, 0, 0, 1);
	}
	
	public void render(Scene sce, TextComponent comp) {
		if(comp.getFill() == null && comp.getStroke() == null) return;
		if(comp.getFont() == null) return;
		if(comp.getText() == null) return;
		if(comp.getText().length() <= 0) return;
		
		Camera cam = sce.getCamera();
		
		this._mat4.set(cam.getProjViewMatrix(true));
		this._mat4.mul(comp.getWorldMatrix(true));
		
		Vector4fc rgba;
		if(comp.getFill() instanceof Color) {
			rgba = ((Color) comp.getFill()).getRGBA();
		} else {
			// TODO: support text stroke, and more paints
			return;
		}
		
		this.render(comp.getText(), 0, 0, this._mat4, comp.getFont(), comp.getAlignment(), comp.getBaseline(), rgba, null, comp.getAlpha(), comp.isOpaque());
	}
	
	public void render2D(CharSequence text, float x, float y, Matrix3fc transform, float scaleX, float scaleY, Font font, TextAlignment align, TextBaseline baseline, Vector4fc fillColor, Vector4fc strokeColor, OGLClipping clip, float alpha, boolean opaque) {
		if(fillColor == null/* && strokeColor == null */) return;
		if(font == null) return;
		if(text == null) return;
		if(text.length() <= 0) return;
		
		this.gbufferTextProgram.use();
		
		OGLStateManager.INSTANCE.blend(!opaque);
		
		this.gbufferTextProgram.loadToUniform("fontTexture", this.textures.update(font.getTexture()), 0);
		this.gbufferTextProgram.loadToUniform("projViewModel3", transform);
		this.gbufferTextProgram.loadToUniform("scaleXY", scaleX, scaleY);
		this.gbufferTextProgram.loadToUniform("cvsMode", true);
		this.gbufferTextProgram.loadToUniform("clip.enabled", !clip.isNull);
		
		if(!clip.isNull) {
			this.gbufferTextProgram.loadToUniform("clip.xform", clip.xform);
			this.gbufferTextProgram.loadToUniform("clip.extent", clip.extent);
		}
		
		this.renderChunks(text, x, y, transform, null, font, align, baseline, fillColor, strokeColor, clip, alpha, opaque);
	}
	
	public void render(CharSequence text, float x, float y, Matrix4fc transform, Font font, TextAlignment align, TextBaseline baseline, Vector4fc fillColor, Vector4fc strokeColor, float alpha, boolean opaque) {
		if(fillColor == null/* && strokeColor == null */) return;
		if(font == null) return;
		if(text == null) return;
		if(text.length() <= 0) return;
		
		this.gbufferTextProgram.use();
		
		OGLStateManager.INSTANCE.blend(!opaque);
		
		this.gbufferTextProgram.loadToUniform("fontTexture", this.textures.update(font.getTexture()), 0);
		this.gbufferTextProgram.loadToUniform("projViewModel", transform);
		this.gbufferTextProgram.loadToUniform("cvsMode", false);
		this.gbufferTextProgram.loadToUniform("clip.enabled", false);
		
		this.renderChunks(text, x, y, null, transform, font, align, baseline, fillColor, strokeColor, null, alpha, opaque);
	}
	
	private void renderChunks(CharSequence text, float x, float y, Matrix3fc xform3, Matrix4fc xform4, Font font, TextAlignment align, TextBaseline baseline, Vector4fc fillColor, Vector4fc strokeColor, OGLClipping clip, float alpha, boolean opaque) {
		this.glyph_rendererPosX = 0;
		this.glyph_rendererPosY = 0;
		
		float nextLineWidth = Nhutils.measureTextUntilLineFeed(font, text, 0);
		switch(align) {
			case LEFT:
				this.glyph_rendererPosX = 0;
				break;
			case CENTER:
				this.glyph_rendererPosX = -nextLineWidth / 2.0f;
				break;
			case RIGHT:
				this.glyph_rendererPosX = -nextLineWidth;
				break;
		}
		
		float totalHeight = Nhutils.measureTextHeight(font, text) + font.getDescent();
		switch(baseline) {
			case BASELINE:
				this.glyph_rendererPosY = 0;
				break;
			case TOP:
				this.glyph_rendererPosY = font.getAscent() + font.getDescent();
				break;
			case BOTTOM:
				this.glyph_rendererPosY = font.getAscent() + font.getDescent() - totalHeight;
				break;
			case MIDDLE:
				this.glyph_rendererPosY = font.getAscent() + font.getDescent() - totalHeight / 2f;
				break;
			case ASCENT_MIDDLE:
				this.glyph_rendererPosY = font.getAscent() - totalHeight / 2f;
				break;
		}
		
		int textOffset = 0, renderedCharCount = 0;
		while(textOffset < text.length()) {
			int[] preparationResult = this.prepareChars(text, textOffset, x, y, xform3, xform4, font, align, fillColor, strokeColor, alpha, opaque);
			renderedCharCount = preparationResult[0];
			textOffset = preparationResult[1];
			if(renderedCharCount <= 0) continue;
			
			this.glyphRectVAO.bind();
			glDrawArraysInstanced(GL_TRIANGLE_STRIP, 0, 4, renderedCharCount);
			this.glyphRectVAO.unbind();
		}
	}
	
	private int[] prepareChars(CharSequence text, int textOffset, float x, float y, Matrix3fc transform, Matrix4fc transform4, Font font, TextAlignment align, Vector4fc fillColor, Vector4fc strokeColor, float alpha, boolean opaque) {
		float r, g, b, a;
		r = fillColor.x();
		g = fillColor.y();
		b = fillColor.z();
		a = fillColor.w() * alpha;
		
		float lineHeight = font.getHeight() + font.getLineGap();
		float nextLineWidth = Nhutils.measureTextUntilLineFeed(font, text, textOffset);
		float stringWidth = 0;
		
		int renderedCharCount = 0;
		
		int i = textOffset;
		float x0, x1, y0, y1, s0, s1, t0, t1,
				is = 1.0f / font.getTexture().getWidth();
		for(i = textOffset; i < text.length(); i++) {
			int c = Nhutils.getCodepoint(text, i);
			
			if(Character.isLowSurrogate(text.charAt(i)) || c == '\0' || c == '\r') continue;
			if(Nhutils.isNewLineIgnoreR(c)) {
				nextLineWidth = Nhutils.measureTextUntilLineFeed(font, text, i + 1);
				switch(align) {
					case LEFT:
						this.glyph_rendererPosX = 0;
						break;
					case CENTER:
						this.glyph_rendererPosX = -nextLineWidth / 2.0f;
						break;
					case RIGHT:
						this.glyph_rendererPosX = -nextLineWidth;
						break;
				}
				
				this.glyph_rendererPosY += lineHeight;
				continue;
			}
			
			FontPackedGlyph bc = font.getPackedGlyph(c);
			if(bc == null) continue;
			
			x0 = this.glyph_rendererPosX + bc.xoff() + x;
			y0 = this.glyph_rendererPosY + bc.yoff() + y;
			
			x1 = x0 + bc.x1() - bc.x0();
			y1 = y0 + bc.y1() - bc.y0();
			
			this.glyph_rendererPosX += bc.xadvance();
			stringWidth = Math.max(this.glyph_rendererPosX, stringWidth);
			
			// TODO: Pre-clipping for text
			
			s0 = bc.x0() * is;
			t0 = bc.y0() * is;
			s1 = bc.x1() * is;
			t1 = bc.y1() * is;
			
			this.glyph_letterDiffuseColorAlpha[renderedCharCount * 4] = r;
			this.glyph_letterDiffuseColorAlpha[renderedCharCount * 4 + 1] = g;
			this.glyph_letterDiffuseColorAlpha[renderedCharCount * 4 + 2] = b;
			this.glyph_letterDiffuseColorAlpha[renderedCharCount * 4 + 3] = a;
			
			this.glyph_instPositionStartXYEndXY[renderedCharCount * 4] = x0;
			this.glyph_instPositionStartXYEndXY[renderedCharCount * 4 + 1] = y0;
			this.glyph_instPositionStartXYEndXY[renderedCharCount * 4 + 2] = x1;
			this.glyph_instPositionStartXYEndXY[renderedCharCount * 4 + 3] = y1;
			
			this.glyph_instTextureStartSTEndST[renderedCharCount * 4] = s0;
			this.glyph_instTextureStartSTEndST[renderedCharCount * 4 + 1] = t0;
			this.glyph_instTextureStartSTEndST[renderedCharCount * 4 + 2] = s1;
			this.glyph_instTextureStartSTEndST[renderedCharCount * 4 + 3] = t1;
			
			renderedCharCount++;
			
			if(renderedCharCount >= MAX_GLYPH_INSTANCES) break;
		}
		
		if(renderedCharCount > 0) {
			this.glyph_instPositionStartXYEndXYVBO.loadSubData(this.glyph_instPositionStartXYEndXY);
			this.glyph_instTextureStartSTEndSTVBO.loadSubData(this.glyph_instTextureStartSTEndST);
			this.glyph_letterDiffuseColorAlphaVBO.loadSubData(this.glyph_letterDiffuseColorAlpha);
			
			OGLArrayBuffer.unbindAll();
		}
		
		this._i2[0] = renderedCharCount;
		this._i2[1] = i + 1;
		
		return this._i2;
	}
	
	public void dispose() {
		this.glyph_instPositionStartXYEndXYVBO.dispose();
		this.glyph_instTextureStartSTEndSTVBO.dispose();
		this.glyph_letterDiffuseColorAlphaVBO.dispose();
		
		this.gbufferTextProgram.dispose();
	}
}
