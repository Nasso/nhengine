package io.github.nasso.nhengine.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;

import io.github.nasso.nhengine.component.SpriteComponent;
import io.github.nasso.nhengine.level.Scene;

public class OGLSpriteRenderer extends OGLComponentRenderer<SpriteComponent> {
	private OGLSpriteProgram program;
	
	private OGLVertexArray spriteRectVAO;
	
	public OGLSpriteRenderer(OGLArrayBuffer rectPosVBO, int width, int height) throws IOException {
		super(width, height);
		
		this.program = new OGLSpriteProgram();
		
		// Sprites
		this.spriteRectVAO = new OGLVertexArray();
		
		this.spriteRectVAO.bind();
		
		this.spriteRectVAO.setAttribLocationEnabled(0, true);
		this.spriteRectVAO.loadVBOToAttrib(0, 2, rectPosVBO);
		
		OGLVertexArray.unbindAll();
		OGLArrayBuffer.unbindAll();
	}
	
	public void render(Scene sce, SpriteComponent comp) {
		OGLStateManager.INSTANCE.blend(!comp.isOpaque());
		
		this.program.use();
		this.program.loadToUniform("widthHeightDepth", comp.getWidth(), comp.getHeight(), comp.getWorldDepth());
		this.program.loadToUniform("spriteSubRegion", comp.getViewRectangle());
		this.program.loadToUniform("diffuseColorAlpha", comp.getColorTeint().x(), comp.getColorTeint().y(), comp.getColorTeint().z(), comp.isOpaque() ? 1.0f : comp.getOpacity());
		this.program.loadToUniform("model", comp.getWorldMatrix(true));
		
		this.program.loadToUniform("diffuseTexture", OGLTextures.get().update(comp.getTexture()), 0);
		this.program.loadToUniform("projView", sce.getCamera().getProjViewMatrix(true));
		
		this.spriteRectVAO.bind();
		glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
		this.spriteRectVAO.unbind();
		
		OGLManager.fastCheckError("sprite render");
	}
	
	public void dispose() {
		this.program.dispose();
		
		this.spriteRectVAO.dispose();
		
		this.program.dispose();
	}
}
