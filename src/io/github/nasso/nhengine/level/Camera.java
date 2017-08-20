package io.github.nasso.nhengine.level;

import org.joml.Matrix4f;
import org.joml.Vector2f;

public class Camera {
	private Vector2f position = new Vector2f();
	private float rotation = 0;
	private float fov = 15;
	private float aspectRatio = 1;
	private float zNear = -10000;
	private float zFar = 10000;
	private boolean matrixDirty = true;
	
	private Matrix4f projViewMatrix = new Matrix4f();
	
	public Camera() {
		this.updateProjViewMatrix();
	}
	
	public void updateProjViewMatrix() {
		this.projViewMatrix.setOrtho(this.position.x - this.fov, this.position.x + this.fov, this.position.y + this.fov / this.aspectRatio, this.position.y - this.fov / this.aspectRatio, this.zNear, this.zFar);
		this.projViewMatrix.rotate(this.rotation / 180.0f * 3.141593f, 0, 0, 1);
		
		this.matrixDirty = false;
	}
	
	public Matrix4f getProjViewMatrix(boolean updateIfNeeded) {
		if(updateIfNeeded && this.matrixDirty) this.updateProjViewMatrix();
		
		return this.projViewMatrix;
	}
	
	public Vector2f getPosition() {
		return this.position;
	}
	
	public float getRotation() {
		return this.rotation;
	}
	
	public float getFieldOfView() {
		return this.fov;
	}
	
	public void setMatrixNeedUpdate() {
		this.matrixDirty = true;
	}
	
	public void setPosition(Vector2f position) {
		this.setPosition(position.x, position.y);
	}
	
	public void setRotation(float rotation) {
		if(this.rotation == rotation) return;
		
		this.rotation = rotation;
		
		this.setMatrixNeedUpdate();
	}
	
	public void setPosition(float x, float y) {
		if(this.position.x == x && this.position.y == y) return;
		
		this.position.x = x;
		this.position.y = y;
		
		this.setMatrixNeedUpdate();
	}
	
	public void setPosX(float x) {
		if(this.position.x == x) return;
		
		this.position.x = x;
		
		this.setMatrixNeedUpdate();
	}
	
	public void setPosY(float y) {
		if(this.position.y == y) return;
		
		this.position.y = y;
		
		this.setMatrixNeedUpdate();
	}
	
	public void translate(Vector2f vec) {
		this.position.x += vec.x;
		this.position.y += vec.y;
		
		this.setMatrixNeedUpdate();
	}
	
	public void translate(float x, float y) {
		this.position.x += x;
		this.position.y += y;
		
		this.setMatrixNeedUpdate();
	}
	
	public void translate(float x, float y, float depth) {
		this.position.x += x;
		this.position.y += y;
		
		this.setMatrixNeedUpdate();
	}
	
	public void rotate(float ang) {
		this.rotation += ang;
		
		this.setMatrixNeedUpdate();
	}
	
	public void translateX(float x) {
		this.position.x += x;
		
		this.setMatrixNeedUpdate();
	}
	
	public void translateY(float y) {
		this.position.y += y;
		
		this.setMatrixNeedUpdate();
	}
	
	public float getAspectRatio() {
		return this.aspectRatio;
	}
	
	public void setAspectRatio(float aspectRatio) {
		if(this.aspectRatio == aspectRatio) return;
		
		this.aspectRatio = aspectRatio;
		
		this.setMatrixNeedUpdate();
	}
	
	public void setAspectRatio(float w, float h) {
		this.setAspectRatio(w / h);
	}
	
	public float getZNear() {
		return this.zNear;
	}
	
	public void setZNear(float zNear) {
		if(this.zNear == zNear) return;
		
		this.zNear = zNear;
		
		this.setMatrixNeedUpdate();
	}
	
	public float getZFar() {
		return this.zFar;
	}
	
	public void setZFar(float zFar) {
		if(this.zFar == zFar) return;
		
		this.zFar = zFar;
		
		this.setMatrixNeedUpdate();
	}
	
	public void setFieldOfView(float fov) {
		if(this.fov == fov) return;
		
		this.fov = fov;
		
		this.setMatrixNeedUpdate();
	}
	
	public void setViewport2D(float w, float h) {
		this.aspectRatio = w / h;
		this.fov = w / 2.0f;
		this.position.set(w / 2.0f, h / 2.0f);
		this.rotation = 0;
		
		this.zNear = -10000;
		this.zFar = 10000;
		
		this.setMatrixNeedUpdate();
	}
}
