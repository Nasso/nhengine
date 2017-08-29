package io.github.nasso.nhengine.core;

import java.io.IOException;
import java.util.List;

import com.codedisaster.steamworks.SteamAPI;

import io.github.nasso.nhengine.audio.AudioPlayer;
import io.github.nasso.nhengine.audio.openal.OALManager;
import io.github.nasso.nhengine.audio.openal.OALPlayer;
import io.github.nasso.nhengine.graphics.Renderer;
import io.github.nasso.nhengine.graphics.opengl.OGLManager;
import io.github.nasso.nhengine.graphics.opengl.OGLRenderer;
import io.github.nasso.nhengine.level.Level;
import io.github.nasso.nhengine.level.Scene;
import io.github.nasso.nhengine.utils.TimeManager;

public class Game {
	private static Game instance;
	
	public static Game instance() {
		return (Game.instance == null ? Game.instance = new Game() : Game.instance);
	}
	
	private GameWindow window;
	private GameRunner runner;
	
	private InputManager inputManager = new InputManager();
	private OGLRenderer renderer;
	private OALPlayer audioPlayer;
	private Level currentLevel;
	
	private float frameTime = 0;
	private float fps = 0;
	private int maxFPS = 0;
	
	private boolean useSteam = false;
	private boolean shouldQuit = false;
	
	private Game() {
	}
	
	public void init(GameRunner runner, LaunchSettings settings) {
		GLFWManager.init();
		this.window = new GameWindow(settings);
		this.window.makeContextCurrent();
		OALManager.init(settings);
		
		try {
			OGLManager.init(settings);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		this.maxFPS = settings.getMaxFPS();
		
		if(!settings.isVSYNC()) this.window.swapInterval(0);
		
		this.renderer = OGLManager.get().renderer;
		this.audioPlayer = OALManager.get().audioPlayer;
		this.runner = runner;
		
		if(settings.isSteamClient()) {
			// Init steam
			try {
				if(!SteamAPI.init()) {
					System.err.println("No steam logon");
				} else {
					this.useSteam = true;
				}
			} catch(Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void loadLevel(Level lvl) {
		if(this.currentLevel != null) this.currentLevel.dispose();
		
		this.currentLevel = lvl;
	}
	
	public GameWindow window() {
		return this.window;
	}
	
	public Renderer getRenderer() {
		return this.renderer;
	}
	
	public AudioPlayer getAudioPlayer() {
		return this.audioPlayer;
	}
	
	private void terminate() {
		this.window.dispose();
		
		this.loadLevel(null);
		
		OGLManager.dispose();
		OALManager.dispose();
		
		if(this.useSteam) {
			SteamAPI.shutdown();
		}
	}
	
	public void quit() {
		this.shouldQuit = true;
	}
	
	public void start() {
		try {
			this.runner.init();
			
			this.window.show();
			
			float startTime = TimeManager.getTimeMS();
			float lastDeltaUpdateTime = startTime;
			float endTime = 0.0f;
			float delta = 0.0f;
			
			while(!this.window.shouldClose() && !this.shouldQuit) {
				startTime = TimeManager.getTimeMS();
				delta = startTime - lastDeltaUpdateTime;
				lastDeltaUpdateTime = startTime;
				
				if(this.useSteam) {
					if(SteamAPI.isSteamRunning()) {
						SteamAPI.runCallbacks();
					}
				}
				
				this.window.pollEvents();
				
				TimeManager.step(delta);
				this.runner.update(delta);
				
				if(this.currentLevel != null) {
					this.renderer.updateSize(this.window.getFrameWidth(), this.window.getFrameHeight());
					
					this.currentLevel.update(delta);
					
					List<Scene> sceneList = this.currentLevel.getOverlayScenes();
					for(int i = 0; i < sceneList.size(); i++) {
						Scene s = sceneList.get(i);
						if(s.getRoot().isEnabled()) s.update(delta);
					}
					
					this.inputManager.processInput(this.currentLevel);
					if(!this.window.isMinified()) this.renderer.render(this.currentLevel);
				}
				
				this.window.swapBuffers();
				
				endTime = TimeManager.getTimeMS();
				
				// FPS cap
				this.frameTime = endTime - startTime;
				if(this.maxFPS > 0 && (1000.0f / this.maxFPS) > this.frameTime) {
					try {
						Thread.sleep((long) (1000.0f / this.maxFPS - endTime + startTime));
					} catch(InterruptedException e) {
						e.printStackTrace();
					}
					
					this.frameTime = TimeManager.getTimeMS() - startTime;
				}
				
				// FPS
				this.fps = 1000.0f / this.frameTime;
			}
			
			this.runner.dispose();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		this.terminate();
	}
	
	public float getFPS() {
		return this.fps;
	}
	
	public float getFrameTime() {
		return this.frameTime;
	}
	
	public int getMaxFPS() {
		return this.maxFPS;
	}
	
	public void setMaxFPS(int maxFPS) {
		this.maxFPS = maxFPS;
	}
}
