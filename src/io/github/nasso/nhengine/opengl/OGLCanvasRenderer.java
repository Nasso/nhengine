package io.github.nasso.nhengine.opengl;

import static io.github.nasso.nhengine.graphics.GraphicsContext2D.*;
import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL30.*;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.joml.Matrix3f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import io.github.nasso.nhengine.component.CanvasComponent;
import io.github.nasso.nhengine.core.Game;
import io.github.nasso.nhengine.event.Observable;
import io.github.nasso.nhengine.graphics.Font;
import io.github.nasso.nhengine.graphics.TextAlignment;
import io.github.nasso.nhengine.graphics.TextBaseline;
import io.github.nasso.nhengine.graphics.Texture2D;
import io.github.nasso.nhengine.level.Camera;
import io.github.nasso.nhengine.level.Scene;
import io.github.nasso.nhengine.nanovg.NVGColors;
import io.github.nasso.nhengine.nanovg.NVGContext;
import io.github.nasso.nhengine.utils.MathUtils;

public class OGLCanvasRenderer extends OGLComponentRenderer<CanvasComponent> {
	private ArrayDeque<GraphicsContext2DState> stateDeque = new ArrayDeque<GraphicsContext2DState>();
	private List<GraphicsContext2DState> freeStates = new ArrayList<GraphicsContext2DState>();
	
	private float[] _f4 = new float[4];
	
	private Matrix3f _mat3_1 = new Matrix3f();
	private Matrix3f _mat3_2 = new Matrix3f();
	
	private Vector3f _vec3 = new Vector3f();
	
	private NVGContext gtx;
	
	private OGLTextRenderer textRenderer;
	private Map<CanvasComponent, OGLRenderTarget2D> canvases = new HashMap<CanvasComponent, OGLRenderTarget2D>();
	
	private boolean hasSetupShapeMatrix = false;
	
	private OGLTextureQuadProgram textureQuadProg;
	private OGLAdvancedTextureQuadProgram advancedTextureProg;
	
	private OGLVertexArray quadVAO;
	
	public OGLCanvasRenderer(OGLArrayBuffer rectPosVBO, int width, int height) throws IOException {
		super(width, height);
		
		this.gtx = NVGContext.create(true);
		this.stateDeque.push(new GraphicsContext2DState(null));
		
		this.textRenderer = new OGLTextRenderer(rectPosVBO, width, height);
		
		this.textureQuadProg = new OGLTextureQuadProgram();
		this.advancedTextureProg = new OGLAdvancedTextureQuadProgram();
		
		this.quadVAO = new OGLVertexArray();
		
		// Bind the VAO
		this.quadVAO.bind();
		
		// Bind the VBO to the VAO at POSITION_ATTRIB_LOCATION
		this.quadVAO.setAttribLocationEnabled(0, true);
		this.quadVAO.loadVBOToAttrib(0, 2, rectPosVBO);
		
		// Unbind everything
		this.quadVAO.unbind();
	}
	
	public void prepareScene(Scene sce) {
		OGLProgram.unuseAll();
		if(this.hasSetupShapeMatrix) return;
		
		Camera cam = sce.getCamera();
		this.gtx.beginFrame(this.getWidth(), this.getHeight(), Game.instance().window().getDevicePixelRatio());
		
		// LMAO I DON'T KNOW HOW I FOUND THIS FORMULA BUT IT WORKS, DON'T ASK ME HOW PLZ
		float camScale = (this.getWidth()) * (0.5f / cam.getScale());
		
		this.gtx.save();
		this.gtx.translate(this.getWidth() / 2f, this.getHeight() / 2f);
		this.gtx.scale(camScale, camScale);
		this.gtx.translate(-cam.getPosition().x, -cam.getPosition().y);
		this.gtx.rotate(cam.getRotation() / 180 * 3.141593f);
		
		this.hasSetupShapeMatrix = true;
	}
	
	public void endScene() {
		if(!this.hasSetupShapeMatrix) return;
		
		this.gtx.restore();
		this.gtx.endFrame();
		
		this.updateStateAfterNVG();
		
		this.hasSetupShapeMatrix = false;
	}
	
	private void updateStateAfterNVG() {
		OGLStateManager.INSTANCE.refreshState();
		OGLStateManager.INSTANCE.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	private void saveAllStates() {
		Iterator<GraphicsContext2DState> i = this.stateDeque.iterator();
		
		while(i.hasNext()) {
			i.next().saveMatrix(this.gtx);
			if(i.hasNext()) {
				this.gtx.restore();
			}
		}
	}
	
	private void restoreAllStates() {
		Iterator<GraphicsContext2DState> i = this.stateDeque.descendingIterator();
		
		while(i.hasNext()) {
			i.next().apply(this.gtx);
			if(i.hasNext()) {
				this.gtx.save();
			}
		}
	}
	
	private GraphicsContext2DState createState(GraphicsContext2DState s) {
		if(this.freeStates.isEmpty()) return new GraphicsContext2DState(s);
		
		return this.freeStates.remove(0).init(s);
	}
	
	private void freeState(GraphicsContext2DState s) {
		this.freeStates.add(s);
	}
	
	public void render(Scene sce, CanvasComponent cvs) {
		final OGLRenderTarget2D target;
		if(this.canvases.containsKey(cvs)) {
			target = this.canvases.get(cvs);
			
			if(target.getColorTexture().getWidth() != cvs.getWidth() || target.getColorTexture().getHeight() != cvs.getHeight()) {
				target.getColorTexture().setWidth(cvs.getWidth());
				target.getColorTexture().setHeight(cvs.getHeight());
				target.getColorTexture().update();
				
				OGLRenderBuffer2D stencilBuffer = target.getFBO().getRenderBuffer(GL_STENCIL_ATTACHMENT);
				stencilBuffer.setWidth(cvs.getWidth());
				stencilBuffer.setHeight(cvs.getHeight());
				stencilBuffer.update();
			}
		} else {
			target = new OGLRenderTarget2D(new OGLTexture2D(cvs.getWidth(), cvs.getHeight(), GL_LINEAR, GL_NEAREST, GL_CLAMP_TO_EDGE, GL_CLAMP_TO_EDGE, GL_RGBA, GL_RGBA, GL_UNSIGNED_BYTE, null));
			
			target.getFBO().bind();
			OGLRenderBuffer2D stencilBuffer = new OGLRenderBuffer2D(GL_STENCIL_INDEX8, cvs.getWidth(), cvs.getHeight());
			target.getFBO().bindRenderBuffer(GL_STENCIL_ATTACHMENT, stencilBuffer);
			
			// can't use lambda
			cvs.addEventListener("dispose", new Consumer<Observable>() {
				public void accept(Observable e) {
					e.removeEventListener("dispose", this); // < because that
					
					target.dispose(true);
					stencilBuffer.dispose();
					
					OGLCanvasRenderer.this.canvases.get(e).dispose(true);
					OGLCanvasRenderer.this.canvases.remove(e);
				}
			});
			
			target.getFBO().unbind();
			stencilBuffer.unbind();
			
			this.canvases.put(cvs, target);
		}
		
		if(!cvs.getContext2D().isCommandStackEmpty()) {
			target.getFBO().bind();
			glViewport(0, 0, cvs.getWidth(), cvs.getHeight());
			
			float[] commandStack = cvs.getContext2D().commandStack();
			Object[] args = cvs.getContext2D().argStack();
			int stackSize = cvs.getContext2D().getCommandStackSize();
			
			OGLProgram.unuseAll();
			
			this.gtx.beginFrame(cvs.getWidth(), cvs.getHeight(), Game.instance().window().getDevicePixelRatio());
			GraphicsContext2DState state = this.createState(null);
			this.stateDeque.push(state);
			
			int saveCount = 0;
			float angle = 0;
			
			int argI = 0;
			for(int i = 0; i < stackSize;) {
				int command = (int) commandStack[i++];
				
				switch(command) {
					case CMD_CLEAR:
						glClearColor(0, 0, 0, 0);
						glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
						glClearColor(0, 0, 0, 1);
						break;
					
					case CMD_STATE_SAVE:
						this.gtx.save();
						state = this.createState(state);
						this.stateDeque.push(state);
						saveCount++;
						break;
					
					case CMD_STATE_RESTORE:
						if(saveCount > 0) {
							this.gtx.restore();
							this.freeState(this.stateDeque.pop());
							state = this.stateDeque.peek();
							saveCount--;
						}
						break;
					
					case CMD_STATE_RESET:
						this.gtx.reset();
						break;
					
					case CMD_RDR_SET_FILL_RGBA:
						state.fill.set(commandStack[i++], commandStack[i++], commandStack[i++], commandStack[i++]);
						this.gtx.fillColor(NVGColors.INSTANCE.get(state.fill.x, state.fill.y, state.fill.z, state.fill.w));
						break;
					
					case CMD_RDR_SET_STROKE_RGBA:
						state.stroke.set(commandStack[i++], commandStack[i++], commandStack[i++], commandStack[i++]);
						this.gtx.strokeColor(NVGColors.INSTANCE.get(state.stroke.x, state.stroke.y, state.stroke.z, state.stroke.w));
						break;
					
					case CMD_RDR_SET_MITER_LIMIT:
						this.gtx.miterLimit(state.miterLimit = commandStack[i++]);
						break;
					
					case CMD_RDR_SET_STROKE_SIZE:
						this.gtx.strokeWidth(state.strokeWidth = commandStack[i++]);
						break;
					
					case CMD_RDR_SET_LINE_CAP:
						switch((int) commandStack[i++]) {
							case CONST_LINECAP_BUTT:
								this.gtx.lineCap(NVG_BUTT);
								state.lineCap = NVG_BUTT;
								break;
							case CONST_LINECAP_ROUND:
								this.gtx.lineCap(NVG_ROUND);
								state.lineCap = NVG_ROUND;
								break;
							case CONST_LINECAP_SQUARE:
								this.gtx.lineCap(NVG_SQUARE);
								state.lineCap = NVG_SQUARE;
								break;
						}
						break;
					
					case CMD_RDR_SET_LINE_JOIN:
						switch((int) commandStack[i++]) {
							case CONST_LINEJOIN_BEVEL:
								this.gtx.lineJoin(NVG_BEVEL);
								state.lineJoin = NVG_BEVEL;
								break;
							
							case CONST_LINEJOIN_MITER:
								this.gtx.lineJoin(NVG_MITER);
								state.lineJoin = NVG_MITER;
								break;
							
							case CONST_LINEJOIN_ROUND:
								this.gtx.lineJoin(NVG_ROUND);
								state.lineJoin = NVG_ROUND;
								break;
						}
						break;
					
					case CMD_RDR_SET_GLOBAL_ALPHA:
						state.globalAlpha = MathUtils.clamp(commandStack[i++], 0.0f, 1.0f) * state.globalAlpha;
						this.gtx.globalAlpha(state.globalAlpha);
						break;
					
					case CMD_TRANS_RESET:
						this.gtx.resetTransform();
						state.transform.identity();
						break;
					
					case CMD_TRANS_APPLY:
						float a = commandStack[i++], b = commandStack[i++],
								c = commandStack[i++], d = commandStack[i++],
								e = commandStack[i++], f = commandStack[i++];
						this.gtx.transform(a, b, c, d, e, f);
						MathUtils.apply(state.transform, a, b, c, d, e, f);
						break;
					
					case CMD_TRANS_TRANSLATE:
						MathUtils.translate(state.transform, commandStack[i], commandStack[i + 1]);
						this.gtx.translate(commandStack[i++], commandStack[i++]);
						break;
					
					case CMD_TRANS_ROTATE:
						angle = commandStack[i++] / 180 * 3.141592f;
						MathUtils.rotate(state.transform, angle);
						this.gtx.rotate(angle);
						break;
					
					case CMD_TRANS_SKEW_X:
						angle = commandStack[i++] / 180 * 3.141592f;
						MathUtils.skewX(state.transform, angle);
						this.gtx.skewX(angle);
						break;
					
					case CMD_TRANS_SKEW_Y:
						angle = commandStack[i++] / 180 * 3.141592f;
						MathUtils.skewY(state.transform, angle);
						this.gtx.skewY(angle);
						break;
					
					case CMD_TRANS_SCALE:
						MathUtils.scale(state.transform, commandStack[i], commandStack[i + 1]);
						this.gtx.scale(commandStack[i++], commandStack[i++]);
						break;
					
					case CMD_IMG_DRAW:
						if(!state.clip.isNull && state.clip.isEmpty()) {
							argI += 1;
							i += 8;
							break;
						}
						
						this.saveAllStates();
						this.gtx.endFrame();
						
						Texture2D tex = (Texture2D) args[argI++];
						
						this.advancedTextureProg.use();
						this.advancedTextureProg.loadToUniform("transform", state.transform);
						this.advancedTextureProg.loadToUniform("sourceXYWH", commandStack[i++] / tex.getWidth(), commandStack[i++] / tex.getHeight(), commandStack[i++] / tex.getWidth(), commandStack[i++] / tex.getHeight());
						this.advancedTextureProg.loadToUniform("destXYWH", commandStack[i++], commandStack[i++], commandStack[i++], commandStack[i++]);
						this.advancedTextureProg.loadToUniform("color", OGLTextures.get().update(tex), 0);
						this.advancedTextureProg.loadToUniform("globalAlpha", state.globalAlpha);
						this.advancedTextureProg.loadToUniform("scaleXY", (float) cvs.getWidth(), (float) cvs.getHeight());
						this.advancedTextureProg.loadToUniform("clip.enabled", !state.clip.isNull);
						
						if(!state.clip.isNull) {
							this.advancedTextureProg.loadToUniform("clip.xform", state.clip.xform);
							this.advancedTextureProg.loadToUniform("clip.extent", state.clip.extent);
						}
						
						glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
						
						this.quadVAO.bind();
						glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
						this.quadVAO.unbind();
						
						OGLProgram.unuseAll();
						
						this.gtx.beginFrame(cvs.getWidth(), cvs.getHeight(), Game.instance().window().getDevicePixelRatio());
						this.restoreAllStates();
						break;
					
					case CMD_CLIP_SET:
						if(!state.clip.isNull && state.clip.isEmpty()) {
							i += 4;
							break;
						}
						
						float x = commandStack[i++];
						float y = commandStack[i++];
						float w = commandStack[i++];
						float h = commandStack[i++];
						
						if(!state.clip.isNull) {
							this._vec3.set(state.clip.x, state.clip.y, 1.0f);
							
							Matrix3f pxform = this._mat3_1;
							Matrix3f invxform = this._mat3_2;
							
							state.transform.invert(pxform);
							state.clip.xform.invert(invxform);
							pxform.mul(invxform);
							
							float textentx = state.clip.extent.x * Math.abs(pxform.m00()) + state.clip.extent.y * Math.abs(pxform.m10());
							float textenty = state.clip.extent.x * Math.abs(pxform.m01()) + state.clip.extent.y * Math.abs(pxform.m11());
							
							float bx = pxform.m20() - textentx;
							float by = pxform.m21() - textenty;
							float bw = textentx * 2;
							float bh = textenty * 2;
							
							MathUtils.intersectRects(this._f4, x, y, w, h, bx, by, bw, bh);
							
							x = this._f4[0];
							y = this._f4[1];
							w = this._f4[2];
							h = this._f4[3];
						}
						
						state.clip.x = x;
						state.clip.y = y;
						state.clip.w = w;
						state.clip.h = h;
						
						state.clip.xform.set(state.transform);
						MathUtils.translate(state.clip.xform, state.clip.x + state.clip.w * 0.5f, state.clip.y + state.clip.h * 0.5f);
						state.clip.xform.invert();
						
						state.clip.extent.x = state.clip.w * 0.5f;
						state.clip.extent.y = state.clip.h * 0.5f;
						
						this.gtx.currentTransform(state.clip.settime_xform);
						this.gtx.scissor(state.clip.x, state.clip.y, state.clip.w, state.clip.h);
						
						state.clip.isNull = false;
						break;
					
					case CMD_CLIP_RESET:
						this.gtx.resetScissor();
						state.clip.isNull = true;
						break;
					
					case CMD_PATH_BEGIN:
						this.gtx.beginPath();
						break;
					
					case CMD_PATH_MOVETO:
						this.gtx.moveTo(commandStack[i++], commandStack[i++]);
						break;
					
					case CMD_PATH_LINETO:
						this.gtx.lineTo(commandStack[i++], commandStack[i++]);
						break;
					
					case CMD_PATH_BEZIERTO:
						this.gtx.bezierTo(commandStack[i++], commandStack[i++], commandStack[i++], commandStack[i++], commandStack[i++], commandStack[i++]);
						break;
					
					case CMD_PATH_QUADTO:
						this.gtx.quadTo(commandStack[i++], commandStack[i++], commandStack[i++], commandStack[i++]);
						break;
					
					case CMD_PATH_ARCTO:
						this.gtx.arcTo(commandStack[i++], commandStack[i++], commandStack[i++], commandStack[i++], commandStack[i++]);
						break;
					
					case CMD_PATH_CLOSE:
						this.gtx.closePath();
						break;
					
					case CMD_PATH_SET_WINDING:
						this.gtx.pathWinding(commandStack[i++] == CONST_CW ? NVG_CW : NVG_CCW);
						break;
					
					case CMD_PATH_ARC:
						this.gtx.arc(commandStack[i++], commandStack[i++], commandStack[i++], commandStack[i++] / 180 * 3.141592f, commandStack[i++] / 180 * 3.141592f, commandStack[i++] == CONST_CW ? NVG_CW : NVG_CCW);
						break;
					
					case CMD_PATH_RECT:
						this.gtx.rect(commandStack[i++], commandStack[i++], commandStack[i++], commandStack[i++]);
						break;
					
					case CMD_PATH_ROUNDEDRECT:
						this.gtx.roundedRect(commandStack[i++], commandStack[i++], commandStack[i++], commandStack[i++], commandStack[i++]);
						break;
					
					case CMD_PATH_ELLIPSE:
						this.gtx.ellipse(commandStack[i++], commandStack[i++], commandStack[i++], commandStack[i++]);
						break;
					
					case CMD_PATH_CIRCLE:
						this.gtx.circle(commandStack[i++], commandStack[i++], commandStack[i++]);
						break;
					
					case CMD_PATH_FILL:
						this.gtx.fill();
						break;
					
					case CMD_PATH_STROKE:
						this.gtx.stroke();
						break;
					
					case CMD_TEXT_SET_FONT:
						state.font = (Font) args[argI++];
						break;
					
					case CMD_TEXT_FILL:
						if(state.clip != null && state.clip.isEmpty()) {
							argI += 1;
							i += 2;
							break;
						}
						
						this.saveAllStates();
						this.gtx.endFrame();
						glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
						this.textRenderer.render2D((CharSequence) args[argI++], commandStack[i++], commandStack[i++], state.transform, cvs.getWidth(), cvs.getHeight(), state.font, state.textAlign, state.textBaseline, state.fill, null, state.clip, state.globalAlpha, false);
						OGLProgram.unuseAll();
						this.gtx.beginFrame(cvs.getWidth(), cvs.getHeight(), Game.instance().window().getDevicePixelRatio());
						this.restoreAllStates();
						break;
					
					case CMD_TEXT_STROKE:
						if(!state.clip.isNull && state.clip.isEmpty()) {
							argI += 1;
							i += 2;
							break;
						}
						
						this.saveAllStates();
						this.gtx.endFrame();
						glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
						this.textRenderer.render2D((CharSequence) args[argI++], commandStack[i++], commandStack[i++], state.transform, cvs.getWidth(), cvs.getHeight(), state.font, state.textAlign, state.textBaseline, null, state.stroke, state.clip, state.globalAlpha, false);
						OGLProgram.unuseAll();
						this.gtx.beginFrame(cvs.getWidth(), cvs.getHeight(), Game.instance().window().getDevicePixelRatio());
						this.restoreAllStates();
						break;
					
					case CMD_TEXT_SET_ALIGN:
						switch((int) commandStack[i++]) {
							case CONST_TEXT_ALIGN_CENTER:
								state.textAlign = TextAlignment.CENTER;
								break;
							
							case CONST_TEXT_ALIGN_LEFT:
								state.textAlign = TextAlignment.LEFT;
								break;
							
							case CONST_TEXT_ALIGN_RIGHT:
								state.textAlign = TextAlignment.RIGHT;
								break;
						}
						break;
					
					case CMD_TEXT_SET_BASELINE:
						switch((int) commandStack[i++]) {
							case CONST_TEXT_BASELINE_TOP:
								state.textBaseline = TextBaseline.TOP;
								break;
							
							case CONST_TEXT_BASELINE_MIDDLE:
								state.textBaseline = TextBaseline.MIDDLE;
								break;
							
							case CONST_TEXT_BASELINE_BASELINE:
								state.textBaseline = TextBaseline.BASELINE;
								break;
							
							case CONST_TEXT_BASELINE_ASCENT_MIDDLE:
								state.textBaseline = TextBaseline.ASCENT_MIDDLE;
								break;
							
							case CONST_TEXT_BASELINE_BOTTOM:
								state.textBaseline = TextBaseline.BOTTOM;
								break;
						}
						break;
					
					case CMD_STROKE_LINE:
						this.gtx.strokeLine(commandStack[i++], commandStack[i++], commandStack[i++], commandStack[i++]);
						break;
					
					case CMD_STROKE_RECT:
						this.gtx.strokeRect(commandStack[i++], commandStack[i++], commandStack[i++], commandStack[i++]);
						break;
					
					case CMD_STROKE_ROUNDED_RECT:
						this.gtx.strokeRoundedRect(commandStack[i++], commandStack[i++], commandStack[i++], commandStack[i++], commandStack[i++]);
						break;
					
					case CMD_FILL_RECT:
						this.gtx.fillRect(commandStack[i++], commandStack[i++], commandStack[i++], commandStack[i++]);
						break;
					
					case CMD_FILL_ROUNDED_RECT:
						this.gtx.fillRoundedRect(commandStack[i++], commandStack[i++], commandStack[i++], commandStack[i++], commandStack[i++]);
						break;
				}
			}
			// Clear
			cvs.getContext2D().discardAll();
			
			for(int i = 0; i < saveCount; i++) {
				this.gtx.restore();
			}
			
			while(!this.stateDeque.isEmpty()) {
				this.freeState(this.stateDeque.pop());
			}
			
			this.gtx.endFrame();
			target.getFBO().unbind();
			glViewport(0, 0, this.getWidth(), this.getHeight());
			
			this.updateStateAfterNVG();
		}
		
		OGLStateManager.INSTANCE.blend(true);
		
		OGLFramebuffer2D.unbindAll();
		
		// Now render the texture!
		Camera cam = sce.getCamera();
		
		this.textureQuadProg.use();
		this.textureQuadProg.loadToUniform("projView", cam.getProjViewMatrix(true));
		this.textureQuadProg.loadToUniform("model", cvs.getWorldMatrix(true));
		this.textureQuadProg.loadToUniform("color", target.getColorTexture(), 0);
		this.textureQuadProg.loadToUniform("globalAlpha", 1.0f);
		this.textureQuadProg.loadToUniform("flipY", !cvs.isFlipY());
		
		this.quadVAO.bind();
		glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
		this.quadVAO.unbind();
		this.textureQuadProg.unuse();
	}
	
	public void dispose() {
		NVGContext.delete();
		
		this.textRenderer.dispose();
		this.textureQuadProg.dispose();
		this.advancedTextureProg.dispose();
		this.quadVAO.dispose();
		
		for(CanvasComponent cvs : this.canvases.keySet()) {
			this.canvases.get(cvs).dispose(true);
		}
		
		this.canvases.clear();
	}
	
	private static class GraphicsContext2DState {
		private float[] _float6 = new float[6];
		
		public Vector4f fill = new Vector4f(1, 1, 1, 1);
		public Vector4f stroke = new Vector4f(0, 0, 0, 1);
		public float strokeWidth = 1;
		public float miterLimit = 10;
		public int lineJoin = NVG_MITER;
		public int lineCap = NVG_BUTT;
		public float globalAlpha = 1.0f;
		public Matrix3f transform = new Matrix3f();
		public OGLClipping clip = new OGLClipping();
		public Font font = null;
		public TextAlignment textAlign = TextAlignment.LEFT;
		public TextBaseline textBaseline = TextBaseline.BASELINE;
		
		public GraphicsContext2DState(GraphicsContext2DState s) {
			this.init(s);
		}
		
		public GraphicsContext2DState init(GraphicsContext2DState s) {
			if(s != null) {
				this.fill.set(s.fill);
				this.stroke.set(s.stroke);
				this.strokeWidth = s.strokeWidth;
				this.miterLimit = s.miterLimit;
				this.lineJoin = s.lineJoin;
				this.lineCap = s.lineCap;
				this.globalAlpha = s.globalAlpha;
				this.transform.set(s.transform);
				this.clip.set(s.clip);
				this.font = s.font;
				this.textAlign = s.textAlign;
				this.textBaseline = s.textBaseline;
			} else {
				this.fill.set(1, 1, 1, 1);
				this.stroke.set(0, 0, 0, 1);
				this.strokeWidth = 1;
				this.miterLimit = 10;
				this.lineJoin = NVG_MITER;
				this.lineCap = NVG_BUTT;
				this.globalAlpha = 1.0f;
				this.transform.identity();
				this.clip.isNull = true;
				this.font = null;
				this.textAlign = TextAlignment.LEFT;
				this.textBaseline = TextBaseline.BASELINE;
				
				this._float6[0] = this._float6[1] = this._float6[2] = this._float6[3] = this._float6[4] = this._float6[5] = 0;
			}
			
			return this;
		}
		
		public void saveMatrix(NVGContext gtx) {
			gtx.currentTransform(this._float6);
		}
		
		public void apply(NVGContext gtx) {
			if(!this.clip.isNull) {
				gtx.resetTransform();
				gtx.transform(this.clip.settime_xform[0], this.clip.settime_xform[1], this.clip.settime_xform[2], this.clip.settime_xform[3], this.clip.settime_xform[4], this.clip.settime_xform[5]);
				gtx.scissor(this.clip.x, this.clip.y, this.clip.w, this.clip.h);
			}
			
			gtx.resetTransform();
			gtx.transform(this._float6[0], this._float6[1], this._float6[2], this._float6[3], this._float6[4], this._float6[5]);
			
			gtx.fillColor(NVGColors.INSTANCE.get(this.fill.x, this.fill.y, this.fill.z, this.fill.w));
			gtx.strokeColor(NVGColors.INSTANCE.get(this.stroke.x, this.stroke.y, this.stroke.z, this.stroke.w));
			gtx.strokeWidth(this.strokeWidth);
			gtx.miterLimit(this.miterLimit);
			gtx.lineJoin(this.lineJoin);
			gtx.lineCap(this.lineCap);
			gtx.globalAlpha(this.globalAlpha);
			
			// other stuff of nvg is for font, but we're drawing text ourselves
		}
	}
}
