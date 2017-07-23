package io.github.nasso.nhengine.ui.control;

import org.joml.Vector2f;

import io.github.nasso.nhengine.graphics.GraphicsContext2D;
import io.github.nasso.nhengine.graphics.Texture2D;
import io.github.nasso.nhengine.ui.UIComponent;

public class UIImageView extends UIComponent {
	private Texture2D image;
	
	private float x = 0;
	private float y = 0;
	private float w = 1;
	private float h = 1;
	private boolean keepRatio = true;
	
	public UIImageView(Texture2D img, float x, float y, float w, float h) {
		this.image = img;
		
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public UIImageView(Texture2D img) {
		this(img, 0, 0, img == null ? 1 : img.getWidth(), img == null ? 1 : img.getHeight());
	}
	
	public UIImageView() {
		this(null);
	}
	
	public void transformPositionRelativeToImage(Vector2f pos) {
		float imgW = this.getImage().getWidth();
		float imgH = this.getImage().getHeight();
		
		float w = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
		float h = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
		
		if(this.keepRatio) {
			float scale = Math.min(w / imgW, h / imgH);
			
			imgW *= scale;
			imgH *= scale;
		} else {
			imgW = w;
			imgH = h;
		}
		
		float imgX = this.getPaddingLeft() + w / 2f - imgW / 2f;
		float imgY = this.getPaddingTop() + h / 2f - imgH / 2f;
		
		pos.x -= imgX;
		pos.y -= imgY;
		
		pos.x /= imgW;
		pos.y /= imgH;
		
		pos.x *= this.getImage().getWidth();
		pos.y *= this.getImage().getHeight();
	}
	
	protected void paintComponent(GraphicsContext2D gtx) {
		if(this.image == null) return;
		
		float imgW = this.w;
		float imgH = this.h;
		
		float w = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
		float h = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
		
		if(this.keepRatio) {
			float scale = Math.min(w / imgW, h / imgH);
			
			imgW *= scale;
			imgH *= scale;
		} else {
			imgW = w;
			imgH = h;
		}
		
		gtx.drawImage(this.image, this.x, this.y, this.w, this.h, this.getPaddingLeft() + w / 2f - imgW / 2f, this.getPaddingTop() + h / 2f - imgH / 2f, imgW, imgH);
	}
	
	public void computePreferredSize() {
		this.preferredSize.set(this.w, this.h);
	}
	
	public Texture2D getImage() {
		return this.image;
	}
	
	public void setViewport(float x, float y, float w, float h) {
		this.x = x < 0 ? 0 : x;
		this.y = y < 0 ? 0 : y;
		this.w = w < 0 ? 1 : w;
		this.h = h < 0 ? 1 : h;
		
		if(this.parent != null) this.parent.requestRepack();
		this.repaint();
	}
	
	public float getViewportX() {
		return this.x;
	}
	
	public float getViewportY() {
		return this.y;
	}
	
	public float getViewportWidth() {
		return this.w;
	}
	
	public float getViewportHeight() {
		return this.h;
	}
	
	public void setImage(Texture2D image, float x, float y, float w, float h) {
		this.image = image;
		this.x = x < 0 ? 0 : x;
		this.y = y < 0 ? 0 : y;
		this.w = w < 0 ? 1 : w;
		this.h = h < 0 ? 1 : h;
		
		if(this.parent != null) this.parent.requestRepack();
		this.repaint();
	}
	
	public void setImage(Texture2D image) {
		this.setImage(image, 0, 0, image.getWidth(), image.getHeight());
	}
	
	public boolean isKeepRatio() {
		return this.keepRatio;
	}
	
	public void setKeepRatio(boolean keepRatio) {
		this.keepRatio = keepRatio;
	}
}
