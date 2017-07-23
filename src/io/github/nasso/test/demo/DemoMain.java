package io.github.nasso.test.demo;

import java.io.IOException;

import io.github.nasso.nhengine.core.Game;
import io.github.nasso.nhengine.core.GameRunner;
import io.github.nasso.nhengine.core.LaunchSettings;
import io.github.nasso.nhengine.core.Nhengine;
import io.github.nasso.nhengine.core.LaunchSettings.VideoMode;

public class DemoMain implements GameRunner {
	public static final int GAME_WIDTH = 640;
	public static final int GAME_HEIGHT = 480;
	
	public static final int GAME_KEY_ACT = Nhengine.KEY_ENTER;
	public static final int GAME_KEY_RETURN = Nhengine.KEY_RIGHT_SHIFT;
	public static final int GAME_KEY_MENU = Nhengine.KEY_RIGHT_CONTROL;
	public static final int GAME_KEY_UP = Nhengine.KEY_UP;
	public static final int GAME_KEY_RIGHT = Nhengine.KEY_RIGHT;
	public static final int GAME_KEY_DOWN = Nhengine.KEY_DOWN;
	public static final int GAME_KEY_LEFT = Nhengine.KEY_LEFT;
	
	// Main
	public static void main(String[] args) {
		new DemoMain();
	}
	
	// Instance du jeu
	private Game game;
	
	// Instance du niveau pour la démo
	private DemoLevel lvl;
	
	public DemoMain() {
		// Créé une instance d'un jeu. Rien n'est fait ici pour l'instant.
		this.game = Game.instance();
		
		// Initialisation du jeu. La classe LaunchSettings est utilisée pour spécifier les différents paramêtres tel que le titre de la fenêtre, sa taille initiale, son mode initial...
		this.game.init(this, new LaunchSettings().windowTitle("Nhengine test").videoWidth(GAME_WIDTH).videoHeight(GAME_HEIGHT).resizable(false).videoMode(VideoMode.WINDOWED));
		
		// Pour limiter les FPS, setMaxFPS(int fps) dans les launch settings ou bien la méthode suivante:
		this.game.setMaxFPS(30);
		
		// C'est parti! Rendez vous méthode "init"!
		this.game.start();
	}
	
	public void init() {
		try {
			DemoAssets.initAll();
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		// On créé le niveau
		this.lvl = new DemoLevel();
		
		// Plus qu'à le charger!
		this.game.loadLevel(this.lvl);
		
		// Et hop! Le jeu est lancé lorsque cette méthode ce termine! Maintenant, update() sera appelé a chaque tour de boucle!
	}
	
	public void update(float delta) {
		
	}
	
	public void dispose() {
		this.lvl.dispose();
		
		DemoAssets.disposeAll();
	}
}
