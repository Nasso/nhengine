package io.github.nasso.nhengine.graphics.opengl;

import java.io.IOException;

import io.github.nasso.nhengine.utils.Nhutils;

public class OGLBoxBlurProgram extends OGLPostFXProgram {
	public OGLBoxBlurProgram() throws IOException {
		super("Box blur program", Nhutils.readFile("res/shaders/postfx/boxBlur.fs", true), true, false);
		
		this.loadUniforms("size", "textureDim");
	}
}
