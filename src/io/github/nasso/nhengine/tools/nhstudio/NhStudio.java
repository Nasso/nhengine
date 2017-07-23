package io.github.nasso.nhengine.tools.nhstudio;

import io.github.nasso.nhengine.core.Game;
import io.github.nasso.nhengine.core.GameRunner;
import io.github.nasso.nhengine.core.LaunchSettings;
import io.github.nasso.nhengine.core.LaunchSettings.VideoMode;

public class NhStudio implements GameRunner {
	public static final int DEFAULT_WIN_WIDTH = 1280;
	public static final int DEFAULT_WIN_HEIGHT = 720;
	
	private Game g;
	
	public NhStudio() {
		this.g = Game.instance();
		
		this.g.init(this, new LaunchSettings().windowTitle("Nhengine Studio").videoMode(VideoMode.WINDOWED).videoSize(DEFAULT_WIN_WIDTH, DEFAULT_WIN_HEIGHT).maxFPS(60));
		this.g.start();
	}
	
	public static void main(String[] argv) {
		new NhStudio();
	}
	
	public void init() {
		this.g.loadLevel(new NhStudioLevel(DEFAULT_WIN_WIDTH, DEFAULT_WIN_HEIGHT));
	}
	
	public void update(float delta) {
	}
	
	public void dispose() {
	}
}
