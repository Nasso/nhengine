package io.github.nasso.nhengine.opengl;

import java.io.IOException;

import io.github.nasso.nhengine.utils.Nhutils;

public class OGLFXAAFilterProgram extends OGLPostFXProgram {
	public OGLFXAAFilterProgram() throws IOException {
		super("FXAA filter program", Nhutils.readFile("res/shaders/postfx/fxaa.fs", true), true, false);
		
		this.loadUniforms("reduceMin", "reduceMul", "spanMax", "resolution");
	}
}
