package io.github.nasso.test.ui;

import io.github.nasso.nhengine.core.Game;
import io.github.nasso.nhengine.core.GameRunner;
import io.github.nasso.nhengine.core.LaunchSettings;

public class TestUIMain implements GameRunner {
	private static final int WINDOW_WIDTH = 800;
	private static final int WINDOW_HEIGHT = 600;
	
	private TestUILevel mainMenu;
	
	private Game game;
	
	public TestUIMain() {
		this.game = Game.instance();
		
		this.game.init(this, new LaunchSettings().windowTitle("Nhengine UI").maxFPS(60).resizable(true).videoSize(WINDOW_WIDTH, WINDOW_HEIGHT));
		
		this.game.start();
	}
	
	public void init() {
		this.mainMenu = new TestUILevel(WINDOW_WIDTH, WINDOW_HEIGHT);
		
		// Load main menu
		this.game.loadLevel(this.mainMenu);
	}
	
	public void update(float delta) {
	}
	
	public void dispose() {
	}
	
	public static void main(String[] argv) {
		new TestUIMain();
	}
}
