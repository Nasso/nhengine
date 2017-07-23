package io.github.nasso.nhengine.opengl;

import java.io.IOException;

import io.github.nasso.nhengine.utils.Nhutils;

public class OGLVignetteEffectProgram extends OGLPostFXProgram {
	public OGLVignetteEffectProgram() throws IOException {
		super("Vignette effect program", Nhutils.readFile("res/shaders/postfx/vignette.fs", true), true, false);
		
		this.loadUniforms("vignetteColor", "parameters");
	}
}
