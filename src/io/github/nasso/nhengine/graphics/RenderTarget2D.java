package io.github.nasso.nhengine.graphics;

import io.github.nasso.nhengine.core.Disposable;
import io.github.nasso.nhengine.core.Nhengine;
import io.github.nasso.nhengine.event.Observable;

public class RenderTarget2D extends Observable implements Disposable {
	private Texture2D texture;
	private String lvl_id = "";
	
	public void setID(String id) {
		this.lvl_id = id;
	}
	
	public String getID() {
		return this.lvl_id;
	}
	
	private String name = "";
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public RenderTarget2D(Texture2D texture) {
		this.texture = texture;
	}
	
	public RenderTarget2D(int width, int height) {
		this(new Texture2D.Builder().width(width).height(height).minFilter(Nhengine.NEAREST).magFilter(Nhengine.LINEAR).internalFormat(Nhengine.RGBA).wrapS(Nhengine.REPEAT).wrapT(Nhengine.REPEAT).type(Nhengine.UNSIGNED_BYTE).build());
	}
	
	public Texture2D getTexture() {
		return this.texture;
	}
	
	public void setTexture(Texture2D texture) {
		this.texture = texture;
	}
	
	public void dispose() {
		this.triggerEvent("dispose");
	}
}
