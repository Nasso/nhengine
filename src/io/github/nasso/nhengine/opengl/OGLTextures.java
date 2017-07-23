package io.github.nasso.nhengine.opengl;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import io.github.nasso.nhengine.core.Nhengine;
import io.github.nasso.nhengine.event.Observable;
import io.github.nasso.nhengine.graphics.Texture2D;

public class OGLTextures {
	private Map<Texture2D, OGLTexture2D> textures2D = new HashMap<>();
	
	private Consumer<Observable> texDisposer;
	
	private static OGLTextures INSTANCE;
	
	public static OGLTextures get() {
		return INSTANCE != null ? INSTANCE : (INSTANCE = new OGLTextures());
	}
	
	public static void dispose() {
		if(INSTANCE != null) INSTANCE.disposeAll();
	}
	
	private OGLTextures() {
		this.texDisposer = (tex) -> {
			if(this.textures2D.containsKey(tex)) {
				this.textures2D.get(tex).dispose();
				this.textures2D.remove(tex);
			}
			
			tex.removeEventListener("dispose", this.texDisposer);
		};
	}
	
	private void disposeAll() {
		// 2D clear
		for(Texture2D t : this.textures2D.keySet()) {
			OGLTexture2D tex = this.textures2D.get(t);
			
			if(tex != null) tex.dispose();
		}
		
		this.textures2D.clear();
	}
	
	public OGLTexture2D update(Texture2D tex) {
		if(tex == null) return null;
		
		OGLTexture2D oglTex = this.textures2D.get(tex);
		
		if(oglTex == null) {
			oglTex = new OGLTexture2D(tex.getWidth(), tex.getHeight(), Nhengine.getOpenGLConst(tex.getMagFilter()), Nhengine.getOpenGLConst(tex.getMinFilter()), Nhengine.getOpenGLConst(tex.getWrapS()), Nhengine.getOpenGLConst(tex.getWrapT()), Nhengine.getOpenGLConst(tex.getInternalFormat()), Nhengine.getOpenGLConst(tex.getFormat()), Nhengine.getOpenGLConst(tex.getType()), tex.getData());
			oglTex.setVersion(tex.getVersion());
			
			tex.addEventListener("dispose", this.texDisposer);
			this.textures2D.put(tex, oglTex);
		} else if(oglTex.getVersion() != tex.getVersion()) {
			oglTex.setWidth(tex.getWidth());
			oglTex.setHeight(tex.getHeight());
			oglTex.setMagFilter(Nhengine.getOpenGLConst(tex.getMagFilter()));
			oglTex.setMinFilter(Nhengine.getOpenGLConst(tex.getMinFilter()));
			oglTex.setWrapS(Nhengine.getOpenGLConst(tex.getWrapS()));
			oglTex.setWrapT(Nhengine.getOpenGLConst(tex.getWrapT()));
			oglTex.setInternalFormat(Nhengine.getOpenGLConst(tex.getInternalFormat()));
			oglTex.setFormat(Nhengine.getOpenGLConst(tex.getFormat()));
			oglTex.setType(Nhengine.getOpenGLConst(tex.getType()));
			oglTex.setData(tex.getData());
			oglTex.update();
			
			oglTex.setVersion(tex.getVersion());
		}
		
		return oglTex;
	}
}
