package io.github.nasso.nhengine.tools.nhstudio.game;

public class GameObject {
	private String name;
	
	public GameObject(String name) {
		this.setName(name);
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
