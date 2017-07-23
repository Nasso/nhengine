package io.github.nasso.nhengine.opengl;

import java.io.IOException;

import io.github.nasso.nhengine.utils.Nhutils;

public class OGLAdvancedTextureQuadProgram extends OGLProgram {
	public OGLAdvancedTextureQuadProgram() throws IOException {
		super("Advanced texture quad", Nhutils.readFile("res/shaders/advancedQuad.vs", true), Nhutils.readFile("res/shaders/textureQuad.fs", true), "CLIPPING");
		
		this.link();
		
		this.loadUniforms("transform", "sourceXYWH", "destXYWH", "color", "globalAlpha", "scaleXY", "clip.enabled", "clip.xform", "clip.extent");
	}
}
