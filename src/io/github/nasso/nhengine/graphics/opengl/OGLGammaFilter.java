package io.github.nasso.nhengine.graphics.opengl;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import io.github.nasso.nhengine.graphics.fx.Gamma;
import io.github.nasso.nhengine.graphics.fx.PostEffect;

public class OGLGammaFilter extends OGLPostEffect {
	private OGLGammaPostFXProgram program;
	
	private float gamma = 2.2f;
	
	public OGLGammaFilter() throws IOException {
		this.program = new OGLGammaPostFXProgram();
	}
	
	public void apply(OGLTexture2D sourceColor, OGLTexture2D sourceDepth, OGLFramebuffer2D dest) {
		dest.bind();
		
		this.program.use();
		this.program.loadToUniform("color", sourceColor, 0);
		this.program.loadToUniform("gamma", this.gamma);
		
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		this.program.unuse();
		
		dest.unbind();
	}
	
	public float getGamma() {
		return this.gamma;
	}
	
	public void setGamma(float gamma) {
		this.gamma = gamma;
	}
	
	public void dispose() {
		this.program.dispose();
	}
	
	public void update(PostEffect e) {
		if(e instanceof Gamma) {
			Gamma effect = (Gamma) e;
			
			this.setGamma(effect.getGamma());
		}
	}
}
