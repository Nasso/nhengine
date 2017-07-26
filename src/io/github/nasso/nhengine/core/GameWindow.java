package io.github.nasso.nhengine.core;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryUtil;

import io.github.nasso.nhengine.core.LaunchSettings.VideoMode;

public class GameWindow {
	private long window_id;
	private long monitor;
	
	private int[] itemp_a = new int[1];
	private int[] itemp_b = new int[1];
	
	private DoubleBuffer dbtemp_a = null;
	private DoubleBuffer dbtemp_b = null;
	
	private Cursor currentCursor, setCursor;
	
	private List<Integer> keyInputs = new ArrayList<Integer>();
	private List<Integer> textInputs = new ArrayList<Integer>();
	private List<Integer> mouseInputs = new ArrayList<Integer>();
	
	private Set<Integer> pressedInputs = new HashSet<>();
	private Set<Integer> releasedInputs = new HashSet<>();
	
	private Set<Integer> pressedMouseInputs = new HashSet<>();
	private Set<Integer> releasedMouseInputs = new HashSet<>();
	
	private float mousex = -1;
	private float mousey = -1;
	private float mouserelx = 0;
	private float mouserely = 0;
	private float scrollrelx = 0;
	private float scrollrely = 0;
	
	private int width;
	private int height;
	private int frameWidth;
	private int frameHeight;
	private boolean fullscreen;
	private String windowTitle;
	
	private boolean vsync = false;
	
	private int cursorMode = Nhengine.CURSOR_NORMAL;
	
	GameWindow(LaunchSettings settings) {
		int monIndex = settings.getWindowMonitor();
		String title = settings.getWindowTitle();
		boolean resizable = settings.isResizable();
		VideoMode mode = settings.getVideoMode();
		
		this.width = settings.getVideoWidth();
		this.height = settings.getVideoHeight();
		
		this.frameWidth = this.width;
		this.frameHeight = this.height;
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		if(!resizable) glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		
		this.monitor = GLFWManager.getMonitor(monIndex);
		this.window_id = glfwCreateWindow(this.width, this.height, title, mode == VideoMode.FULLSCREEN ? this.monitor : MemoryUtil.NULL, MemoryUtil.NULL);
		if(this.window_id == MemoryUtil.NULL) throw new IllegalStateException("Couldn't create the window");
		
		if(mode == VideoMode.FULLSCREEN) this.fullscreen = true;
		else this.fullscreen = false;
		
		if(mode == VideoMode.MAXIMIZED) glfwMaximizeWindow(this.window_id);
		
		// Creates buffers
		this.dbtemp_a = BufferUtils.createDoubleBuffer(1);
		this.dbtemp_b = BufferUtils.createDoubleBuffer(1);
		
		glfwSetCharCallback(this.window_id, (window, codepoint) -> {
			this.textInputs.add(codepoint);
		});
		
		glfwSetKeyCallback(this.window_id, (window, key, scancode, action, mods) -> {
			this.keyInputs.add(action);
			this.keyInputs.add(key);
			
			if(action == GLFW_PRESS) {
				this.pressedInputs.add(key);
			} else if(action == GLFW_RELEASE) {
				this.releasedInputs.add(key);
			}
		});
		
		glfwSetMouseButtonCallback(this.window_id, (window, button, action, mods) -> {
			this.mouseInputs.add(action);
			this.mouseInputs.add((int) this.mousex);
			this.mouseInputs.add((int) this.mousey);
			this.mouseInputs.add(button);
			
			if(action == GLFW_PRESS) {
				this.pressedMouseInputs.add(button);
			} else if(action == GLFW_RELEASE) {
				this.releasedMouseInputs.add(button);
			}
		});
		
		glfwSetScrollCallback(this.window_id, (window, xoffset, yoffset) -> {
			this.scrollrelx += xoffset;
			this.scrollrely += yoffset;
		});
		
		glfwSetWindowSizeCallback(this.window_id, (window, width, height) -> {
			this.width = width;
			this.height = height;
		});
		
		glfwSetFramebufferSizeCallback(this.window_id, (window, width, height) -> {
			this.frameWidth = width;
			this.frameHeight = height;
		});
	}
	
	public void show() {
		glfwShowWindow(this.window_id);
		
		// Refresh framebuffer size
		glfwGetFramebufferSize(this.window_id, this.itemp_a, this.itemp_b);
		this.frameWidth = this.itemp_a[0];
		this.frameHeight = this.itemp_b[0];
	}
	
	public void hide() {
		glfwHideWindow(this.window_id);
	}
	
	// Package visibility
	void dispose() {
		Callbacks.glfwFreeCallbacks(this.window_id);
		glfwDestroyWindow(this.window_id);
	}
	
	void makeContextCurrent() {
		glfwMakeContextCurrent(this.window_id);
	}
	
	void contextVersion(int api, int major, int minor) {
		glfwWindowHint(GLFW_CLIENT_API, api);
		// glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_FALSE);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, major);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, minor);
		glfwWindowHint(GLFW_DEPTH_BITS, 24);
		glfwWindowHint(GLFW_STENCIL_BITS, 8);
	}
	
	List<Integer> getLastTextInputs() {
		return this.textInputs;
	}
	
	List<Integer> getLastKeyInputs() {
		return this.keyInputs;
	}
	
	List<Integer> getLastMouseInputs() {
		return this.mouseInputs;
	}
	
	void swapInterval(int value) {
		glfwSwapInterval(value);
	}
	
	void swapBuffers() {
		glfwSwapBuffers(this.window_id);
	}
	
	void pollEvents() {
		if(this.setCursor != this.currentCursor) {
			glfwSetCursor(this.window_id, this.setCursor == null ? 0 : this.setCursor.id);
			this.currentCursor = this.setCursor;
		}
		
		this.mouseInputs.clear();
		this.keyInputs.clear();
		this.textInputs.clear();
		
		this.pressedInputs.clear();
		this.releasedInputs.clear();
		
		this.pressedMouseInputs.clear();
		this.releasedMouseInputs.clear();
		
		this.mouserelx = 0;
		this.mouserely = 0;
		
		this.scrollrelx = 0;
		this.scrollrely = 0;
		
		glfwGetCursorPos(this.window_id, this.dbtemp_a, this.dbtemp_b);
		float newx = (float) this.dbtemp_a.get(0);
		float newy = (float) this.dbtemp_b.get(0);
		
		if(this.mousex != -1) this.mouserelx = newx - this.mousex;
		if(this.mousey != -1) this.mouserely = newy - this.mousey;
		
		this.mousex = newx;
		this.mousey = newy;
		
		glfwPollEvents();
	}
	
	public void maximize() {
		glfwMaximizeWindow(this.window_id);
	}
	
	public String getClipboard() {
		return glfwGetClipboardString(this.window_id);
	}
	
	public void setClipboard(CharSequence string) {
		glfwSetClipboardString(this.window_id, string);
	}
	
	/**
	 * Set the current cursor. Note that it doesn't actually apply the changes, instead it keeps a reference of <code>curs</code> and really sets it on the next loop,
	 * 
	 * so you're free to call this method as much as you want without real performances issues.
	 * 
	 * @param curs
	 */
	public void setCursor(Cursor curs) {
		this.setCursor = curs;
	}
	
	public Cursor getCursor() {
		return this.setCursor;
	}
	
	public void setCursorMode(int mode) {
		glfwSetInputMode(this.window_id, GLFW_CURSOR, mode);
		this.cursorMode = mode;
	}
	
	public int getCursorMode() {
		return this.cursorMode;
	}
	
	public boolean isMouseButtonPressed(int btn) {
		return this.pressedMouseInputs.contains(btn);
	}
	
	public boolean isMouseButtonReleased(int btn) {
		return this.releasedMouseInputs.contains(btn);
	}
	
	public boolean isKeyPressed(int key) {
		return this.pressedInputs.contains(key);
	}
	
	public boolean isKeyReleased(int key) {
		return this.releasedInputs.contains(key);
	}
	
	public boolean isKeyDown(int key) {
		return glfwGetKey(this.window_id, key) == GLFW_PRESS;
	}
	
	public boolean isButtonDown(int btn) {
		return glfwGetMouseButton(this.window_id, btn) == GLFW_PRESS;
	}
	
	public boolean shouldClose() {
		return glfwWindowShouldClose(this.window_id);
	}
	
	public void setShouldClose(boolean v) {
		glfwSetWindowShouldClose(this.window_id, v);
	}
	
	public void enableVSYNC() {
		this.swapInterval(1);
	}
	
	public void disableVSYNC() {
		this.swapInterval(0);
	}
	
	public void setVSYNC(boolean value) {
		this.vsync = value;
		
		if(value) this.enableVSYNC();
		else this.disableVSYNC();
	}
	
	public boolean isVSYNC() {
		return this.vsync;
	}
	
	public boolean isVisible() {
		return glfwGetWindowAttrib(this.window_id, GLFW_VISIBLE) == GLFW_VISIBLE;
	}
	
	public String getWindowTitle() {
		return this.windowTitle;
	}
	
	public void setWindowTitle(String value) {
		this.windowTitle = value;
		glfwSetWindowTitle(this.window_id, this.windowTitle);
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public void setWidth(int width) {
		this.width = width;
		glfwSetWindowSize(this.window_id, width, this.height);
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void setHeight(int height) {
		this.height = height;
		glfwSetWindowSize(this.window_id, this.width, height);
	}
	
	public int getFrameWidth() {
		return this.frameWidth;
	}
	
	public int getFrameHeight() {
		return this.frameHeight;
	}
	
	public float getDevicePixelRatio() {
		return (float) this.frameWidth / this.width;
	}
	
	public boolean isFullscreen() {
		return this.fullscreen;
	}
	
	public void setFullscreen() {
		this.fullscreen = true;
		glfwSetWindowMonitor(this.window_id, this.monitor, 0, 0, this.width, this.height, GLFW_DONT_CARE);
	}
	
	public void setFullscreen(boolean value) {
		if(value != this.isFullscreen()) if(value) this.setFullscreen();
		else this.setWindowed();
	}
	
	public void setWindowed() {
		this.fullscreen = false;
		
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowMonitor(this.window_id, MemoryUtil.NULL, (vidmode.width() - this.width) / 2, (vidmode.height() - this.height) / 2, this.width, this.height, GLFW_DONT_CARE);
	}
	
	public float getMouseX() {
		return this.mousex;
	}
	
	public float getMouseY() {
		return this.mousey;
	}
	
	public float getMouseRelX() {
		return this.mouserelx;
	}
	
	public float getMouseRelY() {
		return this.mouserely;
	}
	
	public float getScrollRelX() {
		return this.scrollrelx;
	}
	
	public float getScrollRelY() {
		return this.scrollrely;
	}
	
	public void toggleFullscreen() {
		this.setFullscreen(!this.isFullscreen());
	}
	
	public boolean isMinified() {
		return glfwGetWindowAttrib(this.window_id, GLFW_ICONIFIED) == GLFW_TRUE;
	}
}
