package io.github.nasso.nhengine.graphics.opengl;

import java.io.IOException;

import io.github.nasso.nhengine.utils.Nhutils;

public class OGLTileMapProgram extends OGLProgram {
	public OGLTileMapProgram() throws IOException {
		super("Tile-map rendering program", Nhutils.readFile("res/shaders/tilemap.vs", true), Nhutils.readFile("res/shaders/sprite.fs", true));
		
		this.link();
		
		this.loadUniforms("tilesetSize", "tileWidthHeightDepth", "opacity", "model", "isometric", "diffuseTexture", "projView");
	}
}
