package io.github.nasso.nhengine.core;

import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public class GLFWManager {
	private static boolean initialized = false;
	private static PointerBuffer monitorsBuffer = null;
	
	public static int getMonitorCount() {
		return GLFWManager.monitorsBuffer.limit();
	}
	
	public static long getMonitor(int index) {
		return GLFWManager.monitorsBuffer.get(index);
	}
	
	public static boolean isInitialized() {
		return GLFWManager.initialized;
	}
	
	public static void init() {
		GLFWManager.initialized = GLFW.glfwInit();
		
		if(GLFWManager.initialized) {
			GLFWErrorCallback.createPrint(System.err).set();
			
			GLFWManager.monitorsBuffer = GLFW.glfwGetMonitors();
		} else throw new RuntimeException("Couldn't init GLFW");
	}
	
	public static void release() {
		GLFW.glfwTerminate();
		
		GLFWManager.initialized = false;
	}
}
