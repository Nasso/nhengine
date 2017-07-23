package io.github.nasso.nhengine.opengl;

import java.io.IOException;

import io.github.nasso.nhengine.utils.Nhutils;

public class OGLPostFXProgram extends OGLProgram {
	public OGLPostFXProgram(String name, String fragmentSource, boolean color, boolean depth) throws IOException {
		super(name, Nhutils.readFile("res/shaders/fullQuad.vs", true), fragmentSource);
		
		this.link();
		
		if(color) this.loadUniform("color");
		
		if(depth) this.loadUniform("depth");
	}
}
