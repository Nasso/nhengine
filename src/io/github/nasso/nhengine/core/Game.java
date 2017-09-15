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

/**
 * This class represents the game/application. There can only be 1 instance, retrievable with {@link Game#instance()}.
 * @author nasso
 */
public class Game {
	private static Game instance;
	
	/** 
	 * Returns the global instance of this game. Creates it if it hasn't yet.
	 * 
	 * @return The global instance. 
	 */
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
	
	private boolean updateLevelBefore = false;
	private boolean useSteam = false;
	private boolean shouldQuit = false;
	
	private Game() {
	}
	
	/**
	 * Initializes the game with the given {@link GameRunner runner} and {@link LaunchSettings settings}.
	 * 
	 * @param runner The game runner.
	 * @param settings The launch settings.
	 */
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
	
	/**
	 * Loads the given level. Any previously loaded level will be unloaded (disposed).
	 * 
	 * @param lvl The level to load.
	 */
	public void loadLevel(Level lvl) {
		if(this.currentLevel != null) this.currentLevel.dispose();
		
		this.currentLevel = lvl;
	}
	
	/**
	 * @return The game window.
	 */
	public GameWindow window() {
		return this.window;
	}
	
	/**
	 * @return The game renderer.
	 */
	public Renderer getRenderer() {
		return this.renderer;
	}
	
	/**
	 * @return The game audio player.
	 */
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
	
	/**
	 * Quits the game. Note that this method doesn't immediatly close the game, but rather sets a "shouldQuit" flag to true, that will in fact quit the game on the next frame.
	 * It means that the current frame will terminate before the game stops.
	 */
	public void quit() {
		this.shouldQuit = true;
	}
	
	/**
	 * Starts the game. This method doesn't return until the end of the game. You generally want to call this last when creating the game instance.
	 */
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
				if(!this.updateLevelBefore && this.currentLevel != null) this.runner.update(delta);
				
				if(this.currentLevel != null) {
					this.renderer.updateSize(this.window.getFrameWidth(), this.window.getFrameHeight());
					
					this.currentLevel.step(delta);
					if(this.updateLevelBefore) this.runner.update(delta);
					
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
	
	/**
	 * @return The current number of Frames Per Seconds.
	 */
	public float getFPS() {
		return this.fps;
	}
	
	/**
	 * @return The duration of the last frame, in milliseconds.
	 */
	public float getFrameTime() {
		return this.frameTime;
	}
	
	/**
	 * @return The current FPS limit, or 0 for unlimited.
	 */
	public int getMaxFPS() {
		return this.maxFPS;
	}
	
	/**
	 * Sets the frame-rate limit.<br>
	 * <ul>
	 * 	<li>
	 * 		If the value is a strictly positive integer, then it'll be used as the maximum number of frames per second and the frame time will be limited accordingly.
	 * 		Note that it doesn't guarantee that the game will run at the given frame-rate
	 * 	</li>
	 * 	<li>
	 * 		If the value is <code>0</code>, then the frame-rate will be unlimited and the game will run as fast as possible.
	 * 	</li>
	 * </ul> 
	 * 
	 * @param maxFPS The FPS limit.
	 */
	public void setMaxFPS(int maxFPS) {
		this.maxFPS = maxFPS < 0 ? 0 : maxFPS;
	}

	/**
	 * @return True if the currently loaded {@link Level level} {@link Level#update(float) update(float)} method should called before calling this game's runner {@link GameRunner#update(float) update(float)} method.
	 */
	public boolean doesUpdateLevelBefore() {
		return this.updateLevelBefore;
	}

	/**
	 * Sets which of the {@link GameRunner game runner} or {@link Level level} should be updated first (call to the <code>update(float)</code> method).
	 * 
	 * @param updateLevelBefore True to update the level before the game runner, false otherwise.
	 */
	public void setUpdateLevelBefore(boolean updateLevelBefore) {
		this.updateLevelBefore = updateLevelBefore;
	}
}
