package io.github.nasso.nhengine.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL31.*;

import java.io.IOException;

import io.github.nasso.nhengine.component.TileMapComponent;
import io.github.nasso.nhengine.component.TileMapComponent.TileSet;
import io.github.nasso.nhengine.level.Scene;

public class OGLTileMapRenderer extends OGLComponentRenderer<TileMapComponent> {
	public static final int MAX_INSTANCES = 256;
	
	private OGLTileMapProgram program;
	private OGLVertexArray vao;
	
	private float[] arr_tileSetLocationMapPosition = new float[MAX_INSTANCES * 4];
	private OGLArrayBuffer vbo_tileSetLocationMapPosition;
	
	public OGLTileMapRenderer(OGLArrayBuffer rectPosVBO, int width, int height) throws IOException {
		super(width, height);
		
		this.vbo_tileSetLocationMapPosition = new OGLArrayBuffer(this.arr_tileSetLocationMapPosition.length * 4, GL_STREAM_DRAW);
		
		this.program = new OGLTileMapProgram();
		this.vao = new OGLVertexArray();
		
		this.vao.bind();
		
		this.vao.setAttribLocationEnabled(0, true);
		this.vao.loadVBOToAttrib(0, 2, rectPosVBO);
		
		this.vao.setAttribLocationEnabled(1, true);
		this.vao.loadVBOToAttrib(1, 4, this.vbo_tileSetLocationMapPosition, 0, 0, 1);
		
		OGLVertexArray.unbindAll();
		OGLArrayBuffer.unbindAll();
	}
	
	private void renderBatch(int count) {
		this.vbo_tileSetLocationMapPosition.loadSubData(this.arr_tileSetLocationMapPosition);
		glDrawArraysInstanced(GL_TRIANGLE_STRIP, 0, 4, count);
	}
	
	public void render(Scene sce, TileMapComponent comp) {
		OGLStateManager.INSTANCE.blend(!comp.isOpaque());
		
		this.program.use();
		this.program.loadToUniform("tilesetSize", comp.getTileSet().getColumns(), comp.getTileSet().getRows());
		this.program.loadToUniform("tileWidthHeightDepth", comp.getCellWidth(), comp.getCellHeight(), comp.getWorldDepth());
		this.program.loadToUniform("opacity", comp.isOpaque() ? 1.0f : comp.getOpacity());
		this.program.loadToUniform("model", comp.getWorldMatrix(true));
		this.program.loadToUniform("isometric", comp.isIsometric());
		
		this.program.loadToUniform("diffuseTexture", OGLTextures.get().update(comp.getTileSet().getTexture()), 0);
		this.program.loadToUniform("projView", sce.getCamera().getProjViewMatrix(true));
		
		this.vao.bind();
		
		float gridSizeX = comp.getMapSizeX() * sce.getCamera().getScale();
		float gridSizeY = comp.getMapSizeY() * sce.getCamera().getScale();
		
		int cellStartX = (int) (sce.getCamera().getPosition().x / comp.getCellWidth() - gridSizeX) - 1;
		int cellStartY = (int) (sce.getCamera().getPosition().y / comp.getCellHeight() - gridSizeY / sce.getCamera().getAspectRatio()) - 1;
		
		int cellEndX = cellStartX + ((int) gridSizeX + 1) * 2 + 1;
		int cellEndY = cellStartY + ((int) (gridSizeY / sce.getCamera().getAspectRatio()) + 1) * 2 + 2;
		
		cellStartX = Math.max(cellStartX, 0);
		cellStartY = Math.max(cellStartY, 0);
		
		cellEndX = Math.min(cellEndX, comp.getMapSizeX());
		cellEndY = Math.min(cellEndY, comp.getMapSizeY());
		
		TileSet tileset = comp.getTileSet();
		int[] tiles = comp.getTiles();
		
		// Render the stuff MAX_INSTANCES by MAX_INSTANCES
		int bInstanceID = 0;
		
		for(int cellX = cellStartX; cellX < cellEndX; cellX++) {
			for(int cellY = cellStartY; cellY < cellEndY; cellY++) {
				int tileID = tiles[cellY * comp.getMapSizeX() + cellX];
				if(tileID == -1) continue;
				
				int locx = tileset.getTileColumn(tileID);
				int locy = tileset.getTileRow(tileID);
				
				this.arr_tileSetLocationMapPosition[bInstanceID * 4] = locx;
				this.arr_tileSetLocationMapPosition[bInstanceID * 4 + 1] = locy;
				this.arr_tileSetLocationMapPosition[bInstanceID * 4 + 2] = cellX;
				this.arr_tileSetLocationMapPosition[bInstanceID * 4 + 3] = cellY;
				
				bInstanceID++;
				
				if(bInstanceID >= MAX_INSTANCES) {
					// At this point, bInstanceID == rendered tile count
					this.renderBatch(bInstanceID);
					
					bInstanceID = 0;
				}
			}
		}
		
		if(bInstanceID > 0) {
			this.renderBatch(bInstanceID);
		}
		
		OGLVertexArray.unbindAll();
		OGLArrayBuffer.unbindAll();
		
		OGLManager.fastCheckError("tilemap render");
	}
	
	public void dispose() {
		this.arr_tileSetLocationMapPosition = null;
		
		this.vbo_tileSetLocationMapPosition.dispose();
		
		this.program.dispose();
		this.vao.dispose();
	}
}
