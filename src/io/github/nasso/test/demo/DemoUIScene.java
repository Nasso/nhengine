package io.github.nasso.test.demo;

import io.github.nasso.nhengine.core.Game;
import io.github.nasso.nhengine.core.GameWindow;
import io.github.nasso.nhengine.core.Nhengine;
import io.github.nasso.nhengine.level.Scene;
import io.github.nasso.nhengine.ui.UICanvas;
import io.github.nasso.nhengine.ui.UIRootPane;
import io.github.nasso.nhengine.ui.theme.UIDefaultDarkTheme;

public class DemoUIScene extends Scene {
	private UICanvas cvs;
	
	private DemoPerfDialog perfDialog;
	
	public DemoUIScene() {
		this.cvs = new UICanvas(new UIDefaultDarkTheme(false), DemoMain.GAME_WIDTH, DemoMain.GAME_HEIGHT);
		
		this.getCamera().setViewport2D(DemoMain.GAME_WIDTH, DemoMain.GAME_HEIGHT);
		this.setRoot(this.cvs);
		
		UIRootPane rootPane = this.cvs.getRootPane();
		rootPane.getContentPane().setOpaque(false);
		
		this.perfDialog = new DemoPerfDialog();
	}
	
	public void update(float delta) {
		GameWindow win = Game.instance().window();
		
		if(win.isPressed(Nhengine.KEY_F3)) {
			this.perfDialog.open(this.cvs.getRootPane());
		}
		
		if(this.perfDialog.isShown()) {
			this.perfDialog.update(delta, Game.instance().getFPS());
		}
		
		this.cvs.update();
	}
}
