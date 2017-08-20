package io.github.nasso.test.tilediso;

import io.github.nasso.nhengine.core.Game;
import io.github.nasso.nhengine.core.GameRunner;
import io.github.nasso.nhengine.core.LaunchSettings;
import io.github.nasso.nhengine.core.LaunchSettings.VideoMode;
import io.github.nasso.nhengine.core.Nhengine;

// FIXME: isometric machin broke
public class TiledIsoMain implements GameRunner {
	public static final int GAME_WIDTH = 640;
	public static final int GAME_HEIGHT = 480;
	
	public static final int GAME_KEY_ACT = Nhengine.KEY_ENTER;
	public static final int GAME_KEY_RETURN = Nhengine.KEY_RIGHT_SHIFT;
	public static final int GAME_KEY_MENU = Nhengine.KEY_RIGHT_CONTROL;
	public static final int GAME_KEY_UP = Nhengine.KEY_UP;
	public static final int GAME_KEY_RIGHT = Nhengine.KEY_RIGHT;
	public static final int GAME_KEY_DOWN = Nhengine.KEY_DOWN;
	public static final int GAME_KEY_LEFT = Nhengine.KEY_LEFT;
	
	public static void main(String[] args) {
		new TiledIsoMain();
	}
	
	private Game game;
	
	private TiledIsoLevel lvl;
	
	public TiledIsoMain() {
		this.game = Game.instance();
		
		this.game.init(this, new LaunchSettings().windowTitle("Tiled test").videoWidth(GAME_WIDTH).videoHeight(GAME_HEIGHT).maxFPS(60).videoMode(VideoMode.WINDOWED));
		
		this.game.start();
	}
	
	public void init() {
		
		this.lvl = new TiledIsoLevel();
		
		this.game.loadLevel(this.lvl);
	}
	
	public void update(float delta) {
		
	}
	
	public void dispose() {
		this.lvl.dispose();
	}
}
