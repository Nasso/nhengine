package io.github.nasso.nhengine.opengl;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

import io.github.nasso.nhengine.graphics.fx.BoxBlur;
import io.github.nasso.nhengine.graphics.fx.PostEffect;

public class OGLBoxBlur extends OGLPostEffect {
	private OGLBoxBlurProgram program;
	private OGLTextureQuadProgram fullProg;
	
	private OGLFramebuffer2D fbo1;
	private OGLTexture2D fbo1Target;
	
	private OGLFramebuffer2D fbo2;
	private OGLTexture2D fbo2Target;
	
	private int size = 1;
	private int downsample = 4;
	private int iterations = 2;
	
	public OGLBoxBlur(int w, int h) throws IOException {
		this.program = new OGLBoxBlurProgram();
		this.fullProg = new OGLTextureQuadProgram();
		
		this.fullProg.use();
		this.fullProg.loadToUniform("copyDepth", false);
		this.fullProg.unuse();
		
		this.fbo1 = new OGLFramebuffer2D();
		this.fbo2 = new OGLFramebuffer2D();
		
		OGLTexture2D.Builder targetBuilder = new OGLTexture2D.Builder().width(Math.max(1, w / this.downsample)).height(Math.max(1, h / this.downsample)).internalFormat(GL11.GL_RGBA16).type(GL11.GL_FLOAT).magFilter(GL11.GL_LINEAR).minFilter(GL11.GL_NEAREST).wrapS(GL14.GL_MIRRORED_REPEAT).wrapT(GL14.GL_MIRRORED_REPEAT);
		this.fbo1Target = targetBuilder.build();
		this.fbo2Target = targetBuilder.build();
		
		this.fbo1.bind();
		this.fbo1.bindTexture(GL30.GL_COLOR_ATTACHMENT0, this.fbo1Target);
		this.fbo2.bind();
		this.fbo2.bindTexture(GL30.GL_COLOR_ATTACHMENT0, this.fbo2Target);
		this.fbo2.unbind();
		
		this.setWidth(w);
		this.setHeight(h);
	}
	
	public void updateSize(int w, int h) {
		this.setWidth(w);
		this.setHeight(h);
		
		this.refreshFBOs();
	}
	
	private void refreshFBOs() {
		int newWidth = Math.max(1, this.getWidth() / this.downsample);
		int newHeight = Math.max(1, this.getHeight() / this.downsample);
		
		this.fbo1Target.setWidth(newWidth);
		this.fbo1Target.setHeight(newHeight);
		
		this.fbo2Target.setWidth(newWidth);
		this.fbo2Target.setHeight(newHeight);
		
		this.fbo1Target.update();
		this.fbo2Target.update();
	}
	
	public void apply(OGLTexture2D sourceColor, OGLTexture2D sourceDepth, OGLFramebuffer2D dest) {
		int downWidth = Math.max(1, this.getWidth() / this.downsample);
		int downHeight = Math.max(1, this.getHeight() / this.downsample);
		
		this.program.use();
		this.program.loadToUniform("size", this.size);
		this.program.loadToUniform("textureDim", downWidth, downHeight);
		
		GL11.glViewport(0, 0, downWidth, downHeight);
		this.fbo1.bind();
		this.program.loadToUniform("color", sourceColor, 0);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		
		OGLTexture2D finalTexture = this.fbo1Target;
		for(int i = 0; i < this.iterations - 1; i++) {
			OGLTexture2D itDownSource = this.fbo1Target;
			OGLFramebuffer2D itDownDest = this.fbo2;
			
			// Ping pong
			if(finalTexture == this.fbo1Target) finalTexture = this.fbo2Target;
			else if(finalTexture == this.fbo2Target) {
				finalTexture = this.fbo1Target;
				
				itDownSource = this.fbo2Target;
				itDownDest = this.fbo1;
			} else {
				System.out.println("We lost the ball during the Ping-Pong match with FBOs in box blur.");
				continue;
			}
			
			itDownDest.bind();
			this.program.loadToUniform("color", itDownSource, 0);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		}
		
		GL11.glViewport(0, 0, this.getWidth(), this.getHeight());
		dest.bind();
		this.fullProg.use();
		this.fullProg.loadToUniform("color", finalTexture, 0);
		
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		this.fullProg.unuse();
		dest.unbind();
	}
	
	public void dispose() {
		this.program.dispose();
		this.fullProg.dispose();
		
		this.fbo1.dispose();
		this.fbo1Target.dispose();
		
		this.fbo2.dispose();
		this.fbo2Target.dispose();
	}
	
	public int getSize() {
		return this.size;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public int getDownsample() {
		return this.downsample;
	}
	
	public void setDownsample(int downsample) {
		if(this.downsample != downsample) {
			this.downsample = downsample;
			
			this.refreshFBOs();
		}
	}
	
	public int getIterations() {
		return this.iterations;
	}
	
	public void setIterations(int iterations) {
		this.iterations = iterations;
	}
	
	public void update(PostEffect e) {
		if(e instanceof BoxBlur) {
			BoxBlur effect = (BoxBlur) e;
			
			this.setSize(effect.getSize());
			this.setDownsample(effect.getDownsample());
			this.setIterations(effect.getIterations());
		}
	}
}
