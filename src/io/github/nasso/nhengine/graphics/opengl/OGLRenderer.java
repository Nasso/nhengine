package io.github.nasso.nhengine.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import io.github.nasso.nhengine.component.CanvasComponent;
import io.github.nasso.nhengine.component.DrawableComponent;
import io.github.nasso.nhengine.component.SpriteComponent;
import io.github.nasso.nhengine.component.TileMapComponent;
import io.github.nasso.nhengine.graphics.Renderer;
import io.github.nasso.nhengine.graphics.TextureData;
import io.github.nasso.nhengine.level.Component;
import io.github.nasso.nhengine.level.Level;
import io.github.nasso.nhengine.level.Node;
import io.github.nasso.nhengine.level.Scene;

public class OGLRenderer extends Renderer {
	private OGLSpriteRenderer spriteRenderer;
	private OGLTileMapRenderer tileMapRenderer;
	private OGLCanvasRenderer canvasRenderer;
	
	private OGLArrayBuffer rectPosVBO;
	
	private List<Component> componentsPool = new ArrayList<Component>();
	
	private Comparator<Component> painterSort;
	
	public OGLRenderer(int width, int height) throws IOException {
		super(width, height);
		
		this.painterSort = (a, b) -> {
			return Float.compare(a.getWorldDepth(), b.getWorldDepth());
		};
		
		this.rectPosVBO = new OGLArrayBuffer();
		this.rectPosVBO.loadData(new float[] { 0f, 0f, 1f, 0f, 0f, 1f, 1f, 1f });
		
		this.spriteRenderer = new OGLSpriteRenderer(this.rectPosVBO, this.getWidth(), this.getHeight());
		this.tileMapRenderer = new OGLTileMapRenderer(this.rectPosVBO, this.getWidth(), this.getHeight());
		this.canvasRenderer = new OGLCanvasRenderer(this.rectPosVBO, this.getWidth(), this.getHeight());
		
		OGLArrayBuffer.unbindAll();
		
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public void setWidth(int w) {
		if(this.getWidth() == w) return;
		
		super.setWidth(w);
		
		this.updateSize();
	}
	
	public void setHeight(int h) {
		if(this.getHeight() == h) return;
		
		super.setHeight(h);
		
		this.updateSize();
	}
	
	public void updateSize(int w, int h) {
		// Ok, so this branch saves ~3 ms. That's a lot. Trust me.
		if(this.getWidth() == w && this.getHeight() == h) return;
		
		super.updateSize(w, h);
		
		this.updateSize();
	}
	
	public void updateSize() {
		GL11.glViewport(0, 0, this.getWidth(), this.getHeight());
		
		this.spriteRenderer.resize(this.getWidth(), this.getHeight());
		this.canvasRenderer.resize(this.getWidth(), this.getHeight());
	}
	
	private void renderComponent(Scene sce, Component aComp) {
		if(aComp == null) return;
		
		if(aComp instanceof TileMapComponent) {
			this.tileMapRenderer.render(sce, (TileMapComponent) aComp);
		} else if(aComp instanceof SpriteComponent) {
			this.spriteRenderer.render(sce, (SpriteComponent) aComp);
		} else if(aComp instanceof CanvasComponent) {
			this.canvasRenderer.render(sce, (CanvasComponent) aComp);
		}
	}
	
	private void projectNode(Node n) {
		if(!n.isEnabled()) return;
		
		List<Component> comps = n.getComponents();
		for(int i = 0; i < comps.size(); i++) {
			Component c = comps.get(i);
			if(!c.isEnabled()) continue;
			
			if(c instanceof DrawableComponent) {
				this.componentsPool.add(c);
			}
		}
		
		List<Node> children = n.getChildren();
		for(int i = 0; i < children.size(); i++)
			this.projectNode(children.get(i));
	}
	
	public void render(Level lvl) {
		List<Scene> sceneList = lvl.getOverlayScenes();
		if(sceneList.isEmpty()) return;
		
		OGLStateManager.INSTANCE.culling(false);
		
		glClear(GL_COLOR_BUFFER_BIT);
		
		for(int s = 0; s < sceneList.size(); s++) {
			Scene sce = sceneList.get(s);
			
			if(!sce.getRoot().isEnabled()) continue;
			
			this.projectNode(sce.getRoot());
			if(this.componentsPool.isEmpty()) continue;
			
			this.componentsPool.sort(this.painterSort);
			
			// Sort the things
			if(sce.isDepthTest()) {
				OGLStateManager.INSTANCE.depthTest(true);
				
				glClear(GL_DEPTH_BUFFER_BIT);
			} else {
				OGLStateManager.INSTANCE.depthTest(false);
			}
			
			for(int i = 0; i < this.componentsPool.size(); i++) {
				Component aComp = this.componentsPool.get(i);
				
				this.renderComponent(sce, aComp);
			}
			
			this.canvasRenderer.endScene();
			
			this.componentsPool.clear();
		}
	}
	
	public void dispose() {
		this.rectPosVBO.dispose();
		
		this.spriteRenderer.dispose();
		this.tileMapRenderer.dispose();
		this.canvasRenderer.dispose();
	}
	
	public TextureData takeScreenshot() {
		int width = this.getWidth();
		int height = this.getHeight();
		
		OGLFramebuffer2D.unbindAll();
		
		ByteBuffer data = BufferUtils.createByteBuffer(width * height * 3);
		GL11.glReadPixels(0, 0, width, height, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, data);
		
		TextureData tex = new TextureData();
		tex.setData(data);
		tex.setWidth(width);
		tex.setHeight(height);
		tex.setBytesPerPixel(3);
		
		return tex;
	}
}
