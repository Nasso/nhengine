package io.github.nasso.nhengine.opengl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import java.io.IOException;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import io.github.nasso.nhengine.core.LaunchSettings;
import io.github.nasso.nhengine.core.Nhengine;

public class OGLManager {
	public static final boolean DEBUG = Nhengine.DEBUG;
	
	public static final int VERSION_MAJOR = 3;
	public static final int VERSION_MINOR = 3;
	
	private static OGLManager singleton;
	
	// Capabilities and their properties
	public final int maxCombinedTextureImageUnits;
	public final int maxFragmentTextureImageUnits;
	
	public final OGLRenderer renderer;
	
	public OGLManager(LaunchSettings settings) throws IOException {
		GL.createCapabilities();
		
		this.maxCombinedTextureImageUnits = GL11.glGetInteger(GL20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS);
		System.out.println("[OGLManager] " + this.maxCombinedTextureImageUnits + " max texture units (combined)");
		
		this.maxFragmentTextureImageUnits = GL11.glGetInteger(GL20.GL_MAX_TEXTURE_IMAGE_UNITS);
		System.out.println("[OGLManager] " + this.maxFragmentTextureImageUnits + " max texture units (fragment)");
		
		this.renderer = new OGLRenderer(settings.getVideoWidth(), settings.getVideoHeight());
	}
	
	private void disposeInst() {
		this.renderer.dispose();
	}
	
	public static OGLManager get() {
		return OGLManager.singleton;
	}
	
	public static OGLManager init(LaunchSettings settings) throws IOException {
		if(OGLManager.singleton == null) OGLManager.singleton = new OGLManager(settings);
		
		return OGLManager.singleton;
	}
	
	public static void dispose() {
		OGLManager.singleton.disposeInst();
		OGLManager.singleton = null;
		
		OGLTextures.dispose();
		
		GL.destroy();
	}
	
	// Utils
	public static void fastCheckError(String where) {
		if(!DEBUG) return;
		
		String name = OGLManager.getError(GL11.glGetError());
		
		if(name != null) System.err.println("OpenGL error at '" + where + "': " + name);
	}
	
	private static String getError(int err) {
		String error = null;
		
		switch(err) {
			case GL_INVALID_ENUM:
				error = "GL_INVALID_ENUM";
				break;
			case GL_INVALID_VALUE:
				error = "GL_INVALID_VALUE";
				break;
			case GL_INVALID_OPERATION:
				error = "GL_INVALID_OPERATION";
				break;
			case GL_INVALID_FRAMEBUFFER_OPERATION:
				error = "GL_INVALID_FRAMEBUFFER_OPERATION";
				break;
			case GL_OUT_OF_MEMORY:
				error = "GL_OUT_OF_MEMORY";
				break;
			case GL_STACK_UNDERFLOW:
				error = "GL_STACK_UNDERFLOW";
				break;
			case GL_STACK_OVERFLOW:
				error = "GL_STACK_OVERFLOW";
				break;
		}
		
		return error;
	}
}
