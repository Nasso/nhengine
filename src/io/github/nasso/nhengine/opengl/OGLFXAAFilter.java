package io.github.nasso.nhengine.opengl;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import io.github.nasso.nhengine.graphics.fx.FXAA;
import io.github.nasso.nhengine.graphics.fx.PostEffect;

public class OGLFXAAFilter extends OGLPostEffect {
	private OGLFXAAFilterProgram program;
	
	private float reduceMin = 1.0f / 128.0f;
	private float reduceMul = 1.0f / 8.0f;
	private float spanMax = 8.0f;
	
	public OGLFXAAFilter(int width, int height) throws IOException {
		this.program = new OGLFXAAFilterProgram();
		
		this.setWidth(width);
		this.setHeight(height);
	}
	
	public void apply(OGLTexture2D sourceColor, OGLTexture2D sourceDepth, OGLFramebuffer2D dest) {
		dest.bind();
		
		this.program.use();
		this.program.loadToUniform("color", sourceColor, 0);
		this.program.loadToUniform("resolution", this.getWidth(), this.getHeight());
		this.program.loadToUniform("reduceMin", this.reduceMin);
		this.program.loadToUniform("reduceMul", this.reduceMul);
		this.program.loadToUniform("spanMax", this.spanMax);
		
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		this.program.unuse();
		
		dest.unbind();
	}
	
	public void dispose() {
		this.program.dispose();
	}
	
	public void update(PostEffect e) {
		if(e instanceof FXAA) {
			FXAA effect = (FXAA) e;
			
			this.reduceMul = effect.getReduceMul();
			this.reduceMin = effect.getReduceMin();
			this.spanMax = effect.getSpanMax();
		}
	}
	
	public void updateSize(int width, int height) {
		this.setWidth(width);
		this.setHeight(height);
	}
}
