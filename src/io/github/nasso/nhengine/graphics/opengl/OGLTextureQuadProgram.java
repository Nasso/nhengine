package io.github.nasso.nhengine.graphics.opengl;

import java.io.IOException;

import io.github.nasso.nhengine.utils.Nhutils;

public class OGLTextureQuadProgram extends OGLProgram {
	public OGLTextureQuadProgram() throws IOException {
		super("Texture quad", Nhutils.readFile("res/shaders/quad.vs", true), Nhutils.readFile("res/shaders/textureQuad.fs", true));
		
		this.link();
		
		this.loadUniforms("projView", "model", "color", "globalAlpha", "flipY");
	}
}
