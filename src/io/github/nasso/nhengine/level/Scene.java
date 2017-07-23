package io.github.nasso.nhengine.level;

import io.github.nasso.nhengine.core.Disposable;
import io.github.nasso.nhengine.event.Observable;

public class Scene extends Observable implements Disposable {
	private String name = null;
	
	private boolean depthTest = false;
	
	private Camera cam = new Camera();
	private Node root = new Node();
	
	public Scene() {
		this(null);
	}
	
	public Scene(String name) {
		this.name = name;
	}
	
	public void update(float delta) {
		
	}
	
	public void dispose() {
		this.triggerEvent("dispose");
	}
	
	public Camera getCamera() {
		return this.cam;
	}
	
	public void setCamera(Camera cam) {
		this.cam = cam;
	}
	
	public Node getRoot() {
		return this.root;
	}
	
	public void setRoot(Node root) {
		this.root.rootOf = null;
		this.root = root;
		this.root.rootOf = this;
	}
	
	public boolean isDepthTest() {
		return this.depthTest;
	}
	
	public void setDepthTest(boolean depthTest) {
		this.depthTest = depthTest;
	}
	
	public String toString() {
		return "[scene:" + this.name + "]";
	}
}
