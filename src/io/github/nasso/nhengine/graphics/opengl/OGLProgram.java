package io.github.nasso.nhengine.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix3fc;
import org.joml.Matrix4fc;
import org.joml.Vector2fc;
import org.joml.Vector2ic;
import org.joml.Vector3fc;
import org.joml.Vector3ic;
import org.joml.Vector4fc;
import org.joml.Vector4ic;
import org.lwjgl.BufferUtils;

public class OGLProgram {
	private static int usedProgram = 0;
	
	private List<String> defines = new ArrayList<String>();
	private Map<String, Integer> uniforms = new HashMap<>();
	
	private FloatBuffer _floatBuffer9 = BufferUtils.createFloatBuffer(9);
	private FloatBuffer _floatBuffer16 = BufferUtils.createFloatBuffer(16);
	
	private int version = 330;
	private String profile = "core";
	
	private int id;
	private int vertShader;
	private int fragShader;
	
	private boolean compiled = false;
	
	private String name;
	
	public OGLProgram(String programName) {
		this.name = programName;
	}
	
	public OGLProgram(String programName, String vertSource, String fragSource) {
		this(programName, vertSource, fragSource, (String[]) null);
	}
	
	public OGLProgram(String programName, String vertSource, String fragSource, String... defines) {
		this.name = programName;
		
		if(defines != null) {
			for(int i = 0; i < defines.length; i++)
				this.define(defines[i]);
		}
		
		this.compile(vertSource, fragSource);
	}
	
	public void compile(String vertSource, String fragSource) {
		StringBuilder headerBuilder = new StringBuilder();
		
		headerBuilder.append("#version " + this.version + " " + this.profile + System.lineSeparator());
		
		if(this.defines != null) {
			for(int i = 0; i < this.defines.size(); i++)
				headerBuilder.append("#define " + this.defines.get(i) + System.lineSeparator());
		}
		
		String header = headerBuilder.toString();
		
		// Vert
		this.vertShader = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(this.vertShader, header + vertSource);
		
		// Frag
		this.fragShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(this.fragShader, header + fragSource);
		
		glCompileShader(this.vertShader);
		glCompileShader(this.fragShader);
		
		int vCompileStatus = glGetShaderi(this.vertShader, GL_COMPILE_STATUS);
		int fCompileStatus = glGetShaderi(this.fragShader, GL_COMPILE_STATUS);
		
		if(vCompileStatus == GL_FALSE) {
			System.err.println("Vertex shader compile error:");
			System.err.println(glGetShaderInfoLog(this.vertShader));
		}
		
		if(fCompileStatus == GL_FALSE) {
			System.err.println("Fragment shader compile error:");
			System.err.println(glGetShaderInfoLog(this.fragShader));
		}
		
		this.id = glCreateProgram();
		glAttachShader(this.id, this.vertShader);
		glAttachShader(this.id, this.fragShader);
		
		this.compiled = true;
	}
	
	public void link() {
		glLinkProgram(this.id);
		
		int linkStatus = glGetProgrami(this.id, GL_LINK_STATUS);
		
		if(linkStatus == GL_FALSE) {
			System.err.println("Program link error:");
			System.err.println(glGetProgramInfoLog(this.id));
		}
		
		glDetachShader(this.id, this.vertShader);
		glDetachShader(this.id, this.fragShader);
		
		glDeleteShader(this.vertShader);
		glDeleteShader(this.fragShader);
	}
	
	public boolean isDefined(String def) {
		return this.defines.contains(def);
	}
	
	public void define(String def) {
		if(!this.compiled && !this.isDefined(def)) this.defines.add(def);
	}
	
	public void loadUniforms(String... uniforms) {
		for(int i = 0, l = uniforms.length; i < l; i++)
			this.loadUniform(uniforms[i]);
	}
	
	public void loadUniform(String name) {
		this.loadUniform(name, name);
	}
	
	public void loadUniform(String shaderName, String name) {
		int loc = glGetUniformLocation(this.id, shaderName);
		
		if(loc == -1) System.out.println("Warning: Unfound or unused uniform '" + shaderName + "' in program '" + this.name + "'");
		
		this.uniforms.put(name, loc);
	}
	
	public int getUniformLoc(String name) {
		return this.uniforms.get(name);
	}
	
	public void loadToUniform(String name, boolean value) {
		this.loadToUniform(name, value ? 1 : 0);
	}
	
	public void loadToUniform(String name, int value) {
		int uniform = this.uniforms.getOrDefault(name, -1);
		
		if(uniform != -1) glUniform1i(uniform, value);
	}
	
	public void loadToUniform(String name, int a, int b) {
		int uniform = this.uniforms.getOrDefault(name, -1);
		
		if(uniform != -1) glUniform2i(uniform, a, b);
	}
	
	public void loadToUniform(String name, int a, int b, int c) {
		int uniform = this.uniforms.getOrDefault(name, -1);
		
		if(uniform != -1) glUniform3i(uniform, a, b, c);
	}
	
	public void loadToUniform(String name, int a, int b, int c, int d) {
		int uniform = this.uniforms.getOrDefault(name, -1);
		
		if(uniform != -1) glUniform4i(uniform, a, b, c, d);
	}
	
	public void loadToUniform(String name, float value) {
		int uniform = this.uniforms.getOrDefault(name, -1);
		
		if(uniform != -1) glUniform1f(uniform, value);
	}
	
	public void loadToUniform(String name, float a, float b) {
		int uniform = this.uniforms.getOrDefault(name, -1);
		
		if(uniform != -1) glUniform2f(uniform, a, b);
	}
	
	public void loadToUniform(String name, float a, float b, float c) {
		int uniform = this.uniforms.getOrDefault(name, -1);
		
		if(uniform != -1) glUniform3f(uniform, a, b, c);
	}
	
	public void loadToUniform(String name, float a, float b, float c, float d) {
		int uniform = this.uniforms.getOrDefault(name, -1);
		
		if(uniform != -1) glUniform4f(uniform, a, b, c, d);
	}
	
	public void loadToUniform(String name, Vector2ic v) {
		this.loadToUniform(name, v.x(), v.y());
	}
	
	public void loadToUniform(String name, Vector3ic v) {
		this.loadToUniform(name, v.x(), v.y(), v.z());
	}
	
	public void loadToUniform(String name, Vector4ic v) {
		this.loadToUniform(name, v.x(), v.y(), v.z(), v.w());
	}
	
	public void loadToUniform(String name, Vector2fc v) {
		this.loadToUniform(name, v.x(), v.y());
	}
	
	public void loadToUniform(String name, Vector3fc v) {
		this.loadToUniform(name, v.x(), v.y(), v.z());
	}
	
	public void loadToUniform(String name, Vector4fc v) {
		this.loadToUniform(name, v.x(), v.y(), v.z(), v.w());
	}
	
	public void loadToUniform(String name, Matrix3fc mat3) {
		int uniform = this.uniforms.getOrDefault(name, -1);
		
		if(uniform != -1) {
			glUniformMatrix3fv(uniform, false, mat3.get(this._floatBuffer9));
		}
	}
	
	public void loadToUniform(String name, Matrix4fc mat4) {
		int uniform = this.uniforms.getOrDefault(name, -1);
		
		if(uniform != -1) {
			glUniformMatrix4fv(uniform, false, mat4.get(this._floatBuffer16));
		}
	}
	
	public void loadToUniform(String name, OGLTexture2D tex, int textureUnit) {
		if(tex == null || tex.isNull() || textureUnit < 0 || textureUnit >= OGLManager.get().maxFragmentTextureImageUnits) return;
		
		int uniform = this.uniforms.getOrDefault(name, -1);
		
		if(uniform != -1) {
			glUniform1i(uniform, textureUnit);
			
			glActiveTexture(GL_TEXTURE0 + textureUnit);
			tex.bind();
		}
	}
	
	public boolean hasUniform(String name) {
		int uniform = this.uniforms.getOrDefault(name, -1);
		
		return uniform != -1;
	}
	
	public void dispose() {
		if(glIsProgram(this.id)) {
			glDeleteProgram(this.id);
			this.id = 0;
		}
	}
	
	public void use() {
		if(OGLProgram.usedProgram != this.id) {
			glUseProgram(this.id);
			OGLProgram.usedProgram = this.id;
		}
	}
	
	public void unuse() {
		OGLProgram.unuseAll();
	}
	
	public static void unuseAll() {
		glUseProgram(0);
		OGLProgram.usedProgram = 0;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getVersion() {
		return this.version;
	}
	
	public void setVersion(int version) {
		this.version = version;
	}
	
	public String getProfile() {
		return this.profile;
	}
	
	public void setProfile(String profile) {
		this.profile = profile;
	}
}
