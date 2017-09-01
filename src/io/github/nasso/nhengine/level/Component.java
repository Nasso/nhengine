package io.github.nasso.nhengine.level;

import java.util.Objects;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import io.github.nasso.nhengine.component.AudioSourceComponent;
import io.github.nasso.nhengine.component.CanvasComponent;
import io.github.nasso.nhengine.component.InputComponent;
import io.github.nasso.nhengine.component.SpriteComponent;
import io.github.nasso.nhengine.component.TileMapComponent;
import io.github.nasso.nhengine.core.Disposable;
import io.github.nasso.nhengine.event.Observable;

/**
 * A component is an abstract object you can add to any {@link Node} to give it a certain functionnality.<br>
 * Common components are:
 * 
 * <ul>
 * <li>{@link AudioSourceComponent}: An audio emitter.</li>
 * <li>{@link CanvasComponent}: A flat drawable 2D cartesian surface.</li>
 * <li>{@link InputComponent}: A component that can react to user input (mouse/keyboard).</li>
 * <li>{@link SpriteComponent}: Displays a sprite.</li>
 * <li>{@link TextComponent}: Displays text with a particular font.</li>
 * <li>{@link TileMapComponent}: Efficiently displays multiple {@link SpriteComponent}s in a 2D grid (like a terrain).</li>
 * </ul>
 * 
 * @author nasso
 */
public abstract class Component extends Observable implements Disposable {
	private boolean enabled = true;
	
	private Node owner;
	
	// The z is the depth
	private Vector3f position = new Vector3f();
	private float rotation = 0f;
	private Vector2f scale = new Vector2f(1);
	
	private Matrix4f localMatrix = new Matrix4f(), worldMatrix = new Matrix4f();
	private boolean localMatrixDirty = false, worldMatrixDirty = false;
	
	/**
	 * Called each frame. This methods calls <code>{@link Component#update(delta) update(delta)}</code>.
	 * @param delta
	 */
	public final void step(float delta) {
		this.update(delta);
	}
	
	/**
	 * Called each frame, if the component is enabled and is in a scene in the currently loaded level.
	 * If you want to implement some behaviors, that's the method you'll want to override.
	 * 
	 * @param delta Delta time, in seconds.
	 */
	public void update(float delta) {
		
	}
	
	void setOwner(Node n) {
		this.owner = n;
		
		this.matricesNeedUpdate();
	}
	
	/**
	 * Returns the {@link Node} owning this component.
	 * 
	 * @return
	 */
	public Node getOwner() {
		return this.owner;
	}
	
	/**
	 * Disposes this component. It triggers a <code>"dispose"</code> event.
	 */
	public void dispose() {
		this.triggerEvent("dispose");
	}
	
	/**
	 * @return The position of the component. It is relative to its owning {@link Node}, and the Z coordinate represents the depth.
	 */
	public Vector3f getPosition() {
		return this.position;
	}
	
	/**
	 * @return The current rotation in degrees.
	 */
	public float getRotation() {
		return this.rotation;
	}
	
	/**
	 * @return The current scale factors for each axis.
	 */
	public Vector2f getScale() {
		return this.scale;
	}
	
	/**
	 * Returns the world position of this component, i.e. the total position when adding the local position to the world position of its owner.
	 * 
	 * @param target
	 *            A vector to hold the result value. Shouldn't be <code>null</code>.
	 * @return <code>target</code>
	 * @throws NullPointerException
	 *             if <code>target</code> is <code>null</code>.
	 */
	public Vector3f getWorldPosition(Vector3f target) {
		Objects.requireNonNull(target);
		
		target.zero();
		
		if(this.owner != null) {
			this.owner.getWorldPosition(target);
		}
		
		target.x += this.position.x;
		target.y += this.position.y;
		target.z += this.position.z;
		
		return target;
	}
	
	/**
	 * Returns the world scale of this component, i.e. the total scale when multiplying the local scale to the world scale of its owner.
	 * 
	 * @param target
	 *            A vector to hold the result.
	 * @return <code>target</code>
	 * @throws NullPointerException
	 *             if <code>target</code> is <code>null</code>.
	 */
	public Vector2f getWorldScale(Vector2f target) {
		Objects.requireNonNull(target);
		
		target.set(1.0f);
		
		if(this.owner != null) {
			this.owner.getWorldScale(target);
		}
		
		target.x *= this.scale.x;
		target.y *= this.scale.y;
		
		return target;
	}
	
	/**
	 * Returns the world rotation of this component, i.e. the total rotation when adding the local rotation to the world rotation of its owner.
	 * 
	 * @return The world rotation.
	 */
	public float getWorldRotation() {
		float rot = 0;
		
		if(this.owner != null) rot = this.owner.getWorldRotation();
		
		rot += this.rotation;
		
		return rot;
	}
	
	/**
	 * Returns the world depth of this component, i.e. the total depth when adding the local depth to the world depth of its owner.
	 * It is equal to the <code>z</code> component of the world position.
	 * 
	 * @return The world depth.
	 */
	public float getWorldDepth() {
		float depth = 0;
		
		if(this.owner != null) depth = this.owner.getWorldDepth();
		
		depth += this.position.z;
		
		return depth;
	}
	
	/**
	 * Updates the local matrix, applying the current position, rotation, and scale.
	 * This is called automatically <strong>once per frame</strong>, if the component is enabled.<br>
	 * <br>
	 * So if you just set the position using {@link #setPosition(Vector3f)} or any derivate, the matrix won't be updated immediatly.
	 * If you need the changes to take effect in the local matrix before the end of the current frame, you should call this method.<br>
	 */
	public void updateLocalMatrix() {
		this.localMatrix.identity();
		this.localMatrix.translate(this.position);
		this.localMatrix.rotate(this.rotation / 180.0f * 3.141593f, 0, 0, 1);
		this.localMatrix.scale(this.scale.x, this.scale.y, 1);
		
		this.localMatrixDirty = false;
	}
	
	/**
	 * Updates the world matrix of this component.
	 * Note that calling this method will eventually update the local matrix and/or the world matrix of its owning {@link Node} if they need to.<br>
	 * This method doesn't do anything if this component doesn't have any owning node.
	 */
	public void updateWorldMatrix() {
		if(this.owner == null) return;
		
		this.owner.getWorldMatrix(true).mul(this.getLocalMatrix(true), this.worldMatrix);
		
		this.worldMatrixDirty = false;
	}
	
	/**
	 * Updates both the local and world matrix of this component. It is equivalent to:
	 * 
	 * <pre>
	 * // comp is this component
	 * comp.updateLocalMatrix();
	 * comp.updateWorldMatrix();
	 * </pre>
	 */
	public void updateAllMatrices() {
		this.updateLocalMatrix();
		this.updateWorldMatrix();
	}
	
	/**
	 * Returns the local matrix of this component.
	 * 
	 * @param updateIfNeeded
	 *            True to update the local matrix if it needs to before returning it, false otherwise.
	 * @return The local matrix.
	 */
	public Matrix4f getLocalMatrix(boolean updateIfNeeded) {
		if(updateIfNeeded && this.localMatrixDirty) this.updateLocalMatrix();
		
		return this.localMatrix;
	}
	
	/**
	 * Returns the world matrix of this component.
	 * Note that if the world matrix is getting updated, the local matrix and/or the world matrix of the owning node will too, as specified by {@link #updateWorldMatrix()}.
	 * 
	 * @param updateIfNeeded
	 *            True to update the world matrix if it needs to before returning it, false otherwise.
	 * @return The local matrix.
	 */
	public Matrix4f getWorldMatrix(boolean updateIfNeeded) {
		if(updateIfNeeded && this.worldMatrixDirty) this.updateWorldMatrix();
		
		return this.worldMatrix;
	}
	
	/**
	 * Invalidates the local and world matrices.
	 * Invalid matrices gets updated on the next call of {@link #getLocalMatrix()}/{@link #getWorldMatrix()} when the <code>updateIfNeeded</code> argument is set to true.
	 */
	public void matricesNeedUpdate() {
		this.localMatrixDirty = this.worldMatrixDirty = true;
	}
	
	/**
	 * Sets the local position of this component. The <code>z</code> component of the given vector represents the depth.<br>
	 * This method calls {@link #matricesNeedUpdate()}.
	 * 
	 * @param position
	 *            The new position.
	 */
	public void setPosition(Vector3f position) {
		this.position.set(position);
		
		this.matricesNeedUpdate();
	}
	
	/**
	 * Sets the local rotation of this component.<br>
	 * This method calls {@link #matricesNeedUpdate()}.
	 * 
	 * @param rotation
	 *            The new rotation, in degrees.
	 */
	public void setRotation(float rotation) {
		this.rotation = rotation;
		
		this.matricesNeedUpdate();
	}
	
	/**
	 * Sets the local scale of this component.<br>
	 * This method calls {@link #matricesNeedUpdate()}.
	 * 
	 * @param scale
	 *            The new scaling vector, one component for each axis.
	 */
	public void setScale(Vector2f scale) {
		this.scale.set(scale);
		
		this.matricesNeedUpdate();
	}
	
	/**
	 * Sets the local 2D position of this component.<br>
	 * This method calls {@link #matricesNeedUpdate()}.
	 * 
	 * @param x
	 *            The new x coordinate.
	 * @param y
	 *            The new y coordinate.
	 */
	public void setPosition(float x, float y) {
		this.position.x = x;
		this.position.y = y;
		
		this.matricesNeedUpdate();
	}
	
	/**
	 * Sets the local scale of this component.<br>
	 * This method calls {@link #matricesNeedUpdate()}.
	 * 
	 * @param x
	 *            The new scale for the x axis.
	 * @param y
	 *            The new scale for the y axis.
	 */
	public void setScale(float x, float y) {
		this.scale.set(x, y);
		
		this.matricesNeedUpdate();
	}
	
	/**
	 * Sets the local x position of this component.<br>
	 * This method calls {@link #matricesNeedUpdate()}.
	 * 
	 * @param x
	 *            The new x coordinate.
	 */
	public void setPosX(float x) {
		this.position.x = x;
		
		this.matricesNeedUpdate();
	}
	
	/**
	 * Sets the local y position of this component.<br>
	 * This method calls {@link #matricesNeedUpdate()}.
	 * 
	 * @param y
	 *            The new y coordinate.
	 */
	public void setPosY(float y) {
		this.position.y = y;
		
		this.matricesNeedUpdate();
	}
	
	/**
	 * Sets the local depth of this component.<br>
	 * This method calls {@link #matricesNeedUpdate()}.
	 * 
	 * @param z
	 *            The new depth
	 */
	public void setDepth(float z) {
		this.position.z = z;
		
		this.matricesNeedUpdate();
	}
	
	/**
	 * Sets the local scale for the x axis of this component.<br>
	 * This method calls {@link #matricesNeedUpdate()}.
	 * 
	 * @param x
	 *            The new scale for the x axis.
	 */
	public void setScaleX(float x) {
		this.scale.x = x;
		
		this.matricesNeedUpdate();
	}
	
	/**
	 * Sets the local scale for the y axis of this component.<br>
	 * This method calls {@link #matricesNeedUpdate()}.
	 * 
	 * @param y
	 *            The new scale for the y axis.
	 */
	public void setScaleY(float y) {
		this.scale.y = y;
		
		this.matricesNeedUpdate();
	}
	
	/**
	 * Translates this component.<br>
	 * This method calls {@link #matricesNeedUpdate()}.
	 * 
	 * @param vec
	 *            The translation vector.
	 */
	public void translate(Vector2f vec) {
		this.position.x += vec.x;
		this.position.y += vec.y;
		
		this.matricesNeedUpdate();
	}
	
	/**
	 * Translates this component, and adds <code>depth</code> to its current depth.<br>
	 * This method calls {@link #matricesNeedUpdate()}.
	 * 
	 * @param vec
	 *            The translation vector.
	 */
	public void translate(Vector2f vec, float depth) {
		this.position.x += vec.x;
		this.position.y += vec.y;
		this.position.z += depth;
		
		this.matricesNeedUpdate();
	}
	
	/**
	 * Translates this component. The <code>z</code> component of the given vector is the depth.<br>
	 * This method calls {@link #matricesNeedUpdate()}.
	 * 
	 * @param vec
	 *            The translation vector, with <code>z</code> being the amount of depth to add (or remove).
	 */
	public void translate(Vector3f vec) {
		this.position.x += vec.x;
		this.position.y += vec.y;
		this.position.z += vec.z;
		
		this.matricesNeedUpdate();
	}
	
	/**
	 * Translates this component.<br>
	 * This method calls {@link #matricesNeedUpdate()}.
	 * 
	 * @param x
	 *            The translation on the x axis.
	 * @param y
	 *            The translation on the y axis.
	 */
	public void translate(float x, float y) {
		this.position.x += x;
		this.position.y += y;
		
		this.matricesNeedUpdate();
	}
	
	/**
	 * Translates this component, and adds <code>depth</code> to its current depth.<br>
	 * This method calls {@link #matricesNeedUpdate()}.
	 * 
	 * @param x
	 *            The translation on the x axis.
	 * @param y
	 *            The translation on the y axis.
	 * @param depth
	 *            The amount of depth to add (or remove)
	 */
	public void translate(float x, float y, float depth) {
		this.position.x += x;
		this.position.y += y;
		this.position.z += depth;
		
		this.matricesNeedUpdate();
	}
	
	/**
	 * Translates this component on the x axis.
	 * 
	 * @param x
	 *            The translation on the x axis.
	 */
	public void translateX(float x) {
		this.position.x += x;
		
		this.matricesNeedUpdate();
	}
	
	/**
	 * Translates this component on the y axis.
	 * 
	 * @param y
	 *            The translation on the y axis.
	 */
	public void translateY(float y) {
		this.position.y += y;
		
		this.matricesNeedUpdate();
	}
	
	/**
	 * Applies a rotation by <code>ang</code> degrees to this component.<br>
	 * This method calls {@link #matricesNeedUpdate()}.
	 * 
	 * @param ang
	 *            The rotation angle, in degrees.
	 */
	public void rotate(float ang) {
		this.rotation += ang;
		
		this.matricesNeedUpdate();
	}
	
	/**
	 * Scales this component.<br>
	 * This method calls {@link #matricesNeedUpdate()}.
	 * 
	 * @param vec
	 *            The scaling vector.
	 */
	public void scale(Vector2f vec) {
		this.scale.mul(vec);
		
		this.matricesNeedUpdate();
	}
	
	/**
	 * Scales this component.<br>
	 * This method calls {@link #matricesNeedUpdate()}.
	 * 
	 * @param x
	 *            The scaling factor on the x axis.
	 * @param y
	 *            The scaling factor on the y axis.
	 */
	public void scale(float x, float y) {
		this.scale.mul(x, y);
		
		this.matricesNeedUpdate();
	}
	
	/**
	 * Scales this component.<br>
	 * This method calls {@link #matricesNeedUpdate()}.
	 * 
	 * @param xy
	 *            The scaling factor for both the x and y axes.
	 */
	public void scale(float xy) {
		this.scale.mul(xy);
		
		this.matricesNeedUpdate();
	}
	
	/**
	 * Scales this component.<br>
	 * This method calls {@link #matricesNeedUpdate()}.
	 * 
	 * @param x
	 *            The scaling factor for the x axis.
	 */
	public void scaleX(float x) {
		this.scale.x *= x;
		
		this.matricesNeedUpdate();
	}
	
	/**
	 * Scales this component.<br>
	 * This method calls {@link #matricesNeedUpdate()}.
	 * 
	 * @param y
	 *            The scaling factor for the y axis.
	 */
	public void scaleY(float y) {
		this.scale.y *= y;
		
		this.matricesNeedUpdate();
	}
	
	/**
	 * @return True if this component is enabled, false otherwise.
	 */
	public boolean isEnabled() {
		return this.enabled && (this.owner == null || this.owner.isEnabled());
	}
	
	/**
	 * Sets the <code>enabled</code> state of this component. A disabled component doesn't do anything and acts as if it wasn't present.
	 * 
	 * @param enabled
	 *            True to enable this component, false to disable it.
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
