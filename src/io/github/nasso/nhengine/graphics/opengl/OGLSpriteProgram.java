package io.github.nasso.nhengine.graphics.opengl;

import java.io.IOException;

import io.github.nasso.nhengine.utils.Nhutils;

public class OGLSpriteProgram extends OGLProgram {
	public OGLSpriteProgram() throws IOException {
		super("Sprite rendering program", Nhutils.readFile("res/shaders/sprite.vs", true), Nhutils.readFile("res/shaders/sprite.fs", true));
		
		this.link();
		
		this.loadUniforms("widthHeightDepth", "spriteSubRegion", "diffuseColorAlpha", "model", "diffuseTexture", "projView");
	}
}
