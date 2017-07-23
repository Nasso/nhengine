package io.github.nasso.nhengine.opengl;

import java.io.IOException;

import io.github.nasso.nhengine.utils.Nhutils;

public class OGLGaussianBlurProgram extends OGLPostFXProgram {
	public OGLGaussianBlurProgram() throws IOException {
		super("Gaussian blur program", Nhutils.readFile("res/shaders/postfx/gaussianBlur.fs", true), true, false);
		
		this.loadUniforms("size", "horizontal", "textureLength");
	}
}
