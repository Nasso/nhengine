package io.github.nasso.nhengine.opengl;

import java.io.IOException;

import io.github.nasso.nhengine.utils.Nhutils;

public class OGLTextProgram extends OGLProgram {
	public OGLTextProgram() throws IOException {
		super("Text rendering program", Nhutils.readFile("res/shaders/gbuffer_text.vs", true), Nhutils.readFile("res/shaders/gbuffer_text.fs", true));
		this.link();
		
		this.loadUniforms("fontTexture", "projViewModel", "projViewModel3", "scaleXY", "cvsMode", "clip.enabled", "clip.xform", "clip.extent");
	}
}
