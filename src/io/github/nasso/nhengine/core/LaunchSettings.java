package io.github.nasso.nhengine.core;

public class LaunchSettings {
	public enum VideoMode {
		FULLSCREEN, WINDOWED, MAXIMIZED
	}
	
	private VideoMode videoMode = VideoMode.WINDOWED;
	private int videoWidth = 800, videoHeight = 600;
	private int windowMonitor = 0;
	private int maxFPS = 0;
	private String windowTitle = "A Nhengine Game";
	private boolean resizable = true;
	private boolean vsync = false;
	private boolean steamClient = false;
	
	public LaunchSettings() {
	}
	
	/**
	 * Default to FULLSCREEN
	 * 
	 * @param v
	 * @return
	 */
	public LaunchSettings videoMode(VideoMode v) {
		this.videoMode = v;
		return this;
	}
	
	/**
	 * Default to 800
	 * 
	 * @param v
	 * @return
	 */
	public LaunchSettings videoWidth(int v) {
		this.videoWidth = v;
		return this;
	}
	
	/**
	 * Default to 600
	 * 
	 * @param v
	 * @return
	 */
	public LaunchSettings videoHeight(int v) {
		this.videoHeight = v;
		return this;
	}
	
	/**
	 * Default to 800x600
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public LaunchSettings videoSize(int x, int y) {
		return this.videoWidth(x).videoHeight(y);
	}
	
	/**
	 * Default to false
	 * 
	 * @param value
	 * @return
	 */
	public LaunchSettings vsync(boolean value) {
		this.vsync = value;
		return this;
	}
	
	/**
	 * Default to "A Nhengine Game"
	 * 
	 * @param title
	 * @return
	 */
	public LaunchSettings windowTitle(String title) {
		this.windowTitle = title;
		return this;
	}
	
	/**
	 * Default to 0
	 * 
	 * @param mon
	 * @return
	 */
	public LaunchSettings windowMonitor(int mon) {
		this.windowMonitor = mon;
		return this;
	}
	
	/**
	 * Default to 0 (unlimited)
	 * 
	 * @param fps
	 * @return
	 */
	public LaunchSettings maxFPS(int fps) {
		this.maxFPS = fps;
		return this;
	}
	
	/**
	 * Default to true
	 * 
	 * @param value
	 * @return
	 */
	public LaunchSettings resizable(boolean value) {
		this.resizable = value;
		return this;
	}
	
	/**
	 * Default to false
	 * 
	 * @param value
	 * @return
	 */
	public LaunchSettings steamClient(boolean value) {
		this.steamClient = value;
		return this;
	}
	
	public boolean isSteamClient() {
		return this.steamClient;
	}
	
	public VideoMode getVideoMode() {
		return this.videoMode;
	}
	
	public int getVideoWidth() {
		return this.videoWidth;
	}
	
	public int getVideoHeight() {
		return this.videoHeight;
	}
	
	public boolean isVSYNC() {
		return this.vsync;
	}
	
	public String getWindowTitle() {
		return this.windowTitle;
	}
	
	public int getWindowMonitor() {
		return this.windowMonitor;
	}
	
	public int getMaxFPS() {
		return this.maxFPS;
	}
	
	public boolean isResizable() {
		return this.resizable;
	}
}
