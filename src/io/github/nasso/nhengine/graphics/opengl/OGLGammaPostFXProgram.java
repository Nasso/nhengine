package io.github.nasso.nhengine.graphics.opengl;

import java.io.IOException;

import io.github.nasso.nhengine.utils.Nhutils;

public class OGLGammaPostFXProgram extends OGLPostFXProgram {
	public OGLGammaPostFXProgram() throws IOException {
		super("Gamma filter program", Nhutils.readFile("res/shaders/postfx/gamma.fs", true), true, false);
		
		this.loadUniforms("gamma");
	}
}
