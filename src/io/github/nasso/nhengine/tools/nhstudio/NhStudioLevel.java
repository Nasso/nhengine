package io.github.nasso.nhengine.tools.nhstudio;

import org.joml.Vector2f;

import io.github.nasso.nhengine.core.Game;
import io.github.nasso.nhengine.core.GameWindow;
import io.github.nasso.nhengine.level.Level;
import io.github.nasso.nhengine.level.Scene;
import io.github.nasso.nhengine.ui.UICanvas;

public class NhStudioLevel extends Level {
	private UICanvas cvs;
	private Scene sce;
	
	private Vector2f frameSize = new Vector2f();
	private NhStudioApp app = new NhStudioApp();
	
	public NhStudioLevel(float w, float h) {
		this.cvs = new UICanvas(w, h);
		this.app.start(this.cvs);
		
		this.sce = new Scene("nhstudio-main-scene");
		this.sce.setRoot(this.cvs);
		
		this.addOverlayScene(this.sce);
	}
	
	public void update(float delta) {
		GameWindow win = Game.instance().window();
		
		if(win.getFrameWidth() != this.frameSize.x || win.getFrameHeight() != this.frameSize.y) {
			this.frameSize.set(win.getFrameWidth(), win.getFrameHeight());
			
			this.sce.getCamera().setViewport2D(this.frameSize.x, this.frameSize.y);
			this.cvs.setSize((int) this.frameSize.x, (int) this.frameSize.y);
		}
		
		this.cvs.update();
	}
}
