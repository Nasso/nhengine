package io.github.nasso.nhengine.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL31.*;

import java.io.IOException;

import io.github.nasso.nhengine.component.SpriteComponent;
import io.github.nasso.nhengine.component.TileMapComponent;
import io.github.nasso.nhengine.level.Camera;
import io.github.nasso.nhengine.level.Scene;

public class OGLSpriteRenderer extends OGLInstancedComponentRenderer<SpriteComponent> {
	private OGLTextures textures = OGLTextures.get();
	
	public static final int MAX_INSTANCES = 256;
	
	private float[] sprite_instQuickTransformWHDepth = new float[MAX_INSTANCES * 3];
	private float[] sprite_instSpriteSubRegion = new float[MAX_INSTANCES * 4];
	private float[] sprite_instDiffuseColorAlpha = new float[MAX_INSTANCES * 4];
	private float[] sprite_instModelMatrix = new float[MAX_INSTANCES * 16];
	private float[] sprite_gridXYPositionOffset = new float[MAX_INSTANCES * 2];
	
	private OGLSpriteProgram gbufferSpriteProgram;
	
	private OGLVertexArray spriteRectVAO;
	
	private OGLArrayBuffer sprite_instQuickTransformWHDepthVBO; // vec4
	private OGLArrayBuffer sprite_instSpriteSubRegionVBO; // vec4
	private OGLArrayBuffer sprite_instDiffuseColorAlphaVBO; // vec4
	private OGLArrayBuffer sprite_instModelMatrixVBO; // mat4
	private OGLArrayBuffer sprite_gridXYPositionOffsetVBO; // vec2
	
	public OGLSpriteRenderer(OGLArrayBuffer rectPosVBO, int width, int height) throws IOException {
		super(width, height);
		
		this.gbufferSpriteProgram = new OGLSpriteProgram();
		
		this.sprite_instQuickTransformWHDepthVBO = new OGLArrayBuffer(MAX_INSTANCES * 12, GL_STREAM_DRAW);
		this.sprite_instSpriteSubRegionVBO = new OGLArrayBuffer(MAX_INSTANCES * 16, GL_STREAM_DRAW);
		this.sprite_instDiffuseColorAlphaVBO = new OGLArrayBuffer(MAX_INSTANCES * 16, GL_STREAM_DRAW);
		this.sprite_instModelMatrixVBO = new OGLArrayBuffer(MAX_INSTANCES * 64, GL_STREAM_DRAW);
		this.sprite_gridXYPositionOffsetVBO = new OGLArrayBuffer(MAX_INSTANCES * 8, GL_STREAM_DRAW);
		
		// Sprites
		this.spriteRectVAO = new OGLVertexArray();
		
		this.spriteRectVAO.bind();
		
		this.spriteRectVAO.setAttribLocationEnabled(0, true);
		this.spriteRectVAO.loadVBOToAttrib(0, 2, rectPosVBO);
		
		this.spriteRectVAO.setAttribLocationEnabled(1, true);
		this.spriteRectVAO.loadVBOToAttrib(1, 3, this.sprite_instQuickTransformWHDepthVBO, 0, 0, 1);
		
		this.spriteRectVAO.setAttribLocationEnabled(2, true);
		this.spriteRectVAO.loadVBOToAttrib(2, 4, this.sprite_instSpriteSubRegionVBO, 0, 0, 1);
		
		this.spriteRectVAO.setAttribLocationEnabled(3, true);
		this.spriteRectVAO.loadVBOToAttrib(3, 4, this.sprite_instDiffuseColorAlphaVBO, 0, 0, 1);
		
		this.spriteRectVAO.setAttribLocationEnabled(4, true);
		this.spriteRectVAO.loadVBOToAttrib(4, 4, this.sprite_instModelMatrixVBO, 64, 0, 1);
		this.spriteRectVAO.setAttribLocationEnabled(5, true);
		this.spriteRectVAO.loadVBOToAttrib(5, 4, this.sprite_instModelMatrixVBO, 64, 16, 1);
		this.spriteRectVAO.setAttribLocationEnabled(6, true);
		this.spriteRectVAO.loadVBOToAttrib(6, 4, this.sprite_instModelMatrixVBO, 64, 32, 1);
		this.spriteRectVAO.setAttribLocationEnabled(7, true);
		this.spriteRectVAO.loadVBOToAttrib(7, 4, this.sprite_instModelMatrixVBO, 64, 48, 1);
		
		this.spriteRectVAO.setAttribLocationEnabled(8, true);
		this.spriteRectVAO.loadVBOToAttrib(8, 2, this.sprite_gridXYPositionOffsetVBO, 0, 0, 1);
		
		OGLVertexArray.unbindAll();
		OGLArrayBuffer.unbindAll();
	}
	
	private void renderPart(Camera cam, OGLTexture2D tex, int count) {
		this.gbufferSpriteProgram.loadToUniform("diffuseTexture", tex, 0);
		this.gbufferSpriteProgram.loadToUniform("projView", cam.getProjViewMatrix(true));
		
		this.spriteRectVAO.bind();
		glDrawArraysInstanced(GL_TRIANGLE_STRIP, 0, 4, count);
		this.spriteRectVAO.unbind();
	}
	
	private void updateVBOs() {
		this.sprite_instQuickTransformWHDepthVBO.loadSubData(this.sprite_instQuickTransformWHDepth);
		this.sprite_instSpriteSubRegionVBO.loadSubData(this.sprite_instSpriteSubRegion);
		this.sprite_instDiffuseColorAlphaVBO.loadSubData(this.sprite_instDiffuseColorAlpha);
		this.sprite_instModelMatrixVBO.loadSubData(this.sprite_instModelMatrix);
		this.sprite_gridXYPositionOffsetVBO.loadSubData(this.sprite_gridXYPositionOffset);
		OGLArrayBuffer.unbindAll();
		
		OGLManager.fastCheckError("sprite vbo update");
	}
	
	private void prepareSprite(SpriteComponent comp, float[] gridXYPositionOffset, int ic, int i) {
		this.sprite_instQuickTransformWHDepth[i * 3] = comp.getWidth();
		this.sprite_instQuickTransformWHDepth[i * 3 + 1] = comp.getHeight();
		this.sprite_instQuickTransformWHDepth[i * 3 + 2] = comp.getWorldDepth();
		
		this.sprite_instSpriteSubRegion[i * 4] = comp.getViewRectangle().x();
		this.sprite_instSpriteSubRegion[i * 4 + 1] = comp.getViewRectangle().y();
		this.sprite_instSpriteSubRegion[i * 4 + 2] = comp.getViewRectangle().z();
		this.sprite_instSpriteSubRegion[i * 4 + 3] = comp.getViewRectangle().w();
		
		this.sprite_instDiffuseColorAlpha[i * 4] = comp.getColorTeint().x();
		this.sprite_instDiffuseColorAlpha[i * 4 + 1] = comp.getColorTeint().y();
		this.sprite_instDiffuseColorAlpha[i * 4 + 2] = comp.getColorTeint().z();
		this.sprite_instDiffuseColorAlpha[i * 4 + 3] = comp.isOpaque() ? 1.0f : comp.getOpacity();
		
		if(gridXYPositionOffset != null) {
			this.sprite_gridXYPositionOffset[i * 2] = gridXYPositionOffset[ic * 2];
			this.sprite_gridXYPositionOffset[i * 2 + 1] = gridXYPositionOffset[ic * 2 + 1];
		} else {
			this.sprite_gridXYPositionOffset[i * 2] = 0;
			this.sprite_gridXYPositionOffset[i * 2 + 1] = 0;
		}
		
		comp.getWorldMatrix(true).get(this.sprite_instModelMatrix, i * 16);
	}
	
	private void renderSpriteInstanceBuffers(Camera cam, SpriteComponent[] comps, float[] gridXYPositionOffset, int count) {
		boolean translucent = false;
		
		int x = 0;
		for(int i = 0; i < count; i++) {
			SpriteComponent c = comps[i];
			if(c == null || !c.isEnabled() || c.getSprite() == null || (!c.isOpaque() && c.getOpacity() == 0.0f)) continue;
			
			this.prepareSprite(c, gridXYPositionOffset, i, x);
			
			translucent |= (!c.isOpaque() || c.getOpacity() >= 1.0f);
			
			x++;
			
			if(x == MAX_INSTANCES) {
				OGLStateManager.INSTANCE.blend(translucent);
				this.updateVBOs();
				this.renderPart(cam, this.textures.update(comps[0].getSprite()), x);
				x = 0;
				translucent = false;
			}
		}
		
		if(x > 0) {
			OGLStateManager.INSTANCE.blend(translucent);
			this.updateVBOs();
			this.renderPart(cam, this.textures.update(comps[0].getSprite()), x);
		}
	}
	
	public void render(Scene sce, SpriteComponent comp) {
		OGLStateManager.INSTANCE.blend(!comp.isOpaque());
		
		this.gbufferSpriteProgram.use();
		this.gbufferSpriteProgram.loadToUniform("isTileMap", false);
		
		this.prepareSprite(comp, null, 0, 0);
		this.updateVBOs();
		this.renderPart(sce.getCamera(), this.textures.update(comp.getSprite()), 1);
		
		OGLManager.fastCheckError("single sprite render");
	}
	
	public void render(Scene sce, TileMapComponent tileMap, SpriteComponent[] comps, float[] gridXYPositionOffset, int count) {
		this.gbufferSpriteProgram.use();
		this.gbufferSpriteProgram.loadToUniform("isTileMap", tileMap != null);
		
		if(tileMap != null) {
			this.gbufferSpriteProgram.loadToUniform("tileMapModel", tileMap.getWorldMatrix(true));
		}
		
		this.renderSpriteInstanceBuffers(sce.getCamera(), comps, gridXYPositionOffset, count);
		
		OGLManager.fastCheckError("multiple sprite render");
	}
	
	public void render(Scene sce, SpriteComponent[] comps, float[] gridXYPositionOffset, int count) {
		this.render(sce, null, comps, gridXYPositionOffset, count);
	}
	
	public void dispose() {
		this.sprite_instQuickTransformWHDepth = this.sprite_instSpriteSubRegion = this.sprite_instDiffuseColorAlpha = this.sprite_instModelMatrix = null;
		
		this.gbufferSpriteProgram.dispose();
		
		this.spriteRectVAO.dispose();
		
		this.sprite_instQuickTransformWHDepthVBO.dispose();
		this.sprite_instDiffuseColorAlphaVBO.dispose();
		this.sprite_instSpriteSubRegionVBO.dispose();
		this.sprite_instModelMatrixVBO.dispose();
		
		this.gbufferSpriteProgram.dispose();
	}
}
