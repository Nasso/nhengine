package io.github.nasso.nhengine.level;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import io.github.nasso.nhengine.core.Disposable;
import io.github.nasso.nhengine.event.Observable;

public abstract class Component extends Observable implements Disposable {
	private boolean enabled = true;
	
	private Node owner;
	
	private Vector3f position = new Vector3f();
	private float rotation = 0f;
	private Vector2f scale = new Vector2f(1);
	
	private Matrix4f localMatrix = new Matrix4f(), worldMatrix = new Matrix4f();
	private boolean localMatrixDirty = false, worldMatrixDirty = false;
	
	public Component() {
	}
	
	void setOwner(Node n) {
		this.owner = n;
		
		this.setMatrixNeedUpdate();
	}
	
	public Node getOwner() {
		return this.owner;
	}
	
	public void dispose() {
		this.triggerEvent("dispose");
	}
	
	public Vector3f getPosition() {
		return this.position;
	}
	
	public float getRotation() {
		return this.rotation;
	}
	
	public Vector2f getScale() {
		return this.scale;
	}
	
	public Vector3f getWorldPosition(Vector3f target) {
		target.zero();
		
		if(this.owner != null) {
			this.owner.getWorldPosition(target);
		}
		
		target.x += this.position.x;
		target.y += this.position.y;
		target.z += this.position.z;
		
		return target;
	}
	
	public Vector2f getWorldScale(Vector2f target) {
		target.set(1.0f);
		
		if(this.owner != null) {
			this.owner.getWorldScale(target);
		}
		
		target.x *= this.scale.x;
		target.y *= this.scale.y;
		
		return target;
	}
	
	public float getWorldRotation() {
		float rot = 0;
		
		if(this.owner != null) rot = this.owner.getWorldRotation();
		
		rot += this.rotation;
		
		return rot;
	}
	
	public float getWorldDepth() {
		float depth = 0;
		
		if(this.owner != null) depth = this.owner.getWorldDepth();
		
		depth += this.position.z;
		
		return depth;
	}
	
	public void updateLocalMatrix() {
		this.localMatrix.identity();
		this.localMatrix.translate(this.position);
		this.localMatrix.rotate(this.rotation / 180.0f * 3.141593f, 0, 0, 1);
		this.localMatrix.scale(this.scale.x, this.scale.y, 1);
		
		this.localMatrixDirty = false;
	}
	
	public void updateWorldMatrix() {
		if(this.owner == null) return;
		
		this.owner.getWorldMatrix(true).mul(this.getLocalMatrix(true), this.worldMatrix);
		
		this.worldMatrixDirty = false;
	}
	
	public void updateAllMatrices() {
		this.updateLocalMatrix();
		this.updateWorldMatrix();
	}
	
	public Matrix4f getLocalMatrix(boolean updateIfNeeded) {
		if(updateIfNeeded && this.localMatrixDirty) this.updateLocalMatrix();
		
		return this.localMatrix;
	}
	
	public Matrix4f getWorldMatrix(boolean updateIfNeeded) {
		if(updateIfNeeded && this.worldMatrixDirty) this.updateWorldMatrix();
		
		return this.worldMatrix;
	}
	
	public void setMatrixNeedUpdate() {
		this.localMatrixDirty = this.worldMatrixDirty = true;
	}
	
	public void setPosition(Vector3f position) {
		this.position.set(position);
		
		this.setMatrixNeedUpdate();
	}
	
	public void setRotation(float rotation) {
		this.rotation = rotation;
		
		this.setMatrixNeedUpdate();
	}
	
	public void setScale(Vector2f scale) {
		this.scale.set(scale);
		
		this.setMatrixNeedUpdate();
	}
	
	public void setPosition(float x, float y) {
		this.position.x = x;
		this.position.y = y;
		
		this.setMatrixNeedUpdate();
	}
	
	public void setScale(float x, float y) {
		this.scale.set(x, y);
		
		this.setMatrixNeedUpdate();
	}
	
	public void setPosX(float x) {
		this.position.x = x;
		
		this.setMatrixNeedUpdate();
	}
	
	public void setPosY(float y) {
		this.position.y = y;
		
		this.setMatrixNeedUpdate();
	}
	
	public void setDepth(float z) {
		this.position.z = z;
		
		this.setMatrixNeedUpdate();
	}
	
	public void setScaleX(float x) {
		this.scale.x = x;
		
		this.setMatrixNeedUpdate();
	}
	
	public void setScaleY(float y) {
		this.scale.y = y;
		
		this.setMatrixNeedUpdate();
	}
	
	public void translate(Vector2f vec) {
		this.position.x += vec.x;
		this.position.y += vec.y;
		
		this.setMatrixNeedUpdate();
	}
	
	public void translate(Vector2f vec, float depth) {
		this.position.x += vec.x;
		this.position.y += vec.y;
		this.position.z += depth;
		
		this.setMatrixNeedUpdate();
	}
	
	public void translate(Vector3f vec) {
		this.position.x += vec.x;
		this.position.y += vec.y;
		this.position.z += vec.z;
		
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
		this.position.z += depth;
		
		this.setMatrixNeedUpdate();
	}
	
	public void rotate(float ang) {
		this.rotation += ang;
		
		this.setMatrixNeedUpdate();
	}
	
	public void scale(Vector2f vec) {
		this.scale.mul(vec);
		
		this.setMatrixNeedUpdate();
	}
	
	public void scale(float x, float y) {
		this.scale.mul(x, y);
		
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
	
	public void scale(float xy) {
		this.scale.mul(xy);
		
		this.setMatrixNeedUpdate();
	}
	
	public void scaleX(float x) {
		this.scale.x *= x;
		
		this.setMatrixNeedUpdate();
	}
	
	public void scaleY(float y) {
		this.scale.y *= y;
		
		this.setMatrixNeedUpdate();
	}
	
	public boolean isEnabled() {
		return this.enabled && (this.owner == null || this.owner.isEnabled());
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
