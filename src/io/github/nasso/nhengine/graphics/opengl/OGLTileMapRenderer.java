package io.github.nasso.nhengine.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL31.*;

import java.io.IOException;

import org.joml.Vector3f;

import io.github.nasso.nhengine.component.TileMapComponent;
import io.github.nasso.nhengine.component.TileMapComponent.TileSet;
import io.github.nasso.nhengine.level.Camera;
import io.github.nasso.nhengine.level.Scene;

public class OGLTileMapRenderer extends OGLComponentRenderer<TileMapComponent> {
	public static final int MAX_INSTANCES = 256;
	
	private Vector3f _vec3 = new Vector3f();
	
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
		
		Camera cam = sce.getCamera();
		float xfov = cam.getFieldOfView();
		float yfov = xfov / cam.getAspectRatio();
		
		comp.getWorldPosition(this._vec3);
		float wMapX = this._vec3.x;
		float wMapY = this._vec3.y;
		
		this.program.use();
		this.program.loadToUniform("tileWidthHeightDepth", comp.getCellWidth(), comp.getCellHeight(), comp.getWorldDepth());
		this.program.loadToUniform("model", comp.getWorldMatrix(true));
		this.program.loadToUniform("isometric", comp.isIsometric());
		
		this.program.loadToUniform("projView", cam.getProjViewMatrix(true));
		
		this.vao.bind();
		
		for(int tilesetID = 0; tilesetID < comp.getTileSetCount(); tilesetID++) {
			TileSet tileset = comp.getTileSet(tilesetID);
			
			this.program.loadToUniform("tilesetSize", tileset.getColumns(), tileset.getRows());
			this.program.loadToUniform("diffuseTexture", OGLTextures.get().update(tileset.getTexture()), 0);
			
			for(int layerID = 0; layerID < comp.getLayerCount(); layerID++) {
				TileMapComponent.Layer layer = comp.getLayer(layerID);
				if(!layer.isVisible()) continue;
				
				this.program.loadToUniform("opacity", comp.isOpaque() ? 1.0f : comp.getOpacity() * layer.getOpacity());

				int cellStartX = (int) ((cam.getPosition().x - wMapX - xfov - layer.getHorizontalOffset()) / comp.getCellWidth());
				int cellStartY = (int) ((cam.getPosition().y - wMapY - yfov - layer.getVerticalOffset()) / comp.getCellHeight());
				
				int cellEndX = (int) (cellStartX + xfov * 2) + 1;
				int cellEndY = (int) (cellStartY + yfov * 2) + 1;
				
				cellStartX = Math.max(cellStartX, 0);
				cellStartY = Math.max(cellStartY, 0);
				
				cellEndX = Math.min(cellEndX, comp.getMapWidth());
				cellEndY = Math.min(cellEndY, comp.getMapHeight());
				
				int[] tiles = layer.getData();
				
				// Render the stuff MAX_INSTANCES by MAX_INSTANCES
				int bInstanceID = 0;
				
				for(int cellX = cellStartX; cellX < cellEndX; cellX++) {
					for(int cellY = cellStartY; cellY < cellEndY; cellY++) {
						int tileID = tiles[cellY * comp.getMapWidth() + cellX];
						if(tileID == -1 || !tileset.containsGlobalID(tileID)) continue;
						
						int locx = tileset.getTileColumn(tileID);
						int locy = tileset.getTileRow(tileID);
						
						this.arr_tileSetLocationMapPosition[bInstanceID * 4] = locx;
						this.arr_tileSetLocationMapPosition[bInstanceID * 4 + 1] = locy;
						this.arr_tileSetLocationMapPosition[bInstanceID * 4 + 2] = cellX + layer.getHorizontalOffset() / comp.getCellWidth();
						this.arr_tileSetLocationMapPosition[bInstanceID * 4 + 3] = cellY + layer.getVerticalOffset() / comp.getCellHeight();
						
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
			}
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
