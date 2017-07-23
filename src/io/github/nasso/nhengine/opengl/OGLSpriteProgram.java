package io.github.nasso.nhengine.opengl;

import java.io.IOException;

import io.github.nasso.nhengine.utils.Nhutils;

public class OGLSpriteProgram extends OGLProgram {
	public OGLSpriteProgram() throws IOException {
		super("Sprite rendering program", Nhutils.readFile("res/shaders/gbuffer_sprite.vs", true), Nhutils.readFile("res/shaders/gbuffer_sprite.fs", true));
		
		this.link();
		
		this.loadUniforms("isTileMap", "diffuseTexture", "projView", "tileMapModel");
	}
}
