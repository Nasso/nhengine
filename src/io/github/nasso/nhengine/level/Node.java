package io.github.nasso.nhengine.level;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import io.github.nasso.nhengine.core.Disposable;
import io.github.nasso.nhengine.event.Observable;

public class Node extends Observable implements Disposable {
	private Vector3f position = new Vector3f();
	private float rotation = 0f;
	private Vector2f scale = new Vector2f(1);
	
	private Matrix4f localMatrix = new Matrix4f(), worldMatrix = new Matrix4f();
	private boolean localMatrixDirty = false, worldMatrixDirty = false;
	
	private List<Node> children = new ArrayList<Node>();
	private List<Component> components = new ArrayList<Component>();
	
	private Node parent;
	Scene rootOf = null;
	
	private boolean enabled = true;
	
	/**
	 * Add a node to the children of this node
	 * 
	 * @param node
	 *            the new child to add
	 */
	public void addChild(Node node) {
		node.setParent(this);
	}
	
	/**
	 * Returns the children nodes. The returned list is assumed to be READ ONLY and shouldn't be modified.
	 * 
	 * @return The children nodes
	 */
	public List<Node> getChildren() {
		return this.children;
	}
	
	/**
	 * Returns an iterator over the children of this node. The returned iterator shouldn't modify the list, as it's assumed to be READ ONLY.
	 * 
	 * @return An iterator over the children
	 */
	public Iterator<Node> childrenIterator() {
		return this.children.iterator();
	}
	
	/**
	 * Returns the parent of this node.
	 * 
	 * @return The parent of this node
	 */
	public Node getParent() {
		return this.parent;
	}
	
	/**
	 * Sets the parent of this node to be <code>newParent</code>. This node is removed from its parent if any, and added to the children list of the supplied node.
	 * 
	 * @param newParent
	 *            the new parent
	 */
	public void setParent(Node newParent) {
		if(this.parent != null) this.parent.children.remove(this);
		
		this.parent = newParent;
		
		if(newParent != null) {
			this.parent.children.add(this);
		}
		
		this.worldMatrixDirty = true;
	}
	
	public Scene getScene() {
		return this.rootOf != null ? this.rootOf : this.parent == null ? null : this.parent.getScene();
	}
	
	public void removeChild(Node child) {
		if(child.parent == this) child.setParent(null);
	}
	
	public void dispose() {
		this.triggerEvent("dispose");
	}
	
	public List<Component> getComponents() {
		return this.components;
	}
	
	public void addComponent(Component c) {
		c.setOwner(this);
		this.components.add(c);
	}
	
	public void addComponents(Component... cs) {
		for(Component c : cs)
			this.addComponent(c);
	}
	
	public void removeComponent(Component c) {
		c.setOwner(null);
		this.components.remove(c);
	}
	
	public Vector3f getPosition() {
		return this.position;
	}
	
	public float getWorldDepth() {
		float depth = 0;
		
		if(this.parent != null) depth = this.parent.getWorldDepth();
		
		depth += this.position.z;
		
		return depth;
	}
	
	public float getRotation() {
		return this.rotation;
	}
	
	public Vector2f getScale() {
		return this.scale;
	}
	
	public Vector3f getWorldPosition(Vector3f target) {
		target.zero();
		
		if(this.parent != null) {
			this.parent.getWorldPosition(target);
		}
		
		target.x += this.position.x;
		target.y += this.position.y;
		target.z += this.position.z;
		
		return target;
	}
	
	public Vector2f getWorldScale(Vector2f target) {
		target.set(1.0f);
		
		if(this.parent != null) {
			this.parent.getWorldScale(target);
		}
		
		target.x *= this.scale.x;
		target.y *= this.scale.y;
		
		return target;
	}
	
	public float getWorldRotation() {
		float rot = 0;
		
		if(this.parent != null) rot = this.parent.getWorldRotation();
		
		rot += this.rotation;
		
		return rot;
	}
	
	public void updateLocalMatrix() {
		this.localMatrix.identity();
		this.localMatrix.translate(this.position);
		this.localMatrix.rotate(this.rotation / 180.0f * 3.141593f, 0, 0, 1);
		this.localMatrix.scale(this.scale.x, this.scale.y, 1);
		
		this.localMatrixDirty = false;
	}
	
	public void updateWorldMatrix() {
		if(this.parent != null) this.parent.getWorldMatrix(true).mul(this.getLocalMatrix(true), this.worldMatrix);
		else this.worldMatrix.set(this.getLocalMatrix(true));
		
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
		
		for(int i = 0; i < this.components.size(); i++) {
			this.components.get(i).setMatrixNeedUpdate();
		}
	}
	
	public void setPosition(Vector3f position) {
		this.setPosition(position.x, position.y);
	}
	
	public void setRotation(float rotation) {
		if(this.rotation == rotation) return;
		
		this.rotation = rotation;
		
		this.setMatrixNeedUpdate();
	}
	
	public void setScale(Vector2f scale) {
		if(this.scale.x == scale.x && this.scale.y == scale.y) return;
		
		this.scale.set(scale);
		
		this.setMatrixNeedUpdate();
	}
	
	public void setPosition(float x, float y) {
		if(this.position.x == x && this.position.y == y) return;
		
		this.position.x = x;
		this.position.y = y;
		
		this.setMatrixNeedUpdate();
	}
	
	public void setScale(float x, float y) {
		if(this.scale.x == x && this.scale.y == y) return;
		
		this.scale.set(x, y);
		
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
	
	public void setDepth(float z) {
		if(this.position.z == z) return;
		
		this.position.z = z;
		
		this.setMatrixNeedUpdate();
	}
	
	public void setScaleX(float x) {
		if(this.scale.x == x) return;
		
		this.scale.x = x;
		
		this.setMatrixNeedUpdate();
	}
	
	public void setScaleY(float y) {
		if(this.scale.y == y) return;
		
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
		return this.enabled && (this.parent == null || this.parent.isEnabled());
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
