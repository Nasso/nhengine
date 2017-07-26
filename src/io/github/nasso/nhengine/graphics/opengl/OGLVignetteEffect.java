package io.github.nasso.nhengine.graphics.opengl;

import java.io.IOException;

import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import io.github.nasso.nhengine.graphics.fx.PostEffect;
import io.github.nasso.nhengine.graphics.fx.Vignette;

public class OGLVignetteEffect extends OGLPostEffect {
	private OGLVignetteEffectProgram program;
	
	private Vector4f vignetteColor = new Vector4f();
	private Vector2f parameters = new Vector2f();
	
	public OGLVignetteEffect() throws IOException {
		this.program = new OGLVignetteEffectProgram();
	}
	
	public void apply(OGLTexture2D sourceColor, OGLTexture2D sourceDepth, OGLFramebuffer2D dest) {
		dest.bind();
		
		this.program.use();
		this.program.loadToUniform("color", sourceColor, 0);
		this.program.loadToUniform("vignetteColor", this.vignetteColor);
		this.program.loadToUniform("parameters", this.parameters);
		
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		this.program.unuse();
		
		dest.unbind();
	}
	
	public void dispose() {
		this.program.dispose();
	}
	
	public void update(PostEffect e) {
		if(e instanceof Vignette) {
			Vignette ve = (Vignette) e;
			
			this.vignetteColor.set(ve.getColor(), ve.getOpacity());
			this.parameters.set(ve.getDistance(), ve.getSmoothness());
		}
	}
}
