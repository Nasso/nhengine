package io.github.nasso.nhengine.graphics.opengl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import io.github.nasso.nhengine.event.Observable;
import io.github.nasso.nhengine.graphics.RenderTarget2D;

public class OGLRenderTargets {
	private static OGLRenderTargets INSTANCE;
	
	public static OGLRenderTargets get() {
		return INSTANCE != null ? INSTANCE : (INSTANCE = new OGLRenderTargets(OGLTextures.get()));
	}
	
	private Map<RenderTarget2D, OGLRenderTarget2D> targets2d = new HashMap<>();
	
	private Consumer<Observable> targetDisposer;
	
	private OGLTextures textures;
	
	private OGLRenderTargets(OGLTextures textureManager) {
		this.textures = textureManager;
		
		this.targetDisposer = (s) -> {
			if(this.targets2d.containsKey(s)) {
				this.targets2d.get(s).dispose(false);
				this.targets2d.remove(s);
			}
		};
	}
	
	public OGLRenderTarget2D update(RenderTarget2D rt) {
		OGLRenderTarget2D target = this.targets2d.get(rt);
		
		if(target == null) {
			target = new OGLRenderTarget2D(this.textures.update(rt.getTexture()));
			
			rt.addEventListener("dispose", this.targetDisposer);
			this.targets2d.put(rt, target);
		} else target.setColorTexture(this.textures.update(rt.getTexture()));
		
		return target;
	}
	
	public OGLRenderTarget2D get(RenderTarget2D rt) {
		return this.targets2d.get(rt);
	}
	
	public Map<RenderTarget2D, OGLRenderTarget2D> getTargets2D() {
		return this.targets2d;
	}
	
	public void disposeAll(boolean deleteTextures) {
		Set<RenderTarget2D> keys = this.targets2d.keySet();
		for(RenderTarget2D rt : keys)
			this.targets2d.get(rt).dispose(deleteTextures);
		
		this.targets2d.clear();
	}
}
